package de.tutorial.authentication.backend.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserEntity register(UserRegisterationDto userRegisterationDto) {
        return userRepository.save(new UserEntity()
                .setUsername(userRegisterationDto.getUsername())
                .setPassword(userRegisterationDto.getPassword()));
    }
}
