package com.practice.demo

class Teacher {
    User user
    String name
    String phoneNumber

    static belongsTo = [user: User]

    static constraints = {
        user unique: true, nullable: false
        name blank: false
        phoneNumber blank: false
    }

}
