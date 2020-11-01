package org.fan.teat.security.config.security.component;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author Fan
 * @version 1.0
 * @date 2020.11.1 13:11
 */
public class SysUserDetail extends User {

  @JsonCreator
  public SysUserDetail(@JsonProperty("username") String username,@JsonProperty("password") String password,
      Collection<? extends GrantedAuthority> authorities) {
    super(username, password, authorities);
  }

  public SysUserDetail(String username, String password, boolean enabled, boolean accountNonExpired,
      boolean credentialsNonExpired,
      boolean accountNonLocked,
      Collection<? extends GrantedAuthority> authorities) {
    super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
  }
}
