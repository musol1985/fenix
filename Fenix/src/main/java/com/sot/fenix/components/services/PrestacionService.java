package com.sot.fenix.components.services;

import org.springframework.stereotype.Service;

import com.sot.fenix.components.models.Prestacion;
import com.sot.fenix.dao.PrestacionDAO;
import com.sot.fenix.templates.basic.ABasicService;

@Service
public class PrestacionService extends ABasicService<PrestacionDAO, Prestacion>{

}
