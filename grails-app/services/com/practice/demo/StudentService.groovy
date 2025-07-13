package com.practice.demo


import grails.gorm.transactions.Transactional

@Transactional
class StudentService {

    RoleService roleService;

    Student createStudent(Long userId, String name, Date dob, String address) {
        User user = User.get(userId)
        if (!user) {
            throw new IllegalArgumentException("User not found")
        }

        def assignResult = roleService.assignRoleToUser(user.username, "ROLE_STUDENT")
        if (assignResult.status != 200 && assignResult.status != 400) {
            throw new IllegalStateException(assignResult.body.message)
        }

        Student existing = Student.findByUser(user)
        if (existing) {
            return existing
        }

        Student student = new Student(user: user, name: name, dob: dob, address: address)
        student.save(flush: true, failOnError: true)
        return student
    }

    Student getStudentById(Long id) {
        return Student.get(id)
    }

    Student getStudentByUserId(Long userId) {
        User user = User.get(userId)
        if (user) {
            return Student.findByUser(user)
        } else {
            return null
        }
    }

    List<Student> getAllStudents() {
        return Student.list()
    }

    Student updateStudent(Long id, String name, Date dob, String address) {
        Student student = Student.get(id)
        if (!student) return null

        student.name = name
        student.dob = dob
        student.address = address
        student.save(flush: true, failOnError: true)
        return student
    }

    boolean deleteStudentById(Long id) {
        Student student = Student.get(id)
        if (!student) return false

        student.delete(flush: true)
        return true
    }
}
