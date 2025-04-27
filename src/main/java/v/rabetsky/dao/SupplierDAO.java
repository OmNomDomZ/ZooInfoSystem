package v.rabetsky.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import v.rabetsky.models.filters.SupplierFilter;
import v.rabetsky.models.entities.Supplier;
import java.util.List;

@Component
public class SupplierDAO {
    private final JdbcTemplate jdbc;

    public SupplierDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Supplier> findByFilter(SupplierFilter f) {
        return jdbc.query(
                "SELECT * FROM get_suppliers(?,?,?,?,?,?)",
                new BeanPropertyRowMapper<>(Supplier.class),
                f.getName(),
                f.getFoodIds(),
                f.getFoodTypeIds(),
                f.getDateFrom(),
                f.getDateTo(),
                f.getIsProducedInternally()
        );
    }

    public Supplier findById(int id) {
        return jdbc.queryForObject(
                "SELECT * FROM suppliers WHERE id = ?",
                new BeanPropertyRowMapper<>(Supplier.class),
                id
        );
    }

    public void save(Supplier s) {
        jdbc.update(
                "INSERT INTO suppliers (name, contacts) VALUES (?, ?)",
                s.getName(), s.getContacts()
        );
    }

    public void update(int id, Supplier s) {
        jdbc.update(
                "UPDATE suppliers SET name = ?, contacts = ? WHERE id = ?",
                s.getName(), s.getContacts(), id
        );
    }

    public void delete(int id) {
        jdbc.update("DELETE FROM suppliers WHERE id = ?", id);
    }
}
