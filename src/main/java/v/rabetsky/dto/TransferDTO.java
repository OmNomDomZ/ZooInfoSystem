package v.rabetsky.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class TransferDTO {
    private Integer id;
    private String nickname;
    private String zooName;
    private String reason;
    private LocalDate transferDate;
}
