package com.practice.demo
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient
import org.springframework.security.core.context.SecurityContextHolder


import grails.converters.JSON

class TeacherController {

    TeacherService teacherService

    RoleService roleService;

    @Secured('ROLE_ADMIN')
    def createTeacher() {
        def data = request.JSON
        Long userId = data.userId
        String name = data.name
        String phoneNumber = data.phoneNumber

        try {
            Teacher teacher = teacherService.createTeacher(userId, name, phoneNumber);
            render teacher as JSON;
        } catch (IllegalArgumentException e) {
            render(status: 400, text: e.message);
        }
    }

    @Secured('ROLE_ADMIN')
    def getTeacherById(Long id) {
        Teacher teacher = teacherService.getTeacherById(id);
        if (teacher) {
            render teacher as JSON;
        } else {
            render(status: 404, text: "StudentManagementSystem.Teacher not found");
        }
    }

    @Secured('ROLE_ADMIN')
    def getAllTeachers() {
        List<Teacher> teachers = teacherService.getAllTeachers();
        render teachers as JSON;
    }

    @Secured('ROLE_ADMIN')
    def updateTeacher(Long id) {
        def data = request.JSON;
        String name = data.name
        String phoneNumber = data.phoneNumber

        Teacher updated = teacherService.updateTeacher(id, name, phoneNumber)
        if (updated) {
            render updated as JSON
        } else {
            render(status: 404, text: "StudentManagementSystem.Teacher not found")
        }
    }

    @Secured('ROLE_ADMIN')
    def deleteTeacherById(Long id) {
        boolean deleted = teacherService.deleteTeacherById(id)
        if (deleted) {
            render(status: 200, text: "StudentManagementSystem.Teacher deleted")
        } else {
            render(status: 404, text: "StudentManagementSystem.Teacher not found")
        }
    }

    @Secured(['ROLE_ADMIN', 'ROLE_TEACHER'])
    def getTeacherByUsername() {
        try {
            def authentication = SecurityContextHolder.context.authentication
            String username = authentication?.principal?.username
            if (!username) {
                render(status: 400, text: "Username not found in authentication context")
                return
            }
            Teacher teacher = teacherService.getTeacherByUsername(username)
            render teacher as JSON
        } catch (IllegalArgumentException e) {
            render(status: 404, text: e.message)
        } catch (Exception e) {
            render(status: 500, text: "Internal server error: ${e.message}")
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
                    path: "teacher",
                    headers: [Authorization: "Bearer ${token}"]
            )
            def teacherData = response.data.collect {
                [
                        id          : it.id,
                        name        : it.name,
                        phoneNumber : it.phoneNumber,
                        userId      : it.user?.id
                ]
            }
            render(view: "index", model: [teachers: teacherData])
        } catch (Exception e) {
            println "Error fetching teachers: ${e.message}"
            flash.message = "Error fetching teachers: ${e.message}"
            render(view: "index", model: [teachers: []])
        }
    }

    @Secured('ROLE_ADMIN')
    def create() {
        render(view: "create")
    }

    @Secured('ROLE_ADMIN')
    def save() {
        def client = new RESTClient(apiBaseUrl)
        def token = request.cookies.find { it.name == 'token' }?.value
        if (!token) {
            flash.message = "Authentication token missing. Please log in again."
            redirect(controller: 'auth', action: 'loginPage')
            return
        }
        try {
            def response = client.post(
                    path: "teacher",
                    body: [
                            userId     : params.long('userId'),
                            name       : params.name,
                            phoneNumber: params.phoneNumber
                    ],
                    requestContentType: ContentType.JSON,
                    headers: [Authorization: "Bearer ${token}"]
            )
            flash.message = "Teacher created"
            redirect(action: "index")
        } catch (Exception e) {
            println "Error creating teacher: ${e.message}"
            flash.message = "Error creating teacher: ${e.message}"
            redirect(action: "create")
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
                    path: "teacher/${id}",
                    headers: [Authorization: "Bearer ${token}"]
            )
            flash.message = "Teacher deleted"
            redirect(action: "index")
        } catch (Exception e) {
            println "Error deleting teacher: ${e.message}"
            flash.message = "Error deleting teacher: ${e.message}"
            redirect(action: "index")
        }
    }

    @Secured('ROLE_ADMIN')
    def editTeacher(Long id) {
        def client = new RESTClient(apiBaseUrl)
        def token = request.cookies.find { it.name == 'token' }?.value
        if (!token) {
            flash.message = "Authentication token missing. Please log in again."
            redirect(controller: 'auth', action: 'loginPage')
            return
        }
        try {
            def response = client.get(
                    path: "teacher/${id}",
                    headers: [Authorization: "Bearer ${token}"]
            )
            render(view: "editTeacher", model: [teacher: response.data])
        } catch (Exception e) {
            println "Error fetching teacher: ${e.message}"
            flash.message = "Error fetching teacher: ${e.message}"
            redirect(action: "index")
        }
    }

    @Secured('ROLE_ADMIN')
    def updateTeacherREST() {
        def id = params.id as Long
        def name = params.name
        def phoneNumber = params.phoneNumber
        def client = new RESTClient(apiBaseUrl)
        def token = request.cookies.find { it.name == 'token' }?.value
        if (!token) {
            flash.message = "Authentication token missing. Please log in again."
            redirect(controller: 'auth', action: 'loginPage')
            return
        }
        try {
            def response = client.put(
                    path: "teacher/${id}",
                    body: [name: name, phoneNumber: phoneNumber],
                    requestContentType: ContentType.JSON,
                    headers: [Authorization: "Bearer ${token}"]
            )
            flash.message = "Teacher updated"
            redirect(action: "index")
        } catch (Exception e) {
            println "Error updating teacher: ${e.message}"
            flash.message = "Error updating teacher: ${e.message}"
            redirect(action: "editTeacher", id: id)
        }
    }

    @Secured('ROLE_TEACHER')
    def indexTeacher() {
        def client = new RESTClient(apiBaseUrl)
        def token = request.cookies.find { it.name == 'token' }?.value
        if (!token) {
            flash.message = "Authentication token missing. Please log in again."
            redirect(controller: 'auth', action: 'loginPage')
            return
        }

        try {
            // Fetch current teacher's details
            def teacherResponse = client.get(
                    path: "teacher/byUsername",
                    headers: [Authorization: "Bearer ${token}"]
            )
            def teacher = teacherResponse.data

            // Fetch students assigned to the teacher
            def studentsResponse = client.get(
                    path: "teacher-students/${teacher.id}/students",
                    headers: [Authorization: "Bearer ${token}"]
            )
            def students = studentsResponse.data ?: [] // Fallback to empty list if null

            // Fetch recent attendance records
            def attendanceResponse = client.get(
                    path: "attendance/byTeacher/${teacher.id}",
                    headers: [Authorization: "Bearer ${token}"]
            )
            def attendanceRecords = attendanceResponse.data ?: [] // Fallback to empty list if null

            render(view: "dashboard", model: [
                    teacher: teacher,
                    students: students,
                    attendanceRecords: attendanceRecords
            ])
        } catch (Exception e) {
            println "Error fetching dashboard data: ${e.message}"
            flash.message = "Error fetching dashboard data: ${e.message}"
            render(view: "dashboard", model: [teacher: null, students: [], attendanceRecords: []])
        }
    }
}
