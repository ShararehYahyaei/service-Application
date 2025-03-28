package org.example.serviceapplication.user.userRepository;


import org.example.serviceapplication.user.enumPackage.Role;
import org.example.serviceapplication.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByNameContainingIgnoreCase(String name);
    List<User> findByActiveTrue();
    List<User> findByRole(Role role);
    User findByEmail(String email);
}

