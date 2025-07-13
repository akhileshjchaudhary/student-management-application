package com.practice.demo

import grails.core.GrailsApplication
import grails.plugin.springsecurity.SpringSecurityService
import grails.gorm.transactions.Transactional
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm

import javax.crypto.spec.SecretKeySpec
import java.time.Instant
import java.time.temporal.ChronoUnit

@Transactional
class AuthService {

    SpringSecurityService springSecurityService
    GrailsApplication grailsApplication

    Map login(String username, String password) {
        def user = User.findByUsername(username)

        if (!user || !springSecurityService.passwordEncoder.matches(password, user.password)) {
            return [status: 401, body: [message: 'Invalid username or password']]
        }

        if (!user.enabled) {
            return [status: 401, body: [message: 'Account is disabled']]
        }

        String token = generateJwtToken(user)
        def roles = UserRole.findAllByUser(user).collect { it.role.authority }

        return [
                status: 200,
                body: [
                        username     : user.username,
                        roles        : roles,
                        access_token : token,
                        token_type   : 'Bearer',
                        expires_in   : jwtExpiration()
                ]
        ]
    }

    private String generateJwtToken(User user) {
        def key = new SecretKeySpec(jwtSecret().bytes, SignatureAlgorithm.HS256.getJcaName())
        Instant now = Instant.now()

        return Jwts.builder()
                .setSubject(user.username)
                .claim('roles', UserRole.findAllByUser(user)*.role.authority)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(jwtExpiration(), ChronoUnit.SECONDS)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact()
    }

    private String jwtSecret() {
        grailsApplication.config.grails.plugin.springsecurity.rest.token.storage.jwt.secret
    }

    private long jwtExpiration() {
        grailsApplication.config.grails.plugin.springsecurity.rest.token.storage.jwt.expiration as long
    }
}
