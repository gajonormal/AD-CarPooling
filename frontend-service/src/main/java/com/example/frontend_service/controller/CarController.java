package com.example.frontend_service.controller;

import com.example.frontend_service.client.VehicleClient;
import com.example.frontend_service.dto.VehicleDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class CarController {

    @Autowired
    private VehicleClient vehicleClient;

    // 1. Listar Carros (Já deves ter isto)
    @GetMapping("/cars")
    public String listCars(Model model, HttpSession session) {
        if (session.getAttribute("user") == null) return "redirect:/login";

        try {
            List<VehicleDTO> cars = vehicleClient.getAllVehicles();
            model.addAttribute("cars", cars);
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao carregar carros.");
        }
        return "cars";
    }

    // 2. Mostrar Formulário de Novo Carro (FALTAVA ISTO!)
    @GetMapping("/cars/new")
    public String showCreateCarForm(Model model, HttpSession session) {
        if (session.getAttribute("user") == null) return "redirect:/login";

        model.addAttribute("car", new VehicleDTO());
        return "car-form"; // Precisamos de criar este HTML no Passo 2
    }

    // 3. Guardar o Carro (FALTAVA ISTO!)
    @PostMapping("/cars")
    public String createCar(@ModelAttribute VehicleDTO car, HttpSession session) {
        if (session.getAttribute("user") == null) return "redirect:/login";

        try {
            // Opcional: associar o ID do dono se o DTO tiver esse campo
            // car.setOwnerId(user.getId());
            vehicleClient.createVehicle(car);
        } catch (Exception e) {
            System.out.println("Erro ao criar carro: " + e.getMessage());
        }
        return "redirect:/cars";
    }
}