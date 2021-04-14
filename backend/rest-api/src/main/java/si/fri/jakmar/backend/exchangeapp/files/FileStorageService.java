package si.fri.jakmar.backend.exchangeapp.files;

import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;
import si.fri.jakmar.backend.exchangeapp.exceptions.FileException;

import java.io.File;

public interface FileStorageService {
    void init();
    void save(MultipartFile multipartFile, String fileName) throws FileException;
    void delete(String filename);
    File getFile(String filename);
    InputStreamResource getInputStreamResourceOfFile(String filename) throws FileException;
}
