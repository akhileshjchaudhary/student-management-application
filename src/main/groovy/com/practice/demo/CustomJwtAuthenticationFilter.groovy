package com.practice.demo

import grails.core.GrailsApplication
import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

import javax.crypto.SecretKey
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletRequestWrapper
import javax.servlet.http.HttpServletResponse

class CustomJwtAuthenticationFilter extends OncePerRequestFilter {

    GrailsApplication grailsApplication

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        HttpServletRequest httpRequest = (HttpServletRequest) request


        String token = null
        String uri = request.getRequestURI()
        String uri2 = httpRequest.getRequestURI()
        println("Requested uri: "+uri+" "+uri2)
        def header = request.getHeader('Authorization')
        if (header?.startsWith('Bearer ')) {
            token = header.replace('Bearer ', '')
            println "JWT found in Authorization header"
        }

        if (!token && request.cookies) {
            Cookie jwtCookie = request.cookies.find { it.name == 'token' }
            if (jwtCookie) {
                token = jwtCookie.value
                println "JWT found in Cookie"
            }
        }

        // Wrap the request to add Authorization header if token is found
        def wrappedRequest = httpRequest
        if (token) {
            wrappedRequest = new HttpServletRequestWrapper(httpRequest) {
                @Override
                String getHeader(String name) {
                    if ("Authorization".equalsIgnoreCase(name)) {
                        return "Bearer ${token}"
                    }
                    return super.getHeader(name)
                }

                @Override
                Enumeration<String> getHeaders(String name) {
                    if ("Authorization".equalsIgnoreCase(name)) {
                        return Collections.enumeration(Arrays.asList("Bearer ${token}"))
                    }
                    return super.getHeaders(name)
                }

                @Override
                Enumeration<String> getHeaderNames() {
                    List<String> names = Collections.list(super.getHeaderNames())
                    names.add("Authorization")
                    return Collections.enumeration(names)
                }
            }
        }



        if (token) {
            try {
                String secret = grailsApplication.config.grails.plugin.springsecurity.rest.token.storage.jwt.secret
                if (!secret || secret.trim().isEmpty()) {
                    log.error("JWT secret is not configured properly in application.yml")
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "JWT secret not configured")
                    return
                }

                SecretKey key = Keys.hmacShaKeyFor(secret.bytes)

                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(token)
                        .getBody()

                String username = claims.sub
                def roles = claims.roles ?: []
                def authorities = roles.collect { new SimpleGrantedAuthority(it) }

                def auth = new UsernamePasswordAuthenticationToken(username, null, authorities)
                SecurityContextHolder.context.authentication = auth

                // Set Authorization header in response+++
//                response.setHeader("Authorization", "Bearer ${token}")

            } catch (Exception e) {

                println "JWT error: ${e.message}"
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT")
                return
            }
        }

        filterChain.doFilter(wrappedRequest, response)
    }
}
