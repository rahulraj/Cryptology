import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.io.IOException;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileInputStream;
import java.io.BufferedInputStream;;

public class SolveRandomCipher 
{
	private String cipher;
	private String freqs;
	private char[][] keys;
	private ArrayList<String> words;
	private int sampleStart;
	private int sampleSize;
	private LetterCounterRan counter;
	
	public static final int DEF_SAMPLE_SIZE = 20;
	
	public SolveRandomCipher(String input)
	{
		cipher = input.toUpperCase();
		freqs = "";
		
		keys = new char[2][26];
		// 2 rows, 26 columns
		// upper row is the cipher letters
		// lower is their plain equivalents
		
		for (int c = 0; c < keys[0].length; c++)
		{
			keys[0][c] = (char)(c + 65);
			keys[1][c] = '?';
		}
		
		words = new ArrayList<String>();
		StringTokenizer tok = new StringTokenizer(cipher);
		while (tok.hasMoreTokens())
		{
			words.add(tok.nextToken());
		}
		sampleSize = DEF_SAMPLE_SIZE;
		sampleStart = 0;
		counter = new LetterCounterRan(cipher);
	}
	
	public void analyze() throws IOException
	{
		Scanner kbd = new Scanner(System.in);
		String input;
		freqs = counter.getFrequencies();
		
		System.out.println("Enter name of file to print frequency data.");
		String filePath = kbd.nextLine();
		
		File anOutFile = new File(filePath);
		PrintWriter freqOut = new PrintWriter(anOutFile);
		freqOut.println(freqs);
		freqOut.close();
		
		System.out.println("Frequencies of letters are outputted to the file.");
		
		// computer solving stage
		solveFreqs();
		
		// user input stage
		while (true)
		{
			giveInstructions();
			
			input = kbd.next();
			int choice;
			
			try
			{
				choice = Integer.parseInt(input);
			} catch (NumberFormatException e)
			{
				choice = -1; // will be handled in the switch statement
			}

			switch(choice)
			{
			case 1:
				System.out.println("Enter the ciphertext letter(s), followed by a space, then their plaintext equivalents.");
				String ciph = kbd.next().toUpperCase();
				String pla = kbd.next().toUpperCase();
				if (ciph.length() == pla.length())
				{
					for (int index = 0; index < ciph.length(); index++)
					{
						char aChar = ciph.charAt(index);
						keys[1][aChar - 65] = pla.charAt(index);
					}
				}
				else
				{
					System.out.println("Invalid input! Strings not the same length!");
				}
				break;
			case 2:
				System.out.println("<Ciphertext letter> = <Plaintext letter>");
				for (int col = 0; col < keys[0].length; col++)
				{
					System.out.println(keys[0][col] + " = " + keys[1][col]);
				}
				break;
			case 3:
				System.out.println("Which ciphertext letter do you want to make uncertain?");
				input = kbd.next().toUpperCase();
				char let = input.charAt(0);
				keys[1][let - 65] = '?';
				break;
			case 4:
				System.out.println(freqs);
				break;
			case 5:
				System.out.println("Enter the number of the word to start at (1st word is word 0).");
				int st = kbd.nextInt();
				if (st < 0 || st >= words.size()) // invalid input
				{
					System.out.println("Not possible. (There are " + words.size() + " words.");
					st = sampleStart;
				}
				sampleStart = st;
				if (sampleStart > words.size()) // avoid outofbounds exception
				{
					sampleStart = 0;
				}
				break;
			case 6:
				System.out.println("Give size of the sample in number of words.");
				System.out.println("Alternatively, type \"ALL\" to make the sample the entire text input.");
				input = kbd.next().toUpperCase();
				if (input.equalsIgnoreCase("ALL"))
				{
					sampleSize = words.size();
				}
				else
				{
					try
					{
						int in = Integer.parseInt(input);
						if (in < words.size())
						{
							sampleSize = in;
						}
						else
						{
							System.out.println("Number too large!");
						}
					} 
					catch (NumberFormatException e)
					{
						System.out.println("Invalid input!");
					} 
				}
				break;
			case 7:
				kbd.nextLine();
				System.out.println("Enter name of file to print deciphered text.");
				String pathToFile = kbd.nextLine();
				File outFile = new File(pathToFile);
				PrintWriter dataOut = new PrintWriter(outFile);

				StringBuilder output = new StringBuilder("");
				for (int index = 0; index < cipher.length(); index++)
				{
					char current = cipher.charAt(index);
					if (current >= 65 && current <= 90)
					{
						output.append(keys[1][current - 65]);
					}
					else
					{
						output.append(current);
					}
				}
				dataOut.println(output);
				dataOut.close();
				System.out.println("Entire decoded message printed to file.");
				break;
			case 8:
				System.out.println(getSolvingData());
				break;
			case 9:
				System.exit(0);
				break;
			default:
				System.out.println("Please enter a valid number choice.");
				break;
			}
		}
	}
	
