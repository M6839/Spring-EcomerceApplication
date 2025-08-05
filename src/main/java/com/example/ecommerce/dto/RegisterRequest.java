
package com.example.ecommerce.dto;

import java.util.Set;

import com.example.ecommerce.enums.Role;

public class RegisterRequest {
    private String username;
    private String password;
    private String email;
    private Set<Role> role; 

    public RegisterRequest() {}

    public RegisterRequest(String username, String password,String email, Set<Role> role) {
        this.username = username;
        this.password = password;
        this.email=email;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
		return email;
	}


	public String getPassword() {
        return password;
    }

    public Set<Role> getRole() {
        return role;
    }
}
