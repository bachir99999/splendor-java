package fr.umlv.data;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import fr.umlv.zen5.Application;
import fr.umlv.zen5.ApplicationContext;
import fr.umlv.zen5.Event;
import fr.umlv.zen5.Event.Action;
import fr.umlv.zen5.ScreenInfo;

public class Zen5Graph {
	  
	private static void graphMiniToken(ApplicationContext context, Token token, float x, float y, int nbr) {
			  context.renderFrame(graphics ->{
				  graphics.setColor(gameColorToAwtColor(token.color()));
			      graphics.fill(new Ellipse2D.Float(x - 7, y - 7, 15, 15));
			      graphics.setColor(Color.PINK);
			      graphics.drawString(Integer.toString(nbr), x - 2, y + 5);
				  
			  });
		  }
	   
	   private static void graphMiniTokenBank(ApplicationContext context, Map<Token, Integer> tokenbank, float x, float y) {
		   var i = 10;
		   for(var k : tokenbank.keySet()) {
			   if(tokenbank.get(k) == 0) {
				   continue;
			   }
			   graphMiniToken(context, k, x + i, y + 10, tokenbank.get(k));
			   i+=15;
		   }
	   }
 	 
	   private static void graphCard(ApplicationContext context, Card card, float x, float y) {
		   context.renderFrame(graphics->{
			   graphics.setColor(gameColorToAwtColor(card.color()));
			   graphics.drawRect((int)x, (int)y, 100, 150);
			   graphics.setFont(new Font(null, Font.PLAIN, 54));
			   graphics.drawString(Integer.toString(card.prestige()), x + 35, y + 90);
			   graphMiniTokenBank(context, card.price().tokenBank(), x, y);
		   });
	   }
	   
	   private static void graphToken(ApplicationContext context, Token token, float x, float y, int nbr) {
			  context.renderFrame(graphics ->{
				  graphics.setColor(gameColorToAwtColor(token.color()));
			      graphics.fill(new Ellipse2D.Float(x - 35, y - 35, 75, 75));
			      graphics.setColor(Color.PINK);
			      graphics.setFont(new Font(null, Font.PLAIN, 54));
			      graphics.drawString(Integer.toString(nbr), x - 12, y + 25);
				  
			  });
	   }
	   
	   private static void graphMiniCardStack(ApplicationContext context, GameColor color, float x, float y, int nbr) {
		   context.renderFrame(graphics->{
			   graphics.setColor(gameColorToAwtColor(color));
			   graphics.drawRect((int)x, (int)y, 25, 37);
			   graphics.setFont(new Font(null, Font.PLAIN, 30));
			   graphics.drawString(Integer.toString(nbr), x + 5, y + 30);
		   });
	   }
	  
	   private static void graphDeck(ApplicationContext context, int nbrCardLeft, int level,float x, float y) {
		   context.renderFrame(graphics->{
			   graphics.setColor(Color.DARK_GRAY);
			   graphics.drawRect((int)x, (int)y, 100, 150);
			   graphics.drawString(Integer.toString(nbrCardLeft) + " cartes", x + 25, y + 120);  
			   graphics.drawString("restantes", x + 25, y + 130);
			   graphics.drawString("DECK LEVEL", x + 15, y + 45);
			   graphics.setFont(new Font(null, Font.PLAIN, 54));
			   graphics.drawString(Integer.toString(level), x + 35, y + 90);
			 
		   });
	 }
	  
	   private static void graphPlayerCardHand(ApplicationContext context, Player player, float x, float y) {
		 var d = 5;
		 for(var c : GameColor.values()) {
			 if(c != GameColor.NOBLE && c != GameColor.GOLD) {
				 graphMiniCardStack(context, c,  x + d,  y, player.nbrOfColorCardPlayer(c));
				 d+=30;
			 }
		 }
	 }
	 
