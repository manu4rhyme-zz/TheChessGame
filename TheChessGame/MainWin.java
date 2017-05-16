/*  DEVELOPED BY    :                AVINASH AGARWAL
 *  DATE                        :                25th February, 2014
 *  PLATFORM             :                JAVA 1.6.0_24-b07
 *   PROGRAM             :                The Chess Game
 */

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.io.IOException;

class MainWin implements Runnable, ActionListener
{

    protected JTextField inp;
    protected JScrollPane sp, sp2;
    protected JTextPane tp, tp2;
    protected JButton[][] situ;
    char myColorFV,opColorFV;
    boolean moved, underCheck;
    String selectedPiece;
    int sPCX, sPCY, cCM;
    protected Color ctpc;

    MainWin()
    {

        SetUp.frame.remove(SetUp.panel[1]);

        SetUp.panel[2] = new JPanel();
        SetUp.setUpPanel(SetUp.panel[2], 0, 0, 1240, 715, SetUp.sColor, SetUp.titledBorderSetup("The Chess Game vBETA", Color.GREEN));

        inp = new JTextField();
        SetUp.setUpTextField(inp,680, 650, 545, 25, Color.BLACK, Color.WHITE , "", true, false);
        keyInp();

        selectedPiece = "";

        tp = new JTextPane();
        sp = new JScrollPane(tp);

        tp2 = new JTextPane();
        sp2 = new JScrollPane(tp2);

        Font f = new Font("Tahoma", Font.PLAIN, 12);
        scrollPaneSetUp(sp, tp, 680,20,400,625, f, Color.BLACK);
        tp.setForeground(Color.WHITE);

        Font g = new Font("Comic Sans MS", Font.PLAIN, 14);
        scrollPaneSetUp(sp2, tp2, 1085,20,140,625, g ,Color.BLACK);
        tp2.setForeground(Color.WHITE);

        situ = new JButton[8][8];
        myColorFV = (char)(SetUp.myColor.charAt(0) + 32);

        if(myColorFV == 'b') opColorFV = 'w';
        else opColorFV = 'b';

        moved = false;

        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
                situ[i][j] = new JButton();
        }

        if(SetUp.myColor.equals("BLACK"))
            invertColors();

        for(int x = 15, i = 0; x <= 619; x += 80)
        {
            for(int y = 20, j = 0; y <= 619; y += 80)
            {
                ImageIcon temp = SetUp.black;
                SetUp.situation[i][j] = "b";

                if((i + j)%2 ==  0)
                {
                    temp = SetUp.white;
                    SetUp.situation[i][j] = "w";
                }

                situ[i][j] = new JButton();
                SetUp.setUpButton(situ[i][j], ""+i+" "+j, "this button is good",
                    x, y, 82, 82, new Color(0,25,30), new Color(0,25,30), KeyEvent.VK_C, temp);
                situ[i][j].setBorder(BorderFactory.createEmptyBorder());
                //situ[i][j].setContentAreaFilled(false);
                if(!SetUp.isSpec)situ[i][j].addActionListener(this);

                if(SetUp.myColor.equals("WHITE")) situ[i][j].setActionCommand(i + " " + j + " f");
                else situ[i][j].setActionCommand(" ");

                SetUp.panel[2].add(situ[i][j]);
                j++;
            }
            i++;
        }

        for(int y = 1, x = 0; x < 8; x++)
        {
            if(SetUp.situation[x][y].equals("b"))
            {
                situ[x][y].setIcon(SetUp.bPawnb);
                SetUp.situation[x][y] = "bPawnb";
            }
            else
            {
                situ[x][y].setIcon(SetUp.bPawnw);
                SetUp.situation[x][y] = "bPawnw";
            }
        }

        for(int y = 6, x = 0; x < 8; x++)
        {
            if(SetUp.situation[x][y].equals("b"))
            {
                situ[x][y].setIcon(SetUp.wPawnb);
                SetUp.situation[x][y] = "wPawnb";
            }
            else
            {
                situ[x][y].setIcon(SetUp.wPawnw);
                SetUp.situation[x][y] = "wPawnw";
            }
        }

        setStartingGame();

        if(SetUp.myColor.equals("BLACK"))
        {
            for(int i = 0; i < 8; i++)
            {
                for(int j = 0; j < 8; j++)
                {
                    if(SetUp.situation[i][j].length() > 1)
                    {
                        if(SetUp.situation[i][j].charAt(0) == 'w')
                            SetUp.situation[i][j] = "b" + SetUp.situation[i][j].substring(1,SetUp.situation[i][j].length());
                        else SetUp.situation[i][j] = "w" + SetUp.situation[i][j].substring(1,SetUp.situation[i][j].length());
                    }
                }
            }
        }
        SetUp.setUpFrame(SetUp.frame, "The Chess Game", 1240, 715, SetUp.appImage, false, true);

