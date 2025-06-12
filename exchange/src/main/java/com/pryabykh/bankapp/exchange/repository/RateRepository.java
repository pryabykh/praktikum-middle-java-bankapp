package com.pryabykh.bankapp.exchange.repository;

import com.pryabykh.bankapp.exchange.entity.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RateRepository extends JpaRepository<Rate, Long> {
}
