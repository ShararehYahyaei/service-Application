package org.example.serviceapplication.Cusotmer.model;


import jakarta.persistence.*;
import lombok.*;

import org.example.serviceapplication.enumPackage.Role;
import org.example.serviceapplication.enumPackage.Status;

@Entity
@AllArgsConstructor
@Table
@Getter
@Setter
@NoArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String address;
    private String phone;
    private String name;
    private String email;
    private String password;
    private boolean active;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Lob
    private byte[] profileImage;




    public Customer(String name, String email, String phone, String address, String password,
                    boolean active, Role role, Status status,byte[] profileImage) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.password = password;
        this.active = active;
        this.role = role;
        this.status = status;
        this.profileImage = profileImage;
    }
//todo list od orders

}
