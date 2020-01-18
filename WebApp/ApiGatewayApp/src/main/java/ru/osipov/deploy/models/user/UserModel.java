package ru.osipov.deploy.models.user;

import ru.osipov.deploy.entities.Role;

import java.time.LocalDate;
import java.util.List;

public interface UserModel  {
    Long getId();
    String getUsername();
    String getName();
    String getEmail();
    String getPassword();
    List<Role> getRoles();
    LocalDate getCreatedAt();
    LocalDate getUpdatedAt();
}
