/*
 * This code enciphers a message with a randomly generated cipher
 * It also outputs the cipher that is used as a key for the message sender
 */

public class RandomEncipherer 
{
	private String plainLanguage;
	private char[] randomShifts;
	
	public RandomEncipherer(String input)
	{
		plainLanguage = input.toUpperCase(); // case insensitive
		randomShifts = getRandomShifts();
	}
	
	private char[] getRandomShifts()
	{
		char[] shifts = new char[26];
		boolean[] lettersUsed = new boolean[26];
		
		for (int loc = 0; loc < shifts.length; loc++)
		{
			boolean letterFound = false;
			while (!letterFound)
			{
				int aLetter = (int)(Math.random() * 26); // generate a random int between 0 and 25
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
	
	public String encipher()
	{
		StringBuilder output = new StringBuilder("");
		//String output = "";
		System.out.println("Enciphering the plaintext (this may take time).");
		System.out.println("There are " + plainLanguage.length() + " characters to encipher(!)");
		for (int index = 0; index < plainLanguage.length(); index++)
		{
			char current = plainLanguage.charAt(index);
			if (current >= 65 && current <= 90) // a letter
			{
				output.append(randomShifts[current - 65]);
			}
			else
			{
				output.append(current);
			}
		}
		return group(output.toString());
	}

	private String group(String coded)
	{
		// divides the coded message in groups of 5 letters each
		// eliminates blank spaces and punctuation in original message
		StringBuilder output = new StringBuilder("");
		coded = coded.trim();
		
		int letters = 0; // variable to keep track of how many letters are in the current word
		int numWords = 0;
		for (int index = 0; index < coded.length(); index++)
		{
			if (letters == 5)
			{
				output.append(' ');
				letters = 0;
				numWords++;
			}
			if (numWords == 10)
			{
				output.append("\n");
				numWords = 0;
			}
			if (coded.charAt(index) >= 65 && coded.charAt(index) <= 90)
			{
				output.append(coded.charAt(index));
				letters++;
			}
		}
		return output.toString();
	}
	
	public String getCipher()
	{
		String str = "";
		for (int pos = 0; pos < randomShifts.length; pos++)
		{
			str += (char)(pos + 65) + ":   " + randomShifts[pos] + "\n";
		}
		return str;
	}
}