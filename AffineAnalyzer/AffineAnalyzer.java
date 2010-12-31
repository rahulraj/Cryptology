import java.util.Scanner;

public class AffineAnalyzer 
{
	private String coded;
	private Scanner scan;
	
	public AffineAnalyzer(String input, Scanner dataIn)
	{
		coded = input;
		scan = dataIn;
	}
	
	public void analyze()
	{
		LetterCounter counter = new LetterCounter(coded);
		System.out.println("Letter Frequencies are:\n\n" + counter.getFrequencies());
		
		// output possible answers based on most frequent digraphs in the cipher
		// the most frequent digraphs in English include EN, RE, ER, TH, and HE
		// Note: several sources conflicted as to exactly which digraphs were the most frequent
		// moreso than with single letters, digraph frequency may differ based on the type of message
		String mostFrequent = counter.getMostFrequentPair();
		System.out.println("Some possible answers are:");
		System.out.println(solveWithDigraph("EN", mostFrequent));
		System.out.println(solveWithDigraph("RE", mostFrequent));
		System.out.println(solveWithDigraph("ER", mostFrequent));
		System.out.println(solveWithDigraph("TH", mostFrequent));
		System.out.println(solveWithDigraph("HE", mostFrequent));
		
		// the most frequent digraph could be an anamoly, therefore also test the second most frequent
		String secondMost = counter.getSecondMostFrequentPair();
		System.out.println(solveWithDigraph("EN", secondMost));
		System.out.println(solveWithDigraph("RE", secondMost));
		System.out.println(solveWithDigraph("ER", secondMost));
		System.out.println(solveWithDigraph("TH", secondMost));
		System.out.println(solveWithDigraph("HE", secondMost));
		
		// add more as needed
		
		// user input phase
		System.out.println("Do any of these possible answers make sense? (Y/N)");
		String input = scan.next();
		while (!(input.equalsIgnoreCase("Y") || input.equalsIgnoreCase("N")))
		{
			System.out.println("Please enter either \"Y\" or \"N\".");
			input = scan.next();
		}
		if (input.equalsIgnoreCase("Y"))
		{
			System.exit(0);
		}
		
		System.out.println("There are two ways to proceed.");
		System.out.println("1. Input a single plaintext letter and its cipher equivalent that you think works.");
		System.out.println("2. Input a possible digraph in the same manner.");
		System.out.println("Please type the number you wish to do.");
		input = scan.next();
		
		while (true)
		{
			if (input.equals("1"))
			{
				System.out.println("Please input a letter pair that you think works.");
				System.out.println("When entering the letters, please type the plain text letter, then a space," +
								   " then its cipher equivalent.");
				String plain = scan.next().toUpperCase();
				String cipher = scan.next().toUpperCase();
				System.out.println("Possible answers are:\n\n" + getPossibleAnswers(plain, cipher));
			}
			if (input.equals("2"))
			{
				System.out.println("Please input two digraphs.");
				System.out.println("Type the plaintext digraph, a space," +
						           " and the ciphertext digraph on the same line please.");
				String pl = scan.next().toUpperCase();
				String cip = scan.next().toUpperCase();
				System.out.println(solveWithDigraph(pl, cip));
			}
			System.out.println("Does the answer make sense? (Y/N)");
			input = scan.next();
			while (!(input.equalsIgnoreCase("Y") || input.equalsIgnoreCase("N")))
			{
				System.out.println("Please enter either \"Y\" or \"N\".");
				input = scan.next();
			}
			if (input.equalsIgnoreCase("Y"))
			{
				System.exit(0);
			}
			System.out.println("Please enter either \"1\" for single letters or \"2\" for digraphs.");
			input = scan.next();
			// the loop will continue until the user finds a satisfactory answer
		}
	}

