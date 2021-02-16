package si.fri.jakmar.backend.exchangeapp.functions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import si.fri.jakmar.backend.exchangeapp.storage.FileStorageService;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public abstract class ZipperFunction {

    public static ByteArrayInputStream createZip(List<File> filesInput, String inputExtension, List<File> filesOutput, String outputExtension) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(bos);

        addFilesToZip(filesInput, inputExtension, zip);
        addFilesToZip(filesOutput, outputExtension, zip);

        zip.close();
        return new ByteArrayInputStream(bos.toByteArray());
    }

    private static void addFilesToZip(List<File> files, String extension, ZipOutputStream zip) throws IOException {
        byte[] b = new byte[1024];
        for(var file : files) {
            var stream = new FileInputStream(file);
            zip.putNextEntry(new ZipEntry(file.getName() + extension));
            int count;
            while ((count = stream.read(b)) > 0) {
                zip.write(b, 0, count);
            }
        }
    }
}
