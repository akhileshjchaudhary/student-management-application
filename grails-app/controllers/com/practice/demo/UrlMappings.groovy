    package com.practice.demo

    class UrlMappings {
        static mappings = {
            delete "/$controller/$id(.$format)?"(action:"delete")
            get "/$controller(.$format)?"(action:"index")
            get "/$controller/$id(.$format)?"(action:"show")
            post "/$controller(.$format)?"(action:"save")
            put "/$controller/$id(.$format)?"(action:"update")
            patch "/$controller/$id(.$format)?"(action:"patch")
            "/"(controller: 'application', action:'index')
            "500"(view:'/error')
            "404"(view:'/notFound')


            "/$controller/$action?/$id?(.$format)?" {
                constraints {
                    // Apply constraints here
                }
            }

            "/api/user"(controller: "auth", action: "user", method: "GET")

            //auth for login
            "/api/login"(controller: "auth", action: "login", method: "POST")

            "/login/page"(controller: 'auth', action: 'loginPage')
            "/my/login"(controller: 'auth', action: 'loginFrontend', method: 'POST')


            //for user
            "/user"(controller: "user", action: "register", method: "POST")
            "/user"(controller: "user", action: "listAll", method: "GET")
            "/user/$id"(controller: "user", action: "getUser", method: "GET")
            "/user/$id"(controller: "user", action: "update", method: "PUT")
            "/user/$id"(controller: "user", action: "delete", method: "DELETE")

            "/users"(controller: "user", action: "index")
            "/users/create"(controller: "user", action: "create")
            "/users/save"(controller: "user", action: "saveUser", method: "POST")
            "/users/delete/$id"(controller: "user", action: "deleteUser")



            // Role
            "/role"(controller: 'role', action: 'create', method: 'POST')
            "/role"(controller: 'role', action: 'getAll', method: 'GET')
            "/role/$id"(controller: 'role', action: 'getById', method: 'GET')
            "/role/$id"(controller: 'role', action: 'update', method: 'PUT')
            "/role/$id"(controller: 'role', action: 'delete', method: 'DELETE')

            "/roles"(controller: 'role', action: 'index')
            "/roles/create"(controller: 'role', action: 'createForm')
            "/roles/save"(controller: 'role', action: 'saveRole', method: 'POST')
            "/roles/delete/$id"(controller: 'role', action: 'deleteRole')

            // Role assignment
            "/assign-role"(controller: 'role', action: 'assignRole', method: 'POST')

            //Teacher
            "/teacher"(controller: "teacher", action: "createTeacher", method: "POST")
            "/teacher"(controller: "teacher", action: "getAllTeachers", method: "GET")
            "/teacher/$id"(controller: "teacher", action: "getTeacherById", method: "GET")
            "/teacher/$id"(controller: "teacher", action: "updateTeacher", method: "PUT")
            "/teacher/$id"(controller: "teacher", action: "deleteTeacherById", method: "DELETE")
            "/teacher/byUsername"(controller: "teacher", action: "getTeacherByUsername", method: "GET")

            "/teachers"(controller: "teacher", action: "index")
            "/teachers/create"(controller: "teacher", action: "create")
            "/teachers/save"(controller: "teacher", action: "save", method: "POST")
            "/teachers/delete/$id"(controller: "teacher", action: "delete")
            "/teacher/edit/$id"(controller: "teacher", action: "editTeacher")
            "/teachers/update"(controller: "teacher", action: "updateTeacherREST", method: "POST")
            "/teachers/dashboard"(controller: "teacher", action: "indexTeacher")


            //Student
            "/student"(controller: "student", action: "createStudent", method: "POST")
            "/student"(controller: "student", action: "getAllStudents", method: "GET")
            "/student/$id"(controller: "student", action: "getStudentById", method: "GET")
            "/student/$id"(controller: "student", action: "updateStudent", method: "PUT")
            "/student/$id"(controller: "student", action: "deleteStudentById", method: "DELETE")
            "/student/user/$userId"(controller: "student", action: "getStudentByUserId", method: "GET")

            "/students"(controller: "student", action: "index")
            "/students/create"(controller: "student", action: "create")
            "/students/edit/$id"(controller: "student", action: "edit")
            "/students/update"(controller: "student", action: "update", method: "POST")
            "/students/save"(controller: "student", action: "saveStudent", method: "POST")
            "/students/delete/$id"(controller: "student", action: "delete", method: "GET")

            //TeacherStudent
            "/teacher-students/assign"(controller: "teacherStudent", action: "assignStudents", method: "POST")
            "/teacher-students/unassign"(controller: "teacherStudent", action: "unassignStudent", method: "POST")
            "/teacher-students/students/$teacherId"(controller: "teacherStudent", action: "studentsForTeacher", method: "GET")
            "/teacher-students/$id/teachers"(controller: "teacherStudent", action: "teachersForStudent", method: "GET")
            "/teacher-students/attendance"(controller: "teacherStudent", action: "takeAttendance", method: "POST")

            "/teacherStudents/"(controller: "teacherStudent", action: "assignForm")
            "/teacherStudents/assign"(controller: "teacherStudent", action: "assign", method: "POST")

            //Attendance
            "/attendance"(controller: "attendance", action: "takeAttendance", method: "POST")
    //        "/attendance/$id"(controller: "attendance", action: "getAttendanceById", method: "GET")
    //        "/attendance"(controller: "attendance", action: "getAllAttendance", method: "GET")
    //        "/attendance/student/$studentId"(controller: "attendance", action: "getAttendanceByStudentId", method: "GET")
            "/attendance/byTeacher/$teacherId"(controller: "attendance", action: "getAttendanceByTeacherId", method: "GET")
    //        "/attendance/date"(controller: "attendance", action: "getAttendanceByDate", method: "GET")
    //        "/attendance/$id"(controller: "attendance", action: "updateAttendance", method: "PUT")
    //        "/attendance/$id"(controller: "attendance", action: "deleteAttendanceById", method: "DELETE")


            "/attendances/create"(controller: "attendance", action: "create")
            "/attendances/save"(controller: "attendance", action: "save", method: "POST")

            //Subject
            "/subject"(controller: "subject", action: "createSubject", method: "POST")
            "/subject"(controller: "subject", action: "listAllSubjects", method: "GET")
            "/subject/$id"(controller: "subject", action: "getSubjectById", method: "GET")
            "/subject/$id"(controller: "subject", action: "updateSubject", method: "PUT")
            "/subject/$id"(controller: "subject", action: "deleteSubject", method: "DELETE")

            "/subjects"(controller: "subject", action: "index")
            "/subjects/create"(controller: "subject", action: "create")
            "/subjects/edit/$id"(controller: "subject", action: "edit")
            "/subjects/save"(controller: "subject", action: "saveSubject", method: "POST")
            "/subjects/update"(controller: "subject", action: "update", method: "POST")
            "/subjects/delete/$id"(controller: "subject", action: "delete")

            //Mark
            "/mark"(controller: "mark", action: "create", method: "POST")
            "/mark"(controller: "mark", action: "list", method: "GET")
            "/mark/$id"(controller: "mark", action: "show", method: "GET")
            "/mark/$id"(controller: "mark", action: "update", method: "PUT")
            "/mark/$id"(controller: "mark", action: "delete", method: "DELETE")

            "/marks"(controller: "mark", action: "index")
            "/marks/create"(controller: "mark", action: "createMark")
            "/marks/edit$id"(controller: "mark", action: "edit")
            "/marks/delete$id"(controller: "mark", action: "deleteMark")
            "/marks/save"(controller: "mark", action: "save", method: "POST")
            "/marks/update"(controller: "mark", action: "updateMark", method: "POST")
        }
    }
