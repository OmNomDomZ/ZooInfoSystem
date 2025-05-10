package v.rabetsky.services;

import org.springframework.stereotype.Service;
import v.rabetsky.dao.*;
import v.rabetsky.dto.AnimalDTO;
import v.rabetsky.dto.BirthRecordDTO;
import v.rabetsky.models.entities.*;
import v.rabetsky.models.filters.AnimalFilter;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AnimalService {
    private final AnimalDAO animalDAO;
    private final AnimalTypeDAO animalTypeDAO;
    private final DietTypeDAO dietTypeDAO;
    private final AnimalCageHistoryDAO historyDAO;
    private final MedicalRecordDAO medicalDAO;
    private final BirthRecordDAO birthDAO;
    private final ZooDAO zooDAO;
    private final CageDAO cageDAO;

    public AnimalService(AnimalDAO animalDAO,
                         AnimalTypeDAO animalTypeDAO,
                         DietTypeDAO dietTypeDAO,
                         AnimalCageHistoryDAO historyDAO,
                         MedicalRecordDAO medicalDAO,
                         BirthRecordDAO birthDAO,
                         ZooDAO zooDAO,
                         CageDAO cageDAO) {
        this.animalDAO = animalDAO;
        this.animalTypeDAO = animalTypeDAO;
        this.dietTypeDAO = dietTypeDAO;
        this.historyDAO = historyDAO;
        this.medicalDAO = medicalDAO;
        this.birthDAO = birthDAO;
        this.zooDAO = zooDAO;
        this.cageDAO = cageDAO;
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
                    AnimalDTO child = findById(r.getChildId());
                    return new BirthRecordDTO(
                            r.getBirthDate(),
                            r.getStatus(),
                            child.getNickname()
                    );
                })
                .collect(Collectors.toList());
    }

    public int saveAnimalWithOrigin(Animal animal,
                                    String arrivalType,
                                    String originZooName,
                                    Integer parent1Id,
                                    Integer parent2Id) {
        // 1) создаём животное
        int newId = animalDAO.saveReturningId(animal);

        if ("ARRIVED".equals(arrivalType)) {
            // 2) обрабатываем привоз
            int zooId = zooDAO.getOrCreateZoo(originZooName);
            zooDAO.recordTransfer(
                    newId,
                    zooId,
                    "ARRIVAL",
                    animal.getArrivalDate()
            );

        } else if ("BORN".equals(arrivalType)) {
            // ваша уже готовая валидация и birthDAO.save(...)
            validateNewBorn(animal, parent1Id, parent2Id);
            birthDAO.save(
                    newId,
                    parent1Id,
                    parent2Id,
                    animal.getArrivalDate(),
                    "оставлен"
            );

        } else {
            throw new IllegalArgumentException("Unknown arrivalType: " + arrivalType);
        }

        return newId;
    }

    private void validateNewBorn(Animal animal, Integer p1, Integer p2) {
        if (p1 == null || p2 == null) {
            throw new RuntimeException("Нужно указать и отца, и мать.");
        }

        AnimalDTO father = findById(p1);
        AnimalDTO mother = findById(p2);

        // 1. Одинаковый вид у родителей
        if (!father.getAnimalTypeId().equals(mother.getAnimalTypeId())) {
            throw new RuntimeException("Отец и мать должны быть одного вида.");
        }
        // 2. Обе клетки совпадают
        if (!Objects.equals(father.getCageId(), mother.getCageId())) {
            throw new RuntimeException("Отец и мать должны находиться в одной клетке.");
        }
        // 3. Ребёнок того же вида
        if (!animal.getAnimalTypeId().equals(father.getAnimalTypeId())) {
            throw new RuntimeException("Вид ребёнка должен совпадать с родителями.");
        }
        // 4. Ребёнок в той же клетке
        if (!Objects.equals(animal.getCageId(), father.getCageId())) {
            throw new RuntimeException("Клетка ребёнка должна совпадать с клеткой родителей.");
        }

        // 5. Родители должны быть разного пола
        if(father.getGender().equals(mother.getGender())) {
            throw new RuntimeException("Родители должны быть разного пола");
        }

        // 6. Клетка переполнена
        if(cageDAO.findById(father.getCageId()).getCapacity() == cageDAO.numAnimalsInCage(father.getCageId())) {
            throw new RuntimeException("Клетка переполнена");
        }
    }

    public void moveCage(int animalId, int newCageId) {
        animalDAO.updateCage(animalId, newCageId);
    }

}
