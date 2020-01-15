package ru.osipov.deploy.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "password", nullable = false)
    private String password;


    @Transient
    private Collection<GrantedAuthority> grantedAuthoritiesList = new ArrayList<>();

    public Collection<GrantedAuthority> getGrantedAuthoritiesList(){
        Collection<GrantedAuthority> la = new ArrayList<>();
        la.add(new SimpleGrantedAuthority("ROLE_SYSTEMADMIN"));
        return la;
    }
}