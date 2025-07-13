package com.practice.demo


import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import groovyx.net.http.RESTClient
import groovyx.net.http.ContentType


import java.text.SimpleDateFormat

class TeacherStudentController {

    TeacherStudentService teacherStudentService

    @Secured('ROLE_ADMIN')
    def assignStudents() {
        def data = request.JSON
        Long teacherId = data.teacherId
        List<Long> studentIds = data.studentIds

        Teacher teacher = Teacher.get(teacherId)
        List<Student> students = Student.findAllByIdInList(studentIds)

        if (!teacher || students.isEmpty()) {
            render(status: 400, text: "Invalid teacher or students")
            return
        }

        teacherStudentService.assignStudentsToTeacher(teacher, students)
        render(status: 200, text: "Students assigned successfully")
    }

    @Secured(['ROLE_ADMIN', 'ROLE_TEACHER'])
    def studentsForTeacher(Long teacherId) {
        try {
            println "Fetching students for teacher ID: ${teacherId}"
            Teacher teacher = Teacher.get(teacherId)
            if (!teacher) {
                println "Teacher not found for ID: ${teacherId}"
                render(status: 404, text: "Teacher not found")
                return
            }
            List<Student> students = teacherStudentService.getStudentsForTeacher(teacher) ?: []
            println "Found ${students.size()} students for teacher ID: ${teacherId}"
            def response = students.collect { [
                    id: it.id,
                    name: it.name,
                    dob: it.dob?.format("yyyy-MM-dd") ?: null,
                    address: it.address ?: null
            ] }
            render response as JSON
        } catch (Exception e) {
            println "Error in studentsForTeacher: ${e.message}, Stacktrace: ${e.stackTrace.join('\n')}"
            render(status: 500, text: "Error fetching students: ${e.message}")
        }
    }

    @Secured('ROLE_ADMIN')
    def teachersForStudent(Long id) {
        Student student = Student.get(id)

        if (!student) {
            render(status: 404, text: "Student not found")
            return
        }

        List<Teacher> teachers = teacherStudentService.getTeachersForStudent(student)
        render teachers as JSON
    }

    @Secured('ROLE_ADMIN')
    def unassignStudent() {
        def data = request.JSON
        Long teacherId = data.teacherId
        Long studentId = data.studentId

        Teacher teacher = Teacher.get(teacherId)
        Student student = Student.get(studentId)

        if (!teacher || !student) {
            render(status: 404, text: "Teacher or Student not found")
            return
        }

        boolean result = teacherStudentService.unassignStudentFromTeacher(teacher, student)
        if (result) {
            render(status: 200, text: "Unassigned successfully")
        } else {
            render(status: 400, text: "Assignment not found")
        }
    }

//    @Secured('ROLE_TEACHER')
//    def takeAttendance() {
//        def data = request.JSON
//
//        Long teacherId = data.teacherId as Long
//        String dateStr = data.date
//        Map studentMap = data.studentMap
//
//        if (!teacherId || !dateStr || !studentMap) {
//            render([message: "Missing required fields"] as JSON)
//            return
//        }
//
//        Teacher teacher = Teacher.get(teacherId)
//        if (!teacher) {
//            render([message: "StudentManagementSystem.Teacher not found"] as JSON)
//            return
//        }
//
//        Date date;
//        try {
//            date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr)
//        } catch (Exception e) {
//            render([message: "Invalid date format. Use yyyy-MM-dd"] as JSON)
//            return
//        }
//
//        Map<Long, String> studentStatusMap = new HashMap<>()
//        Set keys = studentMap.keySet()
//
//        for (Object keyObj : keys) {
//            Long studentId = Long.parseLong(keyObj.toString())
//            String status = studentMap.get(keyObj).toString()
//            studentStatusMap.put(studentId, status)
//        }
//
//        teacherStudentService.takeAttendance(teacher, studentStatusMap, date)
//
//        render([message: "StudentManagementSystem.Attendance saved successfully"] as JSON)
//    }

    //Frontend

    String apiBaseUrl = "http://localhost:8080/"

    @Secured('ROLE_ADMIN')
    def assignForm() {
        def client = new RESTClient(apiBaseUrl)
        def token = request.cookies.find { it.name == 'token' }?.value
        if (!token) {
            flash.message = "Authentication token missing. Please log in again."
            redirect(controller: 'auth', action: 'loginPage')
            return
        }

        try {
            // Fetch all teachers via API
            def teachersResponse = client.get(
                    path: "teacher",
                    headers: [Authorization: "Bearer ${token}"]
            )
            // Fetch all students via API
            def studentsResponse = client.get(
                    path: "student",
                    headers: [Authorization: "Bearer ${token}"]
            )
            render(view: "assignForm", model: [teachers: teachersResponse.data, students: studentsResponse.data])
        } catch (Exception e) {
            println "Error fetching data for assignment form: ${e.message}"
            flash.message = "Error fetching data for assignment form: ${e.message}"
            render(view: "assignForm", model: [teachers: [], students: []])
        }
    }

    @Secured('ROLE_ADMIN')
    def assign() {
        def client = new RESTClient(apiBaseUrl)
        def token = request.cookies.find { it.name == 'token' }?.value
        if (!token) {
            flash.message = "Authentication token missing. Please log in again."
            redirect(controller: 'auth', action: 'loginPage')
            return
        }

        Long teacherId = params.long("teacherId")
        List<Long> studentIds = params.list("studentIds")*.toLong()

        try {
            def response = client.post(
                    path: "teacher-students/assign",
                    body: [teacherId: teacherId, studentIds: studentIds],
                    requestContentType: ContentType.JSON,
                    headers: [Authorization: "Bearer ${token}"]
            )
            flash.message = "Students assigned successfully"
            redirect(action: "assignForm")
        } catch (Exception e) {
            println "Error assigning students: ${e.message}"
            flash.message = "Error assigning students: ${e.message}"
            redirect(action: "assignForm")
        }
    }
}
