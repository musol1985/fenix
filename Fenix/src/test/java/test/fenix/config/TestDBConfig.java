package test.fenix.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.MongoClient;

@Configuration
@EnableMongoRepositories("com.sot.fenix.dao")
public class TestDBConfig {
 
	 @Bean
	 public MongoTemplate mongoTemplate() throws Exception {
		 return new MongoTemplate(new MongoClient("localhost"), "FenixTest");
	 }
 
}
/*

@Configuration
@EnableCassandraRepositories(basePackages = { "com.sot.fenix.dao" })
public class TestDBConfig extends AbstractCassandraConfiguration {

    @Override
    public String getKeyspaceName() {
        return "test_config";
    }

    @Override
    public SchemaAction getSchemaAction() {
        return SchemaAction.CREATE_IF_NOT_EXISTS;
    }

    @Bean
    public CassandraOperations cassandraOperations() throws Exception {
        return new CassandraTemplate(session().getObject());
    }

}*/
