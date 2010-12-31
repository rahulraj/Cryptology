/*
 * Class that holds two monoalphabets and can analyze them for digraphs
 */

import java.util.ArrayList;

public class DigraphAnalyzer 
{
	private MonoalphabetAnalyzer first; // first letters of digraphs
	private MonoalphabetAnalyzer second; // second letters of digraphs, sucessive to first
	
	private LetterPair[][] theDigraphs; // stores all the digraphs
	// sorted arrays for faster searching of most commonly accessed digraphs
	private LetterPair[] mostFrequent;
	private LetterPair[] theRepeats;
	
	public DigraphAnalyzer(MonoalphabetAnalyzer fir, MonoalphabetAnalyzer sec, boolean lAndF)
	{
		// lAndF is true whenever first is the last monoalphabet and second is the first monoalphabet
		// in this case, the indexes of characters in second must be shifted to access the proper digraphs
		
		// each MonoalphabetAnalyzer should have already solved the most common letters (E, T, O, A)
		first = fir;
		second = sec;
		theDigraphs = new LetterPair[LetterCounter.NUM_LETTERS][LetterCounter.NUM_LETTERS];
		
		// fill theDigraphs
		for (int row = 0; row < theDigraphs.length; row++)
			for (int col = 0; col < theDigraphs[row].length; col++)
			{
				theDigraphs[row][col] = new LetterPair(row, col);
			}
		
		String firstMono = first.getCipher();
		String secondMono = second.getCipher();
		
		if (lAndF)
		{
			// account for the shift
			StringBuilder s = new StringBuilder("");
			s.append(secondMono.substring(1, secondMono.length()));
			s.append(" "); // make up for disparity in length
			secondMono = s.toString();
		}
		
		for (int index = 0; index < secondMono.length(); index++)
		{
			// there may be whitespace at the ends of the alphabets (with the purpose of making
			// them all the same length). This is handled here
			if (firstMono.charAt(index) >= 65 && firstMono.charAt(index) <= 90 
				&& secondMono.charAt(index) >= 65 && secondMono.charAt(index) <= 90)
			{
				//char first = firstMono.charAt(index); // debug purposes
				//char second = secondMono.charAt(index);
				theDigraphs[firstMono.charAt(index) - 65]
					       [secondMono.charAt(index) - 65].incrementFreqency(); 
			} 
			// if out of bounds then nothing needs to be done
		}
		
		mostFrequent = new LetterPair[LetterCounter.PAIRS_TO_OUT];
		for (int count = 0; count < mostFrequent.length; count++)
		{
			mostFrequent[count] = getHighestUnlistedPair();
		}
		
		theRepeats = new LetterPair[theDigraphs.length];
		for (int loc = 0; loc < theDigraphs.length; loc++)
		{
			theRepeats[loc] = theDigraphs[loc][loc];
		}
		sort(theRepeats);
	}

	private LetterPair getHighestUnlistedPair()
	{
		// search for the highest
		int highFrequency = -1;
		LetterPair highPair = null;
		for (int row = 0; row < theDigraphs.length; row++)
			for (int col = 0; col < theDigraphs[0].length; col++)
			{
				LetterPair pair = theDigraphs[row][col];
				if (!pair.isListed() && pair.getFrequency() > highFrequency)
				{
					highPair = pair;
					highFrequency = pair.getFrequency();
				}
				
			}
		highPair.setListed(true);
		return highPair;
	}

	private LetterPair[] getDigraphsWith(char letter, boolean isFirst)
	{
		// returns a sorted array with all the LetterPairs with letter in them
		// if isFirst = true, returns digraphs starting with that letter
		// otherwise, returns digraphs ending with that letter
		if (letter < 65 || letter > 90)
		{
			return null;
		}
		
		LetterPair[] output = new LetterPair[theDigraphs.length];
		if (isFirst)
		{
			for (int col = 0; col < theDigraphs[0].length; col++)
			{
				output[col] = theDigraphs[letter - 65][col];
			}
		}
		else
		{
			for (int row = 0; row < theDigraphs.length; row++)
			{
				output[row] = theDigraphs[row][letter - 65];
			}
		}
		sort(output); // sorted in rising order
		return output;
	}
	
	
	private void sort(Comparable[] objects)
	{
		// insertion sort algorithm, sorts in rising order
		for (int index = 1; index < objects.length; index++)
		{
			Comparable key = objects[index];
			int position = index;
			
			// shift larger values to the right
			while (position > 0 && objects[position-1].compareTo(key) > 0)
			{
				objects[position] = objects[position-1];
	            position--;
			}
			
			objects[position] = key;
		}
	}
	
