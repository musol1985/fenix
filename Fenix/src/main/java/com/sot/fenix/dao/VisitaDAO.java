package com.sot.fenix.dao;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import com.sot.fenix.components.models.Visita;
import com.sot.fenix.templates.basic.IBasicDAO;

@Repository
public interface VisitaDAO extends IBasicDAO<Visita>{	
	public Visita findByCita_id(ObjectId citaId);
}
