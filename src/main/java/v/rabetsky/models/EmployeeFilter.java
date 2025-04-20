package v.rabetsky.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeFilter {
    private String fullName;
    private Integer[] positionIds;
    private String[] genders;
}
