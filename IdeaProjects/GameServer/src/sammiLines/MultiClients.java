package sammiLines;

/**
 * Created with IntelliJ IDEA.
 * User: sammi
 * Date: 10/15/13
 * Time: 4:00 PM
 * To change this template use File | Settings | File Templates.
 */

import javax.swing.*;
import java.io.EOFException;
import java.net.Socket;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.lang.Long;



public class MultiClients implements Runnable
{
    Socket connection;
    ObjectOutputStream output;
    ObjectInputStream input;




    public MultiClients(Socket socketConnection)
    {
        connection = socketConnection;

    }

    public void run()
    {
        try
        {
            int points=0;

            GameServer.connectionsCount++;

            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();

            GameServer.connections.add(output);

            input = new ObjectInputStream(connection.getInputStream());
            try
            {
                output.writeObject("you have been connected\nwait for a series of characters to type....................\n");
                output.flush();
            }
            catch (IOException io)
            {
                io.printStackTrace();
            }


            if(GameServer.connected_TA.getText().equals("waiting for connections..."))
            {
                GameServer.displayMessage("Connected:\n"+connection.getInetAddress().getHostName()+" at "+connection.getInetAddress().getHostAddress());
            }
            else
            {
                GameServer.displayMessageAppend(connection.getInetAddress().getHostName()+" at "+connection.getInetAddress().getHostAddress());
            }

            if(GameServer.flag==0)
            {
                GameServer.flag=1;

                GameServer.delayCountDown();
            }

            String string = null;
            Long thisTime=Long.valueOf(0);
            GameServer.flag=0;
            do
            {

                try
                {


                    string=(String)input.readObject();

                    thisTime=Long.valueOf((Long)input.readObject());
                    GameServer.displayStringSent(string);
                    if(string.equals("STOP"))
                    {
                        break;
                    }
                    try
                    {
                        if(string.equals(GameServer.gameString))
                        {
                            GameServer.connectionsCount--;
                            if(GameServer.time>thisTime || GameServer.time==0)
                            {
                                GameServer.time=thisTime;
                                while(GameServer.connectionsCount>0)
                                {
                                    TimeUnit.SECONDS.sleep(1);
                                }
                                if(GameServer.time==thisTime)
                                {
                                    points+=GameServer.gameString.length()*100;

                                }
                            }

                        }
                    }
                    catch (NullPointerException n)
                    {
                        points=points;
                    }

                }
                catch (ClassNotFoundException e)
                {
                    e.printStackTrace();
                }
                catch (EOFException eof)
                {
                    eof.printStackTrace();
                }
                if(GameServer.flag==0)
                {
                    GameServer.flag=1;
                    GameServer.game();
                }
                GameServer.flag=0;
            }while(!string.equals("STOP"));

            output.writeObject("score");
            //output.writeObject(points);

            if(GameServer.flag==0)
            {
                GameServer.flag=1;
                GameServer.len();

            }
            if((GameServer.max<points) || GameServer.max==points)
            {

                GameServer.max=points;
                GameServer.connectionsCount--;
                while(GameServer.connectionsCount>0)
                {
                    TimeUnit.SECONDS.sleep(1);
                }
                if(GameServer.max==points)
                {
                    if(points==0)
                    {
                        output.writeObject(String.valueOf(points)+" you have not earned points!!");
                        output.flush();
                    }
                    else
                    {
                        output.writeObject(String.valueOf(points)+" You won!!");
                        output.flush();
                    }
                    //output.writeObject("You won!!");
                    //output.flush();

                }
            }
            else
            {
                GameServer.connectionsCount--;


                output.writeObject(String.valueOf(points)+" You lost!!");
                output.flush();
                //output.writeObject();
                //output.flush();

            }

            output.close();
            input.close();
            //connection.close();

        }
        catch(IOException ioX)
        {
            ioX.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private void closeConnection()
    {

        try
        {
            output.close();
            input.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
