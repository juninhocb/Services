package com.carlosjr.products.sub.unittype;

import com.carlosjr.products.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/unit-type")
@RequiredArgsConstructor
@Slf4j
public class UnitTypeResource {

    private final UnitTypeRepository unitTypeRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createUnitType(@RequestParam(name = "name")String name){
        unitTypeRepository.save(UnitType.builder().name(name).build());
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Cacheable(cacheNames = "sub-product-cache")
    public List<UnitType> listUnitType(Pageable pageable){

        log.trace(" [ UnitTypeResource ] Attempt to find all unit type");

        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "id"));

        return unitTypeRepository
                .findAll(pageRequest).getContent();
    }

    @GetMapping("/find")
    @ResponseStatus(HttpStatus.OK)
    public UnitType findByName(@RequestParam(name = "name")String name){
        return findInRepositoryByName(name);
    }
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUnitType(@RequestParam(name = "name")String name){
        UnitType unitTypeToDelete = findInRepositoryByName(name);
        unitTypeRepository.delete(unitTypeToDelete);
    }

    private UnitType findInRepositoryByName(String name){
        Optional<UnitType> unitType = unitTypeRepository
                .findUnitTypeByName(name);
        if (unitType.isEmpty()){
            throw new ResourceNotFoundException("This unit type was not found in DB: " + name);
        }
        return unitType.get();
    }


}
