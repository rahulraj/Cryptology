
public class LetterShifter 
{
	private int a;
	private int b;
	private String messageToCode;
	// shifts all letters in messageToCode by ax + b
	
	public LetterShifter(int anA, int aB, String aMessage)
	{
		a = anA;
		b = aB;
		messageToCode = aMessage.toUpperCase(); // case insensitive
	}
	
	public String shiftMessage()
	{
		if (!isRelativePrime())
		{
			return "Message cannot be shifted by the given a value.";
		}
		String codedMessage = "";
		char currentLetter;
		
		for (int index = 0; index < messageToCode.length(); index++)
		{
			currentLetter = messageToCode.charAt(index);
			if (currentLetter >= 65 && currentLetter <= 90)
			{
				codedMessage += shiftLetter(currentLetter);
			}
			else
			{
				codedMessage += currentLetter;
			}
		}
		return codedMessage;
	}
	
	private boolean isRelativePrime()
	{
		if ((a == 13) || ((a % 2) == 0))
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	private char shiftLetter(char toShift)
	{
		// the int values of characters A-Z are 65-90 on the ASCII table
		// it must be converted to values 1-26
		int x = toShift - 64;
		
		// shift x by
		// x = ax + b 
		x *= a;
		x += b;
		
		// make it between 1 and 26 (wrap around mod 26)
		while (x > 26)
		{
			x -= 26;
		}
		
		// convert to the coded letter
		x += 64;
		return (char)x;
	}
	
}