	private void giveInstructions()
	{
		System.out.println("A sample of the text is:");
		System.out.println(getSample());
		System.out.println("Your options are (input the number):");
		System.out.println("1. Input possible plaintext/ciphertext pair.");
		System.out.println("2. See the current keys of letter pairs being used.");
		System.out.println("3. Undo a certain key set, making the cipher letter unknown again.");
		System.out.println("4. See the frequency counts.");
		System.out.println("5. Get a different text sample.");
		System.out.println("6. Change size of the sample text.");
		System.out.println("7. Decode the entire input text, outputting the answer to a file.");
		System.out.println("8. Get data explaining how much of the text has been solved.");
		System.out.println("9. Terminate the program.");
	}
	
	private void solveFreqs()
	{
		/**
		 *  computer solving stage
		 *	the computer will attempt to use frequency rules to solve certain letters
		 *	the rules are based off the keys gained from the frequency counts of Hamlet, Macbeth,
	 	 *	A Midsummer Night's Dream, and The Tempest
		 *	IMPORTANT: this code may need modification for input with large differences from the key! 
		 *	If all tests go correctly, 96.86% of letters will be solved (21 distinct letters are identified)
		 */
		
		solveE();
		solveT();
		solveO();
		solveA();
		solveL();
		solveS();
		solveH();
		solveN();
		solveD();
		
		/**
		 * Solve for letters based on the knowledge of other letters
		 */
		
		solveR();
		solveU();
		solveI();
		solveY();
		solveG();
		solveQ();
		solveM();
		solveFAndW();
		solveC();
		solveVAndB();
	}
	
	private void solveE()
	{
		/*
		 * Attempt to identify E's cipher letter
		 * It is the most common letter
		 * The 2nd most common repeat
		 * The 2nd letter in "HE" (the 2nd most common digraph)
		 * The 1st letter in "ER" (another common digraph) this has been the 3rd, 4th, and 5th most in the samples
		 * (ER clue has not yet been implemented)
		 * And the 3rd letter in "THE" (the most common trigraph)
		 */
		char[] poss = new char[4]; //possibilities for the cipher letter
		poss[0] = counter.getLetterOfFrequency(1);
		poss[1] = counter.getRepeatOfFrequency(2).getLetters().charAt(0);
		poss[2] = counter.getPairOfFrequency(2).getLetters().charAt(1);
		poss[3] = counter.getTrigraphOfFrequency(1).getThird();
		char cip = getMostCommon(poss);
		if (cip != '?')
		{
			keys[1][cip - 65] = 'E';
		}
	}
	
	private void solveT()
	{
		/*
		 * Attempt to identify T's cipher
		 * 2nd most common letter
		 * 1st letter in "TH" (the most common digraph)
		 * 1st letter in "THE" (the most common trigraph)
		 */
		char[] poss = new char[3];
		poss[0] = counter.getLetterOfFrequency(2);
		poss[1] = counter.getPairOfFrequency(1).getLetters().charAt(0);
		poss[2] = counter.getTrigraphOfFrequency(1).getFirst();
		char cip = getMostCommon(poss);
		if (cip != '?' && keys[1][cip - 65] == '?') 
		{
			// if conditions are
			// necessary to ensure that cip is definite and avoid "collisions" with other determined keys
			keys[1][cip - 65] = 'T';
		}
	}
	
