package sammiLines;

import javax.swing.JFrame;

public class Main {

    public static void main(String[] args)
    {
	    System.out.println("Tumeanza");

        GameServer game = new GameServer();
        game.runner();
        game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
}
