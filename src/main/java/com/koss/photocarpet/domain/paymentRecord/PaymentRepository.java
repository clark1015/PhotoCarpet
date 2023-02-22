package com.koss.photocarpet.domain.paymentRecord;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.Id;

public interface PaymentRepository extends JpaRepository<PaymentRecord, Long> {

}
