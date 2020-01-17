package ru.osipov.deploy.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.osipov.deploy.entities.Role;
import ru.osipov.deploy.entities.RoleName;
import ru.osipov.deploy.entities.UserEntity;
import ru.osipov.deploy.exceptions.HttpNotFoundException;
import ru.osipov.deploy.models.user.UserDto;
import ru.osipov.deploy.models.user.UserModel;
import ru.osipov.deploy.repositories.RoleRepository;
import ru.osipov.deploy.repositories.UserEntityRepository;
import ru.osipov.deploy.repositories.AuthorizationCodeRepository;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserEntityRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserEntityRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

//    @Override
//    public Page<UserModel> findAll(Pageable pageable) {
//        logger.info("findAll() method called: ");
//        Page<UserModel> result = userRepository.findAllUserEntities(pageable);
//        logger.info("FIND ALL Users: {}", result.getContent());
//        return result;
//    }

    @Override
    public UserModel findById(Long id) {
        logger.info("findById() method called: ");
        UserEntity result = userRepository.findById(id)
                .orElseThrow(() -> new HttpNotFoundException(String.format("User could not be found with id: %s", id)));
        logger.info("FIND BY ID User: {} - found by id: {}", result, id);
        return toUserModelFromEntity(result);
    }

    @Override
    public UserEntity findByUsername(String userName) {
        logger.info("findByUsername() method called: ");
        UserEntity result = userRepository.findByUsername(userName)
                .orElseThrow(() -> new HttpNotFoundException(String.format("User could not be found with username: %s", userName)));
        logger.info("FIND BY USERNAME User: {} - found by username: {}", result, userName);
        return result;
    }

    @Override
    public UserEntity findByEmail(String email) {
        logger.info("findByEmail() method called: ");
        UserEntity result = userRepository.findByEmail(email)
                .orElseThrow(() -> new HttpNotFoundException(String.format("User could not be found with email: %s", email)));
        logger.info("FIND BY EMAIL User: {} - found by email: {}", result, email);
        return result;
    }

    @Override
    @Transactional
    public UserEntity create(UserEntity user) {
        logger.info("create() method called: ");

        Optional<Role> roleUser = roleRepository.findByName(RoleName.ROLE_USER);
        List<Role> userRoles = new ArrayList<>();
        roleUser.ifPresent(userRoles::add);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(userRoles);
        checkUserData(user);

        UserEntity result = userRepository.saveAndFlush(user);

        logger.info("CREATE User: {} - successfully", result);
        return result;
    }

    @Override
    public UserEntity update(Long id, UserEntity user) {
        logger.info("update() method called: ");
        user.setId(id);
        checkUserData(user);
        UserEntity result = userRepository.saveAndFlush(user);
        logger.info("UPDATE User: {} - successfully", result);
        return result;
    }

    private void checkUserData(UserEntity user) {
        UserEntity checkLogin = userRepository.findByUsername(user.getUsername()).orElse(null);
        UserEntity checkEmail = userRepository.findByEmail(user.getEmail()).orElse(null);

        if (checkLogin != null && checkEmail != null)
            throw new ConstraintViolationException("(username, email)=("+user.getUsername()+", "+user.getEmail()+")", null);
        else if (checkLogin != null)
            throw new ConstraintViolationException("(username)=("+user.getUsername()+")", null);
        else if (checkEmail != null)
            throw new ConstraintViolationException("(email)=("+user.getEmail()+")", null);
    }

    @Override
    public void delete(Long id) {
        logger.info("delete() method called.");
        userRepository.deleteById(id);
    }

    private UserModel toUserModelFromEntity(UserEntity entity){
        UserModel m = new UserDto(entity);
        return m;
    }
}
