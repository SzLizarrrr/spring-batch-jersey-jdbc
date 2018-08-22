package hello.jdbctemplate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class PetDao {
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	public List<Pet> getAllPet() {
		return jdbcTemplate.query("SELECT * FROM PET", (rs, rowNum) -> new Pet(rs.getInt("id"), rs.getString("type")));
	}
	
	public Pet getPetById(int id) {
		log.info("id: {}", id);
		Pet pet = new Pet();
		pet.setId(id);
		SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(pet);
//		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("id", id);
		return namedParameterJdbcTemplate.queryForObject("SELECT * FROM PET WHERE ID = :id", namedParameters, (rs, rowNum) -> new Pet(rs.getInt("id"), rs.getString("type")));
	}
	
	public int addPet(Pet pet) {
		return jdbcTemplate.update("INSERT INTO PET(type) VALUES(?)", pet.getType());
	}
	

}
