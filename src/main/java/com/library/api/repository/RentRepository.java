package com.library.api.repository;

import com.library.api.entities.Rent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentRepository extends JpaRepository<Rent, Long> {

    @Query("SELECT r FROM rent r WHERE r.client.id = :clientId AND r.paymentStatus = 1")
    List<Rent> findRentsByClientId(@Param("clientId") Long clientId);

}
