package com.company;

import java.io.*;
import java.net.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

//declare class Requester to extend JFrame for GUI
public class Requester extends JFrame{
    Socket requestSocket;
    ObjectOutputStream out;
    ObjectInputStream in;
    long timeReceived, timeSend, timeDifference;
    String message,stringTyped, chatServer,messageRead;
    JTextArea connected_TA, instructionString ;
    JTextArea currentString_TA, stringToBeTypedArea;
    GridLayout scoreGrid;
    JLabel connected_Label,  stringToBeTypedLabel;
    JLabel currentString_Label,instructioLabel;
    JLabel score;
    JTextArea scoreField ;
    JTextField enterTextField,enter;
    JPanel panel,panel2;
    String chat;
    //private Scanner type;

    //class constructor, implementation of the GUI
    Requester()
    {
        super("Typing Pro");
        panel = new JPanel();
        panel.setBorder(BorderFactory.createLineBorder(Color.black , 2));
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));


        instructioLabel = new JLabel("Instructions: type the strings as fast as possible"
                + "\n then press enter\n");
        instructioLabel.setFont(new Font("Serif", Font.BOLD, 20));
        panel.add(instructioLabel);

        connected_Label = new JLabel(" connection....");
        connected_Label.setFont(new Font("Serif", Font.BOLD, 20));
        panel.add(connected_Label);


        connected_TA = new JTextArea();

        connected_TA.setBorder(BorderFactory.createLineBorder(Color.cyan,4));
        connected_TA.setEditable(false);



        connected_TA.setFont(new Font("Serif", Font.PLAIN, 25));
        //JScrollPane scroll = new JScrollPane();

        panel.add(connected_TA);


        currentString_Label = new JLabel("current Word");
        currentString_Label.setFont(new Font("Serif", Font.BOLD, 20));
        panel.add(currentString_Label);



        stringToBeTypedLabel = new JLabel("Type Current String Here");
        stringToBeTypedLabel.setFont(new Font("Serif", Font.BOLD, 20));
        panel.add(stringToBeTypedLabel);
        currentString_TA = new JTextArea();
        currentString_TA.setEditable(false);
        currentString_TA.setBorder(BorderFactory.createLineBorder(Color.cyan,4));
        currentString_TA.setFont(new Font("Serif", Font.PLAIN, 25));

        panel.add(currentString_TA);



        //JScrollPane scroll2 = new JScrollPane();
        //scroll2.add();
        panel.add(stringToBeTypedLabel);


        //stringToBeTypedArea = new JTextArea();
        //stringToBeTypedArea.setBorder(BorderFactory.createLineBorder(Color.cyan,4));
        //stringToBeTypedArea.setEditable(true);


        JPanel south = new JPanel(new GridLayout(1,2));


        scoreField = new JTextArea();
        scoreField.setBorder(BorderFactory.createLineBorder(Color.red, 2));
        scoreField.setEditable(false);
        scoreField.setFont(new Font("Serif",Font.PLAIN,25));
        south.add(scoreField);

        JButton endGame = new JButton("END\nGAME");

        endGame.setFont(new Font("Serif",Font.PLAIN,20));
        south.add(endGame);


        enterTextField = new JTextField();
        //enterTextField.setLocation(50, 5);
        //enterTextField.setSize(700, 85);
        enterTextField.setFont(new Font("Serif", Font.PLAIN, 25));
        //enterTextField.setAlignmentX(CENTER_ALIGNMENT);
        //enterTextField.setBackground(null);
        enterTextField.setBorder(BorderFactory.createLineBorder(Color.cyan,4));
        enterTextField.setEditable(true);
        enterTextField.addActionListener(
                new ActionListener()
                {
                    public void actionPerformed(ActionEvent event)
                    {

                        sendMessageToServer(event.getActionCommand());


                    }
                });

        //stringToBeTypedArea.add(enterTextField);
        panel.add(enterTextField);


        add(panel,BorderLayout.CENTER);
        add(south,BorderLayout.SOUTH);

        setSize(1000,700);
        setVisible(true);


        endGame.addActionListener(
                new ActionListener()
                {
                    public void actionPerformed(ActionEvent event)
                    {

                        sendMessageToServer("STOP");


                    }
                });
    }



    public void Runner()
    {
        try
        {
            //create socket to the server on port 5000
            // 5000 is the port to be listened to
            requestSocket = new Socket("41.89.64.72", 5000 );
            // server = new ServerSocket(5000,5);


            //while connection is true then proceed else throw exceptions
            while(true)
            {
                try
                {
                    waitForConnection();
                    getStreams();
                    processConnection();
                }
                catch (EOFException eofException)
                {
                    JOptionPane.showMessageDialog(null,"Server Terminated connection");
                }
                finally
                {
                    closeConnection();
                }
            }
        }
        catch (IOException ioException)
        {
            ioException.printStackTrace();
        }


    }
    private void waitForConnection()  throws IOException
    {
        sendMessage("waiting for connections...");

        // display connection information to the client
        sendMessage( requestSocket.getInetAddress().getHostName() +": You are Connected");
        // connection = client.accept();
    }
    private void getStreams() throws IOException
    {
        //setup the output and the input stream for the socket
        out = new ObjectOutputStream(requestSocket.getOutputStream());
        //flush the message to send the output to the server
        out.flush();

        //setup the input stream to receive the input from the server
        in = new ObjectInputStream(requestSocket.getInputStream());
    }


    private void processConnection() throws IOException
    {
        // setTextFieldEditable( true );
        try{
            //message read is the connection message from the server
            message = (String)in.readObject();

            //display the message to the client
            sendMessage(message);

        }
        catch(ClassNotFoundException classNot){
            System.err.println("data received in unknown format");
        }
        //message = (String)in.readObject();
        do
        {

            try{
                //message read is the timer message for the timer to elapse before the string is send
                //loop till the last word is GOOOOOOO!! then jump out of the loop
                message = (String)in.readObject();

                //display the loop to the client
                displayMessage(message);

            }
            catch(ClassNotFoundException classNot){
                System.err.println("data received in unknown format");
            }

        }while(!message.equals("GOOOOOOO!!"));

        do{
            try{
                // read the stream from the server  while making comparison to the word "score"

                messageRead = (String)in.readObject();
                enterTextField.setText("");
                //client pressed the "END GAME" button thus requesting for the score
                if(messageRead.equals("score"))
                {

                    //messageRead = (String)in.readObject();
                    //score.setText(messageRead);
                    try{
                        //read the score from the server then display it to the client
                        String points = (String)in.readObject();
                        scoreField.setText("score: "+ points);
                        //JOptionPane.showMessageDialog(null, points);
                        //JOptionPane.showMessageDialog(null, "Sure?");

                        // String status = (String)in.readObject();
                        //scoreField.setText(scoreField.getText()+status);
                        // scoreField.setText(status);
                    }
                    catch(ClassNotFoundException classNot)
                    {
                        System.err.println("data received in unknown format");
                    }

                }
                else{

                    //capture the time string was send
                    timeReceived= System.currentTimeMillis();
                    //JOptionPane.showMessageDialog(null, messageRead);
                    displayMessage(messageRead);



                    processString();
                }
            }
            catch(ClassNotFoundException classNot){
                System.err.println("data received in unknown format");
            }
        }while(!(message.equals("STOP")));

    }

    private void processString()
    {
        // stringTyped= JOptionPane.showInputDialog(stringTyped);
//		 setTextFieldEditable(true);
//		 stringTyped= 
//
//		 sendMessageToServer(stringTyped);
		 
		 /* allow the client to type and send the string to the server*/
        enterTextField.addActionListener(
                new ActionListener()
                {
                    public void actionPerformed(ActionEvent event)
                    {

                        sendMessageToServer(event.getActionCommand());


                    }
                });

    }
    // close the connection
    private void closeConnection() throws IOException
    {
        out.close();
        in.close();
        requestSocket.close();
    }

    //method to display the contents in the client side
    //information displayed is that for connection status and the waiting for connection
    private void sendMessage(final String msg)
    {
        SwingUtilities.invokeLater(
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        connected_TA.setText(msg);
                    }
                }
        );


    }
    // displays string to be typed in the currentString_TA text area
    private void displayMessage(final String msg)
    {
        SwingUtilities.invokeLater(
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        currentString_TA.setText(msg);
                    }
                }
        );


    }

    //method to send message to the server for processing
    // the message entails the string typed and the time difference
    private void sendMessageToServer(String mesg)
    {

        try{
            //capture the time string was send
            timeSend= System.currentTimeMillis();

            // find the time difference
            timeDifference= timeSend - timeReceived;

            //send the string to the server
            out.writeObject(mesg);
            out.flush();

            //send the time difference to the server
            out.writeObject(timeDifference);
            out.flush();

        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }

    }

    private void setTextFieldEditable( final boolean editable )
    {

        enterTextField.setEditable( editable );

    }


}