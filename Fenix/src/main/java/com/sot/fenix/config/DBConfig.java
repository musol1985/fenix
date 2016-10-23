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
import com.sot.fenix.components.models.Cliente;
import com.sot.fenix.components.models.HorarioOld;
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
		//c.setHorario(HorarioOld.getGenerico());
		
		db.insert(c);
		
		Usuario u=new Usuario();
		u.setCorreo("root");
		u.setNombre("root");
		u.setPassword("16f84a#16f84a");
		u.setPerfil(PERFILES.ROOT);
		u.setCentro(c);
		
		db.insert(u);
		
		Cliente c3=new Cliente();c3.setId(null);c3.setNombre("juan2");c3.setApellidos("Martinc3 Garcia");c3.setCentro(c);c3.setCorreo("juan@gmail.com");c3.setDni("123");c3.setTelefono("6123");
		db.insert(c3);
		c3=new Cliente();c3.setId(null);c3.setNombre("juan3");c3.setApellidos("ZMartinc3 Garcia");c3.setCentro(c);c3.setCorreo("juan@gmail.com");c3.setDni("1234");c3.setTelefono("6123");
		db.insert(c3);
		c3=new Cliente();c3.setId(null);c3.setNombre("juan4");c3.setApellidos("Martinc3 Garcia");c3.setCentro(c);c3.setCorreo("juan@gmail.com");c3.setDni("1223");c3.setTelefono("6123");
		db.insert(c3);
	}

}