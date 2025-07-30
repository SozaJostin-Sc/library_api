package com.personal.library.library_api.repository;

import com.personal.library.library_api.model.Customers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomersRepository extends JpaRepository<Customers, Long> {
    List<Customers> findAllByOrderByCustomerIdAsc();
    boolean existsByEmail(String email);
    boolean existsByEmailAndCustomerIdNot(String email, Long id);
    boolean existsByPhone(String phone);
    boolean existsByPhoneAndCustomerIdNot(String phone, Long id);
}
