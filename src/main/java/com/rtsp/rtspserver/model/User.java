package com.rtsp.rtspserver.model;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class User {
    private int userId;
    private String userLogin;
    private int roleId;
    private String hashPassword;

    public User(int userId, String userLogin, int roleId, String password) {
        this.userId = userId;
        this.userLogin = userLogin;
        this.roleId = roleId;
        this.hashPassword = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // Getters and setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getHashPassword() {
        return hashPassword;
    }

    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }
}

