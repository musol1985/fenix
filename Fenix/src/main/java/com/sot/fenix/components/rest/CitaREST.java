package com.sot.fenix.components.rest;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sot.fenix.components.exceptions.ExceptionREST;
import com.sot.fenix.components.json.CitasRequest;
import com.sot.fenix.components.json.MaestrosCitaJSON;
import com.sot.fenix.components.json.CitaRequestJSON;
import com.sot.fenix.components.json.ResponseJSON;
import com.sot.fenix.components.models.Visita;
import com.sot.fenix.components.models.citacion.Cita;
import com.sot.fenix.components.services.CitaService;
import com.sot.fenix.components.services.HorarioService;
import com.sot.fenix.components.services.PrestacionService;
import com.sot.fenix.components.services.UsuarioService;
import com.sot.fenix.dao.CitaDAO;
import com.sot.fenix.templates.REST.ACentroIdREST;

@RestController
@RequestMapping("/cita")
@SuppressWarnings("unchecked")
public class CitaREST extends ACentroIdREST<CitaService, Cita, CitaDAO>{
	final static Logger log = LogManager.getLogger(CitaREST.class);

	@Autowired
	private HorarioService horarios;
	@Autowired
	private UsuarioService profesionales;
	@Autowired
	private PrestacionService prestaciones;

		
	@RequestMapping(method=RequestMethod.PUT)
    public ResponseJSON<Cita> nueva(@RequestBody CitaRequestJSON req) {
		try{
			log.debug("Creando nueva cita");
			
			Cita cita=service.nuevaCita(req.cita, req.forzar);
			
			return new ResponseJSON<Cita>(ResponseJSON.OK, cita);
		}catch(ExceptionREST ex){
			if(ex.getCodigo()==ResponseJSON.NO_EXISTE){
				log.error("La cita con código "+req.cita.getJsonId()+" no existe en la BD.");
			}else if(ex.getCodigo()!=ResponseJSON.RES_TIENE_SOLAPA){
				log.error(ex.getMessage());
			}
			return ex.toResponse();
		}
    }
	
	@Override
	@RequestMapping(method=RequestMethod.POST)
    public ResponseJSON<Cita> modificar(@RequestBody Cita cita){
		log.error("se intenta modificar una cita con un metodo incorrecto POST{CITA} y debe ser POST{CitaRequestJSON}");
		return new ResponseJSON<Cita>(ResponseJSON.ACCION_PROHIBIDA_REST);
	}
	
	@RequestMapping(method=RequestMethod.POST, path="/modificar")
    public ResponseJSON<Cita> modificarCita(@RequestBody CitaRequestJSON req) {
		try{
			log.debug("Modificando cita");
			
			Cita cita=service.modificarCita(req.cita, req.forzar);
			

			return new ResponseJSON<Cita>(ResponseJSON.OK, cita);
		}catch(ExceptionREST ex){
			if(ex.getCodigo()==ResponseJSON.NO_EXISTE){
				log.error("La cita con código "+req.cita.getJsonId()+" no existe en la BD.");
			}else if(ex.getCodigo()!=ResponseJSON.RES_TIENE_SOLAPA){
				log.error(ex.getMessage());
			}
			return ex.toResponse();
		}
    }
	
	@RequestMapping(method=RequestMethod.POST, path="/reprogramar")
    public ResponseJSON<Cita> reprogramar(@RequestBody CitaRequestJSON req) {
		try{
			log.debug("Reprogramando cita");
			
			Cita cita=service.reprogramarCita(req.cita, req.forzar);			

			return new ResponseJSON<Cita>(ResponseJSON.OK, cita);
		}catch(ExceptionREST ex){
			if(ex.getCodigo()==ResponseJSON.NO_EXISTE){
				log.error("La cita con código "+req.cita.getJsonId()+" no existe en la BD.");
			}else if(ex.getCodigo()!=ResponseJSON.RES_TIENE_SOLAPA){
				//logueamos el error en caso de que no sea error de solapa
				log.error(ex.getMessage());
			}
			return ex.toResponse();
		}
    }
    
	@RequestMapping(method=RequestMethod.GET, path="/in")
    public List<Cita> in(@RequestParam("centro") String centro, @RequestParam("start") @DateTimeFormat(pattern="yyyy-MM-dd") Date start,  @RequestParam("end") @DateTimeFormat(pattern="yyyy-MM-dd") Date end) {
		List<Cita> res=service.getDAO().findByFechaIniGreaterThanEqualAndFechaFinLessThanEqualAndCentro_id(start, end, new ObjectId(centro));
		return res;	
    }
	
	@RequestMapping(method=RequestMethod.POST, path="/all")
    public List<Cita> modificar(@RequestBody CitasRequest req) {
		List<Cita> res=service.buscar(req);
		return res;	
	}
	
	@RequestMapping(method=RequestMethod.GET, path="/maestros/{centro}")
    public MaestrosCitaJSON modificar(@PathVariable("centro") String centro) {
		MaestrosCitaJSON maestros=new MaestrosCitaJSON();
		
		ObjectId centroId=new ObjectId(centro);
		
		maestros.prestaciones=prestaciones.getDAO().findByCentro_id(centroId);
		maestros.horarios=horarios.getDAO().findByCentro_id(centroId);
		maestros.profesionales=profesionales.getUsuarioByCentro(centro);
		
		return maestros;
    }
	
	
	@RequestMapping(method=RequestMethod.GET, path="/test")
    public String test(@RequestParam("centro") String centro, @RequestParam("start") @DateTimeFormat(pattern="yyyy-MM-dd") Date start) {
		return "CitaREST OK"+centro+start;
	}
	
	
	@RequestMapping(method=RequestMethod.POST, path="/capturar")
    public ResponseJSON<Visita> capturar(@RequestBody Cita cita) {
		Cita item=service.getDAO().findOne(new ObjectId(cita.getId().toHexString()));
		
		try{
			log.debug("Capturando cita "+cita.getJsonId());
			
			Visita visita=service.capturarCita(item);
			
			return new ResponseJSON<Visita>(ResponseJSON.OK,visita);
		}catch(ExceptionREST ex){
			if(ex.getCodigo()==ResponseJSON.NO_EXISTE){
				log.error("La cita con código "+cita.getJsonId()+" no existe en la BD.");
			}else{
				log.error(ex.getMessage());
			}
			return ex.toResponse();
		}
    }
}

