package com.proyecto.Sistema_Informacion.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class LoginController {

    private final CrearService crearService;
    private final EmailService emailService;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public LoginController(CrearService crearService, EmailService emailService) {
        this.crearService = crearService;
        this.emailService = emailService;
    }

    // ===============================
    // LOGIN
    // ===============================
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // ===============================
    // PROCESAR LOGIN
    // ===============================
@PostMapping("/login")
public String procesarLogin(@RequestParam String correo,
        @RequestParam String contrasena,
        HttpSession session,
        Model model) {

    Crear usuario = crearService.buscarPorCorreo(correo);

    if (usuario != null && usuario.getContrasena().equals(contrasena)) {

        session.setAttribute("usuario", usuario);

        String cargo = usuario.getCargo().getCargo(); // nombre del cargo

        if (cargo.equalsIgnoreCase("ADMIN")) {
            return "redirect:/admin/home";
        }

        if (cargo.equalsIgnoreCase("DOCTOR")) {
            return "redirect:/doctor/home";
        }

        if (cargo.equalsIgnoreCase("PACIENTE")) {
            return "redirect:/paciente/home";
        }

        return "redirect:/login";
    }

    model.addAttribute("error", "Correo o contraseña incorrectos");
    return "login";
}

    // ===============================
    // SELECCIONAR ROL
    // ===============================
    @GetMapping("/seleccionar-rol")
    public String seleccionarRol() {
        return "seleccionar-rol";
    }

    @GetMapping("/politicas")
    public String mostrarPoliticas() {
        return "politicas"; // Esto busca el archivo politicas.html en src/main/resources/templates
    }

    @GetMapping("/ver-pqrs")
    public String pqrs() {
        return "redirect:/pqrs/formulario";
    }


    // ===============================
    // MOSTRAR RECUPERAR CONTRASEÑA
    // ===============================
    @GetMapping("/login/recuperar")
    public String mostrarRecuperar() {
        return "recuperar-contra";
    }

    // ===============================
    // ENVIAR CÓDIGO DE RECUPERACIÓN
    // ===============================
    @PostMapping("/login/recuperar")
    public String procesarRecuperar(@RequestParam String correo,
            Model model,
            HttpSession session) {

        Crear usuario = crearService.buscarPorCorreo(correo);

        if (usuario == null) {
            model.addAttribute("error", "El correo no está registrado");
            return "recuperar-contra";
        }

        String codigo = emailService.generarCodigo();

        emailService.enviarCodigo(correo, codigo);

        session.setAttribute("correoRecuperacion", correo);
        session.setAttribute("codigoRecuperacion", codigo);
        session.setAttribute("codigoTiempo", System.currentTimeMillis());
        session.setAttribute("tipoCodigo", "recuperar");

        return "verificar-codigo";
    }

    // ===============================
    // VERIFICAR CÓDIGO
    // ===============================
    @PostMapping("/login/verificar-codigo")
    public String verificarCodigo(@RequestParam String codigo,
            HttpSession session,
            Model model) {

        String tipo = (String) session.getAttribute("tipoCodigo");
        String codigoGuardado = (String) session.getAttribute("codigoRecuperacion");
        Long tiempo = (Long) session.getAttribute("codigoTiempo");

        if (codigoGuardado == null || tiempo == null) {
            model.addAttribute("error", "Sesión expirada");
            return "login";
        }

        if (System.currentTimeMillis() - tiempo > 300000) {
            model.addAttribute("error", "El código expiró");
            return "verificar-codigo";
        }

        if (!codigo.equals(codigoGuardado)) {
            model.addAttribute("error", "Código incorrecto");
            return "verificar-codigo";
        }

        session.setAttribute("codigoVerificado", true);

        if ("recuperar".equals(tipo)) {
            return "cambiar-contrasena";
        }

        if ("registro".equals(tipo)) {
            return "redirect:/cuenta/confirmar-registro";
        }

        return "login";
    }

    // ===============================
    // CAMBIAR CONTRASEÑA
    // ===============================
    @PostMapping("/login/cambiar-contrasena")
    public String cambiarContrasena(@RequestParam String nuevaContrasena,
            @RequestParam String confirmarContrasena,
            HttpSession session,
            Model model) {

        Boolean verificado = (Boolean) session.getAttribute("codigoVerificado");
        String correo = (String) session.getAttribute("correoRecuperacion");

        if (verificado == null || !verificado || correo == null) {
            return "redirect:/login/recuperar";
        }

        if (!nuevaContrasena.equals(confirmarContrasena)) {
            model.addAttribute("error", "Las contraseñas no coinciden");
            return "cambiar-contrasena";
        }

        Crear usuario = crearService.buscarPorCorreo(correo);

        usuario.setContrasena(nuevaContrasena);

        crearService.guardar(usuario);

        session.invalidate();

        model.addAttribute("mensaje", "Contraseña actualizada");

        return "login";
    }

    // ===============================
    // LOGOUT
    // ===============================
    @GetMapping("/logout")
    public String logout(HttpSession session) {

        session.invalidate();

        return "redirect:/seleccionar-rol";
    }

    

}