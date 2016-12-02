package com.sot.fenix.components.services;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.sot.fenix.components.json.CitasRequest;
import com.sot.fenix.components.models.Cita;
import com.sot.fenix.dao.CitaDAO;

@Service
public class CitaService {
	@Autowired
	private CitaDAO dao;
	@Autowired
	private MongoTemplate template;
	

	public List<Cita> getCitasSolapa(Cita cita){
		List<Cita> solapas=new ArrayList<Cita>();
		
		solapas.addAll(dao.findByFechaIniAndFechaFinAndCentro_idAndProfesional_idAndPrestacion_id(cita.getFechaIni(), cita.getFechaFin(), cita.getCentro().getId(), cita.getProfesional().getId(), cita.getPrestacion().getId()));		
		solapas.addAll(dao.findByFechaIniLessThanAndFechaFinGreaterThanAndCentro_idAndProfesional_idAndPrestacion_id(cita.getFechaIni(), cita.getFechaIni(), cita.getCentro().getId(), cita.getProfesional().getId(), cita.getPrestacion().getId()));
		solapas.addAll(dao.findByFechaIniLessThanAndFechaFinGreaterThanAndCentro_idAndProfesional_idAndPrestacion_id(cita.getFechaFin(), cita.getFechaFin(), cita.getCentro().getId(), cita.getProfesional().getId(), cita.getPrestacion().getId()));
		
		return solapas;
	}
	
	
	public List<Cita> buscar(CitasRequest req){
		if(req.profesional!=null && req.prestacion!=null){
			return dao.findByFechaIniGreaterThanEqualAndFechaFinLessThanEqualAndCentro_idAndProfesional_idAndPrestacion_id(req.start, req.end, new ObjectId(req.centro), new ObjectId(req.profesional), new ObjectId(req.prestacion));
		}else if(req.profesional!=null){
			return dao.findByFechaIniGreaterThanEqualAndFechaFinLessThanEqualAndCentro_idAndProfesional_id(req.start, req.end, new ObjectId(req.centro), new ObjectId(req.profesional));
		}else if(req.prestacion!=null){
			return dao.findByFechaIniGreaterThanEqualAndFechaFinLessThanEqualAndCentro_idAndPrestacion_id(req.start, req.end, new ObjectId(req.centro), new ObjectId(req.prestacion));
		}else{
			return dao.findByFechaIniGreaterThanEqualAndFechaFinLessThanEqualAndCentro_id(req.start, req.end, new ObjectId(req.centro));
		}
	}
	
	public CitaDAO getDAO(){
		return dao;
	}
}
