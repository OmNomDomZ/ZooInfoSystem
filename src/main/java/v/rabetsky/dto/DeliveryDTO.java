package v.rabetsky.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class DeliveryDTO {
    private int id;
    private int supplierId;
    private int foodId;
    private String foodName;
    private String foodTypeName;
    private boolean isProducedInternally;
    private LocalDate deliveryDate;
    private BigDecimal quantity;
    private BigDecimal price;
}
