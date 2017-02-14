package com.sot.fenix.templates.service;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.sot.fenix.components.models.Centro;
import com.sot.fenix.components.models.templates.AModelNombre;
import com.sot.fenix.templates.dao.INombreCentroIdDAO;

@Service
public class ANombreCentroIdService<D extends INombreCentroIdDAO<I>, I extends AModelNombre> extends ACentroIdService<D, I>{
	
	public I getByCentroAndNombre(String centro, String nombre){
		Centro c=new Centro();
		c.setId(new ObjectId(centro));
		return getDAO().findByCentroAndNombre(c, nombre);
	}
	
}
