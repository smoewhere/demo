package org.fan.demo.hibernate.entity;

import lombok.Data;

@Data
public class Student {
    /**
     * ID，唯一主键，自增
     */
    private Integer id;

    /**
     * 姓名
     */
    private String name;

    /**
     *
     */
    private Integer classId;
}