        SetUp.panel[2].add(inp);
        SetUp.panel[2].add(sp);
        SetUp.panel[2].add(sp2);

        SetUp.frame.add(SetUp.panel[2]);
        SetUp.panel[2].repaint();
        SetUp.frame.setResizable(false);

        if(SetUp.isServer)
        {   
            tp.setText("My IP : " + SetUp.HostIP + ", My Port : " + SetUp.HostPort + "\n");
            SetUp.insertColorStrings(SetUp.name + "\n", SetUp.colors[0], true, tp2);
            SetUp.names[0] = SetUp.name;
        }
        else tp.setText("Host IP : " + SetUp.HostIP + ", Host Port : " + SetUp.HostPort + "\n");

        exiter();
    }

    public void run()
    {

    }

    private void scrollPaneSetUp(JScrollPane sPane, JTextPane tPane, int locX, int locY, int sizX, int sizY,Font f, Color bColor)
    {
        sPane.setLocation(locX, locY);
        sPane.setSize(sizX, sizY);
        tPane.setFont(f);
        tPane.setBackground(bColor);
        tPane.setEditable(false);

    }

    private void keyInp()
    {
        inp.addKeyListener(new KeyAdapter()
            {
                public void keyPressed(KeyEvent ke)  
                {
                    if (ke.getKeyCode() == ke.VK_ENTER)
                    {
                        String st = inp.getText().trim();
                        if(st.length() > 0)
                        {                            
                            if(SetUp.isServer)
                                Server.sendToAll(st + "0", -1);
                            
                            else Client.pw.println(st+Client.myClientNum);
                            
                            SetUp.insertColorStrings("\n" + SetUp.name + " : ", ctpc, true, tp);
                            SetUp.insertColorStrings(st, ctpc,false, tp);
                        }                                                                                            
                        inp.setText("");                      
                    }
                }                                                                    
            });
    }

    protected void exiter()
    {
        SetUp.frame.addWindowListener(new WindowAdapter()
            {
                public void windowClosing(WindowEvent ev)
                {
                    try
                    {
                        if(SetUp.isServer)
                        {
                            for(int i = 0; i < SetUp.clientSocketNum; i++)
                            {
                                SetUp.pWs[i].println(" ");
                                SetUp.pWs[i].close();
                                SetUp.br[i].close();
                                SetUp.serverSocket.close();
                                SetUp.isServerRunning = false;
                            }
                        }
                        else
                        {
                            if(!SetUp.hostClosed) Client.pw.println(" ");
                            Client.pw.close();
                            Client.br.close();
                            SetUp.socket.close();
                        }

                        SetUp.frame.dispose();
                        System.exit(0);
                    }
                    catch(IOException e) { }
                }
            });

    }

    // MAIN STUFF
    public void actionPerformed(ActionEvent ae)
    {
        String x = ae.getActionCommand();
        if(x.equals(" ")) return;

        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                situ[i][j].setBackground(new Color(0,25,30));
                situ[i][j].setActionCommand(i + " " + j + " f");
            }
        }

        int m = (int)(x.charAt(0)) - 48; int n = (int)(x.charAt(2)) - 48 ;

        if(moved && (!(SetUp.situation[m][n].length() > 1 && SetUp.situation[m][n].charAt(0) == myColorFV)))
        {
            moved = false;
            if(x.charAt(x.length()-1) == 't') 
            {
                situ[sPCX][sPCY].setActionCommand(sPCX + " " + sPCY + " f");
                return;
            }

            char k = SetUp.situation[m][n].charAt(SetUp.situation[m][n].length() - 1);
            paintMoved(k, m , n, sPCX, sPCY);

            SetUp.situation[m][n] = myColorFV+selectedPiece+k;
            SetUp.situation[sPCX][sPCY] = (SetUp.situation[sPCX][sPCY].charAt(SetUp.situation[sPCX][sPCY].length()-1) == 'w') ? "w" : "b";

            if(SetUp.isServer) Server.sendToAll("  TCGv1.0 MOVE " + (7-m) + " " + (7-n) + " " + (7-sPCX) + " " + (7-sPCY), -1);
            else Client.pw.println("  TCGv1.0 MOVE " + (7-m) + " " + (7-n) + " " + (7-sPCX) + " " + (7-sPCY) +""+Client.myClientNum);

            for(int i = 0; i < 8; i++)
            {
                for(int j = 0; j < 8; j++)
                    situ[i][j].setActionCommand(" ");
            }

            return;
        }

        sPCX = (int)(x.charAt(0)) - 48; sPCY = (int)(x.charAt(2)) - 48;        
        System.out.println(sPCX + " " + sPCY);

        if(SetUp.situation[sPCX][sPCY].length() == 1 || SetUp.situation[sPCX][sPCY].charAt(0) == opColorFV) return;

        selectedPiece = SetUp.situation[sPCX][sPCY].substring(1,SetUp.situation[sPCX][sPCY].length() - 1);

        if(x.charAt(x.length()-1) == 't') 
        {
            situ[sPCX][sPCY].setActionCommand(sPCX + " " + sPCY + " f");
            moved = false;
            return;
        }

        moved = true;
        situ[sPCX][sPCY].setActionCommand(sPCX + " " + sPCY + " t");
        situ[sPCX][sPCY].setBackground(Color.GREEN);
        colorPossibleMoves(sPCX,sPCY,selectedPiece);
    }

    private void invertColors()
    {
        SetUp.bBishopb = SetUp.imageSetUp("basicSkin/whiteBishopOnBlack.png");
        SetUp.bBishopw = SetUp.imageSetUp("basicSkin/whiteBishopOnWhite.png");
        SetUp.bKnightb = SetUp.imageSetUp("basicSkin/whiteKnightOnBlack.png");
        SetUp.bKnightw = SetUp.imageSetUp("basicSkin/whiteKnightOnWhite.png");
        SetUp.bPawnb = SetUp.imageSetUp("basicSkin/whitePawnOnBlack.png");
        SetUp.bPawnw = SetUp.imageSetUp("basicSkin/whitePawnOnWhite.png");
        SetUp.bRookb = SetUp.imageSetUp("basicSkin/whiteRookOnBlack.png");
        SetUp.bRookw = SetUp.imageSetUp("basicSkin/whiteRookOnWhite.png");
        SetUp.bQueenb = SetUp.imageSetUp("basicSkin/whiteQueenOnBlack.png");
        SetUp.bQueenw = SetUp.imageSetUp("basicSkin/whiteQueenOnWhite.png");
        SetUp.bKingw = SetUp.imageSetUp("basicSkin/whiteKingOnWhite.png");
        SetUp.bKingb = SetUp.imageSetUp("basicSkin/whiteKingOnBlack.png");

        SetUp.wBishopb = SetUp.imageSetUp("basicSkin/blackBishopOnBlack.png");
        SetUp.wBishopw = SetUp.imageSetUp("basicSkin/blackBishopOnWhite.png");
        SetUp.wKnightb = SetUp.imageSetUp("basicSkin/blackKnightOnBlack.png");
        SetUp.wKnightw = SetUp.imageSetUp("basicSkin/blackKnightOnWhite.png");
        SetUp.wPawnb = SetUp.imageSetUp("basicSkin/blackPawnOnBlack.png");
        SetUp.wPawnw = SetUp.imageSetUp("basicSkin/blackPawnOnWhite.png");
        SetUp.wRookb = SetUp.imageSetUp("basicSkin/blackRookOnBlack.png");
        SetUp.wRookw = SetUp.imageSetUp("basicSkin/blackRookOnWhite.png");
        SetUp.wQueenw = SetUp.imageSetUp("basicSkin/blackQueenOnWhite.png");
        SetUp.wQueenb = SetUp.imageSetUp("basicSkin/blackQueenOnBlack.png");
        SetUp.wKingb = SetUp.imageSetUp("basicSkin/blackKingOnBlack.png"); 
        SetUp.wKingw = SetUp.imageSetUp("basicSkin/blackKingOnWhite.png");    

    }

    private void setStartingGame()
    {

        situ[0][0].setIcon(SetUp.bRookw);
        SetUp.situation[0][0] = "bRookw";
        situ[7][0].setIcon(SetUp.bRookb);
        SetUp.situation[7][0] = "bRookb";
        situ[0][7].setIcon(SetUp.wRookb);
        SetUp.situation[0][7] = "wRookb";
        situ[7][7].setIcon(SetUp.wRookw);
        SetUp.situation[7][7] = "wRookw";

        situ[1][0].setIcon(SetUp.bKnightb);
        SetUp.situation[1][0] = "bKnightb";
        situ[6][0].setIcon(SetUp.bKnightw);
        SetUp.situation[6][0] = "bKnightw";
        situ[1][7].setIcon(SetUp.wKnightw);
        SetUp.situation[1][7] = "wKnightw";
        situ[6][7].setIcon(SetUp.wKnightb);
        SetUp.situation[6][7] = "wKnightb";

        situ[2][0].setIcon(SetUp.bBishopw);
        SetUp.situation[2][0] = "bBishopw";
        situ[5][0].setIcon(SetUp.bBishopb);
        SetUp.situation[5][0] = "bBishopb";
        situ[2][7].setIcon(SetUp.wBishopb);
        SetUp.situation[2][7] = "wBishopb";
        situ[5][7].setIcon(SetUp.wBishopw);
        SetUp.situation[5][7] = "wBishopw";

        if(myColorFV == 'w')
        {
            situ[3][0].setIcon(SetUp.bQueenb);
            SetUp.situation[3][0] = "bQueenb";
            situ[4][0].setIcon(SetUp.bKingw);
            SetUp.situation[4][0] = "bKingw";
            situ[3][7].setIcon(SetUp.wQueenw);
            SetUp.situation[3][7] = "wQueenw";
            situ[4][7].setIcon(SetUp.wKingb);
            SetUp.situation[4][7] = "wKingb";
        }
        else
        {
            situ[3][0].setIcon(SetUp.bKingb);
            SetUp.situation[3][0] = "bKingb";
            situ[4][0].setIcon(SetUp.bQueenw);
            SetUp.situation[4][0] = "bQueenw";
            situ[3][7].setIcon(SetUp.wKingw);
            SetUp.situation[3][7] = "wKingw";
            situ[4][7].setIcon(SetUp.wQueenb);
            SetUp.situation[4][7] = "wQueenb";
        }
    }

    private void colorPossibleMoves(int spx, int spy, String piece)
    {        
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
                if(!(SetUp.situation[i][j].length() > 1 && SetUp.situation[i][j].charAt(0) == myColorFV))situ[i][j].setActionCommand(" ");
        }

        situ[spx][spy].setActionCommand(spx + " " + spy + " t");

        if(piece.equals("Pawn"))
        {   
            if(spy == 0) return;
            if((SetUp.situation)[spx][spy-1].length() == 1)
            {
                situ[spx][spy-1].setBackground(Color.ORANGE);
                situ[spx][spy-1].setActionCommand(spx + " " + (spy-1) + " f");
                
                
                if(spy == 6 && (SetUp.situation)[spx][spy-2].length() == 1)
                {
                    situ[spx][spy-2].setBackground(Color.ORANGE);
                    situ[spx][spy-2].setActionCommand(spx + " " + (spy-2) + " f");                    
                }
            }

            if(((spx+1) < 8 && (spy-1) >= 0) && ((SetUp.situation)[spx+1][spy-1].length() > 1 && ((SetUp.situation)[spx+1][spy-1]).charAt(0) == opColorFV))
            {
                situ[spx+1][spy-1].setBackground(Color.ORANGE);
                situ[spx+1][spy-1].setActionCommand((spx+1) + " " + (spy-1) + " f");
                
            }
            if(((spx-1) >= 0 && (spy-1) >= 0) && ((SetUp.situation)[spx-1][spy-1].length() > 1 && ((SetUp.situation)[spx-1][spy-1]).charAt(0) == opColorFV))
            {
                situ[spx-1][spy-1].setBackground(Color.ORANGE);
                situ[spx-1][spy-1].setActionCommand((spx-1) + " " + (spy-1) + " f");                
            }
        }

        else if(piece.equals("Knight"))
        {
            int m = spx + 1;
            int n = spy - 2;

            if((m < 8 && n >= 0) && (((SetUp.situation[m][n]).length() == 1) || (SetUp.situation[m][n]).charAt(0) == opColorFV))
            {
                situ[m][n].setBackground(Color.ORANGE);
                situ[m][n].setActionCommand(m + " " + n + " f");                
            }

            m = spx - 1;
            n = spy - 2;

            if((m >=0 && n >= 0) && (((SetUp.situation[m][n]).length() == 1) || (SetUp.situation[m][n]).charAt(0) == opColorFV))
            {
                situ[m][n].setBackground(Color.ORANGE);
                situ[m][n].setActionCommand(m + " " + n + " f");                
            }

            m = spx - 1;
            n = spy + 2;

            if((m >=0 && n < 8) && (((SetUp.situation[m][n]).length() == 1) || (SetUp.situation[m][n]).charAt(0) == opColorFV))
            {
                situ[m][n].setBackground(Color.ORANGE);
                situ[m][n].setActionCommand(m + " " + n + " f");                
            }

            m = spx + 1;
            n = spy + 2;

            if((m < 8 && n < 8) && (((SetUp.situation[m][n]).length() == 1) || (SetUp.situation[m][n]).charAt(0) == opColorFV))
            {
                situ[m][n].setBackground(Color.ORANGE);
                situ[m][n].setActionCommand(m + " " + n + " f");                
            }

            m = spx + 2;
            n = spy - 1;

            if((m < 8 && n >= 0) && (((SetUp.situation[m][n]).length() == 1) || (SetUp.situation[m][n]).charAt(0) == opColorFV))
            {
                situ[m][n].setBackground(Color.ORANGE);
                situ[m][n].setActionCommand(m + " " + n + " f");                
            }

            m = spx + 2;
            n = spy + 1;

            if((m < 8 && n < 8) && (((SetUp.situation[m][n]).length() == 1) || (SetUp.situation[m][n]).charAt(0) == opColorFV))
            {
                situ[m][n].setBackground(Color.ORANGE);
                situ[m][n].setActionCommand(m + " " + n + " f");                
            }

            m = spx - 2;
            n = spy + 1;

            if((m >= 0 && n < 8) && (((SetUp.situation[m][n]).length() == 1) || (SetUp.situation[m][n]).charAt(0) == opColorFV))
            {
                situ[m][n].setBackground(Color.ORANGE);
                situ[m][n].setActionCommand(m + " " + n + " f");                
            }

            m = spx - 2;
            n = spy - 1;

            if((m >= 0 && n >= 0) && (((SetUp.situation[m][n]).length() == 1) || (SetUp.situation[m][n]).charAt(0) == opColorFV))
            {
                situ[m][n].setBackground(Color.ORANGE);
                situ[m][n].setActionCommand(m + " " + n + " f");                
            }
        }

        else if(piece.equals("Bishop") || piece.equals("Queen"))
        {
            int m = spx+1, n = spy+1;
            while(m < 8 && n < 8)
            {
                int len = SetUp.situation[m][n].length();
                char cf = SetUp.situation[m][n].charAt(0);

                if(len > 1 && cf == myColorFV) break;

                situ[m][n].setBackground(Color.ORANGE);
                situ[m][n].setActionCommand(m + " " + n + " f");                
                
                if(len > 1 && cf == opColorFV) break;

                m++; n++;
            }

            m = spx-1; n = spy-1;

            while(m >= 0 && n >= 0)
            {
                int len = SetUp.situation[m][n].length();
                char cf = SetUp.situation[m][n].charAt(0);

                if(len > 1 && cf == myColorFV) break;

                situ[m][n].setBackground(Color.ORANGE);
                situ[m][n].setActionCommand(m + " " + n + " f");
                
                
                if(len > 1 && cf == opColorFV) break;

                m--; n--;
            }

            m = spx+1; n = spy-1;

            while(m < 8 && n >= 0)
            {
                int len = SetUp.situation[m][n].length();
                char cf = SetUp.situation[m][n].charAt(0);

                if(len > 1 && cf == myColorFV) break;

                situ[m][n].setBackground(Color.ORANGE);
                situ[m][n].setActionCommand(m + " " + n + " f");                
                
                if(len > 1 && cf == opColorFV) break;

                m++; n--;
            }

            m = spx-1; n = spy+1;

            while(m >= 0 && n < 8)
            {
                int len = SetUp.situation[m][n].length();
                char cf = SetUp.situation[m][n].charAt(0);

                if(len > 1 && cf == myColorFV) break;

                situ[m][n].setBackground(Color.ORANGE);
                situ[m][n].setActionCommand(m + " " + n + " f");                

                if(len > 1 && cf == opColorFV) break;

                m--; n++;
            }            
        }
        else if(piece.equals("King"))
        {   
            for(int i = -1; i < 2; i++)
            {
                for(int j = -1; j < 2; j++)
                {
                    int m = spx+i, n = spy +j;
                    if(m == spx && n == spy) continue;

                    if((m < 8 && m >= 0) && (n < 8 && n >= 0))
                    {
                        char cf = SetUp.situation[m][n].charAt(0);
                        int len = SetUp.situation[m][n].length();
                        if(len == 1 || cf != myColorFV)
                        {
                            situ[m][n].setBackground(Color.ORANGE);
                            situ[m][n].setActionCommand(m + " " + n + " f");                            
                        }
                    }
                }
            }
        }
        if(piece.equals("Rook") || piece.equals("Queen"))
        {
            int m = spx+1, n = spy;
            while(m < 8)
            {
                int len = SetUp.situation[m][n].length();
                char cf = SetUp.situation[m][n].charAt(0);

                if(len > 1 && cf == myColorFV) break;

                situ[m][n].setBackground(Color.ORANGE);
                situ[m][n].setActionCommand(m + " " + n + " f");                

                if(len > 1 && cf == opColorFV) break;

                m++;
            }

            m = spx; n = spy + 1;
            while(n < 8)
            {
                int len = SetUp.situation[m][n].length();
                char cf = SetUp.situation[m][n].charAt(0);

                if(len > 1 && cf == myColorFV) break;

                situ[m][n].setBackground(Color.ORANGE);
                situ[m][n].setActionCommand(m + " " + n + " f");                

                if(len > 1 && cf == opColorFV) break;

                n++;
            }

            m = spx-1; n = spy;

            while(m >= 0)
            {
                int len = SetUp.situation[m][n].length();
                char cf = SetUp.situation[m][n].charAt(0);

                if(len > 1 && cf == myColorFV) break;

                situ[m][n].setBackground(Color.ORANGE);
                situ[m][n].setActionCommand(m + " " + n + " f");                

                if(len > 1 && cf == opColorFV) break;

                m--;
            }

            m = spx; n = spy - 1;

            while(n >= 0)
            {
                int len = SetUp.situation[m][n].length();
                char cf = SetUp.situation[m][n].charAt(0);

                if(len > 1 && cf == myColorFV) break;

                situ[m][n].setBackground(Color.ORANGE);
                situ[m][n].setActionCommand(m + " " + n + " f");                

                if(len > 1 && cf == opColorFV) break;

                n--;
            }
        }
        
        
        int tx = 0, ty = 0;
        
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                if(situ[i][j].getBackground() == Color.ORANGE)
                {
                    String temp[][] = new String[8][8];
                    for(int k = 0; k < 8; k++)
                        System.arraycopy(SetUp.situation[k], 0, temp[k], 0, 8);
                        
                    System.out.println("ORANGE POSITIONS : " + i + " " + j);
                    
                    tx++;
                    temp[spx][spy] = (temp[spx][spy].charAt(temp[spx][spy].length()-1) == 'w') ? "w" : "b" ;
                    temp[i][j] = myColorFV + piece + temp[i][j].charAt(temp[i][j].length()-1);
                    if(isAttacked(temp)) 
                    {
                        System.out.println("CHECK ACCEPTED");
                        situ[i][j].setBackground(new Color(0,25,30));
                        situ[i][j].setActionCommand(" ");
                        ty++;
                    }
                }
            }
        }
        
        System.out.println("CCM b4 RM : " + tx + " CCM : " + (tx - ty));
        cCM = cCM + tx - ty;
    }

    protected void paintMoved(char code, int m, int n, int ix, int iy)
    {
        if(code == 'w')
        {
            if(selectedPiece.equals("Pawn"))
                situ[m][n].setIcon(SetUp.wPawnw);
            else if(selectedPiece.equals("Knight"))
                situ[m][n].setIcon(SetUp.wKnightw);
            else if(selectedPiece.equals("Bishop"))
                situ[m][n].setIcon(SetUp.wBishopw);
            else if(selectedPiece.equals("Rook"))
                situ[m][n].setIcon(SetUp.wRookw);
            else if(selectedPiece.equals("Queen"))
                situ[m][n].setIcon(SetUp.wQueenw);
            else if(selectedPiece.equals("King"))
                situ[m][n].setIcon(SetUp.wKingw);
        }
        else if(code == 'b')
        {
            if(selectedPiece.equals("Pawn"))
                situ[m][n].setIcon(SetUp.wPawnb);
            else if(selectedPiece.equals("Knight"))
                situ[m][n].setIcon(SetUp.wKnightb);
            else if(selectedPiece.equals("Bishop"))
                situ[m][n].setIcon(SetUp.wBishopb);
            else if(selectedPiece.equals("Rook"))
                situ[m][n].setIcon(SetUp.wRookb);
            else if(selectedPiece.equals("Queen"))
                situ[m][n].setIcon(SetUp.wQueenb);
            else if(selectedPiece.equals("King"))
                situ[m][n].setIcon(SetUp.wKingb);
        }

        if(SetUp.situation[ix][iy].charAt(SetUp.situation[ix][iy].length()-1) == 'w')
            situ[ix][iy].setIcon(SetUp.white);
        else situ[ix][iy].setIcon(SetUp.black);

        SetUp.panel[2].repaint();
    }

    protected void paintEnemyMoved(char code, int m, int n, int ix, int iy)
    {
        if(code == 'w')
        {
            if(selectedPiece.equals("Pawn"))
                situ[m][n].setIcon(SetUp.bPawnw);
            else if(selectedPiece.equals("Knight"))
                situ[m][n].setIcon(SetUp.bKnightw);
            else if(selectedPiece.equals("Bishop"))
                situ[m][n].setIcon(SetUp.bBishopw);
            else if(selectedPiece.equals("Rook"))
                situ[m][n].setIcon(SetUp.bRookw);
            else if(selectedPiece.equals("Queen"))
                situ[m][n].setIcon(SetUp.bQueenw);
            else if(selectedPiece.equals("King"))
                situ[m][n].setIcon(SetUp.bKingw);
        }
        else if(code == 'b')
        {
            if(selectedPiece.equals("Pawn"))
                situ[m][n].setIcon(SetUp.bPawnb);
            else if(selectedPiece.equals("Knight"))
                situ[m][n].setIcon(SetUp.bKnightb);
            else if(selectedPiece.equals("Bishop"))
                situ[m][n].setIcon(SetUp.bBishopb);
            else if(selectedPiece.equals("Rook"))
                situ[m][n].setIcon(SetUp.bRookb);
            else if(selectedPiece.equals("Queen"))
                situ[m][n].setIcon(SetUp.bQueenb);
            else if(selectedPiece.equals("King"))
                situ[m][n].setIcon(SetUp.bKingb);
        }

        if(SetUp.situation[ix][iy].charAt(SetUp.situation[ix][iy].length()-1) == 'w')
            situ[ix][iy].setIcon(SetUp.white);
        else situ[ix][iy].setIcon(SetUp.black);

        SetUp.panel[2].repaint();
    }

    protected boolean isAttacked(String config[][])
    {
        int kingX = 0 ,kingY = 0;

        for(int i = 0; i < 8; i++)
            for(int j = 0; j < 8; j++)
                if(config[i][j].length() > 1 && (config[i][j].substring(0,config[i][j].length() - 1)).equals(myColorFV + "King"))
                {
                    kingX = i;
                    kingY = j;
        }

        if(kingX == 0 && kingY == 0) return false;

        for(int i = kingX+1; i < 8; i++)
        {
            if(config[i][kingY].length() > 1)
            {
                if((config[i][kingY].charAt(0) == myColorFV)) break;
                String pc = (config[i][kingY].substring(1,config[i][kingY].length() - 1));
                if(pc.equals("Rook") || pc.equals("Queen")) return true;
                else break;
            }
        }

        for(int i = kingX-1; i >= 0; i--)
        {
            if(config[i][kingY].length() > 1)
            {
                String pc = (config[i][kingY].substring(1,config[i][kingY].length() - 1));
                if(config[i][kingY].charAt(0) == myColorFV) break;
                else if(pc.equals("Rook") || pc.equals("Queen")) return true;
                else break;
            }
        }

        for(int i = kingY+1; i < 8; i++)
        {

            if(config[kingX][i].length() > 1)
            {
                String pc = (config[kingX][i].substring(1,config[kingX][i].length() - 1));
                if(config[kingX][i].charAt(0) == myColorFV) break;
                else if(pc.equals("Rook") || pc.equals("Queen")) return true;
                else break;
            }
        }

        for(int i = kingY-1; i >= 0; i--)
        {
            if(config[kingX][i].length() > 1)
            {
                String pc = (config[kingX][i].substring(1,config[kingX][i].length() - 1));
                if(config[kingX][i].charAt(0) == myColorFV) break;
                else if(pc.equals("Rook") || pc.equals("Queen")) return true;
                else break;
            }
        }

        for(int i = kingX+1, j = kingY + 1; i < 8 && j < 8; i++,j++)
        {
            if(config[i][j].length() > 1)
            {
                String pc = (config[i][j].substring(1,config[i][j].length() - 1));
                if(config[i][j].charAt(0) == myColorFV) break;
                else if(pc.equals("Queen") || pc.equals("Bishop")) return true;
                else break;
            }
        }

        for(int i = kingX+1, j = kingY - 1; i < 8 && j >= 0; i++,j--)
        {
            if(config[i][j].length() > 1)
            {
                String pc = (config[i][j].substring(1,config[i][j].length() - 1));
                if(config[i][j].charAt(0) == myColorFV) break;
                else if(pc.equals("Queen") || pc.equals("Bishop")) return true;
                else break;
            }
        }

        for(int i = kingX-1, j = kingY + 1; i >= 0 && j < 8; i--,j++)
        {
            if(config[i][j].length() > 1)
            {
                String pc = (config[i][j].substring(1,config[i][j].length() - 1));
                if(config[i][j].charAt(0) == myColorFV) break;
                else if(pc.equals("Queen") || pc.equals("Bishop")) return true;
                else break;
            }
        }

        for(int i = kingX-1, j = kingY - 1; i >=0 && j >= 0; i--,j--)
        {
            if(config[i][j].length() > 1)
            {
                String pc = (config[i][j].substring(1,config[i][j].length() - 1));
                if(config[i][j].charAt(0) == myColorFV) break;
                else if(pc.equals("Queen") || pc.equals("Bishop")) return true;
                else break;
            }
        }

        if(kingY > 0)
        {
            if(kingX > 0)
                if((config[kingX-1][kingY-1].length() > 1) && ((config[kingX-1][kingY-1].substring(0,config[kingX-1][kingY-1].length()-1)).equals(opColorFV + "Pawn")))
                    return true;
            if(kingX < 7)
                if((config[kingX+1][kingY-1].length() > 1) && ((config[kingX+1][kingY-1].substring(0,config[kingX+1][kingY-1].length()-1)).equals(opColorFV + "Pawn")))
                    return true;
        }       

        
        if(((kingX > 1 && kingY > 0) && (config[kingX-2][kingY-1].length() > 1) && ((config[kingX-2][kingY-1].substring(0,config[kingX-2][kingY-1].length()-1)).equals(opColorFV + "Knight"))) || 
        ((kingX > 0 && kingY > 1) && (config[kingX-1][kingY-2].length() > 1) && ((config[kingX-1][kingY-2].substring(0,config[kingX-1][kingY-2].length()-1)).equals(opColorFV + "Knight"))) ||
        ((kingX < 7 && kingY > 1) && (config[kingX+1][kingY-2].length() > 1) && ((config[kingX+1][kingY-2].substring(0,config[kingX+1][kingY-2].length()-1)).equals(opColorFV + "Knight"))) ||
        ((kingX < 6 && kingY > 0) && (config[kingX+2][kingY-1].length() > 1) && ((config[kingX+2][kingY-1].substring(0,config[kingX+2][kingY-1].length()-1)).equals(opColorFV + "Knight"))) ||
        ((kingX > 1 && kingY < 7) && (config[kingX-2][kingY+1].length() > 1) && ((config[kingX-2][kingY+1].substring(0,config[kingX-2][kingY+1].length()-1)).equals(opColorFV + "Knight"))) ||
        ((kingX > 0 && kingY < 6) && (config[kingX-1][kingY+2].length() > 1) && ((config[kingX-1][kingY+2].substring(0,config[kingX-1][kingY+2].length()-1)).equals(opColorFV + "Knight"))) ||
        ((kingX < 7 && kingY < 6) && (config[kingX+1][kingY+2].length() > 1) && ((config[kingX+1][kingY+2].substring(0,config[kingX+1][kingY+2].length()-1)).equals(opColorFV + "Knight"))) ||
        ((kingX < 6 && kingY < 7) && (config[kingX+2][kingY+1].length() > 1) && ((config[kingX+2][kingY+1].substring(0,config[kingX+2][kingY+1].length()-1)).equals(opColorFV + "Knight"))))
            return true;

            
        if(((kingX > 0) && (config[kingX-1][kingY].length() > 1) && ((config[kingX-1][kingY].substring(0,config[kingX-1][kingY].length()-1)).equals(opColorFV + "King"))) || 
        ((kingX > 0 && kingY > 0) &&(config[kingX-1][kingY-1].length() > 1) && ((config[kingX-1][kingY-1].substring(0,config[kingX-1][kingY-1].length()-1)).equals(opColorFV + "King"))) ||
        ((kingY > 0) &&(config[kingX][kingY-1].length() > 1) && ((config[kingX][kingY-1].substring(0,config[kingX][kingY-1].length()-1)).equals(opColorFV + "King"))) ||
        ((kingX < 7 && kingY > 0) &&(config[kingX+1][kingY-1].length() > 1) && ((config[kingX+1][kingY-1].substring(0,config[kingX+1][kingY-1].length()-1)).equals(opColorFV + "King"))) ||
        ((kingX < 7) &&(config[kingX+1][kingY].length() > 1) && ((config[kingX+1][kingY].substring(0,config[kingX+1][kingY].length()-1)).equals(opColorFV + "King"))) ||
        ((kingX < 7 && kingY < 7) &&(config[kingX+1][kingY+1].length() > 1) && ((config[kingX+1][kingY+1].substring(0,config[kingX+1][kingY+1].length()-1)).equals(opColorFV + "King"))) ||
        ((kingY < 7) &&(config[kingX][kingY+1].length() > 1) && ((config[kingX][kingY+1].substring(0,config[kingX][kingY+1].length()-1)).equals(opColorFV + "King"))) ||
        ((kingX > 0 && kingY < 7) &&(config[kingX-1][kingY+1].length() > 1) && ((config[kingX-1][kingY+1].substring(0,config[kingX-1][kingY+1].length()-1)).equals(opColorFV + "King"))))
            return true;

        return false;
    }

    protected boolean isCheckMate(String config[][])
    {

        boolean ret = false;
        cCM = 0;
        for(int i = 0; i < 8; i++)
            for(int j = 0; j < 8; j++)
                if(config[i][j].length() > 1 && (config[i][j].charAt(0) == myColorFV))
                {
                   String pc = config[i][j].substring(1,config[i][j].length()-1);
                   colorPossibleMoves(i,j,pc);
                   //System.out.println("CCM = "+cCM);
                }
                
        if(cCM == 0)
            ret = true;
        
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                situ[i][j].setBackground(new Color(0,25,30));
                situ[i][j].setActionCommand(i + " " + j + " f");
            }
        }
        
        return ret;               
    }
}