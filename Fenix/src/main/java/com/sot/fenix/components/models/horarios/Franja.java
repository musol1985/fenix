package com.sot.fenix.components.models.horarios;

public class Franja {
	private String ini;
	private String fin;

	
	public Franja(){
		
	}
	
	public Franja(String ini, String fin){
		this.ini=ini;
		this.fin=fin;
	}

	public String getIni() {
		return ini;
	}

	public void setIni(String ini) {
		this.ini = ini;
	}

	public String getFin() {
		return fin;
	}

	public void setFin(String fin) {
		this.fin = fin;
	}
}
