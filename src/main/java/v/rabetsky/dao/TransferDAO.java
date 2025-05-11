package v.rabetsky.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import v.rabetsky.dto.TransferDTO;

import java.time.LocalDate;
import java.util.List;

@Component
public class TransferDAO {
    private final JdbcTemplate jdbc;

    public TransferDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    // Получить все перемещения с JOIN
    public List<TransferDTO> findAll() {
        String sql = """
            SELECT t.id,
                   a.nickname,
                   z.name AS zoo_name,
                   t.reason,
                   t.transfer_date
            FROM transfers t
            JOIN animals a ON a.id = t.animal_id
            LEFT JOIN zoos z ON z.id = t.destination_zoo_id
            ORDER BY t.transfer_date DESC
            """;
        return jdbc.query(sql,
                new BeanPropertyRowMapper<>(TransferDTO.class));
    }

    // Сохранить перемещение
    public void save(int animalId, String reason, Integer zooId, LocalDate date) {
        jdbc.update("""
            INSERT INTO transfers (animal_id, reason, destination_zoo_id, transfer_date)
            VALUES (?, ?, ?, ?)
            """,
                animalId, reason, zooId, date);
    }

    // Удалить перемещение (если нужно)
    public void delete(int id) {
        jdbc.update("DELETE FROM transfers WHERE id = ?", id);
    }
}
