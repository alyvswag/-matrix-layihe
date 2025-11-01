package org.example.demo13213.security;



import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.demo13213.exception.BaseException;
import org.example.demo13213.model.dao.Users;
import org.example.demo13213.repo.user.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDetailsServiceImpl implements UserDetailsService {

    final UserRepo userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = findUserByUser(username);
        return new UserPrincipal(users.getId(), users.getUsername(), users.getPassword());
    }

    private Users findUserByUser(String user) {
        return  userRepository.findUserByUsername(user)
                .orElseThrow(
                        () -> BaseException.notFound(Users.class.getSimpleName(), "user", user)
                );
    }
}