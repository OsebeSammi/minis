package com.company;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
//import jade.lang.acl.MessageTemplate;



/**
 * Created with IntelliJ IDEA.
 * User: sammi
 * Date: 11/17/13
 * Time: 6:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class Greeter extends Agent
{
    AID id;
    GreeterForm gui;


    public void setup()
    {
        id = new AID("Greeter",AID.ISLOCALNAME);
        System.out.println("Greeter started"+" "+getAID());
        //gui = new GreeterForm();

        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        message.addReceiver(new AID("greetee",AID.ISLOCALNAME));
        message.setLanguage("English");
        message.setContent("I greet you");
        send(message);

    }


}