	public void solveH()
	{
		// most common letter to follow T, so it can be solved in second
		char tCip = first.getCipherFor('T');
		if (tCip == '?') // we do not have enough information to solve H
		{
			return; // breaks out of the method
		}
		
		// find the digraph with highest frequency that starts with T
		int highLoc = 0;
		int highFreq = 0;
		for (int col = 0; col < theDigraphs[0].length; col++)
		{
			if (theDigraphs[(int)(tCip - 65)][col].getFrequency() > highFreq)
			{
				highLoc = col;
				highFreq = theDigraphs[tCip - 65][col].getFrequency();
			}
		}
		char hCip = (char)(highLoc + 65);
		if (!second.cipherIsSolved(hCip))
		{
			second.setPlainLetterAs('H', hCip);
		}
	}
	
	public void solveN()
	{
		// most common letter to follow A, can be solved in second
		char aCip = first.getCipherFor('A');
		if (aCip == '?') // we do not have enough information to solve H
		{
			return; // breaks out of the method
		}
		
		// find the digraph with highest frequency that starts with A
		int highLoc = 0;
		int highFreq = 0;
		for (int col = 0; col < theDigraphs[0].length; col++)
		{
			if (theDigraphs[aCip - 65][col].getFrequency() > highFreq)
			{
				highLoc = col;
				highFreq = theDigraphs[aCip - 65][col].getFrequency();
			}
		}
		char nCip = (char)(highLoc + 65);
		if (!second.cipherIsSolved(nCip))
		{
			second.setPlainLetterAs('N', nCip);
		}
	}
	
	public void solveR()
	{
		// most common letter to follow E, can be solved in second
		char eCip = first.getCipherFor('E');
		if (eCip == '?') // we do not have enough information to solve R
		{
			return; // breaks out of the method
		}
		
		// find the digraph with highest frequency that starts with E
		int highLoc = 0;
		int highFreq = 0;
		for (int col = 0; col < theDigraphs[0].length; col++)
		{
			if (theDigraphs[eCip - 65][col].getFrequency() > highFreq)
			{
				highLoc = col;
				highFreq = theDigraphs[eCip - 65][col].getFrequency();
			}
		}
		char rCip = (char)(highLoc + 65);
		if (!second.cipherIsSolved(rCip))
		{
			second.setPlainLetterAs('R', rCip);
		}
	}
	
	public void solveU()
	{
		// U is the most common letter following O in the Shakespeare plays
		// and probably in literature in general
		// Note: Sinkov's analysis disagrees; however, he analyzes text from newspaper
		// articles which would rarely use the word YOU as it is bad form for journalists to address readers
		
		char oCip = first.getCipherFor('O');
		if (oCip == '?') // we do not have enough information to solve R
		{
			return; // breaks out of the method
		}
		
		// find the digraph with highest frequency that starts with E
		int highLoc = 0;
		int highFreq = 0;
		for (int col = 0; col < theDigraphs[0].length; col++)
		{
			if (theDigraphs[oCip - 65][col].getFrequency() > highFreq)
			{
				highLoc = col;
				highFreq = theDigraphs[oCip - 65][col].getFrequency();
			}
		}
		char uCip = (char)(highLoc + 65);
		if (!second.cipherIsSolved(uCip))
		{
			second.setPlainLetterAs('U', uCip);
		}
	}
	
	public void solveS()
	{
		// S is the second most common letter to follow I
		// IN is more common than IS, but N is known
		// IS is significantly more common than any other digraph starting with I
		char iCip = first.getCipherFor('I');
		if (iCip == '?')
		{
			return;
		}
		
		char nCip = second.getCipherFor('N');
		int highLoc = 0;
		int highFreq = 0;
		for (int col = 0; col < theDigraphs[0].length; col++)
		{
			if (theDigraphs[iCip - 65][col].getFrequency() > highFreq &&
				(char)(col + 65) != nCip)
			{
				highLoc = col;
				highFreq = theDigraphs[iCip - 65][col].getFrequency();
			}
		}
		char sCip = (char)(highLoc + 65);
		if (!second.cipherIsSolved(sCip))
		{
			second.setPlainLetterAs('S', sCip);
		}
	}
	
