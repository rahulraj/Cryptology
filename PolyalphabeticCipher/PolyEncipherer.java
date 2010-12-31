
public class PolyEncipherer 
{
	private String toCipher;
	private PolyalphabetKey[] keys;
	
	public static final int MAX_ALPHS = 4;
	
	public PolyEncipherer(String pl)
	{
		// this constructor is not used because the number of alphabets is a decision made by the user
		// it is possible to modify the code that constructs the PolyEncipherer object to use this constructor
		// if you want the number of alphabets used to be randomly generated
		toCipher = pl.toUpperCase();
		
		// randomly generate a number of keys from 1 to 4
		int numKeys = (int)(Math.random() * MAX_ALPHS) + 1;
		keys = new PolyalphabetKey[numKeys];
		for (int loc = 0; loc < keys.length; loc++)
		{
			keys[loc] = new PolyalphabetKey();
		}
	}
	
	public PolyEncipherer(String pl, int numAlphs)
	{
		toCipher = pl.toUpperCase();
		keys = new PolyalphabetKey[numAlphs];
		for (int loc = 0; loc < keys.length; loc++)
		{
			keys[loc] = new PolyalphabetKey();
		}
	}
	
	public String encipher()
	{
		StringBuilder cipherText = new StringBuilder("");
		int currentKey = 0;
		for (int index = 0; index < toCipher.length(); index++)
		{
			if (toCipher.charAt(index) >= 65 && toCipher.charAt(index) <= 90)
			{
				if (currentKey == keys.length)
				{
					currentKey = 0;
				}
				cipherText.append(keys[currentKey].getCipherFor(toCipher.charAt(index)));
				currentKey++;
			}
		}
		return group(cipherText.toString());
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
	
	public String getKeys()
	{
		String str = "The " + keys.length + " polyalphabet keys are as follows:\n\n";
		
		for (PolyalphabetKey key : keys)
		{
			for (int loc = 0; loc < PolyalphabetKey.NUM_LETTERS; loc++)
			{
				str += (char)(loc + 65) + ":    " + key.getCipherFor((char)(loc + 65)) + "\n";
			}
			str += "\n----------\n\n";
		}
		return str;
	}
}