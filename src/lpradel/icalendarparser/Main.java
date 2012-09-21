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

import java.util.Scanner;

/**
 * 
 * @author lpradel
 *
 */
public class Main
{

	private final String fileUrl;
	private URLReader UrlReader;
	private ICalendarObject ICalendar;
	private ICalendarParser ICalParser;
	private ICalendarPrinter ICalPrinter;
	private String data;
	
	public Main(String pFileUrl)
	{
		this.fileUrl = pFileUrl;
		this.UrlReader = new URLReader(this.fileUrl);
	}
	
	/**
	 * Downloads the ICalendar file, parses and prints it.
	 */
	public void readCalendar()
	{
		/* 1. Read file from URL */
		log("Downloading file...");
		try
		{
			data = UrlReader.readFromURL();
		}
		catch (Exception e)
		{
			log("Main: Failed to download from URL!\nMessage: " + e.getMessage());
			return;
		}
		
		/* Parse calendar file data */
		this.ICalParser = new ICalendarParser(data);

		/* 2. Check syntax */
		log("Checking iCal-Syntax...");
		try
		{
			if (ICalParser.checkSyntax())
			{
				log("iCal-Syntax is OK!");
			}
			else
			{
				log("iCal-Syntax is NOT OK! Not a valid iCalendar-file!");
				log("Shutting down...");
				return;
			}
		}
		catch (Exception e)
		{
			log("Main: Error trying to check the iCal-Syntax!\nMessage: "
				+ e.getMessage());
			return;
		}
		
		/* 3. Parse the file */
		log("Parsing...");
		try
		{
			ICalendar = ICalParser.parse();
		}
		catch (Exception e)
		{
			log("Main: Error parsing the file!\nMessage: "
				+ e.getMessage());
		}
		
		/* 4. Interpret the parsed data */
		log("Interpreting...\n");
		ICalPrinter = new ICalendarPrinter(ICalendar);
		try
		{
			ICalPrinter.printCalendar();
		}
		catch (Exception e)
		{
			log("Main: Error interpreting the calendar!\nMessage: "
				+ e.getMessage());
		}
		
	}
	
	/**
	 * Reads the ICalendar file and initiates processing.
	 * 
	 * @param args May or may not contain a URL to ICalendar file.
	 */
	public static void main(String... args)
	{
		String fileUrl;
		Scanner in;
		
		/* Check command line arguments */
		if (args.length == 0)
		{	/* need manual input */
			in  = new Scanner(System.in);
			System.out.println("Bitte URL zur iCalendar-Datei eingeben:");
			fileUrl = in.nextLine();
			in.close();
		}
		else
		{
			/* URL from command line */
			fileUrl = args[0];
		}
		
		/* Get running */
		Main main = new Main(fileUrl);
		
		/* Read Calendar */
		main.readCalendar();
	}

	/**
	 * Logs {@link msg}.
	 * @param msg
	 */
	private void log (String msg)
	{
		System.out.println(msg);
	}
}
