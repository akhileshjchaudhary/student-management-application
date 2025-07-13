package com.practice.demo


import grails.gorm.transactions.Transactional


@Transactional
class TeacherStudentService {

    def assignStudentToTeacher(Teacher teacher, Student student) {
        if (teacher == null || student == null) return null

        TeacherStudent existing = TeacherStudent.findByTeacherAndStudent(teacher, student)
        if (existing != null) return existing

        TeacherStudent ts = new TeacherStudent(teacher: teacher, student: student)
        ts.save(flush: true, failOnError: true)
        return ts
    }

    def assignStudentsToTeacher(Teacher teacher, List<Student> students) {
        if (teacher == null || students == null || students.isEmpty()) {
            return
        }

        for (Student student : students) {
            assignStudentToTeacher(teacher, student)
        }
    }

    def getStudentsForTeacher(Teacher teacher) {
        try {
            if (!teacher) {
                println "Teacher is null in getStudentsForTeacher"
                return []
            }
            println "Fetching students for teacher ID: ${teacher.id}"
            def assignments = TeacherStudent.findAllByTeacher(teacher) ?: []
            def students = assignments*.student.findAll { it != null } ?: []
            println "Found ${students.size()} students for teacher ID: ${teacher.id}"
            return students
        } catch (Exception e) {
            println "Error in getStudentsForTeacher: ${e.message}, Stacktrace: ${e.stackTrace.join('\n')}"
            throw e
        }
    }

    List<Teacher> getTeachersForStudent(Student student) {
        TeacherStudent.findAllByStudent(student)*.teacher
    }

    boolean unassignStudentFromTeacher(Teacher teacher, Student student) {
        TeacherStudent assignment = TeacherStudent.findByTeacherAndStudent(teacher, student)
        if (assignment != null) {
            assignment.delete(flush: true)
            return true
        }
        return false
    }

}