	   private static void graphTile(ApplicationContext context, Card tile, float x, float y) {
		   context.renderFrame(graphics->{
			   graphics.setColor(Color.CYAN);
			   graphics.drawRect((int)x, (int)y, 100, 100);
			   graphics.setFont(new Font(null, Font.PLAIN, 54));
			   graphics.drawString(Integer.toString(tile.prestige()), x + 35, y + 50);
			   var i = 5;
			   for(var k : tile.price().tokenBank().keySet()) {
				   if(tile.price().tokenLeft(k) > 0) {
				   graphMiniCardStack(context, k.color(), x + i,  y + 60, tile.price().tokenLeft(k));
				   i+=30;
				   }
			   }
		   });
	 }
	 
	   private static void graphMiniTileStack(ApplicationContext context, float x, float y, int nbr) {
		   context.renderFrame(graphics->{
			   graphics.setColor(Color.DARK_GRAY);
			   graphics.drawRect((int)x, (int)y, 25, 25);
			   graphics.setFont(new Font(null, Font.PLAIN, 30));
			   graphics.drawString(Integer.toString(nbr), x + 5, y + 24);
		   });
	 }
	 
	   private static void graphMiniCard(ApplicationContext context, Card card,float x, float y) {
		 context.renderFrame(graphics->{ 
		   graphics.setColor(gameColorToAwtColor(card.color()));
		   graphics.drawRect((int)x, (int)y, 60, 90);
		   graphMiniTokenBank(context, card.price().tokenBank(), x - 3, y);
		   graphics.setFont(new Font(null, Font.PLAIN, 30));
		   graphics.drawString(Integer.toString(card.prestige()), x + 20, y + 50);
		 });
	 }
	 
	   private static void graphPlayer(ApplicationContext context, Player player, float x, float y) {
		   context.renderFrame(graphics->{
			   graphics.setColor(Color.DARK_GRAY);		
			   graphics.setFont(new Font(null, Font.PLAIN, 30));
			   graphics.drawString(player.name(), x + 50, y);
			   graphics.setFont(new Font(null, Font.PLAIN, 40));
			   graphics.drawString(Integer.toString(player.totalPrestige()), x, y);
			   graphMiniTokenBank(context, player.playerBank(), x, y + 10);
			   graphPlayerCardHand(context, player, x, y + 40);
			   graphMiniTileStack(context, x + 160, y + 10, player.nbrTileOfPlayer());
			   var i = 5;
			   for(var c : player.reservedCard()) {
				   graphMiniCard(context, c, x + i, y + 90);
				   i+=65;
			   }
		   });
	 }
	 
	   private static void graphTokenBank(ApplicationContext context, Map<Token, Integer> tokenbank, float x, float y) {
		   var i = 0;
		   for(var k : tokenbank.keySet()) {
			   graphToken(context, k, x, y + i, tokenbank.get(k));
			   i+=150;
		   }
	  }
	  
	   private static void graphCardBoard(ApplicationContext context, Map<Integer, CardBoardLine> cardBoard ,float x, float y) {
		  var i = 0;
		  var j = 0;
		  for(var b : cardBoard.values()) {
			  for(var c : b.cardList()) {
				  graphCard(context, c, x + i, y + j);
				  i+=150;
			  }
			  i=0;
			  j+=160;
		  }
	  }
	 
	   private static void graphTileBoardLine(ApplicationContext context, List<Card> tileList ,float x, float y) {
		   var i = 0;
		   for(var t : tileList) {
			   graphTile(context, t, x + i, y);
			   i+=150;
		   }
	 }
	 
	   private static void graphGame(ApplicationContext context, List<Player> listPlayer, Board board) {
		 var i = 0; 
		 for(var p : listPlayer) {
			 graphPlayer(context, p, 20, 50 + i);
			 i+=250;
		 }
		 graphTokenBank(context, board.bank(), 400, 50);
		 graphCardBoard(context, board.cardBoardLines(), 650, 300);
		 graphTileBoardLine(context, board.tileList(), 650, 100);
		 for(i = 1; i <= 3; i++) {
			 graphDeck(context, board.nbrCardLeftInDeck(i), i, 490, 630 - (i-1)*165);
		 }
	 }
	 
