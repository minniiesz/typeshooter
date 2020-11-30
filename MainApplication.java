
package mainapplication;

//import java.awt
import java.awt.*;
import java.awt.event.*;
//import java.io
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
//import java.util
import java.util.ArrayList;
import java.util.Random;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Collections;
//import java.swing
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
//import java.text
import java.text.DecimalFormat;

public class MainApplication extends WindowAdapter implements ActionListener {

    //the main frame's default button
    private static JButton defaultButton = null;
    private static JButton creditButton = null;
    private static JButton gameButton = null;
    private static JButton optionButton = null;
    private static JButton top10Button = null;

    private final static String CREDIT_WINDOW = "credit_win";
    protected final static String GAME_WINDOW = "game_win";
    protected final static String OPTION_WINDOW = "option_win";
    protected final static String TOP10_WINDOW = "top10_win";

    // FROM Game
    // components
    private JPanel contentpane;
    private JLabel drawpane;
    private JToggleButton[] typeToggles;
    private ButtonGroup bgroup;
    private JTextField scoreText, levelText;
    private JTextField textUsername = new JTextField(15);
    private JComboBox soundCombo, bgCombo, laserCombo;

    private MyUFOLabel[] ufoLabel;
    private MyLaserLabel[] laserLabel;
    private flagLabel flagLabel;
    private MyImageIcon backgroundImg, allgunImg;
    private MySoundEffect themeSound, winSound, levelupsound, gameoversound;

    // working variables - adjust the values as you want
    private int frameWidth = 1366, frameHeight = 768;
    private int ufoCurY = 0;
    private int ufoWidth = 100, ufoHeight = 95;
    private Thread controlThread;

    private boolean ufoActive = true;
    private int flagLineY = 468;
    private int speed = 40;
    private int scoreCount = 0;
    private int soundSelect = 1;
    private int laserSelect = 1;
    private int bgSelect = 1;
    private int levelSelect = 1;
    private String typeSelect = "2";
    private ArrayList<userData> arrTop10 = new ArrayList<userData>();

