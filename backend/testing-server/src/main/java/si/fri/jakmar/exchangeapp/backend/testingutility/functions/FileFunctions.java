package si.fri.jakmar.exchangeapp.backend.testingutility.functions;

import lombok.extern.java.Log;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

@Log
public abstract class FileFunctions {

    /**
     * converts file to array of bytes
     * @param file to be converted
     * @return array of bytes
     */
    public static byte[] fileToByte(File file) {
        try {
            return FileUtils.readFileToByteArray(file);
        } catch (IOException e) {
            log.info(e.getMessage());
            return null;
        }
    }

    /**
     * checks if file is empty (has length of 0)
     * @param file
     * @return true if file is empty
     */
    public static boolean isFileEmpty(File file) {
        return file.length() == 0;
    }
}
