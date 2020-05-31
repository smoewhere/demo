package org.fan.demo.dynamic.config.properties;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

/**
 * @author Fan
 * @version 1.0
 * @date 2020.5.31 19:11
 */
public class CompositePropertySourceFactory extends DefaultPropertySourceFactory {
    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
        String filename = resource.getResource().getFilename();
        String sourceName = Optional.ofNullable(name).orElse(filename);
        if (!resource.getResource().exists()) {
            // return an empty Properties
            return new PropertiesPropertySource(sourceName, new Properties());
        } else if (filename != null && (filename.endsWith(".yml") || filename.endsWith(".yaml"))) {
            Properties propertiesFromYaml = loadYaml(resource);
            return new PropertiesPropertySource(sourceName, propertiesFromYaml);
        } else {
            return super.createPropertySource(name, resource);
        }
    }

    /**
     * load yaml file to properties
     *
     * @param resource
     * @return
     * @throws IOException
     */
    private Properties loadYaml(EncodedResource resource) throws IOException {
        YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
        factory.setResources(resource.getResource());
        factory.afterPropertiesSet();
        return factory.getObject();
    }
}
