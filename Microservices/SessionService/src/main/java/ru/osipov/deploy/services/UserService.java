package ru.osipov.deploy.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.osipov.deploy.entities.CustomUser;
import ru.osipov.deploy.entities.UserEntity;
import ru.osipov.deploy.repositories.UserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository rep;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity>  o = rep.getUserEntityByEmail(username);
        if(o.isPresent()){
            CustomUser customUser = new CustomUser(o.get());
            return customUser;
        }
        else
            throw new UsernameNotFoundException("User " + username + " was not found in the database");
    }
}
