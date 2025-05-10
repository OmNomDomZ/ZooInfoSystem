package v.rabetsky.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import v.rabetsky.models.entities.BirthRecord;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Component
public class BirthRecordDAO {

    private final JdbcTemplate jdbc;

    public BirthRecordDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /** Записываем, что animal – это ребёнок от parent1/parent2 */
    public void save(int childId,
                     Integer parent1Id,
                     Integer parent2Id,
                     LocalDate birthDate,
                     String status) {

        jdbc.update("""
            INSERT INTO birth_records
                  (child_id, parent_id_1, parent_id_2, birth_date, status)
            VALUES (?, ?, ?, ?, ?)
        """, childId, parent1Id, parent2Id, birthDate, status);
    }

    /** вся потомственная история родителя */
    public List<BirthRecord> findByParentId(int parentId) {
        return jdbc.query("""
            SELECT *
              FROM birth_records
             WHERE parent_id_1 = ? OR parent_id_2 = ?
             ORDER BY birth_date DESC
        """, BirthRecordDAO::mapRow, parentId, parentId);
    }

    /* приватный маппер без Lombok, чтобы не тянуть зависимости */
    private static BirthRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
        BirthRecord br = new BirthRecord();
        br.setChildId(rs.getInt("child_id"));
        br.setParentId1(rs.getObject("parent_id_1", Integer.class));
        br.setParentId2(rs.getObject("parent_id_2", Integer.class));
        br.setBirthDate(rs.getObject("birth_date", LocalDate.class));
        br.setStatus(rs.getString("status"));
        return br;
    }
}
