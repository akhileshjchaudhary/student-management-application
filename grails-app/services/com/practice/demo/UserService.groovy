package com.practice.demo

import grails.gorm.transactions.Transactional

@Transactional
class UserService {

    User createUser(String username, String password) {
        User user = new User(username: username, password: password, enabled: true)

        if (user.save(flush: true) != null) {
            return user
        } else {
            return null
        }
    }

    List<User> getAllUsers() {
        return User.list()
    }

    User getUserById(Long id) {
        return User.get(id)
    }

    User updateUser(Long id, String newUsername, String newPassword) {
        User user = User.get(id)

        if (user != null) {
            if (newUsername != null && !newUsername.trim().isEmpty()) {
                user.username = newUsername
            }

            if (newPassword != null && !newPassword.trim().isEmpty()) {
                user.password = newPassword
            }

            if (user.save(flush: true) != null) {
                return user
            }
        }

        return null
    }

    boolean deleteUser(Long id) {
        User user = User.get(id)

        if (user != null) {
            user.delete(flush: true)
            return true
        }

        return false
    }
}
