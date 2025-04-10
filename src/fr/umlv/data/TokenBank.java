package fr.umlv.data;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class TokenBank {
	private final HashMap<Token, Integer> tokenBank;
	
	public TokenBank() {
		this.tokenBank = new HashMap<>();
	}
	
	public static TokenBank setTokenBank(int nbrToken, int nbrGoldToken) {/*creer pour chaque couleur une pile de nbrjeton*/
		var tb = new TokenBank();
		if(nbrToken < 0) {
			throw new IllegalArgumentException("nbrJeton < 0");
		}
		Arrays.asList(GameColor.values()).stream().filter(c->c !=GameColor.NOBLE && c != GameColor.GOLD).map(c->new Token(c)).forEach(t-> tb.addToken(t, nbrToken));
		tb.addToken(new Token(GameColor.GOLD), nbrGoldToken);
		return tb;
	}
	
	public void addToken(Token token, int nbr) {/*ajoute le jeton à la bank*/
		Objects.requireNonNull(token, "jeton is null");
		tokenBank.compute(token, (k, v)-> v == null? nbr: v + nbr);
	}
	
	public Token drawTokenFromTokenBank(GameColor color) {/*pioche un jeton de la color de la bank*/
		var token = new Token(Objects.requireNonNull(color));
		if(tokenBank.get(token) < 1) {
			return null;
		}
		tokenBank.compute(token, (k, v)-> v - 1);
		return token;
	}
	
	public void removeTokenFromTokenBank(Token token, int nbr) {
		Objects.requireNonNull(token);
		if(nbr > tokenBank.get(token)) {
			throw new IllegalArgumentException("nbr > tokenBank.get(token), nbr = " + nbr);
		}
		tokenBank.compute(token, (k, v)-> v - nbr);
	}
	
	public Map<Token, Integer> tokenBank(){
		return Collections.unmodifiableMap(tokenBank);
	}
	
	public int tokenLeft(Token token) {
		Objects.requireNonNull(token, "token is null");
		return tokenBank.get(token);
	}

	@Override
	public String toString() {
		return tokenBank.keySet().stream().map(t->tokenBank.get(t) + " " + t).collect(Collectors.joining(" "));
	}
	
	public static void main(String[] args) {
		var tb = TokenBank.setTokenBank(5, 5); 
		System.out.println(tb.toString());
		var tb2 = TokenBank.setTokenBank(3, 3);
		System.out.println(tb2.toString());
		//var p1 = TokenBank.setTokenBank(3);
		//var p2 = TokenBank.setTokenBank(3);
		//System.out.println(GameAction.canBuyCard(tb, p1, p2));
	}
}
