package com.sot.fenix.components.models;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;


public class Ubicacion{

	private String calle;
	private String numero;
	private String poblacion;
	private String provincia;
	private String pais;
	private String id;
	private String CP;
	private GeoJsonPoint posicion;
	
	public Ubicacion(){
		
	}

	public String getCalle() {
		return calle;
	}
	public void setCalle(String calle) {
		this.calle = calle;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getPoblacion() {
		return poblacion;
	}
	public void setPoblacion(String poblacion) {
		this.poblacion = poblacion;
	}
	public String getProvincia() {
		return provincia;
	}
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	public String getPais() {
		return pais;
	}
	public void setPais(String pais) {
		this.pais = pais;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getCP() {
		return CP;
	}

	public void setCP(String cP) {
		CP = cP;
	}

	public GeoJsonPoint getPosicion() {
		return posicion;
	}

	public void setPosicion(GeoJsonPoint posicion) {
		this.posicion = posicion;
	}
	
	
	
}
