package com.shotaroi.pensionservice.citizen.repository;

import com.shotaroi.pensionservice.domain.Citizen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CitizenRepository extends JpaRepository<Citizen, Long> {

    Optional<Citizen> findByPersonalNumber(String personalNumber);

    boolean existsByPersonalNumber(String personalNumber);
}
