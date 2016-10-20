package com.sot.fenix.components.models.horarios;

public class Franja {
	private Hora ini;
	private Hora fin;

	
	public Franja(){
		
	}
	
	public Franja(Hora ini, Hora fin){
		this.ini=ini;
		this.fin=fin;
	}

	public Hora getIni() {
		return ini;
	}

	public void setIni(Hora ini) {
		this.ini = ini;
	}

	public Hora getFin() {
		return fin;
	}

	public void setFin(Hora fin) {
		this.fin = fin;
	}
}
