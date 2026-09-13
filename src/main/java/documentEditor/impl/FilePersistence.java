package documentEditor.impl;

import documentEditor.Persistence;
import java.io.File;
import java.io.FileOutputStream;
import java.util.logging.Logger;

public class FilePersistence implements Persistence {
    Logger logger = Logger.getLogger(String.valueOf(FilePersistence.class));
    int counter = 0;

    @Override
    public void save(String s) {
        String filename = "file-" + counter++ + ".txt";
        File file = new File("./" + filename);
        try(FileOutputStream fileOutputStream = new FileOutputStream(file);) {
            fileOutputStream.write(s.getBytes());
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }
}
