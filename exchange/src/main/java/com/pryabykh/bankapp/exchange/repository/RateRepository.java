package com.pryabykh.bankapp.exchange.repository;

import com.pryabykh.bankapp.exchange.entity.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RateRepository extends JpaRepository<Rate, Long> {

    List<Rate> findAllByBaseFalse();
}
