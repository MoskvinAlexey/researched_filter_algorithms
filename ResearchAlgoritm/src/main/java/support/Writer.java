package support;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Writer {

    public String fileName;
    private File file;
    private FileWriter fileWriter;

    public Writer(String fileName){
        this.fileName = fileName;
        this.file = new File(fileName);
    }

    public void write(String text) {
        try {
            fileWriter = new FileWriter(file,true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fileWriter.write(text + System.getProperty("line.separator"));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
