package ru.osipov.deploy.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.osipov.deploy.entities.UserEntity;
import ru.osipov.deploy.models.user.UserModel;

import java.util.UUID;

public interface UserService {

//    Page<UserModel> findAll(Pageable pageable);
    UserModel findById(Long id);
    UserEntity findByUsername(String userName);
    UserEntity findByEmail(String email);
    UserEntity create(UserEntity user,String role);
    UserEntity update(Long id, UserEntity user);
    void delete(Long id);

}
