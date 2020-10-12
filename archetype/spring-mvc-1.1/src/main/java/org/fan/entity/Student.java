package org.fan.entity;

/**
* @author: Fan
* @version 1.0
* @date  2020.10.12 14:53
*/
public class Student {
    private Integer id;

    private String name;

    private Integer classId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }
}