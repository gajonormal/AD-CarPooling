package com.example.frontend_service.controller;

import com.example.frontend_service.client.AuthClient;
import com.example.frontend_service.dto.UserDTO;
import feign.FeignException; // <--- IMPORTANTE: Adiciona esta importação!
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
        return "login";
    }

    // 2. Processar o Login (POST)
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
            // Se o user estiver suspenso, o AuthClient lança uma exceção AQUI!
            UserDTO user = authClient.login(loginRequest);

            if (user != null) {
                System.out.println(">>> SUCESSO! User ID: " + user.getId());

                // Guardar dados na sessão
                session.setAttribute("userId", user.getId());
                session.setAttribute("userName", user.getName());
                session.setAttribute("user", user); // Objeto completo (útil para verificar admin)

                // --- REDIRECIONAMENTO INTELIGENTE ---
                // Se for ADMIN, vai para o painel de administração
                if ("ADMIN".equalsIgnoreCase(user.getType())) {
                    return "redirect:/admin";
                }

                // Se for user normal, vai para o dashboard
                return "redirect:/dashboard";
            }

        } catch (FeignException e) {
            // Apanhámos um erro vindo do Backend (Auth-Service)

            if (e.status() == 403) {
                // Erro 403 = CONTA SUSPENSA
                model.addAttribute("error", "A tua conta foi suspensa. Contacta o Administrador.");
            } else if (e.status() == 401) {
                // Erro 401 = Password errada
                model.addAttribute("error", "Email ou Password errados!");
            } else {
                // Outro erro qualquer
                model.addAttribute("error", "Erro no sistema. Tenta mais tarde.");
            }
            return "login"; // Volta para o login com a mensagem correta

        } catch (Exception e) {
            // Erros gerais (ex: backend desligado)
            System.out.println(">>> ERRO GERAL: " + e.getMessage());
            model.addAttribute("error", "Erro ao ligar ao servidor.");
            return "login";
        }

        return "login";
    }

    // 3. Logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}