package com.sot.fenix.dao;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.sot.fenix.components.models.Cliente;
import com.sot.fenix.templates.dao.INombreCentroIdDAO;

@Repository
public interface ClienteDAO extends INombreCentroIdDAO<Cliente>{	
	public Cliente findByCentro_idAndDni(ObjectId centro, String dni);

}
