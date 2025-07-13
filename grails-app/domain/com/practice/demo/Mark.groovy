package com.practice.demo

class Mark {
    Student student
    Subject subject
    Integer marksObtained
    Date date

    static constraints = {
        student nullable: false
        subject nullable: false
        marksObtained range: 0..100
        date nullable: false
    }

    static mapping = {
        table 'mark'
        version false
        student lazy: false
        subject lazy: false
        date index: 'mark_date_idx'
        uniqueConstraints 'student, subject, date'
    }
}
