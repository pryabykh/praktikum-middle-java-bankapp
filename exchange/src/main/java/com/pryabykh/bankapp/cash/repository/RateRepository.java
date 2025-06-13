package com.pryabykh.bankapp.cash.repository;

import com.pryabykh.bankapp.cash.entity.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RateRepository extends JpaRepository<Rate, Long> {
}
