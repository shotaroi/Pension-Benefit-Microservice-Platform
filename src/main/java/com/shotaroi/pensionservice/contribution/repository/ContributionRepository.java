package com.shotaroi.pensionservice.contribution.repository;

import com.shotaroi.pensionservice.domain.Contribution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContributionRepository extends JpaRepository<Contribution, Long> {

    List<Contribution> findByCitizenIdOrderByYearAsc(Long citizenId);

    Optional<Contribution> findByCitizenIdAndYear(Long citizenId, Integer year);

    boolean existsByCitizenIdAndYear(Long citizenId, Integer year);
}
