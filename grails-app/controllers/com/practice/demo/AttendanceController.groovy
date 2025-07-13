package com.practice.demo

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient

import java.text.SimpleDateFormat

class AttendanceController {

    AttendanceService attendanceService

    @Secured('ROLE_TEACHER')
    def takeAttendance() {
        def data = request.JSON
        Long teacherId = data.teacherId
        String dateString = data.date
        Map<Long, String> studentStatusMap = data.studentStatusMap

        if (!teacherId || !dateString || !studentStatusMap) {
            render(status: 400, text: "Missing required fields")
            return
        }

        Teacher teacher = Teacher.get(teacherId)
        if (!teacher) {
            render(status: 404, text: "Teacher not found")
            return
        }

        Date date
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString)
        } catch (Exception e) {
            render(status: 400, text: "Invalid date format, use yyyy-MM-dd")
            return
        }

        attendanceService.takeAttendance(teacher, studentStatusMap, date)
        render(status: 200, text: "Attendance recorded successfully")
    }

    @Secured(['ROLE_ADMIN', 'ROLE_TEACHER'])
    def getAttendanceByTeacherId(Long teacherId) {
        try {
            def attendance = attendanceService.getAttendanceByTeacherId(teacherId)
            println "Found ${attendance.size()} attendance records for teacher ID: ${teacherId}"
            render attendance as JSON
        } catch (IllegalArgumentException e) {
            println "Error in getAttendanceByTeacherId: ${e.message}"
            render(status: 404, text: e.message)
        } catch (Exception e) {
            println "Unexpected error in getAttendanceByTeacherId: ${e.message}, Stacktrace: ${e.stackTrace.join('\n')}"
            render(status: 500, text: "Error fetching attendance: ${e.message}")
        }
    }

//    def getAttendanceById(Long id) {
//        StudentManagementSystem.Attendance attendance = attendanceService.getAttendanceById(id)
//        if (attendance) {
//            render attendance as JSON
//        } else {
//            render status: 404, text: "StudentManagementSystem.Attendance not found"
//        }
//    }
//
//    def getAllAttendance() {
//        render attendanceService.getAllAttendance() as JSON
//    }
//
//    def getAttendanceByStudentId(Long studentId) {
//        render attendanceService.getAttendanceByStudentId(studentId) as JSON
//    }
//
//    def getAttendanceByTeacherId(Long teacherId) {
//        render attendanceService.getAttendanceByTeacherId(teacherId) as JSON
//    }
//
//    def getAttendanceByDate() {
//        def dateParam = params.date
//        if (!dateParam) {
//            render status: 400, text: "Missing date parameter"
//            return
//        }
//
//        Date date = Date.parse("yyyy-MM-dd", dateParam)
//        render attendanceService.getAttendanceByDate(date) as JSON
//    }
//
//    def updateAttendance(Long id) {
//        def data = request.JSON
//        String status = data.status
//
//        try {
//            StudentManagementSystem.Attendance updated = attendanceService.updateAttendance(id, status)
//            render updated as JSON
//        } catch (IllegalArgumentException e) {
//            render status: 404, text: e.message
//        }
//    }
//
//    def deleteAttendanceById(Long id) {
//        boolean deleted = attendanceService.deleteAttendanceById(id)
//        if (deleted) {
//            render status: 200, text: "StudentManagementSystem.Attendance deleted"
//        } else {
//            render status: 404, text: "StudentManagementSystem.Attendance not found"
//        }
//    }

    //Frontend

    String apiBaseUrl = "http://localhost:8080/"

    @Secured('ROLE_TEACHER')
    def create() {
        def client = new RESTClient(apiBaseUrl)
        def token = request.cookies.find { it.name == 'token' }?.value
        if (!token) {
            flash.message = "Authentication token missing. Please log in again."
            redirect(controller: 'auth', action: 'loginPage')
            return
        }

        Map<String, Object> model = new HashMap<>()
        try {
            // Fetch all teachers via API
            def allTeachersResponse = client.get(
                    path: "teacher",
                    headers: [Authorization: "Bearer ${token}"]
            )
            model.put("allTeachers", allTeachersResponse.data)

            Long teacherId = params.long("teacherId")
            if (teacherId) {
                // Fetch specific teacher via API
                def singleTeacherResponse = client.get(
                        path: "teacher/${teacherId}",
                        headers: [Authorization: "Bearer ${token}"]
                )
                def teacher = singleTeacherResponse.data

                // Fetch students assigned to the teacher via API
                def assignedResponse = client.get(
                        path: "teacherStudent/byTeacher/${teacherId}",
                        headers: [Authorization: "Bearer ${token}"]
                )
                def students = assignedResponse.data.collect { it.student }

                model.put("teacher", teacher)
                model.put("students", students)
            }

            render(view: "create", model: model)
        } catch (Exception e) {
            println "Error fetching data for attendance: ${e.message}"
            flash.message = "Error fetching data for attendance: ${e.message}"
            render(view: "create", model: [allTeachers: [], teacher: null, students: []])
        }
    }

    @Secured('ROLE_TEACHER')
    def save() {
        def client = new RESTClient(apiBaseUrl)
        def token = request.cookies.find { it.name == 'token' }?.value
        if (!token) {
            flash.message = "Authentication token missing. Please log in again."
            redirect(controller: 'auth', action: 'loginPage')
            return
        }

        Long teacherId = params.long("teacherId")
        Date date = params.date("date", "yyyy-MM-dd")

        Map<String, String> studentStatusMap = new HashMap<>()
        for (String key : params.keySet()) {
            if (key.startsWith("status_")) {
                String idPart = key.replace("status_", "")
                String status = params.get(key)
                studentStatusMap.put(idPart, status)
            }
        }

        def sdf = new java.text.SimpleDateFormat("yyyy-MM-dd")

        Map<String, Object> requestBody = new HashMap<>()
        requestBody.put("teacherId", teacherId)
        requestBody.put("date", sdf.format(date))
        requestBody.put("studentStatusMap", studentStatusMap)

        try {
            client.post(
                    path: "attendance",
                    body: requestBody,
                    requestContentType: ContentType.JSON,
                    headers: [Authorization: "Bearer ${token}"]
            )
            flash.message = "Attendance submitted successfully"
            redirect(action: "create", params: [teacherId: teacherId])
        } catch (Exception e) {
            println "Error submitting attendance: ${e.message}"
            flash.message = "Error submitting attendance: ${e.message}"
            redirect(action: "create", params: [teacherId: teacherId])
        }
    }
}
