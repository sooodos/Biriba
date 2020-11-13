/**
 * @author Sotiris Loizidis - 917309
 */
import java.util.Scanner;

public class PlayingBiriba {

	private static final String[] Suit = { "Clubs", "Diamonds", "Hearts", "Spades" };
	private static final String[] Rank = { "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King",
			"Ace" };

	public static String[] generateDoubleDeck() {
		String[] deck = new String[52];
		int count = 0;
		for (int r = 0; r < Rank.length; r++)
			for (int s = 0; s < Suit.length; s++) {
				deck[count] = Rank[r] + " of " + Suit[s];
				count++;
			}
		String[] doubleDeck = new String[2 * (deck.length + 1)];
		doubleDeck[0] = "Joker";
		doubleDeck[1] = "Joker";
		count = 2;
		for (int i = 1; i <= 2; i++)
			for (int j = 0; j < deck.length; j++) {
				doubleDeck[count] = deck[j];
				count++;
			}
		return doubleDeck;
	}

	public static void shuffleDeck(String[] deck) {
		for (int i = 1; i <= 1000; i++) {
			int j = (int) (Math.random() * deck.length);
			int k = (int) (Math.random() * deck.length);
			String s = deck[j];
			deck[j] = deck[k];
			deck[k] = s;
		}
	}

	public static String getSuit(String card) {
		if (card.contains("Clubs"))
			return "Clubs";
		else if (card.contains("Diamonds"))
			return "Diamonds";
		else if (card.contains("Hearts"))
			return "Hearts";
		else if (card.contains("Spades"))
			return "Spades";
		else if (card.equals("Joker"))
			return "Joker";
		return "";
	}

	public static String getRank(String card) {
		if (card.equals("Joker"))
			return "Joker";
		int i = card.indexOf(" of ");
		return card.substring(0, i);
	}

	public static void display(String[] cards) {
		for (String c : cards)
			System.out.println(c);
	}

	/**
	 * This method is used to draw a card from the deck.
	 * 
	 * @param Deck
	 *            The playing deck
	 * @return returns the card drawn.
	 */
	public static String turnTopCard(String[] Deck) {
		for (int i = 0; i < Deck.length; i++) {
			if (!Deck[i].equals("empty")) { // iterate throught the deck to find
											// the first instance of a card that
											// hasn't yet be drawn
				String topCard = Deck[i];// the card that will be returned
				Deck[i] = "empty";// render the card unavailable.
				return topCard;
			}
		}
		return "Empty"; // The deck is empty
	}

	/**
	 * This method is used to deal the 10 starting cards to the player/computer.
	 * 
	 * @param Player
	 *            The hand of the player
	 * @param Computer
	 *            The hand of the computer
	 * @param Deck
	 *            The playig deck
	 */
	public static void dealStartingHands(String[] Player, String[] Computer, String[] Deck) {
		int playercount = 0;// number of cards dealt to the player
		int computercount = 0;// number of cards dealt to the computer
		for (int i = 0; i < 20; i += 4) {// we will deal 20 cards 5 rounds of
											// dealing
											// two to each player
			Player[playercount++] = Deck[i];// deal the first card
			Player[playercount++] = Deck[i + 1];// deal the second card
			Computer[computercount++] = Deck[i + 2];// deal the first card
			Computer[computercount++] = Deck[i + 3];// deal the second card
		}
		for (int i = 0; i < 20; i++) {// render the first 20 cards unavailable
										// since we dealt them.
			Deck[i] = "empty";
		}
	}

	/**
	 * This method is true only if the given hand posseses 10 cards in a 10-card
	 * range of Rank.
	 * 
	 * @param Hand
	 *            The hand of the player
	 * @return
	 */
	public static boolean isWin(String[] Hand) {
		if (findBest10(Hand) == Hand.length)
			return true;
		return false;
	}

	/**
	 * Returns the index where the element of the given table is equal to the
	 * keyword.
	 * 
	 * @param table
	 *            The table can be either Rank or Suit
	 * @param keyword
	 *            The keyword can be eithe a rank or a suit
	 * @return
	 */
	public static int returnIndex(String[] table, String keyword) {
		for (int i = 0; i < table.length; i++) {
			if (table[i].equals(keyword))
				return i;
		}
		return -1;// element not found in table
	}

	/**
	 * This method encodes the deck given to a 2d table with columns
	 * representing all Suits and rows representing all Ranks. The positions of
	 * the table that have 1 declare that the given hand contains that card.
	 * 
	 * @param deck
	 *            a player's hand
	 * @return the 2d array encoding
	 */
	public static int[][] deckStatus(String[] deck) {
		int[][] deckStats = new int[Rank.length][Suit.length];
		for (int i = 0; i < deck.length; i++) {
			String cardSuit = getSuit(deck[i]);
			String cardRank = getRank(deck[i]);
			/*encode all cards except the joker.*/
			if (!cardRank.equals("Joker")) {
				/*find the row index by finding in which index of the Rank table the current Rank lies.*/
				int rowIndex = returnIndex(Rank, cardRank);
				int columnIndex = returnIndex(Suit, cardSuit);// same logic for
																// the Suit.
				deckStats[rowIndex][columnIndex] = 1;
			}
		}
		return deckStats;
	}

