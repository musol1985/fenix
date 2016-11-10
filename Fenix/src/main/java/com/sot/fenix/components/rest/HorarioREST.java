package com.sot.fenix.components.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sot.fenix.components.models.horarios.Horario;
import com.sot.fenix.components.services.HorarioService;
import com.sot.fenix.dao.HorarioDAO;
import com.sot.fenix.templates.basic.ABasicREST;

@RestController
@RequestMapping("/horario")
public class HorarioREST extends ABasicREST<HorarioService, Horario, HorarioDAO>{
	
}

