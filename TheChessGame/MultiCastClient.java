import javax.swing.JComboBox;
import javax.swing.JTextField;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.io.IOException;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

class MultiCastClient implements Runnable, ActionListener
{
    MulticastSocket socket;
    DatagramPacket packet;
    InetAddress group;
    JTextField ipf, portf;
    JComboBox cb;
    int servernum = 0;
    String[] games, ips, ports;

    MultiCastClient(JComboBox cb, JTextField ip, JTextField port)
    {
        ipf = ip;
        portf = port;
        ip.setText(SetUp.getServerIP());
        portf.setText("65483");
        try
        {
            socket = new MulticastSocket(6969);
        }
        catch(IOException e)
        {
            SetUp.status.setText("Coult not listen on port 6969");
            return;
        }
        try
        {            
            group = InetAddress.getByName("225.69.69.69");
            socket.joinGroup(group);
        }
        catch(IOException e)
        { e.printStackTrace();}

        games = new String[50];
        ips = new String[50];
        ports = new String[50];
        this.cb = cb;
    }

    public void run()
    {
        while(SetUp.isClientListening)
        {                 
            byte[] buf = new byte[512];
            packet = new DatagramPacket(buf, buf.length);
            try
            {
                socket.receive(packet);
                String received = new String(packet.getData());
                if(received == null) continue;
                //System.out.println(received);
                received += " ";
                String b = "";                     
                byte spaceNum = 0,sameName = 0;
                boolean presentAlready = false;

                for(int k = 0; k < received.length(); k++)
                {
                    char c = received.charAt(k);

                    if(c == ' ')
                    {
                        switch(spaceNum)
                        {
                            case 1 :  for(int i = 0; i < servernum; i++)
                                if(games[i].equals(b))
                                    b += (sameName++);

                            games[servernum] = b + "'s Game";
                            //System.out.print(servernum + " " + games[servernum]);
                            b = "";
                            break;

                            case 2 :  for(int i = 0; i < servernum; i++)
                                if(ips[i].equals(b))
                                {
                                    presentAlready = true;
                                    games[servernum] = "";
                                    break;
                                }   

                            if(!presentAlready)
                                ips[servernum] = b;
                            
                            //System.out.print(" " + ips[servernum]);
                            b = "";

                            case 3 :  ports[servernum] = b;
                            b = "";
                            //System.out.print(" " + ports[servernum]);
                            break;
                        }
                        spaceNum++;
                    }
                    else if(spaceNum > 0) b += c;
                    
                    if(presentAlready) break;         
                }

                if(!presentAlready)
                {
                    servernum++;
                    if(servernum == 1) doActionChanges(0);
                    String[] names = new String[servernum];
                    for(int j = 0; j < servernum; j++)
                        names[j] = games[j];
                        
                    SetUp.panel[1].remove(cb);
                    cb = new JComboBox(names);
                    SetUp.setUpComboBox(cb,70,140,260,25,Color.BLACK,Color.WHITE);
                    cb.addActionListener(this);
                    cb.setActionCommand("cb");
                    SetUp.panel[1].add(cb);
                    SetUp.panel[1].repaint();
                }
            }
            catch(IOException e)
            {e.printStackTrace();}
        }
    }

    public void actionPerformed(ActionEvent ae)
    {
        if(ae.getActionCommand().equals("cb"))
        {
            int x = cb.getSelectedIndex();
            doActionChanges(x);
        }
    }
    
    public void doActionChanges(int x)
    {
            ipf.setText(ips[x]);   
            portf.setText(ports[x].trim());
    }
}