package com.sot.fenix.components.rest;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sot.fenix.components.json.CitasRequest;
import com.sot.fenix.components.json.ResponseJSON;
import com.sot.fenix.components.json.ResponseListJSON;
import com.sot.fenix.components.models.Cita;
import com.sot.fenix.components.services.CitaService;

@RestController
@RequestMapping("/cita")
public class CitaREST{
	
	@Autowired
	private CitaService citas;

	
	@RequestMapping(method=RequestMethod.PUT)
    public ResponseJSON<Cita> nueva(@RequestBody Cita cita) {
		System.out.println(cita);
		citas.getDAO().save(cita);
		System.out.println(cita);
		return new ResponseJSON<Cita>(ResponseJSON.OK, cita);	
    }
    
	@RequestMapping(method=RequestMethod.GET, path="/in")
    public List<Cita> modificar(@RequestParam("centro") String centro, @RequestParam("start") @DateTimeFormat(pattern="yyyy-MM-dd") Date start,  @RequestParam("end") @DateTimeFormat(pattern="yyyy-MM-dd") Date end) {
		List<Cita> res=citas.getDAO().findByFechaIniGreaterThanEqualAndFechaFinLessThanEqualAndCentro_id(start, end, new ObjectId(centro));
		return res;	
    }
	
	@RequestMapping(method=RequestMethod.POST, path="/all")
    public List<Cita> modificar(@RequestBody CitasRequest req) {
		List<Cita> res=citas.buscar(req);
		return res;	
	}
	
}

