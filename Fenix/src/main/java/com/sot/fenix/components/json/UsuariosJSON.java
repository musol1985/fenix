package com.sot.fenix.components.json;

import com.sot.fenix.components.models.Usuario;
import com.sot.fenix.components.models.UsuarioPendiente;

public class UsuariosJSON{
	private String id;
	private String correo;
	private String nombre;
	private boolean admin;
	private boolean pendiente;
	
	public UsuariosJSON(Usuario u){
		id=u.getId().toHexString();
		correo=u.getUsername();
		nombre=u.getNombre();
		admin=u.isAdmin();
		pendiente=false;
	}
	
	public UsuariosJSON(UsuarioPendiente u){
		id=u.getId();
		correo=u.getCorreo();
		nombre=u.getNombre();
		admin=false;
		pendiente=true;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public boolean isPendiente() {
		return pendiente;
	}

	public void setPendiente(boolean pendiente) {
		this.pendiente = pendiente;
	}


}
