package v.rabetsky.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BirthRecordDTO {
    private LocalDate birthDate;
    private String status;
    private String childName;
}
