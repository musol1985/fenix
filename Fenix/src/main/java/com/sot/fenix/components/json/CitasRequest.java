package com.sot.fenix.components.json;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class CitasRequest {
	public String profesional;
	public String prestacion;
	public String centro;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	public Date start;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	public Date end;
}
