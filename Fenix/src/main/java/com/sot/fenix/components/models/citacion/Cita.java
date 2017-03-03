package com.sot.fenix.components.models.citacion;

import java.util.Date;
import java.util.LinkedHashMap;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.sot.fenix.components.json.CodigoDescripcionJSON;
import com.sot.fenix.components.models.Cliente;
import com.sot.fenix.components.models.Prestacion;
import com.sot.fenix.components.models.Usuario;
import com.sot.fenix.components.models.templates.AModelCentro;

@Document
@JsonIgnoreProperties(ignoreUnknown=true)
@CompoundIndexes({
	@CompoundIndex(name="idx_solapas",def="{fechaIni: 1, fechaFin: 1, centro: 1, profesional: 1, prestacion: 1}")
})
public class Cita extends AModelCentro{
	
	public enum ESTADO{PROGRAMADA,
						CAPTURANDO, 
						CAPTURADA, 
						NO_PRESENTADO}
	
	private ESTADO estado;

	
	@DateTimeFormat(pattern="dd/MM/yyyy HH:mm")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy HH:mm")
	@Indexed
	private Date fechaIni;
	
	@DateTimeFormat(pattern="dd/MM/yyyy HH:mm")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy HH:mm")
	@Indexed
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
	
	public boolean isCapturando(){
		return estado==ESTADO.CAPTURANDO;
	}
	

	public boolean isHistorico(){
		return false;
	}
	
}
