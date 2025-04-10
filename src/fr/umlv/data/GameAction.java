package fr.umlv.data;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

public class GameAction {

	public static boolean canBuyCard(Player player, Map<Token, Integer> price) {/*renvoie true si le jouer peutr acheter une carte du prix price*/
		Objects.requireNonNull(player, "player is null");
		Objects.requireNonNull(price, "price is null");
		var golds = player.playerTokenLeft(new Token(GameColor.GOLD));
		var goldNeeded = price.keySet().stream().filter(t->price.get(t) > player.playerTokenLeft(t) + player.tokenBonus(t))
				.mapToInt(t-> price.get(t) - (player.playerTokenLeft(t) + player.tokenBonus(t))).sum();
		return golds >= goldNeeded ? true : false; 
	}
	
	public static boolean canBuyTile(Player player, Map<Token, Integer> price) {/*renvoie true si pour chaque couleur le nombre de carte de cette couleur dans boardLine est superieur ou egale au nombre dans price*/
		Objects.requireNonNull(price, "price is null");
		return price.keySet().stream().allMatch(p->price.get(p) <= player.nbrOfColorCardPlayer(p.color()));
	}
	
	public static int drawToken(Set<GameColor> colors, Board board, Player player) {/*Tire jetons de couleurs differente de la banque du jeu*/
		Objects.requireNonNull(colors, "colors is null");
		Objects.requireNonNull(board, "board is null");
		Objects.requireNonNull(player, "player is null");
		if(colors.contains(GameColor.NOBLE) || colors.contains(GameColor.GOLD)) {
			System.out.println("cant draw gold token or noble token");
			return 1;
		}
		if(colors.size() == 3) {
			colors.forEach(c->player.addToken(board.drawTokenFromBoardBank(c), 1));
		}
		else if(colors.size() == 1 && board.tokenLeftInBoardBank(new Token(colors.iterator().next())) >= 4) {
			colors.forEach(c->{player.addToken(board.drawTokenFromBoardBank(c), 1); player.addToken(board.drawTokenFromBoardBank(c), 1);});
		}
		else {
			System.out.println("colors.size() != 3 && colors.size() != 1 || tokenleft(color) < 4, colors.size() = " + colors.size());
			return 2;
		}
		
		return 0;
	}
	
	public static void buyTile(Player player, Board board,  int pos) {/*ajoute une tuile à la main du joueur  depuis le splendor*/
		if(pos < 0 || pos > 3) {
			throw new IllegalArgumentException("pos < 0 || pos > 3, pos = " + pos);
		}
		Objects.requireNonNull(board, "board is null");
		Objects.requireNonNull(player, "player is null");
		player.addTile(board.removeTileFromBoard(pos));
	}
	
	public static void tokenTransfer(Player player, Board board, GameColor color, int nbr) {/*retire de la jetonbank du joueur nbr jeton de la couleur color*/
		Objects.requireNonNull(player, "player is null");
		var token = new Token(Objects.requireNonNull(color, "color is null"));
		nbr = nbr < 0 ? 0 : nbr; 
		if(nbr > player.playerTokenLeft(token)) {
			tokenTransfer(player, board, GameColor.GOLD, nbr - player.playerTokenLeft(token));
			nbr = player.playerTokenLeft(new Token(color));
		}
		player.removeToken(token, nbr);
		board.addTokenToBoardBank(token, nbr);
	}
	
	public static void buyCard(Player player, Board board, int level, int pos) {
		if(level < 1 || level > 3) {
			throw new IllegalArgumentException("level < 1 || level > 3, level = " + level);
		}
		if(pos < 0 || pos > 3) {
			throw new IllegalArgumentException("pos < 0 || pos > 3, pos = " + pos);
		}
		Objects.requireNonNull(player, "player is null");
		Objects.requireNonNull(board, "board is null");
		var price = board.priceOfCard(level, pos);
		price.keySet().stream().forEach(t-> tokenTransfer(player, board, t.color(), price.get(t) - player.tokenBonus(t)));
		player.addCard(board.removeCardFromBoard(level, pos));
	}
	
	public static void reserveCard(Player player, Board board, int level, int pos) {
		if(level < 1 || level > 3) {
			throw new IllegalArgumentException("level < 1 || level > 3, level = " + level);
		}
		if(pos < 0 || pos > 3) {
			throw new IllegalArgumentException("pos < 0 || pos > 3, pos = " + pos);
		}
		Objects.requireNonNull(player, "player is null");
		Objects.requireNonNull(board, "board is null");
		player.addToken(board.drawTokenFromBoardBank(GameColor.GOLD), 1);
		player.addToReserveCard(board.removeCardFromBoard(level, pos));
	}
	
	public static boolean isNumeric(String str) {
		try {  
			Double.parseDouble(str);  
			return true;
		} catch(NumberFormatException e){  
			return false;  
		}  
	}
	
	public static int validNbr(Scanner sc, int min, int max) {
		Objects.requireNonNull(sc, "scanner is null");
		var nbr = sc.next();
		if(!GameAction.isNumeric(nbr) || Integer.parseInt(nbr) < min || Integer.parseInt(nbr) > max) {
			System.out.println("saisie incorrect : " + nbr + "\nle nombre doit etre compris entre " + min + " et " + max);
			System.out.print("resaisir un nombre valide : ");
			return validNbr(sc, min, max);
		}
		System.out.println("nombre saisie = " + nbr);
		return Integer.parseInt(nbr);
	}
	
