package edu.nju.domain;

/**
 * @author LianJianheng
 * @version 1.0.0
 * @createTime 2022/4/5
 * @Description TODO
 */
public class Student
{
    private String name;
    private int id;
    private int age;
    private String gender;

    @Override
    public String toString()
    {
        return "Student{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                '}';
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getAge()
    {
        return age;
    }

    public void setAge(int age)
    {
        this.age = age;
    }

    public String getGender()
    {
        return gender;
    }

    public void setGender(String gender)
    {
        this.gender = gender;
    }
}
