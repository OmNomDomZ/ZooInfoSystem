package v.rabetsky.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
public class QueriesDAO {

    private final JdbcTemplate jdbc;

    @Autowired
    public QueriesDAO(JdbcTemplate jdbcTemplate) {
        this.jdbc = jdbcTemplate;
    }

    public List<EmployeeOverview> getEmployeesOverview() {
        return jdbc.query(
                "SELECT * FROM fn_employees_overview()",
                (rs, i) -> new EmployeeOverview(
                        rs.getString("fio"),
                        rs.getString("gender"),
                        rs.getDate("hire_date").toLocalDate(),
                        rs.getInt("work_duration"),
                        rs.getDate("birth_date").toLocalDate(),
                        rs.getBigDecimal("salary"),
                        rs.getLong("total_employees")
                )
        );
    }

    /* ───────────────────── 2. Cлужащие по виду ───────────────────── */
    public List<EmployeeByType> getEmployeesByAnimalType(String type) {
        return jdbc.query(
                "SELECT * FROM fn_employees_by_animal_type(?)",
                employeeByTypeMapper,
                type
        );
    }

    /* ───────────────────── 3. Служащие с доступом к клеткам ───────────────── */
    public List<EmployeeByType> getEmployeesWithCageAccess(String type) {
        return jdbc.query(
                "SELECT * FROM fn_employees_with_cage_access(?)",
                employeeByTypeMapper,
                type
        );
    }

    /* ───────────────────── 4. Животные в клетке ───────────────────── */
    public List<AnimalInCage> getAnimalsInCage(int cageId) {
        return jdbc.query(
                "SELECT * FROM fn_animals_in_cage(?)",
                animalInCageMapper,
                cageId
        );
    }

    /* ───────────────────── 5. Нуждающиеся в тепле ─────────────────── */
    public List<AnimalHeat> getAnimalsNeedingHeat(String type) {
        return jdbc.query(
                "SELECT * FROM fn_animals_needing_heat(?)",
                animalHeatMapper,
                type
        );
    }

    /* ───────────────────── 6. По прививке ─────────────────────────── */
    public List<AnimalMedical> getAnimalsByVaccine(String vaccine) {
        return jdbc.query(
                "SELECT * FROM fn_animals_by_vaccine(?)",
                animalMedicalMapper,
                vaccine
        );
    }

    /* ───────────────────── 7. Совместимые животные ────────────────── */
    public List<AnimalDiet> getCompatibleAnimals(String type) {
        return jdbc.query(
                "SELECT * FROM fn_compatible_animals(?)",
                animalDietMapper,
                type
        );
    }

    /* ───────────────────── 8. Поставщики за период ────────────────── */
    public List<SupplierDelivery> getSuppliersByPeriod(LocalDate from, LocalDate to) {
        return jdbc.query(
                "SELECT * FROM fn_suppliers_by_period(?,?)",
                supplierDeliveryMapper,
                from, to
        );
    }

    /* ───────────────────── 9. Внутренние корма ─────────────────────── */
    public List<InternalFood> getInternalFood() {
        return jdbc.query(
                "SELECT * FROM fn_internal_food()",
                internalFoodMapper
        );
    }

    /* ───────────────────── 10. Животные по типу корма ─────────────── */
    public List<AnimalFood> getAnimalsByFoodType(String foodType) {
        return jdbc.query(
                "SELECT * FROM fn_animals_by_food_type(?)",
                animalFoodMapper,
                foodType
        );
    }

    /* ───────────────────── 11. Полная инфа о животных ─────────────── */
    public List<AnimalFullInfo> getAnimalFullInfo(String type) {
        return jdbc.query(
                "SELECT * FROM fn_animal_full_info(?)",
                animalFullInfoMapper,
                type
        );
    }

    /* ───────────────────── 12. Потенциальные родители ─────────────── */
    public List<PotentialParent> getPotentialParents(LocalDate start) {
        return jdbc.query(
                "SELECT * FROM fn_potential_parents(?::date)",
                potentialParentMapper,
                start
        );
    }

    /* ───────────────────── 13. Партнёр-зоопарки ───────────────────── */
    public List<PartnerZoo> getPartnerZoos(String type) {
        return jdbc.query(
                "SELECT * FROM fn_partner_zoos(?)",
                partnerZooMapper,
                type
        );
    }

    /* ========= RowMappers ========= */

    private final RowMapper<EmployeeByType> employeeByTypeMapper = (r, i) ->
            new EmployeeByType(
                    r.getString("fio"),
                    r.getString("animal_type"),
                    r.getLong("total_employees")
            );

