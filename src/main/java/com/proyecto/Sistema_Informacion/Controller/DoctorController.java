package com.proyecto.Sistema_Informacion.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.proyecto.Sistema_Informacion.Model.entity.Crear;
import com.proyecto.Sistema_Informacion.Model.service.ExamenMedicoService;
import com.proyecto.Sistema_Informacion.Model.service.RegistroInsumoService;
import com.proyecto.Sistema_Informacion.Model.service.VacunaService;

import jakarta.servlet.http.HttpSession;

@Controller 
public class DoctorController {

     private final ExamenMedicoService examenService;
    private final VacunaService vacunaService;
    private final RegistroInsumoService insumoService;
    

    
   
    public DoctorController(ExamenMedicoService examenService, VacunaService vacunaService,
            RegistroInsumoService insumoService) {
        this.examenService = examenService;
        this.vacunaService = vacunaService;
        this.insumoService = insumoService;
        
    }

    @GetMapping("/doctor/home")
    public String homeDoctor(Model model, HttpSession session) {

        Crear usuario = (Crear) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute("usuario", usuario);

        return "Doctor/home";
    }

    @GetMapping("/doctor/examenes")
    public String verExamenesDoctor(Model model, HttpSession session) {

        Crear usuario = (Crear) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute("examenes", examenService.buscarPorMedico(usuario.getId()));

        return "Doctor/examenes-lista";
    }

    @GetMapping("/doctor/vacunas")
    public String verVacunas(Model model, HttpSession session) {

        Crear usuario = (Crear) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute("vacunas", vacunaService.buscarPorMedico(usuario.getId()));

        return "Doctor/vacunas-lista";
    }

       @GetMapping("/doctor/insumos")
        public String verInsumos(Model model, HttpSession session) {

            Crear usuario = (Crear) session.getAttribute("usuario");

            if (usuario == null) {
                return "redirect:/login";
            }

           model.addAttribute("insumos", insumoService.buscarPorMedico(usuario.getId()));

            return "Doctor/insumo-lista";
        }

      
}
