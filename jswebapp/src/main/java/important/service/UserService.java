package important.service;

import important.model.User;
import important.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(String email, String password) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("User with this email already exists.");
        }

        String hashedPassword = passwordEncoder.encode(password);
        User newUser = User.builder()
                .email(email)
                .password(hashedPassword)
                .build();

        return userRepository.save(newUser);
    }

    public Optional<User> authenticateUser(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            System.out.println("DB Password: " + user.getPassword()); // Debugging
            System.out.println("Input Password: " + passwordEncoder.encode(password)); // Debugging

            if (passwordEncoder.matches(password, user.getPassword())) {  // ← Aici verifică hash-ul!
                return Optional.of(user);
            } else {
                System.out.println("Passwords do NOT match!");
            }
        }
        return Optional.empty();
    }

}
