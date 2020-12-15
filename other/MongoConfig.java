package org.fan.demo.mongotest.config;

import com.mongodb.AuthenticationMechanism;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.MongoDriverInformation;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import java.util.Arrays;
import java.util.Collections;
import org.bson.UuidRepresentation;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @version 1.0
 * @author: Fan
 * @date 2020.12.2 15:46
 */
@Configuration
@EnableMongoRepositories(basePackages = {"org.fan.demo.mongotest.repository"})
@EnableTransactionManagement
public class MongoConfig {

  @Bean
  public MongoClient mongoClient() {
    MongoCredential credential = MongoCredential.createCredential("fan", "fan", "123456".toCharArray())
        .withMechanism(AuthenticationMechanism.SCRAM_SHA_1);
    MongoClientSettings settings = MongoClientSettings.builder()
        .credential(credential)
        .uuidRepresentation(UuidRepresentation.JAVA_LEGACY)
        .readPreference(ReadPreference.secondary())
        .readConcern(ReadConcern.MAJORITY)
        .writeConcern(WriteConcern.MAJORITY)
        .applyToClusterSettings(builder -> {
          builder.hosts(Arrays.asList(
              new ServerAddress("192.168.10.160", 27017),
              new ServerAddress("192.168.10.219", 27017))
          );
          builder.requiredReplicaSetName("rs1");
        })
        .retryWrites(true)
        .build();
    MongoDriverInformation driverInformation = MongoDriverInformation.builder(MongoDriverInformation.builder().build())
        .driverName("spring-boot")
        .build();
    return MongoClients.create(settings, driverInformation);
  }

  @Bean
  MongoTemplate mongoTemplate(MongoDatabaseFactory factory, MongoConverter converter) {
    return new MongoTemplate(factory, converter);
  }

  /**
   * 自定义的MongoConverter，用于处理POJO和Document之间的转换。
   * <p>
   * 其实MongoDB有自己的PojoCodecProvider类做映射，但是由于spring封装之后，直接使用MongoWrite转为document了。
   * <p>
   * 通过设置{@link DefaultMongoTypeMapper}的typeKey为null省略默认存在的_class子段
   *
   * @param factory     存有MongoDB Database的Factory，默认为{@link SimpleMongoClientDatabaseFactory}
   * @param context     存有MongoDB上下的对象
   * @param conversions 自定义的转换类
   * @return MongoDB映射处理类
   * @see org.bson.codecs.pojo.PojoCodecProvider
   * @see org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory
   * @see org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper
   */
  @Bean
  MappingMongoConverter mappingMongoConverter(MongoDatabaseFactory factory, MongoMappingContext context,
      MongoCustomConversions conversions) {
    DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
    MappingMongoConverter mappingConverter = new MappingMongoConverter(dbRefResolver, context);
    mappingConverter.setCustomConversions(conversions);
    mappingConverter.setTypeMapper(new DefaultMongoTypeMapper(null, context, mappingConverter::getWriteTarget));
    return mappingConverter;
  }

  /**
   * MongoDB的事务管理
   * <p>
   * 在MongoDB4.0之后，支持多文档事务，并且要求MongoDB为主从，单个MongoDB Server不能进行事务操作。
   * <p>
   * 并且不能在不存在的collection上操作事务，即先建collection再增删改查
   *
   * @param dbFactory 存有MongoDB Database的Factory，默认为{@link SimpleMongoClientDatabaseFactory}
   * @return MongoDB事务管理
   */
  @Bean
  MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
    return new MongoTransactionManager(dbFactory);
  }

}
