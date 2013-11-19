package com.company;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;


/**
 * Created with IntelliJ IDEA.
 * User: sammi
 * Date: 11/17/13
 * Time: 6:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class Greetee extends Agent
{
    AID id;

    public void setup()
    {
        id = new AID("greetee",AID.ISLOCALNAME);
        System.out.println("Greetee started "+getAID());

        addBehaviour(new receiveMessage());
    }

    private class receiveMessage extends CyclicBehaviour
    {
        public void action()
        {
            ACLMessage message = receive();
            if(message != null)
            {
                System.out.println("sent "+message.getContent());
            }

            else
            {
                block();
            }
        }


    }
}
