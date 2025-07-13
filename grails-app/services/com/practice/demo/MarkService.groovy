package com.practice.demo


import grails.gorm.transactions.Transactional

@Transactional
class MarkService {

    public Mark createMark(Long studentId, Long subjectId, Integer marksObtained, Date date) {
        def student = Student.get(studentId);
        if (!student) throw new Exception("StudentManagementSystem.Student not found");

        def subject = Subject.get(subjectId);
        if (!subject) throw new Exception("StudentManagementSystem.Subject not found");

        def mark = new Mark(student: student, subject: subject, marksObtained: marksObtained, date: date);
        mark.save(flush: true)
        return mark
    }

    public List<Mark> listAllMarks() {
        return Mark.list()
    }

    public Mark getMarkById(Long id) {
        def mark = Mark.get(id)
        if (!mark) throw new Exception("StudentManagementSystem.Mark not found")
        return mark
    }

    public Mark updateMark(Long id, Integer marksObtained, Date date) {
        def mark = Mark.get(id)
        if (!mark) throw new Exception("StudentManagementSystem.Mark not found")

        mark.marksObtained = marksObtained
        mark.date = date
        mark.save(flush: true)

        return mark
    }

    boolean deleteMark(Long id) {
        def mark = Mark.get(id)
        if (mark) {
            mark.delete(flush: true)
            return true
        }
        return false
    }
}
