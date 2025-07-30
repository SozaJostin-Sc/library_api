package com.personal.library.library_api.repository;

import com.personal.library.library_api.model.Loans;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoansRepository extends JpaRepository<Loans, Long> {
       List<Loans> findAllByOrderByLoanIdAsc();
}
