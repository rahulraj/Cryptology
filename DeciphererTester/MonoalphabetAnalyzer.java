import java.util.LinkedList;
import java.util.ArrayList;

public class MonoalphabetAnalyzer 
{
	private String cipher;
	private char[][] keys;
	private LetterCounter counter;
	private String plain;
	private int myNumber; // for debug purposes
	
	public MonoalphabetAnalyzer(String cip)
	{
		cipher = cip.toUpperCase();
		
		keys = new char[2][26];
		// 2 rows, 26 columns
		// upper row is the cipher letters
		// lower is their plain equivalents
		
		for (int c = 0; c < keys[0].length; c++)
		{
			keys[0][c] = (char)(c + 65);
			keys[1][c] = '?';
		}
		
		counter = new LetterCounter(cipher);
		counter.getFrequencies(); // stores frequency data in counter
		plain = "";
		myNumber = 0;
		//solveFreqs(); // fills keys, initializes plain in the process
	}
	
	public ArrayList<Character> getUnsolvedLetters()
	{
		// returns an ArrayList of all the CIPHERTEXT letters in this monoalphabet that have not been solved
		ArrayList<Character> unsolved = new ArrayList<Character>();
		for (int loc = 0; loc < keys[0].length; loc++)
		{
			if (keys[1][loc] == '?')
			{
				// unsolved letter
				unsolved.add(new Character(keys[0][loc]));
			}
		}
		return unsolved;
	}
	
	public void setNumber(int num)
	{
		myNumber = num;
	}
	
	public char getPlainLetterAt(int index)
	{
		return plain.charAt(index);
	}
	
	public char getCipherLetterAt(int index)
	{
		return cipher.charAt(index);
	}
	
	public int getLength()
	{
		return cipher.length();
	}
	
	public String getCipher()
	{
		return cipher;
	}
	
	public void solveAsCaesar()
	{
		// find the letter identified as E
		int letterLoc = -1;
		for (int loc = 0; loc < keys[1].length; loc++)
		{
			if (keys[1][loc] == 'E')
			{
				letterLoc = loc;
				loc = keys[1].length; // break out
			}
		}
		
		if (letterLoc == -1)
		{
			// E was not found, use T instead
			for (int loc = 0; loc < keys[1].length; loc++)
			{
				if (keys[1][loc] == 'T')
				{
					letterLoc = loc;
					loc = keys[1].length; // break out
				}
			}
		}
		
		int encShift = makeMod26(keys[0][letterLoc] - keys[1][letterLoc]);
		int decShift = makeMod26(26 - encShift);
		
		StringBuilder pl = new StringBuilder("");
		for (int index = 0; index < cipher.length(); index++)
		{
			char aCipLetter = cipher.charAt(index);
			int toConvert = aCipLetter - 64; // convert to a range of 1 to 26
			toConvert += decShift;
			toConvert = makeMod26(toConvert);
			toConvert += 64;
			pl.append((char)toConvert);
		}
		plain = pl.toString();
	}
	
	private int makeMod26(int anInt)
	{
		while (anInt >= 26)
		{
			anInt -= 26;
		}
		while (anInt < 0)
		{
			anInt += 26;
		}
		return anInt;
	}
	
	public void makeFirstGuess()
	{
		// make an initial first guess as to the identitiy of a subsitution ciphertext
		// this is based purely on single letter frequencies and WILL NOT be accurate!
		// it requires refinement!
		
		char[] plainFreqRanks = {'E', 'T', 'N', 'R', 'I', 'O', 'A', 'S', 'D', 'H', 'L', 'C', 'F', 
								 'P', 'U', 'M', 'Y', 'G', 'W', 'V', 'B', 'X', 'K', 'Q', 'J', 'Z'};
		for (int freqRank = 1; freqRank <= 26; freqRank++)
		{
			char toCheck = counter.getLetterOfFrequency(freqRank);
			keys[1][toCheck - 65] = plainFreqRanks[freqRank - 1];
		}
		makePlaintext();
	}
	
