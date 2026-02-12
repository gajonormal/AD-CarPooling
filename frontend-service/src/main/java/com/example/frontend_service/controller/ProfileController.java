package com.example.frontend_service.controller;

import com.example.frontend_service.client.AuthClient;
import com.example.frontend_service.dto.UserDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ProfileController {

    @Autowired
    private AuthClient authClient;

    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model) {
        // 1. Verificar se o utilizador está logado
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        // 2. Obter os dados do utilizador (Nome, Email, etc.)
        UserDTO user = authClient.getUser(userId);
        model.addAttribute("user", user);

        // 3. OBTER A MÉDIA DE CLASSIFICAÇÃO (Novo)
        // Tentamos buscar a média; se der erro (sem reviews), pomos 0.0
        try {
            double average = authClient.getUserAverage(userId);
            model.addAttribute("averageRating", average);
        } catch (Exception e) {
            System.err.println("Erro ao obter média do utilizador: " + e.getMessage());
            model.addAttribute("averageRating", 0.0);
        }

        return "profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute UserDTO userDTO, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            authClient.updateUser(userId, userDTO);
            // Opcional: Atualizar o User na sessão se necessário
        }
        return "redirect:/profile?success";
    }
}