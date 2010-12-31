import java.util.Scanner;

public class LinearAnalyzer 
{
	private String enc;
	private LetterCounter count;
	
	public LinearAnalyzer(String input)
	{
		enc = input.toUpperCase(); // ignores case
		count = new LetterCounter(enc);
	}
	
	public void solveMessage(Scanner dataIn) 
	{
		System.out.println("Letter frequencies are:");
		System.out.println(count.getFrequencies());
		
		System.out.println("Possible versions of the plain text are also printed.");
		System.out.println("Most likely shifts: \n\n");
		doMostLikelyShifts();
		
		
		System.out.println("Has a message that makes sense been outputted? (Y/N)");
		String input = dataIn.nextLine();
		while ( !(input.equalsIgnoreCase("Y") || input.equalsIgnoreCase("N")) )
		{
			System.out.println("Please enter \"Y\" or \"N\" (Without the quotes).");
			input = dataIn.nextLine();
		}
		
		if (input.equalsIgnoreCase("Y"))
		{
			System.exit(0);
		}
		
		System.out.println("There are two ways to proceed:");
		
		while (true)
		{
			System.out.println("1. Enter a possible letter pair.");
			System.out.println("2. See all possible shifts.");
			System.out.println("Please enter either \"1\" or \"2\".");
			input = dataIn.next();
			while ( !(input.equals("1") || input.equals("2")) )
			{
				System.out.println("Please enter \"1\" or \"2\" (Without the quotes).");
				input = dataIn.nextLine();
			}
			if (input.equals("1"))
			{
				System.out.println("Enter the plain text character, then a space, then the cipher character " +
								   "that you think goes with it.");
				char plain = dataIn.next().toUpperCase().charAt(0);
				char cipher = dataIn.next().toUpperCase().charAt(0);
				
				int encShift = makeMod26(cipher - plain); 

				int decShift = makeMod26(26 - encShift);
				LetterShifter lShift = new LetterShifter(1, decShift, enc);
				System.out.println("Enciphering shift: " + encShift + " Deciphering shift: " + decShift);
				System.out.println(lShift.shiftMessage());
				
				System.out.println("Is this correct? (Y/N)");
				input = dataIn.next();
				if (input.equalsIgnoreCase("Y"))
				{
					System.exit(0);
				}
				// if input is Y the loop will reiterate
			}
			else if (input.equals("2"))
			{
				shiftByAll();
				System.exit(0);
			}
		}
		
		/*input = dataIn.nextLine();
		while ( !(input.equals("1") || input.equals("2")) )
		{
			System.out.println("Please enter \"1\" or \"2\" (Without the quotes).");
			input = dataIn.nextLine();
		}
		
		if (input.equals("1"))
		{
			System.out.println("Enter the plain text character, then a space, then the cipher character " +
							   "that you think goes with it.");
			while (true)
			{
				char plain = dataIn.next().toUpperCase().charAt(0);
				char cipher = dataIn.next().toUpperCase().charAt(0);
				
				int encShift = makeMod26(cipher - plain); 

				int decShift = makeMod26(26 - encShift);
				LetterShifter lShift = new LetterShifter(1, decShift, enc);
				System.out.println("Enciphering shift: " + encShift + " Deciphering shift: " + decShift);
				System.out.println(lShift.shiftMessage());
				
				System.out.println("Is this correct? (Y/N)");
				input = dataIn.nextLine();
				if (input.equalsIgnoreCase("Y"))
				{
					System.exit(0);
				}
				else
				{
					System.out.println("You may type \"1\" to input another pair " + 
									   "or \"2\" to see all shifts.");	
					input = dataIn.nextLine();
					if (input.equals("2"))
					{
						shiftByAll();
						System.exit(0);
					} 
					if (input.equals("1"))
					{
						// if user inputs "1" the while loop will continue
						System.out.println("Type another set of characters.");
					}
				}
			}
		}
		else
		{
			shiftByAll();
			System.exit(0);
		}*/
	}
	
	private void doMostLikelyShifts()
	{
		// test possibility that most frequent letter is E
		char mostFrequent = count.getMostFrequentLetter();
		int shiftVal = 69 - mostFrequent; // 69 is the value for E
		if (shiftVal < 0) 
		{
			shiftVal += 26;
		}
		LetterShifter shift = new LetterShifter(1, shiftVal, enc);
		System.out.println("If plaintext letter E is ciphertext letter " + mostFrequent + " then:");
		System.out.println("Shift of cipher is: " + (26 - shiftVal) + "\n" + shift.shiftMessage() + "\n");
		
		// most frequent is T (second most common letter)
		shiftVal = 84 - mostFrequent; // 84 is int value of T
		if (shiftVal < 0) 
		{
			shiftVal += 26;
		}
		shift = new LetterShifter(1, shiftVal, enc);
		System.out.println("If plaintext letter T is ciphertext letter " + mostFrequent + " then:");
		System.out.println("Shift of cipher is: " + (26 - shiftVal) + "\n" + shift.shiftMessage() + "\n");
		
		// the most common letter could be an anomaly, therefore test the second-most common
		
		// with E
		char secondMost = count.getSecondMostFrequent();
		shiftVal = 69 - secondMost;
		if (shiftVal < 0) 
		{
			shiftVal += 26;
		}
		shift = new LetterShifter(1, shiftVal, enc);
		System.out.println("If plaintext letter E is ciphertext letter " + secondMost + " then:");
		System.out.println("Shift of cipher is: " + (26 - shiftVal) + "\n" + shift.shiftMessage() + "\n");
		
		// with T
		shiftVal = 84 - secondMost;
		if (shiftVal < 0) 
		{
			shiftVal += 26;
		}
		shift = new LetterShifter(1, shiftVal, enc);
		System.out.println("If plaintext letter T is ciphertext letter " + secondMost + " then:");
		System.out.println("Shift of cipher is: " + (26 - shiftVal) + "\n" + shift.shiftMessage() + "\n");
		
		// one can add more methods of analysis as needed
	}
	
	private void shiftByAll()
	{
		for (int sh = 25; sh >= 0; sh--)
		{
			LetterShifter shift = new LetterShifter(1, sh, enc);
			System.out.println("Enciphering shift: " + (26 - sh) + " Deciphering shift: " + sh);
			System.out.println(shift.shiftMessage() + "\n");
		}
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
	
	public static void main(String[] args) 
	{
		Scanner dataIn = new Scanner(System.in);		
		System.out.println("Enter the encrypted message.");
		System.out.println("Enter \"endOfMessage\" on its own line to finish.");
		String input = "";
		String message = "";
		input = dataIn.nextLine();
		while (!input.equals("endOfMessage"))
		{
			message += input;
			input = dataIn.nextLine();
		}
		
		LinearAnalyzer analyze = new LinearAnalyzer(message);
		
		analyze.solveMessage(dataIn);
	}
}