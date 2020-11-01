package org.fan.teat.security.config.security.component;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import org.springframework.security.core.userdetails.User;

/**
 * User的
 * @author Fan
 * @version 1.0
 * @date 2020.11.1 12:55
 */
  public class UserDeserializer extends JsonDeserializer<User> {

  @Override
  public User deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
      throws IOException, JsonProcessingException {
    return null;
  }
}
