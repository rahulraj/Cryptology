
public class HillEncipherer 
{
	private int[][] encMatrix;
	private String plain;
	
	public HillEncipherer(String key, String pl)
	{
		// convert key to a matrix
		double theMatSize = (Math.sqrt(key.length()));
		if (theMatSize != (int)(theMatSize))
		{
			System.err.println("Key size is " + key.length());
			System.err.println("But its square root is: " + theMatSize);
			throw new IllegalStateException("Can not construct enciphering matrix, not a perfect square!");
		}
		int matSize = (int)(theMatSize);
		encMatrix = new int[matSize][matSize];
		String upKey = key.toUpperCase();
		for (int row = 0; row < encMatrix.length; row++)
		{
			for (int col = 0; col < encMatrix[0].length; col++)
			{
				int loc = row * encMatrix.length + col;
				System.out.print("character at " + loc + " ");
				char letter = upKey.charAt((row * encMatrix.length) + col);
				int letterMod26 = letter - 64;
				System.out.println("added to the matrix at row=" + row + " col=" + col);
				encMatrix[row][col] = letterMod26;
			}
		}
		plain = pl.toUpperCase();
	}
	
	public HillEncipherer(int[][] key, String pl)
	{
		encMatrix = key;
		plain = pl;
	}
	
	public String encipher()
	{
		StringBuilder cipher = new StringBuilder("");
		
		// process a plaintext with all non-letters deleted
		StringBuilder plaintext = new StringBuilder("");
		for (int index = 0; index < plain.length(); index++)
		{
			if (plain.charAt(index) >= 65 && plain.charAt(index) <= 90)
			{
				plaintext.append(plain.charAt(index));
			}
		}
		
		// add X's to the end if necessary to make it compatible with the size of the matrix
		while ((plaintext.length() % encMatrix.length) != 0)
		{
			plaintext.append('X');
		}
		
		int startIndex = 0;
		int endIndex = startIndex + encMatrix.length;
		String pieceToEnc = "";
		int[] matrixToEnc = new int[encMatrix.length];
		int[] cipherPiece;
		while (endIndex <= plaintext.length())
		{
			pieceToEnc = plaintext.substring(startIndex, endIndex);
			for (int ind = 0; ind < pieceToEnc.length(); ind++)
			{
				matrixToEnc[ind] = pieceToEnc.charAt(ind) - 64;
			}
			cipherPiece = multiplyMatrix(matrixToEnc);
			for (int loc = 0; loc < cipherPiece.length; loc++)
			{
				cipher.append((char)(cipherPiece[loc] + 64));
			}
			startIndex = endIndex;
			endIndex += encMatrix.length;
		}
		return group(cipher.toString());
	}
	
	private int[] multiplyMatrix(int[] toMult)
	{
		// toMult.length must = encMatrix.length
		// to encipher the text, the operation encMatrix * toMult is done
		// note the order as matrix multiplication is NOT communitative
		// in fact, it is not possible to perform toMult * encMatrix (due to their dimensions) 
		int[] output = new int[toMult.length];
		int total = 0;
		for (int row = 0; row < encMatrix[0].length; row++)
		{
			for (int col = 0; col < encMatrix.length; col++)
			{
				total += encMatrix[row][col] * toMult[col];
			}
			output[row] = total;
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
			//output[loc] %= 26;
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
}