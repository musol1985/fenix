package com.sot.fenix.components.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.sot.fenix.components.models.horarios.Patron;

@Document
public class Horario extends AModelId{
	public enum DIA{DOMINGO, LUNES, MARTES, MIERCOLES, JUEVES, VIERNES, SABADO};

	
	private List<Patron> patrones;
	private List<Date> festivos;
	
	public void addPatron(Patron p){
		if(patrones==null)
			patrones=new ArrayList<Patron>(7);
		patrones.add(p);
	}
	

	
	public static Horario getGenerico(){
		Horario h=new Horario();
		
		h.addPatron(Patron.getGenerico(DIA.DOMINGO));
		h.addPatron(Patron.getGenerico(DIA.LUNES));
		h.addPatron(Patron.getGenerico(DIA.MARTES));
		h.addPatron(Patron.getGenerico(DIA.MIERCOLES));
		h.addPatron(Patron.getGenerico(DIA.JUEVES));
		h.addPatron(Patron.getGenerico(DIA.VIERNES));
		h.addPatron(Patron.getGenerico(DIA.SABADO));
		
		h.festivos=new ArrayList<Date>();
		
		return h;
	}



	public List<Patron> getPatrones() {
		return patrones;
	}



	public void setPatrones(List<Patron> patrones) {
		this.patrones = patrones;
	}



	public List<Date> getFestivos() {
		return festivos;
	}



	public void setFestivos(List<Date> festivos) {
		this.festivos = festivos;
	}
	
	
}
