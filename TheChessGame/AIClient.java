/*  DEVELOPED BY    :                AVINASH AGARWAL  
 *  DATE                        :                15th May, 2017
 *  PLATFORM             :                JAVA 1.6.0_24-b07
 *   PROGRAM             :                The Chess Game
 */

import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.awt.Color;


class AIClient implements Runnable
{
        protected static BufferedReader br;
        protected static PrintWriter pw;
        protected static int myClientNum;
        char playerColorFV, AIColorFV;
        
        AIClient()
        {
        	playerColorFV = (char)(SetUp.myColor.charAt(0) + 32);

            if(playerColorFV == 'b')
            	AIColorFV = 'w';
            else 
            	AIColorFV = 'b';

        }
               
        public void run()
        {
                System.out.println(SetUp.HostPort);
                try
                {
                        SetUp.socket = new Socket(SetUp.HostIP, SetUp.HostPort);
                }
                catch(UnknownHostException xx)
                {
                        xx.printStackTrace();
                        return;
                }
                catch(IOException e)
                {
                		e.printStackTrace();
                        return;
                }
                
                try
                {
                        br = new BufferedReader(new InputStreamReader(SetUp.socket.getInputStream()));
                        pw = new PrintWriter(SetUp.socket.getOutputStream(), true);
                }
                catch(IOException e)
                {
                		e.printStackTrace();
                		return;
                }
                SetUp.isClientListening = false;
                
                pw.println("Computer");
                
                Thread z = new Thread(new Listen());
                z.start();               
        }
        
        protected class Listen implements Runnable
        {
                private int k, RECURSION_DEPTH = 2;

                class Action
                {
                    int ix,iy,fx,fy;
                    public Action(int a,int b, int c,int d)
                    {
                        ix = a;
                        iy = b;
                        fx = c;
                        fy = d;
                    }
                }

                public void run()
                {
                         String incoming = ""; 
                         int count = 0;
                         k = 0;
                         String names = "";
                         
                         while(true)
                         {
                                incoming = null;
                                try
                                {
                                    incoming = br.readLine();
                                }
                                catch(IOException e){}
                                
                                if(incoming == null) 
                                    continue;

                                switch(count)
                                {
                                        case 0 : names = incoming;
                                                 break;
                                                       
                                        case 1 : myClientNum = Integer.parseInt(incoming);
                                                    break;
                                                       
                                        case 2 : System.out.println(count);
                                                 System.out.println(incoming);
                                                 /*if(incoming.equals("SPECTATOR"))
                                                 {
                                                     SetUp.isSpec = true;
                                                     SetUp.playerColor = "WHITE";
                                                 }
                                                 else SetUp.playerColor = incoming;
                                                 */                             
                                                 break;
                                                       
                                        default : if(incoming.equals(" "))
                                                  {
                                                        System.exit(0);
                                                  }
                                                  else if(incoming.equals("             YOU WIN"))
                                                  {
                                                        //AI Win Behaviour
                                                        pw.println("LOL ! NOOB" +""+myClientNum);
                                                  }
                                                  else if((incoming.substring(0,2)).equals("  "))
                                                  {
                                                      //AI's turn
                                                      //State in Setup
                                                      Action aiact = minimaxDecision();
                                                      pw.println("  TCGv1.0 MOVE " + aiact.fx + " " + aiact.fy + " " + aiact.ix + " " + aiact.iy +""+myClientNum);
                                                      pw.println(aiact.fx + " " + aiact.fy + " " + aiact.ix + " " + aiact.iy +""+myClientNum);
                                                  }                    
                                                       
                                }
                                count++;
                        }
                }

                protected Action minimaxDecision()
                {
                    double val = Double.NEGATIVE_INFINITY, gval, alpha, beta;
                    Action act = new Action(-1,-1,-1,-1);
                    Vector<Action> acts = getAIMoves();
                    beta = Double.MAX_VALUE;
                    alpha = Double.NEGATIVE_INFINITY;

                    for(Action cact : acts)
                    {
                        String old1 = SetUp.situation[cact.ix][cact.iy], old2 = SetUp.situation[cact.fx][cact.fy];
                        SetUp.situation[cact.fx][cact.fy] = SetUp.situation[cact.ix][cact.iy].substring(0,SetUp.situation[cact.ix][cact.iy].length()-1)+SetUp.situation[cact.fx][cact.fy].charAt(SetUp.situation[cact.fx][cact.fy].length()-1);
                        SetUp.situation[cact.ix][cact.iy] = ""+SetUp.situation[cact.ix][cact.iy].charAt(SetUp.situation[cact.ix][cact.iy].length()-1);

                        gval = MN_VALUE(0,alpha,beta);
                        if(gval > val)
                        {
                            val = gval;
                            act = cact;
                        }
                        SetUp.situation[cact.fx][cact.fy] = old2;
                        SetUp.situation[cact.ix][cact.iy] = old1;
                        alpha = Math.max(val,alpha);
                        if(beta <= alpha)
                            return act;
                    }

                    return act;
                }

