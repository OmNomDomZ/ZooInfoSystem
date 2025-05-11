package v.rabetsky.services;

import v.rabetsky.dao.CageDAO;
import v.rabetsky.dao.AnimalDAO;
import v.rabetsky.dao.AnimalTypeDAO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import v.rabetsky.dto.AnimalDTO;
import v.rabetsky.dto.CageDTO;
import v.rabetsky.models.entities.AnimalType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CageService {
    private final CageDAO cageDAO;
    private final AnimalDAO animalDAO;
    private final AnimalTypeDAO animalTypeDAO;

    public CageService(CageDAO cageDAO,
                       AnimalDAO animalDAO,
                       AnimalTypeDAO animalTypeDAO) {
        this.cageDAO       = cageDAO;
        this.animalDAO     = animalDAO;
        this.animalTypeDAO = animalTypeDAO;
    }

    public List<CageDTO> getAllCages() {
        return cageDAO.findAllDTO();
    }

    public List<AnimalType> getAllAnimalTypes() {
        return animalTypeDAO.findAll();
    }

    @Transactional
    public void createCage(int animalTypeId, int capacity) {
        cageDAO.save(animalTypeId, capacity);
    }

    /**
     * Удаляем только пустую клетку.
     * @throws IllegalStateException если в клетке есть животные
     */
    @Transactional
    public void deleteCage(int cageId) {
        List<AnimalDTO> animals = animalDAO.findByCage(cageId);
        if (!animals.isEmpty()) {
            throw new IllegalStateException("Нельзя удалить непустую клетку");
        }
        cageDAO.delete(cageId);
    }

    @Transactional
    public void moveAnimal(int animalId, int newCageId) {
        animalDAO.moveToCage(animalId, newCageId);
    }

    public Map<Integer, List<AnimalDTO>> getAnimalsByCage() {
        return getAllCages().stream()
                .collect(Collectors.toMap(
                        CageDTO::getId,
                        c -> animalDAO.findByCage(c.getId())
                ));
    }
}
