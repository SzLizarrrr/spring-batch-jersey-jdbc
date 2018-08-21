package hello.jdbctemplate;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
@Path("/pet")
public class PetController {
	
	@Autowired
	PetDao petDao;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<Pet> getPet() {
		
		return petDao.listAllPet();
	}

}
