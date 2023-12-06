package tw.iancheng.giftcardsystem.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tw.iancheng.giftcardsystem.dto.user.UserRegisterRequest;
import tw.iancheng.giftcardsystem.model.User;
import tw.iancheng.giftcardsystem.repository.UserRepository;
import tw.iancheng.giftcardsystem.service.AuthService;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User register(UserRegisterRequest userRegisterRequest) {
        if (userExist(userRegisterRequest))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        User user = buildUser(userRegisterRequest);

        return userRepository.save(user);
    }

    private boolean userExist(UserRegisterRequest userRegisterRequest) {
        User user = userRepository.getUserByEmail(userRegisterRequest.getEmail());

        return user != null;
    }

    private User buildUser(UserRegisterRequest userRegisterRequest) {
        String hashedPassword = hashPassword(userRegisterRequest.getPassword());

        return User.builder().
                email(userRegisterRequest.getEmail()).password(hashedPassword)
                .build();
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());

            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
