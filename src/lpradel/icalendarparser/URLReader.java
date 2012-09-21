/*******************************************************************************
 * Copyright (c) 2012 Lukas Pradel.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Lukas Pradel - initial API and implementation
 ******************************************************************************/

/**
 * @author lpradel
 */

package lpradel.icalendarparser;

import java.net.*;
import java.io.*;
import java.util.Scanner;

/**
 * 
 * @author lpradel
 *
 */
public class URLReader
{
	private final String url;
	
	public URLReader(String url)
	{
		this.url = url;
	}
	
	/**
	 * Reads from {@link url} and saves information.
	 * 
	 * @return The file read as a String.
	 * @throws IOException
	 */
	public String readFromURL() throws IOException
	{
		StringBuilder text = new StringBuilder();
		String NL = System.getProperty("line.separator");
		Scanner in = null;;
		URL Url;
		
		/* attempt URL-creation */
		try
		{
			Url = new URL(this.url);
		}
		catch (MalformedURLException e)
		{
			throw new IOException("URLReader:\n"
									+ "Could not create URL!\nException cause: "
									+ e.getMessage());
		}
		
		/* attempt creating InputStream-Reader */
		try
		{
			in = new Scanner(Url.openStream());
		}
		catch (IOException e)
		{
			throw new IOException("URLReader:\n"
									+ "Could not open Stream to URL! "
									+ "Possibly invalid URL:\n"
									+ e.getMessage());
		}
		
		/* actual reading */
		try
		{
			while (in.hasNextLine())
			{
				text.append(in.nextLine() + NL);
			}
		}
		finally
		{
			/* close Scanner */
			in.close();
		}

		return text.toString();
	}
}
