package com.carlosjr.products.schedule;

import com.carlosjr.products.products.Product;
import com.carlosjr.products.products.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DailyDeleteJob {

    private final ProductService productService;
    @Scheduled(cron = "0 0 0 * * *")
    public void deleteDataIfRequired(){
        log.info("[ DailyDeleteJob ] Starting daily schedule.");
        List<Product> listOfNotAvailableProducts = productService.findAllNotAvailable();
        listOfNotAvailableProducts.forEach(productService::hardDelete);
        log.info("[ DailyDeleteJob ] Delete schedule completed!");
    }
}
