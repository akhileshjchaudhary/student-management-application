package com.practice.demo

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient

class SubjectController {

    SubjectService subjectService

    @Secured('ROLE_ADMIN')
    def createSubject() {
        def data = request.JSON
        String name = data.name
        String code = data.code
        Long teacherId = data.teacherId

        try {
            def subject = subjectService.createSubject(name, code, teacherId)
            render subject as JSON
        } catch (Exception e) {
            render(status: 400, text: e.message)
        }
    }

    @Secured('ROLE_ADMIN')
    def listAllSubjects() {
        def subjects = subjectService.listAllSubjects()
        def subjectList = subjects.collect { subject ->
            [
                    id    : subject.id,
                    name  : subject.name,
                    code  : subject.code,
                    teacher: [
                            id  : subject.teacher.id,
                            name: subject.teacher.name
                    ]
            ]
        }
        render subjectList as JSON
    }

    @Secured('ROLE_ADMIN')
    def getSubjectById(Long id) {
        try {
            def subject = subjectService.getSubjectById(id)
            render subject as JSON
        } catch (Exception e) {
            render(status: 404, text: e.message)
        }
    }

    @Secured('ROLE_ADMIN')
    def updateSubject(Long id) {
        def data = request.JSON
        String name = data.name
        String code = data.code
        Long teacherId = data.teacherId

        try {
            def subject = subjectService.updateSubject(id, name, code, teacherId)
            render subject as JSON
        } catch (Exception e) {
            render(status: 400, text: e.message)
        }
    }

    @Secured('ROLE_ADMIN')
    def deleteSubject(Long id) {
        boolean deleted = subjectService.deleteSubject(id)
        if (deleted) {
            render(status: 200, text: "StudentManagementSystem.Subject deleted successfully")
        } else {
            render(status: 404, text: "StudentManagementSystem.Subject not found")
        }
    }


    //Frontend

    String apiBaseUrl = "http://localhost:8080/"

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
                    path: "subject",
                    headers: [Authorization: "Bearer ${token}"]
            )
            render(view: "index", model: [subjects: response.data])
        } catch (Exception e) {
            println "Error fetching subjects: ${e.message}"
            flash.message = "Error fetching subjects: ${e.message}"
            render(view: "index", model: [subjects: []])
        }
    }

    @Secured('ROLE_ADMIN')
    def create() {
        def client = new RESTClient(apiBaseUrl)
        def token = request.cookies.find { it.name == 'token' }?.value
        if (!token) {
            flash.message = "Authentication token missing. Please log in again."
            redirect(controller: 'auth', action: 'loginPage')
            return
        }
        try {
            def response = client.get(
                    path: "teacher",
                    headers: [Authorization: "Bearer ${token}"]
            )
            render(view: "create", model: [teachers: response.data])
        } catch (Exception e) {
            println "Error fetching teachers for subject creation: ${e.message}"
            flash.message = "Error fetching teachers: ${e.message}"
            render(view: "create", model: [teachers: []])
        }
    }

    @Secured('ROLE_ADMIN')
    def saveSubject() {
        def client = new RESTClient(apiBaseUrl)
        def token = request.cookies.find { it.name == 'token' }?.value
        if (!token) {
            flash.message = "Authentication token missing. Please log in again."
            redirect(controller: 'auth', action: 'loginPage')
            return
        }
        try {
            client.post(
                    path: "subject",
                    body: [
                            name      : params.name,
                            code      : params.code,
                            teacherId : params.long("teacherId")
                    ],
                    requestContentType: ContentType.JSON,
                    headers: [Authorization: "Bearer ${token}"]
            )
            flash.message = "Subject created successfully"
            redirect(action: "index")
        } catch (Exception e) {
            println "Error creating subject: ${e.message}"
            flash.message = "Error creating subject: ${e.message}"
            redirect(action: "create")
        }
    }

    @Secured('ROLE_ADMIN')
    def edit(Long id) {
        def client = new RESTClient(apiBaseUrl)
        def token = request.cookies.find { it.name == 'token' }?.value
        if (!token) {
            flash.message = "Authentication token missing. Please log in again."
            redirect(controller: 'auth', action: 'loginPage')
            return
        }
        try {
            def subjectRes = client.get(
                    path: "subject/${id}",
                    headers: [Authorization: "Bearer ${token}"]
            )
            def teachersRes = client.get(
                    path: "teacher",
                    headers: [Authorization: "Bearer ${token}"]
            )
            render(view: "edit", model: [subject: subjectRes.data, teachers: teachersRes.data])
        } catch (Exception e) {
            println "Error fetching subject or teachers: ${e.message}"
            flash.message = "Error fetching subject or teachers: ${e.message}"
            redirect(action: "index")
        }
    }

    @Secured('ROLE_ADMIN')
    def update() {
        def id = params.long("id")
        def client = new RESTClient(apiBaseUrl)
        def token = request.cookies.find { it.name == 'token' }?.value
        if (!token) {
            flash.message = "Authentication token missing. Please log in again."
            redirect(controller: 'auth', action: 'loginPage')
            return
        }
        try {
            client.put(
                    path: "subject/${id}",
                    body: [
                            name      : params.name,
                            code      : params.code,
                            teacherId : params.long("teacherId")
                    ],
                    requestContentType: ContentType.JSON,
                    headers: [Authorization: "Bearer ${token}"]
            )
            flash.message = "Subject updated successfully"
            redirect(action: "index")
        } catch (Exception e) {
            println "Error updating subject: ${e.message}"
            flash.message = "Error updating subject: ${e.message}"
            redirect(action: "edit", id: id)
        }
    }

    @Secured('ROLE_ADMIN')
    def delete(Long id) {
        def client = new RESTClient(apiBaseUrl)
        def token = request.cookies.find { it.name == 'token' }?.value
        if (!token) {
            flash.message = "Authentication token missing. Please log in again."
            redirect(controller: 'auth', action: 'loginPage')
            return
        }
        try {
            client.delete(
                    path: "subject/${id}",
                    headers: [Authorization: "Bearer ${token}"]
            )
            flash.message = "Subject deleted successfully"
            redirect(action: "index")
        } catch (Exception e) {
            println "Error deleting subject: ${e.message}"
            flash.message = "Error deleting subject: ${e.message}"
            redirect(action: "index")
        }
    }
}
