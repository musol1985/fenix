package com.sot.fenix.components.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.sot.fenix.components.exceptions.ExceptionREST;
import com.sot.fenix.components.json.ResponseJSON;
import com.sot.fenix.components.models.Cita;
import com.sot.fenix.components.models.Facturacion;
import com.sot.fenix.components.models.Pago;
import com.sot.fenix.components.models.Visita;
import com.sot.fenix.dao.VisitaDAO;
import com.sot.fenix.templates.basic.ABasicService;

@Service
public class VisitaService extends ABasicService<VisitaDAO, Visita>{
	final static Logger log = LogManager.getLogger(VisitaService.class);
	
	public Visita nuevaVisitaFromCita(Cita cita)throws ExceptionREST{
		log.debug("Creando visita a partir de la cita "+cita.getJsonId());
		Visita v=new Visita();
		
		v.setCentro(cita.getCentro());
		v.setCliente(cita.getCliente());
		v.setEstado(Visita.ESTADO.PROCESO);
		v.setNombre(cita.getPrestacion().getNombre());
		v.setProfesional(cita.getProfesional());
		v.setImporteTotal(cita.getImporte());
		v.setImporteRestante(v.getImporteTotal());
		v.setCita(cita);
		
		getDAO().save(v);
		
		log.debug("Visita con id "+v.getJsonId()+" creada a partir de la cita "+cita.getJsonId());
		
		return v;
	}
	
	public Visita realizarPago(ObjectId id, float cantidad, boolean oficial)throws ExceptionREST{
		Visita v=dao.findOne(id);
		
		log.debug("Realizando pago para la visita con id "+id.toHexString());
		
		if(v==null)
			throw new ExceptionREST(ResponseJSON.NO_EXISTE, "no existe la cita "+id.toHexString());
		
		Facturacion f=v.getFacturacion();
		
		if(f==null){
			f=new Facturacion();
			v.setFacturacion(f);
			log.debug("Se inicializa la facturación para la visita con id "+id.toHexString());
		}

		if(f.getImporteRestante()==0){
			throw new ExceptionREST(ResponseJSON.YA_PAGADA, "La visita "+id.toHexString()+" ya ha sido pagada");
		}
		
		if(f.getImporteRestante()-cantidad<0){
			throw new ExceptionREST(ResponseJSON.PAGO_EN_EXCESO, "Pago en exceso para la visita "+id.toHexString()+": "+f.getImporteRestante()+"-"+cantidad);
		}
		
		Pago p=new Pago();
		p.setImporte(cantidad);
				
		if(!p.isOficial() && oficial){
			p.setNumFactura(getSiguienteNumFactura());
			log.debug("Numero de factura "+p.getNumFactura()+" para la visita "+v.getId().toHexString());
		}
		
		p.setOficial(oficial);
		
		List<Pago> pagos=f.getPagos();
		if(pagos==null){
			pagos=new ArrayList<Pago>();
			f.setPagos(pagos);
		}
		
		f.setImporteRestante(f.getImporteRestante()-p.getImporte());
		
		getDAO().save(v);
		
		return v;
	}
	
	public Visita getByCita(Cita cita)throws ExceptionREST{
		if(cita==null || cita.getId()==null)
			throw new ExceptionREST(ResponseJSON.NO_EXISTE, "getByCita sin cita");
		
		return getDAO().findByCita_id(cita.getId());
	}
	
	public int getSiguienteNumFactura(){
		//TODO generar siguiente numero de factura
		numeroFac++;
		log.debug("Obteniendo nuevo numero de factura"+numeroFac);
		return numeroFac;
	}
	
	private static int numeroFac=1;
}
