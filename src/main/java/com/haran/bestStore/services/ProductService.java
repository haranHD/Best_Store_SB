package com.haran.bestStore.services;

import com.haran.bestStore.models.ProductDto;
import com.haran.bestStore.models.Products;
import com.haran.bestStore.respository.ProductRepo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

@Service
public class ProductService  {


    @Autowired
    ProductRepo productRepo;
//    Products products;


    public List<Products> displayAll() {
        return productRepo.findAll(Sort.by(Sort.Direction.DESC,"id"));
    }
    public Products addProduct(Products products) {
        return productRepo.save(products);
    }

    public void saveProducts(@Valid ProductDto productDto) {

        MultipartFile image = productDto.getImageFile();
        Date createdAt = new Date();
        String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();
        try{
            String uploadDir = "public/products/";
            Path uploadPath = Paths.get(uploadDir);
            if(!Files.exists(uploadPath)){
                Files.createDirectories(uploadPath);
            }
            try(InputStream inputStream = image.getInputStream()){
                Files.copy(inputStream,Paths.get(uploadDir + storageFileName),
                        StandardCopyOption.REPLACE_EXISTING);
            }

        } catch (Exception e) {
            System.out.println("Exception :" + e.getMessage());
        }

        Products products = new Products();
        products.setName(productDto.getName());
        products.setBrand(productDto.getBrand());
        products.setCategory(productDto.getCategory());
        products.setPrice(productDto.getPrice());
        products.setDescription(productDto.getDescription());
        products.setCreatedAt(createdAt);
        products.setImageFileName(storageFileName);

        productRepo.save(products);
    }

    public void findId(int id, Model model) {
        Products products = productRepo.findById(id).get();
        model.addAttribute("products",products);

        ProductDto productDto = new ProductDto();
        productDto.setName(products.getName());
        productDto.setBrand(products.getBrand());
        productDto.setCategory(products.getCategory());
        productDto.setPrice(products.getPrice());
        productDto.setDescription(products.getDescription());

        model.addAttribute("productDto",productDto);
    }

    public void updateProduct(Model model, int id, @Valid ProductDto productDto, BindingResult result) {
        Products products = productRepo.findById(id).get();
        model.addAttribute("products",products);

        if(!productDto.getImageFile().isEmpty()){
            String uploadDir = "public/products/";
            Path oldImagePath = Paths.get(uploadDir+products.getImageFileName());
            try{
                Files.delete(oldImagePath);
            }catch (Exception e){
                System.out.println("Exception : " +e.getMessage());
            }

            MultipartFile image = productDto.getImageFile();
            Date createdAt = new Date();
            String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();

            try(InputStream inputStream = image.getInputStream()){
                Files.copy(inputStream,Paths.get(uploadDir + storageFileName),
                        StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            products.setImageFileName(storageFileName);
        }

        products.setName(productDto.getName());
        products.setBrand(productDto.getBrand());
        products.setCategory(productDto.getCategory());
        products.setDescription(productDto.getDescription());

        productRepo.save(products);
    }

    public void deleteProduct(int id) {
        Products products=productRepo.findById(id).get();
        Path imagePath = Paths.get("public/products/" + products.getImageFileName());
        try{
            Files.delete(imagePath);
        }catch (Exception e){
            System.out.println("Exception :" + e.getMessage());
        }
        productRepo.delete(products);
    }
}
