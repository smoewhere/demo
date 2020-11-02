package org.fan.teat.security.config.security.component;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.util.HashSet;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * User的
 *
 * @author Fan
 * @version 1.0
 * @date 2020.11.1 12:55
 */
public class UserDeserializer extends JsonDeserializer<SysUserDetail> {


  @Override
  public SysUserDetail deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    JsonNode root = p.getCodec().readTree(p);
    String username = root.get("username").asText("");
    boolean accountNonExpired = root.get("accountNonExpired").asBoolean(false);
    boolean enabled = root.get("enabled").asBoolean(false);
    boolean credentialsNonExpired = root.get("credentialsNonExpired").asBoolean(false);
    boolean accountNonLocked = root.get("accountNonLocked").asBoolean(false);
    HashSet<GrantedAuthority> authoritiesList = new HashSet<>();
    JsonNode authorities = root.get("authorities");
    if (!authorities.isEmpty() && authorities.isArray()) {
      for (JsonNode jsonNode : authorities) {
        String text = jsonNode.get("authority").asText();
        if (StringUtils.isNotBlank(text)) {
          authoritiesList.add(new SimpleGrantedAuthority(text));
        }
      }
    }
    return new SysUserDetail(username, "", enabled, accountNonExpired, credentialsNonExpired, accountNonLocked,
        authoritiesList);
  }
}
