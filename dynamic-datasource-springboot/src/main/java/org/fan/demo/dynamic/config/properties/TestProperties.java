package org.fan.demo.dynamic.config.properties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Fan
 * @version 1.0
 * @date 2020.5.31 15:01
 */
@Component
@PropertySource(value = "classpath:config/testmap.yaml", factory = CompositePropertySourceFactory.class)
@ConfigurationProperties(prefix = "test")
@Data
@Order(5)
public class TestProperties {

    private String name;
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
