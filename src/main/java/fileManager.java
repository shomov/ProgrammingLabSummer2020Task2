import java.io.*;
import java.nio.file.*;
import java.util.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class fileManager {
    Logger log = LogManager.getLogger(flagManager.class.getName());
    String creator (boolean approach, String pathOut, boolean custom) throws IOException {
        Path fOut = Paths.get(pathOut);
        System.out.println("out " + pathOut);
        if (custom)
            fOut = Paths.get(pathOut);
        else
            if (approach) {
                fOut = Paths.get(fOut.toString() + ".crp");

            }
            else {
                if (fOut.getFileName().toString().matches("[a-zA-Zа-яёА-ЯЁ0-9-.]+(.crp)"))
                    fOut = Paths.get(pathOut.substring(0, pathOut.lastIndexOf(".")));
                else
                    fOut = Paths.get(pathOut + ".txt");
            }
        try {
            Files.createFile(fOut);
        }
        catch (FileAlreadyExistsException e) {
            System.err.println("A file with this name already exists in this directory. Please use the -o argument.");
            log.error("File" + fOut + "already exists");
            throw e;
        }
        catch (AccessDeniedException e) {
            System.err.println("Access to the folder is forbidden");
            log.error("Access to the folder " + fOut.getParent() + " is forbidden");
            throw e;
        }
        return fOut.toString();
    }

    void reader (flagManager flag) throws IOException {
        Crypto crypter = new Crypto();
        if (flag.approach) {
            FileReader fileIn = new FileReader(flag.pathIn);
            Scanner scan = new Scanner(fileIn);
            while (scan.hasNextLine()) {
                byte[] crypt = crypter.encode(scan.nextLine() + "\n", flag.key);
                byteWriter(flag.pathOut, crypt);
            }
            fileIn.close();
            System.out.println("Encoding completed");
            log.info("Encoding completed");
        }
        else {
            FileInputStream fis = new FileInputStream(flag.pathIn);
            byte[] data = fis.readAllBytes();
            String text = crypter.decode(data, flag.key);
            writer(flag.pathOut, text);
            fis.close();
            System.out.println("Decoding completed");
            log.info("Decoding completed");
        }
    }

    void byteWriter (String path, byte[] data) throws IOException {
        FileOutputStream fos = new FileOutputStream(path, true);
        fos.write(data, 0, data.length);
        fos.close();
    }

    void writer (String path, String data) throws IOException {
        FileWriter fileOut = new FileWriter(path, true);
        fileOut.write(data);
        fileOut.close();
    }
}
