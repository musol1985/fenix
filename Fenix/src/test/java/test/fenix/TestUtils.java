package test.fenix;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sot.fenix.components.models.Centro;
import com.sot.fenix.components.models.Centro.TIPO;
import com.sot.fenix.components.models.Cita;
import com.sot.fenix.components.models.Cita.ESTADO;
import com.sot.fenix.components.models.Cliente;
import com.sot.fenix.components.models.Perfil.PERFILES;
import com.sot.fenix.components.models.Prestacion;
import com.sot.fenix.components.models.Ubicacion;
import com.sot.fenix.components.models.Usuario;
import com.sot.fenix.components.models.Visita;
import com.sot.fenix.components.models.templates.AModelCentro;
import com.sot.fenix.components.models.templates.AModelNombre;
import com.sot.fenix.dao.CitaDAO;

public class TestUtils {

	    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
	 
	    public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
	        ObjectMapper mapper = new ObjectMapper();
	        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
	        //mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	        return mapper.writeValueAsBytes(object);
	    }
	    
	    public static String convertObjectToString(Object object) throws IOException {
	        ObjectMapper mapper = new ObjectMapper();

	        return mapper.writeValueAsString(object);
	    }
	    
	    public static <T> T convertStringToObject(String object, Class<T> obj) throws IOException {
	        ObjectMapper mapper = new ObjectMapper();

	        return mapper.readValue(object, obj);
	    }

	
	    public static <T extends AModelCentro> T generarModelCentro(Centro centro, Class<T> clase){
	    	try{
	    		AModelCentro modelCentro=clase.newInstance();
	    		modelCentro.setCentro(centro);
	    		
	    		return (T)modelCentro;
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}
	    	return null;
	    }
	    
	    
	    public static <T extends AModelNombre> T generarModelNombreCentro(Centro centro, String descripcion, Class<T> clase){
	    	T res=(T)generarModelCentro(centro, clase);
	    	res.setNombre(descripcion);
	    	return res;
	    }
	    
	    
	    public static Centro getNewCentro(){
	    	Centro centro=new Centro();
	    	centro.setColor("purple");
			centro.setCorreoAdmin("test@test.com");
			centro.setNombre("Centro test");
			centro.setTipo(TIPO.SANIDAD);

			centro.setUbicacion(getNewUbicacion());
			
			return centro;
	    }
	    
	    public static Ubicacion getNewUbicacion(){			
			Ubicacion u=new Ubicacion();
			u.setCalle("Calle test");
			u.setCP("08292");
			u.setNumero("33");
			u.setPais("ES");
			u.setPoblacion("Terrassa");
			u.setProvincia("Barcelona");
			u.setPosicion(new GeoJsonPoint(1, 1));
			return u;
	    }
	    
	    public static Usuario getNewUsuario(String nombre, Centro centro){
			Usuario usuario=new Usuario();
			usuario.setCorreo("test"+nombre);
			usuario.setNombre(nombre);
			usuario.setPassword("pass");
			usuario.setPerfil(PERFILES.ROOT);
			usuario.setCentro(centro);
			
			return usuario;		
		}
	    
	    public static Usuario getNewUsuarioNoRoot(String nombre, Centro centro){
	    	Usuario usuario=new Usuario();
			usuario.setCorreo("testUsuario"+nombre);
			usuario.setNombre(nombre);
			usuario.setPassword("pass");
			usuario.setPerfil(PERFILES.USER);
			usuario.setCentro(centro);
			return usuario;
	    }
	    
	    public static Cliente getCliente(Centro centro, String dni, String nombre, String apellidos){
	    	return getCliente(centro, dni, nombre, apellidos, "test@correo.com");
	    }
	    
	    public static Cliente getCliente(Centro centro, String dni, String nombre, String apellidos, String correo){
	    	return getCliente(centro, dni, nombre, apellidos, correo, "617295088");
	    }
	    
	    public static Cliente getCliente(Centro centro, String dni, String nombre, String apellidos, String correo, String telefono){
			Cliente c3=new Cliente();
			c3.setId(null);
			c3.setNombre(nombre);
			c3.setApellidos(apellidos);
			c3.setCentro(centro);
			c3.setCorreo(correo);
			c3.setDni(dni);
			c3.setTelefono(telefono);
			return c3;
		}
		
	    public static Cliente getNewCliente(Centro centro){
			Cliente c3=new Cliente();
			c3.setId(null);
			c3.setNombre("juan2");
			c3.setApellidos("Martinc3");
			c3.setCentro(centro);
			c3.setCorreo("juan@gmail.com");
			c3.setDni("123");
			c3.setTelefono("6123");
			return c3;
		}
	    
	    public static Visita getNewVisita(Centro centro){
	    	Visita v=new Visita();
	    	v.setCentro(centro);
	    	return v;
	    }
	    
	    
	    public static Cita getCita(Centro centro, int offset){
			Cita c=new Cita();
			c.setCentro(centro);
			c.setFechaIni(getDate(offset));
			c.setFechaFin(getDate(offset+30));
			c.setEstado(ESTADO.PROGRAMADA);
			return c;
			
		}
	    
	    public static Cita getSavedCita(CitaDAO dao, Cliente cliente, Prestacion prestacion, Centro centro, Usuario profesional, int offset){
	    	Cita c=getCita(centro, offset);
	    	c.setCliente(cliente);
	    	c.setPrestacion(prestacion);
	    	c.setProfesional(profesional);
	    	c.setImporte(300);
	    	c.setEstado(ESTADO.PROGRAMADA);
	    	dao.save(c);
	    	return c;
	    }
		
		public static Cita getSavedCita(CitaDAO dao, Centro centro, Cliente cliente, Prestacion prestacion, Usuario usuario, Date fechaIni, Date fechaFin){
			Cita c=new Cita();
			c.setCentro(centro);
			c.setFechaIni(fechaIni);
			c.setFechaFin(fechaFin);
			c.setCliente(cliente);
			c.setPrestacion(prestacion);
			c.setProfesional(usuario);
			c.setImporte(300);
			c.setEstado(ESTADO.PROGRAMADA);
			if(dao!=null)
				dao.save(c);
			return c;
			
		}
		
		public static Prestacion getNewPrestacion(Centro centro){
			Prestacion p=new Prestacion();
			p.setCentro(centro);
			p.setImporte(50f);
			p.setNombre("Prueba");
			return p;
		}
		
		public static Date getDate(int offset){
			Calendar date = Calendar.getInstance();
			long t= date.getTimeInMillis();
			return new Date(t + (offset * 60000));
		}
		

		
		public static Date toDate(String date){
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			try {
				return formatter.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		public static String fromDate(Date date){
			DateFormat df = new SimpleDateFormat("yyyyMMdd");
	   
			return df.format(date);
		}
		
		public static String fromDateCalendar(Date date){
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	   
			return df.format(date);
		}
}
