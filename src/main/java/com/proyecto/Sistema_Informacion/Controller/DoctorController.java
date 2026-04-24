package com.proyecto.Sistema_Informacion.Controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.proyecto.Sistema_Informacion.Model.entity.Crear;
import com.proyecto.Sistema_Informacion.Model.entity.DocumentoMedico;
import com.proyecto.Sistema_Informacion.Model.enums.EstadoCita;
import com.proyecto.Sistema_Informacion.Model.enums.EstadoDocumento;
import com.proyecto.Sistema_Informacion.Model.service.CitaService;
import com.proyecto.Sistema_Informacion.Model.service.CrearService;
import com.proyecto.Sistema_Informacion.Model.service.DocumentoMedicoService;
import com.proyecto.Sistema_Informacion.Model.service.ExamenMedicoService;
import com.proyecto.Sistema_Informacion.Model.service.RegistroInsumoService;
import com.proyecto.Sistema_Informacion.Model.service.VacunaService;

import jakarta.servlet.http.HttpSession;

@Controller 
@RequestMapping("/doctor")
public class DoctorController {

    @Value("${app.uploads.doctor-dir}")
    private String uploadDir;

    @Value("${app.uploads.archivo-dir}")
    private String rutaArchivos;

    private final ExamenMedicoService examenService;
    private final VacunaService vacunaService;
    private final RegistroInsumoService insumoService;
    private final CitaService citaService;
    private final CrearService crearService;
    private final DocumentoMedicoService documentoMedicoService;
    
    
    public DoctorController(ExamenMedicoService examenService, VacunaService vacunaService,
            RegistroInsumoService insumoService, CitaService citaService, CrearService crearService,
            DocumentoMedicoService documentoMedicoService) {
        this.examenService = examenService;
        this.vacunaService = vacunaService;
        this.insumoService = insumoService;
        this.citaService = citaService;
        this.crearService = crearService;
        this.documentoMedicoService = documentoMedicoService;
    }

    @GetMapping("/home")
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

    @GetMapping("/citas")
    public String verCitasDoctor(Model model, HttpSession session) {

        Crear doctor = (Crear) session.getAttribute("usuario");

        if (doctor == null) {
            return "redirect:/login";
        }

        model.addAttribute("citas", citaService.citasPorDoctor(doctor.getId()));

        return "Doctor/doctor-citas";
    }
    @GetMapping("/examenes")
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

    @GetMapping("/vacunas")
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

    @GetMapping("/insumos")
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

    @GetMapping("/cita/cancelar/{id}")
    public String cancelarCita(@PathVariable Long id) {

        citaService.actualizarEstado(id, EstadoCita.CANCELADA);

        return "redirect:/doctor/citas";
    }

    @GetMapping("/cita/confirmar/{id}")
    public String confirmarCita(@PathVariable Long id) {

        citaService.actualizarEstado(id, EstadoCita.CONFIRMADA);

        return "redirect:/doctor/citas";
}

@GetMapping("/perfil")
public String verPerfil(Model model, HttpSession session){

    Crear doctor = (Crear) session.getAttribute("usuario");

    if(doctor == null){
        return "redirect:/login";
    }

    model.addAttribute("doctor", doctor);

    return "doctor/perfil";
}

@GetMapping("/pEditar")
public String editarPerfil(Model model, HttpSession session){

    Crear doctor = (Crear) session.getAttribute("usuario");

    if(doctor == null){
        return "redirect:/login";
    }

    model.addAttribute("doctor", doctor);

    return "doctor/editar-perfil";
}

@PostMapping("/perfil/guardar")
public String guardarPerfil(
        @ModelAttribute Crear doctorForm,
        @RequestParam("foto") MultipartFile file,
        HttpSession session) throws Exception {

    Crear doctorSesion =
        (Crear) session.getAttribute("usuario");

    Crear doctor =
        crearService.buscarPorId(doctorSesion.getId());

    doctor.setNombre(doctorForm.getNombre());
    doctor.setApellido(doctorForm.getApellido());
    doctor.setCelular(doctorForm.getCelular());
    doctor.setCorreo(doctorForm.getCorreo());
    doctor.setDireccion(doctorForm.getDireccion());

    if (!file.isEmpty()) {

        String nombreArchivo =
          System.currentTimeMillis()+"_"+
          file.getOriginalFilename();

        Path carpeta = Paths.get(uploadDir);

        if(!Files.exists(carpeta)){
            Files.createDirectories(carpeta);
        }

        Path ruta = carpeta.resolve(nombreArchivo);

        Files.write(ruta, file.getBytes());

        doctor.setImagen(nombreArchivo);
    }

    crearService.guardar(doctor);

    session.setAttribute("usuario", doctor);

    return "redirect:/doctor/perfil";
}

@GetMapping("/documentos")
public String verDocumentos(Model model,
                            HttpSession session){

    Crear doctor =
      (Crear) session.getAttribute("usuario");

    List<DocumentoMedico> docs =
      documentoMedicoService.buscarPorDoctor(
           doctor.getId()
      );

    model.addAttribute("documentos", docs);

    return "doctor/documentos";
}

@GetMapping("/revisar/{id}")
public String revisarDocumento(@PathVariable Long id, Model model) {

    DocumentoMedico doc = documentoMedicoService.buscarPorId(id);

    model.addAttribute("documento", doc);

    return "Doctor/revisar-documento";
}

@PostMapping("/revisar/guardar")
public String guardarRevision(
        @RequestParam Long id,
        @RequestParam String observacion,
        @RequestParam String estado) {

    DocumentoMedico doc = documentoMedicoService.buscarPorId(id);

    doc.setObservacion_medico(observacion);
    doc.setEstado(EstadoDocumento.valueOf(estado));

    documentoMedicoService.guardar(doc);

    return "redirect:/doctor/documentos";
}



@GetMapping("/ver-documento/{id}")
public ResponseEntity<FileSystemResource> verDocumento(@PathVariable Long id) {

    try {

        DocumentoMedico doc = documentoMedicoService.buscarPorId(id);

        if (doc == null) {
            System.out.println("Documento NO existe en BD");
            return ResponseEntity.notFound().build();
        }

        System.out.println("NOMBRE BD: " + doc.getNombreArchivo());
        System.out.println("RUTA BASE: " + rutaArchivos);

        Path path = Paths.get(rutaArchivos, doc.getNombreArchivo());
        File archivo = path.toFile();

        System.out.println("RUTA COMPLETA: " + archivo.getAbsolutePath());

        if (!archivo.exists()) {
            System.out.println("ARCHIVO NO EXISTE");
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + archivo.getName())
                .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
                .body(new FileSystemResource(archivo));

    } catch (Exception e) {
        e.printStackTrace(); // 🔥 ESTE ES EL CLAVE
        return ResponseEntity.internalServerError().build();
    }
}

}
