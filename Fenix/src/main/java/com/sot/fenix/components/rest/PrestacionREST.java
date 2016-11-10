package com.sot.fenix.components.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sot.fenix.components.models.Prestacion;
import com.sot.fenix.components.services.PrestacionService;
import com.sot.fenix.dao.PrestacionDAO;
import com.sot.fenix.templates.basic.ABasicREST;

@RestController
@RequestMapping("/prestacion")
public class PrestacionREST extends ABasicREST<PrestacionService, Prestacion, PrestacionDAO>{
	
	
}

