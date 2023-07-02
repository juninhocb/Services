package com.carlosjr.am.users.bank;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/v1/bank")
@RequiredArgsConstructor
public class BankAccountResource {
    private final BankAccountService bankAccountService;

    @GetMapping("/{bankId}")
    public ResponseEntity<BankAccountDto> findBankAccountById(
            @PathVariable(name = "bankId") UUID id){
        BankAccountDto bankAccountDto = bankAccountService.findBankAccountById(id);
        return ResponseEntity.ok().body(bankAccountDto);
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody BankAccountDto bankAccountDto,
                                       UriComponentsBuilder ucb){
        UUID uuid =  bankAccountService.createNewBankAccount(bankAccountDto);
        URI resourceLocation = ucb
                .path("/v1/bank/{bankId}")
                .buildAndExpand(uuid)
                .toUri();
        return ResponseEntity.created(resourceLocation).build();
    }
    @PutMapping("/{bankId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateBankAccount(@PathVariable(name = "bankId") UUID id,
                                  @RequestParam(name = "name") String name){
        bankAccountService.updateBankAccount(id, name);
    }

    @PutMapping("/toggle/{bankId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void toggleBankAccountActive(@PathVariable(name = "bankId") UUID id){
        bankAccountService.toggleBankAccount(id);
    }

    @PutMapping("/deposit/{bankId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void depositAmount(@PathVariable(name = "bankId") UUID id,
                              @RequestParam(name = "amount") BigDecimal amount){
        bankAccountService.depositAmount(id, amount);
    }

    @PutMapping("/withdraw/{bankId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void withdrawAmount(@PathVariable(name = "bankId") UUID id,
                              @RequestParam(name = "amount") BigDecimal amount){
        bankAccountService.withdrawAmount(id, amount);
    }

}
