package tw.iancheng.giftcardsystem.service;

import tw.iancheng.giftcardsystem.dto.user.UserLoginRequest;
import tw.iancheng.giftcardsystem.dto.user.UserRegisterRequest;
import tw.iancheng.giftcardsystem.model.User;

public interface AuthService {
    User register(UserRegisterRequest userRegisterRequest);

    User login(UserLoginRequest userLoginRequest);
}
