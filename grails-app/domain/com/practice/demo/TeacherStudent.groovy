package com.practice.demo

class TeacherStudent implements Serializable {

    Teacher teacher
    Student student

    static constraints = {
        teacher nullable: false
        student nullable: false, unique: ['teacher']
    }

    static mapping = {
        id composite: ['teacher', 'student']
    }
}
