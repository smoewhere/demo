package org.fan.teat.security.service;

import org.fan.teat.security.model.SysUser;
import java.util.List;
    /**
 * @author  Fan
 * @date  2020.10.18 21:21
 * @version 1.0
 */
public interface SysUserService{


    int deleteByPrimaryKey(String userId);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(String userId);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    List<SysUser> selectAll();

    SysUser selectAllByLoginName(String loginName);

    int updateBatch(List<SysUser> list);

    int updateBatchSelective(List<SysUser> list);

    int batchInsert(List<SysUser> list);

}
