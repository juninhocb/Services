package com.carlosjr.am.users.bank;

import com.carlosjr.am.users.transaction.TransactionService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/v1/bank")
@RequiredArgsConstructor
public class BankAccountResource {
    private final BankAccountService bankAccountService;
    private final TransactionService transactionService;

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
    @Transactional
    public void depositAmount(@PathVariable(name = "bankId") UUID id,
                              @RequestParam(name = "amount") BigDecimal amount){
        bankAccountService.depositAmount(UUID.randomUUID(), id, amount);
        transactionService.createNewTransaction(null);


    }
    @PutMapping("/withdraw/{bankId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void withdrawAmount(@PathVariable(name = "bankId") UUID id,
                              @RequestParam(name = "amount") BigDecimal amount){
        bankAccountService.withdrawAmount(id, amount);
    }

    @GetMapping("/findbyemail/{email}")
    public ResponseEntity<Set<BankAccountDto>> getBankAccountsByUser(
            @PathVariable(name = "email") String userEmail,
            Pageable pageable){

        Set<BankAccountDto> banks = bankAccountService
                .retrieveBankAccountsByUser(userEmail,
                        PageRequest.of(
                                pageable.getPageNumber(),
                                pageable.getPageSize(),
                                pageable.getSortOr(Sort.by(Sort.Direction.ASC, "createdTime"))));

        return ResponseEntity.ok().body(banks);
    }

}
