package com.carlosjr.products.schedule;

import com.carlosjr.products.products.Product;
import com.carlosjr.products.products.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DailyDeleteJob {
    @Autowired
    private ProductService productService;
    @Scheduled(cron = "0 0 0 * * *")
    public void deleteDataIfRequired(){
        List<Product> listOfNotAvailableProducts = productService.findAllNotAvailable();
        listOfNotAvailableProducts.forEach(product -> productService.hardDelete(product));
    }
}
