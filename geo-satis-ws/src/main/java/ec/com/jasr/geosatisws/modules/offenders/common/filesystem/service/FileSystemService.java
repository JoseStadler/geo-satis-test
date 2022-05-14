package ec.com.jasr.geosatisws.modules.offenders.common.filesystem.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.transaction.Transactional;

import ec.com.jasr.geosatisws.core.application.AppSpringCtx;
import ec.com.jasr.geosatisws.core.util.AppConstants;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileSystemService {

    @Transactional
    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename().replace(" ", "-");
        StringBuilder directory = new StringBuilder(AppSpringCtx.getAppProp().getFileSystemPath());

        File dir = new File(directory.toString());
        if (!dir.exists()) { dir.mkdirs(); }

        Path filePath = Paths.get(directory.toString()).resolve(fileName).toAbsolutePath();

        Files.copy(file.getInputStream(), filePath);
        return fileName;
    }

    @Transactional
    public void remove(String fileName) {
        StringBuilder directory = new StringBuilder(AppSpringCtx.getAppProp().getFileSystemPath());
        directory.append(AppConstants.SLASH).append(fileName);
        File oldPicture = new File(directory.toString());
        if (oldPicture.exists() && oldPicture.canRead()) {
            oldPicture.delete();
        }
    }

}

