import javax.swing.JComboBox;
import javax.swing.JTextField;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.io.IOException;

class MultiCastServer implements Runnable
{
    InetAddress group;
    byte[] buf;
    DatagramSocket ssocket;
    DatagramPacket packet;
    
    MultiCastServer()
    {        
        try
        {            
            group = InetAddress.getByName("225.69.69.69");
        }
        catch(IOException e)
        { e.printStackTrace();}
        
        buf = ("TCGMCS " + SetUp.name + " " + SetUp.HostIP + " " + SetUp.HostPort).getBytes();
        
        try
        {
            ssocket = new DatagramSocket(6970);
        }
        catch(SocketException e)
        {
            SetUp.status.setText("Coult not broadcast on port 6970");
            return;
        }
        
        packet = new DatagramPacket(buf,buf.length,group,6969);
    }
    
    public void run()
    {
        while(SetUp.isServerRunning)
        {
            try 
            {
                ssocket.send(packet);
            }
            catch(IOException e)
            { 
                e.printStackTrace();
            }
            
            try 
            {
                Thread.sleep(2000);
            } 
            catch (InterruptedException e) { }
        }
        
        ssocket.close();
    }
}
