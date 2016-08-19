package com.sot.fenix.dao;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.sot.fenix.components.models.Cliente;
import com.sot.fenix.components.models.Prestacion;

@Repository
public interface ClienteDAO extends MongoRepository<Cliente, ObjectId>{	
	public Prestacion findByCentro_idAndDni(ObjectId centro, String dni);
}
