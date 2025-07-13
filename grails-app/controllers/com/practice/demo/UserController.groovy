package com.practice.demo

import grails.plugin.springsecurity.annotation.Secured
import grails.converters.JSON
import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient

class UserController {

    def userService

    @Secured('ROLE_ADMIN')
    def register() {
        def json = request.JSON
        String username = json.username
        String password = json.password

        if (username == null || username.trim().isEmpty() || password == null || password.length() < 6) {
            response.status = 400
            render([message: 'Invalid input', errors: [
                    username: (username == null || username.trim().isEmpty()) ? 'Username is required' : null,
                    password: (password == null) ? 'Password is required' : (password.length() < 6 ? 'Password too short it ' : null)
            ]] as JSON)
            return
        }

        def user = userService.createUser(username, password)

        if (user != null) {
            response.status = 201
            render(user as JSON)
        } else {
            response.status = 400
            render([message: 'User creation failed, username may already exist'] as JSON)
        }
    }

    @Secured('ROLE_ADMIN')
    def listAll() {
        def users = userService.getAllUsers()
        render(users as JSON)
    }

    @Secured('ROLE_ADMIN')
    def getUser() {
        Long id = params.long('id')
        def user = userService.getUserById(id)

        if (user != null) {
            render(user as JSON)
        } else {
            response.status = 404
            render([message: 'User not found'] as JSON)
        }
    }

    @Secured('ROLE_ADMIN')
    def update(Long id) {
        def json = request.JSON
        String newUsername = json.username
        String newPassword = json.password

        def user = userService.updateUser(id, newUsername, newPassword)

        if (user != null) {
            render(user as JSON)
        } else {
            response.status = 404
            render([message: 'User not found or update failed'] as JSON)
        }
    }

    @Secured('ROLE_ADMIN')
    def delete() {
        Long id = params.long('id');

        boolean deleted = userService.deleteUser(id)

        if (deleted) {
            render([message: 'User deleted successfully'] as JSON)
        } else {
            response.status = 404
            render([message: 'User not found'] as JSON)
        }
    }

    def apiBaseUrl = "http://localhost:8080/"

    @Secured('ROLE_ADMIN')
    def index() {
        def client = new RESTClient(apiBaseUrl)
        def token = request.cookies.find { it.name == 'token' }?.value
        if (!token) {
            flash.message = "Authentication token missing. Please log in again."
            redirect(controller: 'auth', action: 'loginPage')
            return
        }

        try {
            def response = client.get(
                    path: "user",
                    headers: [Authorization: "Bearer ${token}"]
            )
            render(view: "index", model: [users: response.data])
        } catch (Exception e) {
            flash.message = "Failed to load users"
            render(view: "index", model: [users: []])
        }
    }


    @Secured('ROLE_ADMIN')
    def create() {
        render(view: "create")
    }

    @Secured('ROLE_ADMIN')
    def saveUser() {
        def client = new RESTClient(apiBaseUrl)
        def token = request.cookies.find { it.name == 'token' }?.value
        if (!token) {
            flash.message = "Authentication token missing. Please log in again."
            redirect(controller: 'auth', action: 'loginPage')
            return
        }
        try {
            client.post(
                    path: "user",
                    body: [
                            username: params.username,
                            password: params.password
                    ],
                    requestContentType: ContentType.JSON,
                    headers: [Authorization: "Bearer ${token}"]
            )
            flash.message = "User created successfully"
            redirect(action: "index")
        } catch (Exception e) {
            flash.message = "Error creating user: ${e.response?.data?.message ?: e.message}"
            redirect(action: "create")
        }
    }

    @Secured('ROLE_ADMIN')
    def deleteUser(Long id) {
        def client = new RESTClient(apiBaseUrl)
        def token = request.cookies.find { it.name == 'token' }?.value
        if (!token) {
            flash.message = "Authentication token missing. Please log in again."
            redirect(controller: 'auth', action: 'loginPage')
            return
        }
        try {
            client.delete(
                    path: "user/${id}",
                    headers: [Authorization: "Bearer ${token}"]
            )
            flash.message = "User deleted"
        } catch (Exception e) {
            flash.message = "Error deleting user: ${e.message}"
        }
        redirect(action: "index")
    }
}
