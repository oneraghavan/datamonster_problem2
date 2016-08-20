package datamonster.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Rule {

    private String name;
    private String type;
    private String field;
    private String comparator;
    private String compared;
    private String notification;
}
