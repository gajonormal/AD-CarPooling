package com.example.frontend_service.controller;

import com.example.frontend_service.client.AuthClient;
import com.example.frontend_service.dto.UserDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private AuthClient authClient;

    // 1. Mostrar a página de Login (GET)
    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // Vai buscar o login.html
    }

    // 2. Processar o Login (POST) - É AQUI QUE O TEU ERRO ESTAVA
    @PostMapping("/login")
    public String processLogin(@RequestParam String email,
                               @RequestParam String password,
                               HttpSession session,
                               Model model) {
        try {
            System.out.println(">>> Tentativa de Login: " + email);

            // Criar objeto para enviar ao Auth-Service
            UserDTO loginRequest = new UserDTO();
            loginRequest.setEmail(email);
            loginRequest.setPassword(password);

            // Chamar o Backend
            UserDTO user = authClient.login(loginRequest);

            if (user != null) {
                System.out.println(">>> SUCESSO! User: " + user.getName());

                // GUARDAR NA SESSÃO (A chave de ouro)
                session.setAttribute("user", user);

                // Redirecionar para a garagem
                return "redirect:/dashboard";
            }

        } catch (Exception e) {
            System.out.println(">>> ERRO: " + e.getMessage());
        }

        // Se falhar, volta ao login com erro
        model.addAttribute("error", "Email ou Password errados!");
        return "login";
    }

    // 3. Logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}