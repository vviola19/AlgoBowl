import java.util.HashMap;
import java.awt.Point;
import java.io.*;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.lang.String;

public class Main {
    public static void main(String[] args){
        String fileName = "InitFiles/TestInput1.txt";
        System.out.println("Hello world!");
        Path filePath = Path.of(fileName);
        FileProcessor fileProcessor = null;
        try {
            fileProcessor = new FileProcessor(filePath);
        }
        catch(IOException e){
            System.out.println("File Not Found");
        }
        if(fileProcessor != null) {
            TentPlacer tentPlacer = new TentPlacer(fileProcessor);
//            Tests.testInitialization(tentPlacer);
//            Tests.testPairinglViolations(tentPlacer);
            tentPlacer.minimizeViolations();


        }
        //main function call to run our code

}
}

