package com.haran.bestStore.controller;


import com.haran.bestStore.models.ProductDto;
import com.haran.bestStore.models.Products;
import com.haran.bestStore.respository.ProductRepo;
import com.haran.bestStore.services.ProductService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLOutput;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductService productService;
    @Autowired
    Products products;

    @GetMapping({"","/"})
    public String display(Model model) {
        List<Products> products = productService.displayAll();
        model.addAttribute("products",products);
        return "allProducts/index";

    }
    @Autowired
    ProductDto productDto;
    @GetMapping("/create")
    public String showCreatePage (Model model){
        model.addAttribute("productDto",productDto);
        return "allProducts/createProducts";
    }

    @GetMapping("/edit")
    public String showEdit(Model model,
                           @RequestParam int id){
        try{
            productService.findId(id,model);
        }catch (Exception e){
            System.out.println("Exception: " + e.getMessage());
            return  "redirect:/products";
        }
        return "allProducts/EditProducts";
    }

    @PostMapping("/edit")
    public String updateProduct(
            Model model,
            @RequestParam int id,
            @Valid @ModelAttribute ProductDto productDto,
            BindingResult result
    ){
        try{
            productService.updateProduct(model,id,productDto,result);
            if(result.hasErrors()){
                return "allProducts/EditProducts";
            }


        } catch (Exception e) {
            System.out.println("Exception: "+ e.getMessage());
        }
        return "redirect:/products";
    }


    @PostMapping("/create")
    public String createProduct(
            @Valid @ModelAttribute ProductDto productDto,
            BindingResult result){
        if(productDto.getImageFile().isEmpty()){
            result.addError(new FieldError("productDto","imageFile","The image is required."));
        }
        if(result.hasErrors()){
            return "allProducts/createProducts";
        }
        try{
            productService.saveProducts(productDto);
        }
        catch (Exception e){
            result.reject("globalError",e.getMessage());
            return "allProducts/createProducts";
        }
        return "redirect:/products";
    }

    @GetMapping("/delete")
    public String deleteProduct(@RequestParam int id){
        try{
            productService.deleteProduct(id);
        }catch (Exception e){
            System.out.println("Exception: "+ e.getMessage());
        }

        return "redirect:/products";
    }




}
