/*  DEVELOPED BY    :                AVINASH AGARWAL  
 *  DATE                        :                1st April, 2011
 *  PLATFORM             :                JAVA 1.6.0_24-b07
 *   PROGRAM             :                Multiple User Chat Messenger
 */

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Vector;

class AskIP implements ActionListener
{

    private JTextField ip, port, colorTF;
    private JLabel IP, Port, color;
    private JButton button;
    private String[] gameNames, gameIPs, gamePorts;
    private JComboBox games;

    AskIP()
    {

        SetUp.panel[1] = new JPanel();
        SetUp.setUpPanel(SetUp.panel[1], 0, 0, 400, 325, SetUp.sColor, SetUp.titledBorderSetup("The Chess Game made by Avinash aka »Â†G«", Color.RED));

        IP = new JLabel("Enter IP ", SwingConstants.CENTER);
        SetUp.setUpLabel(IP, "Enter IP  ", 0, 32, 150, 50, Color.BLACK, Color.WHITE, SetUp.normal);

        Port = new JLabel("Enter Port ", SwingConstants.CENTER);
        SetUp.setUpLabel(Port, "Enter Port ", 0, 77, 150, 50, Color.BLACK, Color.WHITE, SetUp.normal);

        ip = new JTextField();
        SetUp.setUpTextField(ip,140, 45, 225, 25, Color.BLACK, Color.WHITE , "Enter the IP address of the host here.", true, true);

        port = new JTextField();
        SetUp.setUpTextField(port,140, 90, 225, 25, Color.BLACK, Color.WHITE , "Enter the Port Number of the host here.", true, true);

        SetUp.status = new JLabel("",SwingConstants.CENTER);
        SetUp.setUpLabel(SetUp.status, "", 0, 230, 400, 50, Color.BLACK, Color.WHITE, SetUp.normal);

        button = new JButton();
        button.addActionListener(this);

        SetUp.setUpButton(button, "Connect", "Connect to the entered IP on the given Port (Alt + C)",
            110, 155, 180, 60, Color.BLACK, Color.WHITE, KeyEvent.VK_C, SetUp.connect);

        tFieldNum(port, "`~!@#$%^&*()_+=\\|\"':;?/><,.- ");

        if(SetUp.isServer)
        {
            SetUp.panel[1].setSize(400,350);
            button.setLocation(110,190);
            SetUp.status.setLocation(0,260);
            
            //games = new JComboBox(gameNames);
            
            color = new JLabel("Enter Color ", SwingConstants.CENTER);
            SetUp.setUpLabel(color, "Enter Color  ", 0, 120, 150, 50, Color.BLACK, Color.WHITE, SetUp.normal);

            colorTF = new JTextField();
            SetUp.setUpTextField(colorTF ,140, 135, 225, 25, Color.BLACK, Color.WHITE , "Enter WHITE or BLACK.", true, true);

            SetUp.panel[1].add(color);
            SetUp.panel[1].add(colorTF);
        }
        else
        {
            ip.setText("192.168.50.1");
            port.setText("65483");
        }

        SetUp.panel[1].add(IP);
        SetUp.panel[1].add(ip);
        SetUp.panel[1].add(Port);
        SetUp.panel[1].add(port);
        SetUp.panel[1].add(button);
        SetUp.panel[1].add(SetUp.status);

        SetUp.frame.remove(SetUp.panel[0]);
        SetUp.frame.add(SetUp.panel[1]);
        SetUp.frame.setSize(400,325);
        if(SetUp.isServer) SetUp.frame.setSize(400,350);
        ip.requestFocusInWindow();
        SetUp.panel[1].repaint();

        if(!SetUp.isServer) tFieldNum(ip, "`~!@#$%^&*()_+=\\|\"':;?/><,- ");
        else 
        {   
            Thread t = new Thread(new ServerIP());
            t.start();
        }
    }

    public void actionPerformed(ActionEvent ae)
    {
        String a = goodInput();
        if(a == "")
        {
            SetUp.HostIP = ip.getText().trim();
            SetUp.HostPort = Integer.parseInt(port.getText().trim());
            ip.setText(SetUp.HostIP);
            port.setText(""+SetUp.HostPort);

            if(!SetUp.isServer) 
            {
                SetUp.status.setText("Connecting...");
                button.setEnabled(false);
                ip.setEditable(false);
                port.setEditable(false);
                Client c = new Client(button, port, ip); 
                Thread t = new Thread(c);
                t.start();                                
            }           
            else
            {   
                button.setEnabled(false);
                String x = (((colorTF.getText()).trim()).toUpperCase());
                if(x.equals("WHITE") || x.equals("BLACK"))
                {
                    SetUp.myColor = x;
                    Server ser = new Server(SetUp.HostPort, button, port);
                }
                else SetUp.status.setText("Invalid Color");
            }           
        } 
        else  SetUp.status.setText(a);
    }                                                                    

    private String goodInput()
    {
        String IP = ip.getText();
        String PORT = port.getText();

        if(PORT.length() > 0)
        {
            int x = Integer.parseInt(PORT);
            if(x < 65536) 
            {
                if(IP.length() >= 7) 
                {
                    int y = 0, z = 0;
                    String part = "";
                    for (int i = 0; i < ip.getText().length(); i++)
                    {
                        if(IP.charAt(i) == '.')
                        {
                            y++;
                            if(Integer.parseInt(part) > 255)
                            {
                                z++;
                                break;
                            }

                            part = "";
                        }        
                        else part += IP.charAt(i);
                    }         

                    if(y == 3 && z == 0) return "";
                    return "Invalid IP";
                }
                return "Invalid IP";
            }
            return "Invalid Port";
        }
        return "Invalid Port";
    }

    private void tFieldNum(final JTextField tf, final String badChars)
    {
        tf.addKeyListener(new KeyAdapter()
            {
                public void keyTyped(KeyEvent ke)  
                {
                    char code = ke.getKeyChar();

                    if((Character.isLetter(code) && ke.isAltDown() == false) || badChars.indexOf(code) > -1
                    || (tf == ip && ip.getText().length() == 15)  || (tf == port && port.getText().length() == 5))
                    {
                        ke.consume();
                    }
                }                                                                    
            });
    }

    private class ServerIP implements Runnable
    {
        public void run()
        {
            ip.setEnabled(false);
            button.setEnabled(false);
            port.setEnabled(false);
            colorTF.setEnabled(false);
            SetUp.status.setText("Please Wait...");
            String ipadd = "";
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
            {*/
            try
            {
                i = java.net.InetAddress.getLocalHost();
            }
            catch (java.net.UnknownHostException x)
            {
            }
            ipadd = ""+i.getHostAddress();
            //}

            ip.setText(ipadd);
            port.setText("65483");
            colorTF.setText("WHITE");
            SetUp.status.setText("Click Connect");
            port.setEnabled(true);
            ip.setEnabled(true);
            ip.setEditable(true);
            button.setEnabled(true);
        }
    }

}