	/**
	 * Given the deckStats make a 1d array containing how many cards the current
	 * deck contains in each Suit.
	 * 
	 * @param deckStats
	 * @return
	 */
	public static int[] suitStats(int[][] deckStats) {
		int[] suitStats = new int[Suit.length];// an array containing the
												// counters for all four suits.
		for (int i = 0; i < Suit.length; i++)// iterate all ranks for current
												// suit then do the same for the
												// next
			for (int j = 0; j < Rank.length; j++)
				if (deckStats[j][i] == 1)// the card exists
					suitStats[i]++;
		return suitStats;
	}

	/**
	 * Reuturns the index of the Suit in the Suit array with the highest count.
	 * Really simple logic since we just initialize the best suit with zero and
	 * then replace if we find a higher count and return that after iterating
	 * through all four counts.
	 * 
	 * @param suitStats
	 * @return
	 */
	public static int bestSuitIndex(int[] suitStats) {
		int bestSuitIndex = 0;// the index that will be returned.
		int best = 0;
		for (int i = 0; i < Suit.length; i++) {
			if (suitStats[i] > best) {
				bestSuitIndex = i;
				best = suitStats[i];
			}
		}
		return bestSuitIndex;
	}

	/**
	 * This method has one goal. To return the highest possible 10-card score
	 * for a deck. If for instance we have the cards 2 to 10 in the same Suit
	 * the score will be 9 and it means that we only need one more correct card
	 * (The joker, the Ace or the Jack) to complete 10 consecutive cards. We
	 * have thirteen possible 10-card groups. 2-Jack, 3-Queen, 4-King.... Ace -
	 * 10 We check for all of them to find the highest sum of cards that exist
	 * in that range. For example we will check for 2 up to Jack and see that we
	 * have 3 cards. But if we check from 5 to Ace we find out that we have 6
	 * out of 10 cards and after trying out for all possible 10-card groups we
	 * find out that, that is ndeed our best group and we return that in order
	 * to have it as reference point, when we later try to replace with a new
	 * card, our nex score will be higher than 6 and so forth.
	 * 
	 * @param deck
	 * @return
	 */
	public static int findBest10(String[] deck) {
		int[][] deckStats = deckStatus(deck);// get the deck stats
		int[] suitStats = suitStats(deckStats);// get the suit stats
		int bestSuitIndex = bestSuitIndex(suitStats);// suit with most cards.
		/*The jokers count as valid cards in any possible 10-card group.*/
		int best10 = numberOfJokers(deck);
		/*13 possible 10 card groups, each starting and ending one card further each time.*/
		for (int i = 0; i < Rank.length; i++) {
		/* each current score starts once again with the number of jokers.*/
			int currentStreak = numberOfJokers(deck);
			/*start from the current rank of the outer iteration and go 10 further to calculate the next 10-card group.*/
			for (int j = i; j < i + deck.length; j++)
				/* add one if the card exists and zero otherwise. modulo is used to wrap around the ranks.*/
				currentStreak += deckStats[j % Rank.length][bestSuitIndex];
			if (currentStreak > best10)// found a better 10-card group.
				best10 = currentStreak;// make that the new best
		}
		return best10;
	}

	/**
	 * This method is responsible for attempting to switch the card on the pile
	 * or on top of the deck by re-evaluating the 10-card group counter until a
	 * better card group is found. When a better group is found, the newCard
	 * replaces the card that was in the current iteration and got switched. The
	 * card that is tossed is retured in order to put it on top of the pile.
	 * 
	 * @param deck
	 * @param newCard
	 * @return reuturn the card that will be tossed on top of the pile, or null
	 *         if the newCard will not replace any of the existing cards.
	 */
	public static String switchCard(String deck[], String newCard) {
		String throwCard = null;
		int currentStreak = findBest10(deck);// find the initial best 10-card
												// group
		for (int i = 0; i < deck.length; i++) {
			String current = deck[i];
			replace(current, newCard, deck);// attempt a replacement with all
											// existing cards of the deck until
											// a better 10-card group is found
			int newStreak = findBest10(deck);
			if (newStreak > currentStreak) {// the new streak we made by
											// replacing deck[i] with the
											// newCard is the new bestStreak so
											// we stop.
				throwCard = current;
				break;// the reason we breeak immediately is because a single
						// card can only increment the best 10 card group by
						// one. there is not a chance that will find a card to
						// be replaced that will make the group even better.
						// only equal so we choose to stop for time efficiency.
			} else
				replace(newCard, current, deck);// the card of the deck when
												// replaced doesnot make the
												// streak better. hence we
												// replace it back and continue.
		}
		return throwCard;// return the card that is tossed or null if no switch
							// was made.
	}

