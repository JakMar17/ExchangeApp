package si.fri.jakmar.backend.exchangeapp.storage;

import org.hibernate.exception.DataException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import si.fri.jakmar.backend.exchangeapp.exceptions.FileException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface FileStorageService {
    public void init();
    public void save(MultipartFile multipartFile, String fileName) throws FileException;
    public void delete(String filename);
    public File getFile(String filename);
    public InputStreamResource getInputStreamResourceOfFile(String filename) throws FileException;
}
