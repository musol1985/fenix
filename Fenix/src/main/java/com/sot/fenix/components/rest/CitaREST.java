package com.sot.fenix.components.rest;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sot.fenix.components.json.CitasRequest;
import com.sot.fenix.components.json.MaestrosCitaJSON;
import com.sot.fenix.components.json.ResponseJSON;
import com.sot.fenix.components.models.Cita;
import com.sot.fenix.components.models.Cita.ESTADO;
import com.sot.fenix.components.services.CitaService;
import com.sot.fenix.components.services.HorarioService;
import com.sot.fenix.components.services.PrestacionService;
import com.sot.fenix.components.services.UsuarioService;

@RestController
@RequestMapping("/cita")
public class CitaREST{
	
	public static final int RES_NO_CLIENTE=99;
	public static final int RES_NO_PRESTACION=98;
	public static final int RES_NO_PROFESIONAL=97;
	public static final int RES_TIENE_SOLAPA=96;
	
	@Autowired
	private CitaService citas;
	@Autowired
	private HorarioService horarios;
	@Autowired
	private UsuarioService profesionales;
	@Autowired
	private PrestacionService prestaciones;
	
	@RequestMapping(method=RequestMethod.PUT)
    public ResponseJSON<Cita> nueva(@RequestBody Cita cita) {
		if(cita.getCliente()==null)
			return new ResponseJSON<Cita>(RES_NO_CLIENTE, cita);
		if(cita.getPrestacion()==null)
			return new ResponseJSON<Cita>(RES_NO_PRESTACION, cita);
		if(cita.getProfesional()==null)
			return new ResponseJSON<Cita>(RES_NO_PROFESIONAL, cita);
		
		List<Cita> solapas=citas.getCitasSolapa(cita);
		
		if(solapas.size()>0){
			return new ResponseJSON<Cita>(RES_TIENE_SOLAPA, cita);
		}else{			
			cita=citas.crearCita(cita);
			return new ResponseJSON<Cita>(ResponseJSON.OK, cita);	
		}
    }
	
	@RequestMapping(method=RequestMethod.POST)
    public ResponseJSON<Cita> modificar(@RequestBody Cita cita) {
		if(cita.getCliente()==null)
			return new ResponseJSON<Cita>(RES_NO_CLIENTE, cita);
		if(cita.getPrestacion()==null)
			return new ResponseJSON<Cita>(RES_NO_PRESTACION, cita);
		if(cita.getProfesional()==null)
			return new ResponseJSON<Cita>(RES_NO_PROFESIONAL, cita);
		
		List<Cita> solapas=citas.getCitasSolapa(cita);
		
		if(solapas.size()>0 && !(solapas.get(0).getId().toHexString().equals(cita.getId().toHexString()))){
			return new ResponseJSON<Cita>(RES_TIENE_SOLAPA, cita);
		}else{			
			cita=citas.crearCita(cita);
			return new ResponseJSON<Cita>(ResponseJSON.OK, cita);	
		}
    }
    
	@RequestMapping(method=RequestMethod.GET, path="/in")
    public List<Cita> in(@RequestParam("centro") String centro, @RequestParam("start") @DateTimeFormat(pattern="yyyy-MM-dd") Date start,  @RequestParam("end") @DateTimeFormat(pattern="yyyy-MM-dd") Date end) {
		List<Cita> res=citas.getDAO().findByFechaIniGreaterThanEqualAndFechaFinLessThanEqualAndCentro_id(start, end, new ObjectId(centro));
		return res;	
    }
	
	@RequestMapping(method=RequestMethod.POST, path="/all")
    public List<Cita> modificar(@RequestBody CitasRequest req) {
		List<Cita> res=citas.buscar(req);
		return res;	
	}
	
	@RequestMapping(method=RequestMethod.GET, path="/maestros/{centro}")
    public MaestrosCitaJSON modificar(@PathVariable("centro") String centro) {
		MaestrosCitaJSON maestros=new MaestrosCitaJSON();
		
		ObjectId centroId=new ObjectId(centro);
		
		maestros.prestaciones=prestaciones.getDAO().findByCentro_id(centroId);
		maestros.horarios=horarios.getDAO().findByCentro_id(centroId);
		maestros.profesionales=profesionales.getUsuarioByCentro(centro);
		
		return maestros;
    }
	
}

