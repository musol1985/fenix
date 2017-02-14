package com.sot.fenix.components.listeners;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.sot.fenix.components.exceptions.ExceptionREST;
import com.sot.fenix.components.models.Centro;
import com.sot.fenix.components.models.Cita;
import com.sot.fenix.components.models.Visita;
import com.sot.fenix.components.models.facturacion.Facturacion;
import com.sot.fenix.components.services.CentroService;
import com.sot.fenix.components.services.CitaService;
import com.sot.fenix.components.services.ConfigCentroService;
import com.sot.fenix.components.services.FacturacionService;
import com.sot.fenix.components.services.VisitaService;

@Component
public class DeployListener {
	final static Logger log = LogManager.getLogger(DeployListener.class);
	
	@Autowired
	private CitaService citas;
	@Autowired
	private VisitaService visitas;
	@Autowired
	private FacturacionService facturacion;
	@Autowired
	private ConfigCentroService config;
	@Autowired
	private CentroService centros;
	
	@EventListener({ContextRefreshedEvent.class})
    public void contextRefreshedEvent() {
		log.info("Comprobando integridad de la BD->>>>>>>>>");
		
		
		arreglarCitasCapturando();
		for(Centro c:centros.getDAO().findAll()){
			checkIntegridadFacturas(c);
		}
		
		log.info("FIN Comprobando integridad de la BD<<<<<<<-");
    }
	
	
	private void arreglarCitasCapturando(){
		log.info("Arreglar citas que se han quedado capturando");
		List<Cita> citasCapurando=citas.getDAO().findByEstado(Cita.ESTADO.CAPTURANDO);
		log.debug("Citas capturando: "+citasCapurando.size());
		
		if(citasCapurando.size()==0)
			return;
		
		List<Cita> fixProgramadas=new ArrayList<Cita>();
		List<Cita> fixCapturadas=new ArrayList<Cita>();
		
		for(Cita cita:citasCapurando){
			try{
				Visita visita=visitas.getByCita(cita);
				
				if(visita==null){
					log.debug("No existe la visita para la cita "+cita.getJsonId()+". Se pone la cita a programada.");
					citas.cambiarEstado(cita, Cita.ESTADO.PROGRAMADA);
					fixProgramadas.add(cita);
				}else{
					log.debug("Existe la visita para la cita "+cita.getJsonId()+". Se pone la cita a capturada.");
					citas.cambiarEstado(cita, Cita.ESTADO.CAPTURADA);
					fixCapturadas.add(cita);
				}
				
			}catch(ExceptionREST ex){
				log.error(ex.getMessage());
			}
		}
		
		log.info("Citas capturadas arregladas: "+fixCapturadas.size());
		if(log.isDebugEnabled()){
			for(Cita c:fixCapturadas){
				log.debug("--->Cita "+c.getJsonId()+" fecha "+c.getFechaIni());
			}
		}
		
		log.info("Citas programadas arregladas: "+fixProgramadas.size());
		if(log.isDebugEnabled()){
			for(Cita c:fixProgramadas){
				log.debug("--->Cita "+c.getJsonId()+" fecha "+c.getFechaIni());
			}
		}
	}
	
	/**
	 * Comprobar integridad de las facturas
	 */
	private void checkIntegridadFacturas(Centro centro){
		log.info("Comrpobando la integridad de las facturas para el centro "+centro.getJsonId()+" - "+centro.getNombre());
		
		List<Visita> visitasSinCheckSinFactura=facturacion.getVisitasCheckIntegridadFacturaConFacturaNull(centro);
		
		//Si no hay visitas con factura=null
		if(visitasSinCheckSinFactura.size()>0){
			List<Visita> visitasSinCheckConFactura=facturacion.getVisitasCheckIntegridadFacturasConFacturaIdMayor(centro);			
			
			List<Visita> visitasSinCheck=new ArrayList<Visita>();
			visitasSinCheck.addAll(visitasSinCheckConFactura);
			visitasSinCheck.addAll(visitasSinCheckSinFactura);
			
			log.info("Visitas sin checkear: "+visitasSinCheck.size());			

			List<Long> numerosDisponibles=new ArrayList<Long>();
			
			for(int i=0;i<visitasSinCheckConFactura.size()-1;i++){
				Visita v=visitasSinCheckConFactura.get(i);
				Visita v2=visitasSinCheckConFactura.get(i+1);
				
				long idFactura=v.getNumFactura();
				long idFactura2=v2.getNumFactura();
				if(idFactura>-1 && idFactura2>-1){
					if(idFactura2!=idFactura+1){
						for(long e=idFactura+1;e<idFactura2;e++){
							numerosDisponibles.add(e);
						}
					}
				}			
			}
			
			if(visitasSinCheckConFactura.size()>0){
				long lastFacturaGenerada=config.getSequence(centro.getId(), ConfigCentroService.SEQ_FACTURA);
				long lastFacturaVisita=visitasSinCheckConFactura.get(visitasSinCheckConFactura.size()-1).getNumFactura();
				
				for(long i=lastFacturaVisita+1;i<=lastFacturaGenerada;i++){
					numerosDisponibles.add(i);
				}
			}
	
			try{
				for(int i=0;i<visitasSinCheck.size();i++){
					Visita v=visitasSinCheck.get(i);
					Facturacion f=v.getFacturacion();
					if(f.getFactura()==null){
						//Puede darse el caso que la secuence esté aumentada, por lo que habrá que buscar un num libre
						//Puede darse que la secuence no se hubiese aumentado, por lo que habrá que generar factura nueva
						if(numerosDisponibles.size()>0){
							facturacion.generarFacturaYGuardar(numerosDisponibles.remove(0), v);
						}else{
							//Se debe generar la factura
							facturacion.generarFacturaYGuardar(v);								
						}
					}else{
						//No hay que hacer nada(Facturada OK)
					}

				}
			}catch(ExceptionREST ex){
				log.error(ex.getMessage());
			}										
		}
		
		log.debug("Seteando NumFactura integrity para el centro "+centro.getId()+" a: "+facturacion.getMaxIdFacturaByCentro(centro));
		config.setNumFacturaIntegrity(centro, facturacion.getMaxIdFacturaByCentro(centro));
		
		log.info("Fin Comrpobación de la integridad de las facturas para el centro "+centro.getJsonId()+"-"+centro.getNombre());
	}
}