	private void solveO()
	{
		/*
		 * Attempt to ID O's cipher
		 * Implemented:
		 * 3rd most common letter
		 * 3rd most common repeat
		 * 1st letter of "OU" which could be the 4th - 6th most common digraph
		 * 
		 * Not implemented yet:
		 * 2nd letter in "YOU" (3rd-5th most common trigraph)
		 */
		char [] poss = new char[5];
		poss[0] = counter.getLetterOfFrequency(3);
		poss[1] = counter.getRepeatOfFrequency(3).getLetters().charAt(0);
		poss[2] = counter.getPairOfFrequency(4).getLetters().charAt(0);
		poss[3] = counter.getPairOfFrequency(5).getLetters().charAt(0);
		poss[4] = counter.getPairOfFrequency(6).getLetters().charAt(0);
		
		char cip = getMostCommon(poss);
		if (cip != '?' && keys[1][cip - 65] == '?') 
		{
			keys[1][cip - 65] = 'O';
		}
	}
	
	private void solveA()
	{
		/*
		 * Attempt to ID A's cipher
		 * Implemented:
		 * 4th most common letter
		 * 1st letter in "AND" (2nd most common trigraph)
		 * 1st letter in "AN" (3rd or 4th most common digraph)
		 */
		char[] poss = new char[4];
		poss[0] = counter.getLetterOfFrequency(4);
		poss[1] = counter.getTrigraphOfFrequency(2).getFirst();
		poss[2] = counter.getPairOfFrequency(3).getLetters().charAt(0);
		poss[3] = counter.getPairOfFrequency(4).getLetters().charAt(0);
		
		char cip = getMostCommon(poss);
		if (cip != '?' && keys[1][cip - 65] == '?') 
		{
			keys[1][cip - 65] = 'A';
		}
	}
	
	private void solveL()
	{
		/*
		 * ID L's cipher
		 * Implemented:
		 * 1st most common repeat
		 * 10th or 11th most common letter
		 */
		char[] poss = new char[3];
		poss[0] = counter.getRepeatOfFrequency(1).getLetters().charAt(0);
		poss[1] = counter.getLetterOfFrequency(10);
		poss[2] = counter.getLetterOfFrequency(11);
		char cip = getMostCommon(poss);
		if (cip != '?' && keys[1][cip - 65] == '?') 
		{
			keys[1][cip - 65] = 'L';
		}
	}
	
	private void solveS()
	{
		/*
		 * ID S's cipher
		 * Implemented:
		 * 4th most common repeat
		 * 6th or 7th most common letter
		 */
		char[] poss = new char[3];
		poss[0] = counter.getRepeatOfFrequency(4).getLetters().charAt(0);
		poss[1] = counter.getLetterOfFrequency(6);
		poss[2] = counter.getLetterOfFrequency(7);
		char cip = getMostCommon(poss);
		if (cip != '?' && keys[1][cip - 65] == '?') 
		{
			keys[1][cip - 65] = 'S';
		}
	}
	
	private void solveH()
	{
		/*
		 * ID H's cipher
		 * Implemented:
		 * 2nd letter in "THE" (most common trigraph)
		 */
		char cip = counter.getTrigraphOfFrequency(1).getSecond();
		if (cip != '?' && keys[1][cip - 65] == '?') 
		{
			keys[1][cip - 65] = 'H';
		}
	}
	
	private void solveN()
	{
		/*
		 * ID N's cipher
		 * Implemented:
		 * 2nd letter in "AND" (2nd most common trigraph)
		 */
		char cip = counter.getTrigraphOfFrequency(2).getSecond();
		if (cip != '?' && keys[1][cip - 65] == '?') 
		{
			keys[1][cip - 65] = 'N';
		}
	}
	
	private void solveD()
	{
		/*
		 * ID D's cipher
		 * Implemented:
		 * 3rd letter in "AND" (2nd most common trigraph)
		 */
		char cip = counter.getTrigraphOfFrequency(2).getThird();
		if (cip != '?' && keys[1][cip - 65] == '?') 
		{
			keys[1][cip - 65] = 'D';
		}
	}
	
	private void solveR()
	{
		/*
		 * ID R's cipher
		 * It is the 2nd letter in "ER", the most common digraph that starts with E
		 * It is significantly more common than "EN" or "ES"
		 */
		int letterLoc = -1;
		// find E in keys
		for (int pos = 0; pos < keys[1].length; pos++)
		{
			if (keys[1][pos] == 'E')
			{
				letterLoc = pos;
				pos = keys[1].length; // break out
			}
		}
		
		if (letterLoc != -1) // E found
		{
			for (int freq = 1; freq < 20; freq++)
			{
				LetterPairRan pair = counter.getPairOfFrequency(freq);
				if (pair.getLetters().charAt(0) == keys[0][letterLoc])
				{
					keys[1][pair.getLetters().charAt(1) - 65] = 'R';
					freq = 20; // break out
				}
			}
		}
	}
	
