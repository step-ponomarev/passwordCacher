import java.util.ArrayList;
import java.io.*;
import java.util.Scanner;
import java.util.Arrays;

final class Generator {
  private String m_IPass;

  public Generator(String IPass) {
      m_IPass = IPass;
  }

  public void generateIPass(int numb) throws Exception {
    String IPass = "\0";
    if ((numb <= 0) || (numb > 8)) {
      throw new Exception("Bad data");
    }

    for (int i = 0; i < numb; ++i) {
      int dict = (int) ((Math.random() * 9) + 1);
      IPass += Integer.toString(dict);
    }

    m_IPass = IPass;
  }

  public void setIPass(final String IPass) throws Exception {
    if (IPass.length() == 0) {
      throw new Exception("Bad data");
    }

    m_IPass = IPass;
  }

  public String getIPass() {
    return m_IPass;
  }
};

final class Printer {
  public Printer(final ArrayList<String> fils) {
    try {
      for (String path : fils) {
        File file = new File(path);
        if (!file.exists()) {
          file.createNewFile();
        }
      }
    } catch (IOException e) {
      System.out.println("File creating error");
    }
  }

  public ArrayList<String> read(int rows, String path) {
    ArrayList<String> strs = new ArrayList<String>(rows);

    try {
      File file = new File(path);
      Scanner myReader = new Scanner(file);

      for (int k = 0; k < rows; ++k) {
        strs.add(k, myReader.nextLine());
      }

      myReader.close();
    }  catch (IOException e) {
      System.out.println("File reading Error");
    }

    return strs;
   }

   public String read(String path) {
     String data = "\0";

     try {
       File file = new File(path);
       if ((file.length() != 0)) {
         Scanner myReader = new Scanner(file);

         data = myReader.nextLine();

         myReader.close();
       }
     }  catch (IOException e) {
       System.out.println("File reading Error");
     }

     return data;
    }

  public void write(final ArrayList<String> strs, int rows, String path) {
    try {
      FileWriter file = new FileWriter(path);

      for (int k = 0; k < rows; ++k) {
        file.write(strs.get(k) + "\n");
      }

      file.close();
    } catch (IOException e) {
      System.out.println("File writing error");
    }
  }

  public void write(int strs, String path) {
    try {
      FileWriter file = new FileWriter(path);
      file.write(Integer.toString(strs) + "\n");
      file.close();
    } catch (IOException e) {
      System.out.println("File writing error");
    }
  }

