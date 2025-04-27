package v.rabetsky.models.entities;

import lombok.*;
import java.math.BigDecimal;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Cage {
    private int id;
    private String climateZone;
    private BigDecimal size;
    private BigDecimal temperature;
    private int maxNum;
}
