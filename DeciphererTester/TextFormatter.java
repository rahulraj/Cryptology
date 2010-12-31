import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.PrintWriter;

public class TextFormatter 
{
	private File unformatted;
	private File formatted;
	
	public TextFormatter(File unf, File form)
	{
		unformatted = unf;
		formatted = form;
	}

	public void formatText() throws IOException
	{
		FileInputStream fis = new FileInputStream(unformatted);
		BufferedInputStream bis = new BufferedInputStream(fis);
		PrintWriter dataOut = new PrintWriter(formatted);
		StringBuilder toOut = new StringBuilder("");
		
		int numLetters = 0;
		int numWords = 0;
		
		while (bis.available() > 0)
		{
			char in = (char)(bis.read());
			if ((in >= 65 && in <= 90) || (in >= 97 && in <= 122))
			{
				// upper or lowercase letter
				toOut.append(in);
				numLetters++;
				if (numLetters == 5)
				{
					toOut.append(" ");
					numWords++;
					numLetters = 0;
				}
				if (numWords == 10)
				{
					toOut.append("\n");
					numWords = 0;
				}
			}		
		}
		dataOut.println(toOut.toString().toUpperCase());
		dataOut.close();
		fis.close();
		bis.close();
	}
}
