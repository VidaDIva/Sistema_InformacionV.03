package com.proyecto.Sistema_Informacion.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.proyecto.Sistema_Informacion.Model.entity.Crear;

import jakarta.servlet.http.HttpSession;

@Controller
public class PacienteController {

    @GetMapping("/paciente/home")
    public String homePaciente(Model model, HttpSession session) {

        Crear usuario = (Crear) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute("usuario", usuario);

        return "paciente/home";
    }
}