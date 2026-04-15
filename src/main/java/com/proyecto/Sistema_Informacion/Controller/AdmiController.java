package com.proyecto.Sistema_Informacion.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.proyecto.Sistema_Informacion.Model.entity.Crear;
import com.proyecto.Sistema_Informacion.Model.entity.ExamenMedico;
import com.proyecto.Sistema_Informacion.Model.entity.RegistroInsumo;
import com.proyecto.Sistema_Informacion.Model.entity.Vacuna;
import com.proyecto.Sistema_Informacion.Model.enums.EstadoExamen;
import com.proyecto.Sistema_Informacion.Model.enums.EstadoVacuna;
import com.proyecto.Sistema_Informacion.Model.service.CrearService;
import com.proyecto.Sistema_Informacion.Model.service.ExamenMedicoService;
import com.proyecto.Sistema_Informacion.Model.service.PqrsService;
import com.proyecto.Sistema_Informacion.Model.service.RegistroInsumoService;
import com.proyecto.Sistema_Informacion.Model.service.VacunaService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdmiController {

    private final PqrsService pqrsService;
    private final ExamenMedicoService examenService;
    private final VacunaService vacunaService;
    private final RegistroInsumoService insumoService;
    private final CrearService usuarioService;

    public AdmiController(PqrsService pqrsService,
                          ExamenMedicoService examenService,
                          VacunaService vacunaService,
                          RegistroInsumoService insumoService,
                          CrearService usuarioService) {

        this.pqrsService = pqrsService;
        this.examenService = examenService;
        this.vacunaService = vacunaService;
        this.insumoService = insumoService;
        this.usuarioService = usuarioService;
    }

    // ===============================
    // 🏠 HOME ADMIN
    // ===============================
@GetMapping("/home")
public String homeAdmin(Model model, HttpSession session) {

    Crear usuario = (Crear) session.getAttribute("usuario");

    System.out.println("SESION EN HOME: " + usuario);

    if (usuario == null) {
        return "redirect:/login";
    }

    model.addAttribute("usuario", usuario);

    return "Admi/home";
}

    // ===============================
    // 📩 PQRS
    // ===============================
    @GetMapping("/pqrs")
    public String verPqrs(Model model) {
        model.addAttribute("listaPqrs", pqrsService.listar());
        return "Pqrs/pqrs-lista";
    }

    // =====================================================
    // 🧪 EXÁMENES MÉDICOS (ADMIN)
    // =====================================================

    @GetMapping("/examenes")
    public String verExamenesAdmin(Model model) {
        model.addAttribute("examenes", examenService.listar());
        return "Admi/examenes-lista";
    }

    @GetMapping("/examenes/nuevo")
    public String nuevoExamenAdmin(Model model) {
        model.addAttribute("examen", new ExamenMedico());
        model.addAttribute("medicos", usuarioService.listarMedicos());
        return "Admi/examen-formulario";
    }

   @PostMapping("/examenes/guardar")
    public String guardarExamenAdmin(@ModelAttribute ExamenMedico examen) {

        Crear medico = usuarioService.buscarPorId(
                examen.getMedico().getId()
        );

        examen.setMedico(medico);

        if (examen.getFechaRealizacion() != null) {
            examen.setFechaVencimiento(
                    examen.getFechaRealizacion().plusYears(1)
            );
        }

        examen.setEstado(EstadoExamen.VIGENTE);

        examenService.guardar(examen);

        return "redirect:/admin/examenes";
    }

    @GetMapping("/examenes/eliminar/{id}")
    public String eliminarExamenAdmin(@PathVariable Long id) {
        examenService.eliminar(id);
        return "redirect:/admin/examenes";
    }

    // =====================================================
    // 💉 VACUNAS (ADMIN)
    // =====================================================

    @GetMapping("/vacunas")
    public String verVacunasAdmin(Model model) {
        model.addAttribute("vacunas", vacunaService.listar());
        return "Admi/vacunas-lista";
    }

    @GetMapping("/vacunas/nuevo")
    public String nuevaVacunaAdmin(Model model) {
        model.addAttribute("vacuna", new Vacuna());
        model.addAttribute("medicos", usuarioService.listarMedicos());
        return "Admi/vacuna-formulario";
    }

    @PostMapping("/vacunas/guardar")
    public String guardarVacunaAdmin(@ModelAttribute Vacuna vacuna) {

        Crear medico = usuarioService.buscarPorId(
                vacuna.getMedico().getId()
        );

        vacuna.setMedico(medico);

        // 🔥 lógica automática
        if (vacuna.getFechaAplicacion() != null) {
            vacuna.setFechaRefuerzo(
                    vacuna.getFechaAplicacion().plusYears(1)
            );
        }

        vacuna.setEstado(EstadoVacuna.VIGENTE);

        vacunaService.guardar(vacuna);

        return "redirect:/admin/vacunas";
    }

    @GetMapping("/vacunas/eliminar/{id}")
    public String eliminarVacunaAdmin(@PathVariable Long id) {
        vacunaService.eliminar(id);
        return "redirect:/admin/vacunas";
    }

    // =====================================================
    // 🧰 INSUMOS (ADMIN)
    // =====================================================

    @GetMapping("/insumos")
    public String verInsumosAdmin(Model model) {
        model.addAttribute("insumos", insumoService.listar());
        return "Admi/insumos-lista";
    }

    @GetMapping("/insumos/nuevo")
    public String nuevoInsumoAdmin(Model model) {
        model.addAttribute("insumo", new RegistroInsumo());
        model.addAttribute("medicos", usuarioService.listarMedicos());
        return "Admi/insumo-formulario";
    }

    @PostMapping("/insumos/guardar")
    public String guardarInsumoAdmin(@ModelAttribute RegistroInsumo insumo) {

        Crear medico = usuarioService.buscarPorId(
                insumo.getMedico().getId()
        );

        insumo.setMedico(medico);

        insumoService.guardar(insumo);

        return "redirect:/admin/insumos";
    }

    @GetMapping("/insumos/eliminar/{id}")
    public String eliminarInsumoAdmin(@PathVariable Long id) {
        insumoService.eliminar(id);
        return "redirect:/admin/insumos";
    }
}