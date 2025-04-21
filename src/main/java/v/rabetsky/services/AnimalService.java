package v.rabetsky.services;

import org.springframework.stereotype.Service;
import v.rabetsky.dao.AnimalDAO;
import v.rabetsky.dao.AnimalTypeDAO;
import v.rabetsky.dao.DietTypeDAO;
import v.rabetsky.dto.AnimalDTO;
import v.rabetsky.models.AnimalFilter;
import v.rabetsky.models.entities.Animal;
import v.rabetsky.models.entities.AnimalType;
import v.rabetsky.models.entities.DietType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AnimalService {
    private final AnimalDAO animalDAO;
    private final AnimalTypeDAO animalTypeDAO;
    private final DietTypeDAO dietTypeDAO;

    public AnimalService(AnimalDAO animalDAO, AnimalTypeDAO animalTypeDAO, DietTypeDAO dietTypeDAO) {
        this.animalDAO = animalDAO;
        this.animalTypeDAO = animalTypeDAO;
        this.dietTypeDAO = dietTypeDAO;
    }

    public List<AnimalDTO> getAllAnimals(AnimalFilter filter) {
        List<Animal> animals = animalDAO.findByFilter(filter);
        List<AnimalType> animalTypesList = animalTypeDAO.findAll();
        List<DietType> dietTypesList = dietTypeDAO.findAll();

        Map<Integer, String> animalTypes = animalTypesList.stream()
                .collect(Collectors.toMap(AnimalType::getId, AnimalType::getType));
        Map<Integer, String> dietTypes = dietTypesList.stream()
                .collect(Collectors.toMap(DietType::getId, DietType::getType));
        Map<Integer, String> animalTypeToDietType = animalTypesList.stream()
                .collect(Collectors.toMap(
                        AnimalType::getId,
                        at -> dietTypes.get(at.getDietTypeId())
                ));


        return animals.stream()
                .map(a -> AnimalDTO.builder()
                        .id(a.getId())
                        .nickname(a.getNickname())
                        .gender(a.getGender())
                        .arrivalDate(a.getArrivalDate())
                        .needsWarmHousing(a.isNeedsWarmHousing())
                        .animalTypeId(a.getAnimalTypeId())
                        .animalTypeName(animalTypes.get(a.getAnimalTypeId()))
                        .animalDietType(animalTypeToDietType.get(a.getAnimalTypeId()))
                        .cageId(a.getCageId())
                        .build()
                )
                .collect(Collectors.toList());
    }

    public AnimalDTO findById(Integer id) {
        Animal a = animalDAO.show(id);
        List<AnimalType> animalTypesList = animalTypeDAO.findAll();
        List<DietType> dietTypesList = dietTypeDAO.findAll();

        Map<Integer, String> animalTypes = animalTypesList.stream()
                .collect(Collectors.toMap(AnimalType::getId, AnimalType::getType));
        Map<Integer, String> dietTypes = dietTypesList.stream()
                .collect(Collectors.toMap(DietType::getId, DietType::getType));
        Map<Integer, String> animalTypeToDietType = animalTypesList.stream()
                .collect(Collectors.toMap(
                        AnimalType::getId,
                        at -> dietTypes.get(at.getDietTypeId())
                ));

        return AnimalDTO.builder()
                .id(a.getId())
                .nickname(a.getNickname())
                .gender(a.getGender())
                .arrivalDate(a.getArrivalDate())
                .needsWarmHousing(a.isNeedsWarmHousing())
                .animalTypeId(a.getAnimalTypeId())
                .animalTypeName(animalTypes.get(a.getAnimalTypeId()))
                .animalDietType(animalTypeToDietType.get(a.getAnimalTypeId()))
                .cageId(a.getCageId())
                .build();
    }

    public void save(Animal animal) {
        animalDAO.save(animal);
    }

    public void update(int id, Animal animal) {
        animalDAO.update(id, animal);
    }

    public void delete(int id) {
        animalDAO.delete(id);
    }

}
