package si.fri.jakmar.backend.exchangeapp.storage;

import org.hibernate.exception.DataException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import si.fri.jakmar.backend.exchangeapp.exceptions.FileException;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Service
public class FileStorageServiceImpl implements FileStorageService {
    private final Path root = Paths.get("Z:\\02-izobrazevanje\\02-faks\\diploma\\uploads");

    @Override
    public void init() {
        try {
            Files.createDirectory(root);
        } catch (IOException e) {
            System.out.println("Root folder že obstaja");
        }
    }

    @Override
    public void save(MultipartFile multipartFile, String filename) throws FileException {
        try {
            Files.copy(multipartFile.getInputStream(), root.resolve(filename));
        } catch (Exception e) {
            throw new FileException(
                    e.getMessage() == null
                    ? "Napaka pri shranjevanju"
                    : e.getMessage()
            );
        }
    }

    @Override
    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable())
                return resource;
            else
                throw new RuntimeException();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }

    @Override
    public void delete(String filename) {
        File file = new File(root.toString() + "\\" + filename);
        file.delete();
    }

    @Override
    public File getFile(String filename) {
        return new File(root.toString() + "\\" + filename);
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }
}
