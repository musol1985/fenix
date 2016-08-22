package com.sot.fenix.dao;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.sot.fenix.components.models.Cliente;

@Repository
public interface ClienteDAO extends MongoRepository<Cliente, ObjectId>{	
	public Cliente findByCentro_idAndDni(ObjectId centro, String dni);

}
