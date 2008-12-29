package gjset.engine;

/* 
 *  LEGAL STUFF
 * 
 *  This file is part of gjSet.
 *  
 *  gjSet is Copyright 2008, 2009 Joyce Murton
 *  
 *  The Set Game, card design, and basic game mechanics of the Set Game are
 *  registered trademarks of Set Enterprises. 
 *  
 *  This project is in no way affiliated with Set Enterprises, 
 *  but the authors of gjSet are very grateful for
 *  them creating such an excellent card game.
 *  
 *  gjSet is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *   
 *  gjSet is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details
 *   
 *  You should have received a copy of the GNU General Public License
 *  along with gjSet.  If not, see <http://www.gnu.org/licenses/>.
 */

import gjset.engine.Card;

import java.util.Vector;

public class Deck
{
	private Vector<Card>	cards;
	private Vector<Card>	shuffledDeck;

	public Deck()
	{
		cards = new Vector<Card>(81);
		shuffledDeck = new Vector<Card>(81);

		//Create all of the various cards.
		for (int n = 1; n <= 3; n++)
			for (int c = 1; c <= 3; c++)
				for (int s = 1; s <= 3; s++)
					for (int s2 = 1; s2 <= 3; s2++)
						cards.add(new Card(n, c, s, s2));

		shuffle();
	}

	public void shuffle()
	{
		//Create our companion array that we will shuffle.
		double arr[] = new double[81];
		for (int i = 0; i < 81; i++)
		{
			arr[i] = Math.random();
		}

		//Get a copy of the other deck
		shuffledDeck.removeAllElements();
		for (int i = 0; i < 81; i++)
		{
			shuffledDeck.add(cards.get(i));
		}

		//Now sort both arrays together.		
		for (int i = 0; i < 81; i++)
		{
			for (int j = 80; j > 0; j--)
			{
				if (arr[j - 1] > arr[j])
				{
					double temp = arr[j - 1];
					arr[j - 1] = arr[j];
					arr[j] = temp;

					Card tempCard = shuffledDeck.get(j - 1);
					shuffledDeck.set(j - 1, shuffledDeck.get(j));
					shuffledDeck.set(j, tempCard);
				}
			}
		}
	}

	public Card drawCard()
	{
		Card card = shuffledDeck.firstElement();
		shuffledDeck.remove(0);
		//System.out.println(remainingCards() + " cards remaining");
		return card;
	}

	public Vector<Card> drawCards( int number )
	{
		Vector<Card> vector = new Vector<Card>(number);
		for (int i = 0; i < number; i++)
		{
			vector.add(drawCard());
		}
		return vector;
	}

	public int remainingCards()
	{
		return shuffledDeck.size();
	}
}
