package com.sot.fenix.components.models.horarios;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.sot.fenix.components.models.Horario.DIA;

public class Patron {
	private DIA dia;
	private List<Franja> horas;
	
	public Patron(){
		
	}
	
	public Patron(DIA dia){
		this.dia=dia;
		this.horas=new ArrayList<Franja>(1);
	}
	
	public void addHora(String ini, String fin){
		horas.add(new Franja(ini, fin));
	}
	
	@JsonGetter("dia")
	public int getDia_js(){
		return dia.ordinal();
	}
	public DIA getDia() {
		return dia;
	}

	public void setDia(DIA dia) {
		this.dia = dia;
	}

	public List<Franja> getHoras() {
		return horas;
	}

	public void setHoras(List<Franja> horas) {
		this.horas = horas;
	}
	
	public static Patron getGenerico(DIA dia){
		Patron p=new Patron(dia);
		p.addHora("08:00", "20:00");
		return p;
	}
	
	public static Patron getGenericoDescanso(DIA dia){
		Patron p=new Patron(dia);
		p.addHora("08:00", "13:30");
		p.addHora("16:00", "20:00");
		return p;
	}
}
