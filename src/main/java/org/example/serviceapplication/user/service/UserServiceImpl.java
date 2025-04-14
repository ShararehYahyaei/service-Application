package org.example.serviceapplication.user.service;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.example.serviceapplication.Category.exception.NoActiveUsersFound;
import org.example.serviceapplication.Category.service.ServiceCategoryInterface;
import org.example.serviceapplication.subCategory.service.SubServiceCategoryInterface;
import org.example.serviceapplication.user.dto.*;
import org.example.serviceapplication.user.enumPackage.Role;
import org.example.serviceapplication.user.enumPackage.Status;
import org.example.serviceapplication.user.exception.*;
import org.example.serviceapplication.user.model.User;
import org.example.serviceapplication.user.service.customerService.CustomerService;
import org.example.serviceapplication.user.service.specialistService.SpecialistServiceImpl;
import org.example.serviceapplication.user.userRepository.UserRepository;
import org.example.serviceapplication.verification.model.VerificationToken;
import org.example.serviceapplication.verification.repository.VerificationTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @PersistenceContext
    private EntityManager entityManager;
    private final UserRepository userRepository;
    private final ServiceCategoryInterface categoryService;
    private final CustomerService customerService;
    private final SpecialistServiceImpl specialistService;
    private final SubServiceCategoryInterface subService;
    private final EmailService emailService;
    private final VerificationTokenRepository verificationTokenRepository;
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(UserRepository userRepository,
                           ServiceCategoryInterface categoryService,
                           CustomerService customerService,
                           SpecialistServiceImpl specialistService,
                           SubServiceCategoryInterface subService
            , EmailService emailService, VerificationTokenRepository verificationTokenRepository) {

        this.userRepository = userRepository;
        this.categoryService = categoryService;
        this.customerService = customerService;
        this.specialistService = specialistService;
        this.subService = subService;
//        this.verificationService = verificationService;
        this.emailService = emailService;
        this.verificationTokenRepository = verificationTokenRepository;
    }

    @Transactional
    @Override
    public UserResponseDto createUser(UserRequest userRequest, MultipartFile profileImage) {
        logger.info("Creating user");
        if (!isEmailUnique(userRequest.email())) {
            logger.error("Email already exists");
            throw new EmailNotUniqueException("The email is already taken.");
        }
        if (!isPhone(userRequest.phone())) {
            logger.error("phone already exists");
            throw new PhoneIsDuplicated("The phone is already taken.");
        }
        UserResponseDto userResponse = null;
        User user = convertRequestIntoEntity(userRequest);
        user.setActive(false);
        user.setCreatedAt(LocalDateTime.now());
        if (user.getRole() == Role.Customer) {
            user.setStatus(Status.newJoiner);
            userResponse = customerService.createCustomer(user);
        } else if (user.getRole() == Role.Specialist && profileImage != null) {
            user.setStatus(Status.isPendingApproval);
            Long Id = userRequest.subServiceCategoryId();
            userResponse = specialistService.createSpecialist(user, Id);
        }
//        User userByEmail = getUserByEmail(userRequest.email());
//        String token = verificationService.generateVerificationToken(userByEmail.getEmail());
//        emailService.sendVerificationEmail(userRequest.email(), token);
        return userResponse;

    }


    @Transactional(readOnly = true)
    @Override
    public UserResponseDto findById(Long id) {
        Optional<User> userFound = userRepository.findById(id);
        if (userFound.isPresent()) {
            return convertUserToResponseDto(userFound.get());
        }
        logger.error("User not found");
        throw new RuntimeException("User not found");
    }


    @Transactional(readOnly = true)
    @Override
    public List<UserResponseDto> getUsersByName(String name) {
        List<User> users = userRepository.findByNameContainingIgnoreCase(name);
        return users.stream()
                .map(this::convertUserToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserResponseDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::convertUserToResponseDto)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    @Override
    public List<UserResponseDto> getAllActiveUsers() {
        List<User> allUsers = userRepository.findByActiveTrue();

        if (allUsers.isEmpty()) {
            throw new NoActiveUsersFound("No active users found.");
        }
        return allUsers.stream()
                .map(this::convertUserToResponseDto)
                .collect(Collectors.toList());
    }


    @Transactional
    @Override
    public void addSubCategory(Long idSpecialist, Long categoryId) {
        User userFound = getUserSpecialistById(idSpecialist);
        boolean result = userFound.getSubServiceCategories().stream().anyMatch(c -> c.getId().equals(categoryId));
        if (result) {
            throw new DuplicateSubCategoryException("user already has this subServiceCategory");
        }
        specialistService.addSubCategoryToSpecialist(userFound, categoryId);

    }

    @Transactional(readOnly = true)
    @Override
    public List<SpecialistWithSubService> getUserWithSubServiceCategory(Long idSpecialist) {
        User userSpecialistById = getUserSpecialistById(idSpecialist);
        if (userSpecialistById.getSubServiceCategories() != null) {
            return specialistService.getUserWithSubServiceCategory(userSpecialistById);
        } else {
            throw new NotSubServiceCategory("No active users found.");
        }

    }


    @Transactional
    @Override
    public void removeSubCategoryForSpecialist(Long idSpecialist, Long subServiceCategory) {
        User userFound = getUserSpecialistById(idSpecialist);
        specialistService.removeSubCategory(userFound, subServiceCategory);

    }

    @Transactional
    @Override
    public void editSubServiceCategory(Long userId, Long subServiceCategoryOld, Long subServiceCategoryNew) {
        User user = getUserSpecialistById(userId);
        specialistService.editSubServiceCategory(user, subServiceCategoryOld, subServiceCategoryNew);
    }

    private User getUserSpecialistById(Long idSpecialist) {
        Optional<User> userFound = userRepository.findById(idSpecialist);
        if (userFound.isEmpty()) {
            throw new UserNotFond("User not found");
        }

        if (userFound.get().getRole() == Role.Customer || userFound.get().getRole() == Role.Admin) {
            throw new UserHasWrongRole("User has wrong role");
        }
        return userFound.get();
    }


    private User convertRequestIntoEntity(UserRequest userRequest) {
        byte[] image = null;
        try {
            if (userRequest.profileImage() != null) {
                image = userRequest.profileImage().getBytes();
            }
        } catch (IOException e) {
            e.printStackTrace();
            //todo handle It exception
        }

        return new User(
                userRequest.address(),
                userRequest.phone(),
                userRequest.name(),
                userRequest.lastName(),
                userRequest.userName(),
                userRequest.email(),
                userRequest.password(),
                userRequest.role(),
                image
        );
    }


    private UserResponseDto convertUserToResponseDto(User user) {
        UserResponseDto userResponseDto = null;
        if (user.getRole() == Role.Customer) {
            userResponseDto = customerService.convertEntityToResponseDto(user);
        } else if (user.getRole() == Role.Specialist) {
            userResponseDto = specialistService.convertEntityToResponseDto(user);
        }
        return userResponseDto;
    }


    @Transactional(readOnly = true)
    @Override
    public User getUserById(Long id) {
        Optional<User> userFound = userRepository.findById(id);
        if (userFound.isPresent()) {
            return userFound.get();
        } else {
            throw new UserNotFond("User not found");
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<CustomerResponseDto> getAllCustomers() {
        List<User> byRole = userRepository.findByRole(Role.Customer);
        return customerService.
                convertEntitiesToResponseDtos(byRole);

    }

    @Transactional(readOnly = true)
    @Override
    public List<SpecialistResponseDto> getAllSpecialists() {
        List<User> byRole = userRepository.findByRole(Role.Specialist);
        return specialistService.convertEntitiesToResponseDtos(byRole);

    }

    @Override
    @Transactional
    public void updatePassword(PasswordUserRequest changePasswordRequest) {
        if (changePasswordRequest.password().equals(changePasswordRequest.confirmPassword())) {
            User user = userRepository.findById(changePasswordRequest.userId()).get();
            user.setPassword(changePasswordRequest.password());
            userRepository.saveAndFlush(user);
        } else
            throw new RuntimeException("the password is not equal");

    }

    public boolean isEmailUnique(String email) {
        return userRepository.findByEmail(email) == null;
    }

    @Transactional(readOnly = true)
    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void activateUser(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        User user = verificationToken.getUser();
        user.setActive(true);
        userRepository.save(user);
    }


    public boolean isPhone(String phone) {
        return userRepository.findByPhone(phone) == null;
    }


    @Transactional(readOnly = true)
    @Override

    public List<User> searchUsers(String name, String email, String role) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> userRoot = query.from(User.class);
        List<Predicate> predicates = new ArrayList<>();

        if (name != null && !name.isEmpty()) {
            predicates.add(cb.like(userRoot.get("name"), "%" + name + "%"));
        }

        if (email != null && !email.isEmpty()) {
            predicates.add(cb.like(userRoot.get("email"), "%" + email + "%"));
        }


        if (role != null && !role.isEmpty()) {
            predicates.add(cb.like(userRoot.get("role"), "%" + role + "%"));
        }

        query.where(cb.and(predicates.toArray(new Predicate[0])));
        return entityManager.createQuery(query).getResultList();
    }


    @Transactional
    @Override
    public User upadteUser(User user) {
     return    userRepository.save(user);
    }
}
