package com.proyecto.Sistema_Informacion.Controller;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.proyecto.Sistema_Informacion.Model.entity.Cita;
import com.proyecto.Sistema_Informacion.Model.entity.Crear;
import com.proyecto.Sistema_Informacion.Model.enums.EstadoCita;
import com.proyecto.Sistema_Informacion.Model.service.CitaService;
import com.proyecto.Sistema_Informacion.Model.service.CrearService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/paciente")
public class PacienteCitaController {

    @Autowired
    private CitaService citaService;

    @Autowired
    private CrearService crearService;

    // 🔹 VER MIS CITAS
    @GetMapping("/mis-citas")
    public String misCitas(Model model, HttpSession session) {

        Crear paciente = (Crear) session.getAttribute("usuario");

        if (paciente == null) {
            return "redirect:/login";
        }

        model.addAttribute("citas", citaService.citasPorPaciente(paciente.getId()));
        return "paciente/mis-citas";
    }

    // 🔹 FORMULARIO AGENDAR
    @GetMapping("/agendar-cita")
    public String agendarCita(Model model, HttpSession session) {

        Crear paciente = (Crear) session.getAttribute("usuario");

        if (paciente == null) {
            return "redirect:/login";
        }

        model.addAttribute("doctores", crearService.listarMedicos());

        return "paciente/agendar-cita";
    }

    // 🔹 GUARDAR CITA
    @PostMapping("/guardar-cita")
    public String guardarCita(
            @RequestParam Long doctorId,
            @RequestParam String fecha,
            @RequestParam String hora,
            @RequestParam String motivo,
            Model model,
            HttpSession session
    ) {

        try {

            Crear paciente = (Crear) session.getAttribute("usuario");
            Crear doctor = crearService.buscarPorId(doctorId);

            Cita cita = new Cita();
            cita.setPaciente(paciente);
            cita.setDoctor(doctor);
            cita.setFecha(LocalDate.parse(fecha));
            cita.setHora(LocalTime.parse(hora));
            cita.setMotivo(motivo);
            cita.setEstado(EstadoCita.PENDIENTE);

            citaService.guardar(cita);

            return "redirect:/paciente/mis-citas";

        } catch (Exception e) {

            model.addAttribute("error", e.getMessage());
            model.addAttribute("doctores", crearService.listarMedicos());

            return "paciente/agendar-cita";
        }
    }
}