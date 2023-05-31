package de.tutorial.authentication.backend.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Transactional
  public void register(UserRegisterationDto userRegisterationDto) {
    userRepository.saveAndFlush(
        new UserEntity()
            .setUsername(userRegisterationDto.getUsername())
            .setPassword(passwordEncoder.encode(userRegisterationDto.getPassword())));
  }
}
