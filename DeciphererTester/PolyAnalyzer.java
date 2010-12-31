import java.io.PrintWriter;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PolyAnalyzer 
{
	private String cipher;
	private File freqs;
	private File plainOut;
	private int numAlphs;
	
	public PolyAnalyzer(String cip, File freqOut, File decOut)
	{
		cipher = cip.toUpperCase();
		freqs = freqOut;
		plainOut = decOut;
		numAlphs = 0;
	}
	
	public PolyAnalyzer(String cip, File freqOut, File decOut, int theNumAlphs)
	{
		cipher = cip.toUpperCase();
		freqs = freqOut;
		plainOut = decOut;
		numAlphs = theNumAlphs;
	}
	
	public void automatedDecipher() throws IOException
	{
		// deciphers text without user input, does not output frequency data or keys
		// does not have all the features that the user input method does
		int friedNum;
		if (numAlphs != 0)
		{
			friedNum = numAlphs;
		}
		else
		{
			friedNum = getNumAlphabets();
		}
		automatedSolve(friedNum);
	}
	
	private void automatedSolve(int alphs) throws IOException
	{
		// when the MonoalphabetAnalyzers are initialized, they solve for a few of the most common letters
		// it is necessary for the analyzers to communicate for the solving of less common letters
		MonoalphabetAnalyzer[] analyzers = makeMonos(alphs);

		// used to analyze letter patterns and solve substitution ciphers
		MonoalphabetComparer comp = new MonoalphabetComparer(analyzers);
		comp.analyzeSubData(); 
		
		String decip = comp.decipheredWithoutKeys();
		PrintWriter plainWrite = new PrintWriter(plainOut);
		plainWrite.println(decip);
		plainWrite.close();
	}
	
	public void decipher() throws IOException
	{
		LetterCounter counter = new LetterCounter(cipher);
		PrintWriter freqDataOut = new PrintWriter(freqs);
		freqDataOut.println(counter.getFrequencies());
		System.out.println("Frequency data has been printed to the frequency output file.");
		freqDataOut.println("Total length is" + cipher.length());
		freqDataOut.close();
		
		
		int friedNum;
		if (numAlphs != 0)
		{
			friedNum = numAlphs;
		}
		else
		{
			friedNum = getNumAlphabets();
		}
		
		Scanner kbd = new Scanner(System.in);
		
		while (true)
		{
			// Make string to search in for the Kaisiski test
			StringBuilder kaisiskiCip = new StringBuilder("");
			for (int index = 0; index < cipher.length(); index++)
			{
				if (cipher.charAt(index) >= 65 && cipher.charAt(index) <= 90)
				{
					kaisiskiCip.append(cipher.charAt(index));
				}
			}
			String toInspect = kaisiskiCip.toString();
			
			System.out.println("Options are:");
			System.out.println("1. Implement the Kaisiski test with an inputted string of characters.");
			System.out.println("2. Input a number of alphabets (if given is unsatisfactory).");
			System.out.println("3. Solve the cipher as a group of substitution ciphers.");
			System.out.println("4. Solve the cipher as a Vigenere cipher.");
			System.out.println("5. Terminate the program.");
			
			int	choice = kbd.nextInt();
			switch(choice)
			{
			case 1:
				// Kaisiski test goes here
				System.out.println("Enter string to search for.");
				String toSearch = kbd.next();
				int startIndex = 0;
				int strIndex = toInspect.indexOf(toSearch, startIndex);
				while (strIndex != -1)
				{
					System.out.print("String found at index " + (strIndex + 1));
					if (startIndex != 0)
					{
						int gap = strIndex - startIndex;
						System.out.println(" which is " + gap + " characters away from" +
										   " the previous index.");
						ArrayList<Integer> factors = new ArrayList<Integer>();
						primeFactorization(gap, factors);
						System.out.println("Factors of " + gap + " are " + factors.toString());
					}
					else
					{
						System.out.println(); // go to a new line
					}
					startIndex = strIndex;
					strIndex = toInspect.indexOf(toSearch, startIndex + 1);
				}
				break;
			case 2:
				System.out.println("What number of alphabets is there?");
				System.out.println("Enter a guess based on the given data.");
				int alphs = kbd.nextInt();
				friedNum = alphs;
				break;
			case 3:
				solveSubCipher(friedNum);
				break;
			case 4:
				solveVigCipher(friedNum);
				break;
			case 5:
				System.exit(0);
				break;
			default:
				System.out.println("Invalid input.");
				break;
			}
		}
	}
	
	private void primeFactorization(int toFactor, ArrayList<Integer> factors)
	{
		// For efficiency, this primeFactorization method assumes that the maximum number of alphabets is 
		// between 5 and 10 and prime factors higher than 7 are not useful
		// It will need modification if the total number of alphabets is higher
		if ((toFactor % 2) == 0)
		{
			toFactor /= 2;
			factors.add(2);
			primeFactorization(toFactor, factors);
		}
		else if ((toFactor % 3) == 0)
		{
			toFactor /= 3;
			factors.add(3);
			primeFactorization(toFactor, factors);
		}
		else if ((toFactor % 5) == 0)
		{
			toFactor /= 5;
			factors.add(5);
			primeFactorization(toFactor, factors);
		}
		else if ((toFactor % 7) == 0)
		{
			toFactor /= 7;
			factors.add(7);
			primeFactorization(toFactor, factors);
		}
		else if (toFactor != 1)
		{
			factors.add(toFactor);
		}
	}
	
	private int getNumAlphabets()
	{
		// Using Friedman Test
		int[] numEachLetter = new int[26];
		long totalLetters = 0;
		for (int index = 0; index < cipher.length(); index++)
		{
			if (cipher.charAt(index) >= 65 && cipher.charAt(index) <= 90)
			{
				totalLetters++;
				numEachLetter[cipher.charAt(index) - 65]++;
			}
		}
		
		double indexCoincidence = getIC(numEachLetter, totalLetters);
		double numer = 0.027 * totalLetters;
		
		double denom = (totalLetters - 1) * indexCoincidence;
		denom -= (0.038 * totalLetters);
		denom += 0.065;
		
		double r = numer / denom;
		int numAlphs = (int)(Math.round(r));
		System.out.println("The Friedman test results in the number " + r);
		System.out.println("Number of alphabets is therefore: " + numAlphs);
		return numAlphs;
	}
	
	private double getIC(int[] numEachLetter, long totalLetters)
	{
		// solve for the index of coincidence of the ciphertext as part of the Friedman test
		double IC = 0.0;
		long denom = (totalLetters * (totalLetters - 1));
		//System.out.println("To find the denominator, " + totalLetters + " was multiplied by " +
			//			   (totalLetters - 1));
		//System.out.println("It is solved as: " + denom);
		//System.out.println("The direct multiplication resolves as " + (136469 * 136468));
		//System.out.println("For reference, the maximum value of an int is: " + Integer.MAX_VALUE);
		for (int i = 0; i < numEachLetter.length; i++)
		{
			int numer = numEachLetter[i] * (numEachLetter[i] - 1);
			
			IC += ((double)numer) / ((double)denom);
		}
		System.out.println("IC is: " + IC);
		return IC;
	}
	
	private void solveSubCipher(int numAlphabets) throws IOException
	{
		// when the MonoalphabetAnalyzers are initialized, they solve for a few of the most common letters
		// it is necessary for the analyzers to communicate for the solving of less common letters
		MonoalphabetAnalyzer[] analyzers = makeMonos(numAlphabets);
		

		// used to analyze letter patterns and solve substitution ciphers
		MonoalphabetComparer comp = new MonoalphabetComparer(analyzers);
		comp.analyzeSubData(); 
		
		String decip = comp.getDeciphered();
		int numSolved = 0;
		for (int index = 0; index < decip.length(); index++)
		{
			if (decip.charAt(index) != '?')
			{
				numSolved++;
			}
		}
		double percentSolved = (double)numSolved / (double)decip.length();
		percentSolved *= 100;
		System.out.println(percentSolved + " percent solved"); 
		printPlain(decip);
	}
	
	private void solveVigCipher(int numAlphabets) throws IOException
	{
		MonoalphabetAnalyzer[] analyzers = makeMonos(numAlphabets);
		for (MonoalphabetAnalyzer an : analyzers)
		{
			an.solveFreqs();
			an.solveAsCaesar();
		}
		MonoalphabetComparer comp = new MonoalphabetComparer(analyzers);
		printPlain(comp.getDeciphered());
	}
	
	private MonoalphabetAnalyzer[] makeMonos(int numAlphabets)
	{
		StringBuilder[] alphabets = new StringBuilder[numAlphabets];
		for (int loc = 0; loc < alphabets.length; loc++)
		{
			alphabets[loc] = new StringBuilder("");
		}
		
		int alphCount = 0;
		for (int index = 0; index < cipher.length(); index++)
		{
			if (cipher.charAt(index) >= 65 && cipher.charAt(index) <= 90)
			{
				alphabets[alphCount].append(cipher.charAt(index));
				if (alphCount < (alphabets.length - 1))
				{
					alphCount++;
				}
				else
				{
					alphCount = 0;
				}
			}
		}
		
		// add whitespace to the ends of the shorter components of alphabets
		// so they are all the same length
		// this avoids a IndexOutOfBounds exception
		while (alphCount < alphabets.length)
		{
			alphabets[alphCount].append(" ");
			alphCount++;
		}
		
		MonoalphabetAnalyzer[] analyzers = new MonoalphabetAnalyzer[numAlphabets];
		for (int pos = 0; pos < analyzers.length; pos++)
		{
			analyzers[pos] = new MonoalphabetAnalyzer(alphabets[pos].toString());
			analyzers[pos].setNumber(pos + 1);
		}
		return analyzers;
	}
	
	private void printPlain(String plain) throws IOException
	{
		PrintWriter plainWrite = new PrintWriter(plainOut);
		plainWrite.println(plain);
		System.out.println("Possible plaintext outputted to file.");
		plainWrite.close();
	}
}