	   private static void graphTurnOf(ApplicationContext context, int indexPlayer) {
		 context.renderFrame(graphics->{ 
			 graphics.setColor(Color.RED);	
			 graphics.setFont(new Font(null, Font.PLAIN, 40));
			 graphics.drawString("<--", 140, 55 + (indexPlayer*250));
		 });
	}
	 
	   private static void graphButton(ApplicationContext context, List<String> buttons ,float x, float y) {
		   context.renderFrame(graphics->{
			   graphics.setColor(Color.WHITE);
			   graphics.setFont(new Font(null, Font.PLAIN, 35));
			   var i = 0;
			   for(var k : buttons) {
				   graphics.drawRect((int)x, (int)y + i*50, 160, 50);
				   graphics.drawString(k, x + 10, y + 35 + i*50);
				   i++;
			   }
		   });
	 }
	 
	   private static int selectOption(ApplicationContext context, List<String> buttons ,float x, float y) {
		  graphButton(context, buttons , x + 10, y); 
		  Event event = null;
		  while(event == null || event.getAction() != Action.POINTER_UP) { event = context.pollOrWaitEvent(10); }
		  Point2D.Float location = event.getLocation();
		  if(location != null && location.x >= x+10 && location.x <= x + 160) {
		  for(var i = 1; i <= buttons.size(); i++) {
			  if(location.y >= y + (i-1)*50 && location.y <= y + i*50) {
				  return i-1;
			  }  
		  }
		  }
		  return -1;
	  }
	 
	   public static Color gameColorToAwtColor(GameColor color) {
		  Objects.requireNonNull(color, "color is null");
		  return switch(color) {
		  	case BLACK -> Color.BLACK;
		  	case RED -> Color.RED;
		  	case GREEN -> Color.GREEN;
		  	case WHITE -> Color.WHITE;
		  	case BLUE -> Color.BLUE;
		  	case GOLD -> Color.YELLOW;
		  	default -> throw new IllegalArgumentException("unknown color, color = " + color);
		  };		  		  
	  }
	  	  	 	  
	   private static int locationToType(float x, float y) {
		  if(x >= 365 && x <= 440 && y >= 15 && y <= 836) {
			  return 1;
		  }
		  
		  if(x >= 650 && x <= 1250 && y >= 300 && y <= 780) {
			  return 2;
		  }
		  
		  if(x >= 650 && x <= 1250 && y >= 100 && y <= 220) {
			  return 3;
		  }
		  return -1;
	  }
	  
	   private static GameColor locationToGameColor(float x, float y, Map<Token, Integer> tokenbank) {
		  var i = 0;
		  if(x >= 365 && x <= 440) {
			  for(var k : tokenbank.keySet()) {
				   if(y >= 50 + i && y <= 125 + i) {
					   return k.color().equals(GameColor.GOLD) ? null : k.color();
				   }
				   i+=150;
			   }
		  }
		  return null;
	  }
	  
	   private static int locationToCardLevel(float y) {
		  if(y >= 300 && y <= 450) {return 3;}
		  if(y >= 460 && y <= 610) {return 2;}
		  if(y >= 620 && y <= 770) {return 1;}
		  return -1;  
	  }
	  
	   private static int locationToCardIndex(float x) {
		  if(x >= 650 && x <= 750) {return 0;}
		  if(x >= 800 && x <= 900) {return 1;}
		  if(x >= 950 && x <= 1050) {return 2;}
		  if(x >= 1100 && x <= 1200) {return 3;} 
		  return -1;  
	  }
	  
	   private static Set<GameColor> selectValidColor(ApplicationContext context, int nbrColor, Map<Token, Integer> tokenbank){
		  Event event = null;
		  var set = new HashSet<GameColor>();
		  while(set.size() < nbrColor) {
			event = context.pollOrWaitEvent(10);
		    while(event == null || event.getAction() != Action.POINTER_UP) { event = context.pollOrWaitEvent(10); }
		    Point2D.Float location = event.getLocation();
			var color = locationToGameColor(location.x, location.y, tokenbank);
			while(color == null || set.contains(color)) {
				event = null;
				while(event == null || event.getAction() != Action.POINTER_UP) { event = context.pollOrWaitEvent(10); }
			    location = event.getLocation();
				color = locationToGameColor(location.x, location.y, tokenbank);
				}
			System.out.println("ajout couleur"+ color);
			graphToken(context, new Token(color), 325 + set.size()*75, 1000, 1);
			set.add(color);
			}
		 return set;
	  }
	   
