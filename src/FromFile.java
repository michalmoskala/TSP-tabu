import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FromFile {

    public static int[][] main(String name) {

        int [][]a;

        try {
            File file = new File(name);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line=bufferedReader.readLine();
            int b=Integer.parseInt(line);
            a=new int[b][b];
            int q=0;
            while ((line = bufferedReader.readLine()) != null) {

                String[] ints = line.split(" ");
                int i=0;
                for (String intt:ints)
                {
                    a[q][i] = Integer.parseInt(intt);
                    i++;
                }
                q++;
            }
            fileReader.close();
            return a;

        } catch (IOException e) {
            e.printStackTrace();
        }
            return null;
    }
}