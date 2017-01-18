package com.sot.fenix.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.sot.fenix.components.models.Centro;
import com.sot.fenix.components.models.Cliente;
import com.sot.fenix.components.models.Perfil.PERFILES;
import com.sot.fenix.components.models.Usuario;
import com.sot.fenix.components.models.horarios.Horario;
import com.sot.fenix.components.models.horarios.HorarioEditor;
import com.sot.fenix.components.services.UsuarioService;

@Configuration
@EnableMongoRepositories("com.sot.fenix.dao")
@EnableMongoAuditing
public class DBConfig extends AbstractMongoConfiguration{
	
	@Autowired
	private UsuarioService usuarios;

	@Override
	protected String getDatabaseName() {
		return "fenixDB";
	}

	@Override
	public Mongo mongo() throws Exception {
		return new MongoClient("localhost");
	}
	
	@Override
	protected String getMappingBasePackage() {
	    return "com.sot.fenix.components.models";
	}
	
	@Bean
    public AuditorAware<Usuario> myAuditorProvider() {
        return new AuditorAware<Usuario>(){
			@Override
			public Usuario getCurrentAuditor() {				
				return usuarios.getCurrentUsuario();
			}
        	
        };
    }

	@Bean
	public MongoTemplate mongoTemplate() throws Exception {
		
		MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());

		if(!mongoTemplate.collectionExists(Usuario.class) || mongoTemplate.getDb().getCollection("usuario").count()==0){
			initDB(mongoTemplate);
		}else{
			//TODO proceso para revisar integridad BD
			System.out.println("Lanzando proceso de comprobación de integridad de BD!!!!");
		}
				
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
		
		Horario h=Horario.getGenerico(c);
		h.setCentro(c);
		db.insert(h);
		HorarioEditor e=HorarioEditor.getGenerico(h);
		db.insert(e);
		
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