package com.sot.fenix.components.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sot.fenix.components.exceptions.ExceptionREST;
import com.sot.fenix.components.json.ResponseJSON;
import com.sot.fenix.components.models.Centro;
import com.sot.fenix.components.models.Cita;
import com.sot.fenix.components.models.Visita;
import com.sot.fenix.components.models.facturacion.Factura;
import com.sot.fenix.components.models.facturacion.Facturacion;
import com.sot.fenix.components.models.facturacion.Pago;
import com.sot.fenix.dao.VisitaDAO;
import com.sot.fenix.templates.basic.ABasicService;

@Service
public class VisitaService extends ABasicService<VisitaDAO, Visita>{
	final static Logger log = LogManager.getLogger(VisitaService.class);
	
	@Autowired
	private ConfigCentroService config;
	
	public Visita nuevaVisitaFromCita(Cita cita)throws ExceptionREST{
		log.debug("Creando visita a partir de la cita "+cita.getJsonId());
		Visita v=new Visita();
		
		v.setCentro(cita.getCentro());
		v.setCliente(cita.getCliente());
		v.setNombre(cita.getPrestacion().getNombre());
		v.setProfesional(cita.getProfesional());
		v.setFacturacion(new Facturacion());
		v.getFacturacion().setImporteTotal(cita.getImporte());
		v.getFacturacion().setPagos(new ArrayList<Pago>(1));
		v.setCita(cita);				
		
		if(config.isAutoPago(v.getCentro())){
			log.debug("Realizando auto pago de la visita con id "+v.getJsonId());
			realizarPago(v, cita.getImporte());
		}else{
			log.debug("AutoPago desactivado");
		}
		
		if(config.isAutoFacturar(v.getCentro())){					
			//generamos factura
			generarFacturaYGuardar(v);			
		}else{
			log.debug("AutoFactura desactivada");
			//recalculamos el estado de la facturación y guardamos
			recalcularEstadoFacturacion(v.getFacturacion());
			
			getDAO().save(v);
		}		
		
		log.debug("Visita con id "+v.getJsonId()+" creada a partir de la cita "+cita.getJsonId());
		
		return v;
	}
	
	public Pago realizarPago(Visita v, float cantidad)throws ExceptionREST{		
		if(v==null)
			throw new ExceptionREST(ResponseJSON.NO_EXISTE, "no existe la visita");
		
		Facturacion f=v.getFacturacion();
		
		if(f==null){
			throw new ExceptionREST(ResponseJSON.NO_EXISTE, "La facturacion no existe para la visita "+v.getJsonId());
		}
		
		if(f.getImportePagado()==f.getImporteTotal()){
			throw new ExceptionREST(ResponseJSON.YA_PAGADA, "La visita "+v.getJsonId()+" ya ha sido pagada");
		}
		
		if(f.getImportePagado()+cantidad>f.getImporteTotal()){
			throw new ExceptionREST(ResponseJSON.PAGO_EN_EXCESO, "Pago en exceso para la visita "+v.getJsonId()+": "+f.getImporteTotal()+"+"+cantidad+">"+f.getImporteTotal());
		}
		
		Pago p=new Pago();
		p.setImporte(cantidad);
				
		log.debug("Importe pagado para la visita"+v.getJsonId()+" antes de realizar el pago: "+f.getImportePagado());
		
		f.setImportePagado(f.getImportePagado()+cantidad);
		
		log.debug("Importe pagado para la visita"+v.getJsonId()+" despues de realizar el pago: "+f.getImportePagado());
		
		log.debug("Realizado pago de "+cantidad+" para la visita "+v.getJsonId());
				
		f.getPagos().add(p);
		
		return p;
	}
	
	public void recalcularEstadoFacturacion(Facturacion f)throws ExceptionREST{		
		if(f==null){
			throw new ExceptionREST(ResponseJSON.NO_EXISTE, "La facturacion no existe");
		}
		
		if(f.getImportePagado()==0){
			f.setNoPagado();
		}else if(f.getImportePagado()==f.getImporteTotal()){
			f.setPagoTotal();
		}else{
			f.setPagoParcial();
		}
	}

	
	public Factura generarFacturaYGuardar(Visita v)throws ExceptionREST{
		return generarFacturaYGuardar(-1, v);
	}

	/**
	 * Genera una factura con id secuencial(el de la ultimaFactura+1) o con el idFactura pasado por param.
	 * Hay que tener en cuenta que se hace un visita.saveBD para cambiar el estado a Facturando
	 * Finalmente se recalcula el estado de la Facturacion y se hace el save de la visita
	 * 
	 * @param idFactura
	 * @param v
	 * @return
	 * @throws ExceptionREST
	 */
	public Factura generarFacturaYGuardar(long idFactura, Visita v)throws ExceptionREST{
		if(v==null)
			throw new ExceptionREST(ResponseJSON.NO_EXISTE, "no existe la visita");
		
		if(v.getFacturacion().getFactura()!=null)
			throw new ExceptionREST(ResponseJSON.YA_FACTURADA, "Ya se ha facturado");
		
		log.debug("Generando la factura para la visita"+v.getJsonId());
		
		v.getFacturacion().setFacturando();
		//Salvamos el estado facturando(por si da error en BD al generar factura
		getDAO().save(v);
		log.debug("Cambiado el estado de facturación a FACTURANDO para la visita "+v.getJsonId());
		
		Factura f=new Factura();
		f.setImporte(v.getFacturacion().getImporteTotal());
		if(idFactura==-1){
			f.setIdFactura(config.siguienteFactura(v.getCentro()));
		}else{
			log.warn("Atención! Setteando idFactura de la visita "+v.getJsonId()+" al idFactura: "+idFactura);
			f.setIdFactura(idFactura);	
		}			
		
		log.debug("Factura generada con id: "+f.getIdFactura()+" para la visita: "+v.getJsonId()+" con un importe de: "+f.getImporte());
		v.getFacturacion().setFactura(f);
		recalcularEstadoFacturacion(v.getFacturacion());
		
		getDAO().save(v);
		log.debug("Factura con id: "+f.getIdFactura()+" guardada en BD para la visita: "+v.getJsonId());
		
		return f;
	}

	
	public Visita getByCita(Cita cita)throws ExceptionREST{
		if(cita==null || cita.getId()==null)
			throw new ExceptionREST(ResponseJSON.NO_EXISTE, "getByCita sin cita");
		
		return getDAO().findByCita_id(cita.getId());
	}

	public List<Visita> getVisitasCheckIntegridadFacturasConFacturaIdMayor(Centro centro){
		return dao.findByCentro_idAndFacturacion_Factura_idFacturaGreaterThanEqualOrderByFacturacion_Factura_idFactura(centro.getId(), config.getNumFacturaIntegrity(centro));
	}
	
	public List<Visita> getVisitasCheckIntegridadFacturaConFacturaNull(Centro centro){
		return dao.findByCentro_idAndFacturacion_estadoAndFacturacion_FacturaIsNull(centro.getId(), Facturacion.ESTADO.FACTURANDO);
	}
}