	   private static void clearScreen(ApplicationContext context, float width, float height, Color color) {
	      context.renderFrame(graphics -> {
		        graphics.setColor(color);
		        graphics.fill(new  Rectangle2D.Float(0, 0, width, height));
		      });
	  }
	    	  	
	   private static boolean doOptionTile(Board board, Player player, int option, int pos) {
	  		if(option == 0) {
	  			if(!GameAction.canBuyTile(player, board.priceOfTile(pos))) {return false;}
				GameAction.buyTile(player, board, pos);
	  		}
	  		else {return false;}
	  		return true;
	  	}
	  
	   private static boolean doOptionCard(Board board, Player player, int option, int level, int pos) {
			Objects.requireNonNull(board, "board is null");
			Objects.requireNonNull(player, "player is null");
			switch(option) {
				case 0->{ 
					if(!GameAction.canBuyCard(player, board.priceOfCard(level, pos))) {return false;}
					GameAction.buyCard(player, board, level, pos);
					return true;
				}
				case 1->{ 
					GameAction.reserveCard(player, board, level, pos);
					return true;
				}
			}
			return false;
		}
	  
	   private static boolean doAction(ApplicationContext context, Player player, Board board, Point2D.Float location) {
			var buttons1 = List.<String>of("Acheter", "Reserver");
			var buttons2 = List.<String>of("Piocher 2", "Piocher 3");
			var buttons3 = List.<String>of("Acheter");
			var action = locationToType(location.x, location.y);
			var level = locationToCardLevel(location.y);
			var index = locationToCardIndex(location.x);
			switch(action) {
				case 1->{ 
					var opt = selectOption(context, buttons2, 325, 900);
        			if(opt == -1) {return false;}
        			GameAction.drawToken(selectValidColor(context, opt > 0 ? 3 : 1, board.bank()), board, player);
        			return true;	
        		}			
				case 2-> {if(level > 0 && index >= 0) {return doOptionCard(board, player, selectOption(context, buttons1, location.x, location.y), level, index);}}
				case 3-> {if(index >= 0) {return doOptionTile(board, player, selectOption(context, buttons3, location.x, location.y), index);}}
			}
			return false;
		}
	  	 
	   	private static void refreshScreen(ApplicationContext context, Board board, List<Player> listPlayer, int indexTurn, ScreenInfo screenInfo) {
			  clearScreen(context, screenInfo.getWidth(), screenInfo.getHeight(), Color.LIGHT_GRAY);
			  graphGame(context, listPlayer, board);
			  graphTurnOf(context, indexTurn);
	   	}
	   
		public static void playGameGraphic(Board board, List<Player> listWinner, List<Player> listPlayer) {
			Objects.requireNonNull(listPlayer, "listPlayer is null");
			Objects.requireNonNull(listWinner, "listWinner is null");
			Objects.requireNonNull(board, "board is null");
			Application.run(Color.LIGHT_GRAY, context -> {			  
			      refreshScreen(context, board, listPlayer, 0, context.getScreenInfo());
				  while(true) {
					  if(!listWinner.isEmpty()) {
						  var winner = GameAction.maxPretige(listWinner);
						  System.out.println("Le gagnant est " + winner.name() + " avec " + winner.totalPrestige() + " de prestige");
						  return;
						}		
					  listPlayer.stream().forEach(p->{
						  graphTurnOf(context, listPlayer.indexOf(p));	
						  Event event = null;
						  while(true) {
							  if ((event = context.pollOrWaitEvent(10)) == null) {continue;}							 
							  if(event.getAction() == Action.POINTER_UP && locationToType(event.getLocation().x, event.getLocation().y) >= 0) {
								  if(!doAction(context, p, board, event.getLocation())) {refreshScreen(context, board, listPlayer, listPlayer.indexOf(p), context.getScreenInfo()); continue;} 
								  break;
							  }
						  }
						  if(p.totalPrestige() >= 15) {listWinner.add(p);}
						  clearScreen(context, context.getScreenInfo().getWidth(), context.getScreenInfo().getHeight(), Color.LIGHT_GRAY);
						  graphGame(context, listPlayer, board);});
				}});
		}
	  
