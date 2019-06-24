import java.util.ArrayList;
import java.io.*;
import java.util.Scanner;

interface Controler {
  //void menu();
  //void generateID();
  void add();
  void remove(int index);
  void clean();
  void printAll();
  void exit();
};

class Cacher implements Controler {
  private ArrayList<String> m_site;
  private ArrayList<String> m_pass;
  private int m_numb;
  private String m_path = "./passwords.txt";
  private void checkData() {

  }
  public Cacher() {
    try {
      File cfile = new File(m_path);

      if (!cfile.exists() || (cfile.length() == 0)) {
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

  public void add() {
    Scanner in = new Scanner(System.in);
    String dataSite = new String();
    String dataPass = new String();

    System.out.println("______________________");
    System.out.println("Для отмены введите 'q'");
    System.out.println("----------------------");

    System.out.println("Введите название сервиса:");
    dataSite = in.nextLine();
    if (dataSite.equals("q")) {
      return;
    }

    System.out.println("Введите пароль:");
    dataPass = in.nextLine();
    if (dataPass.equals("q")) {
      return;
    }

    m_site.add(m_numb, dataSite);
    m_pass.add(m_numb, dataPass);
    m_numb++;
  }

  public void remove(int index) {
    if (index < 0 || index >= m_numb) {
      System.out.println("Неверный индекс");
      return;
    } else if (m_numb == 0) {
      System.out.println("Список пуст");
      return;
    }

    ArrayList<String> c_site = new ArrayList();
    ArrayList<String> c_pass = new ArrayList();
    for (int k = 0; k < m_numb; ++k) {
      if (k != index) {
        c_site.add(m_site.get(k));
        c_pass.add(m_pass.get(k));
      }
    }

    m_site = c_site;
    m_pass = m_pass;
    m_numb--;
  }

  public void clean() {
    m_numb = 0;
    m_site.clear();
    m_pass.clear();
  }

  public void printAll() {
    if (m_numb == 0) {
      System.out.println("Список пуст");
      return;
    }
    System.out.println("сервис : пароль (номер)");
    for (int k = 0; k < m_numb; ++k) {
      System.out.println(m_site.get(k) + " : " + m_pass.get(k) + " (" + (k + 1) + ")");
    }
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
    Controler controler = new Cacher();


    controler.printAll();
    controler.add();
    controler.printAll();
    //controler.remove();
    controler.printAll();

    controler.exit();
  }
}
