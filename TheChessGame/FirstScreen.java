/*  DEVELOPED BY    :                AVINASH AGARWAL  
 *  DATE                        :                21st February, 2014
 *  PLATFORM             :                JAVA 1.6.0_24-b07
 *   PROGRAM             :                The Chess Game
 */

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;

import java.awt.Color;

class FirstScreen implements ActionListener
{
            JButton host, join, singlePlayer;
            JTextField tf;
            JLabel label;           
            
            FirstScreen()
            {
                    host = new JButton();
                    SetUp.setUpButton(host, "Host A New Game",  "Host a new Game, where other players can join you with your IP address (Alt + H)",
                                                 100, 95, 200, 60, Color.BLACK, Color.WHITE, KeyEvent.VK_H, SetUp.host);
                                                 
                    join = new JButton();
                    SetUp.setUpButton(join, "Join a Game", "Join a chess game hosted by someone else with his IP address (Alt + J)",
                                               100, 175, 200, 60, Color.BLACK, Color.WHITE, KeyEvent.VK_J, SetUp.join);
                     
                    singlePlayer = new JButton();
                    SetUp.setUpButton(singlePlayer, "Single Player Game", "Play a match against the computer (Alt + S)",
                    									100, 255, 200, 60, Color.BLACK, Color.WHITE, KeyEvent.VK_S, SetUp.singlePlayer);

                    host.addActionListener(this);
                    join.addActionListener(this);
                    singlePlayer.addActionListener(this);
                    
                    SetUp.setUpFrame(SetUp.frame, "Welcome to The Chess Game!", 400, 430, SetUp.appImage, false, true);
                    
                    SetUp.panel[0] = new JPanel();
                    SetUp.setUpPanel(SetUp.panel[0], 0, 0, 400, 430, SetUp.sColor, SetUp.titledBorderSetup("The Chess Game vBETA", Color.YELLOW));
                    
                    tf = new JTextField();
                    SetUp.setUpTextField(tf, 160, 33, 200, 25, Color.BLACK, Color.WHITE, "Enter your name here.", true, true);
                    tFieldLen();
                    
                    label = new JLabel("Enter Name ", SwingConstants.CENTER);
                    SetUp.setUpLabel(label, "Enter Name ", 4, 20, 150, 50, Color.BLACK, Color.WHITE, SetUp.normal);
                    
                    SetUp.status = new JLabel("",SwingConstants.CENTER);
                    SetUp.setUpLabel(SetUp.status, "",  0, 330, 400, 50, Color.BLACK, Color.WHITE, SetUp.normal);
                   
                    SetUp.panel[0].add(label);
                    SetUp.panel[0].add(tf);
                    SetUp.panel[0].add(host);
                    SetUp.panel[0].add(join);
                    SetUp.panel[0].add(singlePlayer);
                    SetUp.panel[0].add(SetUp.status);
                    SetUp.frame.add(SetUp.panel[0]);
                    
                    tf.requestFocusInWindow();
                    SetUp.frame.setVisible(true);
            }
            
            
            public void actionPerformed(ActionEvent ae)
            {   
                        
                      if(goodInput())
                      {
                            SetUp.name = tf.getText().trim();
                            JButton source = (JButton)ae.getSource();
                            
                            if(source == host)
                            {   
                                    SetUp.isServer = true;
                                    SetUp.names[0] = SetUp.name;
                            }
                            else if(source == singlePlayer)
                            {
                            	SetUp.isServer = true;
                            	SetUp.isSinglePlayer = true;
                            	SetUp.names[0] = SetUp.name;
                            }
                            
                            new AskIP();                            
                      }       
            }
            
           
            
            
            
            private boolean goodInput()
            {
                    String p1 = tf.getText().trim();
                    UIManager.put("OptionPane.background", SetUp.sColor);
                    UIManager.put("Panel.background", SetUp.sColor);
                    UIManager.put("OptionPane.messageForeground", Color.WHITE);
                    UIManager.put("Button.background", Color.BLACK);
                    UIManager.put("Button.foreground", Color.WHITE);
                    
                    if(p1.length() > 0) return true;
                    SetUp.status.setText("Please enter a name");                                                                                      
                    return false;
            }
            
            
           private void tFieldLen()
           {
                 tf.addKeyListener(new KeyAdapter()
                                                              {
                                                                    public void keyTyped(KeyEvent ke)  
                                                                    {
                                                                             if(tf.getText().length() == 16)
                                                                            {
                                                                                   ke.consume();
                                                                            }
                                                                     }                                                                    
                                                              });
           }
                        
}