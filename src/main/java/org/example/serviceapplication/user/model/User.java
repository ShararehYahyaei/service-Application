package org.example.serviceapplication.user.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.serviceapplication.user.enumPackage.Role;
import org.example.serviceapplication.user.enumPackage.Status;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String address;
    private String phone;
    private String name;
    private String lastName;
    private String userName;
    private String email;
    private String password;
    private boolean active;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Enumerated(EnumType.STRING)
    private Status status;
    private LocalDateTime createdAt;
    @Lob
    byte[] profileImage;

    public User(String address, String phone, String name, String lastName,
                String userName, String email, String password, Role role,
                byte[] profileImageBytes) {
        this.address = address;
        this.phone = phone;
        this.name = name;
        this.lastName = lastName;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.profileImage = profileImageBytes;
    }



}
