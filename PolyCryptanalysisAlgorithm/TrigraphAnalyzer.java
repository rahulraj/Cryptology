
public class TrigraphAnalyzer 
{
	private MonoalphabetAnalyzer first;
	private MonoalphabetAnalyzer second;
	private MonoalphabetAnalyzer third;
	
	private Trigraph[][][] theTris;
	private Trigraph[] mostFrequent;
	
	public TrigraphAnalyzer(MonoalphabetAnalyzer fir, MonoalphabetAnalyzer sec, MonoalphabetAnalyzer thi,
							int lastMono)
	{
		first = fir;
		second = sec;
		third = thi;
		
		theTris = new Trigraph[LetterCounter.NUM_LETTERS][LetterCounter.NUM_LETTERS][LetterCounter.NUM_LETTERS];
		// fill theTris
		for (int row = 0; row < theTris.length; row++)
			for (int col = 0; col < theTris[0].length; col++)
				for (int depth = 0; depth < theTris[0][0].length; depth++)
				{
					theTris[row][col][depth] = new Trigraph(row, col, depth);
				}
		
		String firstMono = first.getCipher();
		String secondMono = second.getCipher();
		String thirdMono = third.getCipher();
		
		if (lastMono == 1)
		{
			// firstMono is the last monoalphabet
			StringBuilder s = new StringBuilder("");
			s.append(secondMono.substring(1, secondMono.length()));
			s.append(" ");
			secondMono = s.toString();
			
			s = new StringBuilder("");
			s.append(thirdMono.substring(1, thirdMono.length()));
			s.append(" ");
			thirdMono = s.toString();
		}
		if (lastMono == 2)
		{
			// secondMono is the last monoalphabet
			StringBuilder s = new StringBuilder("");
			s.append(thirdMono.substring(1, thirdMono.length()));
			s.append(" ");
			thirdMono = s.toString();
		}
		
		for (int index = 0; index < thirdMono.length(); index++)
		{
			// check if all characters are inbounds
			// some could be whitespace
			if (firstMono.charAt(index) >= 65 && firstMono.charAt(index) <= 90 &&
				secondMono.charAt(index) >= 65 && secondMono.charAt(index) <= 90 &&
				thirdMono.charAt(index) >= 65 && thirdMono.charAt(index) <= 90)
			{
				theTris[firstMono.charAt(index) - 65]
					   [secondMono.charAt(index) - 65]
					   [thirdMono.charAt(index) - 65].incrementFreqency(); 
			}
		}
		
		mostFrequent = new Trigraph[LetterCounter.TRIS_TO_OUT];
		for (int count = 0; count < mostFrequent.length; count++)
		{
			mostFrequent[count] = getHighestUnlistedTrigraph();
		}
		
	}
	
	private Trigraph getHighestUnlistedTrigraph()
	{
		// search for the highest
		int highFrequency = -1;
		Trigraph highTri = null;
		for (int row = 0; row < theTris.length; row++)
			for (int col = 0; col < theTris[0].length; col++)
				for (int third = 0; third < theTris[0][0].length; third++)
				{
					Trigraph tri = theTris[row][col][third];
					if (!tri.isListed() && tri.getFrequency() > highFrequency)
					{
						highTri = tri;
						highFrequency = tri.getFrequency();
					}
				}
		highTri.setListed(true);
		return highTri;
	}
 
	public void solveD()
	{
		// most common letter to follow the digraph AN
		char aCip = first.getCipherFor('A');
		char nCip = second.getCipherFor('N');
		if (aCip == '?' || nCip == '?') // not enough info
		{
			return;
		}
		
		int highLoc = 0;
		int highFreq = 0;
		for (int loc = 0; loc < theTris[0][0].length; loc++)
		{
			if (theTris[aCip - 65][nCip - 65][loc].getFrequency() > highFreq)
			{
				highLoc = loc;
				highFreq = theTris[aCip - 65][nCip - 65][loc].getFrequency();
			}
		}
		char dCip = (char)(highLoc + 65);
		if (!third.cipherIsSolved(dCip))
		{
			third.setPlainLetterAs('D', dCip);
		}
	}
	
	public void solveI()
	{
		// we know T and O already
		// I is the letter most likely to be between them in the trigraphs
		// H is also common, but we know it. This will be checked for and accounted for.
		char tCip = first.getCipherFor('T');
		char oCip = third.getCipherFor('O');
		char hCip = second.getCipherFor('H');
		if (tCip == '?' || oCip == '?' || hCip == '?')
		{
			return;
		}
		
		int highLoc = 0;
		int highFreq = 0;
		for (int loc = 0; loc < theTris[0].length; loc++)
		{
			if (theTris[tCip - 65][loc][oCip - 65].getFrequency() > highFreq &&
				(char)(loc + 65) != hCip)
			{
				highLoc = loc;
				highFreq = theTris[tCip - 65][loc][oCip - 65].getFrequency();
			}
		}
		char iCip = (char)(highLoc + 65);
		if (!second.cipherIsSolved(iCip))
		{
			second.setPlainLetterAs('I', iCip);
		}
	}
	
	public void solveY()
	{
		// we know O and U
		// Y is the most likely letter to be before them
		char oCip = second.getCipherFor('O');
		char uCip = third.getCipherFor('U');
		if (oCip == '?' && uCip == '?')
		{
			return;
		}
		
		int highLoc = 0;
		int highFreq = 0;
		for (int loc = 0; loc < theTris.length; loc++)
		{
			if (theTris[loc][oCip - 65][uCip - 65].getFrequency() > highFreq)
			{
				highLoc = loc;
				highFreq = theTris[loc][oCip - 65][uCip - 65].getFrequency();
			}
		}
		char yCip = (char)(highLoc + 65);
		if (!first.cipherIsSolved(yCip))
		{
			first.setPlainLetterAs('Y', yCip);
		}
	}
	
	public void solveG()
	{
		// knowing I and N
		// G is the most likely letter to follow them
		char iCip = first.getCipherFor('I');
		char nCip = second.getCipherFor('N');
		if (iCip == '?' || nCip == '?')
		{
			return;
		}
		
		int highLoc = 0;
		int highFreq = 0;
		for (int loc = 0; loc < theTris[0][0].length; loc++)
		{
			if (theTris[iCip - 65][nCip - 65][loc].getFrequency() > highFreq)
			{
				highLoc = loc;
				highFreq = theTris[iCip - 65][nCip - 65][loc].getFrequency();
			}
		}
		char gCip = (char)(highLoc + 65);
		if (!third.cipherIsSolved(gCip))
		{
			third.setPlainLetterAs('G', gCip);
		}
	}
}