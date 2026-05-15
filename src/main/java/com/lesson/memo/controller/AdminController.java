package com.lesson.memo.controller;

import java.time.LocalDateTime;

import jakarta.validation.Valid;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lesson.memo.model.Admin;
import com.lesson.memo.repository.AdminRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

	private final PasswordEncoder passwordEncoder;
    private final AdminRepository adminRepository;
    
    @GetMapping("/signup")
    public String SignupForm(Model model) {
    	model.addAttribute("admin", new Admin());
        return "signup";
    }
    
    @PostMapping("/signup")
	public String signup(@ModelAttribute @Valid Admin admin,
       BindingResult result,
       Model model
    ) {
        if (result.hasErrors()) {
            return "signup";
        }
            
        String encodedPassword = passwordEncoder.encode(admin.getPassword());
        admin.setPassword(encodedPassword);

        admin.setCreatedAt(LocalDateTime.now());
        admin.setUpdatedAt(LocalDateTime.now());
        adminRepository.save(admin);
    	return "redirect:/admin/signin";
    }
    
    @GetMapping("/signin")
    public String signinForm() {
        return "signin";
    }
}