                protected double MN_VALUE(int cdepth, double alpha, double beta)
                {
                    double val, gval;
                    if(cdepth == RECURSION_DEPTH)
                    {
                        //Util value code
                        return getUtilityValue();
                    }
                    val = Double.MAX_VALUE;
                    Action act = new Action(-1,-1,-1,-1);
                    Vector<Action> acts = getPlayerMoves();

                    for(Action cact : acts)
                    {
                        String old1 = SetUp.situation[cact.ix][cact.iy], old2 = SetUp.situation[cact.fx][cact.fy];
                        SetUp.situation[cact.fx][cact.fy] = SetUp.situation[cact.ix][cact.iy].substring(0,SetUp.situation[cact.ix][cact.iy].length()-1)+SetUp.situation[cact.fx][cact.fy].charAt(SetUp.situation[cact.fx][cact.fy].length()-1);
                        SetUp.situation[cact.ix][cact.iy] = ""+SetUp.situation[cact.ix][cact.iy].charAt(SetUp.situation[cact.ix][cact.iy].length()-1);

                        gval = MX_VALUE(cdepth,alpha,beta);
                        if(gval < val)
                        {
                            val = gval;
                            act = cact;
                        }
                        SetUp.situation[cact.fx][cact.fy] = old2;
                        SetUp.situation[cact.ix][cact.iy] = old1;
                        beta = Math.min(beta,val);
                        if(beta <= alpha)
                            return val;
                    }

                    return val;
                }

                protected double MX_VALUE(int cdepth, double alpha, double beta)
                {
                    double val = Double.NEGATIVE_INFINITY, gval;
                    Action act = new Action(-1,-1,-1,-1);
                    Vector<Action> acts = getAIMoves();

                    for(Action cact : acts)
                    {
                        String old1 = SetUp.situation[cact.ix][cact.iy], old2 = SetUp.situation[cact.fx][cact.fy];
                        SetUp.situation[cact.fx][cact.fy] = SetUp.situation[cact.ix][cact.iy].substring(0,SetUp.situation[cact.ix][cact.iy].length()-1)+SetUp.situation[cact.fx][cact.fy].charAt(SetUp.situation[cact.fx][cact.fy].length()-1);
                        SetUp.situation[cact.ix][cact.iy] = ""+SetUp.situation[cact.ix][cact.iy].charAt(SetUp.situation[cact.ix][cact.iy].length()-1);

                        gval = MN_VALUE(cdepth+1,alpha,beta);
                        if(gval > val)
                        {
                            val = gval;
                            act = cact;
                        }
                        SetUp.situation[cact.fx][cact.fy] = old2;
                        SetUp.situation[cact.ix][cact.iy] = old1;
                        alpha = Math.max(alpha,val);
                        if(beta <= alpha)
                            return val;
                    }

                    return val;
                }

                protected double getUtilityValue()
                {
                    double val = 0.0;
                    for(int i = 0; i < 8; ++i)
                    {
                        for(int j = 0; j < 8; ++j)
                        {
                            if(SetUp.situation[i][j].length() == 1)
                                continue;
                            double tval=0;
                            switch(SetUp.situation[i][j].substring(1,SetUp.situation[i][j].length()-1))
                            {
                                case "Pawn" : tval = 10;
                                              break;
                                case "Knight" : tval = 30;
                                              break;
                                case "Bishop" : tval = 30;
                                              break;
                                case "Rook" : tval = 50;
                                              break;
                                case "Queen" : tval = 90;
                                              break;
                                case "King" : tval = 900;
                                              break;
                            }
                            if(SetUp.situation[i][j].charAt(0) == playerColorFV)
                                tval *= -1;
                            val += tval;
                        }
                    }
                    //System.out.println(val);
                    return val;
                }

