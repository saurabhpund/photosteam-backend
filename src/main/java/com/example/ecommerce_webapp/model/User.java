package com.example.ecommerce_webapp.model;

import com.example.ecommerce_webapp.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String fullName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private String bio;

    private String profilePicture;

    private boolean enabled = true;

    private LocalDateTime createdAt = LocalDateTime.now();

    public static List<Role> getRoles(){
        return List.of(Role.SELLER, Role.USER, Role.ADMIN);
    }

    // Purchased images
    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL)
    private List<Purchase> purchases;

    // Spring Security methods
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Change if needed
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Change if implementing lock logic
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // You can add expiry logic if needed
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
