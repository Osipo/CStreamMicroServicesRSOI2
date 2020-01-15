package ru.osipov.deploy.entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Collection;

public class CustomUser extends User {
    private static final long serialVersionUID = 1L;
    public CustomUser(UserEntity user) {
        super(user.getEmail(), user.getPassword(),user.getGrantedAuthoritiesList());
    }
}