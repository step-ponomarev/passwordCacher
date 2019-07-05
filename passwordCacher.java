import java.util.ArrayList;
import java.io.*;
import java.util.Scanner;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

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

  public int add(String site, String pass) {
    m_site.add(m_numb, site);
    m_pass.add(m_numb, pass);
    ++m_numb;

    return 1;
  }

  public int remove(int index) {
    if (index < 0 || index >= m_numb) {
      throw new IndexOutOfBoundsException("Invalid index");
    } else if (m_numb == 0) {
      return -1;
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

    return 1;
  }

  public void clean() {
    m_numb = 0;
    m_site.clear();
    m_pass.clear();
  }
};

class PasswordCacher {
  private MyGui m_gui;
  private Cacher m_cacher;
  private Printer m_printer;
  private ArrayList<String> m_data;
  private final ArrayList<String> m_path;

  public PasswordCacher() {
    m_path = new ArrayList<String>(3);
    m_path.add("./size.txt");
    m_path.add("./sites.txt");
    m_path.add("./passwords.txt");

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

  public int add(String site, String pass) {
    if (site.length() == 0 || pass.length() == 0) {
      return -1;
    }

    if ((site == null) || (pass == null)) {
       return -1;
    }
    int key = m_cacher.add(site, pass);
    synch();

    return key;
  }

  public int remove(String site) {
    int index = m_data.indexOf​(site);
    if ((index >= 0) && (index < m_data.size())) {
      m_cacher.remove(index);
      synch();
      return 1;
    }

    return -1;
  }

  public void clean() {
    m_cacher.clean();
    synch();
  }

  public String getPass(String site) {
    int index = m_data.indexOf​(site);
    return m_cacher.getPass(index);
  }

  public void synch() {
    int numb = (m_cacher.getSize());
    m_data = new ArrayList<String>(numb);

    String data = new String();
    for (int k = 0; k < numb; ++k) {
      data = m_cacher.getSite(k);

      m_data.add(data);
    }

    m_printer.write(m_cacher.getSize(), m_path.get(0));
    m_printer.write(m_cacher.getSites(), m_cacher.getSize(), m_path.get(1));
    m_printer.write(m_cacher.getPasses(), m_cacher.getSize(), m_path.get(2));
  }

  public ArrayList<String> getArray() {
    return m_data;
  }
};

final class MyGui {
  private JFrame m_main;
  private JPanel m_bPanel;
  private JPanel m_lPanel;
  private JList m_list;
  private JTextField m_search;
  private String m_selectedData;
  private PasswordCacher m_program;
  boolean activityMode;

  class SearchListener implements ActionListener {

    public void actionPerformed(ActionEvent event) {
      String serching = m_search.getText();

      if (m_program.getArray().contains(serching)) {
        int size = m_program.getArray().size();
        final ArrayList<String> array = m_program.getArray();
        String [] list = new String[size];

        for (int k = 0; k < size; ++k) {
          list[k] = array.get(k);
        }
        Arrays.sort(list);

        for (int k = 0; k < size; ++k) {
          if (list[k].equals(serching)) {
            m_list.setSelectedIndex​(k);
            break;
          }
        }
      }
    }
  }

  class ShowListener extends WindowAdapter implements ActionListener {
    private JDialog m_showPassword;

    class ApplyShowed implements ActionListener {

      public void actionPerformed(ActionEvent event) {
        activityMode = false;
        m_main.setEnabled(true);
        m_showPassword.dispose();
      }
    };

    public void actionPerformed(ActionEvent event) {
      if (m_selectedData != null) {
        activityMode = true;
        m_main.setEnabled(false);
        m_showPassword = new JDialog(m_main, "Show Password");
        m_showPassword.addWindowListener(this);

        JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(new ApplyShowed());

        JLabel serviceLabel = new JLabel(" Service: " + m_selectedData);
        JTextField passwordLabel = new JTextField("Pass: " + m_program.getPass(m_selectedData));
        serviceLabel.setFont(new Font("Bree", Font.BOLD, 12));
        passwordLabel.setFont(new Font("Bree", Font.BOLD, 12));

        m_showPassword.getContentPane().add(BorderLayout.NORTH,serviceLabel);
        m_showPassword.getContentPane().add(BorderLayout.CENTER, passwordLabel);

        m_showPassword.add(BorderLayout.SOUTH, applyButton);

        m_showPassword.setSize(200, 100);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) (dim.getWidth() / 2 - m_showPassword.getWidth() / 2);
        int y = (int) (dim.getHeight() / 2 - m_showPassword.getHeight() / 2);
        m_showPassword.setLocation(x, y);
        m_showPassword.setResizable(false);
        m_showPassword.setVisible(true);
      }
    }

    public void windowClosing(WindowEvent e) {
      activityMode = false;
      m_main.setEnabled(true);
    }
  };

