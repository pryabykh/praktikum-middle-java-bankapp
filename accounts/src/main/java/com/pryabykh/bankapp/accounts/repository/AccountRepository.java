package com.pryabykh.bankapp.accounts.repository;

import com.pryabykh.bankapp.accounts.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import com.pryabykh.bankapp.accounts.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findByUserId(Long userId);

    List<Account> findByUser(User user);
}
