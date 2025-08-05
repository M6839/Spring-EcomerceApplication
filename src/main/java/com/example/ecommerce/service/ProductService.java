package com.example.ecommerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.ecommerce.entity.Product;
import com.example.ecommerce.entity.UserEntity;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.repository.UserRepository;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	 @Autowired
	 private ImageUploadService imageUploadService;
	
	
	public String createProduct(Product product, MultipartFile imageFile) throws Exception {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String username = auth.getName();
	    UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        String imageUrl = imageUploadService.uploadFile(imageFile);
        product.setImageUrl(imageUrl);
        product.setUser(user); 
        productRepository.save(product);
        return "Product created with image";
    }

	
	
	
	public String deleteProduct(Long id) {
		Product product = productRepository.findById(id)
		        .orElseThrow(() -> new RuntimeException("Product not found"));

		    String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
		    UserEntity currentUser = userRepository.findByEmail(currentUsername);

		    if (!product.getUser().getId().equals(currentUser.getId())) {
		        throw new AccessDeniedException("You can delete only your own products.");
		    }

		productRepository.delete(product);
		return "product deleted successfully";
	}
	
	public List<Product> getProductByUser() throws Exception{
		String currentUsername=SecurityContextHolder.getContext().getAuthentication().getName();
		UserEntity currentUser = userRepository.findByEmail(currentUsername);
		Long userId=currentUser.getId();
		return productRepository.findByUserId(userId);
	}
	
	
	public List<Product> getAllProducts(){
		return productRepository.findAll();
	}

}
