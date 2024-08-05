package com.randomrainbow.springboot.demosecurity.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Date;
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
    private Long id;

    @Column(name = "username", unique = true, nullable = false)
    @NotNull(message = "is required")
    private String username;

    @Column(name = "password")
    @NotNull(message = "is required")
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @NotNull(message = "is required")
    @Column(name = "last_name")
    private String lastName;

    @Column(name = "social_media")
    private String socialMedia;

    @Column(name = "artist_description")
    private String artistDescription;

    @Column(name = "email", unique = true, nullable = false)
    @NotNull(message = "is required")
	@Pattern(regexp="^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;  

    @Column(name = "email_verified")
    private boolean emailVerified;

    @Column(name = "verification_token")
    private String verificationToken;
    
    @Column(name = "admission_date")
    private Date admissionDate;


    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
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

    
    @Override
    public boolean isEnabled() {
       return emailVerified;
    }

    public void setResetToken(String resetToken) {
        verificationToken = resetToken;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", username=" + username + ", password=" + password + ", firstName=" + firstName
                + ", lastName=" + lastName + ", socialMedia=" + socialMedia + ", artistDescription=" + artistDescription
                + ", email=" + email + ", role=" + role + ", emailVerified=" + emailVerified + ", verificationToken="
                + verificationToken + ", admissionDate=" + admissionDate + "]";
    }

  

}