package org.fan.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.fan.entity.Student;

/**
 * @version 1.0
 * @author: Fan
 * @date 2020.10.12 14:53
 */
public interface StudentMapper {

    /**
     * delete by primary key
     *
     * @param id primaryKey
     * @return deleteCount
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * insert record to table
     *
     * @param record the record
     * @return insert count
     */
    int insert(Student record);

    /**
     * insert record to table selective
     *
     * @param record the record
     * @return insert count
     */
    int insertSelective(Student record);

    /**
     * select by primary key
     *
     * @param id primary key
     * @return object by primary key
     */
    Student selectByPrimaryKey(Integer id);

    /**
     * update record selective
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(Student record);

    /**
     * update record
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(Student record);

    int updateBatch(List<Student> list);

    int updateBatchSelective(List<Student> list);

    int batchInsert(@Param("list") List<Student> list);

    List<Student> selectAll();
}