	public static String validAction(Scanner sc) {
		Objects.requireNonNull(sc, "scanner is null");
		System.out.print("action disponible : \n-piocher pour piocher des jetons \n-reserver pour reserver une carte \n-acheter pour acheter une carte ou une tuile \nsaisir une action à effectuer : ");
		var action = sc.next();
		if(!action.equals("piocher") && !action.equals("reserver") && !action.equals("acheter")) {
			System.out.println("saisie incorect : " + action + " , action non definit");
			return validAction(sc);
		}
		return action;
	}
	
	public static Set<GameColor> validColor(Scanner sc, int nbrColor) {
		System.out.print("saisir " + nbrColor + " couleur(s) de jeton à piocher : "); 
		var set = new HashSet<GameColor>();
		while(set.size() < nbrColor) {
			var color = Card.stringToColor(sc.next());
			while(color == null || set.contains(color)) {
				System.out.println("couleur saisie non reconnut ou deja selectionné\ncouleur disponible green, blue, black, red, white");
				System.out.print("saisir " + (nbrColor - set.size()) + " couleur(s) de jeton à piocher :"); 
				color = Card.stringToColor(sc.next());
			}
			set.add(color);
		}
		return set;
	}
	
	public static int validNbrTokenToDraw(Scanner sc, Board board) {
		System.out.print("saisir le nombre de couleurs differentes de jeton à piocher : ");
		var nbr = sc.next();
		if(!GameAction.isNumeric(nbr) || (Integer.parseInt(nbr) != 1 && Integer.parseInt(nbr) != 3)) {
			System.out.println("saisie incorrect : " + nbr + "\nle nombre doit etre egale à 1 ou 3");
			return validNbrTokenToDraw(sc, board);
		}
		return Integer.parseInt(nbr);
	}
	
	public static String validObjectToBuy(Scanner sc) {
		System.out.print("saisir l'objet à acheter : ");
		var objectToBuy = sc.next();
		if(!objectToBuy.equals("carte") && !objectToBuy.equals("tuile")) {
			System.out.println("saisie incorect : " + objectToBuy + "\nle type d'object doit etre égale à carte ou tuile");
			return validObjectToBuy(sc);
		}
		return objectToBuy;
	}
	
	public static boolean validBuyObject(Board board, Player player, Scanner sc, String objectToBuy) {
		Objects.requireNonNull(board, "board is null");
		Objects.requireNonNull(objectToBuy, "objectToBuy is null");
		Objects.requireNonNull(player, "player is null");
		switch(objectToBuy) {
			case"carte"->{ 
				System.out.print("saisir le niveau de la carte puis sa position : "); 
				var level = validNbr(sc, 1, 3); var pos = validNbr(sc, 0, 3); 
				if(!canBuyCard(player, board.priceOfCard(level, pos))) {System.out.println("object " + objectToBuy + " impossible à acheter"); return false;}
				buyCard(player, board, level, pos);
			}
			case"tuile"->{ 
				System.out.print("saisir la position de la tuile : "); 
				var pos = validNbr(sc, 0, 3); 
				if(!canBuyTile(player, board.priceOfTile(pos))) {System.out.println("object " + objectToBuy + " impossible à acheter"); return false;}
				buyTile(player, board, pos);
			}
		}
		return true;
	}
	
	public static boolean asciOrGraphic(Scanner sc) {
		Objects.requireNonNull(sc);
		System.out.print("mode graphique(oui/non) : ");
		var choice = sc.next();
		while(!choice.equals("oui") && !choice.equals("non")){
			System.out.print("mode graphique(oui/non) : ");
			choice = sc.next();
		}
		
	    return choice.equals("oui") ? true : false; 
	}
	
	public static int doAction(Player player, Board board, String action, Scanner sc) {
		Objects.requireNonNull(sc, "scanner is null");
		Objects.requireNonNull(player, "player is null");
		Objects.requireNonNull(board, "board is null");
		Objects.requireNonNull(action, "action is null");
		switch(action) {
			case "reserver"->{ 
				System.out.print("saisir le niveau de la carte puis sa position : ");
				reserveCard(player, board, validNbr(sc, 1, 3), validNbr(sc, 0, 3));
			}
			case "piocher"-> {
				if(drawToken(validColor(sc, validNbrTokenToDraw(sc, board)), board, player) == 2) { 
					System.out.println("impossible de piocher 2 jetons d une couleur s il reste moins de 4 jetons de cette couleur à la banque"); 
					return doAction(player, board, validAction(sc), sc);
				}
			}
			case "acheter"-> {
				if(!validBuyObject(board, player, sc, validObjectToBuy(sc))) {return doAction(player, board, validAction(sc), sc);}
			}
			default -> throw new IllegalArgumentException("unknown action, action = " + action);
		}
		return 0;
	}
	
	public static Player maxPretige(List<Player> players) {
		Objects.requireNonNull("players is null");
		return players.stream().max(Comparator.comparingInt(Player::totalPrestige)).orElse(players.get(0));
	}
	
	public static void playGameAsci(Board board, List<Player> listWinner, List<Player> listPlayer, Scanner sc) {
		System.out.println(board.toString());
		while(true) {
			if(!listWinner.isEmpty()) {
				var winner = maxPretige(listWinner);
				System.out.println("Le gagnant est " + winner.name() + " avec " + winner.totalPrestige() + " de prestige");
				break;
			}
			listPlayer.stream().forEach(p->{ 
				System.out.println("à " + p.name() + " de jouer\n" + p.toString()); 
				doAction(p, board, validAction(sc), sc); 
				if(p.totalPrestige() >= 15) {listWinner.add(p);}
			});
		}
	}
	
	public static void main(String[] args) {
		System.out.println(isNumeric("sd"));
	}
}
