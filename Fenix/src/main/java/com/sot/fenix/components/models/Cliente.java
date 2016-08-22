package com.sot.fenix.components.models;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

@Document
@CompoundIndexes({
    @CompoundIndex(name = "cliente_idx", def = "{'dni': 1, 'nombre': 1, 'apellido1': 1, 'apellido2': 1, 'telefono': 1}")
})
public class Cliente extends AModelId {


	private String dni;
	private String nombre;
	private String apellido1;
	private String apellido2;
	private int telefono;
	
	@Indexed
	@DBRef
	private Centro centro;
	
	private String gcm;
	private String correo;
	public String getDni() {
		return dni;
	}
	public void setDni(String dni) {
		this.dni = dni;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido1() {
		return apellido1;
	}
	public void setApellido1(String apellido1) {
		this.apellido1 = apellido1;
	}
	public String getApellido2() {
		return apellido2;
	}
	public void setApellido2(String apellido2) {
		this.apellido2 = apellido2;
	}
	public int getTelefono() {
		return telefono;
	}
	public void setTelefono(int telefono) {
		this.telefono = telefono;
	}
	public Centro getCentro() {
		return centro;
	}
	public void setCentro(Centro centro) {
		this.centro = centro;
	}
	public String getGcm() {
		return gcm;
	}
	public void setGcm(String gcm) {
		this.gcm = gcm;
	}
	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
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

}
