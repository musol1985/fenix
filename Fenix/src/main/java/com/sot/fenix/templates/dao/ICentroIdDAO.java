package com.sot.fenix.templates.dao;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sot.fenix.components.models.Centro;
import com.sot.fenix.components.models.templates.AModelCentro;

public interface ICentroIdDAO<I extends AModelCentro> extends IBasicIdDAO<I>{
	public Page<I> findByCentro(Centro centro, Pageable pageable);
	public List<I> findByCentro_id(ObjectId centro);
}