	/**
	 * Simple method that displays first who won, their deck, and their sorted
	 * deck with the values of the jokers used.
	 * 
	 * @param name
	 * @param unsortedHand
	 * @param sortedHand
	 * @param Loser 
	 */
	private static void displayResults(String nameWinner, String[] winner, String[] winnerSorted,String nameLoser, String[] loser) {
		System.out.println("\nThe " + nameWinner + " has won!!");
		System.out.println("The Winning Cards Unsorted");
		display(winner);
		System.out.println();
		System.out.println("The Winning Cards Sorted");
		display(winnerSorted);
		System.out.println("\nThe "+ nameLoser+" cards were:");
		display(loser);
	}

	/**
	 * A simple method to randomly choose who plays first. We make use of
	 * Math.random() in order for each player to have 50% chance to play first.
	 * 
	 * @return
	 */
	private static boolean whoPlaysFirst() {
		double rand = Math.random();
		if (rand > 0.5)
			return false;
		return true;
	}

	/**
	 * Utility function that replaces an existing card in a deck with a new one
	 * outside of the deck.
	 * 
	 * @param toBeReplaced
	 *            The card that should be IN the deck and we want to replace.
	 * @param newCard
	 *            The card that we want to make the new value of toBeReplaced
	 * @param Hand
	 *            The player's deck.
	 * @return true for a successful replacement false if cardToBeReplaced was
	 *         not found.
	 */
	private static boolean replace(String toBeReplaced, String newCard, String[] Hand) {
		for (int i = 0; i < Hand.length; i++) {
			if (Hand[i].equals(toBeReplaced)) {
				Hand[i] = newCard;
				return true;
			}
		}
		return false;
	}

	/**
	 * This method is used only we are going to display the winning hand in
	 * sorted order. We have used a few smart techniques in order to keep this
	 * function compact. The logic behind this is obvious when we visualize the
	 * Rank array. A complete 10-card group only takes 10 out of 13 places. That
	 * leaves three consecutive places empty. Our logic is to locate those three
	 * empty spaces and start displaying the cards from the immediate next
	 * filled position. We again make tremendous use of the modulo since if for
	 * example the empty space were that of the Jack the Queen and the Ace, the
	 * next index would be 13 but we modulo that with 13 so we go to index zero
	 * and start correctly printing from 2!. We then start constructing each
	 * card from the startingIndex we found + 10. Along the way if we miss a
	 * card we can say safely that its value will be completed by the joker
	 * since this method is called when we have a deck that has indeed a
	 * complete 10-card group so we can safely do that.
	 * 
	 * @param deck
	 */
	private static String[] sort(String[] deck) {
		int[][] winningDeckStats = deckStatus(deck);
		int bestSuitIndex = bestSuitIndex(suitStats(winningDeckStats));
		String cardsSuit = Suit[bestSuitIndex];
		int start = 0;
		for (int i = 0; i < Rank.length - 2; i++)
			if (winningDeckStats[i][bestSuitIndex] == 0 && winningDeckStats[i + 1][bestSuitIndex] == 0
					&& winningDeckStats[i + 2][bestSuitIndex] == 0)//
				start = (i + 3) % Rank.length;

		String[] sortedCards = new String[deck.length];
		String sortedCard;
		for (int i = start; i < deck.length + start; i++) {
			if (winningDeckStats[i % Rank.length][bestSuitIndex] == 1)// the card exists so we will construct it
				sortedCard = Rank[i % Rank.length] + " of " + cardsSuit;
			else // the card does not exist so we will use a joker to display it.
				sortedCard = "Joker (" + Rank[i % Rank.length] + " of " + cardsSuit + ")";
			sortedCards[i - start] = sortedCard;// i-start means essentially
												// that we start from zero and
												// go up to deck.length-1
		}
		return sortedCards;
	}

	/**
	 * Utility function that counts how many jokers there are are in a deck.
	 * 
	 * @param deck
	 * @return the amount of jokers
	 */
	private static int numberOfJokers(String deck[]) {
		int count = 0;
		for (int i = 0; i < deck.length; i++) { // lookin thw whole deck
			if (deck[i].equals("Joker")) // each time a joker is found
				count++; // add one to the counter
		}
		return count;
	}

