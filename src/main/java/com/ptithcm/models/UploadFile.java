package com.ptithcm.models;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import jakarta.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;

public class UploadFile {

    private String basePath;

    @Autowired
    private ServletContext servletContext;

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    /**
     * Resolves the real path for saving/serving files at runtime.
     */
    public String getResolvedPath() {
        if (basePath == null) {
            return servletContext.getRealPath("/resources/uploads/avatars/");
        }
        if (basePath.startsWith("/") || basePath.startsWith("\\")) {
            String realPath = servletContext.getRealPath(basePath);
            if (realPath == null) {
                // Fallback for unexploded war or specific contexts
                return System.getProperty("user.home") + File.separator + "qldsv_uploads" + basePath;
            }
            return realPath.endsWith("/") || realPath.endsWith("\\") ? realPath : realPath + File.separator;
        }
        return basePath.endsWith("/") || basePath.endsWith("\\") ? basePath : basePath + File.separator;
    }

    /**
     * Dual-saves the uploaded file: 1. Saves to the active webapp deployment
     * directory (so it is visible immediately). 2. Saves to the local maven source
     * directory (so it persists across clean rebuilds).
     */
    public void saveFile(byte[] bytes, String filename) throws IOException {
        // 1. Write to Tomcat runtime/exploded directory
        String runtimeDir = getResolvedPath();
        File runtimeFolder = new File(runtimeDir);
        if (!runtimeFolder.exists()) {
            runtimeFolder.mkdirs();
        }
        Path runtimePath = Paths.get(runtimeDir + filename);
        Files.write(runtimePath, bytes);
        System.out.println("[UploadFile] Saved file to Tomcat runtime directory: " + runtimePath.toAbsolutePath());

        // 2. Write to local development source folder (if it exists)
        File srcBase = new File("src/main/webapp");
        if (srcBase.exists() && srcBase.isDirectory()) {
            String relPath = basePath != null ? basePath : "/resources/uploads/avatars/";
            File srcDir = new File(srcBase, relPath.replace("/", File.separator).replace("\\", File.separator));
            if (!srcDir.exists()) {
                srcDir.mkdirs();
            }
            Path srcPath = Paths.get(srcDir.getAbsolutePath(), filename);
            Files.write(srcPath, bytes);
            System.out
                    .println("[UploadFile] Saved file to Dev persistent source directory: " + srcPath.toAbsolutePath());
        }
    }
}
