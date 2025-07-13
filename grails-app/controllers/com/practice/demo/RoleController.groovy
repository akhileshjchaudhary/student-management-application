package com.practice.demo

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import groovyx.net.http.RESTClient
import groovyx.net.http.ContentType


class RoleController {

    RoleService roleService

    @Secured('ROLE_ADMIN')
    def create() {
        def json = request.JSON
        String authority = json?.authority

        if (!authority) {
            response.status = 400
            render([message: "Authority is required"] as JSON)
            return
        }

        def role = roleService.createRole(authority)
        if (role) {
            response.status = 201
            render(role as JSON)
        } else {
            response.status = 400
            render([message: "Failed to create role."] as JSON)
        }
    }

    @Secured('ROLE_ADMIN')
    def assignRole() {
        def json = request.JSON
        String username = json.username
        String role = json.role

        if (!username || !role) {
            response.status = 400
            render([message: 'Username and role are required'] as JSON)
            return
        }

        def result = roleService.assignRoleToUser(username, role)
        response.status = result.status
        render(result.body as JSON)
    }

    @Secured('ROLE_ADMIN')
    def getById(Long id) {
        def role = roleService.getById(id)
        if (!role) {
            response.status = 404
            render([message: "Role not found"] as JSON)
        } else {
            render(role as JSON)
        }
    }

    @Secured('ROLE_ADMIN')
    def getAll() {
        render(roleService.getAll() as JSON)
    }

    @Secured('ROLE_ADMIN')
    def update(Long id) {
        def json = request.JSON
        String authority = json?.authority

        if (!authority) {
            response.status = 400
            render([message: "Authority is required"] as JSON)
            return
        }

        def updated = roleService.updateRole(id, authority)
        if (!updated) {
            response.status = 404
            render([message: "Role not found or update failed"] as JSON)
        } else {
            render([message: "Role updated successfully", authority: updated.authority] as JSON)
        }
    }

    @Secured('ROLE_ADMIN')
    def delete(Long id) {
        boolean deleted = roleService.deleteRole(id)
        if (deleted) {
            render([message: "Role deleted successfully"] as JSON)
        } else {
            response.status = 404
            render([message: "Role not found"] as JSON)
        }
    }

    //Frontend

    String apiBaseUrl = "http://localhost:8080/" // Backend base URL

    @Secured('ROLE_ADMIN')
    def index() {
        def client = new RESTClient(apiBaseUrl)
        def token = request.cookies.find { it.name == 'token' }?.value

        try {
            def response = client.get(
                    path: "role",
                    headers: [Authorization: "Bearer ${token}"]
            )
            render(view: "index", model: [roles: response.data])
        } catch (Exception e) {
            flash.message = "Failed to load roles: ${e.message}"
            render(view: "index", model: [roles: []])
        }
    }

    @Secured('ROLE_ADMIN')
    def createForm() {
        render(view: "create")
    }

    @Secured('ROLE_ADMIN')
    def saveRole() {
        def authority = params.authority
        def client = new RESTClient(apiBaseUrl)
        def token = request.cookies.find { it.name == 'token' }?.value

        try {
            client.post(
                    path: "role",
                    body: [authority: authority],
                    requestContentType: ContentType.JSON,
                    headers: [Authorization: "Bearer ${token}"]
            )
            flash.message = "Role created successfully"
            redirect(action: "index")
        } catch (Exception e) {
            flash.message = "Failed to create role: ${e.message}"
            redirect(action: "createForm")
        }
    }

//    @Secured('ROLE_ADMIN')
//    def assignForm() {
//        render(view: "assign")
//    }

//    @Secured('ROLE_ADMIN')
//    def assignToUser() {
//        def username = params.username
//        def role = params.role
//        def client = new RESTClient(apiBaseUrl)
//        def token = request.cookies.find { it.name == 'token' }?.value
//
//        try {
//            client.post(
//                    path: "assign-role",
//                    body: [username: username, role: role],
//                    requestContentType: ContentType.JSON,
//                    headers: [Authorization: "Bearer ${token}"]
//            )
//            flash.message = "Role assigned successfully"
//        } catch (Exception e) {
//            flash.message = "Error assigning role: ${e.message}"
//        }
//
//        redirect(action: "assignForm")
//    }

    @Secured('ROLE_ADMIN')
    def deleteRole(Long id) {
        def client = new RESTClient(apiBaseUrl)
        def token = request.cookies.find { it.name == 'token' }?.value

        try {
            client.delete(
                    path: "role/${id}",
                    headers: [Authorization: "Bearer ${token}"]
            )
            flash.message = "Role deleted successfully"
        } catch (Exception e) {
            flash.message = "Failed to delete role: ${e.message}"
        }
        redirect(action: "index")
    }
}
