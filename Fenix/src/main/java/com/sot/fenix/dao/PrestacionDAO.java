package com.sot.fenix.dao;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.sot.fenix.components.models.Prestacion;
import com.sot.fenix.templates.basic.IBasicDAO;

@Repository
public interface PrestacionDAO extends IBasicDAO<Prestacion>{
	@Query("{ horario.id : { $ne : ?0 }} ")
	public List<Prestacion> getSinHorarioGenerico(ObjectId horarioGenId);
}
