package com.sot.fenix.components.models;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.sot.fenix.components.json.CodigoDescripcionJSON;
import com.sot.fenix.components.models.templates.AModelBasic;

@Document
@JsonIgnoreProperties(ignoreUnknown=true)
public class Visita extends AModelBasic{
	
	public enum ESTADO{ PROCESO, 
						RPARCIAL, 
						RTOTAL}
	
	private ESTADO estado;
	
	@DBRef
	private Cliente cliente;
	
	@CreatedDate
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy HH:mm")
	private Date fecha;
	
	@CreatedBy	
	@DBRef
	private Usuario profesional;

	@DBRef
	private Cita cita;

	private Facturacion facturacion;

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


	public Usuario getProfesional() {
		return profesional;
	}

	public void setProfesional(Usuario profesional) {
		this.profesional = profesional;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Cita getCita() {
		return cita;
	}

	public void setCita(Cita cita) {
		this.cita = cita;
	}

	public Facturacion getFacturacion() {
		return facturacion;
	}

	public void setFacturacion(Facturacion facturacion) {
		this.facturacion = facturacion;
	}

	
	
}

