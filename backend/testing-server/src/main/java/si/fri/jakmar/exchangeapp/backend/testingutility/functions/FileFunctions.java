package si.fri.jakmar.exchangeapp.backend.testingutility.functions;

import lombok.extern.java.Log;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

@Log
public abstract class FileFunctions {

    public static byte[] fileToByte(File file) {
        try {
            return FileUtils.readFileToByteArray(file);
        } catch (IOException e) {
            log.info(e.getMessage());
            return null;
        }
    }

    public static boolean isFileEmpty(File file) {
        return file.length() == 0;
    }
}
