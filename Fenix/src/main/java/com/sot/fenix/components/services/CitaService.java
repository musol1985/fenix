package com.sot.fenix.components.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sot.fenix.components.exceptions.ExceptionREST;
import com.sot.fenix.components.json.CitasRequest;
import com.sot.fenix.components.json.ResponseJSON;
import com.sot.fenix.components.models.Centro;
import com.sot.fenix.components.models.Visita;
import com.sot.fenix.components.models.citacion.Cita;
import com.sot.fenix.components.models.citacion.Cita.ESTADO;
import com.sot.fenix.dao.CitaDAO;
import com.sot.fenix.templates.service.ACentroIdService;
import com.sot.fenix.utils.DateUtils;

@Service
public class CitaService extends ACentroIdService<CitaDAO, Cita>{
	final static Logger log = LogManager.getLogger(CitaService.class);

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
	
	private Cita crearCita(Cita cita){
		cita.setEstado(ESTADO.PROGRAMADA);
		return dao.save(cita);		
	}
	
	/**
	 * Comprueba que la cita tenga cliente, prestación y profesional.
	 * @param cita
	 * @throws ExceptionREST
	 */
	private void validacionesBasicas(Cita cita) throws ExceptionREST{
		if(cita.getCliente()==null)
			throw new ExceptionREST(ResponseJSON.NO_CLIENTE, "Cliente no informado");
		
		if(cita.getPrestacion()==null)
			throw new ExceptionREST(ResponseJSON.NO_PRESTACION, "Prestacion no informado");
		
		if(cita.getProfesional()==null)
			throw new ExceptionREST(ResponseJSON.NO_PROFESIONAL, "Profesional no informado");
	}
	public static long MAX=0;
	/**
	 * Crea una cita y la guarda en BD en estado PROGRAMADA
	 * Se mira si tiene solapa, y en funcion del param forzarSolapa se fuerza o no
	 * 
	 * @param citaJSON
	 * @param forzarSolapa (en caso de que haya solapa indica si se tiene que forzar)
	 * @return
	 * @throws ExceptionREST
	 */
	public Cita nuevaCita(Cita citaJSON, boolean forzarSolapa)throws ExceptionREST{
		log.error("-----Iniciando nueva cita, max:"+MAX);
		long t=System.currentTimeMillis();
		
		if(citaJSON.getId()!=null)
			throw new ExceptionREST(ResponseJSON.YA_EXISTE, "No se puede crear cita que ya tenga ID"+citaJSON.getJsonId());
		
		validacionesBasicas(citaJSON);
		log.error("Fecha: "+citaJSON.getFechaIni());
		List<Cita> solapas=getCitasSolapa(citaJSON);
		log.error("------PostSolapas "+(System.currentTimeMillis()-t));
		
		if(solapas.size()>0 && !forzarSolapa){
			log.debug("Se han encontrado solapa y no se debe forzar para la cita "+citaJSON.getJsonId());
			throw new ExceptionREST(ResponseJSON.RES_TIENE_SOLAPA, "Solapa encontrada");
		}else{			
			Cita cita=crearCita(citaJSON);
			log.error("----PostGuardar "+(System.currentTimeMillis()-t));
			if((System.currentTimeMillis()-t)>MAX){
				MAX=(System.currentTimeMillis()-t);
				log.error(" ###########################MAX PostGuardar "+MAX);
			}
				
			log.debug("Cita creada "+citaJSON.getJsonId());
			return cita;	
		}
	}
	
	/**
	 * Modifica una cita
	 * @param citaJSON
	 * @param forzarSolapa
	 * @return cita modificada
	 * @throws ExceptionREST
	 */
	public Cita modificarCita(Cita citaJSON, boolean forzarSolapa)throws ExceptionREST{
		if(citaJSON.getId()==null)
			throw new ExceptionREST(ResponseJSON.RES_NO_ID_CITA, "No se puede modificar una cita sin IdCita");
		
		if(!citaJSON.isProgramada())
			throw new ExceptionREST(ResponseJSON.RES_ESTADO_INCORRECTO, "No se puede modificar una cita cuyo estado no sea programado: "+citaJSON.getJsonId()+" - "+citaJSON.getEstado());
		
		validacionesBasicas(citaJSON);
		
		if(getDAO().findOne(citaJSON.getId())==null)
			throw new ExceptionREST(ResponseJSON.NO_EXISTE, "No existe en BD la cita: "+citaJSON.getJsonId());
		
		
		if(!forzarSolapa){
			List<Cita> solapas=getCitasSolapa(citaJSON);
			
			if(solapas.size()>0 && !(solapas.get(0).getId().toHexString().equals(citaJSON.getId().toHexString()))){
				log.debug("Se ha encontrado solapas para la cita "+citaJSON.getJsonId());
				throw new ExceptionREST(ResponseJSON.RES_TIENE_SOLAPA, "La cita tiene solapas");
			}
		}
		
		return getDAO().save(citaJSON);
	}
	
