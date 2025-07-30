package com.personal.library.library_api.controller;

import com.personal.library.library_api.dto.customer.CreateCustomerDTO;
import com.personal.library.library_api.dto.customer.UpdateCustomerDTO;
import com.personal.library.library_api.model.Customers;
import com.personal.library.library_api.service.CustomersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomersService customersService;

    @GetMapping("/customer")
    public ResponseEntity<List<Customers>> getAll(){
        return ResponseEntity.ok(customersService.getAll());
    }


    @GetMapping("/customer/{id}")
    public ResponseEntity<Customers> getById(@PathVariable Long id){
        return ResponseEntity.ok(customersService.getById(id));
    }

    @PostMapping("/customer")
    public ResponseEntity<Customers> createCustomer(@Valid @RequestBody CreateCustomerDTO dto){
        return ResponseEntity.ok(customersService.create(dto));
    }

    @PatchMapping("/customer/{id}")
    public ResponseEntity<Customers> updateCustomer(@PathVariable Long id, @Valid @RequestBody UpdateCustomerDTO dto){
        return ResponseEntity.ok(customersService.update(id, dto));
    }

    @DeleteMapping("/customer/delete/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long id){
        customersService.delete(id);
        return ResponseEntity.ok("Customer with id " + id + " successfully deleted");
    }

    @DeleteMapping("/customer/activated/{id}")
    public ResponseEntity<String> activatedCustomer(@PathVariable Long id){
        customersService.activate(id);
        return ResponseEntity.ok("Customer with id " + id + " successfully activated");
    }
}
