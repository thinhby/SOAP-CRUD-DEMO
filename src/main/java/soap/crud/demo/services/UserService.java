package soap.crud.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import soap.crud.demo.entities.User;
import soap.crud.demo.repositories.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository repo;

    @Autowired
    private PasswordEncoder encoder;

    private static final String USER_ALREADY_EXISTS = "User already exists";

    public User register(String username, String password) {

        if (repo.findByUsername(username).isPresent()) {
            throw new RuntimeException(USER_ALREADY_EXISTS);
        }

        User u = new User();
        u.setUsername(username);

        // hash password
        u.setPassword(encoder.encode(password));

        return repo.save(u);
    }
}
