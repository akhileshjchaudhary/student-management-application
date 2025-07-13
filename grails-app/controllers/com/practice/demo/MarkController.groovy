package com.practice.demo

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient

import java.text.SimpleDateFormat

class MarkController {

    MarkService markService

    @Secured('ROLE_TEACHER')
    def create() {
        def data = request.JSON
        Long studentId = data.studentId
        Long subjectId = data.subjectId
        Integer marksObtained = data.marksObtained
        String dobString = data.date

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd")
            Date date = sdf.parse(dobString)

            def mark = markService.createMark(studentId, subjectId, marksObtained, date)
            render mark as JSON
        } catch (Exception e) {
            render(status: 400)
        }
    }

    @Secured('ROLE_TEACHER')
    def list() {
        def marks = markService.listAllMarks()

        def markList = marks.collect { mark ->
            [
                    id           : mark.id,
                    marksObtained: mark.marksObtained,
                    date         : mark.date,
                    student      : [
                            id  : mark.student.id,
                            name: mark.student.name
                    ],
                    subject      : [
                            id  : mark.subject.id,
                            name: mark.subject.name
                    ]
            ]
        }

        render markList as JSON
    }

    @Secured('ROLE_TEACHER')
    def show(Long id) {
        try {
            def mark = markService.getMarkById(id)
            render mark as JSON
        } catch (Exception e) {
            render(status: 404, text: e.message)
        }
    }

    @Secured('ROLE_TEACHER')
    def update(Long id) {
        def data = request.JSON
        try {
            Integer marksObtained = data.marksObtained
            String dobString = data.date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd")
            Date date = sdf.parse(dobString)

            def mark = markService.updateMark(id, marksObtained, date)
            render mark as JSON
        } catch (Exception e) {
            String errorMessage = e.message ?: "Unknown error occurred"
            render(status: 400, text: errorMessage)
        }
    }

    @Secured('ROLE_TEACHER')
    def delete(Long id) {
        def deleted = markService.deleteMark(id)
        if (deleted) {
            render(status: 200, text: "StudentManagementSystem.Mark deleted successfully")
        } else {
            render(status: 404, text: "StudentManagementSystem.Mark not found")
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
                    path: "mark",
                    headers: [Authorization: "Bearer ${token}"]
            )
            render(view: "index", model: [marks: response.data])
        } catch (Exception e) {
            println "Error fetching marks: ${e.message}"
            flash.message = "Error fetching marks: ${e.message}"
            render(view: "index", model: [marks: []])
        }
    }

    @Secured('ROLE_ADMIN')
    def createMark() {
        def client = new RESTClient(apiBaseUrl)
        def token = request.cookies.find { it.name == 'token' }?.value
        if (!token) {
            flash.message = "Authentication token missing. Please log in again."
            redirect(controller: 'auth', action: 'loginPage')
            return
        }
        try {
            def studentsResponse = client.get(
                    path: "student",
                    headers: [Authorization: "Bearer ${token}"]
            )
            def subjectsResponse = client.get(
                    path: "subject",
                    headers: [Authorization: "Bearer ${token}"]
            )
            render(view: "create", model: [students: studentsResponse.data, subjects: subjectsResponse.data])
        } catch (Exception e) {
            println "Error fetching students or subjects: ${e.message}"
            flash.message = "Error fetching students or subjects: ${e.message}"
            render(view: "create", model: [students: [], subjects: []])
        }
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
            def studentId = params.long('studentId')
            def subjectId = params.long('subjectId')
            def marksObtained = params.int('marksObtained')
            def date = params.date('date', 'yyyy-MM-dd')
            def formattedDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format(date)

            client.post(
                    path: "mark",
                    body: [
                            studentId     : studentId,
                            subjectId     : subjectId,
                            marksObtained : marksObtained,
                            date          : formattedDate
                    ],
                    requestContentType: ContentType.JSON,
                    headers: [Authorization: "Bearer ${token}"]
            )
            flash.message = "Mark saved successfully"
            redirect(action: "index")
        } catch (Exception e) {
            println "Error saving mark: ${e.message}"
            flash.message = "Error saving mark: ${e.message}"
            redirect(action: "createMark")
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
            def markResponse = client.get(
                    path: "mark/${id}",
                    headers: [Authorization: "Bearer ${token}"]
            )
            def studentsResponse = client.get(
                    path: "student",
                    headers: [Authorization: "Bearer ${token}"]
            )
            def subjectsResponse = client.get(
                    path: "subject",
                    headers: [Authorization: "Bearer ${token}"]
            )
            render(view: "edit", model: [mark: markResponse.data, students: studentsResponse.data, subjects: subjectsResponse.data])
        } catch (Exception e) {
            println "Error fetching mark, students, or subjects: ${e.message}"
            flash.message = "Error fetching mark, students, or subjects: ${e.message}"
            redirect(action: "index")
        }
    }

    @Secured('ROLE_ADMIN')
    def updateMark() {
        def id = params.long("id")
        def client = new RESTClient(apiBaseUrl)
        def token = request.cookies.find { it.name == 'token' }?.value
        if (!token) {
            flash.message = "Authentication token missing. Please log in again."
            redirect(controller: 'auth', action: 'loginPage')
            return
        }
        try {
            def marksObtained = params.int("marksObtained")
            def date = params.date("date", "yyyy-MM-dd")
            def formattedDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format(date)

            client.put(
                    path: "mark/${id}",
                    body: [marksObtained: marksObtained, date: formattedDate],
                    requestContentType: ContentType.JSON,
                    headers: [Authorization: "Bearer ${token}"]
            )
            flash.message = "Mark updated successfully"
            redirect(action: "index")
        } catch (Exception e) {
            println "Error updating mark: ${e.message}"
            flash.message = "Error updating mark: ${e.message}"
            redirect(action: "edit", id: id)
        }
    }

    @Secured('ROLE_ADMIN')
    def deleteMark(Long id) {
        def client = new RESTClient(apiBaseUrl)
        def token = request.cookies.find { it.name == 'token' }?.value
        if (!token) {
            flash.message = "Authentication token missing. Please log in again."
            redirect(controller: 'auth', action: 'loginPage')
            return
        }
        try {
            client.delete(
                    path: "mark/${id}",
                    headers: [Authorization: "Bearer ${token}"]
            )
            flash.message = "Mark deleted successfully"
            redirect(action: "index")
        } catch (Exception e) {
            println "Error deleting mark: ${e.message}"
            flash.message = "Error deleting mark: ${e.message}"
            redirect(action: "index")
        }
    }
}
