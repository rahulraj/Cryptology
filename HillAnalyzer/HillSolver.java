import java.util.Set;
import java.util.TreeSet;
import java.util.Scanner;
import java.io.PrintWriter;

public class HillSolver 
{
	private String cipher;
	private LetterCounter count;
	private PrintWriter dataOut;
	
	// the two most common digraphs in the text samples used primarily
	// different digraphs may be used depending on the text
	public static final String MOST_COMMON_DIGRAPH = "TH";
	public static final String SECOND_MOST_COMMON = "HE";
	
	public HillSolver(String cip, PrintWriter out)
	{
		cipher = cip.toUpperCase();
		count = new LetterCounter(cipher);
		count.getFrequencies(); // causes it to count the letters
		dataOut = out;
	}
	
	public void solveTwoByTwo()
	{
		// get the two most common digraphs in the ciphertext
		String cipDigraph1 = count.getPairOfFrequency(1).digraphAsString();
		String cipDigraph2 = count.getPairOfFrequency(2).digraphAsString();
		
		String plDigraph1 = MOST_COMMON_DIGRAPH;
		String plDigraph2 = SECOND_MOST_COMMON;
		// assign different values to these two Strings if the text has different frequency data
		
		/*
		 * * Let the following be true:
		 *  <first char of plDigraph1> = P1
		 *  < 2nd  char of plDigraph1> = P2
		 *  <first char of plDigraph2> = P3
		 *  < 2nd  char of plDigraph2> = P4
		 * 
		 * <first char of cipDigraph1> = C1
		 * < 2nd  char of cipDigraph1> = C2
		 * <first char of cipDigraph2> = C3
		 * < 2nd  char of cipDigraph2> = C4
		 * 
		 * There exists a system of equations as follows
		 * [a b] X [P1] = [C1]  
		 * [c d]   [P2]   [C2] 
		 * 
		 * [a b] X [P3] = [C3]  
		 * [c d]   [P4]   [C4] 
		 * 
		 * a, b, c, and d are numbers between 1 and 26 (inclusive), are the unknowns,
		 * And are the elements of the enciphering matrix
		 * Both of these equations are mod 26
		 * 
		 * These matrix multiplications resolve to the following system of equations
		 * 1. (a)(P1) + (b)(P2) = C1 (mod 26) 
		 * 2. (a)(P3) + (b)(P4) = C3 (mod 26)
		 * 
		 * 3. (c)(P1) + (d)(P2) = C2 (mod 26)
		 * 4. (c)(P3) + (d)(P4) = C4 (mod 26)
		 * 
		 * We can consider this to be two separate systems of two equations each
		 * Note that there will be multiple solutions because of the modulus
		 * The equations will be solved using the following strategies:
		 * Find all possible solutions for equation 1, then all possible solutions for equation 2
		 * Then find the union of these two sets
		 * Repeat the process for equations 3 and 4
		 * Construct all the possible enciphering matrices from these solutions
		 * Throw out any noninversible ones
		 * Then invert the remaining ones and output the multiple plaintexts
		 * The user can determine which one is correct through knowledge of the English language
		 */
		
		int p1 = plDigraph1.charAt(0) - 64;
		int p2 = plDigraph1.charAt(1) - 64;
		int p3 = plDigraph2.charAt(0) - 64;
		int p4 = plDigraph2.charAt(1) - 64;
		
		int c1 = cipDigraph1.charAt(0) - 64;
		int c2 = cipDigraph1.charAt(1) - 64;
		int c3 = cipDigraph2.charAt(0) - 64;
		int c4 = cipDigraph2.charAt(1) - 64;
		
		// find all possible solutions for equation 1
		// It is necessary to "wrap" the int[] in MatrixRow classes to provide a compareTo method
		// and avoid ClassCastExceptions in the TreeSet's add method
		TreeSet<MatrixRow> eq1 = new TreeSet<MatrixRow>();
		for (int count1 = 1; count1 <= 26; count1++)
			for (int count2 = 1; count2 <= 26; count2++)
			{
				int leftSide = count1 * p1 + count2 * p2;
				// convert to mod 26
				leftSide %= 26;
				if (leftSide == 0)
				{
					leftSide = 26;
				}
				
				if (leftSide == c1)
				{
					int[] anAnswer = new int[2];
					anAnswer[0] = count1;
					anAnswer[1] = count2;
					eq1.add(new MatrixRow(anAnswer));
				}
			}
		
		// find all possible solutions for equation 2
		Set<MatrixRow> eq2 = new TreeSet<MatrixRow>();
		for (int count1 = 1; count1 <= 26; count1++)
			for (int count2 = 1; count2 <= 26; count2++)
			{
				int leftSide = count1 * p3 + count2 * p4;
				// convert to mod 26
				leftSide %= 26;
				if (leftSide == 0)
				{
					leftSide = 26;
				}
				
				if (leftSide == c3)
				{
					int[] anAnswer = new int[2];
					anAnswer[0] = count1;
					anAnswer[1] = count2;
					eq2.add(new MatrixRow(anAnswer));
				}
			}
		
		// find all possible solutions for equation 3
		Set<MatrixRow> eq3 = new TreeSet<MatrixRow>();
		for (int count1 = 1; count1 <= 26; count1++)
			for (int count2 = 1; count2 <= 26; count2++)
			{
				int leftSide = count1 * p1 + count2 * p2;
				// convert to mod 26
				leftSide %= 26;
				if (leftSide == 0)
				{
					leftSide = 26;
				}
				
				if (leftSide == c2)
				{
					int[] anAnswer = new int[2];
					anAnswer[0] = count1;
					anAnswer[1] = count2;
					eq3.add(new MatrixRow(anAnswer));
				}
			}
		
		// find all possible solutions for equation 4
		Set<MatrixRow> eq4 = new TreeSet<MatrixRow>();
		for (int count1 = 1; count1 <= 26; count1++)
			for (int count2 = 1; count2 <= 26; count2++)
			{
				int leftSide = count1 * p3 + count2 * p4;
				// convert to mod 26
				leftSide %= 26;
				if (leftSide == 0)
				{
					leftSide = 26;
				}
				
				if (leftSide == c4)
				{
					int[] anAnswer = new int[2];
					anAnswer[0] = count1;
					anAnswer[1] = count2;
					eq4.add(new MatrixRow(anAnswer));
				}
			}
		
		TreeSet<MatrixRow> aAndB = union(eq1, eq2);
		TreeSet<MatrixRow> cAndD = union(eq3, eq4);
		
		TreeSet<Matrix> possEncMatrices = new TreeSet<Matrix>();
		// Note: The Matrix class is 2X2 and will have to be extended to incorporate larger sizes!
		for (MatrixRow top : aAndB)
			for (MatrixRow bottom : cAndD)
			{
				int[][] encMat = new int[2][2];
				int[] topMat = top.getNumbers();
				int[] botMat = bottom.getNumbers();
				encMat[0][0] = topMat[0];
				encMat[0][1] = topMat[1];
				encMat[1][0] = botMat[0];
				encMat[1][1] = botMat[1];
				if (isInvertible(encMat))
				{
					possEncMatrices.add(new Matrix(encMat));
				}
			}
		
		userAssistedDecipher(possEncMatrices);
	}
	
