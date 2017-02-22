package com.sot.fenix.components.services;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sot.fenix.components.exceptions.ExceptionREST;
import com.sot.fenix.components.json.ResponseJSON;
import com.sot.fenix.components.models.Centro;
import com.sot.fenix.components.models.Cita;
import com.sot.fenix.components.models.Visita;
import com.sot.fenix.components.models.facturacion.Facturacion;
import com.sot.fenix.components.models.facturacion.Pago;
import com.sot.fenix.dao.VisitaDAO;
import com.sot.fenix.templates.service.ACentroIdService;

@Service
public class VisitaService extends ACentroIdService<VisitaDAO, Visita>{
	final static Logger log = LogManager.getLogger(VisitaService.class);
	
	@Autowired
	private ConfigCentroService config;
	@Autowired
	private FacturacionService facturacion;
	@Autowired
	private CitaService citas;
	
	
	/**
	 * Genera una nueva visita a partir de una cita<br>
	 * 	<ol>
	 * 		<li>En función del param {@link ConfigCentroService#isAutoFacturar(Centro) isAutoFacturar} se facturará automaticamente(el importe total) o no</li>
	 * 		<li>En función del param {@link ConfigCentroService#isAutoPago(Centro) isAutoPago} se pagará automaticamente(el importe total) o no</li>
	 *  </ol>
	 *
	 * @param cita
	 * @return Visita (ya guardada en BD)
	 * @throws ExceptionREST
	 */
	public Visita nuevaVisitaFromCita(Cita citaJson)throws ExceptionREST{
		log.debug("Creando visita a partir de la cita "+citaJson.getJsonId());
		
		Cita cita=citas.getDAO().findOne(citaJson.getId());
		
		if(cita==null){
			log.error("No existe la cita "+citaJson.getJsonId());
			throw new ExceptionREST(ResponseJSON.NO_EXISTE, "nuevaVisitaFromCita no existe la cita");
		}
		
		if(!cita.isProgramada()){
			log.error("No se puede capturar una cita que no sea programada: "+citaJson.getJsonId()+" "+cita.getEstado());
			throw new ExceptionREST(ResponseJSON.YA_CAPTURADA, "nuevaVisitaFromCita no existe la cita");
		}
			
		
		Visita v=new Visita();
		
		v.setCentro(cita.getCentro());
		v.setCliente(cita.getCliente());
		v.setMotivo(cita.getPrestacion().getDescripcion());
		v.setProfesional(cita.getProfesional());
		v.setFacturacion(new Facturacion());
		v.getFacturacion().setImporteTotal(cita.getImporte());
		v.getFacturacion().setPagos(new ArrayList<Pago>(1));
		v.setCita(cita);				
		
		if(config.isAutoPago(v.getCentro())){
			log.debug("Realizando auto pago de la visita con id "+v.getJsonId());
			facturacion.realizarPago(v, cita.getImporte());
		}else{
			log.debug("AutoPago desactivado");
		}
		
		if(config.isAutoFacturar(v.getCentro())){					
			//generamos factura
			facturacion.generarFacturaYGuardar(v);			
		}else{
			log.debug("AutoFactura desactivada");
			//recalculamos el estado de la facturación y guardamos
			facturacion.recalcularEstadoFacturacion(v.getFacturacion());
			
			getDAO().save(v);
		}		
		
		log.debug("Visita con id "+v.getJsonId()+" creada a partir de la cita "+cita.getJsonId());
		
		return v;
	}

	/**
	 * Obtiene una visita a partir de su cida
	 * @param cita
	 * @return Visita o null en caso de no existir
	 * @throws ExceptionREST
	 */
	public Visita getByCita(Cita cita)throws ExceptionREST{
		if(cita==null || cita.getId()==null)
			throw new ExceptionREST(ResponseJSON.NO_EXISTE, "getByCita sin cita");
		
		return getDAO().findByCita_id(cita.getId());
	}


}
