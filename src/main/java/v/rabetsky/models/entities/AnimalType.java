package v.rabetsky.models.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnimalType {
    private int id;
    private String type;
    private int dietTypeId;
}
