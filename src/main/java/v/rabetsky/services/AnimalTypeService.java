package v.rabetsky.services;

import org.springframework.stereotype.Service;
import v.rabetsky.dao.AnimalDAO;
import v.rabetsky.dao.AnimalTypeDAO;
import v.rabetsky.dao.DietTypeDAO;
import v.rabetsky.dto.AnimalDTO;
import v.rabetsky.dto.AnimalTypeDTO;
import v.rabetsky.models.entities.DietType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AnimalTypeService {
    private final AnimalTypeDAO animalTypeDAO;
    private final AnimalDAO animalDAO;
    private final DietTypeDAO dietTypeDAO;

    public AnimalTypeService(AnimalTypeDAO atDAO, AnimalDAO aDAO, DietTypeDAO dDAO) {
        this.animalTypeDAO = atDAO;
        this.animalDAO = aDAO;
        this.dietTypeDAO = dDAO;
    }

    public List<AnimalTypeDTO> findAll() {
        return animalTypeDAO.findAllAnimalTypes();
    }

    public Map<Integer, List<AnimalDTO>> getAnimalsByType() {
        return findAll().stream()
                .collect(Collectors.toMap(
                        AnimalTypeDTO::getId,
                        t -> animalDAO.findByType(t.getId())
                ));
    }

    public List<DietType> getAllDietTypes() {
        return dietTypeDAO.findAll();
    }

    public void addType(String type, int dietTypeId) {
        animalTypeDAO.save(type, dietTypeId);
    }

    public void deleteType(int id) {
        if (animalTypeDAO.hasAnimals(id)) {
            throw new IllegalStateException("Нельзя удалить вид, к которому привязаны животные");
        }
        animalTypeDAO.delete(id);
    }
}