import java.util.ArrayList;
import java.io.*;
import java.util.Scanner;

final class Cacher {
  private ArrayList<String> m_site;
  private ArrayList<String> m_pass;
  private int m_numb;

  public Cacher(int numb, final ArrayList<String> site, final ArrayList<String> pass) {
    m_numb = numb;
    m_site = site;
    m_pass = pass;
  }

  public int getSize() {
    return m_numb;
  }

  public String getSite(int index) {
    return m_site.get(index);
  }

  public String getPass(int index) {
    return m_pass.get(index);
  }

  public void add(String site, String pass) {
    m_site.add(m_numb, site);
    m_pass.add(m_numb, pass);
    ++m_numb;
  }

  public void remove(int index) {
    if (index < 0 || index >= m_numb) {
      return;
    } else if (m_numb == 0) {
      System.out.println("Nothing to remove");
      return;
    }

    ArrayList<String> site = new ArrayList();
    ArrayList<String> pass = new ArrayList();

    for (int k = 0; k < m_numb; ++k) {
      if (k != index) {
        site.add(m_site.get(k));
        pass.add(m_pass.get(k));
      }
    }

    m_site = site;
    m_pass = pass;
    --m_numb;
  }

  public void clean() {
    m_numb = 0;
    m_site.clear();
    m_pass.clear();
  }
};

class PasswordCacher {
  private Cacher m_cacher;
  private final String m_path = "./passwords.txt";

  public PasswordCacher() {
    ArrayList<String> site = null;
    ArrayList<String> pass = null;
    int numb = 0;

    try {
      File cfile = new File(m_path);

      if (!cfile.exists() || (cfile.length() == 0)) {
        cfile.createNewFile();
        FileWriter wfile = new FileWriter(m_path);
        wfile.write(Integer.toString(0));
        wfile.close();
      } else {
        File file = new File(m_path);
        Scanner myReader = new Scanner(file);

        numb = Integer.parseInt(myReader.nextLine());
        site = new ArrayList<String>(numb);
        pass = new ArrayList<String>(numb);

        for (int k = 0; k < numb; ++k) {
          site.add(k, myReader.nextLine());
          pass.add(k, myReader.nextLine());
        }

        myReader.close();
      }
    } catch (IOException e) {
      System.out.println("File Error");
    }

    m_cacher = new Cacher(numb, site, pass);
  }

  public String menu() {
    Scanner input = new Scanner(System.in);
    String choose = "";

    System.out.println("---------------------");
    System.out.println("Выберете опцию:");
    System.out.println("print list (1)");
    System.out.println("add (2)");
    System.out.println("remove (3)");
    System.out.println("clean (4)");
    System.out.println("exit (q)");

    System.out.print("> ");
    choose = input.nextLine();

    switch (choose) {
      case "1":
        printAll();
        break;
      case "2":
        add();
        break;
      case "3":
        remove();
        break;
      case "4":
        m_cacher.clean();
        System.out.println("---------------------");
        System.out.println("Список пуст");
        break;
      case "q":
        exit();
        break;

    }

      return choose;
  }

  private void add() {
    Scanner in = new Scanner(System.in);
    String site = new String();
    String pass = new String();

    System.out.println("---------------------");
    System.out.println("Для отмены введите 'q'");

    System.out.println("Введите название сервиса: ");

    System.out.print("> ");
    site = in.nextLine();
    if (site.equals("q")) {
      return;
    }

    System.out.println("Введите пароль: ");
    System.out.print("> ");
    pass = in.nextLine();
    if (pass.equals("q")) {
      return;
    }

    m_cacher.add(site, pass);
  }

  private void remove() {
    Scanner in = new Scanner(System.in);
    String data = new String();

    System.out.println("---------------------");
    System.out.println("Выберите связку для удаления");
    System.out.println("---------------------");
    System.out.println("Для отмены введите 'q'");
    printAll();
    System.out.print("Номер связки: ");
    data = in.nextLine();

    if (data.equals("q")) {
      return;
    }

    m_cacher.remove((Integer.parseInt(data) - 1));
  }

  private void exit() {
    try {
      FileWriter file = new FileWriter(m_path);
      file.write(Integer.toString(m_cacher.getSize()) + "\n");

      for (int k = 0; k < m_cacher.getSize(); ++k) {
        file.write(m_cacher.getSite(k) + "\n");
        file.write(m_cacher.getPass(k) + "\n");
      }

      file.close();
    } catch (IOException e) {
      System.out.println("File Error");
    }

  }

  private void printAll() {
    if (m_cacher.getSize() == 0) {
      System.out.println("---------------------");
      System.out.println("Список пуст");
      return;
    }

    System.out.println("---------------------");
    System.out.println("сервис : пароль (номер)");
    for (int k = 0; k < m_cacher.getSize(); ++k) {
      System.out.println(m_cacher.getSite(k) + " : " + m_cacher.getPass(k) + " (" + (k + 1) + ")");
    }
  }
};

class Launcher {
  public static void main(String [] args) {
    PasswordCacher launcher = new PasswordCacher();
    String code = "";
    while (!code.equals("q")) {
      code = launcher.menu();
    }
  }
};
