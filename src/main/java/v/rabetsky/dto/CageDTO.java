package v.rabetsky.dto;

import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CageDTO {
    private int id;
    private int animalTypeId;
    private String animalTypeName;
    private int capacity;
}
