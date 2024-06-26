package com.randomrainbow.springboot.demosecurity.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User implements UserDetails{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    @NotNull(message = "is required")
	@Size(min = 1, message = "is required")
    private Long id;

    @Column(name = "username")
    @NotNull(message = "is required")
	@Size(min = 1, message = "is required")
    private String username;

    @Column(name = "password")
    @NotNull(message = "is required")
	@Size(min = 1, message = "is required")
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @NotNull(message = "is required")
	@Size(min = 1, message = "is required")
    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    @NotNull(message = "is required")
	@Size(min = 1, message = "is required")
	@Pattern(regexp="^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;  

    @Column(name = "enabled")
    private boolean enabled;



    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + username + '\'' +
                ", enabled=" + enabled +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", roles=" + role +
                '}';
    }

        @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
       return true; 
    }


}