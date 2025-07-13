package com.practice.demo


import grails.gorm.transactions.Transactional

@Transactional
class AttendanceService {

    def takeAttendance(Teacher teacher, Map<Long, String> studentStatusMap, Date date) {
        if (teacher == null || studentStatusMap == null || date == null) {
            return;
        }

        List<TeacherStudent> assignedList = TeacherStudent.findAllByTeacher(teacher);
        Set<Long> allowedStudentIds = new HashSet<>();
        for (TeacherStudent ts : assignedList) {
            allowedStudentIds.add(ts.student.id);
        }

        for (Map.Entry<Long, String> entry : studentStatusMap.entrySet()) {
            Long studentId = Long.parseLong(entry.getKey().toString());
            String status = entry.getValue();

            if (!allowedStudentIds.contains(studentId)) {
                continue;
            }

            Student student = Student.get(studentId);
            if (student == null) {
                continue;
            }

            Attendance existing = Attendance.findByTeacherAndStudentAndDate(teacher, student, date);
            if (existing == null) {
                Attendance attendance = new Attendance();
                attendance.teacher = teacher;
                attendance.student = student;
                attendance.date = date;
                attendance.status = status;
                attendance.save(flush: true, failOnError: true);
            }
        }
    }

//    public StudentManagementSystem.Attendance getAttendanceById(Long id) {
//        return StudentManagementSystem.Attendance.get(id)
//    }
//
//    public List<StudentManagementSystem.Attendance> getAllAttendance() {
//        return StudentManagementSystem.Attendance.list()
//    }
//
//    public List<StudentManagementSystem.Attendance> getAttendanceByStudentId(Long studentId) {
//        StudentManagementSystem.Student student = StudentManagementSystem.Student.get(studentId)
//        if (student == null) return []
//        return StudentManagementSystem.Attendance.findAllByStudent(student)
//    }
//
    def getAttendanceByTeacherId(Long teacherId) {
        Teacher teacher = Teacher.get(teacherId)
        if (!teacher) {
            throw new IllegalArgumentException("Teacher not found for ID: ${teacherId}")
        }
        def attendanceRecords = Attendance.findAllByTeacher(teacher) ?: []
        println "Fetched ${attendanceRecords.size()} attendance records for teacher ID: ${teacherId}"
        return attendanceRecords.collect {
            [
                    id     : it.id,
                    teacher: [id: it.teacher?.id, name: it.teacher?.name ?: 'Unknown'],
                    student: [id: it.student?.id, name: it.student?.name ?: 'Unknown'],
                    date   : it.date ? new java.text.SimpleDateFormat("yyyy-MM-dd").format(it.date) : null,
                    status : it.status ?: 'Unknown'
            ]
        }
    }
//
//    public List<StudentManagementSystem.Attendance> getAttendanceByDate(Date date) {
//        return StudentManagementSystem.Attendance.findAllByDate(date)
//    }
//
//    public StudentManagementSystem.Attendance updateAttendance(Long id, String status) {
//        StudentManagementSystem.Attendance attendance = StudentManagementSystem.Attendance.get(id)
//        if (attendance == null) {
//            throw new IllegalArgumentException("StudentManagementSystem.Attendance not found")
//        }
//
//        attendance.status = status
//        attendance.save(flush: true, failOnError: true)
//        return attendance
//    }
//
//    public boolean deleteAttendanceById(Long id) {
//        StudentManagementSystem.Attendance attendance = StudentManagementSystem.Attendance.get(id)
//        if (attendance == null) return false
//
//        attendance.delete(flush: true)
//        return true
//    }
}