	/**
	 * Driver function of the program. Players take consecutive turns until a
	 * winner prevails.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);// System input
		String[] Deck = null; // The playing deck that the cards will be drawn
								// from
		String Flipped = null; // The card on top of the pile
		String[] Player = new String[10]; // The deck of the human player
		String[] Computer = new String[10]; // The deck of the computer
		boolean PlayerTurn = whoPlaysFirst();// determine who plays first
		boolean playerWin = false; // flag for checking if the player won
		boolean computerWin = false; // flag for checking if the computer won
		Deck = generateDoubleDeck();// generate a double deck
		shuffleDeck(Deck); // shuffle it
		dealStartingHands(Player, Computer, Deck);// deal 10 cards to each
													// player.
		Flipped = turnTopCard(Deck);// flip a new card on top of the 'pile'
		String choice;// user choice 'Y' or 'N'
		String toBeReplaced; //This is the card that the user chooses to toss given from the System.in
		String newCardFromDeck; //This is the newly drawn card from the deck
		if (PlayerTurn)//Display the starting message based on whose turn it is.
			System.out.println("\n>>>> THE GAME BEGINS - THE PLAYER PLAYS FIRST <<<<");
		else
			System.out.println("\n>>>> THE GAME BEGINS - THE COMPUTER PLAYS FIRST <<<<");
		do {
			playerWin = isWin(Player);//check if the user won
			computerWin = isWin(Computer); // check if the compuer won
			if (playerWin) {
				displayResults("USER", Player, sort(Player),"COMPUTER",Computer);//display results with user as winner and compTuer as loser
				break;// break in order to end the game immediately
			}
			if (computerWin) {
				displayResults("COMPUTER", Computer, sort(Computer),"USER",Player);//display results with computer as winner and user as loser
				break;
			}
			if (PlayerTurn) {
				System.out.println(">>> Your cards are:");
				display(Player);
				System.out.println("\nJust Reminding you that the card on the top of the pile is " + Flipped);
				System.out.print("Do you want this card? (Y/N)");
				choice = scan.next();
				if (choice.equals("Y")) {
					System.out.print("Which from your cards are you replacing it with? ");
					do {
						toBeReplaced = scan.nextLine();//scan new input until the user gives a valid card to be replaced
					} while (!replace(toBeReplaced, Flipped, Player));
					Flipped = toBeReplaced;//the new card ontop of the pile is the one that the user tossed from their deck
				} else if (choice.equals("N")) {//the user doesnt want the card on top of the pile
					newCardFromDeck = turnTopCard(Deck);//draw a card fromt the deck
					System.out.print(">>> OK, so do you want the " + newCardFromDeck + " from the deck (Y/N)? ");
					choice = scan.next();
					if (choice.equals("Y")) {// the user wants this card so they have to replace it with one of their existing cards
						System.out.print("Fine. Which from your cards are you replacing it with? ");
						do {
							toBeReplaced = scan.nextLine();
						} while (!replace(toBeReplaced, newCardFromDeck, Player));
						Flipped = toBeReplaced;//the new card ontop of the pile is the one that the user tossed from their deck
					} else if (choice.equals("N")) {
						Flipped = newCardFromDeck;//if the user doesnt want this card either they toss it on top of the pile
					}
				}
				PlayerTurn = !PlayerTurn;//switch turns
				continue;//don't do the computer stuff this round, the user has already played for this round.
			}
			System.out.println();
			/* computer turn */
			String switchedWithFlipped = switchCard(Computer, Flipped);//try to replace one of the cards of the computer with the card on top of the pile
			if (switchedWithFlipped == null) {//if the result is null, the computer did not want to replace
				newCardFromDeck = turnTopCard(Deck);//draw a card from the deck for the computer
				String switchedWithTurnt = switchCard(Computer, newCardFromDeck);//try to replace one of the cards of the computer with the card on top of the deck
				if (switchedWithTurnt == null) {//the computer did not want this card either
					System.out.println("\nC >>> I am throwing the " + newCardFromDeck + " from the deck top\n");
					Flipped = newCardFromDeck;//this means the new card ontop of the pile is the card that was drawn by the computer.
				} else {
					System.out.println("\nC >>> I am getting from the deck top the " + newCardFromDeck
							+ " and I am throwing onto the pile the " + switchedWithTurnt +"\n");
					Flipped = switchedWithTurnt;// the compuet wanted the card that was drawn so the new card on top of the pile is one of the cards from the deck of the computer
				}
			} else {
				System.out.println("\nC >>> I am getting from the top of the pile the " + Flipped
						+ " and I am throwing onto the pile the " + switchedWithFlipped +"\n");
				Flipped = switchedWithFlipped;// the computer wanted the card on top of the pile and took it and the new card on top of the pile is now one of the cards of the deck of the computer that was tossed
			}
			PlayerTurn = !PlayerTurn;//switch turns
		} while (!playerWin && !computerWin);//loop until one of the flags is true
		System.out.println("\n>>>> THANK YOU FOR PLAYING BIRIBA <<<<");
	}

}
