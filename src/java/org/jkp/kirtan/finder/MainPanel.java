package org.jkp.kirtan.finder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;

public class MainPanel extends JPanel{

	   private BufferedImage image;

	private static final long serialVersionUID = 7073872488017129168L;
	public MainPanel() {

        super(new BorderLayout());
	       try {                
		          image = ImageIO.read(this.getClass().getResource("mp3taggingLarge.png"));
		       } catch (IOException ex) {
		            // handle exception...
		       }
		       JLabel picLabel = new JLabel(new ImageIcon( image ));
		       add( picLabel );
		       
        String[] array = CompleteMaster.OfficalTitle;
        JComboBox combo = new JComboBox(array);
        combo.setEditable(true);
        combo.setSelectedIndex(-1);
        JTextField field = (JTextField)combo.getEditor().getEditorComponent();
        field.setText("");
        field.addKeyListener(new ComboKeyHandler(combo));

        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createTitledBorder("Type in some letters/words of Mp3 Kirtan. For Exact search put double quote \" before words"));
        p.add(combo, BorderLayout.NORTH);

        Box box = Box.createVerticalBox();
       // box.add(makeHelpPanel());
        box.add(Box.createVerticalStrut(5));
        box.add(p);
        add(box, BorderLayout.NORTH);
        setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        setSize(getPreferredSize());

        //setPreferredSize(new Dimension(320, 180));
    }
	
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override public void run() {
                createAndShowGUI();
            }
        });
    }
    public static void createAndShowGUI() {
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch(Exception e) {
            e.printStackTrace();
        }
        JFrame frame = new JFrame("Kirtan Tagging Project : TitleFinder ( Ver 1.6) by servant79@gmail.com");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(new MainPanel());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

class ComboKeyHandler extends KeyAdapter{
    private final JComboBox comboBox;
    private final Vector<String> list = new Vector<String>();
    public ComboKeyHandler(JComboBox combo) {
        this.comboBox = combo;
        for(int i=1;i<comboBox.getModel().getSize();i++) {
            list.addElement((String)comboBox.getItemAt(i));
        }
    }
    private boolean shouldHide = false;
    @Override public void keyTyped(final KeyEvent e) {
        EventQueue.invokeLater(new Runnable() {
            @Override public void run() {
                String text = ((JTextField)e.getSource()).getText();
                if(text.length()==0) {
                    setSuggestionModel(comboBox, new DefaultComboBoxModel(list), "");
                    comboBox.hidePopup();
                }else{
                    ComboBoxModel m = getSuggestedModel(list, text);
                    if(m.getSize()==0 || shouldHide) {
                        comboBox.hidePopup();
                    }else{
                        setSuggestionModel(comboBox, m, text);
                        comboBox.showPopup();
                    }
                }
            }
        });
    }
    @Override public void keyPressed(KeyEvent e) {
        JTextField textField = (JTextField)e.getSource();
        String text = textField.getText();
        shouldHide = false;
        switch(e.getKeyCode()) {
          case KeyEvent.VK_RIGHT:
            for(String s: list) {
                if(s.startsWith(text)) {
                    textField.setText(s);
                    return;
                }
            }
            break;
          case KeyEvent.VK_ENTER:
            if(!list.contains(text)) {
                list.addElement(text);
                Collections.sort(list);
                setSuggestionModel(comboBox, getSuggestedModel(list, text), text);
            }
            shouldHide = true;
            break;
          case KeyEvent.VK_ESCAPE:
            shouldHide = true;
            break;
        }
    }
    private static void setSuggestionModel(JComboBox comboBox, ComboBoxModel mdl, String str) {
        comboBox.setModel(mdl);
        comboBox.setSelectedIndex(-1);
        comboBox.setMaximumRowCount(40);
        ((JTextField)comboBox.getEditor().getEditorComponent()).setText(str);
    }
    
    public static ComboBoxModel getTestableSuggestedModel(Vector<String> list, String text){
    	return getSuggestedModel(list, text);
    }
    
    private static ComboBoxModel getSuggestedModel(Vector<String> list, String text) {
    	DefaultComboBoxModel m = new DefaultComboBoxModel();
        String tokens[]=null;
        if(text.charAt(0)=='"') {
        	tokens = new String[]{text.substring(1)};
        }else {
        	tokens= text.trim().split(" ");	
        }
         
        boolean include = true;
        for(String s: list) {
        	include=true;
        	for(String token: tokens){
        		if(!s.contains(token)){
        			include=false;
        		}
        	}
        	if(include){
        		m.addElement(s);
        	}
        }

        return m;
    }
}

