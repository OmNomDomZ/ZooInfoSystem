package v.rabetsky.dto;

import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CageDTO {
    private int id;
    private String animalTypeName;
    private int capacity;
}
