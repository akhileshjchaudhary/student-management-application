package com.practice.demo

class Student {
    User user
    String name
    Date dob
    String address

    static belongsTo = [user: User]

    static constraints = {

        user unique: true
        name blank: false
        dob nullable: false
        address nullable: true
    }
    static mapping = {
        version false
        user column: "user_id"
    }
}