	public void solveFreqs()
	{
		/**
		 *  Fills some of the basic keys, with frequency analysis, the more complex ones have to wait
		 */
		
		solveE();
		solveT();
		solveO();
		solveA();
		//solveL();
		//solveS();
		//solveH();
		//solveN();
		//solveD();
		
		/**
		 * Solve for letters based on the knowledge of other letters
		 */
		
		//solveR();
		//solveU();
		//solveI();
		//solveY();
		//solveG();
		//solveQ();
		//solveM();
		//solveFAndW();
		//solveC();
		//solveVAndB();
		
		makePlaintext();
	}
	
	public LinkedList<Integer> getOccurancesOf(char toFind)
	{
		// gets all occurances of toFind in the PLAINTEXT! 
		
		LinkedList<Integer> occuranceList = new LinkedList<Integer>();
		int startIndex = 0;
		int strIndex = plain.indexOf(toFind, startIndex);
		while (strIndex != -1)
		{
			//System.out.print("String found at index " + (strIndex + 1));
			occuranceList.add(strIndex);
			startIndex = strIndex;
			strIndex = plain.indexOf(toFind, startIndex + 1);
		}
		return occuranceList;
	}
	
	public boolean letterIsSolved(char letter)
	{
		// searches for the plaintext letter (letter being the input)
		// if it is tied to a ciphertext letter already, then return true
		for (int loc = 0; loc < keys[1].length; loc++)
		{
			if (keys[1][loc] == letter)
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean cipherIsSolved(char cipLetter)
	{
		// checks to see if the cipLetter ciphertext letter has a plaintext letter with it
		// returns true if so
		return (keys[1][cipLetter - 65] != '?');
	}
	
	public void setPlainLetterAs(char plain, char cip)
	{
		keys[1][cip - 65] = plain; 
	}
	
	public char getCipherFor(char plain)
	{
		for (int loc = 0; loc < keys[0].length; loc++)
		{
			if (keys[1][loc] == plain)
			{
				return keys[0][loc];
			}
		}
		return '?';
	}
	
	private void solveE()
	{
		/*
		 * Attempt to identify E's cipher letter
		 * It is the most common letter
		 * The 2nd most common repeat
		 * The 2nd letter in "HE" (the 2nd most common digraph)
		 * The 1st letter in "ER" (another common digraph) this has been the 3rd, 4th, and 5th most in the samples
		 * (ER clue has not yet been implemented)
		 * And the 3rd letter in "THE" (the most common trigraph)
		 */
		char[] poss = new char[1]; //possibilities for the cipher letter
		poss[0] = counter.getLetterOfFrequency(1);
		//poss[1] = counter.getRepeatOfFrequency(2).getLetters().charAt(0);
		//poss[2] = counter.getPairOfFrequency(2).getLetters().charAt(1);
		//poss[3] = counter.getTrigraphOfFrequency(1).getThird();
		char cip = getMostCommon(poss);
		if (cip != '?')
		{
			keys[1][cip - 65] = 'E';
		}
	}
	
	private void solveT()
	{
		/*
		 * Attempt to identify T's cipher
		 * 2nd most common letter
		 * 1st letter in "TH" (the most common digraph)
		 * 1st letter in "THE" (the most common trigraph)
		 */
		char[] poss = new char[1];
		poss[0] = counter.getLetterOfFrequency(2);
		//poss[1] = counter.getPairOfFrequency(1).getLetters().charAt(0);
		//poss[2] = counter.getTrigraphOfFrequency(1).getFirst();
		char cip = getMostCommon(poss);
		if (cip != '?' && keys[1][cip - 65] == '?') 
		{
			// if conditions are
			// necessary to ensure that cip is definite and avoid "collisions" with other determined keys
			keys[1][cip - 65] = 'T';
		}
	}
	
	private void solveO()
	{
		/*
		 * Attempt to ID O's cipher
		 * Implemented:
		 * 3rd most common letter
		 * 3rd most common repeat
		 * 1st letter of "OU" which could be the 4th - 6th most common digraph
		 * 
		 * Not implemented yet:
		 * 2nd letter in "YOU" (3rd-5th most common trigraph)
		 */
		char [] poss = new char[1];
		poss[0] = counter.getLetterOfFrequency(3);
		//poss[1] = counter.getRepeatOfFrequency(3).getLetters().charAt(0);
		//poss[2] = counter.getPairOfFrequency(4).getLetters().charAt(0);
		//poss[3] = counter.getPairOfFrequency(5).getLetters().charAt(0);
		//poss[4] = counter.getPairOfFrequency(6).getLetters().charAt(0);
		
		char cip = getMostCommon(poss);
		if (cip != '?' && keys[1][cip - 65] == '?') 
		{
			keys[1][cip - 65] = 'O';
		}
	}
	
	private void solveA()
	{
		/*
		 * Attempt to ID A's cipher
		 * Implemented:
		 * 4th most common letter
		 * 1st letter in "AND" (2nd most common trigraph)
		 * 1st letter in "AN" (3rd or 4th most common digraph)
		 */
		char[] poss = new char[1];
		poss[0] = counter.getLetterOfFrequency(4);
		//poss[1] = counter.getTrigraphOfFrequency(2).getFirst();
		//poss[2] = counter.getPairOfFrequency(3).getLetters().charAt(0);
		//poss[3] = counter.getPairOfFrequency(4).getLetters().charAt(0);
		
		char cip = getMostCommon(poss);
		if (cip != '?' && keys[1][cip - 65] == '?') 
		{
			keys[1][cip - 65] = 'A';
		}
	}
	
	public void solveI()
	{
		// most common not already solved when calling this methods
		char iCip = counter.getHighestFreqUnknownLetter(this);
		keys[1][iCip - 65] = 'I';
		/*for (int freqRank = 1; freqRank <= 26; freqRank--)
		{
			char letterOfFreq = counter.getLetterOfFrequency(freqRank);
			if ((letterOfFreq - 65) == -33)
			{
				System.out.println("Breakpoint here.");
			}
			if (keys[1][letterOfFreq - 65] == '?')
			{
				keys[1][letterOfFreq - 65] = 'I';
				return; // terminates method
			}
		}*/
	}
	
	private char getMostCommon(char[] chars)
	{
		// get most common char in the array
		
		int[] charFreqs = new int[chars.length]; // parallel array to chars
		
		// populate charFreqs
		for (int loc = 0; loc < charFreqs.length; loc++)
		{
			char current = chars[loc];
			for (char ch : chars)
			{
				if (current == ch)
				{
					charFreqs[loc]++;
				}
			}
		}
		
		// find most common letter
		int highFreq = 0;
		int highLoc = 0;
		for (int loc = 0; loc < charFreqs.length; loc++)
		{
			if (charFreqs[loc] > highFreq)
			{
				highFreq = charFreqs[loc];
				highLoc = loc;
			}
		}
		
		// make sure there are no ties
		for (int loc = 0; loc < charFreqs.length; loc++)
		{
			if (charFreqs[loc] == highFreq && chars[loc] != chars[highLoc])
			{
				// there is a tie with two or more characters in the array!
				// so multiple characters fulfilled the same number of conditions 
				// and there is not plurality
				return '?'; // still indefinite
			}
		}
		
		return chars[highLoc];
	}
	
	public void makePlaintext()
	{
		StringBuilder pl = new StringBuilder("");
		for (int index = 0; index < cipher.length(); index++)
		{
			if (cipher.charAt(index) >= 65 && cipher.charAt(index) <= 90)
			{
				pl.append(keys[1][cipher.charAt(index) - 65]);
			}
			else
			{
				pl.append(cipher.charAt(index));
			}
		}
		plain = pl.toString();
	}
	
	public String getKeys()
	{
		String output = "<Plaintext> : <Ciphertext>\n";
		for (int col = 0; col < keys[0].length; col++)
		{
			output += keys[1][col] + " : " + keys[0][col] + "\n";
		}
		return output;
	}
	
	public String toString() // debug purposes
	{
		return "Monoalphabet number " + myNumber;
	}
}
