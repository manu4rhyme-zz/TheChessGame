/*  DEVELOPED BY    :                AVINASH AGARWAL  
 *  DATE                        :                20th February, 2014
 *  PLATFORM             :                JAVA 1.6.0_24-b07
 *   PROGRAM             :                The Chess Game
 */

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JTextPane;

import javax.swing.text.StyledDocument;
import javax.swing.text.StyleConstants;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.BadLocationException;

import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import javax.swing.border.TitledBorder;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.PrintWriter;
import java.io.BufferedReader;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Image;
import java.awt.Color;


class SetUp
{

        protected static final JFrame frame;
        protected static final JPanel[] panel;
        
        protected static String name;
        protected static String[] names;
        protected static String HostIP;
        protected static String myColor;
        protected static String[][] situation;
        
        protected static ImageIcon host, join, connect, white, black, bBishopb, bBishopw, bKnightb, bKnightw, bPawnb, bPawnw, bRookb, bRookw, bQueenb, bQueenw, bKingw,
                                                                      wBishopb, wBishopw, wKnightb, wKnightw, wPawnb, wPawnw, wRookb, wRookw, wQueenw, wQueenb, wKingb,
                                                                      bKingb, wKingw;
        protected static Image appImage;
        
        protected static ServerSocket serverSocket;
        protected static Socket[] clientSocket;
        protected static Socket socket;
        protected static PrintWriter pWs[];
        protected static BufferedReader br[];
        protected static int clientSocketNum;
        protected static int HostPort;  
        protected static int moveNum;
        
        protected static boolean isServer, hostClosed, isSpec, isServerRunning, isClientListening;
        
        protected static Color sColor;
        protected static Font normal;
        
        protected static JLabel status;
        
        protected static Color[] colors;
        
        static
        {
                frame = new JFrame();
                panel = new JPanel[5];
                
                host = imageSetUp("host.png");
                join = imageSetUp("join.png");
                connect = imageSetUp("connect.png");
                
                white = imageSetUp("basicSkin/white.png");
                black = imageSetUp("basicSkin/black.png");
                
                bBishopb = imageSetUp("basicSkin/blackBishopOnBlack.png");
                bBishopw = imageSetUp("basicSkin/blackBishopOnWhite.png");
                bKnightb = imageSetUp("basicSkin/blackKnightOnBlack.png");
                bKnightw = imageSetUp("basicSkin/blackKnightOnWhite.png");
                bPawnb = imageSetUp("basicSkin/blackPawnOnBlack.png");
                bPawnw = imageSetUp("basicSkin/blackPawnOnWhite.png");
                bRookb = imageSetUp("basicSkin/blackRookOnBlack.png");
                bRookw = imageSetUp("basicSkin/blackRookOnWhite.png");
                bQueenb = imageSetUp("basicSkin/blackQueenOnBlack.png");
                bQueenw = imageSetUp("basicSkin/blackQueenOnWhite.png");
                bKingw = imageSetUp("basicSkin/blackKingOnWhite.png");
                bKingb = imageSetUp("basicSkin/blackKingOnBlack.png");
                
                wBishopb = imageSetUp("basicSkin/whiteBishopOnBlack.png");
                wBishopw = imageSetUp("basicSkin/whiteBishopOnWhite.png");
                wKnightb = imageSetUp("basicSkin/whiteKnightOnBlack.png");
                wKnightw = imageSetUp("basicSkin/whiteKnightOnWhite.png");
                wPawnb = imageSetUp("basicSkin/whitePawnOnBlack.png");
                wPawnw = imageSetUp("basicSkin/whitePawnOnWhite.png");
                wRookb = imageSetUp("basicSkin/whiteRookOnBlack.png");
                wRookw = imageSetUp("basicSkin/whiteRookOnWhite.png");
                wQueenw = imageSetUp("basicSkin/whiteQueenOnWhite.png");
                wQueenb = imageSetUp("basicSkin/whiteQueenOnBlack.png");
                wKingb = imageSetUp("basicSkin/whiteKingOnBlack.png");  
                wKingw = imageSetUp("basicSkin/whiteKingOnWhite.png");
                
                appImage = Toolkit.getDefaultToolkit().getImage("appI.png");
                sColor = Color.DARK_GRAY.darker().darker();
                normal = new Font("Comic Sans MS", Font.PLAIN, 18);
                
                clientSocket = new Socket[50];
                clientSocketNum = 0;
                moveNum = 0;
                
                isServer = false;
                hostClosed = false;
                isSpec = false;
                
                status = new JLabel();
                
                name = "";                
                names = new String[101];
                
                situation = new String[8][8];
                
                pWs = new PrintWriter[100];
                br = new BufferedReader[100];
                
                isServerRunning = false;
                isClientListening = true;
                
                colors = new Color[8];
                colors[0] = new Color(181, 137 ,  0);
                colors[1] = new Color(203,  75 , 22);
                colors[2] = new Color(220,  50 , 47);
                colors[3] = new Color(211,  54 ,130);
                colors[4] = new Color(108, 113, 196);
                colors[5] = new Color(38, 139, 210);
                colors[6] = new Color(42, 161, 152);
                colors[7] = new Color(133, 153,   0);        
        }
        
        
        SetUp()
        {
                
        }

        
        protected static void setUpComboBox(JComboBox cb, int locX, int locY, int sizX, int sizY, Color b, Color f)
        {
            cb.setLocation(locX,locY);
            cb.setSize(sizX,sizY);
            cb.setBackground(b);
            cb.setForeground(f);
        }
        
