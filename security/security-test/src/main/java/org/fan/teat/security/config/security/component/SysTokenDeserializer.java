package org.fan.teat.security.config.security.component;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import org.fan.teat.security.utils.JsonUtil;

/**
 * @version 1.0
 * @author: Fan
 * @date 2020.11.2 14:17
 */
public class SysTokenDeserializer extends JsonDeserializer<SysAuthenticationToken> {


  @Override
  public SysAuthenticationToken deserialize(JsonParser p, DeserializationContext ctxt)
      throws IOException, JsonProcessingException {
    JsonNode root = p.getCodec().readTree(p);
    boolean authenticated = root.get("authenticated").asBoolean(false);
    if (!authenticated) {
      return new SysAuthenticationToken(null, null);
    }
    JsonNode details = root.get("details");
    WebRequestAuthenticationDetails webDetails = null;
    if (!details.isNull() && !details.isEmpty()) {
      webDetails = JsonUtil.fromJson(details.toString(), WebRequestAuthenticationDetails.class);
    }
    JsonNode principalNode = root.get("principal");
    SysUserDetail principal = null;
    if (!principalNode.isNull() && !principalNode.isEmpty()) {
      principal = JsonUtil.fromJson(principalNode.toString(), SysUserDetail.class);
    }
    if (principal == null) {
      return new SysAuthenticationToken(principal, null);
    }
    SysAuthenticationToken authenticationToken = new SysAuthenticationToken(principal, null,
        principal.getAuthorities());
    authenticationToken.setDetails(webDetails);
    return authenticationToken;
  }
}
