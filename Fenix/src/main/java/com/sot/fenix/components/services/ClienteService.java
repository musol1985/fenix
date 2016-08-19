package com.sot.fenix.components.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
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
		Query q=TextQuery.queryText(new TextCriteria().matchingPhrase(texto)).addCriteria(Criteria.where("centro.id").);
		return template.find(q, Cliente.class);
	}
	
	public ClienteDAO getDAO(){
		return dao;
	}
}
