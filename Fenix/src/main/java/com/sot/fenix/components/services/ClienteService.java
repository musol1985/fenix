package com.sot.fenix.components.services;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.MongoRegexCreator;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.query.parser.Part;
import org.springframework.stereotype.Service;

import com.sot.fenix.components.models.Cliente;
import com.sot.fenix.dao.ClienteDAO;

@Service
public class ClienteService {
	@Autowired
	private ClienteDAO dao;
	@Autowired
	private MongoTemplate template;
	
	
	public List<Cliente> buscar(String texto, String centro){
		
		String regexp=MongoRegexCreator.INSTANCE.toRegularExpression(texto, Part.Type.EXISTS);
		Query q=new Query(Criteria.where("centro.id").is(new ObjectId(centro)).orOperator(
				Criteria.where("dni").regex(regexp, "i"),	
				Criteria.where("nombre").regex(regexp, "i"),				
				Criteria.where("apellido1").regex(regexp, "i"),
				Criteria.where("apellido2").regex(regexp, "i"),
				Criteria.where("telefono").regex(regexp, "i")));			
		
		
		return template.find(q, Cliente.class);
	}
	
	public ClienteDAO getDAO(){
		return dao;
	}
}
