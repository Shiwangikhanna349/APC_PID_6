package com.example.finance_management;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    

    List<Transaction> findByUser(User user);
    

    List<Transaction> findByUserAndType(User user, TransactionType type);
    

    @Query("SELECT t FROM Transaction t WHERE t.user = :user AND t.transactionDate BETWEEN :startDate AND :endDate")
    List<Transaction> findByUserAndDateRange(@Param("user") User user, 
                                           @Param("startDate") LocalDateTime startDate, 
                                           @Param("endDate") LocalDateTime endDate);
    

    List<Transaction> findByType(TransactionType type);
    

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.user = :user AND t.type = 'INCOME'")
    Double getTotalIncomeByUser(@Param("user") User user);
    

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.user = :user AND t.type = 'EXPENSE'")
    Double getTotalExpensesByUser(@Param("user") User user);
    

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.type = 'INCOME'")
    Double getTotalIncome();
    

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.type = 'EXPENSE'")
    Double getTotalExpenses();
    

    @Query("SELECT t FROM Transaction t WHERE t.user = :user AND YEAR(t.transactionDate) = :year AND MONTH(t.transactionDate) = :month")
    List<Transaction> getMonthlyTransactions(@Param("user") User user, @Param("year") int year, @Param("month") int month);
}
