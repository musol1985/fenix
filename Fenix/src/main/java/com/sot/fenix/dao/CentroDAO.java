package com.sot.fenix.dao;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.sot.fenix.components.models.Centro;
import com.sot.fenix.components.models.Usuario;

@Repository
public interface CentroDAO extends MongoRepository<Centro, ObjectId>{
	public Centro findByCorreoAdmin(String correo);
}
