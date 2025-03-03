package important.service;

import important.model.User;
import important.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        User user = userOpt.get();

        System.out.println("User found: " + user.getEmail());

        UserBuilder builder = org.springframework.security.core.userdetails.User.withUsername(user.getEmail());
        builder.password(user.getPassword());
        builder.roles("USER");

        return builder.build();
    }
}
