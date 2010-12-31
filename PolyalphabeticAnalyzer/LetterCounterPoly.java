
public class LetterCounterPoly 
{
	public static final int NUM_LETTERS = 26;
	public static final int PAIRS_TO_OUT = 30;
	public static final int TRIS_TO_OUT = 10;
	
	private LetterPoly[] singleFrequencies;
	private LetterPairPoly[][] pairFrequencies;
	private TrigraphPoly[][][] trigraphFrequencies;
	private String codedPhrase;
	
	private LetterPoly[] sortSingles;
	private LetterPairPoly[] sortDis;
	private LetterPairPoly[] sortRepeats;
	private TrigraphPoly[] sortTris;

	private int inputLength;
	
	public LetterCounterPoly(String input)
	{
		inputLength = input.length();
		singleFrequencies = new LetterPoly[NUM_LETTERS]; 
		/* 
		 * Each index in singleFrequencies represents a letter
		 * 0 is a, 1 is b ... 25 is z
		 */
		// populate singleFequencies
		for (int loc = 0; loc < singleFrequencies.length; loc++)
		{
			singleFrequencies[loc] = new LetterPoly((char)(loc + 65));
		}
		
		pairFrequencies = new LetterPairPoly[NUM_LETTERS][NUM_LETTERS];
		/*
		 * Using a numerical system similar to singleFreqencies
		 * The row is the first letter, the column is the second
		 */
		// Populate pairFreqencies
		for (int row = 0; row < pairFrequencies.length; row++)
			for (int col = 0; col < pairFrequencies[0].length; col++)
			{
				pairFrequencies[row][col] = new LetterPairPoly(row, col);
			}
		
		trigraphFrequencies = new TrigraphPoly[NUM_LETTERS][NUM_LETTERS][NUM_LETTERS];
		/*
		 * Again, a similar system is used
		 */
		// Populate the array
		for (int row = 0; row < trigraphFrequencies.length; row++)
			for (int col = 0; col < trigraphFrequencies[0].length; col++)
				for (int third = 0; third < trigraphFrequencies[0][0].length; third++)
				{
					trigraphFrequencies[row][col][third] = new TrigraphPoly(row, col, third);
				}
		
		codedPhrase = input.toUpperCase(); // not case sensitive
		
		sortSingles = new LetterPoly[NUM_LETTERS];
		sortDis = new LetterPairPoly[PAIRS_TO_OUT];
		sortRepeats = new LetterPairPoly[NUM_LETTERS];
		sortTris = new TrigraphPoly[TRIS_TO_OUT];
	}
	
	public String getFrequencies()
	{
		StringBuilder output = new StringBuilder("Single frequencies by letter:\n");
		countSingleLetters();
		for (LetterPoly let : singleFrequencies)
		{
			output.append(let + "\n");
		}
		
		output.append("\n--------------------\n\n");
		output.append("Single frequencies by frequency:\n");

		output.append(getSortedLetters());
		
		countLetterPairs();
		
		output.append("\n--------------------\n\n");
		output.append("Most common repeated letters:\n");
		
		output.append(getRepeatedLetters());
		
		output.append("\n--------------------\n\n");
		
		// output the PAIRS_TO_OUT highest pairs
		// in the process, populate sortDis
		for (int count = 0; count < PAIRS_TO_OUT; count++)
		{
			LetterPairPoly pair = getHighestUnlistedPair();
			output.append(pair.toString());
			int pairsFreq = pair.getFrequency();
			double percent = (double)pairsFreq / (double)inputLength;
			percent *= 100;
			output.append(" which is " + percent + " percent.\n");
			sortDis[count] = pair;
		}
		
		sort(sortDis);
		
		output.append("\n--------------------\n\n");
		
		// output TRIS_TO_OUT highest trigraphs and populate sortTris
		countTrigraphs();
		for (int count = 0; count < TRIS_TO_OUT; count++)
		{
			TrigraphPoly tri = getHighestUnlistedTrigraph();
			output.append(tri.toString());
			sortTris[count] = tri;
		}
		
		sort(sortTris);
		
		return output.toString();
	}
	
	private void countSingleLetters()
	{
		for (int index = 0; index < codedPhrase.length(); index++)
		{
			char aLetter = codedPhrase.charAt(index);
			if ((aLetter >= 65) && (aLetter <= 90)) // the range of values for lowercase characters
			{
				singleFrequencies[aLetter - 65].incrementFreq(); // increment the corresponding value in the array
			}
		}
	}
	
	private String getSortedLetters()
	{
		// populate sortSingles in the process
		for (int loc = 0; loc < sortSingles.length; loc++)
		{
			LetterPoly toCopy = singleFrequencies[loc];
			sortSingles[loc] = new LetterPoly(toCopy.getLetter(), toCopy.getFrequency());
		}
		
		sort(sortSingles);
		
		int totalFreqs = 0;
		for (LetterPoly let : sortSingles)
		{
			totalFreqs += let.getFrequency();
		}
		
		StringBuilder out = new StringBuilder("");
		
		for (int pos = sortSingles.length - 1; pos >= 0; pos--)
		{
			int difference = 0;
			double percent = (double)sortSingles[pos].getFrequency() / totalFreqs;
			percent *= 100;
			
			out.append(sortSingles[pos] + " which is %" + percent + "       ");
			if (pos >= 1)
			{
				difference = sortSingles[pos].getFrequency() - sortSingles[pos - 1].getFrequency();
			}
			out.append(difference + " greater than:\n");
		}
		
		return out.toString();
	}
	
