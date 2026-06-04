package com.ptithcm.models;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import jakarta.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;

public class UploadFile {

    @Autowired
    private ServletContext servletContext;

    /**
     * Environment-Aware File Saving Strategy: 1. If running in Docker
     * (AVATAR_UPLOAD_PATH is defined): Save to the external volume. 2. If running
     * locally (AVATAR_UPLOAD_PATH is null/empty): Dual-write to Tomcat runtime and
     * local source tree.
     */
    public void saveFile(byte[] bytes, String filename) throws IOException {
        String dockerPath = System.getenv("AVATAR_UPLOAD_PATH");

        if (dockerPath != null && !dockerPath.trim().isEmpty()) {
            // Docker Environment: Save to external volume path only
            String resolvedPath = dockerPath.endsWith(File.separator) ? dockerPath : dockerPath + File.separator;
            File folder = new File(resolvedPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            Path path = Paths.get(resolvedPath + filename);
            Files.write(path, bytes);
            System.out
                    .println("[UploadFile] Docker mode: Saved file to external volume path: " + path.toAbsolutePath());
        } else {
            // Local Windows Dev Environment: Dual-Saving Strategy

            // 1. Write to Tomcat runtime/exploded directory
            String runtimeDir = servletContext.getRealPath("/resources/uploads/avatars/");
            if (runtimeDir == null) {
                runtimeDir = System.getProperty("user.home") + File.separator + "qldsv_uploads" + File.separator
                        + "avatars" + File.separator;
            } else if (!runtimeDir.endsWith(File.separator)) {
                runtimeDir = runtimeDir + File.separator;
            }
            File runtimeFolder = new File(runtimeDir);
            if (!runtimeFolder.exists()) {
                runtimeFolder.mkdirs();
            }
            Path runtimePath = Paths.get(runtimeDir + filename);
            Files.write(runtimePath, bytes);
            System.out.println(
                    "[UploadFile] Local mode: Saved file to Tomcat runtime directory: " + runtimePath.toAbsolutePath());

            // 2. Write to local development source folder
            File srcBase = new File("src/main/webapp/resources/uploads/avatars");
            if (!srcBase.exists()) {
                srcBase.mkdirs();
            }
            Path srcPath = Paths.get(srcBase.getAbsolutePath(), filename);
            Files.write(srcPath, bytes);
            System.out.println("[UploadFile] Local mode: Saved file to Dev persistent source directory: "
                    + srcPath.toAbsolutePath());
        }
    }
}
