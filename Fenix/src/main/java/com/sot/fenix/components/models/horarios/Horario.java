package com.sot.fenix.components.models.horarios;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sot.fenix.components.models.AModelBasic;
import com.sot.fenix.components.models.Centro;
import com.sot.fenix.components.models.ICodDescr;

public class Horario extends AModelBasic implements ICodDescr{
	public enum TIPO{CENTRO, PROFESIONAL, PRESTACION};
	
	private String laborables;
	private String vacaciones;
	private String festivos;
	private boolean generico;
	
	@Override
	@JsonIgnore
	public String getCodigo() {
		return id.toHexString();
	}

	@Override
	public String getDescripcion() {
		return nombre;
	}


	public String getLaborables() {
		return laborables;
	}

	public void setLaborables(String laborables) {
		this.laborables = laborables;
	}

	public String getVacaciones() {
		return vacaciones;
	}

	public void setVacaciones(String vacaciones) {
		this.vacaciones = vacaciones;
	}

	public String getFestivos() {
		return festivos;
	}

	public void setFestivos(String festivos) {
		this.festivos = festivos;
	}
	
	
	public boolean isGenerico() {
		return generico;
	}

	public void setGenerico(boolean generico) {
		this.generico = generico;
	}

	public static Horario getGenerico(Centro c){
		Horario h=new Horario();
		
		h.setNombre("Generico");
		h.setLaborables("if((moment.month()+1)>=parseInt(1) && (moment.month()+1)<=parseInt(12)){\n     g++;\n       if((moment.isoWeekday())>=parseInt(1) && (moment.isoWeekday())<=parseInt(5)){\n       g++;\n         \taddHueco({s:'08:00',e:'12:00', id: id, color: color, m: moment, g:g});\n      \taddHueco({s:'17:00',e:'20:00', id: id, color: color, m: moment, g:g});\n    }\n  }\n");
		h.setFestivos("if(moment.date()==1 && moment.month()+1==1){\n  \taddHueco({s:'00:00',e:'23:59', id: id, color: color, m: moment, g:g});\n  }\n");
		h.setVacaciones("if((moment.month()+1)==parseInt(8)){\n  \taddHueco({s:'00:00',e:'23:59', id: id, color: color, m: moment, g:g});\n  }\n");
		h.setGenerico(true);
		h.centro=c;
		
		return h;
	}
}
