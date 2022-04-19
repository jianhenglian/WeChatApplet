package edu.nju.controller;

import edu.nju.domain.Student;
import edu.nju.service.StudentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author LianJianheng
 * @version 1.0.0
 * @createTime 2022/4/5
 * @Description TODO
 */
@RestController
public class StudentController
{
    @Resource
    private StudentService service;

    @GetMapping("/add/{id}/{name}/{age}/{gender}")
    public String addStudent(@PathVariable("name") String name,
                             @PathVariable("id") Integer id,
                             @PathVariable("age") Integer age,
                             @PathVariable("gender") String gender)
    {
        Student s = new Student();
        s.setName(name);
        s.setAge(age);
        s.setGender(gender);
        s.setId(id);
        int result = service.addStudent(s);
        return "" + result;
    }

    @GetMapping("/update/{id}/{name}/{age}/{gender}")
    public String updateStudent(@PathVariable("name") String name,
                             @PathVariable("id") Integer id,
                             @PathVariable("age") Integer age,
                             @PathVariable("gender") String gender)
    {
        Student s = new Student();
        s.setName(name);
        s.setAge(age);
        s.setGender(gender);
        s.setId(id);
        int result = service.updateStudent(s);
        return "" + result;
    }

    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable("id") Integer id)
    {
        int result = service.deleteStudent(id);
        return  "" + result;
    }
    @GetMapping("/select/{id}")
    public Student selectStudentById(@PathVariable("id") Integer id)
    {
        return service.selectStudentById(id);
    }

    @GetMapping("/select")
    public List<Student> selectAllStudent()
    {
        return service.selectAllStudents();
    }

}
