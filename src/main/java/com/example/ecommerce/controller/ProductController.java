package com.example.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.ecommerce.entity.Product;
import com.example.ecommerce.service.EmailService;
import com.example.ecommerce.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/product")
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private EmailService emailService;
	
	@PostMapping(value = "/admin/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createProduct(
            @RequestPart("product") String productJson,
            @RequestPart("image") MultipartFile imageFile) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Product product = objectMapper.readValue(productJson, Product.class);

            String message = productService.createProduct(product, imageFile);
            String toEmail = product.getUser().getEmail(); 
            emailService.sendEmail(toEmail, "Product Created", "Your product has been successfully created.");

            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Product creation failed: " + e.getMessage());
        }
    }
	
	@PutMapping("/admin/update")
	@PreAuthorize("hasRole('ADMIN')")
	 public ResponseEntity<String> updateProduct(
	            @RequestPart("product") String productJson,
	            @RequestPart("image") MultipartFile imageFile) {

	        try {
	            ObjectMapper objectMapper = new ObjectMapper();
	            Product product = objectMapper.readValue(productJson, Product.class);

	            String message = productService.createProduct(product, imageFile);
	            return ResponseEntity.ok(message);
	        } catch (Exception e) {
	            return ResponseEntity.internalServerError().body("Product updation failed failed: " + e.getMessage());
	        }
	    }
	
	@GetMapping("/admin/getProducts")
	public ResponseEntity<List<Product>> getAllProductsOfAdmin() throws Exception{
		return ResponseEntity.ok(productService.getProductByUser());
	}

	
	@DeleteMapping("/admin/delete/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> delete(@PathVariable Long id){
		return ResponseEntity.ok(productService.deleteProduct(id));
	}
	
	@GetMapping("/user/getAll")
	public ResponseEntity<List<Product>> getProducts(){
		return ResponseEntity.ok(productService.getAllProducts());
	}
	

}