  class AddListener extends WindowAdapter implements ActionListener {
    private JFrame m_addFrame;
    private JPanel m_addPanel;
    private JTextField m_siteField;
    private JPasswordField m_passField;
    int errorMode;

    class AcceptAddListener implements ActionListener {

      class InvalidDataError extends WindowAdapter implements ActionListener {
        JDialog m_invalidDataDialog;

        public int invalidData(String site, String pass) {
          errorMode = 0;
          int key = handleError(site, pass);
          if (key != -1) {
            m_addFrame.setEnabled(false);
            m_addFrame.setAlwaysOnTop(false);
            m_invalidDataDialog = new JDialog(m_addFrame, "Invalid Data");

            String dataError = "\n";
            switch(key) {
              case 0:
                dataError = "Put in service";
                break;
              case 1:
                dataError = "Put in password";
                break;
              case 2:
                dataError = "Put in both values";
                break;
                case 3:
                dataError = "Duplicate service";
                break;
            }

            JButton applyButton = new JButton("Apply");
            applyButton.addActionListener(this);

            JPanel panel = new JPanel();

            m_invalidDataDialog.getContentPane().add(BorderLayout.NORTH, new JLabel(dataError));
            m_invalidDataDialog.getContentPane().add(BorderLayout.CENTER, applyButton);
            m_invalidDataDialog.setResizable(false);
            m_invalidDataDialog.addWindowListener(this);

            m_invalidDataDialog.setSize(200, 100);
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            int x = (int) (dim.getWidth() / 2 - m_invalidDataDialog.getWidth() / 2);
            int y = (int) (dim.getHeight() / 2 - m_invalidDataDialog.getHeight() / 2);
            m_invalidDataDialog.setLocation(x, y);
            m_invalidDataDialog.setVisible(true);
            m_invalidDataDialog.setAlwaysOnTop(true);
          }

          return key;
        }

        public void actionPerformed(ActionEvent event) {
          errorMode = 0;
          m_invalidDataDialog.dispose();
          m_addFrame.setEnabled(true);
          m_addFrame.setAlwaysOnTop(true);
        }

        public void windowClosing(WindowEvent e) {
          m_addFrame.setEnabled(true);
          m_addFrame.setAlwaysOnTop(true);
          errorMode = 0;
        }
      };

      public int handleError(String site, String pass) {
        int errorCode = -1;

        if (site.length() == 0) {
          errorCode = 0;
        }

        if (pass.length() == 0) {
          errorCode = 1;
        }

        if ((site.length() == 0) && (pass.length() == 0)) {
          errorCode = 2;
        }

        if (m_program.getArray().contains(site)) {
          errorCode = 3;
        }

        return errorCode;
      }

      public void actionPerformed(ActionEvent event) {
        if (errorMode == 0) {
          String site = m_siteField.getText();
          String pass = m_passField.getText();
          int key = new InvalidDataError().invalidData(site, pass);

          if (key == -1) {
            m_program.add(site, pass);
            updateList();
            m_addFrame.dispose();
            activityMode = false;
            m_main.setEnabled(true);
          }
        }
      }
    };

    public AddListener() {
      errorMode = 0;
    }

    public void actionPerformed(ActionEvent event) {
      if (!activityMode) {
        m_addFrame = new JFrame("Add Pare");
        m_siteField = new JTextField("Name of service");
        m_passField = new JPasswordField();
        m_passField.setEchoChar('*');

        activityMode = true;
        m_main.setEnabled(false);

        JButton acceptButton = new JButton("Accept");
        acceptButton.addActionListener(new AcceptAddListener());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(m_siteField);
        panel.add(m_passField);

        panel.add(Box.createRigidArea(new Dimension(70, 0)));
        panel.add(acceptButton);

        m_addFrame.getContentPane().add(BorderLayout.CENTER, panel);
        m_addFrame.setSize(200, 100);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) (dim.getWidth() / 2 - m_addFrame.getWidth() / 2);
        int y = (int) (dim.getHeight() / 2 - m_addFrame.getHeight() / 2);
        m_addFrame.setLocation(x, y);
        m_addFrame.setVisible(true);
        m_addFrame.setResizable(false);
        m_addFrame.setAlwaysOnTop(true);
        m_addFrame.setFocusableWindowState(true);
        m_addFrame.addWindowListener(this);
      }
    }

