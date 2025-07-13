package com.practice.demo

import grails.plugin.springsecurity.annotation.Secured
import grails.converters.JSON
import groovyx.net.http.RESTClient
import groovyx.net.http.ContentType
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder


import javax.servlet.http.Cookie

class AuthController {

    AuthService authService

    @Secured('permitAll')
    def login() {
        def json = request.JSON
        String username = json?.username
        String password = json?.password

        if (!username || !password) {
            response.status = 400
            render([message: 'Username and password are required'] as JSON)
            return
        }

        def result = authService.login(username, password)
        response.status = result.status
        render(result.body as JSON)
    }

    //Frontend

    @Secured('permitAll')
    def loginPage() {
        render(view: 'login')
    }

    @Secured('permitAll')
    def loginFrontend() {
        String username = params.username
        String password = params.password
        println "Login attempt - Username: ${username}, Password: ${password ?: 'empty'}"

        if (!username || !password) {
            flash.message = "Username and password are required"
            redirect(action: 'loginPage')
            return
        }

        def client = new RESTClient("http://localhost:8080/")
        try {
            def resp = client.post(
                    path: '/api/login',
                    body: [username: username, password: password],
                    requestContentType: ContentType.JSON,
                    headers: ['Content-Type': 'application/json', 'Accept': 'application/json']
            )
            println "API response: ${resp.status}, ${resp.data}"

            Cookie jwtCookie = new Cookie('token', resp.data.access_token)
            jwtCookie.setHttpOnly(true)
            jwtCookie.setPath('/')
            jwtCookie.setMaxAge(60 * 60)
            response.addCookie(jwtCookie)

//            redirect(controller: 'teacher', action: 'index')
//            redirect(controller: 'admin', action: 'dashboard')

            // Role-based redirection
            def authorities = SecurityContextHolder.context.authentication.authorities
            if (authorities.contains(new SimpleGrantedAuthority('ROLE_ADMIN'))) {
                redirect(controller: 'admin', action: 'dashboard')
            } else if (authorities.contains(new SimpleGrantedAuthority('ROLE_TEACHER'))) {
                redirect(controller: 'teacher', action: 'indexTeacher')
            } else if (authorities.contains(new SimpleGrantedAuthority('ROLE_STUDENT'))) {
                redirect(controller: 'student', action: 'dashboard')
            } else {
                flash.message = "No valid role assigned to user"
                redirect(action: 'loginPage')
            }
        } catch (Exception e) {
            println "Login failed: ${e.getClass().name} - ${e.message}"
            if (e.response) {
                println "Status: ${e.response.status}, Body: ${e.response.data}"
            }
            flash.message = "Login failed: ${e.message}"
            redirect(action: 'loginPage')
        }
    }

    @Secured('permitAll')
    def logoutFrontend() {
        Cookie cookie = new Cookie('token', '')
        cookie.setMaxAge(0)
        cookie.setPath('/')
        response.addCookie(cookie)

        session.invalidate()
        redirect(action: 'login')
    }

    @Secured(['ROLE_ADMIN', 'ROLE_TEACHER', 'ROLE_STUDENT'])
    def user() {
        def principal = SecurityContextHolder.context.authentication
        def authorities = principal.authorities*.authority
        render([username: principal.name, role: authorities[0]] as JSON) // Adjust based on your role structure
    }
}
