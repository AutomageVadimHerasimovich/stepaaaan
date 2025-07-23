package com.github.vadimher.library.controller;

import com.github.vadimher.library.dto.UserDto;
import com.github.vadimher.library.entity.UserEntity;
import com.github.vadimher.library.mapper.UserMapper;
import com.github.vadimher.library.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class AuthController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    //@PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{username}")
    public ResponseEntity<Void> deleteUser(@PathVariable String username) {
        userService.delete(username);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            Model model
    ) {
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Пароли не совпадают");
            return "register";
        }
        UserDto userDto = UserDto.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .role("ROLE_USER")
                .build();
        try {
            userService.register(userMapper.userDtoToUser(userDto));
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Пользователь с таким именем уже существует");
            return "register";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка регистрации. Попробуйте позже.");
            return "register";
        }
    }
}