  public void write(String strs, String path) {
    try {
      FileWriter file = new FileWriter(path);
      file.write(strs + "\n");
      file.close();
    } catch (IOException e) {
      System.out.println("File writing error");
    }
  }
};

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

  public ArrayList<String> getSites() {
    return m_site;
  }

  public String getPass(int index) {
    return m_pass.get(index);
  }

  public ArrayList<String> getPasses() {
    return m_pass;
  }

  public void add(String site, String pass) {
    m_site.add(m_numb, site);
    m_pass.add(m_numb, pass);
    ++m_numb;
  }

  public void remove(int index) {
    if (index < 0 || index >= m_numb) {
      throw new IndexOutOfBoundsException("Invalid index");
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
  private Printer m_printer;
  private Generator m_generator;
  private boolean m_generatorPower;
  private ArrayList<String> m_data;
  private final ArrayList<String> m_path;

  public PasswordCacher() {
    m_path = new ArrayList<String>(4);
    m_path.add("./size.txt");
    m_path.add("./sites.txt");
    m_path.add("./passwords.txt");
    m_path.add("./individual_password.txt");

    /*m_generator = new Generator(m_printer.read(m_path.get(3)));
    if (m_generator.getIPass() != "\0") {
      m_generatorPower = true;
    } else {
      m_generatorPower = false;
    }*/

    m_printer = new Printer(m_path);

    ArrayList<String> site = null;
    ArrayList<String> pass = null;
    String sNumb = m_printer.read(m_path.get(0));
    int iNumb = 0;
    if (sNumb != "\0") {
      iNumb = Integer.parseInt(sNumb);
    }

    site = m_printer.read(iNumb, m_path.get(1));
    pass = m_printer.read(iNumb, m_path.get(2));

    m_cacher = new Cacher(iNumb, site, pass);
    synch();
  }

  public String menu() {
    Scanner input = new Scanner(System.in);
    String choose = new String();

    System.out.println("---------------------");
    System.out.println("Выберете опцию:");
    System.out.println("print list (1)");
    System.out.println("add (2)");
    System.out.println("remove (3)");
    System.out.println("clean (4)");
    System.out.println("add IPass (5)");
    System.out.println("remove IPass (6)");
    System.out.println("exit (q)");

    System.out.print("> ");
    choose = input.nextLine();
    try {
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
          synch();
          System.out.println("---------------------");
          System.out.println("Список пуст");
          break;
        case "5":
          break;
        case "6":
          break;
        case "q":
          synch();
          break;
      }
    } catch (Exception ex) {
      System.out.println("Bad data");
    }

      return choose;
  }

  private void add() throws Exception {
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
    } else if (site.length() == 0) {
      throw new Exception("Bad data");
    }

    System.out.println("Введите пароль: ");
    System.out.print("> ");
    pass = in.nextLine();
    if (pass.equals("q")) {
      return;
    } else if (pass.length() == 0) {
      throw new Exception("Bad data");
    }

    m_cacher.add(site, pass);

    synch();
  }

  private void remove() throws Exception {
    Scanner in = new Scanner(System.in);
    String data = new String();

    System.out.println("---------------------");
    System.out.println("Выберите связку для удаления");
    System.out.println("---------------------");
    System.out.println("Для отмены введите 'q'");
    printAll();
    System.out.print("Номер связки: ");
    data = in.nextLine();
    if (data.length() == 0) {
      throw new Exception("Bad data");
    }
    // Счет с 1
    if (data.equals("q")) {
      return;
    }

    m_cacher.remove((Integer.parseInt(data) - 1));

    synch();
  }

  private void synch() {
    int numb = (m_cacher.getSize());
    m_data = new ArrayList<String>(numb);

    String data = new String();
    for (int k = 0; k < numb; ++k) {
      data = m_cacher.getSite(k) + " : " + m_cacher.getPass(k);
      if (m_generatorPower) {
        data += m_generator.getIPass();
      }

      m_data.add(data);
    }

    m_printer.write(m_cacher.getSize(), m_path.get(0));
    m_printer.write(m_cacher.getSites(), m_cacher.getSize(), m_path.get(1));
    m_printer.write(m_cacher.getPasses(), m_cacher.getSize(), m_path.get(2));
  }

  private void printAll() {
    if (m_data.size() == 0) {
      System.out.println("---------------------");
      System.out.println("Список пуст");
      return;
    }

    System.out.println("---------------------");
    System.out.println("сервис : пароль (номер)");
    for (int k = 0; k < m_data.size(); ++k) {
      System.out.println(m_data.get(k) + " (" + (k + 1)+")");
    }
  }
};

class Launcher {
  /*public static ArrayList<String> sortStrArrayList(ArrayList<String> array) {
    if (array == null) {
      throw new NullPointerException("Null ptr");
    }



    String [] tmp = new String[array.size()];
    for (int k = 0; k < array.size(); ++k) {
        tmp[k] = array.get(k);
    }
    Arrays.sort(tmp);

    ArrayList<String> tmpArray = new ArrayList<String>(array.size());
    for (int k = 0; k < array.size(); ++k) {
      tmpArray.add(tmp[k]);
    }

    return tmpArray;
  }*/

  public static void main(String [] args) {
    PasswordCacher launcher = new PasswordCacher();
    String code = new String();
    while (!code.equals("q")) {
      code = launcher.menu();
    }
  }
};
