package si.fri.jakmar.exchangeapp.backend.testingutility.storage;

import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;
import si.fri.jakmar.exchangeapp.backend.testingutility.exceptions.FileException;

import java.io.File;

public interface FileStorageService {
    public void init();
    public void save(MultipartFile multipartFile, String fileName) throws FileException;
    public void delete(String filename);
    public File getFile(String filename);
    public InputStreamResource getInputStreamResourceOfFile(String filename) throws FileException;
}