	private void solveU()
	{
		/*
		 * ID U's cipher
		 * It is the second letter in "OU", the most common digraph starting with O
		 * Significantly more common than "OR" or "ON"
		 */
		int letterLoc = -1;
		// find O in keys
		for (int pos = 0; pos < keys[1].length; pos++)
		{
			if (keys[1][pos] == 'O')
			{
				letterLoc = pos;
				pos = keys[1].length; // break out
			}
		}
		if (letterLoc != -1) // O found
		{
			for (int freq = 1; freq < 20; freq++)
			{
				LetterPairRan pair = counter.getPairOfFrequency(freq);
				if (pair.getLetters().charAt(0) == keys[0][letterLoc])
				{
					keys[1][pair.getLetters().charAt(1) - 65] = 'U';
					freq = 20; // break out
				}
			}
		}
	}
	
	private void solveI()
	{
		/*
		 * ID I's cipher
		 * It is the first letter in "IN", the second most common digraph ending with N
		 * Significantly lower than "AN", yet higher that "EN"
		 */
		int letterLoc = -1;
		int aLoc = -1;
		// find N in keys
		for (int pos = 0; pos < keys[1].length; pos++)
		{
			if (keys[1][pos] == 'N')
			{
				letterLoc = pos;
				pos = keys[1].length; // break out
			}
		}
		for (int pos = 0; pos < keys[1].length; pos++)
		{
			if (keys[1][pos] == 'A')
			{
				aLoc = pos;
				pos = keys[1].length; // break out
			}
		}
		
		if (letterLoc != -1 && aLoc != -1) // both letters found
		{
			for (int freq = 1; freq < 20; freq++)
			{
				LetterPairRan pair = counter.getPairOfFrequency(freq);
				if ( pair.getLetters().charAt(1) == keys[0][letterLoc] &&
					 pair.getLetters().charAt(0) != keys[0][aLoc] )
				{
					keys[1][pair.getLetters().charAt(0) - 65] = 'I';
					freq = 20; // break out
				}
			}
		}
	}
	
	private void solveY()
	{
		/*
		 * ID Y's cipher
		 * 1st letter of "YOU" (most common trigraph ending in "OU"
		 */
		int oLoc = -1;
		int uLoc = -1;
		
		// find O
		for (int pos = 0; pos < keys[1].length; pos++)
		{
			if (keys[1][pos] == 'O')
			{
				oLoc = pos;
				pos = keys[1].length; // break out
			}
		}
		
		// find U
		for (int pos = 0; pos < keys[1].length; pos++)
		{
			if (keys[1][pos] == 'U')
			{
				uLoc = pos;
				pos = keys[1].length; // break out
			}
		}
		
		if (oLoc != -1 && uLoc != -1) // both letters found
		{
			for (int freq = 1; freq < 10; freq++)
			{
				if (freq == 11)
				{
					System.out.println("BRKPT ICI");
				}
				TrigraphRan tri = counter.getTrigraphOfFrequency(freq);
				try
				{
					if (tri.getSecond() == keys[0][oLoc] &&
						tri.getThird() == keys[0][uLoc])
						{
							keys[1][tri.getFirst() - 65] = 'Y';
							freq = 20; // break out
						}
				} catch (NullPointerException e)
				{
					System.err.println("HERE! " + oLoc);
					System.err.println(keys[0][oLoc]);
					System.err.println(tri);
					System.err.println(freq);
					throw new IllegalStateException();
				}
				
			}
		}
	}
	
