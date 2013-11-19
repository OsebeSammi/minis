package com.company;

import com.sun.org.apache.xerces.internal.impl.xpath.XPath;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: sammi
 * Date: 11/17/13
 * Time: 6:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class GreeterForm extends JFrame
{
    JTextArea textArea1;
    JButton button1;
    JButton button2;
    JTextArea textArea2;
    GridLayout grid;
    JPanel parent;
    JLabel greet;
    JLabel reply;

    GreeterForm()
    {
        super("Greeter Agent");
        parent = new JPanel();
        parent.setLayout(new BoxLayout(parent, BoxLayout.Y_AXIS));

        //Label
        greet = new JLabel("Type greeting message");
        greet.setFont(new Font("Serif", Font.BOLD, 40));
        parent.add(greet);

        //Adding textarea for typing Greeting
        textArea1 = new JTextArea();
        textArea1.setLineWrap(true);

        textArea1.setFont(new Font("Serif", Font.PLAIN, 30));
        textArea1.setBorder(BorderFactory.createLineBorder(Color.cyan,3));
        parent.add(textArea1);

        //adding button for send
        button2 = new JButton("Send greeting");
        button2.addActionListener
                (
                        new ActionListener()
                        {
                            @Override
                            public void actionPerformed(ActionEvent e)
                            {
                                //send greeting
                            }
                        }
                );
        button2.setSize(500,200);
        button2.setAlignmentX(LEFT_ALIGNMENT);
        parent.add(button2);

        reply = new JLabel("Type greeting message");
        reply.setFont(new Font("Serif", Font.BOLD, 40));
        parent.add(reply);

        //Adding textarea for reply
        textArea2 = new JTextArea();
        textArea2.setFont(new Font("Serif", Font.PLAIN, 30));
        textArea2.setLineWrap(true);
        textArea2.setBorder(BorderFactory.createLineBorder(Color.cyan,3));
        textArea2.setEditable(false);
        parent.add(textArea2);

        //button for pulling down agent
        button1 = new JButton("Remove Agent");
        button1.addActionListener
                (
                        new ActionListener()
                        {
                            @Override
                            public void actionPerformed(ActionEvent e)
                            {
                                //Remove Agent
                            }
                        }
                );
        button1.setSize(500,200);
        button1.setAlignmentX(LEFT_ALIGNMENT);
        parent.add(button1);
        add(parent);
        setSize(1000,500);
        setVisible(true);
    }
}
