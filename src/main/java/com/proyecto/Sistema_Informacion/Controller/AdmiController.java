package com.proyecto.Sistema_Informacion.Controller;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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
import com.proyecto.Sistema_Informacion.Model.enums.EstadoCita;
import com.proyecto.Sistema_Informacion.Model.enums.EstadoExamen;
import com.proyecto.Sistema_Informacion.Model.enums.EstadoVacuna;
import com.proyecto.Sistema_Informacion.Model.service.CitaService;
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
    private final CitaService citaService;



    public AdmiController(PqrsService pqrsService, ExamenMedicoService examenService, VacunaService vacunaService,
            RegistroInsumoService insumoService, CrearService usuarioService, CitaService citaService) {
        this.pqrsService = pqrsService;
        this.examenService = examenService;
        this.vacunaService = vacunaService;
        this.insumoService = insumoService;
        this.usuarioService = usuarioService;
        this.citaService = citaService;
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

    @GetMapping("/backup")
    public ResponseEntity<Resource> descargarBackup() {

        try {

            String archivo = "backup_" + System.currentTimeMillis() + ".sql";
            String ruta = "C:/backups/" + archivo;

            ProcessBuilder pb = new ProcessBuilder(
                    "C:\\Program Files\\MySQL\\MySQL Server 9.3\\bin\\mysqldump.exe",
                    "-u", "root",
                    "-p10232004",
                    "Sistema_Informacion"
            );

            pb.redirectOutput(new File(ruta));

            Process proceso = pb.start();
            int resultado = proceso.waitFor();

            // 🔥 VALIDAMOS SI EL PROCESO FALLÓ
            if (resultado != 0) {
                return ResponseEntity.internalServerError()
                        .body(null);
            }

            File archivoBackup = new File(ruta);

            // 🔥 VALIDAMOS SI EL ARCHIVO EXISTE
            if (!archivoBackup.exists()) {
                return ResponseEntity.internalServerError()
                        .body(null);
            }

            FileSystemResource file = new FileSystemResource(archivoBackup);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + archivo)
                    .body(file);

        } catch (Exception e) {
            e.printStackTrace();

            // 🔥 EN VEZ DE CRASH → RESPUESTA CONTROLADA
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/doctores")
    public String verDoctores(Model model, HttpSession session) {

        Crear usuario = (Crear) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute("doctores", usuarioService.listarMedicos());

        return "Admi/doctores-lista";
    }

        @GetMapping("/pacientes")
        public String verPacientes(Model model, HttpSession session) {

            Crear usuario = (Crear) session.getAttribute("usuario");

            if (usuario == null) {
                return "redirect:/login";
            }

            // 🔥 método que debes tener en tu service
            model.addAttribute("pacientes", usuarioService.listarPacientes());

            return "Admi/pacientes-lista";
        }

        @GetMapping("/reportes")
        public String dashboardBI(Model model) {

            model.addAttribute("totalPacientes", usuarioService.listarPacientes().size());
            model.addAttribute("totalDoctores", usuarioService.listarMedicos().size());
            model.addAttribute("totalCitas", citaService.contar());

            model.addAttribute("confirmadas", citaService.contarPorEstado(EstadoCita.CONFIRMADA));
            model.addAttribute("pendientes", citaService.contarPorEstado(EstadoCita.PENDIENTE));
            model.addAttribute("canceladas", citaService.contarPorEstado(EstadoCita.CANCELADA));

            // 🔥 PROCESAR DATOS PARA GRÁFICAS
            List<String> labelsMes = new ArrayList<>();
            List<Long> valoresMes = new ArrayList<>();

            for (Object[] fila : citaService.citasPorMes()) {
                labelsMes.add(fila[0].toString());
                valoresMes.add((Long) fila[1]);
            }

            List<String> labelsDoc = new ArrayList<>();
            List<Long> valoresDoc = new ArrayList<>();

            for (Object[] fila : citaService.citasPorDoctorBI()) {
                labelsDoc.add(fila[0].toString());
                valoresDoc.add((Long) fila[1]);
            }

            model.addAttribute("labelsMes", labelsMes);
            model.addAttribute("valoresMes", valoresMes);
            model.addAttribute("labelsDoc", labelsDoc);
            model.addAttribute("valoresDoc", valoresDoc);

            return "Admi/reportes";
        }
}