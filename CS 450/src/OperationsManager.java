import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class OperationsManager
{
    public static List<Double> readDatFile(String fileName) {
        try {
            FileReader fileReader = new FileReader("dat\\" + fileName);
            BufferedReader reader = new BufferedReader(fileReader);

            ArrayList<Double> data = new ArrayList<Double>();

            String line = null;
            while((line = reader.readLine()) != null)
            {
                data.add(Double.parseDouble(line));
            }

            reader.close();
            return data;
        }
        catch(FileNotFoundException e) {
            System.err.println("File not found");
        }
        catch(IOException e) {
            System.err.println("Problem reading file");
        }

        return null;
    }

    public static List<Double> readDatFileAndIgnoreZero(String fileName) {
        try {
            FileReader fileReader = new FileReader("dat\\" + fileName);
            BufferedReader reader = new BufferedReader(fileReader);

            ArrayList<Double> data = new ArrayList<Double>();

            String line = null;
            while((line = reader.readLine()) != null)
            {
                double info = Double.parseDouble(line);
                if(info != 0)
                    data.add(info);
            }

            reader.close();
            return data;
        }
        catch(FileNotFoundException e) {
            System.err.println("File not found");
        }
        catch(IOException e) {
            System.err.println("Problem reading file");
        }

        return null;
    }

    public static void writeFile(String fileName, String contents) {
        BufferedWriter writer = null;

        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("csv\\" + fileName), "utf-8"));
            writer.write(contents);
        }
        catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        finally {
            try {
                if (writer != null)
                    writer.close();
            }
            catch (Exception ex) {}
        }
    }
}
