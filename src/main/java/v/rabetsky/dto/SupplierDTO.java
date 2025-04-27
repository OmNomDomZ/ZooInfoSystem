package v.rabetsky.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SupplierDTO {
    private int id;
    private String name;
    private String contacts;
}
