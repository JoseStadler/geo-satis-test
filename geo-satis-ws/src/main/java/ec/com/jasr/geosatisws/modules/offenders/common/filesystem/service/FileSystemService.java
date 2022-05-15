package ec.com.jasr.geosatisws.modules.offenders.common.filesystem.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.transaction.Transactional;

import ec.com.jasr.geosatisws.core.application.AppSpringCtx;
import ec.com.jasr.geosatisws.core.util.AppConstants;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileSystemService {

    @Transactional
    public String uploadFile(String offenderFullName, MultipartFile file) throws IOException {
        StringBuilder directory = this.getDirectoryPath();
        String fileName = this.prepareFileName(offenderFullName, file);
        this.validateDirectoryPath(directory);
        Path filePath = this.getFilePath(directory, fileName);
        this.saveFileInFileSystem(file, filePath);
        return fileName;
    }

    private StringBuilder getDirectoryPath() {
        return new StringBuilder(AppSpringCtx.getAppProp().getFileSystemPath());
    }

    private String prepareFileName(String offenderFullName, MultipartFile file) {
        String fileName = file.getOriginalFilename().replace(" ", "-");
        return offenderFullName + "-" + fileName;
    }

    private void validateDirectoryPath(StringBuilder directory) {
        File dir = new File(directory.toString());
        if (!dir.exists()) { dir.mkdirs(); }
    }

    private Path getFilePath(StringBuilder directory, String fileName) {
        return Paths.get(directory.toString()).resolve(fileName).toAbsolutePath();
    }

    private void saveFileInFileSystem(MultipartFile file, Path filePath) throws IOException {
        Files.copy(file.getInputStream(), filePath);
    }

    @Transactional
    public void remove(String fileName) {
        StringBuilder directory = this.getDirectoryPath().append(AppConstants.SLASH).append(fileName);
        File oldPicture = new File(directory.toString());
        if (oldPicture.exists() && oldPicture.canRead()) {
            oldPicture.delete();
        }
    }

    public Resource findPhoto(String fileName) throws MalformedURLException {
        StringBuilder directory = this.getDirectoryPath();
        Path filePath = this.getFilePath(directory, fileName);
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists() && !resource.isReadable()) {
            throw new RuntimeException("There was an error loading the image: " + fileName);
        }

        return resource;
    }
}

