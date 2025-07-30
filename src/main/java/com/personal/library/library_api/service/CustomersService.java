package com.personal.library.library_api.service;

import com.personal.library.library_api.dto.customer.CreateCustomerDTO;
import com.personal.library.library_api.dto.customer.UpdateCustomerDTO;
import com.personal.library.library_api.exception.customers.*;
import com.personal.library.library_api.model.Customers;
import com.personal.library.library_api.repository.CustomersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomersService {
    private final CustomersRepository customersRepository;

    //Get all customers
    @Cacheable(value = "customers", key = "'all'")
    public List<Customers> getAll(){
        return customersRepository.findAllByOrderByCustomerIdAsc();
    }

    //Get customer by id
    @Cacheable(value = "customers", key = "#id")
    public Customers getById(Long id){
        return customersRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
    }

    @Transactional
    @CacheEvict(value = "customers", allEntries = true)
    public Customers create(CreateCustomerDTO dto){
        boolean existEmail = customersRepository.existsByEmail(dto.getEmail());
        boolean existPhone = customersRepository.existsByPhone(dto.getPhone());

        if(existEmail){
            throw new CustomerEmailExistsException("That email already exist");
        }

        if(existPhone){
            throw new CustomerPhoneExistException("Phone number already exist");
        }

        Customers customer = Customers.builder()
                .firstName(dto.getFirstName().trim())
                .lastName(dto.getLastName().trim())
                .email(dto.getEmail().trim())
                .phone(dto.getPhone().trim())
                .status(true)
                .build();

        log.info("Customer created successfully");
        return customersRepository.save(customer);
    }


    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "customers", key = "#id"),
            @CacheEvict(value = "customers", key = "'all'")
    })
    public Customers update(Long id, UpdateCustomerDTO dto){
        if(dto == null){
            throw new CustomerEmptyException("Update customer cannot be null");
        }

        if( ( dto.getFirstName() == null || dto.getFirstName().trim().isEmpty()) && (dto.getLastName() == null || dto.getLastName().trim().isEmpty())
            && (dto.getEmail() == null || dto.getEmail().trim().isEmpty()) && (dto.getPhone() == null || dto.getPhone().trim().isEmpty())
        ){
            throw  new CustomerEmptyException("At least one field must be provided");
        }

        Customers existingCustomer = getById(id);
        boolean existEmail = customersRepository.existsByEmailAndCustomerIdNot(dto.getEmail(), id);
        boolean existPhone = customersRepository.existsByPhoneAndCustomerIdNot(dto.getPhone(), id);


        if(dto.getFirstName() != null){
            existingCustomer.setFirstName(dto.getFirstName().trim());
        }

        if(dto.getLastName() != null){
            existingCustomer.setLastName(dto.getLastName().trim());
        }

        if(dto.getEmail() != null){
            if(existEmail){
                throw new CustomerEmailExistsException("Email customer with id " + id + " already exists");
            }

            existingCustomer.setEmail(dto.getEmail().trim());
        }

        if(dto.getPhone() != null){
            if(existPhone){
                throw new CustomerPhoneExistException("Phone customer with id " + id + " already exists");
            }

            existingCustomer.setPhone(dto.getPhone().trim());
        }

        log.info("Customer with id {} successfully saved", id);
        return customersRepository.save(existingCustomer);
    }

    @Transactional
    @CacheEvict(value = "customers", key = "#id")
    public void delete(Long id){
        Customers customer = getById(id);

        if(Boolean.TRUE.equals(customer.getStatus())){
            customer.setStatus(false);
            customersRepository.save(customer);
            log.info("Customer with id {} successfully deleted", id);
        }else{
            throw new CustomerDeletedException("Customer with id " + id + " already deleted");
        }
    }

    @Transactional
    @CacheEvict(value = "customers", key = "#id")
    public void activate(Long id){
        Customers customer = getById(id);

        if(Boolean.TRUE.equals(customer.getStatus())){
            throw new CustomerDeletedException("Customer with id " + id + " already active");
        }

        customer.setStatus(true);
        customersRepository.save(customer);
        log.info("Customer with id {} successfully activated", id);
    }
}
