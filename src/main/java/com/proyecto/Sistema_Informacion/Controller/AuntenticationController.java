package com.proyecto.Sistema_Informacion.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.proyecto.Sistema_Informacion.Model.entity.Crear;
import com.proyecto.Sistema_Informacion.Model.service.CrearService;
import com.proyecto.Sistema_Informacion.Model.service.EmailService;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuntenticationController {

    private final CrearService crearService;
    private final EmailService emailService;

    public AuntenticationController(CrearService crearService, EmailService emailService) {
        this.crearService = crearService;
        this.emailService = emailService;
    }

    @GetMapping("/cuenta/verificar")
    public String mostrarVerificarCodigo(Model model, HttpSession session) {
        String correo = (String) session.getAttribute("correoRecuperacion");
        model.addAttribute("correo", correo);
        return "verificar-codigo"; // Thymeleaf template
    }

    @PostMapping("/cuenta/enviarCodigo")
    public String enviarCodigo(
            @RequestParam String correo,
            @RequestParam String contrasena,
            @RequestParam String confirmar,
            HttpSession session,
            Model model) {
    
        if (!contrasena.equals(confirmar)) {
            model.addAttribute("error", "Las contraseñas no coinciden");
            return "registro-credenciales";
        }
    
        if (crearService.buscarPorCorreo(correo) != null) {
            model.addAttribute("error", "El correo ya está registrado");
            return "registro-credenciales";
        }
    
        String codigo = emailService.generarCodigo(); // usa el mismo generador que recuperación
    
        // ✅ UNIFICAR nombres de sesión
        session.setAttribute("codigoRecuperacion", codigo);
        session.setAttribute("correoRecuperacion", correo);
        session.setAttribute("contrasenaTemp", contrasena);
        session.setAttribute("codigoTiempo", System.currentTimeMillis());
        session.setAttribute("tipoCodigo", "registro");
    
        emailService.enviarCodigo(correo, codigo);
    
        return "redirect:/cuenta/verificar";
    }

    @PostMapping("/cuenta/validarCodigo")
    public String validarCodigo(@RequestParam String codigoIngresado,
            HttpSession session,
            Model model) {

        Crear usuario = (Crear) session.getAttribute("usuarioTemp");

        if (usuario == null) {
            return "redirect:/cuenta/registro";
        }

        String codigoGuardado = (String) session.getAttribute("codigoVerificacion");

        if (!codigoIngresado.equals(codigoGuardado)) {
            model.addAttribute("error", "Código incorrecto");
            return "verificar-codigo";
        }

        String correo = (String) session.getAttribute("correoTemp");
        String contrasena = (String) session.getAttribute("contrasenaTemp");

        usuario.setCorreo(correo);
        usuario.setContrasena(contrasena);

        crearService.guardar(usuario);

        session.removeAttribute("usuarioTemp");
        session.removeAttribute("codigoVerificacion");
        session.removeAttribute("correoTemp");
        session.removeAttribute("contrasenaTemp");

        return "redirect:/login";
    }
}
