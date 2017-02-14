package com.sot.fenix.templates.service;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sot.fenix.components.models.Centro;
import com.sot.fenix.components.models.templates.AModelCentro;
import com.sot.fenix.templates.dao.ICentroIdDAO;

@Service
public class ACentroIdService<D extends ICentroIdDAO<I>, I extends AModelCentro> extends ABasicService<D>{

	public Page<I> getByCentro(String centro, Pageable page){
		Centro c=new Centro();
		c.setId(new ObjectId(centro));
		return dao.findByCentro(c, page);
	}
	
	/*public I getByCentroAndNombre(String centro, String nombre){
		Centro c=new Centro();
		c.setId(new ObjectId(centro));
		return dao.findByCentroAndNombre(c, nombre);
	}*/
	

}
