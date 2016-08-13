package com.sot.fenix.components.rest;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sot.fenix.components.json.CentroJSON;
import com.sot.fenix.components.json.ResponseJSON;
import com.sot.fenix.components.models.Centro;
import com.sot.fenix.components.models.Usuario;
import com.sot.fenix.components.models.UsuarioPendiente;
import com.sot.fenix.components.services.CentroService;
import com.sot.fenix.components.services.UsuarioService;

@RestController
@RequestMapping("/centro")
public class CentroREST{
	@Autowired
	private CentroService centros;
	@Autowired
	private UsuarioService usuarios;
	
	@RequestMapping(method=RequestMethod.GET)
    public String current() {
		return "OK";     
    }
	
	@RequestMapping(method=RequestMethod.POST,path="/nuevo")
    public ResponseJSON<CentroJSON> nuevo(@RequestBody CentroJSON centroJSON) {
		
		if(usuarios.getUsuarioByLogin(centroJSON.correoAdmin)==null){
			
			Centro centro=new Centro();
			
			centro.setCorreoAdmin(centroJSON.correoAdmin);
			centro.setNombre(centroJSON.nombre);
			centro.setPoblacion(centroJSON.poblacion);
			centro.setUsuarios(new ArrayList<Usuario>());
			
			centros.getDAO().save(centro);
			
			UsuarioPendiente usuario=usuarios.getUsuarioPendienteByCorreo(centroJSON.correoAdmin);
			
			if(usuario==null){
				usuario=new UsuarioPendiente();
				usuario.setCorreo(centroJSON.correoAdmin);
				usuario.setFechaEnvio(new Date());
				
				usuarios.getPendientesDAO().save(usuario);
			}
			
			usuarios.enviarEmail(usuario);
			
			return new ResponseJSON<CentroJSON>(ResponseJSON.OK, new CentroJSON(centro));
		}
		

		return new ResponseJSON<CentroJSON>(ResponseJSON.YA_EXISTE);
    }

}

