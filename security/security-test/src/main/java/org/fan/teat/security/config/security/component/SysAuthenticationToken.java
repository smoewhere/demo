package org.fan.teat.security.config.security.component;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Collection;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * @version 1.0
 * @author: Fan
 * @date 2020.11.2 14:16
 */
@JsonDeserialize(using = SysTokenDeserializer.class)
public class SysAuthenticationToken extends UsernamePasswordAuthenticationToken {

  public SysAuthenticationToken(Object principal, Object credentials) {
    super(principal, credentials);
  }

  public SysAuthenticationToken(Object principal, Object credentials,
      Collection<? extends GrantedAuthority> authorities) {
    super(principal, credentials, authorities);
  }
}