    //chracters data
    private String[] chr1 = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
    private String[] chr2 = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
        "w", "x", "y", "z"};
    private String[] chr3 = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
        "W", "X", "Y", "Z"};
    private String[] chr4 = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l",
        "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
        "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private String[] chr5 = {"ก", "ข", "ค", "ฆ", "ง", "จ", "ฉ", "ช", "ซ", "ฌ", "ญ", "ฎ", "ฏ", "ฐ", "ฑ", "ฒ", "ณ", "ด", "ต", "ถ", "ท",
        "ธ", "น", "บ", "ป", "ผ", "ฝ", "พ", "ฟ", "ภ", "ม", "ย", "ร", "ล", "ว", "ศ", "ษ", "ส", "ห", "ฬ", "อ", "ฮ"};

    private int[] ufoXp = {300, 820, 560, 40, 170, 1080, 950, 430, 690, 1210};
    private int[] ufoNum = {2, 3, 4, 5, 6, 7, 8, 9, 10, 10};

    public MainApplication() {
        // อ่านไฟล์ top10.txt to arrTop10       
        String fileName = "top10.txt";
        try (Scanner scan = new Scanner(new File(fileName));) {
            String Line = "";
            while (scan.hasNext()) {
                //อ่านและเก็ยข้อมูลสินค้าไว้ที่ arrProduct(ArrayList)
                Line = scan.nextLine();
                String[] buf = Line.split(",");               
                userData temp = new userData(Integer.parseInt(buf[0]), buf[1], Integer.parseInt(buf[2]));
                arrTop10.add(temp);

            }
            // แสดงรายการชื่อสินค้าทั้งหมด
            for (int i = 0; i < arrTop10.size(); i++) {
                arrTop10.get(i).reportUser();
            }
        } catch (FileNotFoundException e) {
            System.out.println(e);
            System.out.printf("Find not found 'top10.txt'");
        }

    }

    public void showWindow(int op) {
        JFrame frame;
        if (op == 1) {
            frame = new CreditFrame();
            frame.setSize(new Dimension(500, 300));
        } else if (op == 2) {
            frame = new LoginFrame();
            frame.setSize(new Dimension(400, 300));
        } else if (op == 3) {
            frame = new OptionFrame();
            frame.setSize(new Dimension(500, 350));
        } else {
            frame = new Top10Frame();
            frame.setSize(new Dimension(290, 300));
        }
        //Show window.
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        //Handle the New window button.
        if (CREDIT_WINDOW.equals(command)) {
            showWindow(1);
        }
        if (GAME_WINDOW.equals(command)) {
            showWindow(2);
        }
        if (OPTION_WINDOW.equals(command)) {
            showWindow(3);
        }
        if (TOP10_WINDOW.equals(command)) {
            showWindow(4);
        }
    }

    protected JComponent creditButtonPane() {
        Icon icon = new ImageIcon("resources/Menu/Credit.jpg");
        JButton button_credit = new JButton("Credit", icon);
        button_credit.setActionCommand(CREDIT_WINDOW);
        button_credit.addActionListener(this);
        button_credit.setPreferredSize(new Dimension(200, 341));
        button_credit.setBorder(null);
        button_credit.setContentAreaFilled(false);
        //Center the button in a panel with some space around it.
        JPanel pane = new JPanel(); //use default FlowLayout      
        pane.setBackground(Color.BLACK);
        pane.add(button_credit);

        return pane;
    }

    protected JComponent gameButtonPane() {
        Icon icon = new ImageIcon("resources/Menu/start.jpg");
        JButton button_game = new JButton("Start", icon);
        button_game.setActionCommand(GAME_WINDOW);
        button_game.addActionListener(this);
        button_game.setPreferredSize(new Dimension(200, 341));

        button_game.setBorder(null);
        button_game.setContentAreaFilled(false);
        defaultButton = button_game; //Used later to make this the frame's default button.

        //Center the button in a panel with some space around it.
        JPanel pane = new JPanel(); //use default FlowLayout      
        pane.add(button_game);
        pane.setBackground(new Color(0, 0, 0));

        return pane;
    }

    protected JComponent optionButtonPane() {
        Icon icon = new ImageIcon("resources/Menu/option.jpg");
        JButton button_option = new JButton("Option", icon);
        button_option.setActionCommand(OPTION_WINDOW);
        button_option.addActionListener(this);
        button_option.setPreferredSize(new Dimension(200, 341));
        button_option.setBorder(null);
        button_option.setContentAreaFilled(false);
        //Center the button in a panel with some space around it.
        JPanel pane = new JPanel(); //use default FlowLayout       
        pane.setBackground(new Color(0, 0, 0));
        pane.add(button_option);

        return pane;
    }

    protected JComponent top10ButtonPane() {
        Icon icon = new ImageIcon("resources/Menu/top10.jpg");
        JButton button_option = new JButton("Top 10", icon);
        button_option.setActionCommand(TOP10_WINDOW);
        button_option.addActionListener(this);
        button_option.setPreferredSize(new Dimension(200, 341));
        button_option.setBorder(null);
        button_option.setContentAreaFilled(false);

        //Center the button in a panel with some space around it.
        JPanel pane = new JPanel();
        pane.setBackground(new Color(0, 0, 0));
        pane.add(button_option);

        return pane;
    }

    private static void createAndShowGUI() {

        //Instantiate the controlling class.     
        JFrame frame = new JFrame("UFO TypeShooter");
        frame.setPreferredSize(new Dimension(1366, 768));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        //set background label
        JLabel bgpane = new JLabel();
        bgpane.setIcon(new ImageIcon("resources/Background/backgroundmenu.jpg"));

        //Get top 10 data files and all var
        MainApplication demo = new MainApplication();

        // create a new panel with GridBagLayout manager
        JPanel newPanel = new JPanel();
        newPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 50, 200));

        //ไม่ให้ panel ถูก bg ทับ
        newPanel.setOpaque(false);

        newPanel.add(demo.top10ButtonPane());
        newPanel.add(demo.optionButtonPane());
        newPanel.add(demo.gameButtonPane());
        newPanel.add(demo.creditButtonPane());

        Icon icon = new ImageIcon("resources/Menu/Exit.jpg");
        JButton button = new JButton("  Exit  ", icon);
        button.setPreferredSize(new Dimension(200, 341));
        button.setBorder(null);
        button.setContentAreaFilled(false);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        newPanel.add(button);

        // add the panel to this frame
        frame.add(newPanel);

        //set dufaultButton in frame
        frame.getRootPane().setDefaultButton(defaultButton);

        //Display the window.
        frame.pack();
        frame.setLocationRelativeTo(null); //center it
        frame.setVisible(true);
        //add background

        frame.add(bgpane);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        //ใช้ SwingUtilities.invokeLater เพื่อป้องกันไม่ให้โปรแกรมทำงานไม่ถูกต้อง
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    class CreditFrame extends JFrame implements ActionListener {

        // Data credit for students.
        private String[] columnNames = {"#", "ID", "Name", "Surname"};
        private Object[][] data = {
            {"1", "6113225", "Yanasiri", "Sopacharoen"},
            {"2", "6113228", "Wittawat", "Pongpipat"},
            {"3", "6113293", "Krittamet", "Chansakaew"},};

        //Create a frame with a button.
        public CreditFrame() {
            super("Credit");
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);

            //This button lets you close
            JButton button = new JButton("Close window");
            button.addActionListener(this);

            //Place the button near the bottom of the window.
            Container contentPane = getContentPane();
            contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));

            // Defines table's column width.
            int[] columnsWidth = {30, 150, 150, 150};
            //Create JTable
            JTable table = new JTable(data, columnNames);
            // Configures table's column width.
            int i = 0;
            for (int width : columnsWidth) {
                TableColumn column = table.getColumnModel().getColumn(i++);
                column.setMaxWidth(width);
            }
            //Set column center
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

            JScrollPane scrollPane = new JScrollPane(table);
            contentPane.add(scrollPane);

            contentPane.add(button);
            button.setAlignmentX(Component.CENTER_ALIGNMENT); //horizontally centered       
            setLocationRelativeTo(null);
        }

        //Make the button do the same thing as the default close operation
        //(DISPOSE_ON_CLOSE).
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
            dispose();
        }
    }

    class Top10Frame extends JFrame implements ActionListener {

        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        private String[] columnNames = {"#", "Name", "Score"};

        //Create a frame with a button.
        public Top10Frame() {
            super("Top10");
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);

            //This button lets you close
            JButton button = new JButton("Close window");
            button.addActionListener(this);

            //Place the button near the bottom of the window.
            Container contentPane = getContentPane();
            contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));

            // arrTop10 to data[]
            Collections.sort(arrTop10);//เรียง Point จากมากไปน้อย
            for (int i = 0; i < arrTop10.size(); i++) {
                int item = arrTop10.get(i).getItem();
                String name = arrTop10.get(i).getName();
                int score = arrTop10.get(i).getScore();

                Object[] data = {item, name, score};
            }

            //Create JTable
            JTable table = new JTable();
            DefaultTableModel model = new DefaultTableModel();
            model.setColumnIdentifiers(columnNames);
            table.setModel(model);

            for (int i = 0; i < 10; i++) {
                model.addRow(new Object[]{i + 1, arrTop10.get(i).getName(), decimalFormat.format(arrTop10.get(i).getScore())});
            }

            // Defines table's column width.
            int[] columnsWidth = {30, 150, 90};
            // Configures table's column width.
            int i = 0;
            for (int width : columnsWidth) {
                TableColumn column = table.getColumnModel().getColumn(i++);
                column.setMinWidth(width);
                column.setMaxWidth(width);
                column.setPreferredWidth(width);
            }

            //Set column center
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

            // Set column right
            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
            rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
            table.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);

            // add JScrollPane
            JScrollPane scrollPane = new JScrollPane(table);
            contentPane.add(scrollPane);

            // add button "Close window"
            contentPane.add(button);
            button.setAlignmentX(Component.CENTER_ALIGNMENT); //horizontally centered
            //set frame to center
            setLocationRelativeTo(null);
        }

        //Make the button do the same thing as the default close operation
        //(DISPOSE_ON_CLOSE).
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
            dispose();
        }
    }

    class OptionFrame extends JFrame implements ActionListener {

        private JLabel labelType = new JLabel("Select Type:");
        private JLabel labelBg = new JLabel("Select Location: ");
        private JLabel labelSound = new JLabel("Select Theme: ");
        private JLabel labelLaser = new JLabel("Select Laser Sound: ");
        private JLabel labelLevel = new JLabel("Select Level: ");

        private JButton button = new JButton("Close window");

        //Create a frame with a button.
        public OptionFrame() {
            super("Option");
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);

            //set panellayout to gridbaglayout
            JPanel pane = new JPanel(new GridBagLayout());
            pane.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
                      
            c.insets = new Insets(10, 10, 10, 10);

            //natural height, maximum width
            //set to to horizontal line
            c.fill = GridBagConstraints.HORIZONTAL;

            //add labeltype
            c.gridx = 0;
            c.gridy = 0;
            pane.add(labelType, c);

            // Add ItemListener (anonymouse class) to each typeToggles -- set type
            typeToggles = new JToggleButton[5];
            bgroup = new ButtonGroup();
            typeToggles[0] = new JRadioButton("0-9");
            typeToggles[0].setName("1");
            typeToggles[1] = new JRadioButton("a-z");
            typeToggles[1].setName("2");
            typeToggles[2] = new JRadioButton("A-Z");
            typeToggles[2].setName("3");
            typeToggles[3] = new JRadioButton("0-Z");
            typeToggles[3].setName("4");
            typeToggles[4] = new JRadioButton("ก-ฮ");
            typeToggles[4].setName("5");

            //default select "1"
            typeToggles[Integer.parseInt(typeSelect) - 1].setSelected(true);
            //add btgroup
            for (int i = 0; i < 5; i++) {
                bgroup.add(typeToggles[i]);
                typeToggles[i].addItemListener(new ItemListener() {
                    public void itemStateChanged(ItemEvent e) {
                        JRadioButton temp = (JRadioButton) e.getItem();
                        if (temp.isSelected()) {
                            typeSelect = temp.getName();
                        }
                    }
                });
                c.gridx = i + 1;
                c.gridy = 0;
                pane.add(typeToggles[i], c);
            }

            //add text select bg
            c.gridx = 0;
            c.gridy = 1;
            pane.add(labelBg, c);

            // define bgCombo       
            String[] bgList = {"Outer Space", "Desert Castle", "Tundra Forest", "City Sunset", "On The Mountain", "Midnight Ocean"};
            bgCombo = new JComboBox(bgList);
            bgCombo.setSelectedIndex(bgSelect - 1);
            bgCombo.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    int index = bgCombo.getSelectedIndex();
                    bgSelect = index + 1;
                }
            });

            //add bgcombo
            c.gridwidth = 3;
            c.gridx = 1;
            c.gridy = 1;
            pane.add(bgCombo, c);

            //add text select sound
            c.gridx = 0;
            c.gridy = 2;
            pane.add(labelSound, c);

            // define soundCombo
            String[] soundList = {"Outer Space", "Desert Castle", "Tundra Forest", "City Sunset", "On The Mountain", "Midnight Ocean"};
            soundCombo = new JComboBox(soundList);
            soundCombo.setSelectedIndex(soundSelect - 1);
            soundCombo.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    int index = soundCombo.getSelectedIndex();
                    soundSelect = index + 1;
                }
            });

            //add soundcombo
            c.gridwidth = 3;
            c.gridx = 1;
            c.gridy = 2;
            pane.add(soundCombo, c);

            //add text laserselect
            c.gridx = 0;
            c.gridy = 3;
            pane.add(labelLaser, c);

            //define lasercombo
            String[] laserList = {"1", "2", "3", "4", "5", "6"};
            laserCombo = new JComboBox(laserList);
            laserCombo.setSelectedIndex(laserSelect - 1);
            laserCombo.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    int index = laserCombo.getSelectedIndex();
                    laserSelect = index + 1;
                }
            });

            //add lasercombo
            c.gridwidth = 3;
            c.gridx = 1;
            c.gridy = 3;
            pane.add(laserCombo, c);

            //add text selectlevel
            c.gridx = 0;
            c.gridy = 4;
            pane.add(labelLevel, c);

            //define Slider
            // Settings on the sliders
            JSlider slider = new JSlider(JSlider.HORIZONTAL, 1, 10, levelSelect);
            slider.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent event) {
                    levelSelect = slider.getValue();
                }
            });

            // Set major ticks for the slider
            slider.setMajorTickSpacing(1);
            //Turn on labels at major tick marks.
            slider.setPaintTicks(true);
            // Set the labels to be painted on the slider
            slider.setPaintLabels(true);

            //add slider
            c.gridwidth = 5;
            c.gridx = 1;
            c.gridy = 4;
            pane.add(slider, c);

            //define close window
            button = new JButton("Close window");
            button.addActionListener(this);
            c.fill = GridBagConstraints.HORIZONTAL;
            c.ipady = 0;       //reset to default    
            c.anchor = GridBagConstraints.PAGE_END; //bottom of space
            c.insets = new Insets(10, 100, 10, 100);  //top padding
            c.gridx = 0;       //aligned with button 2
            c.gridwidth = 6;   //2 columns wide
            c.gridy = 6;       //third row
            pane.add(button, c);

            //add panel
            add(pane);

            // set border for the panel 
            pane.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "Option Panel"));
            pack();

            //set frame location to center
            setLocationRelativeTo(null);

        }

        //Make the button do the same thing as the default close operation
        //(DISPOSE_ON_CLOSE).
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
            //closing & disable frame
            dispose();
        }
    }

    class LoginFrame extends JFrame implements ActionListener {

        private JLabel labelUsername = new JLabel("Enter username: ");

        //Create a frame with a button.
        public LoginFrame() {
            super("Login window");
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setResizable(false);

            // create a new panel with GridBagLayout manager
            JPanel newPanel = new JPanel(new GridBagLayout());

            GridBagConstraints constraints = new GridBagConstraints();
            //keep spacing between button and textfield
            constraints.insets = new Insets(10, 10, 10, 10);

            // add components to the panel
            constraints.gridx = 0;
            constraints.gridy = 0;
            newPanel.add(labelUsername, constraints);

            constraints.gridx = 1;
            constraints.gridy = 0;
            textUsername.setEditable(true);
            textUsername.setFocusable(true);
            textUsername.setText("");
            newPanel.add(textUsername, constraints);

            constraints.gridx = 0;
            constraints.gridy = 1;
            constraints.gridwidth = 2;

            //This button lets you close
            JButton button = new JButton("Play game");
            button.addActionListener(this);
            //set default button for KeyEnter
            getRootPane().setDefaultButton(button);

            newPanel.add(button, constraints);

            // set border for the panel 
            newPanel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "Login Panel"));

            // add the panel to this frame
            add(newPanel);
            pack();
            setLocationRelativeTo(null);

        }

        //Make the button do the same thing as the default close operation
        //(DISPOSE_ON_CLOSE).
        public void actionPerformed(ActionEvent e) {

            if (textUsername.getText().equals("")) {
                JOptionPane.showMessageDialog(new JFrame(), "Please enter your name.", "Warning",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                setVisible(false);
                dispose();
                // Open gameFrame
                JFrame frame = new GameFrame();
                frame.setSize(new Dimension(frameWidth, frameHeight));
            }
        }
    }

    class GameFrame extends JFrame implements KeyListener {

        //Create a frame with a button.
        public GameFrame() {
            super("UFO TypeShooter");
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setBounds(0, 0, frameWidth, frameHeight);
            setResizable(false);
            setVisible(true);
            scoreCount = 0;// initial value = 0
            ufoActive = true;// initial value = true;

            //Action for close frame
            addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    //ไม่ให้ ufo เคลื่อนที่
                    ufoActive = false;
                    // Check Top10 score                                     
                    themeSound.stop();
                    gameoversound.playOnce();
                    JOptionPane.showMessageDialog(new JFrame(), "GAME END!", "Sorry!",
                            JOptionPane.INFORMATION_MESSAGE);
                    frameClose();
                    System.out.println("End!!!!!!!!!");

                }
            });

            contentpane = (JPanel) getContentPane();
            contentpane.setLayout(new BorderLayout());
            contentpane.addKeyListener(this);
            contentpane.setFocusable(true);

            setLocationRelativeTo(null);

            // Add Components
            AddMainComponents();
            setControlThread();

        }

        public void keyReleased(KeyEvent e) {
        }

        public void keyPressed(KeyEvent e) {

        }

        public void keyTyped(KeyEvent e) {
            char keyChar = e.getKeyChar();
            System.out.println("keyType is : " + keyChar);
            for (int i = 0; i < ufoNum[levelSelect - 1]; i++) {
                if (String.valueOf(keyChar).equals(ufoLabel[i].getChr())) {
                    if (ufoLabel[i].getActive()) {
                        laserLabel[i].setActive(true);
                        ufoLabel[i].setBoom();
                        System.out.println("Boom!!!!!!!!!!!!!");
                        scoreCount = scoreCount + 1;
                        scoreText.setText(String.valueOf(scoreCount));
                        if ((scoreCount / 30) + 1 > levelSelect) {
                            if (levelSelect < 10) {
                                levelSelect = (scoreCount / 30) + 1;
                                levelupsound.playOnce();
                            } else {
                                levelSelect = 10;
                            }
                        }
                        levelText.setText(String.valueOf(levelSelect));
                        System.out.printf("You typed %s\n", ufoLabel[i].getChr());
                    }
                }               
            }
        }

        // check top10 score
        public void checkTop10Score() {
            // Check Top10 score
            Collections.sort(arrTop10);//เรียง Point จากมากไปน้อย
            boolean checkTop = false;            
            for (int i = 0; i < 10; i++) {
                if (scoreCount > arrTop10.get(i).getScore()) {
                    checkTop = true;
                }
            }           
            if (checkTop) {
                JOptionPane.showMessageDialog(new JFrame(), scoreCount + " scores! Your ranking in top10.", "Congratulations!",
                        JOptionPane.INFORMATION_MESSAGE);
                userData newTop10 = new userData(11, textUsername.getText(), scoreCount);
                arrTop10.add(newTop10);
                Collections.sort(arrTop10);//เรียง Point จากมากไปน้อย
                String outfile = "top10.txt";
                try {
                    PrintWriter write = new PrintWriter(outfile);
                    for (int i = 0; i < 10; i++) {
                        write.printf("%d,%s,%d\r\n", i + 1, arrTop10.get(i).getName(), arrTop10.get(i).getScore());
                    }
                    write.close();
                } catch (Exception ea) {
                    System.err.println("An write file error occurs.");
                }
            }

        }

        public void frameClose() {
            checkTop10Score();
            dispose();
        }

        // Thread for referee(ผู้ตัดสิน)
        public void setControlThread() {
            controlThread = new Thread() {
                public void run() {
                    boolean gameOver = false;
                    while (!gameOver) {
                        // check laserLabel active
                        for (int i = 0; i < ufoNum[levelSelect - 1]; i++) {
                            if (laserLabel[i].getActive()) {
                                winSound.playOnce();
                                try {
                                    Thread.sleep(300);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                winSound.stop();
                                laserLabel[i].setActive(false);
                            } //clear lasrLabel active

                            // check UFO not active
                            if (!ufoLabel[i].getActive()) {
                                // random new Char
                                if (typeSelect.equals("1")) {
                                    ufoLabel[i].setChr(chr1[usingRandomClass()]);
                                }
                                if (typeSelect.equals("2")) {
                                    ufoLabel[i].setChr(chr2[usingRandomClass()]);
                                }
                                if (typeSelect.equals("3")) {
                                    ufoLabel[i].setChr(chr3[usingRandomClass()]);
                                }
                                if (typeSelect.equals("4")) {
                                    ufoLabel[i].setChr(chr4[usingRandomClass()]);
                                }
                                if (typeSelect.equals("5")) {
                                    ufoLabel[i].setChr(chr5[usingRandomClass()]);
                                }
                                // active ufo
                                ufoLabel[i].setMoveConditions(ufoXp[i], ufoCurY);
                                ufoLabel[i].setActive(true);
                                laserLabel[i].setActive(false);
                            }
                            // check game over
                            if (!gameOver) {
                                if (ufoLabel[i].getY() >= flagLineY + 94 - ufoHeight) {
                                    gameOver = true;
                                    ufoActive = false;
                                    themeSound.stop();
                                    gameoversound.playOnce();
                                    JOptionPane.showMessageDialog(new JFrame(), "GAME OVER!", "Sorry!",
                                            JOptionPane.INFORMATION_MESSAGE);
                                    frameClose();
                                }
                            }
                        }
                        try {
                            Thread.sleep(speed * 3);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } // end while
                } // end run
            }; // end thread creation
            controlThread.start();
        }
    }

    //////////////////////////////////////////////////////////////////////////
    public void AddMainComponents() {
        // add background label
        if (bgSelect == 1) {
            backgroundImg = new MyImageIcon("resources/Background/background1.jpg").resize(frameWidth, frameHeight);
        }
        if (bgSelect == 2) {
            backgroundImg = new MyImageIcon("resources/Background/background2.png").resize(frameWidth, frameHeight);
        }
        if (bgSelect == 3) {
            backgroundImg = new MyImageIcon("resources/Background/background3.png").resize(frameWidth, frameHeight);
        }
        if (bgSelect == 4) {
            backgroundImg = new MyImageIcon("resources/Background/background4.jpg").resize(frameWidth, frameHeight);
        }
        if (bgSelect == 5) {
            backgroundImg = new MyImageIcon("resources/Background/background5.jpg").resize(frameWidth, frameHeight);
        }
        if (bgSelect == 6) {
            backgroundImg = new MyImageIcon("resources/Background/background6.png").resize(frameWidth, frameHeight);
        }

        drawpane = new JLabel();
        drawpane.setIcon(backgroundImg);
        drawpane.setLayout(null);
        // add all-gun base station
        allgunImg = new MyImageIcon("resources/Sprite/all-gun.png").resize(1299, 204);
        JLabel ufoLabel2 = new JLabel(allgunImg);
        ufoLabel2.setBounds(28, 520, 1299, 204);
        drawpane.add(ufoLabel2);
        // add flagLabel
        flagLabel = new flagLabel("resources/Sprite/flagLine.png");
        flagLabel.setMoveConditions(0, 468);
        drawpane.add(flagLabel);

        // add Sound
        if (soundSelect == 1) {
            themeSound = new MySoundEffect("resources/Sound/theme1.wav");
        }
        if (soundSelect == 2) {
            themeSound = new MySoundEffect("resources/Sound/theme2.wav");
        }
        if (soundSelect == 3) {
            themeSound = new MySoundEffect("resources/Sound/theme3.wav");
        }
        if (soundSelect == 4) {
            themeSound = new MySoundEffect("resources/Sound/theme4.wav");
        }
        if (soundSelect == 5) {
            themeSound = new MySoundEffect("resources/Sound/theme5.wav");
        }
        if (soundSelect == 6) {
            themeSound = new MySoundEffect("resources/Sound/theme6.wav");
        }

        //add laser sound
        if (laserSelect == 1) {
            winSound = new MySoundEffect("resources/Sound/laser1.wav");
        }
        if (laserSelect == 2) {
            winSound = new MySoundEffect("resources/Sound/laser2.wav");
        }
        if (laserSelect == 3) {
            winSound = new MySoundEffect("resources/Sound/laser3.wav");
        }
        if (laserSelect == 4) {
            winSound = new MySoundEffect("resources/Sound/laser4.wav");
        }
        if (laserSelect == 5) {
            winSound = new MySoundEffect("resources/Sound/laser5.wav");
        }
        if (laserSelect == 6) {
            winSound = new MySoundEffect("resources/Sound/laser6.wav");
        }

        //add another sound
        levelupsound = new MySoundEffect("resources/Sound/levelup.wav");
        gameoversound = new MySoundEffect("resources/Sound/gameover.wav");
        themeSound.playLoop();

        //set textusername
        textUsername.setEditable(false);
        textUsername.setFocusable(false);

        //set leveltext
        levelText = new JTextField("0", 5);
        levelText.setEditable(false);
        levelText.setFocusable(false);
        levelText.setHorizontalAlignment(JTextField.CENTER);
        levelText.setText(String.valueOf(levelSelect));

        //set scoretext
        scoreText = new JTextField("0", 5);
        scoreText.setEditable(false);
        scoreText.setFocusable(false);
        scoreText.setHorizontalAlignment(JTextField.CENTER);

        // set JPanel
        JPanel control = new JPanel();
        control.setBounds(0, 0, 1366, 50);
        control.setBackground(new Color(0, 255, 255));
        control.add(new JLabel("Username:"));
        control.add(textUsername);
        control.add(new JLabel("Level:"));
        control.add(levelText);
        control.add(new JLabel("Score count:"));
        control.add(scoreText);

        // UFOLabel
        ufoLabel = new MyUFOLabel[11];
        for (int i = 0; i < 10; i++) {
            if (typeSelect.equals("1")) {
                ufoLabel[i] = new MyUFOLabel("resources/Sprite/ufo2.png", "resources/Sprite/boom.png", 0, chr1[usingRandomClass()]);
            }
            if (typeSelect.equals("2")) {
                ufoLabel[i] = new MyUFOLabel("resources/Sprite/ufo2.png", "resources/Sprite/boom.png", 0, chr2[usingRandomClass()]);
            }
            if (typeSelect.equals("3")) {
                ufoLabel[i] = new MyUFOLabel("resources/Sprite/ufo2.png", "resources/Sprite/boom.png", 0, chr3[usingRandomClass()]);
            }
            if (typeSelect.equals("4")) {
                ufoLabel[i] = new MyUFOLabel("resources/Sprite/ufo2.png", "resources/Sprite/boom.png", 0, chr4[usingRandomClass()]);
            }
            if (typeSelect.equals("5")) {
                ufoLabel[i] = new MyUFOLabel("resources/Sprite/ufo2.png", "resources/Sprite/boom.png", 0, chr5[usingRandomClass()]);
            }
            ufoLabel[i].setMoveConditions(ufoXp[i], ufoCurY);
            //active ufo refer level
            if (i < ufoNum[levelSelect - 1]) {
                ufoLabel[i].setActive(true);
            } else {
                ufoLabel[i].setActive(false);
            }
            drawpane.add(ufoLabel[i]);
        }

        // Laser gun
        laserLabel = new MyLaserLabel[11];
        for (int i = 0; i < 10; i++) {
            laserLabel[i] = new MyLaserLabel("resources/Sprite/laser-light.png");
            laserLabel[i].setMoveConditions(ufoXp[i] + 39, 0);
            laserLabel[i].setActive(false);
            drawpane.add(laserLabel[i]);
        }

        // ContentPanel
        contentpane.add(control, BorderLayout.NORTH);
        contentpane.add(drawpane, BorderLayout.CENTER);

    }

    //random character of ufo
    public int usingRandomClass() {
        Random randomGenerator = new Random();
        int len = 0;
        if (typeSelect.equals("1")) {
            len = chr1.length;
        }
        if (typeSelect.equals("2")) {
            len = chr2.length;
        }
        if (typeSelect.equals("3")) {
            len = chr3.length;
        }
        if (typeSelect.equals("4")) {
            len = chr4.length;
        }
        if (typeSelect.equals("5")) {
            len = chr5.length;
        }
        int randomInt = randomGenerator.nextInt(len);    
        return randomInt;
    }

    class MyUFOLabel extends JLabel {

        private int width = 100, height = 95;      // adjust label size as you want
        private MyImageIcon icon1, icon2;
        private int curX, curY;
        private boolean active = true, sleep = false;
        private String text;
        private Runnable r;

        public MyUFOLabel(String file1, String file2, int icn, String t) {
            icon1 = new MyImageIcon(file1).resize(width, height);
            icon2 = new MyImageIcon(file2).resize(width, height);
            setHorizontalAlignment(JLabel.CENTER);
            setIcon(icon1);
            text = t;
            Font font1 = new Font("Tahoma", Font.BOLD, 18);
            setFont(font1);
            setHorizontalTextPosition(JLabel.CENTER);
            setVerticalTextPosition(JLabel.CENTER);

            r = new Runnable() {
                public void run() {
                    while (ufoActive) {
                        if (getY() >= flagLineY + 94 - ufoHeight) {
                        } else {
                            setLocation(getX(), getY() + 1);
                        }
                        repaint();
                        if (!sleep) {
                            try {
                                Thread.sleep(speed - (levelSelect * 3));
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                setText(null);
                                Thread.sleep(250);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            setIcon(null);
                            sleep = false;
                            active = false;
                        }
                    }
                }
            };
            new Thread(r).start();
        }

        public void setMoveConditions(int x, int y) {
            curX = x;
            curY = y;
            setBounds(curX, curY, width, height);

        }

        public String getChr() {
            return text;
        }

        public void setChr(String s) {
            text = s;
            setText(text);

        }

        public boolean getActive() {
            return active;
        }

        public void setActive(boolean b) {
            active = b;
            if (active) {
                setIcon(icon1);
                setText(text);
            } else {
                setIcon(null);
                setText(null);
            }
            repaint();
        }

        public void setBoom() {
            setIcon(icon2);
            sleep = true;
            repaint();
        }
    }

    class MyLaserLabel extends JLabel {

        private int width = 23, height = 663;      // adjust label size as you want
        private MyImageIcon icon2;
        private int curX, curY;
        private boolean active = true;
        private String text;

        public MyLaserLabel(String file2) {
            icon2 = new MyImageIcon(file2).resize(width, height);
            setHorizontalAlignment(JLabel.CENTER);
            setIcon(icon2);

        }

        public void setMoveConditions(int x, int y) {
            curX = x;
            curY = y;
            setBounds(curX, curY, width, height);

        }

        public boolean getActive() {
            return active;
        }

        public void setActive(boolean b) {
            active = b;
            if (active) {
                setIcon(icon2);
            } else {
                setIcon(null);
            }
            repaint();
        }

    }

    class flagLabel extends JLabel implements MouseListener, MouseMotionListener {

        private int width = 1323, height = 94;      // adjust label size as you want
        private MyImageIcon icon;
        private int curX, curY;
        int sX = -1, sY = -1;
        boolean dragging = false;
        private Point offset;

        public flagLabel(String file) {
            icon = new MyImageIcon(file).resize(width, height);
            setHorizontalAlignment(JLabel.CENTER);
            setIcon(icon);
            addMouseListener(this);
            addMouseMotionListener(this);
        }

        public void mouseClicked(MouseEvent e) {
            System.out.println("mouseClicked");
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {
            offset = new Point(e.getPoint().x - this.getBounds().x, e.getPoint().y - this.getBounds().y);

            Point point = e.getPoint();
            System.out.println("mousePressed at " + point);
            sX = point.x;
            sY = point.y;
            dragging = true;
        }

        public void mouseReleased(MouseEvent e) {
            dragging = false;
            flagLineY = this.getBounds().y;
            System.out.printf("mouseReleased with y: %d\n", flagLineY);
        }

        public void mouseDragged(MouseEvent e) {
            System.out.println("mouseDragged");
            Point p = e.getPoint();
            curX = p.x;
            curY = p.y;
            if (dragging) {
                if (offset != null) {
                    int newX = e.getPoint().x - offset.x;
                    int newY = e.getPoint().y - offset.y;
                    //set flagLabel y-exit
                    this.setLocation(0, newY);
                }
                repaint();
            }
        }

        public void mouseMoved(MouseEvent e) {
            System.out.println("mouseMoved");
        }

        public void setMoveConditions(int x, int y) {
            curX = x;
            curY = y;
            setBounds(curX, curY, width, height);

        }

    }

}

///////////////////////////////////////////////////////
// Auxiliary class to resize image
class MyImageIcon extends ImageIcon {

    public MyImageIcon(String fname) {
        super(fname);
    }

    public MyImageIcon(Image image) {
        super(image);
    }

    public MyImageIcon resize(int width, int height) {
        Image oldimg = this.getImage();
        Image newimg = oldimg.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
        return new MyImageIcon(newimg);
    }
};

// Auxiliary class to play sound effect (support .wav or .mid file)
class MySoundEffect {

    private java.applet.AudioClip audio;

    public MySoundEffect(String filename) {
        try {
            java.io.File file = new java.io.File(filename);
            audio = java.applet.Applet.newAudioClip(file.toURL());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playOnce() {
        audio.play();
    }

    public void playLoop() {
        audio.loop();
    }

    public void stop() {
        audio.stop();
    }
}

// Array userData
class userData implements Comparable<userData> {

    private String name;
    public int item = 0, score = 0;

    userData(int id, String pn, int sc) {
        item = id;
        name = pn;
        score = sc;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public int getItem() {
        return item;
    }

    public void setItem(int it) {
        item = it;
    }

    public void setName(String sn) {
        name = sn;
    }

    public void setScore(int sc) {
        score = sc;
    }

    public void reportUser() {
        System.out.printf("%3d,%-15s %10d\n", item, name, score);
    }

    public int compareTo(userData other) {
        if (this.score <= other.score) {
            return 1;
        }
        if (this.score > other.score) {
            return -1;
        }
        return 0;
    }

}



