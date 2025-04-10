package fr.umlv.data;
import java.util.Objects;

public record Token(GameColor color) {

	public Token {
		if(color == GameColor.NOBLE) {/*un Token ne peut pas etre de type noble*/
			throw new IllegalArgumentException("color == GameColor.NOBLE");
		}
		Objects.requireNonNull(color, "color is null");
	}
	
	@Override
	public String toString() {
		return color.toString();	
	}
}
