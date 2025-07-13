package com.practice.demo

class Attendance {
    Student student
    Teacher teacher
    Date date
    String status

    static constraints = {
        student nullable: false
        teacher nullable: false
        date nullable: false
        status inList: ['Present', 'Absent', 'Leave']
    }

    static mapping = {
        table 'attendance'
        id column: 'id'
        version false
        student lazy: false
        teacher lazy: false
        date index: 'attendance_date_idx'
        uniqueConstraints 'student, teacher, date'
    }
}
