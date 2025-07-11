package com.example.ecommerce_webapp.dto;

import com.example.ecommerce_webapp.enums.Role;
import com.example.ecommerce_webapp.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {
    private Long id;
    private String fullName;
    private String email;
    private Role role;

    public UserResponseDTO(User user){
        this.id = user.getId();
        this.fullName = user.getFullName();
        this.email = user.getEmail();
        this.role = user.getRole();
    }

}
