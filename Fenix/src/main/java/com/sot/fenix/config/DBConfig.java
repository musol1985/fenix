package com.sot.fenix.config;

import java.util.ArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.MongoClient;
import com.sot.fenix.components.models.Centro;
import com.sot.fenix.components.models.Perfil.PERFILES;
import com.sot.fenix.components.models.Usuario;

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
		
		Centro c=new Centro();
		c.setNombre("Centro ADMIN");
		c.setCorreoAdmin("root");
		c.setColor("teal");
		
		db.insert(c);
		
		Usuario u=new Usuario();
		u.setCorreo("root");
		u.setNombre("root");
		u.setPassword("16f84a#16f84a");
		u.setPerfil(PERFILES.ROOT);
		u.setCentro(c);
		
		db.insert(u);
	}

}