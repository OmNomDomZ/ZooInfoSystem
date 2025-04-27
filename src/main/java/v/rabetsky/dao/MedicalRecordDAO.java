package v.rabetsky.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import v.rabetsky.models.entities.MedicalRecord;

import java.sql.Date;
import java.util.List;

@Slf4j
@Component
public class MedicalRecordDAO {
    private final JdbcTemplate jdbc;

    public MedicalRecordDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<MedicalRecord> findByAnimalId(int animalId) {
        return jdbc.query(
                "select * from medical_records where animal_id = ? order by checkup_date desc",
                new BeanPropertyRowMapper<>(MedicalRecord.class),
                animalId
        );
    }

    public MedicalRecord findById(int id) {
        return jdbc.queryForObject(
                "select * from medical_records where id = ?",
                new BeanPropertyRowMapper<>(MedicalRecord.class),
                id
        );
    }

    public void saveForAnimal(int animalId, MedicalRecord mr) {
        jdbc.update(
                "insert into medical_records (animal_id,birth_date,weight,height,vaccinations,illnesses,checkup_date) values (?,?,?,?,?,?,?)",
                animalId,
                Date.valueOf(mr.getBirthDate()),
                mr.getWeight(),
                mr.getHeight(),
                mr.getVaccinations(),
                mr.getIllnesses(),
                Date.valueOf(mr.getCheckupDate())
        );
        log.info("MedicalRecord: created for animal {}", animalId);
    }

    public void update(MedicalRecord mr) {
        jdbc.update(
                "update medical_records set birth_date=?, weight=?, height=?, vaccinations=?, illnesses=?, checkup_date=? where id=?",
                Date.valueOf(mr.getBirthDate()),
                mr.getWeight(),
                mr.getHeight(),
                mr.getVaccinations(),
                mr.getIllnesses(),
                Date.valueOf(mr.getCheckupDate()),
                mr.getId()
        );
        log.info("MedicalRecord: updated id={}", mr.getId());
    }
}
