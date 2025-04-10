package fr.umlv.data;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;

public class Player {
	private final String name;
	private final HashMap<Integer, TokenBank> playerBank;/*0 bonus, 1 bank */
	private final HashMap<Integer, CardBoardLine> hand; /*1 card, 2 reserved card*/
	private final TileBoardLine tileHand;
	private int totalPrestige;
	
	public Player(String name) {
		Objects.requireNonNull(name, "name is null");
		this.playerBank = new HashMap<>();
		this.hand = new HashMap<>();
		tileHand = new TileBoardLine();
		IntStream.range(0, 2).forEach(i-> playerBank.compute(i,(k,v)-> v = TokenBank.setTokenBank(0, 0)));
		IntStream.range(1, 3).forEach(i-> hand.compute(i,(k,v)-> v = new CardBoardLine(i)));
		this.totalPrestige = 0;
		this.name = name;
	}
	
	public void addToken(Token token, int nbr) {
		if(nbr < 1) {
			throw new IllegalArgumentException("nbr < 1, nbr = " + nbr);
		}
		if(token == null) {
			return ;
		}
		playerBank.get(1).addToken(token, nbr);
	}
	
	public void removeToken(Token token, int nbr) {
		Objects.requireNonNull(token);
		if(nbr < 0 || nbr > playerBank.get(1).tokenLeft(token)) {
			throw new IllegalArgumentException("nbr < 0 || nbr > playerBank.get(1).tokenLeft(token), nbr = " + nbr);
		}
		playerBank.get(1).removeTokenFromTokenBank(token, nbr);
	}
	
	public void addTile(Card tile) {
		Objects.requireNonNull(tile);
		if(!tile.color().equals(GameColor.NOBLE)) {
			throw new IllegalArgumentException("!tile.color().equals(GameColor.NOBLE), color = " + tile.color());
		}
		tileHand.add(tile);
		totalPrestige += tile.prestige();
	}
	
	public void addCard(Card card) {
		Objects.requireNonNull(card);
		if(card.color().equals(GameColor.NOBLE)) {
			throw new IllegalArgumentException("card.color().equals(GameColor.NOBLE), color = " + card.color());
		}
		totalPrestige += card.prestige();
		playerBank.get(0).addToken(new Token(card.color()), 1);
		hand.get(1).add(card);
	}
	
	public void addToReserveCard(Card card) {
		Objects.requireNonNull(card);
		if(card.color().equals(GameColor.NOBLE)) {
			throw new IllegalArgumentException("card.color().equals(GameColor.NOBLE), color = " + card.color());
		}
		hand.get(2).add(card);
	}
	
	public int playerTokenLeft(Token token){
		return playerBank.get(1).tokenLeft(token);
	}
	
	public int tokenBonus(Token token) {
		return playerBank.get(0).tokenLeft(token);
	}
	
	public int nbrOfColorCardPlayer(GameColor color) {
		return CardBoardLine.nbrofColorCard(color, hand.get(1));
	}
	
	public int nbrTileOfPlayer() {
		return tileHand.size();
	}
	
	public List<Card> reservedCard(){
		return hand.get(2).cardList();
	}
	
	public Map<Token, Integer> playerBank() {
		return playerBank.get(1).tokenBank();
	}
	
	public String name() {
		return name;
	}
	
	public int totalPrestige() {
		return totalPrestige;
	}
	
	@Override
	public String toString() {
		var builder = new StringBuilder();
		builder.append(name).append(" possessions\n").append("prestige = ").append(totalPrestige).append("\n");
		builder.append("Reserved Card of player :\n").append(hand.get(2).toString()).append("\n");
		builder.append("Tile of player :\n").append(tileHand.toString()).append("\n");
		builder.append("Cards of player :\n").append(hand.get(1).toString()).append("\n");
		builder.append("Token of player :\n").append(playerBank.get(1).toString()).append("\n");
		return builder.toString();
	}
	
	public static void main(String[] args) {
		var p1 = new Player("j");
		System.out.println(p1);
		System.out.println("-------------");
	}
}
