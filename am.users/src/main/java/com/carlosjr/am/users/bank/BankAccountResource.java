package com.carlosjr.am.users.bank;

import com.carlosjr.am.users.common.InvoiceDto;
import com.carlosjr.am.users.exceptions.AccessTokenExpirationException;
import com.carlosjr.am.users.transaction.TransactionDto;
import com.carlosjr.am.users.transaction.TransactionService;
import com.carlosjr.am.users.user.UserService;
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

import java.net.URI;
import java.security.Principal;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/v1/bank")
@RequiredArgsConstructor
public class BankAccountResource {
    private final BankAccountService bankAccountService;
    private final TransactionService transactionService;
    private final UserService userService;

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

    @PostMapping("/deposit")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void depositAmount(@RequestBody InvoiceDto invoiceDto,
                              Principal principal){

        if (userService.validateUserLoggedIn(principal.getName())){
            BankAccountDto bankAccountDto = bankAccountService.
                    depositAmount(invoiceDto.accountNumber(),
                            invoiceDto.amount());
            transactionService.createNewTransaction(TransactionDto.builder()
                    .invoiceId(invoiceDto.invoiceId())
                    .amount(invoiceDto.amount())
                    .bankAccountDto(bankAccountDto)
                    .build());
        } else {
            throw new AccessTokenExpirationException("Not logged in.");
        }
    }
    @PostMapping("/withdraw")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void withdrawAmount(@RequestBody InvoiceDto invoiceDto,
                               Principal principal){

        if (userService.validateUserLoggedIn(principal.getName())){
            BankAccountDto bankAccountDto = bankAccountService.
                    withdrawAmount(invoiceDto.accountNumber(),
                            invoiceDto.amount());
            transactionService.createNewTransaction(TransactionDto.builder()
                    .invoiceId(invoiceDto.invoiceId())
                    .amount(invoiceDto.amount())
                    .bankAccountDto(bankAccountDto)
                    .build());
        } else {
            throw new AccessTokenExpirationException("Not logged in.");
        }
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
