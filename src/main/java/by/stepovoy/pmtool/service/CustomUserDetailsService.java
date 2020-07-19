package by.stepovoy.pmtool.service;

import by.stepovoy.pmtool.domain.User;
import by.stepovoy.pmtool.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User loadedUser = userRepository.findByUsername(username);

        if (loadedUser == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return loadedUser;
    }

    @Transactional
    public User loadUserById(Long id) {
        User loadedUser = userRepository.getById(id);
        if (loadedUser == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return loadedUser;
    }
}
