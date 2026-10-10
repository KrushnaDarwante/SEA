package com.sea.repository;

import com.sea.model.QRVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QRVerificationRepository extends JpaRepository<QRVerification, Long> {
    Optional<QRVerification> findByVerificationHash(String verificationHash);
    Optional<QRVerification> findByStudentRollAndExamCode(String studentRoll, String examCode);
    Boolean existsByStudentRollAndExamCode(String studentRoll, String examCode);
}
