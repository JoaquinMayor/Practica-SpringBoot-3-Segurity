package com.joaquin.curso.springboot.app.crudjpa.springbootcrudjpa.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.joaquin.curso.springboot.app.crudjpa.springbootcrudjpa.entities.Product;
import com.joaquin.curso.springboot.app.crudjpa.springbootcrudjpa.repository.IProductRepository;

@Service
public class ProductServiceImpl implements IProductService{

    @Autowired
    private IProductRepository repository;

    @Transactional(readOnly = true)
    @Override
    public List<Product> findAll() {
        return (List<Product>) repository.findAll();

    }
        
    @Transactional(readOnly = true)
    @Override
    public Optional<Product> findById(Long id) {
        return repository.findById(id);
    }
    @Transactional
    @Override
    public Product save(Product product) {
       return repository.save(product);
    }

    @Transactional
    @Override
    public Optional<Product> update(Long id, Product product) {
        Optional<Product> productDB = repository.findById(id);

        if(productDB.isPresent()){
            Product productNew = productDB.orElseThrow();
            productNew.setSku(product.getSku());
            productNew.setName(product.getName());
            productNew.setDescription(product.getDescription());
            productNew.setPrice(product.getPrice());
            return Optional.of(repository.save(productNew));
        }
        return productDB;
    }

    @Transactional
    @Override
    public Optional<Product> delete(Long id) {
        Optional<Product> productDB = repository.findById(id);
        productDB.ifPresent(prod ->{
            repository.delete(prod);
        });
        return productDB;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsBySku(String sku) {
        return repository.existsBySku(sku);
    }

    
}
