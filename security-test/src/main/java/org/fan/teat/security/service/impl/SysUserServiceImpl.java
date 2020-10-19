package org.fan.teat.security.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.fan.teat.security.model.SysUser;
import org.fan.teat.security.mapper.SysUserMapper;
import java.util.List;
import org.fan.teat.security.service.SysUserService;
/**
 * @author  Fan
 * @date  2020.10.18 21:21
 * @version 1.0
 */
@Service("sysUserService")
public class SysUserServiceImpl implements SysUserService{

    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    public int deleteByPrimaryKey(String userId) {
        return sysUserMapper.deleteByPrimaryKey(userId);
    }

    @Override
    public int insert(SysUser record) {
        return sysUserMapper.insert(record);
    }

    @Override
    public int insertSelective(SysUser record) {
        return sysUserMapper.insertSelective(record);
    }

    @Override
    public SysUser selectByPrimaryKey(String userId) {
        return sysUserMapper.selectByPrimaryKey(userId);
    }

    @Override
    public int updateByPrimaryKeySelective(SysUser record) {
        return sysUserMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(SysUser record) {
        return sysUserMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<SysUser> selectAll() {
        return sysUserMapper.selectAll();
    }

    @Override
    public SysUser selectAllByLoginName(String loginName) {
        return sysUserMapper.selectAllByLoginName(loginName);
    }

    @Override
    public int updateBatch(List<SysUser> list) {
        return sysUserMapper.updateBatch(list);
    }

    @Override
    public int updateBatchSelective(List<SysUser> list) {
        return sysUserMapper.updateBatchSelective(list);
    }

    @Override
    public int batchInsert(List<SysUser> list) {
        return sysUserMapper.batchInsert(list);
    }

}
