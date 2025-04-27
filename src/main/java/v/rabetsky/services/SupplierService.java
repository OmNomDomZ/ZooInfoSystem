package v.rabetsky.services;

import org.springframework.stereotype.Service;
import v.rabetsky.dao.*;
import v.rabetsky.dto.*;
import v.rabetsky.models.filters.SupplierFilter;
import v.rabetsky.models.entities.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SupplierService {
    private final SupplierDAO supplierDAO;
    private final SupplierFoodDAO supplierFoodDAO;
    private final FoodDAO foodDAO;
    private final FoodTypeDAO foodTypeDAO;

    public SupplierService(SupplierDAO supplierDAO,
                           SupplierFoodDAO supplierFoodDAO,
                           FoodDAO foodDAO,
                           FoodTypeDAO foodTypeDAO) {
        this.supplierDAO     = supplierDAO;
        this.supplierFoodDAO = supplierFoodDAO;
        this.foodDAO         = foodDAO;
        this.foodTypeDAO     = foodTypeDAO;
    }

    /** Основная страница: DTO с базовыми полями */
    public List<SupplierDTO> getAllSuppliers(SupplierFilter filter) {
        return supplierDAO.findByFilter(filter).stream()
                .map(s -> SupplierDTO.builder()
                        .id(s.getId())
                        .name(s.getName())
                        .contacts(s.getContacts())
                        .build())
                .collect(Collectors.toList());
    }

    /** Детальный просмотр: поставщик + все доставки (+ типа продуктов) */
    public List<DeliveryDTO> getDeliveries(int supplierId) {
        // мапим все продукты и типы в словари
        Map<Integer, String> foodNames = foodDAO.findAll().stream()
                .collect(Collectors.toMap(Food::getId, Food::getName));
        Map<Integer, String> foodTypeNames = foodTypeDAO.findAll().stream()
                .collect(Collectors.toMap(FoodType::getId, FoodType::getType));
        Map<Integer, Boolean> isInternal = foodDAO.findAll().stream()
                .collect(Collectors.toMap(Food::getId, Food::getIsProducedInternally));

        return supplierFoodDAO.findBySupplier(supplierId).stream()
                .map(sf -> DeliveryDTO.builder()
                        .id(sf.getId())
                        .supplierId(sf.getSupplierId())
                        .foodId(sf.getFoodId())
                        .foodName(foodNames.get(sf.getFoodId()))
                        .foodTypeName(foodTypeNames.get(
                                foodDAO.findAll().stream()
                                        .filter(f -> f.getId() == sf.getFoodId())
                                        .findFirst().get().getFoodTypeId()))
                        .isProducedInternally(isInternal.get(sf.getFoodId()))
                        .deliveryDate(sf.getDeliveryDate())
                        .quantity(sf.getQuantity())
                        .price(sf.getPrice())
                        .build())
                .collect(Collectors.toList());
    }

    /** CRUD поставщиков */
    public Supplier findSupplier(int id) {
        return supplierDAO.findById(id);
    }
    public void saveSupplier(Supplier s) {
        supplierDAO.save(s);
    }
    public void updateSupplier(int id, Supplier s) {
        supplierDAO.update(id, s);
    }
    public void deleteSupplier(int id) {
        supplierDAO.delete(id);
    }

    /** CRUD доставок */
    public void addDelivery(SupplierFood sf) {
        supplierFoodDAO.save(sf);
    }
    public void updateDelivery(int id, SupplierFood sf) {
        supplierFoodDAO.update(id, sf);
    }
    public void deleteDelivery(int id) {
        supplierFoodDAO.delete(id);
    }

    /** Списки для форм */
    public List<Food> getAllFood() { return foodDAO.findAll(); }
    public List<FoodType> getAllFoodTypes() { return foodTypeDAO.findAll(); }
}
