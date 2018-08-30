package hello.jdbctemplate;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Path("/pet")
public class PetController {
	
	@Autowired
	PetDao petDao;
	
	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<Pet> getAllPet() {
		return petDao.getAllPet();
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Pet getPetById(@PathParam("id") Integer id) {
		return petDao.getPetById(id);
	}
	
	@GET
	@Path("/range")
	@Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<Pet> getRangePet() {
		return petDao.getRangePet();
	}
	
	@POST
	public int addPet(Pet pet) {
		return petDao.addPet(pet);
	}

}
