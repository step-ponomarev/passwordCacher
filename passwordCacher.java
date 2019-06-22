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
        m_site = new ArrayList<String>(m_numb);
        m_pass = new ArrayList<String>(m_numb);

        for (int k = 0; k < m_numb; ++k) {
          m_site.add(k, myReader.nextLine());
          m_pass.add(k, myReader.nextLine());
        }
        myReader.close();
      }
    } catch (IOException error) {
      System.out.println("File Error");
    }
  }
  
  public void print() {
    for (int k = 0; k < m_numb; ++k) {
      System.out.println(m_site.get(k) + " : " + m_pass.get(k));
    }
  }

  public void addPass() {

  }

  public void exit() {
    try {
      FileWriter file = new FileWriter(m_path);
      file.write(Integer.toString(m_numb) + "\n");
      for (int k = 0; k < m_numb; ++k) {
        file.write(m_site.get(k) + "\n");
        file.write(m_pass.get(k) + "\n");
      }

      file.close();
    } catch (IOException error) {
      System.out.println("File Error");
    }
  }
};

class Launcher {
  public static void main(String [] args) {
    Cacher cache = new Cacher();
    /*String user_input = "";
    while (user_input != "q") {

    }*/
    cache.print();

    cache.exit();
  }
}
