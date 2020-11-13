## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)
* [Game info](#game-info)
* [Implementation info](#implementation-info)

## General info
This project simulates an interactive gameplay of the card game Biriba between a human player and a computer.
	
## Technologies
Project is created with:
* Java
	
## Setup
To run this project, simply downlad PlayingBiriba.java and open a terminal on your current directory with the file in it:

```
$ javac PlayingBiriba.java
$ java PlayingBiriba
```
## Game Info

This program implements a simplified version of the card game ‘Biriba’, which is played with a human player and a computer-controlled player. The winner is the first player who manages to possess ten cards in their hand of the same suit (Clubs, Diamonds, Hearts, Spades) and of consecutive rank, that is determined in a cyclic manner.



![Alt text](Pictures/Rank.png?raw=true "Ranks")


The game is played with a double deck which contains two Joker cards. The deck is shuffled before the game begins.

The human player and the computer are dealt two cards each until they both have 10 cards in their hand.

The player who goes first is determined randomly, and the first card of the double deck is flipped in order to have pile with the cards that are tossed in each round from both players.

The game continues until one of the players manages to possess ten consecutive cards that adhere to the above figure and are of the same suit. Note that the Joker can be any card that is useful to the player.

Until a winner is found the players take turns. On a player’s turn, they must decide if they want the card that sits on top of the pile of the flipped cards, or if they want to try their luck and draw a new unknown card from the deck. If the chose the card from the top of the flipped pile, they now must choose which of their existing cards they are going to toss in the pile in order to always have ten cards on their possession. If they choose to draw, they have the choice of whether to keep or dismiss this new card. If they dismiss it, it is tossed on top of the pile of the flipped cards. If it is a card that they think will get them one step closer to victory they keep it but first they must once again choose one of their existing cards to be tossed on top of the pile.
When it is the human player’s turn, their ten-card hand is printed for convenience, and the computer acts as a dealer, taking you step by step until you complete the round.

When it is the computer’s turn, only its actions are printed and not its hand. For example, in one round the computer might say “I am taking the Ace of Spades, win some lose some, it’s all a game to me!... and I’m throwing the 2 of Clubs” , or something like that, but that is it. You must play smart and keep track of the computer’s choices if you want to win.

When a winner emerges, the winning hand is presented as is, then a sorted version of the winning hand, sorted by Rank as well as the value of the Joker if there was one, or two and finally the hand of the loser.

## Implementation info

The game has several different principles. We have tackled each of them with a few methods that encapsulate one or two major game rules. We will go step by step from the beginning of a game of Biriba and explain our reasoning behind each method. Each paragraph can be seen as an abstract explanation of the lines of code of the main() method which in turn uses all other supplementary methods.

The game starts with a random player playing first so we had to implement a method that chooses a player randomly and we do that with whoPlaysFirst().

After that we generate a new double deck and shuffle it with the given methods.

Next, we must deal 10 cards to each player from the deck and flip a card to be on top of the ‘pile’. To do that we first define a method that takes as input the two hands of the players and gives in an alternate fashion two cards to each player from the top of the deck until they have 10 cards each. When a take a card out of the deck we replace that card’s value in the deck with ‘empty’ to make the deck finite and not draw the same cards. We do this with the dealStartingHands() method. Then we need to flip an additional card on top of the ‘pile’ for the players to have the option of choosing either a card from on top of the pile or drawing from the deck. This is done with turnTopCard() which takes the double deck and finds the first occurrence of a non-empty card and returns it to be the next flipped card.

We now form a loop that goes on which allows the players to play round after round until one of them is declared winner with the help of isWin(),which will be explained later, and a flag is raised as to stop the loop and end the game. Depending on whose turn it is we take different routes in execution of methods.


### User’s turn
If it is the user’s turn we have a series of interaction with the System Input in order to determine if the user wants a card from the top of the pile, if they want to draw from the deck, or skip their turn entirely. If they choose to replace a card from their hand with card either from the pile or the deck we make use of the replace() method which takes as input the card the user want to replace from their hand with their new card and of course their deck. We also need to check that the card they declared to be replaced must exist in their hand in the first place and we also do this check. According to the user’s actions the card on top of the pile can change in a number of ways, that is also true for the computer, If the user for example takes the card from on top of the pile, the new card on top of the pile will be the one that the user chooses to toss from their hand. If the user skips the card on top of the pile and tries to draw, there are two options: Either they like the card that they drew and keep it so they toss once again a card from their hand, or they don’t like this card and choose to toss it on top of the pile. We can see that for any of the given scenarios, the card on top of the pile always changes, so that is why we can depict it as a single String variable since it changes every round and we just give it the correct value.

After the player completes their turn, we flip the Boolean flag that remembers whose turn it is so the computer can play on the next round and vice versa.

### Computer’s Turn
For the computer’s round, the path of choices is the same. The computer looks at the card on top of the pile and must evaluate it. If it is a ‘good’ choice it switches with one of its worst cards and ends it round, else it will draw from the deck, follow the same procedure and end its round.

#### Computer Card Evaluation
We explained in a higher level what the computer’s course of action is, which is the same as the user’s. But the tricky part is how to make the computer evaluate a card in a smart way. switchCard() takes as input the computer’s deck and the candidate card. Next, This method is responsible for attempting to switch the card on the pile or on top of the deck by re-evaluating the 10-card group counter by trial and error, trying to replace each of the existing cards on the computer’s hand with the newCard until a better card group is found. When a better group is found, the newCard replaces the card that was in the current iteration and got switched. The card that is tossed is returned in order to put it on top of the pile. The concept of the best 10-card group is implemented by the findBest10() method. What we do in this method is first encode the stats of the current deck of the computer in a useful way. We do that by encoding all the cards of the computer in a 2-d array that symbolizes all possible Ranks and Suits. The columns are the four Suits and the rows are the Ranks. Then we find the best Suit, which is the one that has the most cards in and attempt to find the best 10-card group in that best Suit. isWin() uses this method as well in order to determine if a player won. It simply checks if the result of findBest10() is equal to the length of the deck of the player. There are thirteen possible 10-card groups with the cyclic nature of the Ranks.

#### 13 Different 10-card groups


- 2 to Jack
- 3 to Queen
- 4 to King
- 5 to Ace
- 6 to 2
- 7 to 3
- 8 to 4
- 9 to 5
- 10 to 6
- Jack to 7
- Queen to 8
- King to 9
- Ace to 10
 

We find how many cards exist in each of these thirteen different groups and return the biggest one. That way, when we switch cards, we will only switch when we find a newCard AND an existing card, that when switched, they achieve a bigger count than that of the last best 10-card group.
A keynote is that the Joker is always counted towards the count of 10-card group. So, we start counting not from zero, but from the number of joker’s that the computer possesses. That way we ensure that the Joker is always going to be a valuable card since it always adds one towards our counting.

We have successfully implemented a rigorous procedure that makes the computer a very tough opponent.

All that remains is the implementation of presenting nicely the card in a sorted manner as well as identifying which was the card the Joker was ultimately translated to in order to win, when a joker, or two, exists in the winning hand. For this we implemented the sort() method which needed very good understanding of some concrete properties of the Ranking system in the game. A very important note is this method is called only when we achieve a score of 10 from the findBest10() method, which means the player in question won, so we just need to find a way to display the cards correctly. 

We can identify them with the help of the figure below:

![Alt text](Pictures/examples.png?raw=true "Examples")

We have laid four possible scenarios:

- The computer possesses 10 consecutive cards from 8 to 4.
- The computer possesses 9 cards from the range of Q to 8 and it misses 3 to complete 10 cards. (We have one Joker)
- The computer possesses 8 cards either from the range of J to 7 or from the range of Q to 8. (We have two Jokers)
- The computer possesses 10 consecutive cards from 2 to J.

On all four scenarios, notice that there is always a gap of three consecutive empty spaces that can never be filled. Even in the third scenario, whatever our choice is for the value of the second Joker, the gap remains. After this finding, our logic is simple.

Iterate through the corresponding Stats column until you find that gap. When you find it, declare the first position to be the position immediately after the end of the three-square gap as the starting index in which we will start to print the Cards. This means that for our third example, our method would choose the Joker to take the value of 8. Of course, this does not interfere with the rules of the game since the Joker can be any card and we just choose a value that does not in any way obstruct certainty of victory. We construct the cards by taking the value of the index that corresponds from the Rank table, and the Suit of the best Suit for that deck. That way we correctly construct all cards. When we go to an index that the card does not exist, but that index is included in our 10-card best group, that means that we must use a Joker to depict its value.
The modulo operation plays a very important role in this method and we can appreciate it with the fourth scenario which displays modulo’s power very elegantly. We find the three-square gap, which is on indices 10 through 12, Queen through Ace. The next index is going to be 13 which is out of range but using modulo Rank.length, which is 13,  13 modulo 13 is equal to 0, which is going to give us a starting index of 0, hence we will start correctly printing the cards starting from 2.

This concludes the logic behind the implementation.
