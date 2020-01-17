package ru.osipov.deploy.services.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.osipov.deploy.entities.UserEntity;
import ru.osipov.deploy.exceptions.ResourceNotFoundException;
import ru.osipov.deploy.repositories.UserEntityRepository;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    @Autowired
    UserEntityRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(JwtUserDetailsService.class);
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String usernameOrEmail)
            throws UsernameNotFoundException {
        logger.info("Load user By email or username");
        // Let people login with either username or email
        UserEntity user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> { logger.info("Error from db extraction.");
                    return new UsernameNotFoundException("User not found with username or email : " + usernameOrEmail);
                });
        logger.info("Successful LOADED '{}' '{}'",usernameOrEmail, usernameOrEmail);
        return UserPrincipal.create(user);
    }

    @Transactional
    public UserDetails loadUserById(Long id) {
        UserEntity user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id)
        );

        return UserPrincipal.create(user);
    }
}
