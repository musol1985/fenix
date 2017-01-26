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
import com.sot.fenix.components.models.Cita;
import com.sot.fenix.components.models.Visita;
import com.sot.fenix.components.services.CitaService;
import com.sot.fenix.components.services.VisitaService;

@Component
public class DeployListener {
	final static Logger log = LogManager.getLogger(DeployListener.class);
	
	@Autowired
	private CitaService citas;
	private VisitaService visitas;
	
	@EventListener({ContextRefreshedEvent.class})
    void contextRefreshedEvent() {
		log.info("Comprobando integridad de la BD->>>>>>>>>");
		
		
		arreglarCitasCapturando();
		
		log.info("FIN Comprobando integridad de la BD<<<<<<<-");
    }
	
	
	private void arreglarCitasCapturando(){
		log.info("Arreglar citas que se han quedado capturando");
		List<Cita> citasCapurando=citas.getDAO().findByEstado(Cita.ESTADO.CAPTURANDO);
		log.debug("Citas capturando: "+citasCapurando.size());
		
		if(citasCapurando.size()==0)
			return;
		
		List<Cita> fixProgramadas=new ArrayList<Cita>();;
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
}
