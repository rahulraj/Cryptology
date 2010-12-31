
public class PolyalphabetKey 
{
	private char[] theKeys;
	
	public static final int NUM_LETTERS = 26;
	
	public PolyalphabetKey()
	{
		theKeys = getRandomShifts(); // generates an alphabet for the cipher
	}

	private char[] getRandomShifts()
	{
		char[] shifts = new char[NUM_LETTERS];
		boolean[] lettersUsed = new boolean[NUM_LETTERS];
		
		for (int loc = 0; loc < shifts.length; loc++)
		{
			boolean letterFound = false;
			while (!letterFound)
			{
				int aLetter = (int)(Math.random() * NUM_LETTERS); // generate a random int between 0 and NUM_LETTERS - 1
				if (!lettersUsed[aLetter]) // letter not already assigned
				{
					shifts[loc] = (char)(aLetter + 65);
					lettersUsed[aLetter] = true;
					letterFound = true;
				}
			}
		}
		return shifts;
	}
	
	public char getCipherFor(char plainLetter)
	{
		return theKeys[plainLetter - 65];
	}
}