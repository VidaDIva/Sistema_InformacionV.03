package com.proyecto.Sistema_Informacion.Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.proyecto.Sistema_Informacion.Model.entity.Cargo;
import com.proyecto.Sistema_Informacion.Model.entity.Crear;
import com.proyecto.Sistema_Informacion.Model.service.CargoService;
import com.proyecto.Sistema_Informacion.Model.service.CrearService;
import com.proyecto.Sistema_Informacion.Model.service.EmailService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/cuenta")
public class CuentaController {

    private final CrearService crearService;
    private final EmailService emailService;
    private final CargoService cargoService;

    public CuentaController(CrearService crearService, EmailService emailService, CargoService cargoService) {
        this.crearService = crearService;
        this.emailService = emailService;
        this.cargoService = cargoService;
    }
    @GetMapping("/registro")
    public String mostrarDatos(Model model) {

        model.addAttribute("crear", new Crear());
        model.addAttribute("cargos", cargoService.listar());

        return "registro-datos";
    }

    @PostMapping("/registro-datos")
    public String guardarDatos(@ModelAttribute Crear crear,
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file,
            HttpSession session) throws IOException {

        if (!file.isEmpty()) {
            String nombreArchivo = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path carpeta = Paths.get("uploads/Doctor/");
            if (!Files.exists(carpeta)) {
                Files.createDirectories(carpeta);
            }
            Path ruta = carpeta.resolve(nombreArchivo);
            Files.write(ruta, file.getBytes());
            crear.setImagen(nombreArchivo);
        }

        // 🔹 Obtener el cargo completo desde la base de datos
        Long idCargo = crear.getCargo().getId();

        Cargo cargoReal = cargoService.buscarPorId(idCargo);

        crear.setCargo(cargoReal);

        session.setAttribute("usuarioTemp", crear);

        return "redirect:/cuenta/credenciales";
    }

    @GetMapping("/credenciales")
    public String mostrarCredenciales() {
        return "registro-credenciales";
    }

    @PostMapping("/credenciales")
    public String procesarCredenciales(@RequestParam String correo,
            @RequestParam String contrasena,
            HttpSession session,
            org.springframework.ui.Model model) {

        Crear usuarioTemp = (Crear) session.getAttribute("usuarioTemp");

        if (usuarioTemp == null) {
            model.addAttribute("error", "La sesión expiró, vuelve a registrar los datos.");
            return "registro-datos";
        }

        session.setAttribute("correoRecuperacion", correo);
        session.setAttribute("contrasenaTemp", contrasena);

        String codigo = emailService.generarCodigo();
        emailService.enviarCodigo(correo, codigo);

        session.setAttribute("codigoRecuperacion", codigo);
        session.setAttribute("codigoTiempo", System.currentTimeMillis());
        session.setAttribute("tipoCodigo", "registro");

        return "verificar-codigo";
    }

    @GetMapping("/confirmar-registro")
    public String confirmarRegistro(HttpSession session) {

        Boolean verificado = (Boolean) session.getAttribute("codigoVerificado");

        if (verificado == null || !verificado) {
            return "redirect:/login";
        }

        Crear usuarioTemp = (Crear) session.getAttribute("usuarioTemp");

        if (usuarioTemp == null) {
            return "redirect:/cuenta/registro";
        }

        String correo = (String) session.getAttribute("correoRecuperacion");
        String contrasena = (String) session.getAttribute("contrasenaTemp");

        usuarioTemp.setCorreo(correo);
        usuarioTemp.setContrasena(contrasena);

        crearService.guardar(usuarioTemp);

        session.invalidate();

        return "login";
    }

    // ✅ CORRECCIÓN: usar el mismo nombre de atributo de sesión que LoginController
    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
        Crear usuario = (Crear) session.getAttribute("usuario"); // ⚡ clave correcta
        model.addAttribute("usuario", usuario); // ⚡ debe llamarse "usuario" para Thymeleaf
        return "home";
    }
}