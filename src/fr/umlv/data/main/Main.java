package fr.umlv.data.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.IntStream;

import fr.umlv.data.Board;
import fr.umlv.data.GameAction;
import fr.umlv.data.Player;
import fr.umlv.data.Zen5Graph;

public class Main {
	public static void main(String[] args) {
		var listPlayer = new ArrayList<Player>();
		var listWinner = new ArrayList<Player>();
		var sc = new Scanner(System.in);
		System.out.print("saisir nombre de joueur : ");
		var nbrPlayer = GameAction.validNbr(sc, 2, 4);
		IntStream.range(0, nbrPlayer).forEach(i-> {System.out.print("saisir le nom du joueur numero " + (i + 1) + " : "); listPlayer.add(new Player(sc.next()));});
		var modeGraphic = GameAction.asciOrGraphic(sc);
		try {
			var board = Board.setBoard(nbrPlayer);
			if(modeGraphic) {
				Zen5Graph.playGameGraphic(board, listWinner, listPlayer);
			}
			else {
				GameAction.playGameAsci(board, listWinner, listPlayer, sc);
			}
		}catch(IOException e) {
	    	 System.err.println(e.getMessage());
	    	 System.exit(1);
	    	 return;
	     }
		sc.close();
	}
}
