package datamonster.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Rule {

    private String name;
    private String type;
    private String field;
    private String comparator;
    private String compared;
    private List<String> notifications;
    private Boolean isAgg;
    private String aggType;
    private Long limit;
}
