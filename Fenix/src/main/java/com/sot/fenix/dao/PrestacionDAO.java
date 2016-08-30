package com.sot.fenix.dao;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.sot.fenix.components.models.Centro;
import com.sot.fenix.components.models.Prestacion;

@Repository
public interface PrestacionDAO extends MongoRepository<Prestacion, ObjectId>{
	public Page<Prestacion> findByCentro(Centro centro, Pageable pageable);
	public Prestacion findByCentroAndNombre(Centro centro, String nombre);
	public List<Prestacion> findByCentro_id(ObjectId centro);
}
