package v.rabetsky.services;

import org.springframework.stereotype.Service;
import v.rabetsky.dao.*;
import v.rabetsky.dto.AnimalDTO;
import v.rabetsky.dto.BirthRecordDTO;
import v.rabetsky.models.entities.*;
import v.rabetsky.models.filters.AnimalFilter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AnimalService {
    private final AnimalDAO animalDAO;
    private final AnimalTypeDAO animalTypeDAO;
    private final DietTypeDAO dietTypeDAO;
    private final AnimalCageHistoryDAO historyDAO;
    private final MedicalRecordDAO medicalDAO;
    private final BirthRecordDAO birthDAO;

    public AnimalService(AnimalDAO animalDAO,
                         AnimalTypeDAO animalTypeDAO,
                         DietTypeDAO dietTypeDAO,
                         AnimalCageHistoryDAO historyDAO,
                         MedicalRecordDAO medicalDAO,
                         BirthRecordDAO birthDAO) {
        this.animalDAO      = animalDAO;
        this.animalTypeDAO  = animalTypeDAO;
        this.dietTypeDAO    = dietTypeDAO;
        this.historyDAO     = historyDAO;
        this.medicalDAO     = medicalDAO;
        this.birthDAO       = birthDAO;
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

    public int save(Animal animal) {
        return animalDAO.saveReturningId(animal);
    }

    public void update(int id, Animal animal) {
        animalDAO.update(id, animal);
    }

    public void delete(int id) {
        animalDAO.delete(id);
    }

    public List<AnimalCageHistory> getHistory(int animalId) {
        return historyDAO.findByAnimalId(animalId);
    }

    public List<MedicalRecord> getMedicalRecords(int animalId) {
        return medicalDAO.findByAnimalId(animalId);
    }

    public List<BirthRecordDTO> getBirthRecords(int animalId) {
        List<BirthRecord> recs = birthDAO.findByParentId(animalId);
        return recs.stream()
                .map(r -> {
                    // Предполагаем, что в birth_records поле id — это ID самого детёныша
                    AnimalDTO child = findById(r.getId());
                    return new BirthRecordDTO(r.getBirthDate(),
                            r.getStatus(),
                            child.getNickname());
                })
                .collect(Collectors.toList());
    }

}
