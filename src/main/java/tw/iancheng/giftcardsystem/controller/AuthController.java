package tw.iancheng.giftcardsystem.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tw.iancheng.giftcardsystem.dto.user.UserRegisterRequest;
import tw.iancheng.giftcardsystem.model.User;
import tw.iancheng.giftcardsystem.service.AuthService;

@Validated
@RestController
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/auth/register")
    public ResponseEntity<User> register(@RequestBody @Validated UserRegisterRequest userRegisterRequest) {
        User user = authService.register(userRegisterRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}
