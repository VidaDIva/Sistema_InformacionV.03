package com.proyecto.Sistema_Informacion.Model.service;

import java.io.File;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class BackupService {

    private final String DB = "Sistema_Informacion";
    private final String USER = "root";
    private final String PASS = "10232004";

    @Scheduled(cron = "0 0 2 * * ?") // 🔥 todos los días a las 2 AM
    public void backupAutomatico() {

        try {
            String fecha = java.time.LocalDateTime.now()
                    .toString()
                    .replace(":", "-");

            String ruta = "C:/backups/backup_" + fecha + ".sql";

            ProcessBuilder pb = new ProcessBuilder(
                    "C:\\Program Files\\MySQL\\MySQL Server 8.0\\bin\\mysqldump",
                    "-u", USER,
                    "-p" + PASS,
                    DB
            );

            pb.redirectOutput(new File(ruta));
            Process p = pb.start();
            p.waitFor();

            System.out.println("✅ Backup creado: " + ruta);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}