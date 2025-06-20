package com.example.fdtracker.repository;

import com.example.fdtracker.model.FixedDeposit;
import com.example.fdtracker.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FixedDepositRepository extends JpaRepository<FixedDeposit, Long> {
    
    @Query("SELECT fd FROM FixedDeposit fd WHERE fd.fromDate >= :fromDate AND fd.toDate <= :toDate")
    List<FixedDeposit> findByDateRange(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);
    
    @Query("SELECT fd FROM FixedDeposit fd WHERE fd.toDate = :date AND fd.mailSent = false")
    List<FixedDeposit> findByToDate(@Param("date") LocalDate date);
    
    @Query("SELECT fd FROM FixedDeposit fd WHERE fd.toDate <= :toDate")
    List<FixedDeposit> findByToDateOnly(@Param("toDate") LocalDate toDate);
    
    @Query("SELECT fd FROM FixedDeposit fd WHERE fd.status = :status")
    List<FixedDeposit> findByStatus(@Param("status") Status status);
    
    @Query("SELECT fd FROM FixedDeposit fd WHERE LOWER(fd.personName) LIKE LOWER(CONCAT('%', :personName, '%'))")
    List<FixedDeposit> findByPersonName(@Param("personName") String personName);
    
    @Query("SELECT fd FROM FixedDeposit fd WHERE fd.toDate BETWEEN :fromDate AND :toDate AND fd.status IN ('ACTIVE', 'NEW') AND fd.mailSent = false")
    List<FixedDeposit> findMaturityInRange(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);
    
    @Query("SELECT fd FROM FixedDeposit fd WHERE fd.status IN ('ACTIVE', 'NEW') AND fd.mailSent = true AND (fd.lastNotificationDate IS NULL OR fd.lastNotificationDate <= :reminderDate)")
    List<FixedDeposit> findPendingReminders(@Param("reminderDate") LocalDate reminderDate);
    
    @Query("SELECT fd FROM FixedDeposit fd WHERE fd.fromDate >= :fromDate AND fd.toDate <= :toDate AND fd.status = :status")
    List<FixedDeposit> findByDateRangeAndStatus(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate, @Param("status") Status status);
    
    @Query("SELECT fd FROM FixedDeposit fd WHERE fd.fromDate >= :fromDate AND fd.toDate <= :toDate AND LOWER(fd.personName) LIKE LOWER(CONCAT('%', :personName, '%'))")
    List<FixedDeposit> findByDateRangeAndPersonName(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate, @Param("personName") String personName);
    
    @Query("SELECT fd FROM FixedDeposit fd WHERE fd.status = :status AND LOWER(fd.personName) LIKE LOWER(CONCAT('%', :personName, '%'))")
    List<FixedDeposit> findByStatusAndPersonName(@Param("status") Status status, @Param("personName") String personName);
    
    @Query("SELECT fd FROM FixedDeposit fd WHERE fd.fromDate >= :fromDate AND fd.toDate <= :toDate AND fd.status = :status AND LOWER(fd.personName) LIKE LOWER(CONCAT('%', :personName, '%'))")
    List<FixedDeposit> findByAllParams(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate, @Param("status") Status status, @Param("personName") String personName);
    
    List<FixedDeposit> findAll();
}