	/**
	 * Reprogramar una cita
	 * @param citaJSON(solo viene el codigo, fechaIni y fechaFin)
	 * @param forzarSolapa
	 * @return cita reprogramada
	 * @throws ExceptionREST
	 */
	public Cita reprogramarCita(Cita citaJSON, boolean forzarSolapa)throws ExceptionREST{
		if(citaJSON.getId()==null)
			throw new ExceptionREST(ResponseJSON.RES_NO_ID_CITA, "No se puede modificar una cita sin IdCita");
		
		if(!citaJSON.isProgramada())
			throw new ExceptionREST(ResponseJSON.RES_ESTADO_INCORRECTO, "No se puede modificar una cita cuyo estado no sea programado: "+citaJSON.getJsonId()+" - "+citaJSON.getEstado());
		
		Cita cita=getDAO().findOne(citaJSON.getId());
		if(cita==null)
			throw new ExceptionREST(ResponseJSON.NO_EXISTE, "No existe en BD la cita: "+citaJSON.getJsonId());
				
		if(!forzarSolapa){
			List<Cita> solapas=getCitasSolapa(citaJSON);
			
			if(solapas.size()>0 && !(solapas.get(0).getId().toHexString().equals(citaJSON.getId().toHexString()))){
				log.debug("Se ha encontrado solapas para la cita "+citaJSON.getJsonId());
				throw new ExceptionREST(ResponseJSON.RES_TIENE_SOLAPA, "La cita tiene solapas");
			}
		}
		
		cita.setFechaIni(citaJSON.getFechaIni());
		cita.setFechaFin(citaJSON.getFechaFin());
		getDAO().save(cita);
		
		return cita;
	}
	
	/**
	 * Captura una cita y genera una visita
	 * @param cita
	 * @return Visita
	 * @throws ExceptionREST
	 */
	public Visita capturarCita(Cita cita)throws ExceptionREST{
		if(cita==null)
			throw new ExceptionREST(ResponseJSON.NO_EXISTE,"La cita no existe en BD");
		
		if(!cita.isProgramada())
			throw new ExceptionREST(ERR_CITA_PROGRAMADA, "El estado de la cita no es correcto");
		
		log.debug("Capturando cita "+cita.getJsonId());
		cambiarEstadoAndSave(cita, ESTADO.CAPTURANDO);			
		
		Visita visita=null;
		try{			
			visita=visitas.nuevaVisitaFromCita(cita);
		}catch(ExceptionREST ex){
			log.error("Error al crear la visita desde la cita "+cita.getJsonId()+": "+ex.getMessage());
			ex.printStackTrace();
			cambiarEstadoAndSave(cita, ESTADO.PROGRAMADA);
			log.debug("Restaurado el estado a programada para la cita "+cita.getJsonId());
			throw ex;
		}
				
		cambiarEstadoAndSave(cita, ESTADO.CAPTURADA);
		log.debug("Cita "+cita.getJsonId()+" capturada correctamente");
		
		return visita;				
	}
	
	public void cambiarEstadoAndSave(Cita cita, ESTADO estado)throws ExceptionREST{
		log.debug("Cita "+cita.getJsonId()+" cambiando estado de: "+cita.getEstado()+" a "+estado);
		cita.setEstado(estado);
		getDAO().save(cita);
	}
	
	
	/**
	 * Obtiene las citas >=fechaini <=fechafin dado un centro
	 * En funcion de las citas las coge de cita o citahisto
	 * 
	 * @param fechaIni
	 * @param fechaFin
	 * @param centro
	 * @return
	 * @throws ExceptionREST
	 */
	public List<Cita> getCitasIn(Date fechaIni, Date fechaFin, Centro centro)throws ExceptionREST{
		return getDAO().findByFechaIniGreaterThanEqualAndFechaFinLessThanEqualAndCentro_id(fechaIni, fechaFin, centro.getId());
	}
}
