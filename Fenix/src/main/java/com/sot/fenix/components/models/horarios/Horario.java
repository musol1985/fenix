package com.sot.fenix.components.models.horarios;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sot.fenix.components.models.AModelBasic;
import com.sot.fenix.components.models.ICodDescr;

public class Horario extends AModelBasic implements ICodDescr{
	public enum TIPO{CENTRO, PRESTACION, PROFESIONAL}
	
	private String laborables;
	private String vacaciones;
	private String festivos;
	private TIPO tipo;
	

	
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

	public TIPO getTipo() {
		return tipo;
	}

	public void setTipo(TIPO tipo) {
		this.tipo = tipo;
	}
	
}
