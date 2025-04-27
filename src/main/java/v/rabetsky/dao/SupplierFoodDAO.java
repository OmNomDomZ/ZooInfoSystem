package v.rabetsky.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import v.rabetsky.models.entities.SupplierFood;
import java.util.List;

@Component
public class SupplierFoodDAO {
    private final JdbcTemplate jdbc;

    public SupplierFoodDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<SupplierFood> findBySupplier(int supplierId) {
        return jdbc.query(
                "SELECT * FROM suppliers_food WHERE supplier_id = ? ORDER BY delivery_date DESC",
                new BeanPropertyRowMapper<>(SupplierFood.class),
                supplierId
        );
    }

    public void save(SupplierFood sf) {
        jdbc.update(
                "INSERT INTO suppliers_food (supplier_id, food_id, delivery_date, quantity, price) VALUES (?,?,?,?,?)",
                sf.getSupplierId(), sf.getFoodId(), sf.getDeliveryDate(), sf.getQuantity(), sf.getPrice()
        );
    }

    public void update(int id, SupplierFood sf) {
        jdbc.update(
                "UPDATE suppliers_food SET food_id=?, delivery_date=?, quantity=?, price=? WHERE id=?",
                sf.getFoodId(), sf.getDeliveryDate(), sf.getQuantity(), sf.getPrice(), id
        );
    }

    public void delete(int id) {
        jdbc.update("DELETE FROM suppliers_food WHERE id = ?", id);
    }
}
