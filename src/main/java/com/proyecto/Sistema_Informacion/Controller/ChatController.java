package com.proyecto.Sistema_Informacion.Controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.proyecto.Sistema_Informacion.Model.dao.ChatDAO;
import com.proyecto.Sistema_Informacion.Model.entity.ChatMensaje;
import com.proyecto.Sistema_Informacion.Model.entity.Crear;
import com.proyecto.Sistema_Informacion.Model.entity.RegistroInsumo;
import com.proyecto.Sistema_Informacion.Model.entity.Vacuna;
import com.proyecto.Sistema_Informacion.Model.service.ExamenMedicoService;
import com.proyecto.Sistema_Informacion.Model.service.IAService;
import com.proyecto.Sistema_Informacion.Model.service.RegistroInsumoService;
import com.proyecto.Sistema_Informacion.Model.service.VacunaService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/doctor/chat")
public class ChatController {

    private final ChatDAO chatDAO;
    private final IAService iaService;
    private final ExamenMedicoService examenService;
    private final VacunaService vacunaService;
    private final RegistroInsumoService registroInsumoService;

 

    public ChatController(ChatDAO chatDAO, IAService iaService, ExamenMedicoService examenService,
            VacunaService vacunaService, RegistroInsumoService registroInsumoService) {
        this.chatDAO = chatDAO;
        this.iaService = iaService;
        this.examenService = examenService;
        this.vacunaService = vacunaService;
        this.registroInsumoService = registroInsumoService;
    }

    // 🟢 Mostrar chat
@PostMapping
public String enviarMensaje(@RequestParam String mensaje,
                        Model model,
                        HttpSession session) {

    Crear usuario = (Crear) session.getAttribute("usuario");

    if (usuario == null) {
        return "redirect:/login";
    }

    // 🔥 CONTROL DE SPAM
    long ahora = System.currentTimeMillis();
    Long ultima = (Long) session.getAttribute("ultimaPeticion");

    if (ultima != null && (ahora - ultima) < 15000) {
        model.addAttribute("error", "⏳ Espera unos segundos antes de enviar otra pregunta");

        List<ChatMensaje> historialDB = chatDAO.findByMedicoIdOrderByFechaAsc(usuario.getId());
        model.addAttribute("historial", historialDB);

        return "Doctor/chat";
    }

    session.setAttribute("ultimaPeticion", ahora);

    // 🔥 GUARDAR MENSAJE USER
    ChatMensaje userMsg = new ChatMensaje();
    userMsg.setRol("USER");
    userMsg.setMensaje(mensaje);
    userMsg.setFecha(LocalDateTime.now());
    userMsg.setMedico(usuario);
    chatDAO.save(userMsg);

    // 🔥 NORMALIZAR TEXTO (CLAVE)
    String msg = mensaje.toLowerCase()
        .replace("á", "a")
        .replace("é", "e")
        .replace("í", "i")
        .replace("ó", "o")
        .replace("ú", "u");

    String respuesta;

    // 🔹 SALUDO
    if (msg.contains("hola")) {
        respuesta = "Hola doctor 👨‍⚕️, ¿en qué puedo ayudarte?";

    }// 🔹 ESTADO GENERAL (INTELIGENTE)
else if (
    msg.contains("al dia") ||
    msg.contains("vencido") ||
    msg.contains("vencida") ||
    msg.contains("vencidos") ||
    msg.contains("pendiente") ||
    msg.contains("pendientes") ||
    msg.contains("falta") ||
    msg.contains("faltan") ||
    msg.contains("que me falta") ||
    msg.contains("que me falta") ||
    msg.contains("algo mal")
) {

    var examenes = examenService.buscarPorMedico(usuario.getId());
    List<Vacuna> vacunas = vacunaService.buscarPorMedico(usuario.getId());

    boolean hayVencidos = false;
    boolean sinVacunas = vacunas.isEmpty();

    StringBuilder r = new StringBuilder("🧠 Estado general:\n\n");

    // 🔍 EXÁMENES
    for (var e : examenes) {
        if (e.getFechaVencimiento() != null &&
            e.getFechaVencimiento().isBefore(java.time.LocalDate.now())) {

            hayVencidos = true;
            r.append("❌ Examen vencido: ")
             .append(e.getTipoExamen())
             .append(" (Venció: ")
             .append(e.getFechaVencimiento())
             .append(")\n");
        }
    }

    // 🔍 VACUNAS
    if (sinVacunas) {
        r.append("⚠️ No tienes vacunas registradas\n");
    }

    // 🔥 RESULTADO FINAL
    if (!hayVencidos && !sinVacunas) {
        r.append("\n✅ Estás al día. Todo en orden 👍");
    } else {
        r.append("\n⚠️ Tienes pendientes por revisar");
    }

    respuesta = r.toString();
}

    // 🔹 VACUNAS
    else if (msg.contains("vacuna")) {

        List<Vacuna> vacunas = vacunaService.buscarPorMedico(usuario.getId());

        if (vacunas.isEmpty()) {
            respuesta = "No tienes vacunas registradas.";
        } else {
            StringBuilder r = new StringBuilder("💉 Tus vacunas:\n");
            for (Vacuna v : vacunas) {
                r.append("- ").append(v.getNombre())
                 .append(" (").append(v.getFechaAplicacion()).append(")\n");
            }
            respuesta = r.toString();
        }
    }

    // 🔹 EXÁMENES
    else if (msg.contains("examen")) {

        var examenes = examenService.buscarPorMedico(usuario.getId());

        if (examenes.isEmpty()) {
            respuesta = "No tienes exámenes registrados.";
        } else {
            StringBuilder r = new StringBuilder("📋 Tus exámenes:\n");

            for (var e : examenes) {

                if (e.getFechaVencimiento() != null &&
                    e.getFechaVencimiento().isBefore(java.time.LocalDate.now())) {

                    r.append("❌ ");
                } else {
                    r.append("✅ ");
                }

                r.append(e.getTipoExamen())
                 .append(" (Vence: ")
                 .append(e.getFechaVencimiento())
                 .append(")\n");
            }

            respuesta = r.toString();
        }
    }

    // 🔹 INSUMOS
    else if (msg.contains("insumo")) {

        List<RegistroInsumo> insumos = registroInsumoService.buscarPorMedico(usuario.getId());

        if (insumos.isEmpty()) {
            respuesta = "No tienes insumos registrados.";
        } else {
            StringBuilder r = new StringBuilder("📦 Insumos:\n");
            for (RegistroInsumo i : insumos) {
                r.append("- ").append(i.getNombreInsumo())
                 .append(" (Cantidad: ").append(i.getCantidad()).append(")\n");
            }
            respuesta = r.toString();
        }
    }

    // 🔹 RESPUESTA GENERAL
    else {
        respuesta = "🤖 Puedo ayudarte con:\n" +
                "- Vacunas\n" +
                "- Exámenes\n" +
                "- Insumos\n\n" +
                "Ejemplo: ¿Tengo exámenes?";
    }

    // 🔥 GUARDAR RESPUESTA
    ChatMensaje iaMsg = new ChatMensaje();
    iaMsg.setRol("IA");
    iaMsg.setMensaje(respuesta);
    iaMsg.setFecha(LocalDateTime.now());
    iaMsg.setMedico(usuario);
    chatDAO.save(iaMsg);

    // 🔥 RECARGAR HISTORIAL
    List<ChatMensaje> historialDB = chatDAO.findByMedicoIdOrderByFechaAsc(usuario.getId());
    model.addAttribute("historial", historialDB);

    return "Doctor/chat";
}


}