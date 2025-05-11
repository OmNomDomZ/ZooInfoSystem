package v.rabetsky.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import v.rabetsky.models.entities.Zoo;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Component
public class ZooDAO {

    private final JdbcTemplate jdbc;

    public ZooDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /**
     * Ищет зоопарк по имени (case-insensitive), либо создаёт новый
     * (address и contacts оставляем NULL).
     */
    public List<Zoo> findAll() {
        String sql = "SELECT id, name, address, contacts FROM zoos ORDER BY name";
        return jdbc.query(sql,
                (rs, rowNum) -> {
                    Zoo z = new Zoo();
                    z.setId(rs.getInt("id"));
                    z.setName(rs.getString("name"));
                    // при желании можно вернуть адрес/контакты
                    return z;
                });
    }

    public int getOrCreateZoo(String name) {
        Integer id = jdbc.queryForObject(
                "SELECT id FROM zoos WHERE lower(name)=lower(?)",
                Integer.class,
                name
        );
        if (id != null) {
            return id;
        }

        KeyHolder kh = new GeneratedKeyHolder();
        jdbc.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO zoos(name, address, contacts) VALUES (?, NULL, NULL)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, name);
            return ps;
        }, kh);

        return kh.getKey().intValue();
    }

    /**
     * Записывает факт перемещения/прибытия животного.
     */
    public void recordTransfer(int animalId,
                               int destinationZooId,
                               String reason,
                               java.time.LocalDate transferDate) {
        jdbc.update(
                "INSERT INTO transfers(animal_id, reason, destination_zoo_id, transfer_date) " +
                        "VALUES (?, ?, ?, ?)",
                animalId, reason, destinationZooId, transferDate
        );
    }
}
