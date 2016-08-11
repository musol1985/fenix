package com.sot.fenix.config;

import org.bson.types.ObjectId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.MongoClient;
import com.sot.fenix.components.models.Usuario;
import com.sot.fenix.components.models.Perfil.PERFILES;

@Configuration
@EnableMongoRepositories("com.sot.fenix.dao")
public class DBConfig {

	@Bean
	public MongoDbFactory mongoDbFactory() throws Exception {
		return new SimpleMongoDbFactory(new MongoClient("localhost"), "fenixDB");
	}

	@Bean
	public MongoTemplate mongoTemplate() throws Exception {
		
		MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());

		if(!mongoTemplate.collectionExists(Usuario.class) || mongoTemplate.getDb().getCollection("usuario").count()==0)
			initDB(mongoTemplate);
				
		return mongoTemplate;
		
	}
	
	
	private void initDB(MongoTemplate db){
		System.out.println("***************************************");
		System.out.println("*		La BD es nueva		   		  *");
		System.out.println("***************************************");
		Usuario u=new Usuario();
		u.setUsername("root");
		u.setNombre("root");
		u.setPassword("16f84a#16f84a");
		u.setPerfil(PERFILES.ROOT);
		
		db.insert(u);
	}

}