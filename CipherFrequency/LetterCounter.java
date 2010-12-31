
public class LetterCounter 
{
	public static final int NUM_LETTERS = 26;
	
	private Letter[] singleFrequencies;
	private LetterPair[][] pairFrequencies;
	private Trigraph[][][] trigraphFrequencies;
	private String codedPhrase;
	
	private Letter[] sortSingles;
	private LetterPair[] sortDis;
	private LetterPair[] sortRepeats;
	private Trigraph[] sortTris;

	
	public LetterCounter(String input)
	{
		singleFrequencies = new Letter[NUM_LETTERS]; 
		/* 
		 * Each index in singleFrequencies represents a letter
		 * 0 is a, 1 is b ... 25 is z
		 */
		// populate singleFequencies
		for (int loc = 0; loc < singleFrequencies.length; loc++)
		{
			singleFrequencies[loc] = new Letter((char)(loc + 65));
		}
		
		pairFrequencies = new LetterPair[NUM_LETTERS][NUM_LETTERS];
		/*
		 * Using a numerical system similar to singleFreqencies
		 * The row is the first letter, the column is the second
		 */
		// Populate pairFreqencies
		for (int row = 0; row < pairFrequencies.length; row++)
			for (int col = 0; col < pairFrequencies[0].length; col++)
			{
				pairFrequencies[row][col] = new LetterPair(row, col);
			}
		
		trigraphFrequencies = new Trigraph[NUM_LETTERS][NUM_LETTERS][NUM_LETTERS];
		/*
		 * Again, a similar system is used
		 */
		// Populate the array
		for (int row = 0; row < trigraphFrequencies.length; row++)
			for (int col = 0; col < trigraphFrequencies[0].length; col++)
				for (int third = 0; third < trigraphFrequencies[0][0].length; third++)
				{
					trigraphFrequencies[row][col][third] = new Trigraph(row, col, third);
				}
		
		codedPhrase = input.toUpperCase(); // not case sensitive
		
		sortSingles = new Letter[NUM_LETTERS];
		sortDis = new LetterPair[20];
		sortRepeats = new LetterPair[NUM_LETTERS];
		sortTris = new Trigraph[10];
	}
	
	public String getFrequencies()
	{
		StringBuilder output = new StringBuilder("Single frequencies by letter:\n");
		countSingleLetters();
		for (Letter let : singleFrequencies)
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
		
		// output the 20 highest pairs
		// in the process, populate sortDis
		output.append("Most common digraphs:\n");
		for (int count = 0; count < 20; count++)
		{
			LetterPair pair = getHighestUnlistedPair();
			output.append(pair.toString());
			sortDis[count] = pair;
		}
		
		sort(sortDis);
		
		output.append("\n--------------------\n\n");
		
		// output 10 highest trigraphs and populate sortTris
		output.append("Most common trigraphs:\n");
		countTrigraphs();
		for (int count = 0; count < 10; count++)
		{
			Trigraph tri = getHighestUnlistedTrigraph();
			output.append(tri.toString());
			sortTris[count] = tri;
		}
		
		sort(sortTris);

		// to get in-depth frequency data about digraphs with a certain letter in it
		// you can add lines of code here calling the method digraphsWithLetter(char letter, boolean isStart)
		// an example is as follows:
		//output.append(digraphsWithLetter('E', true));
		// the above line of code, if uncommented, will add to the output file a list of all digraphs
		// that start with the letter E
		
		return output.toString();
	}
	
	private String digraphsWithLetter(char letter, boolean isStart)
	{
		// this method can be called to return a string listing the frequencies of digraphs containing letter
		// if isStart is true, it returns digraphs starting with letter
		// else it returns digraphs ending with letter
		// this method is not called normally, but can be in a code modification to look for
		// in-depth frequency data
		StringBuilder output = new StringBuilder("\n--------------------\n\nDigraphs");
		if (isStart)
		{
			output.append(" starting with ");
		}
		else
		{
			output.append(" ending in ");
		}
		output.append(letter + "\n");
		LetterPair[] dis = digraphsWithLetterHelper(letter, isStart);
		for (int loc = dis.length - 1; loc >= 0; loc--)
		{
			output.append(dis[loc].toString());
		}
		return output.toString();
	}
	
	private LetterPair[] digraphsWithLetterHelper(char letter, boolean isFirst)
	{
		LetterPair[] output = new LetterPair[pairFrequencies.length];
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
			Letter toCopy = singleFrequencies[loc];
			sortSingles[loc] = new Letter(toCopy.getLetter(), toCopy.getFrequency());
		}
		
		sort(sortSingles);
		
		int totalFreqs = 0;
		for (Letter let : sortSingles)
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
			out.append("This letter appears " + difference + " more times than\n");
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
		for (int pos = sortRepeats.length - 1; pos >= sortRepeats.length - 11; pos--)
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

	private LetterPair getHighestUnlistedPair()
	{
		// search for the highest
		int highFrequency = -1;
		LetterPair highPair = null;
		for (int row = 0; row < pairFrequencies.length; row++)
			for (int col = 0; col < pairFrequencies[0].length; col++)
			{
				LetterPair pair = pairFrequencies[row][col];
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
	
	private Trigraph getHighestUnlistedTrigraph()
	{
		// search for the highest
		int highFrequency = -1;
		Trigraph highTri = null;
		for (int row = 0; row < trigraphFrequencies.length; row++)
			for (int col = 0; col < trigraphFrequencies[0].length; col++)
				for (int third = 0; third < trigraphFrequencies[0][0].length; third++)
				{
					Trigraph tri = trigraphFrequencies[row][col][third];
					if (!tri.isListed() && tri.getFrequency() > highFrequency)
					{
						highTri = tri;
						highFrequency = tri.getFrequency();
					}
				
				}
		highTri.setListed(true);
		return highTri;
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
	
	public LetterPair getPairOfFrequency(int freqRank)
	{
		if (freqRank < 1 || freqRank > sortDis.length)
		{
			return null;
		}
		
		return sortDis[sortDis.length - freqRank];
	}
	
	public LetterPair getRepeatOfFrequency(int freqRank)
	{
		if (freqRank < 1 || freqRank > sortRepeats.length)
		{
			return null;
		}
		
		return sortRepeats[sortRepeats.length - freqRank];
	}
	
	public Trigraph getTrigraphOfFrequency(int freqRank)
	{
		if (freqRank < 1 || freqRank > sortTris.length)
		{
			return null;
		}
		
		Trigraph tri = sortTris[sortTris.length - freqRank];
		return tri;
	}
}