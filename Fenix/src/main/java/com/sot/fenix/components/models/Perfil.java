package com.sot.fenix.components.models;

import org.springframework.security.core.GrantedAuthority;
 
public class Perfil implements GrantedAuthority{
	public enum PERFILES{
		USER, ADMIN, ROOT
	}
	
    private String nombre;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Override
	public String getAuthority() {
		return "ROLE_"+nombre;
	}

	public Perfil(String nombre) {
		this.nombre = nombre;
	}
     
 

}