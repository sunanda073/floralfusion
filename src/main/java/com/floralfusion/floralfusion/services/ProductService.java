package com.floralfusion.floralfusion.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.floralfusion.floralfusion.entities.Product;
import com.floralfusion.floralfusion.repositories.ProductRepository;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    public List<Product> findProductsByCompanyId(Long companyId) {
        return productRepository.findByCompanyCompanyID(companyId);
    }

    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }

    public void updateProduct(Product product) {
        productRepository.save(product);
    }

    public Product findProductById(Long productId) {
        return productRepository.findById(productId).orElse(null);
    }
}
