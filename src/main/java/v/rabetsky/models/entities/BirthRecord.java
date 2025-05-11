package v.rabetsky.models.entities;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
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

    @NotNull(message = "Дата рождения обязательна")
    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    private LocalDate birthDate;

    @Pattern(regexp = "оставлен|обменен|отдан",
            message = "Статус должен быть одним из: оставлен, обменен, отдан")
    private String status; 
}