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
import com.sot.fenix.components.models.Visita;
import com.sot.fenix.components.models.facturacion.FacturaOficial;
import com.sot.fenix.components.models.facturacion.Facturacion;
import com.sot.fenix.components.models.facturacion.Pago;
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
		v.setFacturacion(new Facturacion());
		v.getFacturacion().setImporteTotal(cita.getImporte());
		v.setCita(cita);
		
		getDAO().save(v);
		
		log.debug("Visita con id "+v.getJsonId()+" creada a partir de la cita "+cita.getJsonId());
		
		return v;
	}
	
	public Visita realizarPago(ObjectId id, float cantidad, boolean generarFactura)throws ExceptionREST{
		Visita v=dao.findOne(id);
		
		log.debug("Realizando pago para la visita con id "+id.toHexString());
		
		if(v==null)
			throw new ExceptionREST(ResponseJSON.NO_EXISTE, "no existe la visita "+id.toHexString());
		
		Facturacion f=v.getFacturacion();
		
		if(f==null){
			throw new ExceptionREST(ResponseJSON.NO_EXISTE, "La facturacion no existe para la visita "+id.toHexString());
		}

		if(f.getImportePagado()==f.getImporteTotal()){
			throw new ExceptionREST(ResponseJSON.YA_PAGADA, "La visita "+id.toHexString()+" ya ha sido pagada");
		}
		
		if(f.getImportePagado()+cantidad>f.getImporteTotal()){
			throw new ExceptionREST(ResponseJSON.PAGO_EN_EXCESO, "Pago en exceso para la visita "+id.toHexString()+": "+f.getImporteTotal()+"+"+cantidad+">"+f.getImporteTotal());
		}
		
		Pago p=new Pago();
		p.setImporte(cantidad);

		
		List<Pago> pagos=f.getPagos();
		if(pagos==null){
			pagos=new ArrayList<Pago>();
			f.setPagos(pagos);
		}
		
		f.setImportePagado(cantidad);
		
		getDAO().save(v);
		
		return v;
	}
	
	
	public FacturaOficial generarFactura(Visita v, List<String> pagos)throws ExceptionREST{
		if(v==null)
			throw new ExceptionREST(ResponseJSON.NO_EXISTE, "no existe la visita");
		
		if(pagos.size()==0)
			throw new ExceptionREST(ResponseJSON.NO_HAY_PAGOS, "no se han recibido pagos");
		
		FacturaOficial f=null;
		
		for(String pId:pagos){
			Pago p=getPagoById(v, pId);
			if(p==null)
				throw new ExceptionREST(ResponseJSON.NO_HAY_PAGOS, "no se ha encontrado el pago "+pId+" en la visita "+v.getId().toHexString());
			
			if(!p.hasFactura()){
				if(f==null){
					
				}
			}
		}
		
		return f;
	}
	
	public List<Pago> getPagosByIds(Visita v, List<String> ids){
		List<Pago> res=new ArrayList<Pago>(ids.size());
		for(String id:ids){
			Pago p=getPagoById(v, id);
			if(p!=null)
				res.add(p);
		}
		return res;
	}
	
	public Pago getPagoById(Visita v, String id){
		for(Pago p:v.getFacturacion().getPagos()){
			if(p.getId().toHexString().equals(id))
				return p;
		}
		return null;
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
