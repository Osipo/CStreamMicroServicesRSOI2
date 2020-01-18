package ru.osipov.deploy.models.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.catalina.User;
import ru.osipov.deploy.entities.Role;
import ru.osipov.deploy.entities.UserEntity;

import java.time.LocalDate;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto implements UserModel {
    private Long id;
    private String username;
    private String name;
    private String email;
    private List<Role> roles;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private String password;


    public UserEntity toUser() {
        UserEntity user = new UserEntity();
        user.setId(id);
        user.setUsername(username);
        user.setName(name);
        user.setEmail(email);
        user.setRoles(roles);
        user.setPassword(password);
        return user;
    }



    public  UserDto(UserEntity user) {
        this.setId(user.getId());
        this.setUsername(user.getUsername());
        this.setName(user.getName());
        this.setEmail(user.getEmail());
        this.setRoles(user.getRoles());
        this.setPassword(user.getPassword());
        this.setCreatedAt(user.getCreatedAt());
        this.setUpdatedAt(user.getUpdatedAt());
    }


    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword(){return password;}

    public void setPassword(String password){this.password = password;}

    public void setRoles(List<Role> roles){this.roles = roles;}

    public List<Role> getRoles(){return roles;}

    public void setCreatedAt(LocalDate date){this.createdAt = date;}

    public void setUpdatedAt(LocalDate date){this.updatedAt = date;}

    public LocalDate getCreatedAt(){return createdAt;}

    public LocalDate getUpdatedAt(){return updatedAt;}
}
