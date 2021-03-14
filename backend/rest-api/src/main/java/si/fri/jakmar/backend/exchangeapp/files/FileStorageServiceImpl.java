package si.fri.jakmar.backend.exchangeapp.files;

import org.springframework.core.env.Environment;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import si.fri.jakmar.backend.exchangeapp.exceptions.FileException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageServiceImpl implements FileStorageService {
    private final Path root;

    public FileStorageServiceImpl(Environment environment) {
        String path = environment.getProperty("fileserver.path");
        root = Paths.get(path);
        System.out.println("PATH: " + path);
    }

    @Override
    public void init() {
        try {
            Files.createDirectory(root);
            System.out.println("Ustvarjen: " + root);
        } catch (IOException e) {
            System.out.println("Root folder že obstaja: " + root);
        }
    }

    @Override
    public void save(MultipartFile multipartFile, String filename) throws FileException {
        try {
            Files.copy(multipartFile.getInputStream(), root.resolve(filename));
        } catch (Exception e) {
            e.printStackTrace();
            throw new FileException(
                    e.getMessage() == null
                            ? "Napaka pri shranjevanju"
                            : e.getMessage()
            );
        }
    }

    @Override
    public void delete(String filename) {
        File file = new File(root.toString() + "/" + filename);
        file.delete();
    }

    @Override
    public File getFile(String filename) {
        return new File(root.toString() + "/" + filename);
    }

    @Override
    public InputStreamResource getInputStreamResourceOfFile(String filename) throws FileException {
        try {
            return new InputStreamResource(new FileInputStream(root.toString() + "/" + filename));
        } catch (FileNotFoundException e) {
            throw new FileException(e.getMessage());
        }
    }
}
