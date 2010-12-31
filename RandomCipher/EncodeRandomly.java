import java.io.IOException;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.util.Scanner;

public class EncodeRandomly 
{
	public static void main(String[] args) throws IOException
	{
		Scanner kbd = new Scanner(System.in);
		System.out.println("Enter the name of a file with plaintext input:");
		String inputFile = kbd.nextLine();
		File inFile = new File(inputFile);
		FileInputStream fis = new FileInputStream(inFile);
		BufferedInputStream bis = new BufferedInputStream(fis);
		System.out.println("Enter the name of a file for the ciphertext output:");
		String outputFile = kbd.nextLine();
		File outFile = new File(outputFile);
		PrintWriter dataOut = new PrintWriter(outFile);
		
		StringBuilder message = new StringBuilder("");
		char input = (char)bis.read();
		
		while (bis.available() > 0)
		{
			message.append(input);
			input = (char)bis.read();
		}
		
		RandomEncipherer enc = new RandomEncipherer(message.toString());
		dataOut.println(enc.encipher() + "\n\n" + enc.getCipher());

		bis.close();
		fis.close();
		dataOut.close();
	}
}