package v.rabetsky.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import v.rabetsky.dto.AnimalDTO;
import v.rabetsky.models.filters.AnimalFilter;
import v.rabetsky.models.entities.Animal;

import java.util.List;

@Slf4j
@Component
public class AnimalDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AnimalDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Animal> findByFilter(AnimalFilter f) {
        List<Animal> animals =  jdbcTemplate.query(
                "SELECT * FROM get_animals(?, ?, ?, ?, ?) ORDER BY id",
                new BeanPropertyRowMapper<>(Animal.class),
                f.getNickname(),
                f.getAnimalTypeIds(),
                f.getGenders(),
                f.getNeedsWarmHousing(),
                f.getCageIds()
        );
        log.info("Filters: {}, {}, {}, {}, {}", f.getNickname(),
                f.getAnimalTypeIds(),
                f.getGenders(),
                f.getNeedsWarmHousing(),
                f.getCageIds());
        return animals;
    }

    public Animal show(int id) {
        Animal animal = jdbcTemplate.queryForObject("SELECT * FROM animals WHERE id = ? ORDER BY id",
                new BeanPropertyRowMapper<>(Animal.class), id);
        log.info("Animals: показ животного id={}", id);
        return animal;
    }

    public void update(int id, Animal animal) {
        jdbcTemplate.update(
                "UPDATE animals SET nickname = ?, gender = ?, arrival_date = ?, needs_warm_housing = ?, animal_type_id = ?, cage_id = ? WHERE id = ?",
                animal.getNickname(), animal.getGender(), animal.getArrivalDate(), animal.isNeedsWarmHousing(), animal.getAnimalTypeId(), animal.getCageId(), id
        );
        log.info("Animals: обновлено животное id={}", id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM animals WHERE id = ?", id);
        log.info("Animals: удалено животное id={}", id);
    }

    public int saveReturningId(Animal a) {
        log.info("Animals: добавлено новое животное");
        return jdbcTemplate.queryForObject("INSERT INTO animals (nickname, gender, arrival_date, needs_warm_housing, animal_type_id, cage_id) " +
                        "VALUES (?,?,?,?,?,?) RETURNING id", Integer.class,
                a.getNickname(), a.getGender(), a.getArrivalDate(),
                a.isNeedsWarmHousing(), a.getAnimalTypeId(), a.getCageId()
        );
    }

    public void updateCage(int animalId, int newCageId) {
        jdbcTemplate.update(
                "UPDATE animals SET cage_id = ? WHERE id = ?",
                newCageId, animalId
        );
    }

    public List<AnimalDTO> findByCage(int cageId) {
        log.info("Animals: получили список животных из клетки={}", cageId);
        return jdbcTemplate.query("SELECT * FROM animals WHERE cage_id = ?",
                new BeanPropertyRowMapper<>(AnimalDTO.class), cageId);
    }

    public void moveToCage(int animalId, int newCageId) {
        jdbcTemplate.update(
                "UPDATE animals SET cage_id = ? WHERE id = ?",
                newCageId, animalId
        );
    }
}
