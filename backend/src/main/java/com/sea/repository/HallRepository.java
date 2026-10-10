package com.sea.repository;

import com.sea.model.Hall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HallRepository extends JpaRepository<Hall, Long> {
    Optional<Hall> findByHallNumber(String hallNumber);
    List<Hall> findByIsActiveTrue();
    List<Hall> findByBlock(String block);
    Boolean existsByHallNumber(String hallNumber);
}
