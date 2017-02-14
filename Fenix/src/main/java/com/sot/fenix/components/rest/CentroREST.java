package com.sot.fenix.components.rest;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sot.fenix.components.json.NuevoCentroJSON;
import com.sot.fenix.components.json.PageJSON;
import com.sot.fenix.components.json.ResponseJSON;
import com.sot.fenix.components.json.ResponseListJSON;
import com.sot.fenix.components.models.Centro;
import com.sot.fenix.components.models.UsuarioPendiente;
import com.sot.fenix.components.services.CentroService;
import com.sot.fenix.components.services.UsuarioService;
import com.sot.fenix.dao.CentroDAO;
import com.sot.fenix.templates.REST.ABasicREST;

@RestController
@RequestMapping("/centro")
public class CentroREST extends ABasicREST<CentroService, Centro, CentroDAO>{
	final static Logger log = LogManager.getLogger(CentroREST.class);
	
	@Autowired
	private UsuarioService usuarios;
	
	@RequestMapping(method=RequestMethod.GET)
    public String current() {
		return "OK";     
    }
	
	@Override
	@RequestMapping(method=RequestMethod.DELETE, path="/{id}")
    public ResponseJSON<Centro> eliminar(@PathVariable String id){
		return new ResponseJSON<Centro>(ResponseJSON.ACCION_PROHIBIDA_REST);
	}
	
	/*@Override
	@RequestMapping(method=RequestMethod.POST)
    public ResponseJSON<Centro> modificar(@RequestBody Centro item) {
		return new ResponseJSON<Centro>(ResponseJSON.ACCION_PROHIBIDA_REST);
	}*/
	
	@RequestMapping(method=RequestMethod.POST,path="/nuevo")
    public ResponseJSON<Centro> nuevo(@RequestBody NuevoCentroJSON nuevoCentro) {
		
		if(usuarios.getUsuarioByCorreo(nuevoCentro.centro.getCorreoAdmin())==null){
			
			nuevoCentro.centro.getUbicacion().setPosicion(new GeoJsonPoint(nuevoCentro.posicion.lat, nuevoCentro.posicion.lng));
			//nuevoCentro.centro.setHorario(HorarioOld.getGenerico());
			
			service.getDAO().save(nuevoCentro.centro);
			
			UsuarioPendiente usuario=usuarios.getUsuarioPendienteByCorreo(nuevoCentro.centro.getCorreoAdmin());
			
			if(usuario==null){
				usuario=new UsuarioPendiente();
				usuario.setCorreo(nuevoCentro.centro.getCorreoAdmin());
				usuario.setNombre(nuevoCentro.nombreAdmin);
				usuario.setFechaEnvio(new Date());
				usuario.setCentro(nuevoCentro.centro);
				
				usuarios.getPendientesDAO().save(usuario);
			}
			
			usuarios.enviarEmail(usuario);
			
			return new ResponseJSON<Centro>(ResponseJSON.OK, nuevoCentro.centro);
		}
		

		return new ResponseJSON<Centro>(ResponseJSON.YA_EXISTE);
    }
	
	
	@RequestMapping(method=RequestMethod.GET, path="/all/{page}/{size}")
    public PageJSON<Centro> getAll(@PathVariable int page, @PathVariable int size) {
		Page<Centro> centros=this.service.getDAO().findAll(new PageRequest(page-1, size));   

		return new PageJSON<Centro>(centros.getTotalElements(), centros.getContent());
    }
	
	@RequestMapping(method=RequestMethod.GET, path="/all")
    public ResponseListJSON<Centro> getAll() {
		List<Centro> centros=this.service.getDAO().findAll();   

		return new ResponseListJSON<Centro>(ResponseJSON.OK, centros);
    }

}