                protected Vector<Action> getPlayerMoves()
                {
                    Vector<Action> ans = new Vector<Action>();

                    for(int i = 0; i < 8; ++i)
                    {
                        for(int j = 0; j < 8; ++j)
                        {
                            if(SetUp.situation[i][j].length() == 1)
                                continue;
                            if(SetUp.situation[i][j].charAt(0) == AIColorFV)
                                continue;

                            String piece = SetUp.situation[i][j].substring(1,SetUp.situation[i][j].length()-1);

                            if(piece.equals("Pawn"))
                            {
                                if(j == 0) break;
                                if((SetUp.situation)[i][j-1].length() == 1)
                                {
                                    ans.add(new Action(i,j,i,j-1));
                                    
                                    if(j == 6 && (SetUp.situation)[i][j-2].length() == 1)
                                        ans.add(new Action(i,j,i,j-2));                   
                                }
                    
                                if(((i+1) < 8 && (j-1) >= 0) && ((SetUp.situation)[i+1][j-1].length() > 1 && ((SetUp.situation)[i+1][j-1]).charAt(0) == AIColorFV))
                                {
                                    ans.add(new Action(i,j,i+1,j-1));                                    
                                }
                                if(((i-1) >= 0 && (j-1) >= 0) && ((SetUp.situation)[i-1][j-1].length() > 1 && ((SetUp.situation)[i-1][j-1]).charAt(0) == AIColorFV))
                                {
                                    ans.add(new Action(i,j,i-1,j-1));               
                                }
                            }
                            else if(piece.equals("Knight"))
                            {
                                int m = i + 1;
                                int n = j - 2;
                    
                                if((m < 8 && n >= 0) && (((SetUp.situation[m][n]).length() == 1) || (SetUp.situation[m][n]).charAt(0) == AIColorFV))
                                {
                                    ans.add(new Action(i,j,m,n));                
                                }
                    
                                m = i - 1;
                                n = j - 2;
                    
                                if((m >=0 && n >= 0) && (((SetUp.situation[m][n]).length() == 1) || (SetUp.situation[m][n]).charAt(0) == AIColorFV))
                                {
                                    ans.add(new Action(i,j,m,n));                
                                }
                    
                                m = i - 1;
                                n = j + 2;
                    
                                if((m >=0 && n < 8) && (((SetUp.situation[m][n]).length() == 1) || (SetUp.situation[m][n]).charAt(0) == AIColorFV))
                                {
                                    ans.add(new Action(i,j,m,n));                 
                                }
                    
                                m = i + 1;
                                n = j + 2;
                    
                                if((m < 8 && n < 8) && (((SetUp.situation[m][n]).length() == 1) || (SetUp.situation[m][n]).charAt(0) == AIColorFV))
                                {
                                    ans.add(new Action(i,j,m,n));                 
                                }
                    
                                m = i + 2;
                                n = j - 1;
                    
                                if((m < 8 && n >= 0) && (((SetUp.situation[m][n]).length() == 1) || (SetUp.situation[m][n]).charAt(0) == AIColorFV))
                                {
                                    ans.add(new Action(i,j,m,n));                 
                                }
                    
                                m = i + 2;
                                n = j + 1;
                    
                                if((m < 8 && n < 8) && (((SetUp.situation[m][n]).length() == 1) || (SetUp.situation[m][n]).charAt(0) == AIColorFV))
                                {
                                    ans.add(new Action(i,j,m,n));                 
                                }
                    
                                m = i - 2;
                                n = j + 1;
                    
                                if((m >= 0 && n < 8) && (((SetUp.situation[m][n]).length() == 1) || (SetUp.situation[m][n]).charAt(0) == AIColorFV))
                                {
                                    ans.add(new Action(i,j,m,n));                 
                                }
                    
                                m = i - 2;
                                n = j - 1;
                    
                                if((m >= 0 && n >= 0) && (((SetUp.situation[m][n]).length() == 1) || (SetUp.situation[m][n]).charAt(0) == AIColorFV))
                                {
                                    ans.add(new Action(i,j,m,n));                 
                                }
                            }
                            else if(piece.equals("Bishop") || piece.equals("Queen"))
                            {
                                int m = i+1, n = j+1;
                                while(m < 8 && n < 8)
                                {
                                    int len = SetUp.situation[m][n].length();
                                    char cf = SetUp.situation[m][n].charAt(0);
                    
                                    if(len > 1 && cf == playerColorFV) break;
                    
                                    ans.add(new Action(i,j,m,n));                
                                    
                                    if(len > 1 && cf == AIColorFV) break;
                    
                                    m++; n++;
                                }
                    
                                m = i-1; n = j-1;
                    
                                while(m >= 0 && n >= 0)
                                {
                                    int len = SetUp.situation[m][n].length();
                                    char cf = SetUp.situation[m][n].charAt(0);
                    
                                    if(len > 1 && cf == playerColorFV) break;
                    
                                    ans.add(new Action(i,j,m,n));                                    
                                    
                                    if(len > 1 && cf == AIColorFV) break;
                    
                                    m--; n--;
                                }
                    
                                m = i+1; n = j-1;
                    
                                while(m < 8 && n >= 0)
                                {
                                    int len = SetUp.situation[m][n].length();
                                    char cf = SetUp.situation[m][n].charAt(0);
                    
                                    if(len > 1 && cf == playerColorFV) break;
                    
                                    ans.add(new Action(i,j,m,n));                
                                    
                                    if(len > 1 && cf == AIColorFV) break;
                    
                                    m++; n--;
                                }
                    
                                m = i-1; n = j+1;
                    
                                while(m >= 0 && n < 8)
                                {
                                    int len = SetUp.situation[m][n].length();
                                    char cf = SetUp.situation[m][n].charAt(0);
                    
                                    if(len > 1 && cf == playerColorFV) break;
                    
                                    ans.add(new Action(i,j,m,n));               
                    
                                    if(len > 1 && cf == AIColorFV) break;
                    
                                    m--; n++;
                                }
                            }
                            else if(piece.equals("King"))
                            {
                                for(int ii = -1; ii < 2; ii++)
                                {
                                    for(int jj = -1; jj < 2; jj++)
                                    {
                                        int m = i+ii, n = j + jj;
                                        if(m == i && n == j) continue;
                    
                                        if((m < 8 && m >= 0) && (n < 8 && n >= 0))
                                        {
                                            char cf = SetUp.situation[m][n].charAt(0);
                                            int len = SetUp.situation[m][n].length();
                                            if(len == 1 || cf != playerColorFV)
                                            {
                                                ans.add(new Action(i,j,m,n));                            
                                            }
                                        }
                                    }
                                }
                            }
                            else if(piece.equals("Rook") || piece.equals("Queen"))
                            {
                                int m = i+1, n = j;
                                while(m < 8)
                                {
                                    int len = SetUp.situation[m][n].length();
                                    char cf = SetUp.situation[m][n].charAt(0);
                    
                                    if(len > 1 && cf == playerColorFV) break;
                    
                                    ans.add(new Action(i,j,m,n));                
                    
                                    if(len > 1 && cf == AIColorFV) break;
                    
                                    m++;
                                }
                    
                                m = i; n = j + 1;
                                while(n < 8)
                                {
                                    int len = SetUp.situation[m][n].length();
                                    char cf = SetUp.situation[m][n].charAt(0);
                    
                                    if(len > 1 && cf == playerColorFV) break;
                    
                                    ans.add(new Action(i,j,m,n));                
                    
                                    if(len > 1 && cf == AIColorFV) break;
                    
                                    n++;
                                }
                    
                                m = i-1; n = j;
                    
                                while(m >= 0)
                                {
                                    int len = SetUp.situation[m][n].length();
                                    char cf = SetUp.situation[m][n].charAt(0);
                    
                                    if(len > 1 && cf == playerColorFV) break;
                    
                                    ans.add(new Action(i,j,m,n));                
                    
                                    if(len > 1 && cf == AIColorFV) break;
                    
                                    m--;
                                }
                    
                                m = i; n = j - 1;
                    
                                while(n >= 0)
                                {
                                    int len = SetUp.situation[m][n].length();
                                    char cf = SetUp.situation[m][n].charAt(0);
                    
                                    if(len > 1 && cf == playerColorFV) break;
                    
                                    ans.add(new Action(i,j,m,n));                
                    
                                    if(len > 1 && cf == AIColorFV) break;
                    
                                    n--;
                                }
                            }

                        }
                    }
                    return ans;
                }

