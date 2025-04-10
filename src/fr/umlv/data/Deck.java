package fr.umlv.data;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

public class Deck{
	
	private final ArrayList<Card> deck;
	private int size;
	private int level;
	
	public Deck(int level) {
		if(level < 0 || level > 3) {
			throw new IllegalArgumentException("level < 0 || level > 3, level = " + level);
		}
		this.deck = new ArrayList<>();
		size = 0;
		this.level = level;
	}
	
	public Card draw() {/*retire une carte du deck et la renvoie*/
		if(size < 1) {
			return null;
		}
		return deck.remove(--size);
	}
	
	public static Deck setCardDeck(int level) throws IOException {/*initialise le deck avec des cartes du niveaux choisis*/
		if(level < 1 || level > 3) {
			throw new IllegalArgumentException("level < 1 || level > 3, level = " + level);
		}
		var deck =  new Deck(level);
		deck.ReadFile(Path.of("Card_List_Lv" + level + ".txt"));
		deck.size = deck.deck.size();
		Collections.shuffle(deck.deck);
		return deck;
	}
	
	public static Deck setTileDeck() throws IOException {
		var deck =  new Deck(0);
		deck.ReadFile(Path.of("Noble_List.txt"));
		deck.size = deck.deck.size();
		Collections.shuffle(deck.deck);
		return deck;
	}
	
	public void ReadFile(Path path) throws IOException {/*Lit un fichier et associe chaque ligne du fichier à carte*/
		try(var reader = Files.newBufferedReader(path)){
			String line;
			while((line = reader.readLine()) != null) {
				var card = Card.stringToCard(line);
				if(card.color() == GameColor.NOBLE && level != 0) {
					throw new IllegalStateException("card is a tile but deck is a card deck");
				}
				deck.add(card);
			}
		}
		return;
	}
	
	@Override
	public String toString() {
		return deck.stream().map(c->c.toString()).collect(Collectors.joining("\n"));
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(deck.hashCode(), size, level);
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof Deck deckt && deckt.deck.equals(deck) && deckt.size == size && deckt.level == level;
	}
	
	public int size() {
		return size;
	}
	
	public int level() {
		return level;
	}
	
	public static void main(String[] args) {
		try {
			var dk = Deck.setCardDeck(1);
			System.out.println(dk);
			System.out.println("size of = " + dk.size());
		}catch(IOException e) {
	    	 System.err.println(e.getMessage());
	    	 System.exit(1);
	    	 return;
	     }
	}
	
}