	public void solveQ()
	{
		// test all letters until we find Q's unusual digraph distribution
		// the only letter that follows Q is U
		// go in sequence for the unknown letters in the first monoalphabet
		char uCip = second.getCipherFor('U');
		if (uCip == '?')
		{
			return;
		}
		
		ArrayList<Character> uns = first.getUnsolvedLetters();
		for (Character ch : uns)
		{
			// test for this distribution
			LetterPair[] digraphs = getDigraphsWith(ch, true); // digraphs starting with ch
			if (digraphs[digraphs.length - 1].getFrequency() > 0 &&
				digraphs[digraphs.length - 2].getFrequency() == 0)
			{
				// this is Q's frequency distribution
				first.setPlainLetterAs('Q', ch);
				return; // method is done
			}
		}
	}
	
	public void solveM()
	{
		/*
		 * Find M
		 * It is the first letter in "MY" one of the most common
		 * digraphs ending in Y.
		 * Other common digraphs ending in Y have first letters identified earlier
		 */
		char yCip = second.getCipherFor('Y');
		if (yCip == '?')
		{
			return;
		}
		LetterPair[] digraphs = getDigraphsWith(yCip, false);
		for (int loc = digraphs.length - 1; loc >= 0; loc--)
		{
			char current = digraphs[loc].getFirst();
			if (!first.cipherIsSolved(current))
			{
				first.setPlainLetterAs('M', current);
				return;
			}
		}
	}
	
	public void solveFAndW()
	{
		// THIS DOESN'T WORK WITH POLYALPHABETIC CIPHERS
		// It checks for the same letter as the first and second letter of a digraph - this can't be done!
		// THIS METHOD IS NOT BEING CALLED
		if (true) // necessary so code will compile, 
				  // otherwise statements after this one will be considered unreachable
		{
			throw new IllegalStateException("solveFAndW() does not work and should not be called!");
		}
		
		/*
		 * Find both F and W
		 * Check the digraphs starting with O
		 * "OF" and "OW" will be the two highest digraphs with unidentified letters
		 * To tell them apart, check the digraphs ending with O
		 * "FO" is significantly higher than "WO" 
		 */
		
		char oCip1 = first.getCipherFor('O');
		char oCip2 = second.getCipherFor('O');
		if (oCip1 == '?' || oCip2 == '?')
		{
			return;
		}
		LetterPair[] digraphs = getDigraphsWith(oCip1, true);
		char cip1 = '?';
		char cip2 = '?';
		int loc = digraphs.length - 1;
		while (cip1 == '?' && cip2 == '?' && loc >= 0)
		{
			char current = digraphs[loc].getSecond();
			if (!second.cipherIsSolved(current))
			{
				if (cip1 == '?')
				{
					cip1 = current;
				}
				else
				{
					cip2 = current;
				}
			}
			loc--;
		}
		if (cip1 == '?' || cip2 == '?')
		{
			return; // did not find 2 letters, can't continue
		}
		// cip1 and cip2 are F and W, we now need to find out which is which
		LetterPair pair1 = null;
		LetterPair pair2 = null;
		
		LetterPair[] cip1Start = getDigraphsWith(cip1, true);
		// find the digraph that ends with O (in second)
		for (int pos = cip1Start.length - 1; pos >= 0; pos--)
		{
			if (cip1Start[pos].getSecond() == oCip2)
			{
				pair1 = cip1Start[pos];
				pos = -1; // break out
			}
		}
		
		LetterPair[] cip2Start = getDigraphsWith(cip2, true);
		// find the digraph that ends with O (in second)
		for (int aPos = cip2Start.length - 1; aPos >= 0; aPos--)
		{
			if (cip2Start[aPos].getSecond() == oCip2)
			{
				pair2 = cip2Start[aPos];
				aPos = -1; // break out
			}
		}
		
		// FO is higher than WO, so one pair will be higher than the other
		if (pair1.getFrequency() > pair2.getFrequency())
		{
			char fCip = pair1.getFirst();
			char wCip = pair2.getFirst();
			
		}
		
	}
	
	public String toString() // debug purposes
	{
		return first.toString() + " and " + second.toString();
	}
}