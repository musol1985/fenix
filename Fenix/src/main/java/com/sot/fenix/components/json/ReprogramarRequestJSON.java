package com.sot.fenix.components.json;

import com.sot.fenix.components.models.Cita;

public class ReprogramarRequestJSON {
	public Cita cita;
	public boolean forzar;
	public Cita getCita() {
		return cita;
	}
	public void setCita(Cita cita) {
		this.cita = cita;
	}
	public boolean isForzar() {
		return forzar;
	}
	public void setForzar(boolean forzar) {
		this.forzar = forzar;
	}
	
	
}
