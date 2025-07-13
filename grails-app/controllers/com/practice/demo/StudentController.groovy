package com.practice.demo


import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient

import java.text.SimpleDateFormat

class StudentController {

    StudentService studentService

    @Secured('ROLE_ADMIN')
    def createStudent() {
        def data = request.JSON
        Long userId = data.userId
        String name = data.name
        String dobString = data.dob
        String address = data.address

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd")
            Date dob = sdf.parse(dobString)

            Student student = studentService.createStudent(userId, name, dob, address)
            render student as JSON
        } catch (IllegalArgumentException e) {
            render(status: 400, text: e.message)
        } catch (Exception e) {
            render(status: 400, text: "Invalid date format. Use yyyy-MM-dd")
        }
    }

    @Secured(['ROLE_ADMIN', 'ROLE_STUDENT'])
    def getStudentById(Long id) {
        Student student = studentService.getStudentById(id)
        if (student) {
            render student as JSON
        } else {
            render(status: 404, text: "StudentManagementSystem.Student not found")
        }
    }

    @Secured('ROLE_ADMIN')
    def getStudentByUserId(Long userId) {
        Student student = studentService.getStudentByUserId(userId)
        if (student) {
            render student as JSON
        } else {
            render(status: 404, text: "StudentManagementSystem.Student not found")
        }
    }

    @Secured('ROLE_ADMIN')
    def getAllStudents() {
        List<Student> students = studentService.getAllStudents()
        render students as JSON
    }

    @Secured('ROLE_ADMIN')
    def updateStudent(Long id) {
        def data = request.JSON
        String name = data.name
        String dobString = data.dob
        String address = data.address

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd")
            Date dob = sdf.parse(dobString)

            Student updated = studentService.updateStudent(id, name, dob, address)
            if (updated) {
                render updated as JSON
            } else {
                render(status: 404, text: "StudentManagementSystem.Student not found")
            }
        } catch (IllegalArgumentException e) {
            render(status: 400, text: e.message)
        } catch (Exception e) {
            render(status: 400, text: "Invalid date format. Use yyyy-MM-dd")
        }
    }

    @Secured('ROLE_ADMIN')
    def deleteStudentById(Long id) {
        boolean deleted = studentService.deleteStudentById(id)
        if (deleted) {
            render(status: 200, text: "StudentManagementSystem.Student deleted")
        } else {
            render(status: 404, text: "StudentManagementSystem.Student not found")
        }
    }

    //Frontend

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
                    path: "student",
                    headers: [Authorization: "Bearer ${token}"]
            )
            render(view: "index", model: [students: response.data])
        } catch (Exception e) {
            println "Error fetching students: ${e.message}"
            flash.message = "Error fetching students: ${e.message}"
            render(view: "index", model: [students: []])
        }
    }

    @Secured('ROLE_ADMIN')
    def create() {
        render(view: "create")
    }

    @Secured('ROLE_ADMIN')
    def saveStudent() {
        def client = new RESTClient(apiBaseUrl)
        def token = request.cookies.find { it.name == 'token' }?.value
        if (!token) {
            flash.message = "Authentication token missing. Please log in again."
            redirect(controller: 'auth', action: 'loginPage')
            return
        }
        try {
            def response = client.post(
                    path: "student",
                    body: [
                            userId : params.long("userId"),
                            name   : params.name,
                            dob    : params.dob,
                            address: params.address
                    ],
                    requestContentType: ContentType.JSON,
                    headers: [Authorization: "Bearer ${token}"]
            )
            flash.message = "Student created successfully"
            redirect(action: "index")
        } catch (Exception e) {
            println "Error creating student: ${e.message}"
            flash.message = "Error creating student: ${e.message}"
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
            def response = client.get(
                    path: "student/${id}",
                    headers: [Authorization: "Bearer ${token}"]
            )
            render(view: "edit", model: [student: response.data])
        } catch (Exception e) {
            println "Error fetching student: ${e.message}"
            flash.message = "Error fetching student: ${e.message}"
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
            def response = client.put(
                    path: "student/${id}",
                    body: [
                            name   : params.name,
                            dob    : params.dob,
                            address: params.address
                    ],
                    requestContentType: ContentType.JSON,
                    headers: [Authorization: "Bearer ${token}"]
            )
            flash.message = "Student updated successfully"
            redirect(action: "index")
        } catch (Exception e) {
            println "Error updating student: ${e.message}"
            flash.message = "Error updating student: ${e.message}"
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
                    path: "student/${id}",
                    headers: [Authorization: "Bearer ${token}"]
            )
            flash.message = "Student deleted"
            redirect(action: "index")
        } catch (Exception e) {
            println "Error deleting student: ${e.message}"
            flash.message = "Error deleting student: ${e.message}"
            redirect(action: "index")
        }
    }
}
