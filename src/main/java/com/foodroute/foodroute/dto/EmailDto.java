package com.foodroute.foodroute.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailDto {

    private Long userId;
    private String subject;
    private String comment;

}
