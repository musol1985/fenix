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
import com.sot.fenix.templates.service.ABasicService;

@Service
public class FacturacionService {
	final static Logger log = LogManager.getLogger(FacturacionService.class);
	
	@Autowired
	private ConfigCentroService config;
	@Autowired
	private VisitaDAO visitaDAO;
	
	/**
	 * Realiza el pago de una "cantidad" sobre la visita pasada por parametro
	 * NO SE HACE SAVE
	 * @param v
	 * @param cantidad
	 * @return
	 * @throws ExceptionREST
	 */
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
	
	/**
	 * Recalcula el estado de facturacion:
	 * 		Si está todo el importe pagado, se pone estado=PAGO_TOTAL
	 * 		Si está a 0, se pone estado=NO_PAGADO
	 * 		Si está < total, se pone estado=PAGO_PARCIAL
	 * 
	 * NO SE HACE SAVE
	 * @param Objeto Facturacion de una visita
	 * @throws ExceptionREST
	 */
	public void recalcularEstadoFacturacion(Facturacion f)throws ExceptionREST{		
		if(f==null){
			throw new ExceptionREST(ResponseJSON.NO_EXISTE, "La facturacion no existe");
		}
		
		log.debug("Recalculando estado facturación...");
		if(f.getImportePagado()==0){
			f.setNoPagado();
		}else if(f.getImportePagado()==f.getImporteTotal()){
			f.setPagoTotal();
		}else{
			f.setPagoParcial();
		}
		log.debug("Recalculado: "+f.getImportePagado()+"/"+f.getImporteTotal()+"-->"+f.getEstado());
	}

	
	/**
	 * Generar una nueva factura obteniendo el ID de factura de la sequence<br>
	 * Ver {@link #generarFacturaYGuardar(long, Visita) generarFacturaYGuardar}.
	 * @param Visita
	 * @return Factura
	 * @throws ExceptionREST
	 */
	public Factura generarFacturaYGuardar(Visita v)throws ExceptionREST{
		return generarFacturaYGuardar(-1, v);
	}

	/**
	 * Genera una factura con id secuencial(el de la ultimaFactura+1) o con el idFactura pasado por param.<br>
	 * Hay que tener en cuenta que se hace un visita.saveBD para cambiar el estado a Facturando.<br>
	 * Finalmente se recalcula el estado de la Facturacion y se hace el save de la visita
	 * 
	 * @param idFactura
	 * @param visita
	 * @return Factura
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
		visitaDAO.save(v);
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
		
		visitaDAO.save(v);
		log.debug("Factura con id: "+f.getIdFactura()+" guardada en BD para la visita: "+v.getJsonId());
		
		return f;
	}

	/**
	 * Obtiene las visitas con id factura mayor al del parametro de check de integridad 
	 * {@link ConfigCentroService#getNumFacturaIntegrity(Centro) SEQ_FACTURA_INTEGRITY}
	 * @param centro
	 * @return visitas con numFactura>= al param SEQ_FACTURA_INTEGRITY
	 */
	public List<Visita> getVisitasCheckIntegridadFacturasConFacturaIdMayor(Centro centro){
		return visitaDAO.findByCentro_idAndFacturacion_Factura_idFacturaGreaterThanEqualOrderByFacturacion_Factura_idFactura(centro.getId(), config.getNumFacturaIntegrity(centro));
	}
	
	/**
	 * Obtiene las visitas con Facturacion.factura=null y estado=FACTURANDO 
	 * @param centro
	 * @return visitas
	 */
	public List<Visita> getVisitasCheckIntegridadFacturaConFacturaNull(Centro centro){
		return visitaDAO.findByCentro_idAndFacturacion_estadoAndFacturacion_FacturaIsNull(centro.getId(), Facturacion.ESTADO.FACTURANDO);
	}
	
	/**
	 * OBtiene el maxId de factura para un centro
	 * @param centro
	 * @return maxId de factura de todas las visitas de un centro
	 */
	public long getMaxIdFacturaByCentro(Centro centro){
		log.debug("Obteniendo el max idFacturaByCentro para "+centro.getJsonId());
		Visita v=visitaDAO.findFirstByCentro_idAndFacturacion_Factura_idFacturaGreaterThanEqualOrderByFacturacion_Factura_idFacturaDesc(centro.getId(), config.getNumFacturaIntegrity(centro));
		if(v!=null){
			log.debug("Obtenido el maxFacturaId: "+v.getNumFactura()+" para el centro: "+centro.getJsonId());
			return v.getNumFactura();			
		}
		log.debug("Obtenido el maxFacturaId: 0 para el centro: "+centro.getJsonId());
		return 0;
	}
}
