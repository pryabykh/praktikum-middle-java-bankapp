package com.pryabykh.bankapp.front.service;

import com.pryabykh.bankapp.front.feign.accounts.AccountsFeignClient;
import com.pryabykh.bankapp.front.feign.accounts.UserDto;
import feign.FeignException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final AccountsFeignClient accountsFeignClient;

    public UserDetailsServiceImpl(AccountsFeignClient accountsFeignClient) {
        this.accountsFeignClient = accountsFeignClient;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            UserDto userInfo = accountsFeignClient.fetchUserByLogin(username);

            return User.withUsername(username)
                    .password(userInfo.getPassword())
                    .authorities(new GrantedAuthority[0])
                    .accountExpired(false)
                    .credentialsExpired(false)
                    .accountLocked(false)
                    .disabled(false)
                    .build();

        } catch (FeignException.FeignClientException ex) {
            throw new UsernameNotFoundException("User service unavailable", ex);
        }
    }
}
