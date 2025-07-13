package com.practice.demo

import grails.gorm.transactions.Transactional

@Transactional
class RoleService {

    Role createRole(String authority) {
        if (!authority?.trim()) {
            return null
        }

        Role role = Role.findByAuthority(authority)
        if (!role) {
            role = new Role(authority: authority)
            if (!role.save(flush: true)) {
                return null
            }
        }

        return role
    }

    Map assignRoleToUser(String username, String role) {
        def user = User.findByUsername(username)
        def roleObj = Role.findByAuthority(role)

        if (!user || !roleObj) {
            return [status: 404, body: [message: 'User or role not found']]
        }

        if (UserRole.exists(user.id, roleObj.id)) {
            return [status: 400, body: [message: 'Role already assigned']]
        }

        UserRole.create(user, roleObj, true)
        return [status: 200, body: [message: "Assigned ${roleObj.authority} to ${user.username}"]]
    }

    Role getById(Long id) {
        return Role.get(id)
    }

    List<Role> getAll() {
        return Role.list()
    }

    Role updateRole(Long id, String newAuthority) {
        def role = Role.get(id)
        if (!role) return null

        role.authority = newAuthority
        role.save(flush: true)
        return role
    }

    boolean deleteRole(Long id) {
        def role = Role.get(id)
        if (!role) return false

        // Delete all UserRole associations for this role
        UserRole.removeAll(role)

        // Now delete the role itself
        role.delete(flush: true)
        return true
    }

}
