import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CreateTable{
    private float[][] w;
    private String inputFile;

    public CreateTable(String inputFile){
        this.inputFile = inputFile;
        readInterferenceFactorFile();
        printTable();
    }

    public void readInterferenceFactorFile() {
        int z, m;
        int n = 0;
        try {
          BufferedReader file = new BufferedReader(new FileReader(inputFile));
          String line = new String();
          while (file.ready()) {
            line = file.readLine();
    
            if (line.startsWith("#"))
              continue;
    
            String[] items = line.split(" ");
            if (items == null)
              continue;
    
            String key = items[0];
            if (key.equals("all_possible_channels:")) {
              n = Integer.parseInt(items[1]);
              this.w = new float[n][n];
            }
            if (key.equals("interference-factor:")) {
              for (m = 0; m < n; m++) {
                line = file.readLine();
                String str[] = line.split(" ");
                for (z = 0; z < n; z++) {
                  this.w[z][m] = Float.parseFloat(str[z]);
                }
              }
            }
          }
          file.close();
        } catch (IOException e) {
          System.err.println("I got an error when I tried to read the file " + inputFile);
        }
      }
    
    public void printTable(){
      for(int i = 0; i < 29; i++){
        for(int j = 0; j < 29; j++){
          System.out.print(w[i][j] + " ");
        }
        System.out.println();
      }
    } 

    public float[][] getW(){
      return w;
    }
}