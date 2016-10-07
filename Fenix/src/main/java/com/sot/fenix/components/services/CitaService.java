package com.sot.fenix.components.services;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.MongoRegexCreator;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.query.parser.Part;
import org.springframework.stereotype.Service;

import com.sot.fenix.components.json.CitasRequest;
import com.sot.fenix.components.models.Cita;
import com.sot.fenix.components.models.Cliente;
import com.sot.fenix.dao.CitaDAO;

@Service
public class CitaService {
	@Autowired
	private CitaDAO dao;
	@Autowired
	private MongoTemplate template;
	
	
	public List<Cliente> buscar(String texto, String centro){
		
		String regexp=MongoRegexCreator.INSTANCE.toRegularExpression(texto, Part.Type.EXISTS);
		Query q=new Query(Criteria.where("centro.id").is(new ObjectId(centro)).orOperator(
				Criteria.where("dni").regex(regexp, "i"),	
				Criteria.where("nombre").regex(regexp, "i"),				
				Criteria.where("apellido1").regex(regexp, "i"),
				Criteria.where("apellido2").regex(regexp, "i"),
				Criteria.where("telefono").regex(regexp, "i")));			
		
		
		return template.find(q, Cliente.class);
	}
	
	
	public List<Cita> buscar(CitasRequest req){
		if(req.profesional!=null && req.prestacion!=null){
			return dao.findByFechaIniGreaterThanEqualAndFechaFinLessThanEqualAndCentro_idAndProfesional_idAndPrestacion_id(req.start, req.end, new ObjectId(req.centro), new ObjectId(req.profesional), new ObjectId(req.prestacion));
		}else if(req.profesional!=null){
			return dao.findByFechaIniGreaterThanEqualAndFechaFinLessThanEqualAndCentro_idAndProfesional_id(req.start, req.end, new ObjectId(req.centro), new ObjectId(req.profesional));
		}else if(req.prestacion!=null){
			return dao.findByFechaIniGreaterThanEqualAndFechaFinLessThanEqualAndCentro_idAndPrestacion_id(req.start, req.end, new ObjectId(req.centro), new ObjectId(req.prestacion));
		}else{
			return dao.findByFechaIniGreaterThanEqualAndFechaFinLessThanEqualAndCentro_id(req.start, req.end, new ObjectId(req.centro));
		}
	}
	
	public CitaDAO getDAO(){
		return dao;
	}
}
