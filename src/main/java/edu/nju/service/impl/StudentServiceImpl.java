package edu.nju.service.impl;

import edu.nju.dao.StudentDao;
import edu.nju.domain.Student;
import edu.nju.service.StudentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author LianJianheng
 * @version 1.0.0
 * @createTime 2022/4/5
 * @Description TODO
 */
@Service
public class StudentServiceImpl implements StudentService
{
    @Resource
    private StudentDao studentDao;
    @Override
    public int addStudent(Student student)
    {
        return studentDao.addStudent(student);
    }

    @Override
    public int deleteStudent(int id)
    {
        return studentDao.deleteStudent(id);
    }

    @Override
    public int updateStudent(Student student)
    {
        return studentDao.updateStudent(student);
    }

    @Override
    public Student selectStudentById(int id)
    {
        return studentDao.selectStudentById(id);
    }

    @Override
    public List<Student> selectAllStudents()
    {
        return studentDao.selectAllStudents();
    }
}
