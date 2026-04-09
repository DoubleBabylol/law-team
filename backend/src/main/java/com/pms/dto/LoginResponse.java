package com.pms.dto;

import java.util.List;

public class LoginResponse {
    private String token;
    private String userId;
    private String username;
    private List<RoleDTO> roles;
    private List<MenuTreeDTO> menus;

    public LoginResponse() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<RoleDTO> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleDTO> roles) {
        this.roles = roles;
    }

    public List<MenuTreeDTO> getMenus() {
        return menus;
    }

    public void setMenus(List<MenuTreeDTO> menus) {
        this.menus = menus;
    }
}
