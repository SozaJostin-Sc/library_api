package com.personal.library.library_api.controller;

import com.personal.library.library_api.dto.loans.CreateLoansDTO;
import com.personal.library.library_api.dto.loans.UpdateLoansDTO;
import com.personal.library.library_api.model.Loans;
import com.personal.library.library_api.service.LoansService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LoanController {
    private final LoansService loansService;

    /// GET ALL
    @GetMapping("/loan")
    public ResponseEntity<List<Loans>> getAllLoans(){
        return ResponseEntity.ok(loansService.getAll());
    }

    @GetMapping("/loan/{id}")
    public ResponseEntity<Loans> getById(@PathVariable Long id){
        return ResponseEntity.ok(loansService.getById(id));
    }

    @PostMapping("/loan")
    public ResponseEntity<Loans> createLoans(@Valid @RequestBody CreateLoansDTO dto){
        return ResponseEntity.ok(loansService.create(dto));
    }

    @PatchMapping("/loan/{id}")
    public ResponseEntity<Loans> updateLoans(@PathVariable Long id, @Valid @RequestBody UpdateLoansDTO dto){
        return ResponseEntity.ok(loansService.update(id, dto));
    }

    @DeleteMapping("/loan/returned/{id}")
    public ResponseEntity<String> changeStatusReturned(@PathVariable Long id){
        loansService.updateReturned(id);
        return ResponseEntity.ok("Status returned changed of id " + id);
    }

}
