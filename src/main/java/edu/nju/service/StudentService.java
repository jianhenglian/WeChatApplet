package edu.nju.service;

import edu.nju.domain.Student;

import java.util.List;

/**
 * @author LianJianheng
 * @version 1.0.0
 * @createTime 2022/4/5
 * @Description TODO
 */
public interface StudentService
{
    int addStudent(Student student);
    int deleteStudent(int id);
    int updateStudent(Student student);
    Student selectStudentById(int id);
    List<Student> selectAllStudents();
}
