package com.proyecto.Sistema_Informacion.Controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
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

import com.proyecto.Sistema_Informacion.Model.entity.Cita;
import com.proyecto.Sistema_Informacion.Model.entity.Crear;
import com.proyecto.Sistema_Informacion.Model.entity.DocumentoMedico;
import com.proyecto.Sistema_Informacion.Model.enums.EstadoDocumento;
import com.proyecto.Sistema_Informacion.Model.service.CitaService;
import com.proyecto.Sistema_Informacion.Model.service.CrearService;
import com.proyecto.Sistema_Informacion.Model.service.DocumentoMedicoService;
import com.proyecto.Sistema_Informacion.Model.service.UsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/paciente")
public class PacienteController {

     @Value("${app.uploads.paciente-dir}")
    private String uploadPac;
     @Value("${app.uploads.archivo-dir}")
    private String uploadArc;

    private final CrearService crearService;
    private final DocumentoMedicoService documentoMedicoService;
    private final UsuarioService usuarioService;
    private final CitaService citaService;
    
    
    public PacienteController(CrearService crearService, DocumentoMedicoService documentoMedicoService,
            UsuarioService usuarioService, CitaService citaService) {
        this.crearService = crearService;
        this.documentoMedicoService = documentoMedicoService;
        this.usuarioService = usuarioService;
        this.citaService = citaService;
    }

    @GetMapping("/home")
    public String homePaciente(Model model, HttpSession session) {

        Crear usuario = (Crear) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute("usuario", usuario);

        return "paciente/home";
    }
    @GetMapping("/perfil")
    public String verPerfil(Model model, HttpSession session){

        Crear paciente = (Crear) session.getAttribute("usuario");

        if(paciente == null){
            return "redirect:/login";
        }

        model.addAttribute("paciente", paciente);

        return "paciente/perfil";
    }

@GetMapping("/pEditar")
public String editarPerfil(Model model, HttpSession session){

    Crear paciente = (Crear) session.getAttribute("usuario");

    if(paciente == null){
        return "redirect:/login";
    }

    model.addAttribute("paciente", paciente);

    return "paciente/editar-perfil";
}

@PostMapping("/perfil/guardar")
public String guardarPerfil(
        @ModelAttribute Crear pacienteForm,
        @RequestParam("foto") MultipartFile file,
        HttpSession session) throws Exception {

    Crear pacienteSesion =
        (Crear) session.getAttribute("usuario");

    Crear paciente =
        crearService.buscarPorId(pacienteSesion.getId());

    paciente.setNombre(pacienteForm.getNombre());
    paciente.setApellido(pacienteForm.getApellido());
    paciente.setCelular(pacienteForm.getCelular());
    paciente.setCorreo(pacienteForm.getCorreo());
    paciente.setDireccion(pacienteForm.getDireccion());

    if (!file.isEmpty()) {

        String nombreArchivo =
          System.currentTimeMillis()+"_"+
          file.getOriginalFilename();

        Path carpeta = Paths.get(uploadPac);

        if(!Files.exists(carpeta)){
            Files.createDirectories(carpeta);
        }

        Path ruta = carpeta.resolve(nombreArchivo);

        Files.write(ruta, file.getBytes());

        paciente.setImagen(nombreArchivo);
    }

    crearService.guardar(paciente);

    session.setAttribute("usuario", paciente);

    return "redirect:/paciente/perfil";
}

@GetMapping("/subir-documento")
public String verFormularioDocumento(
        Model model,
        HttpSession session) {

    Crear paciente = (Crear) session.getAttribute("usuario");

    if (paciente == null) {
        return "redirect:/login";
    }

    model.addAttribute(
        "citas",
        citaService.citasPorPaciente(paciente.getId())
    );

    return "paciente/subir-documento";
}

@PostMapping("/guardar-documento")
public String guardarDocumento(
        @RequestParam("archivo") MultipartFile file,
        @RequestParam Long citaId,
        @RequestParam String tipoDocumento,
        @RequestParam String nota,
        HttpSession session) throws Exception {

    Crear paciente = (Crear) session.getAttribute("usuario");

    if (paciente == null) {
        return "redirect:/login";
    }

    if (file.isEmpty()) {
        throw new RuntimeException("Debe seleccionar un archivo");
    }

   
    Cita cita = citaService.buscarPorId(citaId);

   
    DocumentoMedico documentoMedico = new DocumentoMedico();
    documentoMedico.setPaciente(paciente);
    documentoMedico.setCita(cita);
    documentoMedico.setTipoDocumento(tipoDocumento);
    documentoMedico.setNota(nota);
    documentoMedico.setFechaSubida(LocalDateTime.now());
    documentoMedico.setEstado(EstadoDocumento.PENDIENTE);
    documentoMedico.setObservacion_medico("Sin revisión");



    String nombreLimpio = file.getOriginalFilename().replaceAll("\\s+", "_");
    String nombreArchivo = System.currentTimeMillis() + "_" + nombreLimpio;

    // Ruta completa
    Path ruta = Paths.get(uploadArc)
                     .toAbsolutePath()
                     .resolve(nombreArchivo);

    // Crear carpeta si no existe
    if (!Files.exists(ruta.getParent())) {
        Files.createDirectories(ruta.getParent());
    }

    Files.write(ruta, file.getBytes());

    documentoMedico.setNombreArchivo(nombreArchivo);


    documentoMedicoService.guardar(documentoMedico);

    return "redirect:/paciente/home";
}

@GetMapping("/ver-documento/{id}")
public ResponseEntity<FileSystemResource> verDocumentoPaciente(
        @PathVariable Long id,
        HttpSession session) {

    Crear paciente = (Crear) session.getAttribute("usuario");

    if (paciente == null) {
        return ResponseEntity.status(403).build();
    }

    DocumentoMedico doc = documentoMedicoService.buscarPorId(id);

    // 🔒 Validar que el documento pertenece al paciente
    if (doc == null || !doc.getPaciente().getId().equals(paciente.getId())) {
        return ResponseEntity.status(403).build();
    }

    Path path = Paths.get(uploadArc)
            .toAbsolutePath()
            .resolve(doc.getNombreArchivo());

    File archivo = path.toFile();

    if (!archivo.exists()) {
        return ResponseEntity.notFound().build();
    }

    try {
        String tipo = Files.probeContentType(path);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + archivo.getName() + "\"")
                .header(HttpHeaders.CONTENT_TYPE,
                        tipo != null ? tipo : "application/octet-stream")
                .body(new FileSystemResource(archivo));

    } catch (Exception e) {
        return ResponseEntity.internalServerError().build();
    }
}

@GetMapping("/documentos")
public String verDocumentosPaciente(Model model, HttpSession session) {

    Crear paciente = (Crear) session.getAttribute("usuario");

    if (paciente == null) {
        return "redirect:/login";
    }

    List<DocumentoMedico> docs =
        documentoMedicoService.buscarPorPaciente(paciente.getId());

    model.addAttribute("documentos", docs);

    return "paciente/documentos";
}


}