package garbage;


import CardRating;
import Player;
import Table;

import java.util.ArrayList;
import java.util.List;

public class PokerManager {
	

	private static int roundsPlayed=0;
	private static CardRating cardRating = new CardRating();
	
	
	public static void main(String[] args) {
		
		
		while(roundsPlayed < 1) {
			System.out.println("Round " + roundsPlayed);

			
			initializeBlinds();
			dealStartingHandForPlayers();
			printCardsForPlayers();
			
			dealFlop();
			table.printCards();
			
			
			while (numOfFolds <3) {
				//while more than 1 player left and raises <3
				for (Player player : players) {
					int [] powerRatings = getPowerRatingArrayForPLayer(player);
					if (!player.hasFolded()) {
						player.makeBettingDecision(powerRatings);
					}
					System.out.println(player.getId() + ": " + player.getStatusForPlayer());
					System.out.println("current bet: " + table.bet);
				}
				
			}
			

			System.out.println(table.getPot());
			table.printCards();
			
			
			// river card
			table.dealCard(deck.dealCard());
			table.printCards();
			
			// turn card
			table.dealCard(deck.dealCard());
			table.printCards();
			
			
			roundsPlayed++;
		}
	}
		
	
	
	
	private static void initializeBlinds() {
		p1.setSmallBlind(true);
		p2.setBigBlind(true);
		table.RaisePot(p1.raise(smallBlind)); 
		table.RaisePot(p2.raise(bigBlind));
	}

	private static void printCardsForPlayers() {
		for (Player player : players) {
			player.printCards();
		}
	}

	
	
	public static Table getTable() {
		return table;
	}
	
}
