package com.practice.demo

class Subject {
    String name
    String code
    Teacher teacher

    static constraints = {
        name blank: false
        code blank: false, unique: true
        teacher nullable: false
    }
}