	private void sort(Comparable[] objects)
	{
		// insertion sort algorithm
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
	
	private String getRepeatedLetters()
	{
		// this method returns the most commmon pairs of repeated letters
		// in English plaintext, these pairs would be ss, ee, tt, ff, ll, mm, and oo
		for (int loc = 0; loc < sortRepeats.length; loc++)
		{
			sortRepeats[loc] = pairFrequencies[loc][loc];
		}
		
		sort(sortRepeats);
		
		StringBuilder output = new StringBuilder("");
		
		// get the top ten
		for (int pos = sortRepeats.length - 1; pos >= sortRepeats.length - 10; pos--)
		{
			output.append(sortRepeats[pos]);
			//System.out.println("Getting the repeated letters.");
		}
		
		return output.toString();
	}

	private void countLetterPairs()
	{	
		for (int index = 0; index < (codedPhrase.length() - 1); index++)
		{
			char first = codedPhrase.charAt(index);
			char second = codedPhrase.charAt(index + 1);
			if ((first >= 65) && (first <= 90) && (second >= 65) && (second <= 90))
			{
				// both first and seconds are letters and should be tallied as a pair
				int row = first - 65;
				int col = second - 65;
				pairFrequencies[row][col].incrementFreqency();
			}
		}
	}

	private LetterPairPoly getHighestUnlistedPair()
	{
		// search for the highest
		int highFrequency = -1;
		LetterPairPoly highPair = null;
		for (int row = 0; row < pairFrequencies.length; row++)
			for (int col = 0; col < pairFrequencies[0].length; col++)
			{
				LetterPairPoly pair = pairFrequencies[row][col];
				if (!pair.isListed() && pair.getFrequency() > highFrequency)
				{
					highPair = pair;
					highFrequency = pair.getFrequency();
				}
				
			}
		highPair.setListed(true);
		return highPair;
	}
	
	private void countTrigraphs()
	{
		for (int index = 0; index < (codedPhrase.length() - 2); index++)
		{
			char first = codedPhrase.charAt(index);
			char second = codedPhrase.charAt(index + 1);
			char third = codedPhrase.charAt(index + 2);
			if ((first >= 65) && (first <= 90) && (second >= 65) && (second <= 90)
				&& (third >= 65) && (third <= 90))
			{
				// both first and seconds are letters and should be tallied as a pair
				int row = first - 65;
				int col = second - 65;
				int theThird = third - 65;
				trigraphFrequencies[row][col][theThird].incrementFreqency();
			}
		}
	}
	
	private TrigraphPoly getHighestUnlistedTrigraph()
	{
		// search for the highest
		int highFrequency = -1;
		TrigraphPoly highTri = null;
		for (int row = 0; row < trigraphFrequencies.length; row++)
			for (int col = 0; col < trigraphFrequencies[0].length; col++)
				for (int third = 0; third < trigraphFrequencies[0][0].length; third++)
				{
					TrigraphPoly tri = trigraphFrequencies[row][col][third];
					if (!tri.isListed() && tri.getFrequency() > highFrequency)
					{
						highTri = tri;
						highFrequency = tri.getFrequency();
					}
				
				}
		highTri.setListed(true);
		return highTri;
	}
	
	public char getHighestFreqUnknownLetter(MonoalphabetAnalyzer analyzer)
	{
		for (int freqRank = (sortSingles.length - 1); freqRank >= 0; freqRank--)
		{
			if (!analyzer.letterIsSolved(sortSingles[freqRank].getLetter()))
			{
				return sortSingles[freqRank].getLetter();
			}
		}
		return '?';
	}
	
	public char getLetterOfFrequency(int freqRank)
	{
		// freqRank represents the relative frequency of a letter
		// freqRank=1 would be the the most frequent, 2 the second most, and so on
		if (freqRank < 1 || freqRank > sortSingles.length) // invalid input
		{
			return ' ';
		}
		
		return sortSingles[sortSingles.length - freqRank].getLetter();
	}
	
	public LetterPairPoly getPairOfFrequency(int freqRank)
	{
		if (freqRank < 1 || freqRank > sortDis.length)
		{
			return null;
		}
		
		return sortDis[sortDis.length - freqRank];
	}
	
	public LetterPairPoly getRepeatOfFrequency(int freqRank)
	{
		if (freqRank < 1 || freqRank > sortRepeats.length)
		{
			return null;
		}
		
		return sortRepeats[sortRepeats.length - freqRank];
	}
	
	public TrigraphPoly getTrigraphOfFrequency(int freqRank)
	{
		if (freqRank < 1 || freqRank > sortTris.length)
		{
			return null;
		}
		
		TrigraphPoly tri = sortTris[sortTris.length - freqRank];		
		return tri;
	}
	
	public LetterPairPoly[] getDigraphsWithLetter(char letter, boolean isFirst)
	{
		if (letter < 65 || letter > 90)
		{
			return null;
		}
		
		LetterPairPoly[] output = new LetterPairPoly[pairFrequencies.length];
		if (isFirst)
		{
			for (int col = 0; col < pairFrequencies[0].length; col++)
			{
				output[col] = pairFrequencies[letter - 65][col];
			}
		}
		else
		{
			for (int row = 0; row < pairFrequencies.length; row++)
			{
				output[row] = pairFrequencies[row][letter - 65];
			}
		}
		sort(output);
		return output;
	}
}