	private void solveG()
	{
		/*
		 * ID G's cipher
		 * 3rd letter in "ING", most common trigraph starting with "IN"
		 */
		int iLoc = -1;
		int nLoc = -1;
		
		// find O
		for (int pos = 0; pos < keys[1].length; pos++)
		{
			if (keys[1][pos] == 'I')
			{
				iLoc = pos;
				pos = keys[1].length; // break out
			}
		}
		
		// find U
		for (int pos = 0; pos < keys[1].length; pos++)
		{
			if (keys[1][pos] == 'N')
			{
				nLoc = pos;
				pos = keys[1].length; // break out
			}
		}
		
		if (iLoc != -1 && nLoc != -1) // both letters found
		{
			for (int freq = 1; freq < 10; freq++)
			{
				TrigraphRan tri = counter.getTrigraphOfFrequency(freq);
				if (tri.getFirst() == keys[0][iLoc] &&
					tri.getSecond() == keys[0][nLoc])
				{
					keys[1][tri.getThird() - 65] = 'G';
					freq = 20; // break out
				}
			}
		}
	}
	
	private void solveQ()
	{
		/*
		 * find Q
		 * Get digraphs with the letter in question as the first letter
		 * The most common one will have U as the second letter
		 * All other digraphs will not occur at all
		 * Test letters starting from the bottom of the frequency counts
		 */ 
		LetterPairRan[] dis;
		char cip;
		for (int rank = 26; rank >= 1; rank--)
		{
			cip = counter.getLetterOfFrequency(rank);
			dis = counter.getDigraphsWithLetter(cip, true);
			if (dis != null)
			{
				char second = dis[dis.length - 1].getLetters().charAt(1);
				if ((keys[1][second - 65] ==  'U') && (dis[dis.length - 2].getFrequency() == 0))
				{
					keys[1][cip - 65] = 'Q';
					rank = 0; // break out
				}
			}
		}
	}
	
	private void solveM()
	{
		/*
		 * Find M
		 * It is the first letter in "MY" one of the most common
		 * digraphs ending in Y.
		 * Other common digraphs ending in Y have first letters identified earlier
		 */
		char cip = '?';
		for (int loc = 0; loc < keys[1].length; loc++)
		{
			if (keys[1][loc] == 'Y')
			{
				cip = keys[0][loc];
				loc = keys[1].length; // break out
			}
		}
		
		LetterPairRan[] dis = counter.getDigraphsWithLetter(cip, false);
		if (dis != null)
		{
			for (int loc = dis.length - 1; loc >= 0; loc--)
			{
				char first = dis[loc].getLetters().charAt(0);
				if (keys[1][first - 65] == '?')
				{
					keys[1][first - 65] = 'M';
					loc = -1; // break out
				}
			}
		}
	}
	
	private void solveFAndW()
	{
		/*
		 * Find both F and W
		 * Check the digraphs starting with O
		 * "OF" and "OW" will be the two highest digraphs with unidentified letters
		 * To tell them apart, check the digraphs ending with O
		 * "FO" is significantly higher than "WO" 
		 */
		char cip = '?';
		for (int loc = 0; loc < keys[1].length; loc++)
		{
			if (keys[1][loc] == 'O')
			{
				cip = keys[0][loc];
				loc = keys[1].length; // break out
			}
		}
		
		LetterPairRan[] dis = counter.getDigraphsWithLetter(cip, true);
		if (dis == null)
		{
			return; // can't continue
		}
		char cip1 = '?';
		char cip2 = '?';
		int loc = dis.length - 1;
		while ((cip1 == '?') || (cip2 == '?'))
		{
			char sec = dis[loc].getLetters().charAt(1);
			if (keys[1][sec - 65] == '?')
			{
				// unknown letter found, should find two before breaking out
				if (cip1 != '?')
				{
					cip2 = sec;
				}
				else
				{
					cip1 = sec;
				}
			}
			loc--;
		}
		
		LetterPairRan pair1 = null;
		LetterPairRan pair2 = null; // these will become the ciphered "FO" and "WO"
		
		dis = counter.getDigraphsWithLetter(cip1, true);
		if (dis == null)
		{
			return;
		}
		// find the digraph that starts with cip1 and ends with O
		for (int pos = dis.length - 1; pos >= 0; pos--)
		{
			if (dis[pos].getLetters().charAt(1) == cip)
			{
				pair1 = dis[pos];
			}
		}
		
		dis = counter.getDigraphsWithLetter(cip2, true);
		if (dis == null)
		{
			return;
		}
		for (int pos = dis.length - 1; pos >= 0; pos--)
		{
			if (dis[pos].getLetters().charAt(1) == cip)
			{
				pair2 = dis[pos];
			}
		}
		
		if (pair1.getFrequency() > pair2.getFrequency())
		{
			// the first letter of pair1 is F and the first letter of pair2 is W
			keys[1][pair1.getLetters().charAt(0) - 65] = 'F';
			keys[1][pair2.getLetters().charAt(0) - 65] = 'W';
		}
		else
		{
			keys[1][pair1.getLetters().charAt(0) - 65] = 'W';
			keys[1][pair2.getLetters().charAt(0) - 65] = 'F';
		}
	}
	
