package com.sot.fenix.components.json;

import com.sot.fenix.components.models.Usuario;

public class UsuarioJSON{
	private String id;
	private String correo;
	private String nombre;
	private boolean admin;
	private boolean root;
	
	public UsuarioJSON(Usuario u){
		this.id=u.getId().toHexString();
		this.correo=u.getUsername();
		this.nombre=u.getNombre();
		this.admin=u.isAdmin();
		this.root=u.isRoot();
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

	public boolean isRoot() {
		return root;
	}

	public void setRoot(boolean root) {
		this.root = root;
	}


}
