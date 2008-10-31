package gjset.engine;

import gjset.gui.Card;

import java.util.Vector;


public class Deck
{
	private Vector<Card> cards;
	private Vector<Card> shuffledDeck;
	
	public Deck()
	{
		cards = new Vector<Card>(81);
		shuffledDeck = new Vector<Card>(81);
		
		//Create all of the various cards.
		for(int n = 1; n <= 3; n++)
			for(int c = 1; c <= 3; c++)
				for(int s = 1; s <= 3; s++)
					for(int s2 = 1; s2 <= 3; s2++)
						cards.add(new Card(n, c, s, s2));
		
		shuffle();
	}
	
	public void shuffle()
	{
		//Reset the characteristics of all the cards.
		for(int i = 0; i < 18; i++)
		{
			cards.get(i).reset();
		}
		
		//Create our companion array that we will shuffle.
		double arr[] = new double[81];
		for(int i = 0; i < 81; i++)
		{
			arr[i] = Math.random();
		}
		
		//Get a copy of the other deck
		shuffledDeck.removeAllElements();
		for(int i = 0; i < 81; i++)
		{
			shuffledDeck.add(cards.get(i));
		}
		
		//Now sort both arrays together.		
		for(int i = 0; i < 81; i++)
		{
			for(int j = 80; j > 0; j--)
			{
				if(arr[j - 1 ] > arr[j])
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
	
	public Vector<Card> drawCards(int number)
	{
		Vector<Card> vector = new Vector<Card>(number);
		for(int i = 0; i < number; i++)
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
