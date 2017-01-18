package com.sot.fenix.components.models;

import java.util.Date;
import java.util.LinkedHashMap;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.sot.fenix.components.json.CodigoDescripcionJSON;
import com.sot.fenix.components.models.templates.AModelId;

@Document
@JsonIgnoreProperties(ignoreUnknown=true)
public class Cita extends AModelId{
	
	public enum ESTADO{PROGRAMADA, 
						CAPTURADA, 
						NO_PRESENTADO}
	
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
	private Usuario profesional;
	
	@DBRef
	private Cliente cliente;
	
	private float importe;
	
	@CreatedDate
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy HH:mm")
	private Date fechaCrea;
	
	@CreatedBy	
	@DBRef
	private Usuario citador;


	public ESTADO getEstado() {
		return estado;
	}

	public void setEstado(ESTADO estado) {
		this.estado = estado;
	}
	
	@JsonGetter("className")
	public String getJsonClassName(){
		if(estado==null)
			return "";
		return "fc-"+estado.name().toLowerCase();
	}
	
	@JsonGetter("icono")
	public String getJsonIcono(){
		if(estado==null)
			return "";
		return "zmdi-"+estado.name().toLowerCase();
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
			centro.setId(new ObjectId(id));
		}
	}
	
	@JsonGetter("cliente")
	public CodigoDescripcionJSON getJsonCliente(){
		if(cliente==null)
			return new CodigoDescripcionJSON();
		CodigoDescripcionJSON cd=new CodigoDescripcionJSON(cliente);
		return cd;
	}
	
	@JsonSetter("cliente")
	public void setJsonCliente(Object id) {
		if(id!=null){
			if(id instanceof LinkedHashMap){				
				id=((LinkedHashMap) id).get("id");
			}
			if(id instanceof String){				
				if(!((String)id).isEmpty()){		
					cliente=new Cliente();
					cliente.setId(new ObjectId((String)id));
				}
			}else if(id instanceof Cliente){
				cliente=(Cliente)id;
			}
		}
	}

	
	@JsonSetter("prestacion")
	public void setJsonPrestacion(Object id) {
		if(id!=null){
			if(id instanceof LinkedHashMap){				
				id=((LinkedHashMap) id).get("id");
			}
			
			if(id instanceof String){								
				if(!((String)id).isEmpty()){
					prestacion=new Prestacion();
					prestacion.setId(new ObjectId((String)id));
				}
			}else if(id instanceof Prestacion){
				prestacion=(Prestacion)id;
			}
		}
	}
	
	@JsonGetter("prestacion")
	public CodigoDescripcionJSON getJsonPrestacion(){
		if(prestacion==null)
			return new CodigoDescripcionJSON();
		CodigoDescripcionJSON cd=new CodigoDescripcionJSON(prestacion);
		return cd;
	}
	
	@JsonSetter("profesional")
	public void setJsonProfesional(Object id) {
		if(id!=null){
			if(id instanceof LinkedHashMap){				
				id=((LinkedHashMap) id).get("id");
			}
			
			if(id instanceof String){
				if(!((String)id).isEmpty()){		
					profesional=new Usuario();
					profesional.setId(new ObjectId((String)id));
				}
			}else if(id instanceof Usuario){
				profesional=(Usuario)id;
			}
		}
	}
	
	@JsonGetter("profesional")
	public CodigoDescripcionJSON setJsonProfesional() {
		if(profesional==null)
			return new CodigoDescripcionJSON();
		CodigoDescripcionJSON cd=new CodigoDescripcionJSON(profesional);
		return cd;
	}

	public void setCentro(Centro centro) {
		this.centro = centro;
	}

	public Centro getCentro() {
		return centro;
	}
	
	

	public Usuario getProfesional() {
		return profesional;
	}

	public void setProfesional(Usuario profesional) {
		this.profesional = profesional;
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

	public float getImporte() {
		return importe;
	}

	public void setImporte(float importe) {
		this.importe = importe;
	}
	
	public boolean isProgramada(){
		return estado==ESTADO.PROGRAMADA;
	}
	

	/*@DBRef
	private Usuario profesional;
	
	@DBRef
	private Prestacion prestacion;
	
	
	
	
		


	@CreatedDate
	private Date fechaCreacion;*/
	
	
	
}