	private String solveWithDigraph(String plain, String cipher)
	{
		/*
		 * There are two unknowns in an affine cipher
		 * So it can be solved with two letters providing two equations
		 * The equations are: a * C + b = P (mod 26) where C and P are known
		 * Subtract the second from the first
		 */
		
		String output = "If plaintext " + plain + " equals ciphertext " + cipher + "\n";
		
		// first equation
		int plain1 = plain.charAt(0) - 64;
		int cip1 = cipher.charAt(0) - 64;
		
		// second equation
		int plain2 = plain.charAt(1) - 64;
		int cip2 = cipher.charAt(1) - 64;
		
		// exception occurs if plaintext letters are 13 apart, which causes ciphertext letters to also be 13 apart
		// in these cases the equation system cannot be used to solve the cipher
		// it is like a system with two equations for the same line
		if (((plain1 - plain2) == 13) || ((plain2 - plain1) == 13))
		{
			if (((cip1 - cip2) == 13) || ((cip2 - cip1) == 13))
			{
				String p = "" + plain.charAt(0);
				String c = "" + cipher.charAt(0);
				output += "The two plaintext letters are 13 apart and cannot be used in the algebraic method" +
						  " to obtain a single answer. Possible answers for this case are:\n" +
						  getPossibleAnswers(p, c);
				return output;
			}
			else
			{
				return "It is not possible for plaintext " + plain + " to equal ciphertext " +
					   cipher + " because this is a violation of the Thirteen Apart Theorem.\n";
			}
		}
		
		
		// subtracting the second equation from the first gets (cip1 - cip2)a = (plain1 - plain2)
		// however it is possible that the coefficient of a will be even or 13
		// in this case the Thirteen Apart Theorem must be implemented
		// this states that two plaintext letters 13 places apart will correspond to two ciphertext letters 13 apart
		if (((makeMod26(cip1 - cip2) % 2) == 0) || (makeMod26(cip1 - cip2) == 13))
		{
			cip2 = makeMod26(cip2 - 13);
			plain2 = makeMod26(plain2 - 13);
		}
		
		int aCoefficient = makeMod26(cip1 - cip2);
		int rightSide = makeMod26(plain1 - plain2);
		
		// isolate a, the deciphering shift
		int aVal = makeMod26(rightSide * getMultInverse(aCoefficient));
		
		// a can now be plugged back into one of the original equations to get b
		// the first equation is used here
		int bVal = makeMod26(plain1 - (aVal * cip1));
		
		// output the answers
		output += "Enciphering multiplier: " + getMultInverse(aVal) + 
				  " Enciphering shift: " + (26 - bVal) + "\n";
		output += "Deciphering multiplier: " + aVal + " Deciphering shift: " + bVal + "\n";
		LetterShifter shift = new LetterShifter(aVal, bVal, coded);
		output += shift.shiftMessage() + "\n\n";
		
		return output;
	}
	
	private String getPossibleAnswers(String plain, String cipher)
	{
		/*
		 * When one has a plain/cipher letter pair for an Affine cipher
		 * The equation between them is: a * C + b = P (mod 26)
		 * where C is the cipher letter, P is the plain one, 
		 * and a and b are unknown (the deciphering shift and multiplier)
		 * One can attempt to solve the cipher through output of all possible values of a and b
		 */
		
		int pl = plain.charAt(0) - 64;
		int cip = cipher.charAt(0) - 64;
		String output = "";
		/*
		 * pl is P and cip is C
		 * The equation derives to:
		 * b = P - C * a (mod 26)
		 * 
		 * a can be any of the numbers relatively prime to 26
		 * so plug in all these values and solve for b
		 */
		int[] aValues = {1, 3, 5, 7, 9, 11, 15, 17, 19, 21, 23, 25};
		
		for (int anA : aValues)
		{
			int aB = makeMod26(pl - cip * anA);
			//int aB = makeMod26(pl - cip) * anA;
			//aB = makeMod26(aB);
			int encShift = findEncShift(pl, cip, getMultInverse(anA));
			output += "Enciphering multiplier: " + getMultInverse(anA) + " Enciphering shift: " + encShift + "\n";
			output += "Deciphering multiplier: " + anA + " Deciphering shift: " + aB + "\n";
			LetterShifter shift = new LetterShifter(anA, aB, coded);
			output += shift.shiftMessage() + "\n\n";
		}
		return output;
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
	
	private int getMultInverse(int num)
	{
		int output = 0;
		switch(num)
		{
		case 1:
			output = 1;
			break;
		case 3:
			output = 9;
			break;
		case 5:
			output = 21;
			break;
		case 7:
			output = 15;
			break;
		case 9:
			output = 3;
			break;
		case 11:
			output = 19;
			break;
		case 15:
			output = 7;
			break;
		case 17:
			output = 23;
			break;
		case 19:
			output = 11;
			break;
		case 21:
			output = 5;
			break;
		case 23:
			output = 17;
			break;
		case 25:
			output = 25;
			break;
		}

		return output;
	}
	
	private int findEncShift(int plain, int cip, int encMult)
	{
		plain *= encMult;
		plain = makeMod26(plain);
		return makeMod26(cip - plain);
	}
	
	public static void main(String[] args) 
	{
		Scanner dataIn = new Scanner(System.in);
		System.out.println("Enter message to be decoded. You may use as many lines as needed.");
		System.out.println("Enter \"endOfMessage\" to finish input.");
		
		String message = "";
		String input = dataIn.nextLine();
		
		while (!input.equals("endOfMessage"))
		{
			message += input;
			input = dataIn.nextLine();
		}
		
		AffineAnalyzer an = new AffineAnalyzer(message, dataIn);
		an.analyze();
	}
}