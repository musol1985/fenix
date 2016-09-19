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
	
	@DBRef
	private Prestacion prestacion;
	
	@DBRef
	private Cliente cliente;

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
	
	@JsonGetter("cliente")
	public String getJsonCliente(){
		if(cliente==null)
			return "";
		return cliente.getId().toHexString();
	}
	
	@JsonSetter("cliente")
	public void setJsonCliente(String id) {
		if(id!=null && !id.isEmpty()){
			cliente=new Cliente();
			cliente.id=new ObjectId(id);
		}
	}
	
	@JsonGetter("title")
	public String setJsonTitle() {
		if(cliente!=null)
			return cliente.getNombre()+" "+cliente.getApellidos();
		return "";
	}
	
	@JsonSetter("prestacion")
	public void setJsonPrestacion(String id) {
		if(id!=null && !id.isEmpty()){
			prestacion=new Prestacion();
			prestacion.id=new ObjectId(id);
		}
	}
	
	@JsonGetter("prestacion")
	public String getJsonPrestacion(){
		if(prestacion==null)
			return "";
		return prestacion.getId().toHexString();
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
	
	public Prestacion getPrestacion() {
		return prestacion;
	}

	public void setPrestacion(Prestacion prestacion) {
		this.prestacion = prestacion;
	}
	
	@JsonGetter("color")
	public String getJsonColor(){
		if(prestacion!=null)
			return prestacion.getColor();
		return "";
	}

	@JsonGetter("start")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm")
	public Date getJsonStart(){
		return fechaIni;
	}
	
	@JsonGetter("end")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm")
	public Date getJsonEnd(){
		return fechaFin;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
	
	
	/*@DBRef
	private Usuario profesional;
	
	@DBRef
	private Prestacion prestacion;
	
	
	
	
		


	@CreatedDate
	private Date fechaCreacion;*/
	
	
	
}
