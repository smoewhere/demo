package org.fan.teat.security.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.fan.teat.security.model.SysUser;

/**
 * @author  Fan
 * @date  2020.10.18 21:21
 * @version 1.0
 */
public interface SysUserMapper {
    int deleteByPrimaryKey(String userId);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(String userId);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    List<SysUser> selectAll();

    SysUser selectAllByLoginName(@Param("loginName") String loginName);

    int updateBatch(List<SysUser> list);

    int updateBatchSelective(List<SysUser> list);

    int batchInsert(@Param("list") List<SysUser> list);
}