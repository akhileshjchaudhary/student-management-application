package com.practice.demo

import grails.plugin.springsecurity.annotation.Secured

class AdminController {

    @Secured('ROLE_ADMIN')
    def dashboard() {
        render(view: "dashboard")
    }
}