        protected static void setUpButton(JButton button, String n, String tText, int locX, int locY, int X, int Y, Color b, Color f, int mne, ImageIcon i)
        {
                button.setText(n);
                button.setIcon(i);
                button.setLocation(locX,locY);
                button.setSize(X, Y);
                button.setBackground(b);
                button.setForeground(f);
                button.setToolTipText(tText);
                button.setMnemonic(mne);
        }
        
        
        protected static void setUpTextField(JTextField tf, int locX, int locY, int sizX, int sizY, Color b, Color f, String t, boolean ena, boolean frmMid)
        {
                tf.setLocation(locX, locY);
                tf.setSize(sizX, sizY);
                tf.setBackground(b);
                tf.setForeground(f);
                tf.setToolTipText(t);
                if(frmMid) tf.setHorizontalAlignment(0);
                tf.setEnabled(ena);
        }
        
        protected static void setUpLabel(JLabel l, String t, int locX, int locY, int sizX, int sizY, Color b, Color f, Font font)
        {
                l.setLocation(locX, locY);
                l.setSize(sizX, sizY);
                l.setFont(font);
                l.setText(t);
                l.setBackground(b);
                l.setForeground(f);
        }      
        
        protected static void setUpPanel(JPanel panel, int locX, int locY, int sizX, int sizY, Color b, TitledBorder tb)
        {
                panel.setLayout(null);
                panel.setBackground(b);
                panel.setLocation(locX, locY);
                panel.setSize(sizX, sizY);
                panel.setBorder(tb);
        }
                        
        
        protected static void setUpFrame(JFrame frame, String title, int sizX, int sizY, Image aI, boolean a, boolean b)
        {
                frame.setTitle(title);
                frame.setSize(sizX,sizY);
                frame.setLocationRelativeTo(null);
                frame.setResizable(a);
                frame.setIconImage(aI);
                if(b) frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
                else frame.setDefaultCloseOperation(frame.DO_NOTHING_ON_CLOSE);
        }        
        
        
        
        protected static ImageIcon imageSetUp(String path)
        {
                SetUp su = new SetUp();
                java.net.URL url = su.getClass().getResource(path);
                if (url != null)
                {
                    return new ImageIcon(url);
                }
                else
                System.out.println("Couldn't find the file : " + path);
                
                return null;
        }
        
        
        protected static TitledBorder titledBorderSetup(String title, Color c)
        {
                TitledBorder b = new TitledBorder(title);
                b.setTitleColor(c);
                return b;
        }
        
        
        protected static String getServerGroup(String a)
        {
            String b = "";
            byte dN = 0, i = 0;
        
            while(dN != 3)
            {
                if(a.charAt(i) == '.') dN++;           
                b += a.charAt(i++);
            }        
            b += "255";
            System.out.println(b);
            return b;
        }
        
        protected static String getServerIP()
        {
            java.net.InetAddress i = null;
            /*try
            {
                java.net.URL u = new java.net.URL("http://myip.xname.org/");

                java.net.HttpURLConnection h = (java.net.HttpURLConnection)(u.openConnection());
                java.io.BufferedReader br = new java.io.BufferedReader(new java.io.InputStreamReader(h.getInputStream()));
                ipadd = br.readLine();
                System.out.println(ipadd);
            }
            catch(Exception e)
            {}*/
            try
            {
                i = java.net.InetAddress.getLocalHost();
            }
            catch (java.net.UnknownHostException x)
            {
            }
            return ""+i.getHostAddress();
        }
        
        
        protected static void insertColorStrings(String s, Color c, boolean bold, JTextPane tp)
        {
            StyledDocument tpd = tp.getStyledDocument();
            Style style = tp.addStyle("I'm a Style", null);
            StyleConstants.setForeground(style, c);
            StyleConstants.setBold(style, bold);
            StyleConstants.setFontSize(style, 14);
            
            try
            {
                tpd.insertString(tpd.getLength(), s, style);
            }
            catch(BadLocationException e)
            {}
           /*tp.setEditable(false);
           tp.setText(tp.getText() + "\n" + s);
           MutableAttributeSet mas = tp.getInputAttributes();
           StyledDocument tpd = tp.getStyledDocument();
           
           StyleConstants.setForeground(mas, c);
           tpd.setCharacterAttributes((tp.getText().length()) - s.length() - 1, s.length() + 1, mas, false);*/
        }
}