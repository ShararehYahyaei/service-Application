package org.example.serviceapplication.user.userController;

import jakarta.validation.Valid;
import org.example.serviceapplication.user.dto.UserRequest;
import org.example.serviceapplication.user.enumPackage.Role;
import org.example.serviceapplication.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
@RequestMapping("/user")
public class UserWebController {

    private final UserService userService;

    public UserWebController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/create")
    public String showCreateUserForm(Model model) {
        model.addAttribute("userRequest", new UserRequest("",
                "", "", "", "", "", "",
                Role.Customer, null));
        return "userCreate";
    }

    @PostMapping("/create")
    public String createUser(@ModelAttribute @Valid UserRequest userRequest,
                             BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "userCreate";
        }

        userService.createUser(userRequest);

        model.addAttribute("success", "User created successfully!");
        return "userCreate";
    }


//    // نمایش فرم تغییر رمز عبور
//    @GetMapping("/changePassword")
//    public String changePasswordPage(Model model) {
//        model.addAttribute("passwordUserRequest", new PasswordUserRequest(null,
//                "",""));
//        return "change-password";
//    }
//
//    // پردازش تغییر رمز عبور
//    @PostMapping("/changePassword")
//    public ResponseEntity<String> updatePassword(@ModelAttribute PasswordUserRequest changePasswordRequest) {
//
//        userService.updatePassword(changePasswordRequest);
//        return ResponseEntity.ok("رمز عبور با موفقیت تغییر کرد");
//
//    }


    @GetMapping("/")
    public String viewListUsers(Model model) {
        model.addAttribute("listUsers", userService.getAllUsers());
        return "users";
    }


}
