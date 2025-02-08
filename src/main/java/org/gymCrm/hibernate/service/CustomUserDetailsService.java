package org.gymCrm.hibernate.service;

import org.gymCrm.hibernate.model.User;
import org.gymCrm.hibernate.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private  UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User>userOpt = userRepository.findByUsername(username);
        if(userOpt.isEmpty()){
            throw  new UsernameNotFoundException("User not found with username " + username);
        }
        return new org.springframework.security.core.userdetails.User(
                userOpt.get().getUsername(),
                userOpt.get().getPassword(),
                userOpt.get().isActive(),
                true, true, true,
                AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER")
        );
    }


}
