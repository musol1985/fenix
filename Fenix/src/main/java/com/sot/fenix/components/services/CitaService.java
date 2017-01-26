package com.sot.fenix.components.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.sot.fenix.components.exceptions.ExceptionREST;
import com.sot.fenix.components.json.CitasRequest;
import com.sot.fenix.components.json.ResponseJSON;
import com.sot.fenix.components.models.Cita;
import com.sot.fenix.components.models.Cita.ESTADO;
import com.sot.fenix.components.rest.CitaREST;
import com.sot.fenix.components.models.Visita;
import com.sot.fenix.dao.CitaDAO;

@Service
public class CitaService {
	final static Logger log = LogManager.getLogger(CitaService.class);
	
	@Autowired
	private CitaDAO dao;
	@Autowired
	private MongoTemplate template;
	
	@Autowired
	private VisitaService visitas;
	
	public static final int ERR_CITA_PROGRAMADA=80;
	

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
	
	public Cita crearCita(Cita cita){
		cita.setEstado(ESTADO.PROGRAMADA);
		dao.save(cita);
		return dao.findOne(cita.getId());
	}
	
	public Visita capturarCita(Cita cita)throws ExceptionREST{
		if(cita==null)
			throw new ExceptionREST(ResponseJSON.NO_EXISTE,"La cita no existe en BD");
		
		if(!cita.isProgramada())
			throw new ExceptionREST(ERR_CITA_PROGRAMADA, "El estado de la cita no es correcto");
		
		log.debug("Capturando cita "+cita.getJsonId());
		cambiarEstado(cita, ESTADO.CAPTURANDO);			
		
		Visita visita=null;
		try{			
			visita=visitas.nuevaVisitaFromCita(cita);
		}catch(ExceptionREST ex){
			log.error("Error al crear la visita desde la cita "+cita.getJsonId()+": "+ex.getMessage());
			cambiarEstado(cita, ESTADO.PROGRAMADA);
			log.debug("Restaurado el estado a programada para la cita "+cita.getJsonId());
			throw ex;
		}
				
		cambiarEstado(cita, ESTADO.CAPTURADA);
		log.debug("Cita "+cita.getJsonId()+" capturada correctamente");
		
		return visita;				
	}
	
	public void cambiarEstado(Cita cita, ESTADO estado)throws ExceptionREST{
		log.debug("Cita "+cita.getJsonId()+" cambiando estado de: "+cita.getEstado()+" a "+estado);
		cita.setEstado(estado);
		getDAO().save(cita);
	}
}
