package com.catalog.catalog.security;

import com.catalog.catalog.model.User;
import com.catalog.catalog.repository.UserRepository;
import org.hibernate.annotations.DialectOverride;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        User user = userRepository.findByEmail(email);

        if(user != null){
            return new org.springframework.security.core.userdetails.User(user.getEmail(),
                    user.getPassword(),getAuthorities());
        }else{
            throw new UsernameNotFoundException("Niepoprawne dane");
        }
    }
    private Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

}
