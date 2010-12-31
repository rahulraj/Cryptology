
public class LetterCounter 
{
	public static final int NUM_LETTERS = 26;
	
	private int[] singleFrequencies;
	private LetterPair[][] pairFrequencies;
	private Trigraph[][][] trigraphFrequencies;
	private String codedPhrase;

	
	public LetterCounter(String input)
	{
		singleFrequencies = new int[NUM_LETTERS]; 
		/* 
		 * Each index in singleFrequencies represents a letter
		 * 0 is a, 1 is b ... 25 is z
		 */
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
	}
	
	public String getFrequencies()
	{
		String output = "";
		countSingleLetters();
		for (int loc = 0; loc < singleFrequencies.length; loc++)
		{
			output += (char)(loc + 97) + ": " + singleFrequencies[loc] + "\n";
		}
		
		output += "\n--------------------\n\n";
		// count pairs and output the 20 highest
		countLetterPairs();
		
		for (int count = 1; count <= 20; count++)
		{
			output += getHighestUnlistedPair();
		}
		
		output += "\n--------------------\n\n";
		// output 10 highest trigraphs
		countTrigraphs();
		for (int count = 1; count <= 10; count++)
		{
			output += getHighestUnlistedTrigraph();
		}
		
		// facilitate future searches
		for (int row = 0; row < pairFrequencies.length; row++)		
			for (int col = 0; col < pairFrequencies[0].length; col++)
			{
				pairFrequencies[row][col].setListed(false);
			}
		
		for (int r = 0; r < trigraphFrequencies.length; r++)
			for (int c = 0; c < trigraphFrequencies[0].length; c++)
				for (int d = 0; d < trigraphFrequencies[0][0].length; d++)
				{
					trigraphFrequencies[r][c][d].setListed(false);
				}
		
		return output;
	}
	
	public char getMostFrequentLetter()
	{
		int highFreq = 0;
		int freqIndex = 0;
		for (int loc = 0; loc < singleFrequencies.length; loc++)
		{
			if (singleFrequencies[loc] > highFreq)
			{
				highFreq = singleFrequencies[loc];
				freqIndex = loc;
			}
		}
		return (char)(freqIndex + 65);
	}
	
	public char getSecondMostFrequent()
	{
		int[] temp = new int[singleFrequencies.length];
		for (int loc = 0; loc < singleFrequencies.length; loc++)
		{
			temp[loc] = singleFrequencies[loc];
		}
		
		// find most frequent index
		int highFreq = 0;
		int freqIndex = 0;
		for (int loc = 0; loc < temp.length; loc++)
		{
			if (temp[loc] > highFreq)
			{
				highFreq = temp[loc];
				freqIndex = loc;
			}
		}
		
		temp[freqIndex] = -1; // remove the most frequent
		
		highFreq = 0;
		freqIndex = 0;
		for (int loc = 0; loc < temp.length; loc++)
		{
			if (temp[loc] > highFreq)
			{
				highFreq = temp[loc];
				freqIndex = loc;
			}
		}
		return (char)(freqIndex + 65);
		
	}
	
	public String getMostFrequentPair()
	{
		/*int highFreq = 0;
		int highRow = 0;
		int highCol = 0;
		
		for (int row = 0; row < pairFrequencies.length; row++)
			for (int col = 0; col < pairFrequencies[row].length; col++)
			{
				if (pairFrequencies[row][col].getFrequency() > highFreq)
				{
					highFreq = pairFrequencies[row][col].getFrequency();
					highRow = row;
					highCol = col;
				}
			}
		
		return "" + (char)(highRow + 65) + (char)(highCol + 65);*/
		LetterPair highPair = getHighestUnlistedPair();
		highPair.setListed(false); // for future searches
		return "" + (char)(highPair.getRow() + 65) + (char)(highPair.getCol() + 65);
  	}
	
	public String getSecondMostFrequentPair()
	{
		/*int highFreq = 0;
		int highRow = 0;
		int highCol = 0;
		
		LetterPair[][] temp = new LetterPair[pairFrequencies.length][pairFrequencies[0].length];
		for (int aRow = 0; aRow < temp.length; aRow++)
			for (int aCol = 0; aCol < temp[aRow].length; aCol++)
			{
				LetterPair pair = new LetterPair(aRow, aCol);
				pair.setFrequency(pairFrequencies[aRow][aCol].getFrequency());
				temp[aRow][aCol] = pair;
			}
		
		for (int row = 0; row < temp.length; row++)
			for (int col = 0; col < temp[row].length; col++)
			{
				if (temp[row][col].getFrequency() > highFreq)
				{
					highFreq = temp[row][col].getFrequency();
					highRow = row;
					highCol = col;
				}
			}
		
		temp[highRow][highCol].setFrequency(-1);
		highFreq = 0;
		highRow = 0;
		highCol = 0;
		
		for (int r = 0; r < temp.length; r++)
			for (int c = 0; c < temp.length; c++)
			{
				if (temp[r][c].getFrequency() > highFreq)
				{
					highFreq = temp[r][c].getFrequency();
					highRow = r;
					highCol = c;
				}
			}
		
		return "" + (char)(highRow + 65) + (char)(highCol + 65);*/
		LetterPair pair1 = getHighestUnlistedPair(); // the highest pair is set to listed
		LetterPair pair2 = getHighestUnlistedPair(); // this returns the second most frequent, pair1 is the most frequent
		
		// facilitate future searches
		pair1.setListed(false);
		pair2.setListed(false);
		return "" + (char)(pair2.getRow() + 65) + (char)(pair2.getCol() + 65);
	}
	
	private void countSingleLetters()
	{
		for (int index = 0; index < codedPhrase.length(); index++)
		{
			char aLetter = codedPhrase.charAt(index);
			if ((aLetter >= 65) && (aLetter <= 90)) // the range of values for lowercase characters
			{
				singleFrequencies[aLetter - 65]++; // increment the corresponding value in the array
			}
		}
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
}