    public void windowClosing(WindowEvent e) {
      m_main.setEnabled(true);
      activityMode = false;
    }
  };

  class RemoveListener implements ActionListener {
    public void actionPerformed(ActionEvent event) {
      if (m_selectedData != null) {
        m_program.remove(m_selectedData);
        m_selectedData = null;
        updateList();
      }
    }
  };

  class CleanListener extends WindowAdapter implements ActionListener {
    JDialog m_youShure;
    class YesListener implements ActionListener {
      public void actionPerformed(ActionEvent event) {
        m_youShure.dispose();
        m_program.clean();
        activityMode = false;
        m_main.setEnabled(true);
        updateList();
      }
    };

    class CancelListener implements ActionListener {
      public void actionPerformed(ActionEvent event) {
        activityMode = false;
        m_main.setEnabled(true);
        m_youShure.dispose();
      }
    };

    public void actionPerformed(ActionEvent event) {
      if (!activityMode && m_program.getArray().size() != 0) {
        m_main.setEnabled(false);
        activityMode = true;
        m_youShure = new JDialog(m_main, "Accept removing");

        JLabel shureLabel = new JLabel("Do you want to clear list?");
        shureLabel.setFont(new Font("Bree", Font.BOLD, 12));

        JButton yesButton = new JButton("Yes");
        yesButton.addActionListener(new YesListener());
        JButton noButton = new JButton("No");
        noButton.addActionListener(new CancelListener());

        m_youShure.getContentPane().add(BorderLayout.NORTH, shureLabel);
        m_youShure.getContentPane().add(BorderLayout.CENTER, yesButton);
        m_youShure.getContentPane().add(BorderLayout.SOUTH, noButton);

        m_youShure.setSize(280, 100);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) (dim.getWidth() / 2 - m_youShure.getWidth() / 2);
        int y = (int) (dim.getHeight() / 2 - m_youShure.getHeight() / 2);
        m_youShure.setLocation(x, y);
        m_youShure.setResizable(false);
        m_youShure.setVisible(true);
      }
    }

    public void windowClosing(WindowEvent e) {
      m_main.setEnabled(true);
      activityMode = false;
    }

  };

  class ListsListener implements ListSelectionListener {
    public void valueChanged(ListSelectionEvent lse) {
      if (!lse.getValueIsAdjusting()) {
        m_selectedData = (String) m_list.getSelectedValue();
      }
    }
  };

  public MyGui() {
    m_program = new PasswordCacher();
    m_main = new JFrame("Password Cacher");
    m_selectedData = null;
    setupList();

    m_bPanel = new JPanel();
    m_bPanel.setLayout(new BoxLayout(m_bPanel, BoxLayout.Y_AXIS));
    m_main.getContentPane().add(BorderLayout.EAST, m_bPanel);

    m_search = new JTextField("Search");
    m_search.setColumns(1);
    m_search.setFont(new Font("Bree", Font.BOLD, 12));
    m_main.getContentPane().add(BorderLayout.NORTH, m_search);
    m_search.addActionListener(new SearchListener());

    JButton showButton = new JButton("Show Password");
    showButton.addActionListener(new ShowListener());
    JButton addButton = new JButton("Add Pare");
    addButton.addActionListener(new AddListener());
    JButton removeButton = new JButton("Remove Pare");
    removeButton.addActionListener(new RemoveListener());
    JButton cleanButton = new JButton("Clean the list");
    cleanButton.addActionListener(new CleanListener());

    m_bPanel.add(new JLabel(" "));
    m_bPanel.add(showButton);
    m_bPanel.add(addButton);
    m_bPanel.add(removeButton);
    m_bPanel.add(cleanButton);

    m_main.setSize(300, 300);
    m_main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (int) (dim.getWidth() / 2 - m_main.getWidth() / 2);
    int y = (int) (dim.getHeight() / 2 - m_main.getHeight() / 2);
    m_main.setLocation(x, y);
    m_main.setVisible(true);
    m_main.setResizable(false);
    activityMode = false;
  }

  private void setupList() {
    ArrayList<String> array = m_program.getArray();
    String [] list = new String[array.size()];
    for (int k = 0; k < array.size(); ++k) {
      list[k] = array.get(k);
    }
    Arrays.sort(list);
    m_list = new JList(list);

    JScrollPane scroller = new JScrollPane(m_list);
    scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

    m_lPanel = new JPanel();
    m_lPanel.setLayout(new BoxLayout(m_lPanel, BoxLayout.Y_AXIS));
    m_lPanel.add(new JLabel("          Services:"));
    m_lPanel.add(scroller);
    m_main.getContentPane().add(BorderLayout.CENTER, m_lPanel);

    ListsListener listner = new ListsListener();
    m_list.addListSelectionListener(listner);
    m_list.setSelectionMode​(ListSelectionModel.SINGLE_SELECTION);
  }

  private void updateList() {
    ArrayList<String> array = m_program.getArray();
    String [] list = new String[array.size()];
    for (int k = 0; k < array.size(); ++k) {
      list[k] = array.get(k);
    }
    Arrays.sort(list);

    m_list.setListData(list);
    m_list.revalidate();
    m_list.repaint();
  }

};

class Launcher {
  public static void main(String [] args) {
    MyGui launcher = new MyGui();
  }
};
