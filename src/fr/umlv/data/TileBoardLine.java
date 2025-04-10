package fr.umlv.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TileBoardLine {
	private final ArrayList<Card> tileBoardLine;
	private int size;
	
	public TileBoardLine() {
		tileBoardLine = new ArrayList<>();
		size = 0;
	}
	
	public Card removeTileFromTileBoardLine(int pos) {
		if(pos < 0 || pos > 3) {
			throw new IllegalArgumentException("pos < 0 || pos >= size but pos = " + pos);
		}
		var tile = tileBoardLine.get(pos);
		tileBoardLine.add(pos, null);
		return tile;
	}
	
	public void add(Card card) {/*ajoute à la CardBoardLine la carte en parametre*/
		Objects.requireNonNull(card, "card is null");
		if(card.color() != GameColor.NOBLE) {
			throw new IllegalStateException("card.color() != GameColor.NOBLE, card = " + card.toString());
		}
		size++;
		tileBoardLine.add(card);
	}
	
	public Card getTileFromBoardLine(int pos) {/*renvoie la carte la position pos*/
		if(pos < 0 || pos > 3) {
			throw new IllegalArgumentException("pos < 0 || pos > 3 but pos = " + pos);
		}
		return tileBoardLine.get(pos);
	}
	
	public static TileBoardLine setTileBoardLine() throws IOException{
		var tbl = new TileBoardLine();
		var deck = Deck.setTileDeck();
		IntStream.range(0, 4).forEach(i->tbl.add(deck.draw()));
		return tbl;
	}
	
	public int size() {
		return size;
	}
	
	public List<Card> tileList(){
		return Collections.unmodifiableList(tileBoardLine);
	}
	
	@Override
	public String toString() {
		if(tileBoardLine.isEmpty()) {
			return "empty";
		}
		return tileBoardLine.stream().map(p->p.toString()).collect(Collectors.joining(", "));
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(tileBoardLine.hashCode(), size);
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof TileBoardLine tbl && tbl.tileBoardLine.equals(tileBoardLine) && tbl.size == size;
	}
	
}
