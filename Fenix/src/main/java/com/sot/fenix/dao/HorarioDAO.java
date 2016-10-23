package com.sot.fenix.dao;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.sot.fenix.components.models.Horario;

@Repository
public interface HorarioDAO extends MongoRepository<Horario, ObjectId>{	
	
}
