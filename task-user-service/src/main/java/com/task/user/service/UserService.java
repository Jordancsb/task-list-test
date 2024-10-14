package com.task.user.service;

import com.task.user.model.User;

public interface UserService {

    public User getUserProfile(String token);

}
