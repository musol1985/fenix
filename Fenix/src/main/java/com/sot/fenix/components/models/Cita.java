package com.sot.fenix.components.models;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

@Document
public class Cita extends AModelId{
	
	public enum ESTADO{PROGRAMADA, CANCELADA, REPROGRAMADA, CAPTURADA, NO_PRESENTADO}
	
	private ESTADO estado;
	
	@DBRef
	private Centro centro;
	
	@DateTimeFormat(pattern="dd/MM/yyyy HH:mm")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy HH:mm")
	private Date fechaIni;
	
	@DateTimeFormat(pattern="dd/MM/yyyy HH:mm")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy HH:mm")
	private Date fechaFin;
	
	

	public ESTADO getEstado() {
		return estado;
	}

	public void setEstado(ESTADO estado) {
		this.estado = estado;
	}
	
	@JsonGetter("profesional")
	public String getJsonProfesional(){
		return "Eduardo Martin";
	}
	
	@JsonGetter("centro")
	public String getJsonCentro(){
		if(centro==null)
			return "";
		return centro.getId().toHexString();
	}
	
	@JsonSetter("centro")
	public void setJsonCentro(String id) {
		if(id!=null && !id.isEmpty()){
			centro=new Centro();
			centro.id=new ObjectId(id);
		}
	}

	public void setCentro(Centro centro) {
		this.centro = centro;
	}

	public Centro getCentro() {
		return centro;
	}

	public Date getFechaIni() {
		return fechaIni;
	}

	public void setFechaIni(Date fechaIni) {
		this.fechaIni = fechaIni;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}
	
	
	
	
	
	/*@DBRef
	private Usuario profesional;
	
	@DBRef
	private Prestacion prestacion;
	
	
	
	
		


	@CreatedDate
	private Date fechaCreacion;*/
	
	
	
}
