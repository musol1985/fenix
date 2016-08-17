package com.sot.fenix.components.services;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sot.fenix.components.models.Centro;
import com.sot.fenix.components.models.Prestacion;
import com.sot.fenix.dao.PrestacionDAO;

@Service
public class PrestacionService {
	@Autowired
	private PrestacionDAO prestaciones;
	
	
	public Page<Prestacion> getByCentro(String centro, Pageable page){
		Centro c=new Centro();
		c.setId(new ObjectId(centro));
		return prestaciones.findByCentro(c, page);
	}
	
	public Prestacion getByCentroId(String centro, ObjectId id){
		Centro c=new Centro();
		c.setId(new ObjectId(centro));
		return prestaciones.findByCentroAndId(c, id);
	}
	
	public PrestacionDAO getDAO(){
		return prestaciones;
	}
}
