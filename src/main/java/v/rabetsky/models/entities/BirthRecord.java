package v.rabetsky.models.entities;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BirthRecord {
    private int id;
    private Integer childId;
    private Integer parentId1;
    private Integer parentId2;
    private LocalDate birthDate;
    private String status; 
}