	private void userAssistedDecipher(TreeSet<Matrix> possEncMatrices)
	{
		final int WORDS_TO_OUT = 10;
		// decipher the text with all possible matrices
		// output the first WORDS_TO_OUT 5-letter words for each possibility and let the user decide which is right
		// completely decipher using that one
		
		// take the first WORDS_TO_OUT "words" (or the entire text if less than that size)
		String[] cipWords = cipher.split(" ");
		StringBuilder toOut = new StringBuilder("");
		if (cipWords.length > WORDS_TO_OUT)
		{
			for (int loc = 0; loc < WORDS_TO_OUT; loc++)
			{
				toOut.append(cipWords[loc] + " ");
			}
		}
		else
		{
			for (String word : cipWords)
			{
				toOut.append(word + " ");
			}
		}
		
		for (Matrix possEncMat : possEncMatrices)
		{
			System.out.print("Enciphering key: ");
			int[][] pEncMat = possEncMat.getMatrix();
			for (int row = 0; row < pEncMat.length; row++)
				for (int col = 0; col < pEncMat[0].length; col++)
				{
					System.out.print((char)(pEncMat[row][col] + 64)); 
				}
			System.out.println(); // go to next line
			
			int[][] pDecMat = invertMatrix(pEncMat);
			System.out.print("Deciphering key: ");
			for (int row = 0; row < pDecMat.length; row++)
				for (int col = 0; col < pDecMat[0].length; col++)
				{
					System.out.print(pDecMat[row][col] + " ");
					// possible revision:
					// the numbers here may be long decimals
					// implement a NumberFormat to make the output look better
				}
			System.out.println();
			
			System.out.println("Deciphered section of the text:");
			System.out.println(decodeText(toOut.toString(), pDecMat));
		}
		
		// read user input as to which is the best
		Scanner kbd = new Scanner(System.in);
		System.out.println("Enter the enciphering matrix that you think is correct.");
		System.out.println("Use the letter format outputted earler.");
		
		String input;
		Matrix encKey = null;
		String key = "";
		
		while (encKey == null)
		{
			input = kbd.next();
			for (Matrix theKey : possEncMatrices)
			{
				int[][] aKey = theKey.getMatrix();
				for (int row = 0; row < aKey.length; row++)
					for (int col = 0; col < aKey[0].length; col++)
					{
						key += (char)(aKey[row][col] + 64); // I'm pretty sure it's 64, but might be a bug
					}
				if (input.equals(key))
				{
					encKey = theKey;
					break; // key found, escape foreach loop
				}
			}
			if (encKey == null)
			{
				System.out.println("Invalid input, please enter another.");
			}
		}
		
		int[][] decKey = invertMatrix(encKey.getMatrix());
		String thePlaintext = decodeText(cipher, decKey);
		dataOut.println(thePlaintext);
		System.out.println("Plaintext printed to file.");
		dataOut.close();
	}
	
