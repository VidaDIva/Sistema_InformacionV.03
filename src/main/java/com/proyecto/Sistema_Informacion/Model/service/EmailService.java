package com.proyecto.Sistema_Informacion.Model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // ===============================
    // GENERAR CÓDIGO
    // ===============================
    public String generarCodigo() {

        int codigoInt = (int) (Math.random() * 900000) + 100000;

        return String.valueOf(codigoInt);
    }

    // ===============================
    // ENVIAR CÓDIGO
    // ===============================
    public void enviarCodigo(String destino, String codigo) {

        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(destino);
        mensaje.setSubject("Código de Verificación - SaludSync");
        mensaje.setText("Tu código de verificación es: " + codigo
                + "\n\nEste código expira en 5 minutos.");

        mailSender.send(mensaje);
    }

    // ===============================
    // VERIFICAR CÓDIGO
    // ===============================
    public boolean verificarCodigo(String codigoIngresado, String codigoGuardado) {

        if (codigoGuardado == null) {
            return false;
        }

        return codigoGuardado.equals(codigoIngresado);
    }
}
