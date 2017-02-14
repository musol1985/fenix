package com.sot.fenix.components.models;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import com.sot.fenix.components.models.templates.AModelNombre;
import com.sot.fenix.components.models.templates.ICodDescr;

@Document
@CompoundIndexes({
    @CompoundIndex(name = "cliente_idx", def = "{'dni': 1, 'nombre': 1, 'apellido1': 1, 'apellido2': 1, 'telefono': 1}")
})
public class Cliente extends AModelNombre implements ICodDescr {


	private String dni;
	private String apellidos;
	private String telefono;
	
	private String gcm;
	private String correo;
	public String getDni() {
		return dni;
	}
	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
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
	
	public String getApellidos() {
		return apellidos;
	}
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	@Override
	public String getCodigo() {
		if(id!=null)
			return id.toHexString();
		return null;
	}
	@Override
	public String getDescripcion() {
		return nombre+" "+apellidos;
	}
}
