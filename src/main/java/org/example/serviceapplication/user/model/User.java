package org.example.serviceapplication.user.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.serviceapplication.offer.model.Offer;
import org.example.serviceapplication.request.model.CustomerRequest;
import org.example.serviceapplication.subCategory.model.SubServiceCategory;
import org.example.serviceapplication.user.enumPackage.Role;
import org.example.serviceapplication.user.enumPackage.Status;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    @Email(message = "InValid email")
    private String email;
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8}$",
            message = "Password must be exactly 8 characters with at least one letter and one digit")
    private String password;
    private boolean active;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Enumerated(EnumType.STRING)
    private Status status;
    private LocalDateTime createdAt;

    @Lob
    byte[] profileImage;

    @ManyToMany(fetch = FetchType.LAZY ,mappedBy = "users",cascade = CascadeType.ALL)
    private List<SubServiceCategory> subServiceCategories=new ArrayList<>();

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
