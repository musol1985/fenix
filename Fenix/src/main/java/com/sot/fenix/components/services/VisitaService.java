package com.sot.fenix.components.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.sot.fenix.components.exceptions.ExceptionREST;
import com.sot.fenix.components.json.ResponseJSON;
import com.sot.fenix.components.models.Cita;
import com.sot.fenix.components.models.Visita;
import com.sot.fenix.dao.VisitaDAO;
import com.sot.fenix.templates.basic.ABasicService;

@Service
public class VisitaService extends ABasicService<VisitaDAO, Visita>{
	final static Logger log = LogManager.getLogger(VisitaService.class);
	
	public Visita nuevaVisitaFromCita(Cita cita)throws ExceptionREST{
		log.debug("Creando visita a partir de la cita "+cita.getJsonId());
		return null;
	}
	
	public Visita getByCita(Cita cita)throws ExceptionREST{
		if(cita==null || cita.getId()==null)
			throw new ExceptionREST(ResponseJSON.NO_EXISTE, "getByCita sin cita");
		
		return getDAO().findByCita_id(cita.getId());
	}
}
