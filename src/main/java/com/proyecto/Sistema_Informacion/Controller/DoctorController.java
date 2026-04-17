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

        // 🔥 EXÁMENES VENCIDOS
        var examenes = examenService.buscarPorMedico(usuario.getId());
        long vencidos = examenes.stream()
            .filter(e -> e.getFechaVencimiento() != null &&
                        e.getFechaVencimiento().isBefore(java.time.LocalDate.now()))
            .count();

        // 🔥 VACUNAS (ejemplo: sin vacunas = alerta)
        var vacunas = vacunaService.buscarPorMedico(usuario.getId());
        long vacunasPendientes = vacunas.isEmpty() ? 1 : 0;

        // 🔥 INSUMOS BAJOS (ejemplo: cantidad < 5)
        var insumos = insumoService.buscarPorMedico(usuario.getId());
        long insumosBajos = insumos.stream()
            .filter(i -> i.getCantidad() < 5)
            .count();

        // 🔥 TOTAL NOTIFICACIONES
        long totalNotificaciones = vencidos + vacunasPendientes + insumosBajos;

        model.addAttribute("totalNotificaciones", totalNotificaciones);
        model.addAttribute("vencidos", vencidos);
        model.addAttribute("vacunasPendientes", vacunasPendientes);
        model.addAttribute("insumosBajos", insumosBajos);

        return "Doctor/home";
    }
    @GetMapping("/doctor/examenes")
    public String verExamenesDoctor(Model model, HttpSession session) {

        Crear usuario = (Crear) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/login";
        }

        var examenes = examenService.buscarPorMedico(usuario.getId());

        boolean hayVencidos = false;

        for (var e : examenes) {
            if (e.getFechaVencimiento() != null &&
                e.getFechaVencimiento().isBefore(java.time.LocalDate.now())) {

                hayVencidos = true;
                break;
            }
        }

        model.addAttribute("examenes", examenes);
        model.addAttribute("hayVencidos", hayVencidos); // 🔥 CLAVE

        return "doctor/examenes-lista";
    }

    @GetMapping("/doctor/vacunas")
    public String verVacunas(Model model, HttpSession session) {

        Crear usuario = (Crear) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/login";
        }

        var vacunas = vacunaService.buscarPorMedico(usuario.getId());

        boolean sinVacunas = vacunas.isEmpty();

        model.addAttribute("vacunas", vacunas);
        model.addAttribute("sinVacunas", sinVacunas);

        return "doctor/vacunas-lista";
    }

    @GetMapping("/doctor/insumos")
    public String verInsumos(Model model, HttpSession session) {

        Crear usuario = (Crear) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/login";
        }

        var insumos = insumoService.buscarPorMedico(usuario.getId());

        boolean hayBajos = false;

        for (var i : insumos) {
            if (i.getCantidad() <= 5) { // 🔥 tú defines el límite
                hayBajos = true;
                break;
            }
        }

        model.addAttribute("insumos", insumos);
        model.addAttribute("hayBajos", hayBajos);

        return "doctor/insumo-lista";
    }

      
}