	private String decodeText(String toDec, int[][] decMatrix)
	{
		StringBuilder plain = new StringBuilder("");
		
		// process a ciphertext with all non-letters deleted
		StringBuilder ciphertext = new StringBuilder("");
		for (int index = 0; index < toDec.length(); index++)
		{
			if (toDec.charAt(index) >= 65 && toDec.charAt(index) <= 90)
			{
				ciphertext.append(toDec.charAt(index));
			}
		}
		
		// add X's to the end if necessary to make it compatible with the size of the matrix
		while ((ciphertext.length() % decMatrix.length) != 0)
		{
			ciphertext.append('X');
		}
		
		int startIndex = 0;
		int endIndex = startIndex + decMatrix.length;
		String pieceToDec = "";
		int[] matrixToDec = new int[decMatrix.length];
		int[] plainPiece;
		while (endIndex <= ciphertext.length())
		{
			pieceToDec = ciphertext.substring(startIndex, endIndex);
			for (int ind = 0; ind < pieceToDec.length(); ind++)
			{
				matrixToDec[ind] = pieceToDec.charAt(ind) - 64;
			}
			plainPiece = multiplyMatrix(decMatrix, matrixToDec);
			for (int loc = 0; loc < plainPiece.length; loc++)
			{
				plain.append((char)(plainPiece[loc] + 64));
			}
			startIndex = endIndex;
			endIndex += decMatrix.length;
		}
		return group(plain.toString());
	}
	
