package com.sot.fenix.components.services;

import org.springframework.stereotype.Service;

import com.sot.fenix.components.models.horarios.Horario;
import com.sot.fenix.dao.HorarioDAO;
import com.sot.fenix.templates.basic.ABasicService;

@Service
public class HorarioService extends ABasicService<HorarioDAO, Horario>{

}
