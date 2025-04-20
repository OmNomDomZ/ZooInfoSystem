package v.rabetsky.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import v.rabetsky.models.entities.Supplier;

import java.util.List;

@Slf4j
@Component
public class SupplierDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SupplierDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Supplier> index() {
        log.info("Suppliers: получение списка поставщиков");
        return jdbcTemplate.query("SELECT * FROM suppliers", new BeanPropertyRowMapper<>(Supplier.class));
    }

    public Supplier show(int id) {
        Supplier supplier = jdbcTemplate.queryForObject("SELECT * FROM suppliers WHERE id = ?",
                new BeanPropertyRowMapper<>(Supplier.class), id);
        log.info("Suppliers: показ поставщика id={}", id);
        return supplier;
    }

    public void save(Supplier supplier) {
        jdbcTemplate.update(
                "INSERT INTO suppliers (name, contacts) VALUES (?, ?)",
                supplier.getName(), supplier.getContacts()
        );
        log.info("Suppliers: добавлен новый поставщик");
    }

    public void update(int id, Supplier supplier) {
        jdbcTemplate.update(
                "UPDATE suppliers SET name = ?, contacts = ? WHERE id = ?",
                supplier.getName(), supplier.getContacts(), id
        );
        log.info("Suppliers: обновлён поставщик id={}", id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM suppliers WHERE id = ?", id);
        log.info("Suppliers: удалён поставщик id={}", id);
    }
}
