package irp;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import  javax.swing.*;

public class Panel extends JPanel
        implements ActionListener {

    static JFrame fMessage;
    Search getSearch = new Search();

    public String userPath = null;
    public JButton learningBtn = new JButton("Learner",
            createImageIcon("Icons/Open16.gif"));
    public JButton getAnswerBtn = new JButton("Search");
    public JButton learnedDataBtn = new JButton("Learned Files",
            createImageIcon("Icons/Open16.gif"));
    public JButton browseDataBtn = new JButton("Browse Text",
            createImageIcon("Icons/Open16.gif"));
    public JButton  evaluatorBtn = new JButton(" Evaluator");
    JTextArea txtArea = new JTextArea(24, 70);

    Panel() {
        learningBtn.addActionListener(this);
        learnedDataBtn.addActionListener(this);
        getAnswerBtn.addActionListener(this);
        browseDataBtn.addActionListener(this);
        evaluatorBtn.addActionListener(this);

        txtArea.setLineWrap(true);
        txtArea.setWrapStyleWord(true);
        JScrollPane sp = new JScrollPane(txtArea);
        sp.setBounds(10, 60, 780, 500);
        sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (size.getWidth());
        int height = (int) (size.getHeight());
        JFrame f = new JFrame("IRP");
//        JPanel panel = new JPanel();
//        panel.setBounds(10, 10, width / 2, height / 2);
//        panel.setBackground(Color.gray);

        learningBtn.setBounds(50, 100, 80, 30);
        learnedDataBtn.setBounds(50, 100, 80, 30);
        txtArea.setBounds(10, 30, 200, 200);
        getAnswerBtn.setBounds(100, 100, 80, 30);
        browseDataBtn.setBounds(100, 100, 80, 30);
        evaluatorBtn.setBounds(100, 100, 80, 30);

        //b1.setBackground(Color.yellow);
        JPanel panelTop = new JPanel(); // FlowLayout
        panelTop.add(learningBtn);
        panelTop.add(learnedDataBtn);
// add as many Components as you like to nested1
        JPanel panelMidle = new JPanel(); // FlowLayout
        panelMidle.add(sp);
// add as many Components as you like to nested2
        JPanel panelDown = new JPanel(); // FlowLayout
        panelDown.add(evaluatorBtn);
        panelDown.add(getAnswerBtn);
        panelDown.add(browseDataBtn);

        f.add(panelTop, BorderLayout.NORTH);
        f.add(panelMidle, BorderLayout.CENTER);
        f.add(panelDown, BorderLayout.SOUTH);
        f.setSize((int) (width / 1.5), (int) (height / 1.5));
        f.setLocation(width / 6, height / 6);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == learningBtn) {
            try {
                getLearningData();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Panel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Panel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (e.getSource() == learnedDataBtn) {
            try {
                getlearnedData();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Panel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Panel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (e.getSource() == getAnswerBtn) {
            //System.out.println(txtArea.getText());
            if (txtArea.getText() != null && !(txtArea.getText().isEmpty())) {
                getSearch.getAnswerViaTextArea(txtArea.getText());
            }else{
                JOptionPane.showMessageDialog(fMessage, ":-|");
            }
        }
        if (e.getSource() == browseDataBtn) {

            try {
                getSearch.getAnswerFile();
            } catch (IOException ex) {
                Logger.getLogger(Panel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (e.getSource() == evaluatorBtn) {

            try {
                if(Learner.gPath != null){
                EvaluationModel evaluator = new EvaluationModel();
                evaluator.evaluation(Learner.gPath);
                }else{
                    JOptionPane.showMessageDialog(fMessage, "Please train model or loade traned data.");
                }
            } catch (IOException ex) {
                Logger.getLogger(Panel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void getLearningData() throws FileNotFoundException, IOException {
        Path currentRelativePath = Paths.get("");
        String current_dir = currentRelativePath.toAbsolutePath().toString();
        JFileChooser dirChooser = new JFileChooser(current_dir);
        dirChooser.setDialogTitle("Select Taget Directory");
        dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int selectDirectoryAction = dirChooser.showDialog(dirChooser, "select");
        if (selectDirectoryAction != 1) {
            userPath = dirChooser.getCurrentDirectory().toString();
            Learner learner = new Learner();
            learner.myModelLearner(userPath);
        }
    }

    public void getlearnedData() throws FileNotFoundException, IOException {
        Path currentRelativePath = Paths.get("");
        String current_dir = currentRelativePath.toAbsolutePath().toString();
        JFileChooser dirChooser = new JFileChooser(current_dir);
        dirChooser.setDialogTitle("Select Taget Directory");
        // dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int selectDirectoryAction = dirChooser.showDialog(dirChooser, "select");
        if (selectDirectoryAction != 1) {
            userPath = dirChooser.getCurrentDirectory().toString();
            JOptionPane.showMessageDialog(dirChooser, userPath);
            GetTokens fetchTokens = new GetTokens(userPath);
        }
    }

    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = Panel.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}
