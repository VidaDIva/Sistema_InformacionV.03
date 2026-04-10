package com.proyecto.Sistema_Informacion.Controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.proyecto.Sistema_Informacion.Model.entity.Crear;
import com.proyecto.Sistema_Informacion.Model.entity.PQRS;
import com.proyecto.Sistema_Informacion.Model.service.PqrsService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/pqrs")
public class PqrsController {

    private final PqrsService pqrsService;

    public PqrsController(PqrsService pqrsService) {
        this.pqrsService = pqrsService;
    }

    // 📌 Mostrar formulario PQRS
    @GetMapping("/formulario")
    public String mostrarFormulario(Model model) {
        model.addAttribute("pqrs", new PQRS());
        return "Pqrs/pqrs-formulario";
    }

    // 📌 Guardar PQRS
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute PQRS pqrs, HttpSession session) {

        Crear usuario = (Crear) session.getAttribute("usuario");

        if (usuario != null) {
            pqrs.setNombre(usuario.getNombre());
            pqrs.setCorreo(usuario.getCorreo());
            pqrs.setTipoUsuario(usuario.getCargo().getCargo()); // ADMIN, PACIENTE...
        } else {
            pqrs.setTipoUsuario("Anonimo");
        }

        pqrs.setEstado("Pendiente");

        pqrsService.guardar(pqrs);

        return "redirect:/ver-pqrs?ok";
    }

    // 📌 Listar PQRS
        @GetMapping("/lista")
    public String listar(Model model, HttpSession session) {

        Crear usuario = (Crear) session.getAttribute("usuario");

        if (usuario == null || 
            !usuario.getCargo().getCargo().equalsIgnoreCase("ADMIN")) {
            return "redirect:/login";
        }

        model.addAttribute("listaPqrs", pqrsService.listar());
        return "Pqrs/pqrs-lista";
    }

    // 📌 Editar PQRS (mostrar datos)
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        PQRS pqrs = pqrsService.buscarPorId(id);
        model.addAttribute("pqrs", pqrs);
        return "pqrs-formulario";
    }

    // 📌 Actualizar PQRS
    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id, @ModelAttribute PQRS pqrs) {
        pqrsService.actualizar(id, pqrs);
        return "redirect:/pqrs/lista";
    }

    // 📌 Eliminar PQRS
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        pqrsService.eliminar(id);
        return "redirect:/pqrs/lista";
    }

    // 🔍 Filtrar por estado
    @GetMapping("/estado/{estado}")
    public String filtrarPorEstado(@PathVariable String estado, Model model) {
        model.addAttribute("listaPqrs", pqrsService.buscarPorEstado(estado));
        return "pqrs-lista";
    }

    // 🔍 Filtrar por tipo
    @GetMapping("/tipo/{tipo}")
    public String filtrarPorTipo(@PathVariable String tipo, Model model) {
        model.addAttribute("listaPqrs", pqrsService.buscarPorTipoSolicitud(tipo));
        return "pqrs-lista";
    }
}
