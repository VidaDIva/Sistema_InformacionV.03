package com.proyecto.Sistema_Informacion.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.proyecto.Sistema_Informacion.Model.entity.Crear;
import com.proyecto.Sistema_Informacion.Model.service.PqrsService;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdmiController {

    private final PqrsService pqrsService;

    public AdmiController(PqrsService pqrsService) {
        this.pqrsService = pqrsService;
    }
    @GetMapping("/admin/home")
    public String homeAdmin(Model model, HttpSession session) {

        Crear usuario = (Crear) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute("usuario", usuario);

        return "Admi/home";
    }

    @GetMapping("/pqrs")
    public String verPqrs(Model model) {
        model.addAttribute("listaPqrs", pqrsService.listar());
        return "Pqrs/pqrs-lista";
    }

}