    private final RowMapper<AnimalInCage> animalInCageMapper = (r, i) ->
            new AnimalInCage(
                    r.getString("nickname"),
                    r.getString("gender"),
                    r.getDate("arrival_date").toLocalDate(),
                    r.getInt("age_years"),
                    r.getBigDecimal("weight"),
                    r.getBigDecimal("height"),
                    r.getLong("total_animals")
            );

    private final RowMapper<AnimalHeat> animalHeatMapper = (r, i) ->
            new AnimalHeat(
                    r.getString("nickname"),
                    r.getString("gender"),
                    r.getDate("arrival_date").toLocalDate(),
                    r.getString("animal_type"),
                    r.getLong("total_animals")
            );

    private final RowMapper<AnimalMedical> animalMedicalMapper = (r, i) ->
            new AnimalMedical(
                    r.getString("nickname"),
                    r.getString("gender"),
                    r.getString("vaccinations"),
                    r.getString("illnesses"),
                    r.getDate("arrival_date").toLocalDate(),
                    r.getLong("total_animals")
            );

    private final RowMapper<AnimalDiet> animalDietMapper = (r, i) ->
            new AnimalDiet(
                    r.getString("nickname"),
                    r.getString("animal_type"),
                    r.getString("diet_type"),
                    r.getBoolean("needs_warm_housing")
            );

    private final RowMapper<SupplierDelivery> supplierDeliveryMapper = (r, i) ->
            new SupplierDelivery(
                    r.getString("supplier_name"),
                    r.getString("contacts"),
                    r.getInt("food_id"),
                    r.getDate("delivery_date").toLocalDate(),
                    r.getBigDecimal("quantity"),
                    r.getBigDecimal("price"),
                    r.getLong("total_suppliers")
            );

    private final RowMapper<InternalFood> internalFoodMapper = (r, i) ->
            new InternalFood(
                    r.getString("food_name"),
                    r.getBoolean("is_produced_internally"),
                    r.getLong("supply_count"),
                    r.getBigDecimal("total_quantity")
            );

    private final RowMapper<AnimalFood> animalFoodMapper = (r, i) ->
            new AnimalFood(
                    r.getString("nickname"),
                    r.getString("gender"),
                    r.getString("food_type"),
                    r.getLong("total_animals")
            );

    private final RowMapper<AnimalFullInfo> animalFullInfoMapper = (r, i) ->
            new AnimalFullInfo(
                    r.getInt("id"),
                    r.getString("nickname"),
                    r.getString("gender"),
                    r.getDate("arrival_date").toLocalDate(),
                    r.getDate("birth_date") != null ? r.getDate("birth_date").toLocalDate() : null,
                    r.getObject("age_years", Integer.class),
                    r.getBigDecimal("weight"),
                    r.getBigDecimal("height"),
                    r.getString("vaccinations"),
                    r.getString("illnesses"),
                    r.getLong("offspring_count"),
                    r.getLong("total_animals")
            );

    private final RowMapper<PotentialParent> potentialParentMapper = (r, i) ->
            new PotentialParent(
                    r.getInt("id"),
                    r.getString("nickname"),
                    r.getString("species"),
                    r.getInt("cage_id")
            );

    private final RowMapper<PartnerZoo> partnerZooMapper = (r, i) ->
            new PartnerZoo(
                    r.getString("zoo_name"),
                    r.getString("address"),
                    r.getString("contacts"),
                    r.getLong("total_zoos")
            );

    /* ========= DTO records ========= */

    public record EmployeeOverview(
            String fio, String gender, LocalDate hireDate,
            int workDurationDays, LocalDate birthDate,
            BigDecimal salary, long total) {}

    public record EmployeeByType(
            String fio, String animalType, long total) {}

    public record AnimalInCage(
            String nickname, String gender, LocalDate arrival,
            int ageYears, BigDecimal weight, BigDecimal height, long total) {}

    public record AnimalHeat(
            String nickname, String gender, LocalDate arrival,
            String animalType, long total) {}

    public record AnimalMedical(
            String nickname, String gender, String vaccinations,
            String illnesses, LocalDate arrival, long total) {}

    public record AnimalDiet(
            String nickname, String animalType, String dietType,
            boolean needsWarmHousing) {}

    public record SupplierDelivery(
            String supplier, String contacts, int foodId,
            LocalDate date, BigDecimal qty, BigDecimal price, long total) {}

    public record InternalFood(
            String foodName, boolean internal, long supplyCount, BigDecimal totalQty) {}

    public record AnimalFood(
            String nickname, String gender, String foodType, long total) {}

    public record AnimalFullInfo(
            int id, String nickname, String gender, LocalDate arrival,
            LocalDate birth, Integer ageYears, BigDecimal weight,
            BigDecimal height, String vacc, String ill, long offspring, long total) {}

    public record PotentialParent(
            int id, String nickname, String species, int cageId) {}

    public record PartnerZoo(
            String name, String address, String contacts, long total) {}
}
