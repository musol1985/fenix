package com.sot.fenix.components.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sot.fenix.components.models.Perfil.PERFILES;

@Document
@JsonIgnoreProperties({ "authorities" })
public class Usuario extends AModelId implements UserDetails, IUsuario {

	@Indexed(unique = true)
	private String correo;
	
	private String nombre;
	
	@JsonIgnore
	private String password;
	
	@Indexed
	@DBRef
	private Centro centro;
	
	private boolean accountNonExpired = true;
	private boolean accountNonLocked = true;
	private boolean credentialsNonExpired = true;
	private boolean enabled = true;

	private PERFILES perfil=PERFILES.USER;

	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public String getPassword() {
		return password;
	}


	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {		
		List<Perfil> perfiles=new ArrayList<Perfil>(3);
		
			
		if(perfil==PERFILES.ADMIN)
			perfiles.add(new Perfil(perfil.name()));
			perfiles.add(new Perfil(PERFILES.USER.name()));	
		if(perfil==PERFILES.ROOT){
			perfiles.add(new Perfil(perfil.name()));
			perfiles.add(new Perfil(PERFILES.ADMIN.name()));
			perfiles.add(new Perfil(PERFILES.USER.name()));	
		}else{
			perfiles.add(new Perfil(PERFILES.USER.name()));	
		}
		
		return perfiles;
	}

	public PERFILES getPerfil() {
		return perfil;
	}

	public void setPerfil(PERFILES perfil) {
		this.perfil = perfil;
	}

	public boolean isAdmin(){
		return perfil==PERFILES.ADMIN || perfil==PERFILES.ROOT;
	}
	
	public boolean isRoot(){
		return perfil==PERFILES.ROOT;
	}

	public Centro getCentro() {
		return centro;
	}

	public void setCentro(Centro centro) {
		this.centro = centro;
	}

	@Override
	public String getUsername() {
		return correo;
	}
	

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	@Override
	public boolean isPendiente() {
		return false;
	}
	
}
