package com.pryabykh.bankapp.transfer.repository;

import com.pryabykh.bankapp.transfer.entity.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RateRepository extends JpaRepository<Rate, Long> {
}