	private void solveC()
	{
		/*
		 * Find C
		 * Search digraphs starting with "N"
		 * "NC" is one of the more common digraphs starting with N
		 * Other digraphs in the area are already found
		 * Also, "CO" is a common digraphs ending in O with other first letters identified.
		 */
		char[] poss = new char[2];
		char cip = '?';
		for (int pos = 0; pos < keys[1].length; pos++)
		{
			if (keys[1][pos] == 'N')
			{
				cip = keys[0][pos];
				pos = keys[1].length; // break out
			}
		}
		
		LetterPairRan[] dis = counter.getDigraphsWithLetter(cip, true);
		if (dis == null)
		{
			return;
		}
		for (int aPos = dis.length - 1; aPos >= 0; aPos--)
		{
			if (keys[1][dis[aPos].getLetters().charAt(1) - 65] == '?')
			{
				poss[0] = dis[aPos].getLetters().charAt(1);
				aPos = -1; // break out
			}
		}
		
		for (int pos = 0; pos < keys[1].length; pos++)
		{
			if (keys[1][pos] == 'O')
			{
				cip = keys[0][pos];
				pos = keys[1].length; // break out
			}
		}
		
		dis = counter.getDigraphsWithLetter(cip, false);
		if (dis == null)
		{
			return;
		}
		for (int aPos = dis.length - 1; aPos >= 0; aPos--)
		{
			if (keys[1][dis[aPos].getLetters().charAt(0) - 65] == '?')
			{
				poss[1] = dis[aPos].getLetters().charAt(0);
				aPos = -1; // break out
			}
		}
		
		cip = getMostCommon(poss);
		if (cip != '?')
		{
			keys[1][cip - 65] = 'C';
		}
	}
	
	private void solveVAndB()
	{
		/*
		 * Find V and B
		 * "VE" and "BE" are common digraphs ending in E, only unknowns in the range
		 * "VE" is higher than "BE", however, they are close in The Tempest
		 * So, use "AV" and "AB" ("AV" is higher) to separate them
		 */
		char cip = '?';
		
		for (int pos = 0; pos < keys[1].length; pos++)
		{
			if (keys[1][pos] == 'E')
			{
				cip = keys[0][pos];
				pos = keys[1].length; // break out
			}
		}
		
		LetterPairRan[] dis = counter.getDigraphsWithLetter(cip, false);
		if (dis == null)
		{
			return;
		}
		char cip1 = '?';
		char cip2 = '?';
		int loc = dis.length - 1;
		while ((cip1 == '?') || (cip2 == '?'))
		{
			char sec = dis[loc].getLetters().charAt(0);
			if (keys[1][sec - 65] == '?')
			{
				// unknown letter found, should find two before breaking out
				if (cip1 != '?')
				{
					cip2 = sec;
				}
				else
				{
					cip1 = sec;
				}
			}
			loc--;
		}
		
		LetterPairRan pair1 = null;
		LetterPairRan pair2 = null; // these will become the ciphered "AV" and "AB"
		
		for (int pos = 0; pos < keys[1].length; pos++)
		{
			if (keys[1][pos] == 'A')
			{
				cip = keys[0][pos];
				loc = keys[1].length; // break out
			}
		}
		
		dis = counter.getDigraphsWithLetter(cip1, false);
		if (dis == null)
		{
			return;
		}
		// find the digraph that starts with cip (A's cipher) and ends with cip1
		for (int pos = dis.length - 1; pos >= 0; pos--)
		{
			if (dis[pos].getLetters().charAt(0) == cip)
			{
				pair1 = dis[pos];
				pos = -1; // break out
			}
		}
		
		dis = counter.getDigraphsWithLetter(cip2, false);
		if (dis == null)
		{
			return;
		}
		for (int pos = dis.length - 1; pos >= 0; pos--)
		{
			if (dis[pos].getLetters().charAt(0) == cip)
			{
				pair2 = dis[pos];
				pos = -1; // break out
			}
		}
		
		if (pair1.getFrequency() > pair2.getFrequency())
		{
			// the second letter of pair1 is V and the second letter of pair2 is B
			keys[1][pair1.getLetters().charAt(1) - 65] = 'V';
			keys[1][pair2.getLetters().charAt(1) - 65] = 'B';
		}
		else
		{
			keys[1][pair1.getLetters().charAt(1) - 65] = 'V';
			keys[1][pair2.getLetters().charAt(1) - 65] = 'B';
		}
	}
	private char getMostCommon(char[] chars)
	{
		// get most common char in the array
		
		int[] charFreqs = new int[chars.length]; // parallel array to chars
		
		// populate charFreqs
		for (int loc = 0; loc < charFreqs.length; loc++)
		{
			char current = chars[loc];
			for (char ch : chars)
			{
				if (current == ch)
				{
					charFreqs[loc]++;
				}
			}
		}
		
		// find most common letter
		int highFreq = 0;
		int highLoc = 0;
		for (int loc = 0; loc < charFreqs.length; loc++)
		{
			if (charFreqs[loc] > highFreq)
			{
				highFreq = charFreqs[loc];
				highLoc = loc;
			}
		}
		
		// make sure there are no ties
		for (int loc = 0; loc < charFreqs.length; loc++)
		{
			if (charFreqs[loc] == highFreq && chars[loc] != chars[highLoc])
			{
				// there is a tie with two or more characters in the array!
				// so multiple characters fulfilled the same number of conditions 
				// and there is not plurality
				return '?'; // still indefinite
			}
		}
		
		return chars[highLoc];
	}
	