		  public static void main(String[] args) {
			try {
		    var player = new Player("jondog93");
		    var player2 = new Player("yuri");
		    var player3 = new Player("popo");
		    var player4 = new Player("ipip");
		    var listPlayer = List.of(player, player2, player3, player4);
			var tbd = TileBoardLine.setTileBoardLine();
			var bd = Board.setBoard(4);
			var deck = Deck.setCardDeck(3);
			var buttons1 = List.<String>of("Piocher", "Reserver");
			var buttons2 = List.<String>of("Piocher 2", "Piocher 3");
			player.addTile(tbd.getTileFromBoardLine(0));
			player.addToReserveCard(deck.draw());
			player.addToReserveCard(deck.draw());
			player.addToReserveCard(deck.draw());
			player.addToReserveCard(deck.draw());
			Application.run(Color.LIGHT_GRAY, context -> {
		      
		      // get the size of the screen
		      ScreenInfo screenInfo = context.getScreenInfo();
		      float width = screenInfo.getWidth();
		      float height = screenInfo.getHeight();
		      System.out.println("size of the screen (" + width + " x " + height + ")");
		      
		      context.renderFrame(graphics -> {
		        graphics.setColor(Color.LIGHT_GRAY);
		        graphics.fill(new  Rectangle2D.Float(0, 0, width, height));
		      });
		      
		      for(;;) {

		        Event event = context.pollOrWaitEvent(10);
		        if (event == null) {  // no event
		          continue;
		        }
		        
		        Action action = event.getAction();
		        if (action == Action.KEY_PRESSED || action == Action.KEY_RELEASED) {
		          System.out.println("abort abort !");
		          context.exit(0);
		          return;
		        }
		        //System.out.println(event);
		        
		        Point2D.Float location = event.getLocation();
		        System.out.println("x = " + location.x + " y = " + location.y);
		        //graphToken(context,new Token(GameColor.WHITE) ,location.x, location.y, 5);
		        //graphCard(context, deck.draw(), location.x, location.y);
		        //graphMiniCardStack(context, deck.draw().color(), location.x, location.y, 3);
		        //graphDeck(context, deck, location.x, location.y);
		        //graphTile(context, til, location.x, location.y);
		        //graphPlayer(context, player, location.x, location.y);
		        //graphMiniCard(context, deck.draw(),location.x, location.y);
		        graphGame(context, listPlayer, bd);
		        graphTurnOf(context, 1);		       
		        if(action == Action.POINTER_UP) { 
		        	switch(locationToType(location.x, location.y)) {
		        		case 1->{		        			
		        			var opt = selectOption(context, buttons2, 325, 900);
		        			System.out.println("---------->" + opt);
		        			if(opt != -1) {selectValidColor(context, opt > 0 ? 3 : 1, bd.bank());}
		        			clearScreen(context, width, height, Color.LIGHT_GRAY);
	    				    graphGame(context, listPlayer, bd);
		        			}
		        		case 2->{
		        			var level = locationToCardLevel(location.y);
		        			var index = locationToCardIndex(location.x);
		        			if(level > 0 && index >= 0) {
		        				var opt = selectOption(context, buttons1, location.x, location.y);
		        				System.out.println("---------->" + opt);
		        				System.out.println(bd.cardBoardLines().get(level).getCardFromBoardLine(index));
		        				clearScreen(context, width, height, Color.LIGHT_GRAY);
		    				    graphGame(context, listPlayer, bd);
		        			}
		        		}
		        		case 3->{}		      
		        	}

		        }
		      }
		    });
			}catch(IOException e){
			    System.err.println(e.getMessage());
			    System.exit(1);
			    return;
			     }
		  }
}
