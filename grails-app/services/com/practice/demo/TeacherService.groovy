package com.practice.demo

import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.annotation.Secured

@Transactional
class TeacherService {
    RoleService roleService

    UserService userService

    @Secured('ROLE_ADMIN')
    public Teacher createTeacher(Long userId, String name, String phoneNumber) {
        User user = User.get(userId)
        if (user == null) {
            throw new IllegalArgumentException("User not found")
        }

        Teacher existing = Teacher.findByUser(user)
        if (existing != null) {
            return existing
        }

        Teacher teacher = new Teacher(user: user, name: name, phoneNumber: phoneNumber)
        teacher.save(flush: true, failOnError: true)

        // Assign ROLE_TEACHER automatically
        roleService.assignRoleToUser(user.username, "ROLE_TEACHER")

        return teacher
    }

    Teacher getTeacherByUsername(String username) {
        if (!username) {
            throw new IllegalArgumentException("Username cannot be null")
        }
        def user = User.findByUsername(username)
        if (!user) {
            throw new IllegalArgumentException("User not found for username: ${username}")
        }
        def teacher = Teacher.findByUser(user)
        if (!teacher) {
            throw new IllegalArgumentException("Teacher not found for username: ${username}")
        }
        return teacher
    }

    public Teacher getTeacherById(Long id) {
        return Teacher.get(id);
    }

    public List<Teacher> getAllTeachers() {
        return Teacher.list();
    }

    public Teacher updateTeacher(Long id, String name, String phoneNumber) {
        Teacher teacher = Teacher.get(id)
        if (teacher == null) return null;

        teacher.setName(name);
        teacher.setPhoneNumber(phoneNumber);
        teacher.save(flush: true, failOnError: true)
        return teacher;
    }

    public boolean deleteTeacherById(Long id) {
        Teacher teacher = Teacher.get(id);
        if (teacher == null) return false;

        teacher.delete(flush: true);
        return true;
    }
}

