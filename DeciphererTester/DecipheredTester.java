import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class DecipheredTester 
{
	private File correct;
	private File toTest;
	private StringBuilder theRight;
	private int numAlphs;
	
	public DecipheredTester(File cor, File toT)
	{
		correct = cor;
		toTest = toT;
		theRight = null;
	}
	
	public DecipheredTester(StringBuilder aCorrect, File toT, int numAl)
	{
		correct = null;
		theRight = aCorrect;
		toTest = toT;
		numAlphs = numAl;
	}
	
	public String testData() throws IOException
	{
		char right = ' ';
		char test = ' ';
		if (theRight == null)
		{
			theRight = new StringBuilder("");
			FileInputStream fis1 = new FileInputStream(correct);
			BufferedInputStream correctRead = new BufferedInputStream(fis1);
			while (correctRead.available() > 0)
			{
				right = (char)(correctRead.read());
				if (right >= 65 && right <= 90)
				{
					theRight.append(right);
				}
				System.out.println("Reading correctRead");
				System.out.println(correctRead.available());
			}
			fis1.close();
			correctRead.close();
		}
		FileInputStream fis2 = new FileInputStream(toTest);
		BufferedInputStream testRead = new BufferedInputStream(fis2);
		
		
		int numRight = 0;
		int numWrong = 0;
		int numNotTried = 0;
		int totalLetters = 0;
		
		StringBuilder theTest = new StringBuilder("");

		while (testRead.available() > 0)
		{
			test = (char)(testRead.read());
			if ((test >= 65 && test <= 90) || test == '?')
			{
				theTest.append(test);
			}
			System.out.println("Reading testRead");
			System.out.println(testRead.available());
		}
		for (int index = 0; index < theRight.length(); index++)
		{
			right = theRight.charAt(index);
			test = theTest.charAt(index);
			totalLetters++;
			if (right == test)
			{
				numRight++;
			}
			else if (test == '?')
			{
				numNotTried++;
			}
			else
			{
				numWrong++;
			}
		}
		
		StringBuilder build = new StringBuilder("");
		build.append("Number of alphabets: " + numAlphs + "\nTotal number of characters: " + totalLetters +
					 "\nTotal number correct: " + numRight + "\nTotal number wrong: " + numWrong +
					 "\nTotal number unattempted: " + numNotTried + "\nPercentage correct: " + (double)numRight / totalLetters +
					 "\n\n");
		return build.toString();
		
		/*PrintWriter dataOut = new PrintWriter(testResults);
		dataOut.println("Number of alphabets: " + numAlphs);
		dataOut.println("Total number of characters: " + totalLetters);
		dataOut.println("Total number correct: " + numRight);
		dataOut.println("Total number wrong: " + numWrong);
		dataOut.println("Total number unattempted: " + numNotTried);
		dataOut.println("Percentage correct: " + (double)numRight / totalLetters);
		
		fis2.close();
		testRead.close();
		dataOut.close();*/
	}
}