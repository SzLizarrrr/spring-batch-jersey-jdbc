package hello.jdbctemplate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	
	public List<Pet> getRangePet() {
		Set<Integer> ids = new HashSet<Integer>();
		ids.add(1);
		ids.add(2);
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("ids", ids);
		return namedParameterJdbcTemplate.query("SELECT * FROM PET WHERE ID IN(:ids)", parameters, (rs, rowNum) -> new Pet(rs.getInt("id"), rs.getString("type")));
	}
	
	public Pet getPetById(int id) {
		log.info("id: {}", id);
		Pet pet = new Pet();
		pet.setId(id);
		SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(pet);
//		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("id", id);
//		Map namedParameters = new HashMap(); // normal map need return (Pet) named...
//		namedParameters.put("id", id);
		return namedParameterJdbcTemplate.queryForObject("SELECT * FROM PET WHERE ID = :id", namedParameters, (rs, rowNum) -> new Pet(rs.getInt("id"), rs.getString("type")));
	}
	
	public int addPet(Pet pet) {
		return jdbcTemplate.update("INSERT INTO PET(type) VALUES(?)", pet.getType());
	}
	

}