	private String getSample()
	{
		StringBuilder plSample = new StringBuilder("");
		StringBuilder cipSample = new StringBuilder("");
		StringBuilder output = new StringBuilder("Cipher text sample: \n");
		if ((sampleStart + sampleSize) < words.size())
		{
			for (int loc = sampleStart; loc < (sampleStart + sampleSize); loc++)
			{
				cipSample.append(words.get(loc) + " ");
			}
		}
		else //just add the entire message
		{
			for (String st : words)
			{
				cipSample.append(st + " ");
			}
		}
		cipSample = new StringBuilder(cipSample.toString().trim());
		output.append(cipSample + "\n");
		output.append("Possible plaintext: \n");
		for (int index = 0; index < cipSample.length(); index++)
		{
			if (cipSample.charAt(index) >= 65 && cipSample.charAt(index) <= 90)
			{
				char current = cipSample.charAt(index);
				plSample.append(keys[1][current - 65]); 
			}
			else
			{
				plSample.append(cipSample.charAt(index));
			}
		}
		output.append(plSample);
		return output.toString();
	}
	
	private String getSolvingData()
	{
		int knownCount = 0;
		int totalCount = 0;
		for (int index = 0; index < cipher.length(); index++)
		{
			if (cipher.charAt(index) >= 65 && cipher.charAt(index) <= 90)
			{
				totalCount++;
				if (keys[1][cipher.charAt(index) - 65] != '?')
				{
					knownCount++;
				}
			}
		}
		double percent = (double)(knownCount) / totalCount * 100;
		int identifiedNum = 0;
		for (int loc = 0; loc < keys[1].length; loc++)
		{
			if (keys[1][loc] != '?')
			{
				identifiedNum++;
			}
		}
		
		return "Number of letters identified: " + identifiedNum + "\n" +
			   "Amount of letters solved: " + knownCount + "\n" +
			   "Total amount of letters: " + totalCount + "\n" + 
			   "Percentage of letters solved: "  + percent + "\n";
	}
	
	public static void main(String[] args) throws IOException
	{
		Scanner in = new Scanner(System.in);
		System.out.println("Enter name of file with enciphered input.");
		String pathFromFile = in.nextLine();
		File inFile = new File(pathFromFile);
		FileInputStream fis = new FileInputStream(inFile);
		BufferedInputStream bis = new BufferedInputStream(fis);
		
		StringBuilder message = new StringBuilder("");
		
		while (bis.available() > 0)
		{
			message.append((char)bis.read());
		}
		
		fis.close();
		bis.close();
		
		SolveRandomCipher ran = new SolveRandomCipher(message.toString());
		ran.analyze();
	}
}