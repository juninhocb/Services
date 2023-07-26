package com.carlosjr.products.sub.subproduct;

import com.carlosjr.products.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/sub-product")
@RequiredArgsConstructor
public class SubProductResource {

    private final SubProductRepository subProductRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createSubProduct(@RequestParam(name = "name")String name){
        subProductRepository.save(SubProduct.builder().name(name).build());
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<SubProduct> listSubProducts(Pageable pageable){

        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "id"));

        return subProductRepository
                .findAll(pageRequest).getContent();
    }

    @GetMapping("/find")
    @ResponseStatus(HttpStatus.OK)
    public SubProduct findByName(@RequestParam(name = "name")String name){
        return findInRepositoryByName(name);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSubProduct(@RequestParam(name = "name")String name){
        SubProduct subProductToDelete = findInRepositoryByName(name);
        subProductRepository.delete(subProductToDelete);
    }


    private SubProduct findInRepositoryByName(String name){
        Optional<SubProduct> subProduct = subProductRepository
                .findSubProductByName(name);
        if (subProduct.isEmpty()){
            throw new ResourceNotFoundException("This product was not found in DB: " + name);
        }
        return subProduct.get();
    }


}
