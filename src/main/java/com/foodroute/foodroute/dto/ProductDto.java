package com.foodroute.foodroute.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto {

    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Description is required")
    private String description;
    @NotBlank(message = "Price is required")
    private Double price;
    @NotBlank(message = "Image is required")
    private String image;
    @NotBlank(message = "Category Id is required")
    private Long categoryId;

}
