package fr.umlv.data;
import java.util.Objects;

public record Card(GameColor color, int prestige, String name, TokenBank price){

	public Card {
		Objects.requireNonNull(color, "color is null");
		Objects.requireNonNull(price, "price is null");
		Objects.requireNonNull(name, "name is null");
		if(color == GameColor.GOLD) {
			throw new IllegalArgumentException("color == GameColor.GOLD");
		}
		if(prestige < 0 || prestige > 8) {
			throw new IllegalStateException("prestige must be between 0 and 8");
		}
	}
	
	@Override
	public String toString() {
		return "Card:" + name + ":" + prestige  + ":" + color + ": price = " + price.toString();
	}
	
	public static GameColor stringToColor(String s) {/*renvoie la couleur conrespondante au string*/
		return switch(s) {
			case "blue" -> GameColor.BLUE;
			case "red" -> GameColor.RED;
			case "black" -> GameColor.BLACK;
			case "green" -> GameColor.GREEN;
			case "white" -> GameColor.WHITE;
			case "noble" -> GameColor.NOBLE;
			default -> null;
		};
	}
	
	public static Card stringToCard(String str) {/*associe le string en pârametre à une carte*/
		Objects.requireNonNull(str, "str is null");
		var strCard = str.split(":"); 
		var tb = new TokenBank();
		for(var i = 3; i < strCard.length - 1; i+=2) {
			tb.addToken(new Token(Card.stringToColor(strCard[i])), Integer.parseInt(strCard[i + 1]));
		}
		return new Card(Card.stringToColor(strCard[2]), Integer.parseInt(strCard[1]), strCard[0], tb);
	}
	public static void main(String[] args) {
		var card = new Card(GameColor.GREEN, 2, "test", TokenBank.setTokenBank(0,0));
		System.out.println(card);
	}
}
