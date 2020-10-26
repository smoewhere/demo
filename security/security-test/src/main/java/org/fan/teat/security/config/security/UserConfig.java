package org.fan.teat.security.config.security;

import javax.annotation.Resource;
import org.fan.teat.security.model.SysUser;
import org.fan.teat.security.service.SysUserService;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * @author Fan
 * @version 1.0
 * @date 2020.10.18 21:18
 */
@Component
public class UserConfig implements UserDetailsService {

  @Resource
  private SysUserService sysUserService;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    SysUser sysUser = sysUserService.selectAllByLoginName(username);
    if (sysUser == null) {
      throw new UsernameNotFoundException("user:{}" + username + "not found");
    }
    return new User(sysUser.getUserName(), sysUser.getPassword(), AuthorityUtils.createAuthorityList("1"));
  }
}
