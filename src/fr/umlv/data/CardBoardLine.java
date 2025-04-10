package fr.umlv.data;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CardBoardLine {
	private final ArrayList<Card> cardBoardLine;
	private int level;
	private int size;
	
	public CardBoardLine(int level) {
		if(level < 1 || level > 3) {
			throw new IllegalArgumentException("level < 1 || level > 3 but pos = " + level);
		}
		cardBoardLine = new ArrayList<>();
		this.level = level;
		size = 0;
	}
	
	@Override
	public String toString() {
		if(cardBoardLine.isEmpty()) {
			return "empty";
		}
		return cardBoardLine.stream().map(p->p.toString()).collect(Collectors.joining(", "));
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(cardBoardLine.hashCode(), size, level);
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof CardBoardLine cbl && cbl.cardBoardLine.equals(cardBoardLine) && cbl.size == size && cbl.level == level;
	}
	
	public Card getCardFromBoardLine(int pos) {/*renvoie la carte la position pos*/
		if(pos < 0 || pos >= size) {
			throw new IllegalArgumentException("pos < 0 || pos > size but pos = " + pos);
		}
		return cardBoardLine.get(pos);
	}
	
	public Card removeCardFromBoardLine(int pos, Deck deck) {/*retire et renvoie la carte à la postion*/
		if(pos < 0 || pos > 3) {
			throw new IllegalArgumentException("pos < 0 || pos > size but pos = " + pos);
		}
		if(deck.level() != level) {
			throw new IllegalArgumentException("deck.level() != level, level = " + level + ", deck.level = " + deck.level());
		}
		var card = cardBoardLine.get(pos);
		cardBoardLine.add(pos, deck.draw());
		cardBoardLine.remove(pos+1);
		return card;
	}

	public void add(Card card) {/*ajoute à la CardBoardLine la carte en parametre*/
		Objects.requireNonNull(card, "card is null");
		cardBoardLine.add(card);
		size++;
	}
	
	public static CardBoardLine setCardBoardLine(Deck deck) {/*assigne à la boardLine des cartes 4 du niveau choisis*/
		Objects.requireNonNull(deck, "deck is null");
		var bl = new CardBoardLine(deck.level());
		IntStream.range(0, 4).forEach(i->bl.add(deck.draw()));
		return bl;
	}

	public static int nbrofColorCard(GameColor color, CardBoardLine cardBoardLine) {/*renvoie le nombre de carte de la couleur choisis*/
		Objects.requireNonNull(color, "color is null");
		return (int)cardBoardLine.cardBoardLine.stream().filter(p->p.color().equals(color)).count();
	}
	
	public int level() {
		return level;
	}
	
	public int size() {
		return size;
	}
	
	public List<Card> cardList(){
		return Collections.unmodifiableList(cardBoardLine);
	}
	
	public static void main(String[] args) {
		try {
		var blt = CardBoardLine.setCardBoardLine(Deck.setCardDeck(2));
		System.out.println(blt.size);
		var deck = Deck.setCardDeck(1);
		var blc = CardBoardLine.setCardBoardLine(deck);
		System.out.println(blc);
		}catch(IOException e) {
	    	 System.err.println(e.getMessage());
	    	 System.exit(1);
	    	 return;
	     }
	}
}
