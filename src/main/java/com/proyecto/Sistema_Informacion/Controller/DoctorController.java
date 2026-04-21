package com.proyecto.Sistema_Informacion.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.proyecto.Sistema_Informacion.Model.entity.Crear;
import com.proyecto.Sistema_Informacion.Model.enums.EstadoCita;
import com.proyecto.Sistema_Informacion.Model.service.CitaService;
import com.proyecto.Sistema_Informacion.Model.service.ExamenMedicoService;
import com.proyecto.Sistema_Informacion.Model.service.RegistroInsumoService;
import com.proyecto.Sistema_Informacion.Model.service.VacunaService;

import jakarta.servlet.http.HttpSession;

@Controller 
public class DoctorController {

     private final ExamenMedicoService examenService;
    private final VacunaService vacunaService;
    private final RegistroInsumoService insumoService;
    private final CitaService citaService;
    

    

    public DoctorController(ExamenMedicoService examenService, VacunaService vacunaService,
            RegistroInsumoService insumoService, CitaService citaService) {
        this.examenService = examenService;
        this.vacunaService = vacunaService;
        this.insumoService = insumoService;
        this.citaService = citaService;
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

    @GetMapping("/doctor/citas")
    public String verCitasDoctor(Model model, HttpSession session) {

        Crear doctor = (Crear) session.getAttribute("usuario");

        if (doctor == null) {
            return "redirect:/login";
        }

        model.addAttribute("citas", citaService.citasPorDoctor(doctor.getId()));

        return "Doctor/doctor-citas";
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

    @GetMapping("/doctor/cita/cancelar/{id}")
    public String cancelarCita(@PathVariable Long id) {

        citaService.actualizarEstado(id, EstadoCita.CANCELADA);

        return "redirect:/doctor/citas";
    }

    @GetMapping("/doctor/cita/confirmar/{id}")
    public String confirmarCita(@PathVariable Long id) {

        citaService.actualizarEstado(id, EstadoCita.CONFIRMADA);

        return "redirect:/doctor/citas";
}
      
}
