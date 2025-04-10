package fr.umlv.data;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Board {
	private final int nbrPlayer;
	private final TokenBank bank;
	private final TileBoardLine tileBoardLine;
	private final LinkedHashMap<Integer, CardBoardLine> board;/*level1 = 1, level2 = 2, level3 = 3*/
	private final HashMap<Integer, Deck> decks;/*level1 = 1, level2 = 2, level3 = 3*/
	
	
	public Board(int nbrPlayer) throws IOException {
		if(nbrPlayer < 2 || nbrPlayer > 4) {
			throw new IllegalArgumentException("nbrPlayer < 2 || nbrPlayer > 4, nbrplayer = " + nbrPlayer);
		}
		this.nbrPlayer = nbrPlayer;
		bank = TokenBank.setTokenBank(Board.nbrPlayerToNbrToken(nbrPlayer), 5);
		board = new LinkedHashMap<>();
		decks = new HashMap<>();
		tileBoardLine = TileBoardLine.setTileBoardLine();
	}
	
	public static Board setBoard(int nbrPlayer) throws IOException {
		var bd = new Board(nbrPlayer);
		IntStream.range(1, 4).forEach(i-> {try { bd.decks.put(4 - i, Deck.setCardDeck(4 - i));} 
		catch (IOException e) {
			System.err.println(e.getMessage());
	    	System.exit(1);
	    	return;
		} bd.board.put(4 - i, CardBoardLine.setCardBoardLine(bd.decks.get(4 - i)));});
		return bd;
	}
	
	public Token drawTokenFromBoardBank(GameColor color) {
		return bank.drawTokenFromTokenBank(color);
	}
	
	public int tokenLeftInBoardBank(Token token) {
		return bank.tokenLeft(token);
	}
	
	public Map<Token, Integer> priceOfCard(int level, int pos) {
		if(level < 1 || level > 3) {
			throw new IllegalArgumentException("level < 1 || level > 3, level = " + level);
		}
		if(pos < 0 || pos > 3) {
			throw new IllegalArgumentException("pos < 0 || pos > 3, pos = " + pos);
		}
		return board.get(level).getCardFromBoardLine(pos).price().tokenBank();
	}
	
	public Map<Token, Integer> priceOfTile( int pos) {
		if(pos < 0 || pos > 3) {
			throw new IllegalArgumentException("pos < 0 || pos > 3, pos = " + pos);
		}
		return tileBoardLine.getTileFromBoardLine(pos).price().tokenBank();
	}
	
	public Card removeTileFromBoard(int pos) {
		if(pos < 0 || pos > 3) {
			throw new IllegalArgumentException("pos < 0 || pos > 3, pos = " + pos);
		}
		return tileBoardLine.removeTileFromTileBoardLine(pos);
	}
	
	public Card removeCardFromBoard(int level, int pos) {
		if(level < 1 || level > 3) {
			throw new IllegalArgumentException("level < 1 || level > 3, level = " + level);
		}
		if(pos < 0 || pos > 3) {
			throw new IllegalArgumentException("pos < 0 || pos > 3, pos = " + pos);
		}
		return board.get(level).removeCardFromBoardLine(pos, decks.get(level));
	}
	
	public void addTokenToBoardBank(Token token, int nbr) {
		bank.addToken(token, nbr);
	}
	
	@Override
	public String toString() {
		return   nbrPlayer + " players\nBoard :\n" + bank.toString() + "\n" + board.values().stream().map(p-> p.toString() + "\n").collect(Collectors.joining(""));
	}
	
	public static int nbrPlayerToNbrToken(int nbrPlayer) {
		return switch(nbrPlayer) {
		       case 2 -> 4;
		       case 3 -> 5;
		       case 4 -> 7;
		       default-> throw new IllegalArgumentException("nbr player must be between 2 and 4");
		};
	}
	
	public Map<Integer, CardBoardLine> cardBoardLines(){
		return Collections.unmodifiableMap(board);
	}
	
	public List<Card> tileList(){
		return tileBoardLine.tileList();
	}
	
	public int nbrCardLeftInDeck(int level) {
		if(level < 0 || level > 3) {
			throw new IllegalArgumentException("level < 0 || level > 3, level = " + level);
		}
		return decks.get(level).size();
	}
	
	public Map<Token, Integer> bank(){
		return bank.tokenBank();
	}
	
	public static void main(String[] args) {
		try {
		var bd = setBoard(2);
		System.out.println(bd);
		}catch (IOException e) {
			System.err.println(e.getMessage());
	    	System.exit(1);
	    	return;
		}
	}
}