	private int[] multiplyMatrix(int[][] decMat, int[] toMult)
	{
		// toMult.length must = encMatrix.length
		// to encipher the text, the operation decMatrix * toMult is done
		// note the order as matrix multiplication is NOT communitative
		// in fact, it is not possible to perform toMult * encMatrix (due to their dimensions) 
		int[] output = new int[toMult.length];
		double total = 0;
		for (int row = 0; row < decMat.length; row++)
		{
			for (int col = 0; col < decMat[0].length; col++)
			{
				total += decMat[row][col] * toMult[col];
			}
			output[row] = (int)(Math.round(total));
			total = 0;
		}
		
		// convert output to mod 26
		for (int loc = 0; loc < output.length; loc++)
		{
			while (output[loc] > 26)
			{
				output[loc] -= 26;
				// can not use Java's % modulus operator as it converts 26's to 0's
			}
		}
		return output;
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
	
	private TreeSet<MatrixRow> union(Set<MatrixRow> s1, Set<MatrixRow> s2)
	{
		TreeSet<MatrixRow> union = new TreeSet<MatrixRow>();
		for (MatrixRow element : s1)
		{
			if (s2.contains(element))
			{
				union.add(element);
			}
		}
		for (MatrixRow elem : s2)
		{
			if (s1.contains(elem))
			{
				union.add(elem);
			}
		}
		return union;
	}
	
	private boolean isInvertible(int[][] toInvert)
	{
		// checks to see if the matrix is invertible mod 26
		if (toInvert.length != toInvert[0].length)
		{
			return false; // matrix not square
		}
		if (toInvert.length > 2)
		{
			throw new IllegalStateException("Currently not equipped to handle larger than 2X2 matrices!");
		}
		
		int determinant = toInvert[0][0] * toInvert[1][1] - toInvert[0][1] * toInvert[1][0];
		if (determinant > 0)
		{
			determinant %= 26;
		}
		else
		{
			while (determinant < 0)
			{
				determinant += 26; // the modulus operator does not behave as intended when given negative numbers 
			}
		}
		if (determinant == 0 || determinant == 13 || (determinant % 2) == 0)
		{
			return false;
		}
		return true;
	}
	
	private int[][] invertMatrix(int[][] toInvert)
	{
		if (toInvert.length != toInvert[0].length)
		{
			throw new IllegalStateException("Matrix not square!");
		}
		if (toInvert.length > 2)
		{
			throw new IllegalStateException("Currently not equipped to handle larger than 2X2 matrices!");
			// this may change in a later stage of development
		}
		
		int[][] inverse = new int[toInvert.length][toInvert[0].length];
		
		// uses the determinant in calculations to determine the inverse
		// to do this:
		// 1. Interchange the top-left number with the bottom-right one
		// 2. Multiply the top-right and the bottom-left numbers by -1
		// 3. Divide all four numbers by the determinant (defined as ad - bc)
		// (All operations done mod 26)
		// For more details, see Lewand's Cryptological Mathematics, page 115
		int determinant = toInvert[0][0] * toInvert[1][1] - toInvert[0][1] * toInvert[1][0];
		// the determinant must be inverted mod 26
		if (determinant > 0)
		{
			determinant %= 26;
			if (determinant == 0)
			{
				determinant = 26; // this statement is a convention only
				// it should never happen because 26 is not invertible mod 26
				// and so toInvert should have been thrown out earlier
			}
		}
		else
		{
			while (determinant < 0)
			{
				determinant += 26;
			}
		}
		
		int detInverse = 0;
		switch(determinant)
		{
		// find the inverses that cause the coefficient of x to equal 1
		case 1:
			detInverse = 1;
			break;
		case 3:
			detInverse = 9;
			break;
		case 5:
			detInverse = 21;
			break;
		case 7:
			detInverse = 15;
			break;
		case 9:
			detInverse = 3;
			break;
		case 11:
			detInverse = 19;
			break;
		case 15:
			detInverse = 7;
			break;
		case 17:
			detInverse = 23;
			break;
		case 19:
			detInverse = 11;
			break;
		case 25:
			detInverse = 25;
			break;
		default: // numbers not relatively prime with 26
			System.err.println("Determinant is: " + determinant);
			throw new IllegalStateException("Determinant not inversible!");
			// this should never happen
		
			// it is conventional to have a break statement here
			// but in this case it is unreachable code that will not compile
		}
		
		inverse[0][0] = (toInvert[1][1]) * (detInverse);
		inverse[0][1] = (toInvert[0][1] * - 1) * (detInverse);
		inverse[1][0] = (toInvert[1][0] * - 1) * (detInverse);
		inverse[1][1] = (toInvert[0][0]) * (detInverse);
		// make these values mod 26
		for (int row = 0; row < inverse.length; row++)
			for (int col = 0; col < inverse[0].length; col++)
			{
				if (inverse[row][col] > 0)
				{
					inverse[row][col] %= 26;
					if (inverse[row][col] == 0)
					{
						inverse[row][col] = 26;
					}
				}
				else
				{
					while (inverse[row][col] < 0)
					{
						inverse[row][col] += 26;
					}
				}
			}
		return inverse;
	}
}