                protected Vector<Action> getAIMoves()
                {
                    Vector<Action> ans = new Vector<Action>();

                    for(int i = 0; i < 8; ++i)
                    {
                        for(int j = 0; j < 8; ++j)
                        {
                            if(SetUp.situation[i][j].length() == 1)
                                continue;
                            if(SetUp.situation[i][j].charAt(0) == playerColorFV)
                                continue;

                            String piece = SetUp.situation[i][j].substring(1,SetUp.situation[i][j].length()-1);

                            if(piece.equals("Pawn"))
                            {
                                if(j == 7) break;
                                if((SetUp.situation)[i][j+1].length() == 1)
                                {
                                    ans.add(new Action(i,j,i,j+1));
                                    
                                    if(j == 1 && (SetUp.situation)[i][j+2].length() == 1)
                                        ans.add(new Action(i,j,i,j+2));                   
                                }
                    
                                if(((i+1) < 8 && (j+1) >= 0) && ((SetUp.situation)[i+1][j+1].length() > 1 && ((SetUp.situation)[i+1][j+1]).charAt(0) == playerColorFV))
                                {
                                    ans.add(new Action(i,j,i+1,j+1));                                    
                                }
                                if(((i-1) >= 0 && (j+1) >= 0) && ((SetUp.situation)[i-1][j+1].length() > 1 && ((SetUp.situation)[i-1][j+1]).charAt(0) == playerColorFV))
                                {
                                    ans.add(new Action(i,j,i-1,j+1));               
                                }
                            }
                            else if(piece.equals("Knight"))
                            {
                                int m = i + 1;
                                int n = j - 2;
                    
                                if((m < 8 && n >= 0) && (((SetUp.situation[m][n]).length() == 1) || (SetUp.situation[m][n]).charAt(0) == playerColorFV))
                                {
                                    ans.add(new Action(i,j,m,n));                
                                }
                    
                                m = i - 1;
                                n = j - 2;
                    
                                if((m >=0 && n >= 0) && (((SetUp.situation[m][n]).length() == 1) || (SetUp.situation[m][n]).charAt(0) == playerColorFV))
                                {
                                    ans.add(new Action(i,j,m,n));                
                                }
                    
                                m = i - 1;
                                n = j + 2;
                    
                                if((m >=0 && n < 8) && (((SetUp.situation[m][n]).length() == 1) || (SetUp.situation[m][n]).charAt(0) == playerColorFV))
                                {
                                    ans.add(new Action(i,j,m,n));                 
                                }
                    
                                m = i + 1;
                                n = j + 2;
                    
                                if((m < 8 && n < 8) && (((SetUp.situation[m][n]).length() == 1) || (SetUp.situation[m][n]).charAt(0) == playerColorFV))
                                {
                                    ans.add(new Action(i,j,m,n));                 
                                }
                    
                                m = i + 2;
                                n = j - 1;
                    
                                if((m < 8 && n >= 0) && (((SetUp.situation[m][n]).length() == 1) || (SetUp.situation[m][n]).charAt(0) == playerColorFV))
                                {
                                    ans.add(new Action(i,j,m,n));                 
                                }
                    
                                m = i + 2;
                                n = j + 1;
                    
                                if((m < 8 && n < 8) && (((SetUp.situation[m][n]).length() == 1) || (SetUp.situation[m][n]).charAt(0) == playerColorFV))
                                {
                                    ans.add(new Action(i,j,m,n));                 
                                }
                    
                                m = i - 2;
                                n = j + 1;
                    
                                if((m >= 0 && n < 8) && (((SetUp.situation[m][n]).length() == 1) || (SetUp.situation[m][n]).charAt(0) == playerColorFV))
                                {
                                    ans.add(new Action(i,j,m,n));                 
                                }
                    
                                m = i - 2;
                                n = j - 1;
                    
                                if((m >= 0 && n >= 0) && (((SetUp.situation[m][n]).length() == 1) || (SetUp.situation[m][n]).charAt(0) == playerColorFV))
                                {
                                    ans.add(new Action(i,j,m,n));                 
                                }
                            }
                            else if(piece.equals("Bishop") || piece.equals("Queen"))
                            {
                                int m = i+1, n = j+1;
                                while(m < 8 && n < 8)
                                {
                                    int len = SetUp.situation[m][n].length();
                                    char cf = SetUp.situation[m][n].charAt(0);
                    
                                    if(len > 1 && cf == AIColorFV) break;
                    
                                    ans.add(new Action(i,j,m,n));                
                                    
                                    if(len > 1 && cf == playerColorFV) break;
                    
                                    m++; n++;
                                }
                    
                                m = i-1; n = j-1;
                            
                                while(m >= 0 && n >= 0)
                                {
                                    int len = SetUp.situation[m][n].length();
                                    char cf = SetUp.situation[m][n].charAt(0);
                    
                                    if(len > 1 && cf == AIColorFV) break;
                    
                                    ans.add(new Action(i,j,m,n));                                    
                                    
                                    if(len > 1 && cf == playerColorFV) break;
                    
                                    m--; n--;
                                }
                    
                                m = i+1; n = j-1;
                    
                                while(m < 8 && n >= 0)
                                {
                                    int len = SetUp.situation[m][n].length();
                                    char cf = SetUp.situation[m][n].charAt(0);
                    
                                    if(len > 1 && cf == AIColorFV) break;
                    
                                    ans.add(new Action(i,j,m,n));                
                                    
                                    if(len > 1 && cf == playerColorFV) break;
                    
                                    m++; n--;
                                }
                    
                                m = i-1; n = j+1;
                    
                                while(m >= 0 && n < 8)
                                {
                                    int len = SetUp.situation[m][n].length();
                                    char cf = SetUp.situation[m][n].charAt(0);
                    
                                    if(len > 1 && cf == AIColorFV) break;
                    
                                    ans.add(new Action(i,j,m,n));               
                    
                                    if(len > 1 && cf == playerColorFV) break;
                    
                                    m--; n++;
                                }
                            }
                            else if(piece.equals("King"))
                            {
                                for(int ii = -1; ii < 2; ii++)
                                {
                                    for(int jj = -1; jj < 2; jj++)
                                    {
                                        int m = i+ii, n = j + jj;
                                        if(m == i && n == j) continue;
                    
                                        if((m < 8 && m >= 0) && (n < 8 && n >= 0))
                                        {
                                            char cf = SetUp.situation[m][n].charAt(0);
                                            int len = SetUp.situation[m][n].length();
                                            if(len == 1 || cf != AIColorFV)
                                            {
                                                ans.add(new Action(i,j,m,n));                            
                                            }
                                        }
                                    }
                                }
                            }
                            else if(piece.equals("Rook") || piece.equals("Queen"))
                            {
                                int m = i+1, n = j;
                                while(m < 8)
                                {
                                    int len = SetUp.situation[m][n].length();
                                    char cf = SetUp.situation[m][n].charAt(0);
                    
                                    if(len > 1 && cf == AIColorFV) break;
                    
                                    ans.add(new Action(i,j,m,n));                
                    
                                    if(len > 1 && cf == playerColorFV) break;
                    
                                    m++;
                                }
                    
                                m = i; n = j + 1;
                                while(n < 8)
                                {
                                    int len = SetUp.situation[m][n].length();
                                    char cf = SetUp.situation[m][n].charAt(0);
                    
                                    if(len > 1 && cf == AIColorFV) break;
                    
                                    ans.add(new Action(i,j,m,n));                
                    
                                    if(len > 1 && cf == playerColorFV) break;
                    
                                    n++;
                                }
                    
                                m = i-1; n = j;
                    
                                while(m >= 0)
                                {
                                    int len = SetUp.situation[m][n].length();
                                    char cf = SetUp.situation[m][n].charAt(0);
                    
                                    if(len > 1 && cf == AIColorFV) break;
                    
                                    ans.add(new Action(i,j,m,n));                
                    
                                    if(len > 1 && cf == playerColorFV) break;
                    
                                    m--;
                                }
                    
                                m = i; n = j - 1;
                    
                                while(n >= 0)
                                {
                                    int len = SetUp.situation[m][n].length();
                                    char cf = SetUp.situation[m][n].charAt(0);
                    
                                    if(len > 1 && cf == AIColorFV) break;
                    
                                    ans.add(new Action(i,j,m,n));                
                    
                                    if(len > 1 && cf == playerColorFV) break;
                    
                                    n--;
                                }
                            }

                        }
                    }
                    return ans;
                }
        }
        
               
}