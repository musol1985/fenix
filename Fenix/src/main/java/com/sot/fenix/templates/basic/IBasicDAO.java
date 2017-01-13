package com.sot.fenix.templates.basic;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.sot.fenix.components.models.Centro;
import com.sot.fenix.components.models.Prestacion;
import com.sot.fenix.components.models.templates.AModelBasic;

public interface IBasicDAO<I extends AModelBasic> extends MongoRepository<I, ObjectId>{
	public Page<I> findByCentro(Centro centro, Pageable pageable);
	public I findByCentroAndNombre(Centro centro, String nombre);
	public List<I> findByCentro_id(ObjectId centro);
}
