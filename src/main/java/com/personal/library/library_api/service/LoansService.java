package com.personal.library.library_api.service;

import com.personal.library.library_api.dto.loans.CreateLoansDTO;
import com.personal.library.library_api.dto.loans.UpdateLoansDTO;
import com.personal.library.library_api.exception.loans.*;
import com.personal.library.library_api.model.Book;
import com.personal.library.library_api.model.Customers;
import com.personal.library.library_api.model.Loans;
import com.personal.library.library_api.repository.BookRepository;
import com.personal.library.library_api.repository.LoansRepository;
import com.personal.library.library_api.util.book.BookUtils;
import com.personal.library.library_api.util.loan.LoanUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoansService {
    // Private instances
    private final LoansRepository loansRepository;
    private final CustomersService customersService;
    private final BookRepository bookRepository;

    // const
    private static final int MAX_LOAN_DAYS = 14; // Max day per loans

    /// Get all loans
    @Cacheable(value = "loans", key = "'all'")
    public List<Loans> getAll(){
        return loansRepository.findAllByOrderByLoanIdAsc();
    }

    /// Get by id
    @Cacheable(value = "loans", key = "#id", unless = "#result == null")
    public Loans getById(Long id){
        return loansRepository.findById(id)
                .orElseThrow(() -> new LoansNotFoundException("Loan with id " + id + " not found"));
    }

    // Create
    @Transactional
    @CacheEvict(value = "loans", allEntries = true)
    public Loans create(CreateLoansDTO dto){
        // Basic dto validation
        if(dto == null) {
            throw new IllegalArgumentException("Loan data cannot be null");
        }

        // Validate loans date
        LoanUtils.validateLoanDates(dto.getLoanDate(), dto.getReturnDate(), MAX_LOAN_DAYS);

        Customers existingCustomer = customersService.getById(dto.getCustomerId());

        // Validate book existence
        List<Long> bookIds = dto.getBookIds();
        if(bookIds == null || bookIds.isEmpty()) {
            throw new IllegalArgumentException("At least one book is required for a loan");
        }

        List<Book> books = bookRepository.findAllByBookIdIn(bookIds);
        BookUtils.validateBooksId(books, bookIds);

        // Validate book availability
        BookUtils.validateBooksAvailability(books);
        BookUtils.reduceBookStock(books, bookRepository);


        Loans loan = Loans.builder()
                .loanDate(dto.getLoanDate())
                .books(books)
                .customer(existingCustomer)
                .returnDate(dto.getReturnDate())
                .returned(false)
                .build();



        log.info("Loan successfully created");
        return loansRepository.save(loan);
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "loans", key = "#id"),
            @CacheEvict(value = "loans", key = "'all'")
    })
    public Loans update(Long id, UpdateLoansDTO dto) {
        if(dto == null) {
            throw new IllegalArgumentException("Update data cannot be null");
        }

        Loans existingLoan = getById(id);

        // Validate update loan date
        if(dto.getLoanDate() != null || dto.getReturnDate() != null) {
            LocalDate newLoanDate = dto.getLoanDate() != null ? dto.getLoanDate() : existingLoan.getLoanDate();
            LocalDate newReturnDate = dto.getReturnDate() != null ? dto.getReturnDate() : existingLoan.getReturnDate();
            LoanUtils.validateLoanDates(newLoanDate, newReturnDate, MAX_LOAN_DAYS);
        }

        // Update client
        if (dto.getCustomerId() != null) {
            Customers customer = customersService.getById(dto.getCustomerId());
            existingLoan.setCustomer(customer);
        }

        // Validate books update
        if (dto.getBookIds() != null && !dto.getBookIds().isEmpty()) {
            validateAndUpdateBooks(existingLoan, dto.getBookIds());
        }

        // Update dates
        if (dto.getLoanDate() != null) {
            existingLoan.setLoanDate(dto.getLoanDate());
        }

        if (dto.getReturnDate() != null) {
            existingLoan.setReturnDate(dto.getReturnDate());
        }

        log.info("Loan with id {} updated successfully", id);
        return loansRepository.save(existingLoan);
    }

    @Transactional
    @CacheEvict(value = "loans", key = "#id")
    public void updateReturned(Long id) {
        Loans loan = getById(id);
        List<Book> books = loan.getBooks();

        if (Boolean.TRUE.equals(loan.getReturned())) {
            loan.setReturned(false);
            loan.setReturnDate(null);
            //  Reduce stock when a book is reserved
            BookUtils.reduceBookStock(books, bookRepository);
            log.info("Loan with id {} marked as unreturned successfully", id);
        } else {
            loan.setReturned(true);
            if(loan.getReturnDate() == null) {
                loan.setReturnDate(LocalDate.now());
            }
            BookUtils.releaseBookStock(books, bookRepository);
            log.info("Loan with id {} marked as returned successfully", id);
        }

        loansRepository.save(loan);
    }


    /// ==== Private methods ====
    /**
     * Validates new books and updates book references for a loan.
     *
     * @param existingLoan The loan to be updated
     * @param newBookIds List of new book IDs to associate with the loan
     * @throws IllegalArgumentException if book validation fails
     */
    private void validateAndUpdateBooks(Loans existingLoan, List<Long> newBookIds) {
        List<Book> originalBooks = existingLoan.getBooks();
        List<Book> newBooks = bookRepository.findAllByBookIdIn(newBookIds);

        BookUtils.validateBooksId(newBooks, newBookIds);
        BookUtils.validateBooksAvailability(newBooks);

        BookUtils.releaseBookStock(originalBooks, bookRepository);
        BookUtils.reduceBookStock(newBooks, bookRepository);

        existingLoan.setBooks(newBooks);
    }
}