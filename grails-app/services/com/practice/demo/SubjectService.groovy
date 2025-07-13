package com.practice.demo


import grails.gorm.transactions.Transactional

@Transactional
class SubjectService {

    public Subject createSubject(String name, String code, Long teacherId) {
        def teacher = Teacher.get(teacherId);
        if (!teacher) {
            throw new Exception("StudentManagementSystem.Teacher not found")
        }
        def subject = new Subject(name: name, code: code, teacher: teacher)
        subject.save(flush: true)
        return subject
    }

    public List<Subject> listAllSubjects() {
        return Subject.list()
    }

    public Subject getSubjectById(Long id) {
        def subject = Subject.get(id)
        if (!subject) {
            throw new Exception("StudentManagementSystem.Subject not found")
        }
        return subject
    }

    public Subject updateSubject(Long id, String name, String code, Long teacherId) {
        def subject = Subject.get(id)
        if (!subject) {
            throw new Exception("StudentManagementSystem.Subject not found")
        }

        def teacher = Teacher.get(teacherId)
        if (!teacher) {
            throw new Exception("StudentManagementSystem.Teacher not found")
        }

        subject.name = name
        subject.code = code
        subject.teacher = teacher
        subject.save(flush: true)

        return subject
    }

    public boolean deleteSubject(Long id) {
        def subject = Subject.get(id)
        if (subject) {
            subject.delete(flush: true)
            return true
        }
        return false
    }
}
