package test.fenix;

import java.io.IOException;
import java.nio.charset.Charset;

import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class TestUtils {

	    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
	 
	    public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
	        ObjectMapper mapper = new ObjectMapper();

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
	
}
