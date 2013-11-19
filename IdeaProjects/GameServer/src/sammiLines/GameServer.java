package sammiLines;

/**
 * Created with IntelliJ IDEA.
 * User: sammi
 * Date: 10/10/13
 * Time: 3:08 PM
 * To change this template use File | Settings | File Templates.
 */

import java.awt.*;
import java.io.IOException;
import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.ServerSocket;
import javax.swing.*;
import java.lang.Math;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



public class GameServer extends JFrame
{
    ServerSocket server;
    public static Socket con;
    ObjectOutputStream output;
    ObjectInputStream input;
    public static JTextArea connected_TA;
    public static JTextArea currentString_TA;
    GridLayout scoreGrid;
    JLabel connected_Label;
    JLabel currentString_Label;
    JLabel score;
    JPanel panel;
    JButton terminate;
    static String[] alphanumeric = {"z","x","c","v","b","n","m",",",".","/","a","s","d","f","g","h","j","k","l",";","q","e","w","r","t","y","u","i","o","p","Z","X","C","V","B","N","M","A","S","D","F","G","H","J","K","L","Q","W","E","R","T","Y","U","I","O","P","[","]","1","2","3","4","5","6","7","8","9","0","-","=","`","~","<",">","?",":","!","@","#","$","%","^","&","*",")","(","_","+",")"};
    public static String gameString;
    static int stringlength;
    static int stringContent;
    public static int connectionsCount=0;
    public static int countDown=5;
    public static ArrayList<ObjectOutputStream> connections = new ArrayList<ObjectOutputStream>();
    public static int flag=0;
    public static Long time;
    public static int max=0;
    private ExecutorService executorService = Executors.newCachedThreadPool();

    public GameServer()
    {
        super("Game Server");
        panel = new JPanel();
        panel.setBorder(BorderFactory.createLineBorder(Color.black , 2));
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));




        connected_Label = new JLabel("clients connected");
        connected_Label.setFont(new Font("Serif", Font.BOLD, 20));
        panel.add(connected_Label);

        connected_TA = new JTextArea();
        connected_TA.setBorder(BorderFactory.createLineBorder(Color.cyan,4));
        connected_TA.setEditable(false);

        connected_TA.setFont(new Font("Serif", Font.PLAIN, 25));
        panel.add(connected_TA);

        currentString_Label = new JLabel("current Word");
        currentString_Label.setFont(new Font("Serif", Font.BOLD, 20));
        panel.add(currentString_Label);

        currentString_TA = new JTextArea();
        currentString_TA.setEditable(false);
        currentString_TA.setBorder(BorderFactory.createLineBorder(Color.cyan,4));
        currentString_TA.setFont(new Font("Serif", Font.PLAIN, 25));

        panel.add(currentString_TA);

       /* score = new JLabel("score");
        score.setFont(new Font("Serif",Font.BOLD,20));
        panel.add(score);

        scoreGrid  = new GridLayout(2,2);
        */
        terminate = new JButton("stop server");
        terminate.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                //To change body of implemented methods use File | Settings | File Templates.
                try {
                    closeConnection();

                    currentString_TA.setText("Connection has been terminated by the server!!");

                    TimeUnit.SECONDS.sleep(2);

                    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                }
                catch (InterruptedException e1) {
                    e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                catch (SocketException se)
                {
                    se.printStackTrace();
                } catch (IOException e1)
                {
                    e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        });

        panel.add(terminate);
        add(panel);

        setSize(1200,800);
        setVisible(true);
    }

    public void runner()
    {
        try
        {
            server = new ServerSocket(5000,5);

            while(true)
            {
                try
                {
                    waitForConnection();
                }
                catch (EOFException eofException)
                {
                    JOptionPane.showMessageDialog(null,"Could not connect clients");
                }
                finally
                {
                    //closeConnection();
                }


               /*new Thread(
                       new MultiClients(con)
               ).start();*/


               executorService.submit(new MultiClients(con));
            }
        }
        catch (IOException ioException)
        {
            ioException.printStackTrace();
        }
    }

    public static void closeConnection() throws IOException
    {

        try{con.close();}
        catch (NullPointerException np)
        {
            np.printStackTrace();
        }
    }


    public static void len()
    {
        connectionsCount=connections.size();
    }


    public static void game() throws IOException
    {
        connectionsCount=connections.size();
        time=Long.valueOf(0);
        len();
        randomAlphaNumeric();
        sendAll(gameString);
        displayStringSent(gameString);
    }

    private void waitForConnection()  throws IOException
    {
        displayMessage("waiting for connections...");
        con = server.accept();

    }

    public static void displayMessage(final String message)
    {
        SwingUtilities.invokeLater(
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        connected_TA.setText(message);
                    }
                }
        );
    }

    public static void displayMessageAppend(final String message)
    {
        SwingUtilities.invokeLater(
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        connected_TA.append(message);
                    }
                }
        );
    }

    public static void displayStringSent(final String message)
    {
        SwingUtilities.invokeLater(
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        currentString_TA.setText(message);
                    }
                }
        );
    }

    private static void randomAlphaNumeric()
    {

        stringlength=5;
        gameString="";

        while(stringlength>0)
        {
            stringContent=(int)(Math.random()*(alphanumeric.length));

            gameString=gameString+alphanumeric[stringContent];
            stringlength--;
        }
    }

    public static void delayCountDown() throws InterruptedException, IOException
    {
        while(countDown>0)
        {

            try
            {
                sendAll(String.valueOf(countDown));
                TimeUnit.SECONDS.sleep(1);
            }
            catch (InterruptedException ie)
            {
                ie.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            countDown--;
        }

        sendAll("GOOOOOOO!!");
        TimeUnit.SECONDS.sleep(1);
        game();
    }



    public static void sendAll(String flood) throws IOException
    {
        try
        {
            for(ObjectOutputStream moja:connections)
            {
               moja.writeObject(flood);
            }
        }
        catch (IOException ioX)
        {
           ioX.printStackTrace();
        }
    }


}
