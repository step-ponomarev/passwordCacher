import java.util.ArrayList;
import java.io.*;
import java.util.Scanner;

class Cacher {
  private ArrayList<String> m_site;
  private ArrayList<String> m_pass;
  private int m_numb;
  private String m_path = "./passwords.txt";
  private void checkData() {
    //File file = new File(m_path);
  }
  public Cacher() {
    try {
      File cfile = new File(m_path);
      if (!cfile.exists()) {
        m_numb = 0;
        cfile.createNewFile();

        FileWriter wfile = new FileWriter(m_path);
        wfile.write(Integer.toString(m_numb));
        wfile.close();
      } else {
        File file = new File(m_path);
        Scanner myReader = new Scanner(file);
        m_numb = Integer.parseInt(myReader.nextLine());

        myReader.close();
      }
    } catch (IOException error) {
      System.out.println("File Error");
    }
  };

  //public void addPass() {  }
  //public String getPass() {  }
};

class Launcher {
  public static void main(String [] args) {
    Cacher cache = new Cacher();
  }
}
