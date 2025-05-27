package com.haran.bestStore.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

@Data
@Setter
@Getter
@Configuration
public class ProductDto {
    @NotEmpty(message = "The name is required")
    private String name;

    @NotEmpty(message = "The brand is required")
    private String brand;

    @NotEmpty(message = "The name is required")
    private String category;

    @Min(0)
    private double price;

    @Size(min=10,message = "The description should be at least 10 characters")
    @Size(max=2000,message = "The description cannot exceed 2000 characters")
    private String description;

    private MultipartFile imageFile;


}
