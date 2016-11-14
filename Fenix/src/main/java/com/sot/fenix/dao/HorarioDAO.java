package com.sot.fenix.dao;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.sot.fenix.components.models.horarios.Horario;
import com.sot.fenix.templates.basic.IBasicDAO;


@Repository
public interface HorarioDAO extends IBasicDAO<Horario>{
	@Query("{ centro : '?0' , generico: true } ")
	public Horario getGenerico(ObjectId centro);
}
