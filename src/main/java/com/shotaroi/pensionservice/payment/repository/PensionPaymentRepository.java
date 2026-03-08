package com.shotaroi.pensionservice.payment.repository;

import com.shotaroi.pensionservice.domain.PensionPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PensionPaymentRepository extends JpaRepository<PensionPayment, Long> {

    List<PensionPayment> findByCitizenIdOrderByPaymentDateDesc(Long citizenId);
}
