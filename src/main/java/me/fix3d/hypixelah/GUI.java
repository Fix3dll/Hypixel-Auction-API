package me.fix3d.hypixelah;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class GUI {
    public JFrame frame;
    public JTextPane console;
    public JTextField input;
    public JScrollPane scrollpane;
    
    public StyledDocument document;
    public static String stext;
    
    public GUI() {
        frame = new JFrame();
        frame.setTitle("Hypixel Auction API");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        List<Image> icons  = new ArrayList();
        icons.add(new ImageIcon(getClass().getResource("/images/16x16.png")).getImage());
        icons.add(new ImageIcon(getClass().getResource("/images/32x32.png")).getImage());
        icons.add(new ImageIcon(getClass().getResource("/images/64x64.png")).getImage());
        icons.add(new ImageIcon(getClass().getResource("/images/128x128.png")).getImage());
        frame.setIconImages(icons);
            
        input = new JTextField();
        input.setEditable(true);
        input.setFont(new Font("monospaced", Font.PLAIN, 12));
        input.setForeground(Color.BLACK);
        input.setCaretColor(Color.BLACK);
        input.setOpaque(false);
        
        new CustomConsole(); //JTextPane
        
        try {
            print("Araştırmak istediğin eşyanın oyundaki tam adını gir: \n", 255, 255, 255);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        
        input.addActionListener((ActionEvent e) -> {
            String text = input.getText();
            if (text.length() > 1) {
                stext = text;
                
                scrollBottom();
                input.selectAll();
            }
        });

        /*
        input.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {}
            @Override
            public void keyReleased(KeyEvent e) {}
            @Override
            public void keyTyped(KeyEvent e) {}
        });
        */
        
        scrollpane = new JScrollPane(console);
        scrollpane.setBorder(null);
        scrollpane.setOpaque(false);
        scrollpane.getViewport().setOpaque(false);

        frame.add(input, BorderLayout.SOUTH);
        frame.add(scrollpane, BorderLayout.CENTER);

        frame.setSize(660, 371);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }
    
    public void scrollBottom() {
        console.setCaretPosition(console.getDocument().getLength());
    }
    
    public void print (String str, int x, int y, int z) throws BadLocationException {
        Style style = console.addStyle("Style", null);
        
        Matcher matchAmpersand = Pattern.compile("&").matcher(str);
        Matcher matchRGB = Pattern.compile("(?<=&\\().*?(?=\\))").matcher(str);
        Matcher matchStr = Pattern.compile("(?<=\\)).*?(.*?)&").matcher(str);
        
        int foundAmper = 0, foundRGB = 0, foundStr = 0;
        while (matchAmpersand.find()) {
            foundAmper++;
        } if (foundAmper == 0) {
            StyleConstants.setForeground(style, new Color(x, y, z));
            document.insertString(document.getLength(), str, style);
            return;
        }
        
        ArrayList rgbArray = new ArrayList();
        while (matchRGB.find()) {
            rgbArray.add(matchRGB.group());
            foundRGB++;
        }
        
        ArrayList strArray = new ArrayList();
        while (matchStr.find()) {
            strArray.add(matchStr.group().replace("&",""));
            foundStr++;
        }
        Matcher lastAmper = Pattern.compile("^(?:[^)]*\\)){"+foundAmper+"}([^*]*)").matcher(str);
        if(lastAmper.find()) {
            strArray.add(lastAmper.group(1));
        }
        
        boolean firstPrinted = false;
        for (int i = 0; i < foundAmper; i++) {
            String beforeAmper = null;
            Matcher firstAmper = Pattern.compile("(.*?)&").matcher(str);
            if (firstAmper.find()) {
                if (firstAmper.group().length() < 2 || firstPrinted) {
                    String[] RGB = rgbArray.get(i).toString().split(",");
                    x = parseInt(RGB[0]); y = parseInt(RGB[1]); z = parseInt(RGB[2]);
                    beforeAmper = strArray.get(i).toString();
                } else if (!firstPrinted) {
                    beforeAmper = firstAmper.group().replace("&","");
                    i--;
                    firstPrinted = true;
                }
            }
            StyleConstants.setForeground(style, new Color(x, y, z));
            document.insertString(document.getLength(), beforeAmper, style);
        } document.insertString(document.getLength(), "\n",style);
    }
    
    BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return resizedImage;
    }
    
    class CustomConsole extends JTextPane {
        public CustomConsole() {
            super();
            setEditable(false);
            setFont(new Font("monospaced", Font.PLAIN, 12));
            setOpaque(false);
            console = this;
            document = getStyledDocument();
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (ConfigManager.read("backgroundRGB").equals("")) {
                g.setColor(new Color(255,170,0));
            } else {
                String[] bgRGB = ConfigManager.read("backgroundRGB").split(",",4);
                int x = parseInt(bgRGB[0].trim());
                int y = parseInt(bgRGB[1].trim());
                int z = parseInt(bgRGB[2].trim());
                if (0 <= x && 255 >= x && 0 <= y && 255 >= y && 0 <= z && 255 >= z) {
                    g.setColor(new Color(x,y,z));
                } else {
                    JOptionPane.showMessageDialog(null,
                            "\"backgroundRGB\" yanlış!",
                            "Hata",
                            JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
                }
            }
            
            g.fillRect(0, 0, getWidth(), getHeight());

            Image img;
            if (ConfigManager.read("backgroundPath").equals("")) {
                img = new ImageIcon(getClass().getResource("/images/bg.png")).getImage();
            } else {
                img = new ImageIcon(ConfigManager.read("backgroundPath")).getImage();
            }
            g.drawImage(img, 0, 0, this);

            super.paintComponent(g);
        }
    }
}
