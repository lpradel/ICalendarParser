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
public class ICalendarParser
{
	private final String calendar;		/* the input from file */
	private Scanner scanner;			/* for parsing */
	private ICalendarObject cal;		/* will eventually hold data after parsing */
	private ICalendarComponent comp; 	/* will hold the currently parsed component */
	
	/* Constants */
	private final static String alpha = "BEGIN:VCALENDAR";
	private final static String omega = "END:VCALENDAR";
	private final static String COMPSTART = "BEGIN";
	private final static String COMPEND = "END";
	
	public ICalendarParser(String pCalendar)
	{
		this.calendar = pCalendar;
		this.scanner = new Scanner(pCalendar);
		this.cal = new ICalendarObject();
	}
	
	/**
	 * Parses the calendar from file.
	 * 
	 * @return Object-representation of the calendar file.
	 * @throws Exception
	 */
	public ICalendarObject parse() throws Exception
	{
		/* String-parsing related */
		String line;	/* holds the currently read line */
		String name; 	/* will hold name of property, i.e. first substring */
		String value;	/* will hold the value of property, i.e. second substring */
		char delim;		/* holds the first delimiter in current substring */
		
		/* Parsing-semantic related */
		boolean parsing_component = false;	/* if true, we are currently parsing
											 * the definition of a component and
											 * are thus adding all properties
											 * to the component rather than
											 * to the object as a whole
											 */
		
		/* Parse line by line 
		 * TODO: line-unfolding: ref. 3.1
		 */
		
		/* Skip the first line, i.e. alpha */
		line = scanner.nextLine();	/* this should be = alpha */
		
		/* Main parsing loop */
		while (scanner.hasNextLine())
		{
			line = scanner.nextLine();	/* get next line */
			delim = first_delimiter(line);	/* get first delimiter */
			name = line_name(line, delim);	/* get name of prop, i.e. first substring */
			value = line_nextsub(line, name.length());
			/* note: line is basically split here */
			
			/* Check if the end is reached */
			if (line.equals(omega))
				break;
			
			/* Evaluation */
			if (name.equals(COMPSTART))
			{	/* Start of a component definition */
				parsing_component = true;
				
				/* Parse line as a new component */
				parseComponent(value);
			}
			else if (name.equals(COMPEND))
			{	/* End of component definition */
				parsing_component = false;
			}
			else
			{	/* by RFC there can only be Properties now!
				 * TODO: possibly omitting MORE ICalendarObjects
				 */
				if (parsing_component)
				{	/* we need to attribute this property to the current component */
					parseComponentProperty(name, value);
				}
				else
				{	/* we need to attribute this property to the whole Object */
					parseProperty(name, value);
				}
			}
		}
		
		/* Cleanup */
		scanner.close();
		
		/* Result */
		return cal;
	}
	
	/**
	 * Parses the given string as a new Component and adds it
	 * to the data structure, ref. 3.6
	 */ 
	private void parseComponent(String pName) throws Exception
	{
		/* create new Component, n.b. omitting syntax check! */
		comp = new ICalendarComponent(pName);
		
		/* add the component to data structure */
		cal.addComponent(comp);
	}
	
	/**
	 * Parses the current line given as name and value as a new Property 
	 * attributed to the Object as a whole, ref. 3.7
	 */
	private void parseProperty(String pName, String pValue)
	{
		ICalendarProperty property;

		/* Check if the property value has no more delimiters
		 * and thus has only 1 parameter, it's value
		 */
		if (first_delimiter(pValue) == '_')
		{	/* 1-parameter-Property */
			property = new ICalendarProperty(pName, pValue);
		}
		else
		{	/* Multiple-parameter-Property */
			property = new ICalendarProperty(pName);
			
			/* Determine all parameters
			 * separated by ';'
			 * syntax: <name>=<value>
			 */
			String parameters = pValue;
			char delim = param_delimiter(parameters);
			String name;
			String value;
			
			/* read parameter-by-parameter */
			while (delim != '_' & parameters.length() > 0)
			{
				name = line_name(parameters, delim);
				parameters = line_nextsub(parameters, name.length());
				delim = param_delimiter(parameters);
				value = line_name(parameters, delim);
				parameters = line_nextsub(parameters, value.length());
				
				/* Check for keyword, if it is, skip it! */
				if (isAllUpper(value))
				{
					delim = param_delimiter(parameters);
					value = line_name(parameters, delim);
				}
				
				/* Add current parameter */
				property.addParameter(name, value);
				
				/* Continue */
				delim = param_delimiter(name);
			}
		}
		
		/* add the new property to the object as a whole */
		cal.addProperty(property);
	}
	
	/**
	 * Parses the current line given as name and value as a new Property 
	 * attributed to the currently parsed Component, ref. 3.8
	 */
	private void parseComponentProperty(String pName, String pValue)
	{
		ICalendarComponentProperty property;
		
		/* Check if the property value has no more delimiters
		 * and thus has only 1 parameter, it's value
		 */
		if (first_delimiter(pValue) == '_')
		{	/* 1-parameter-Property */
			property = new ICalendarComponentProperty(pName, pValue);
		}
		else
		{	/* Multiple-parameter-Property */
			property = new ICalendarComponentProperty(pName);
			
			/* Determine all parameters
			 * separated by ';'
			 * syntax: <name>=<value>
			 */
			String parameters = pValue;
			char delim = param_delimiter(parameters);
			String name;
			String value;
			
			/* read parameter-by-parameter */
			while (delim != '_' & parameters.length() > 0)
			{
				name = line_name(parameters, delim);
				parameters = line_nextsub(parameters, name.length());
				delim = param_delimiter(parameters);
				value = line_name(parameters, delim);
				parameters = line_nextsub(parameters, value.length());
				
				/* Check for keyword, if it is, skip it! */
				if (isAllUpper(value))
				{
					delim = param_delimiter(parameters);
					value = line_name(parameters, delim);
				}
				
				/* Add current parameter */
				property.addParameter(name, value);
				
				/* Continue */
				delim = param_delimiter(name);
			}
		}
		
		/* add the new property to the current component */
		comp.addProperty(property);
	}
	
	/**
	 * Checks if the file syntax is valid ICalendar format.
	 * 
	 * @return true iff. the file is a valid ICalendar formatted file.
	 * @throws Exception
	 */
	public boolean checkSyntax() throws Exception
	{
		boolean valid = true;
		String line;
		
		if (scanner.hasNextLine())
		{
			if ((line = scanner.nextLine()).equals(alpha))
			{	/* First line must be = alpha */
				while (scanner.hasNextLine())
				{
					line = scanner.nextLine();
				}
				if (!line.equals(omega))
				{	/* Last line must be = omega */
					valid = false;
				}
			}
			else
			{
				valid = false;
			}
		}
		else
		{
			valid = false;
		}
		
		/* Reset scanner */
		scanner.close();
		scanner = new Scanner(calendar);
		
		return valid;
	}
	
	/* Helpers for String analysis
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * */
	
	/**
	 * Returns next substring from length of previous substring.
	 * 
	 * @param line
	 * @param pre
	 * @return
	 */
	private String line_nextsub(String line, int pre)
	{
		return line.substring(pre+1);	/* note: skip the delimiter! */
	}
	
	/**
	 * Returns substring from beginning to first delimiter.
	 * 
	 * @param line
	 * @param delim
	 * @return
	 */
	private String line_name(String line, char delim)
	{
		int i;
		
		for (i = 0; i < line.length(); i++)
		{
			if (line.charAt(i) == delim)
				break;
		}
		
		return line.substring(0, i);
	}
	
	/**
	 * Returns the first delimiter.
	 * 
	 * @param line
	 * @return
	 */
	private char first_delimiter(String line)
	{
		char linearr[] = new char[line.length()];
		linearr = line.toCharArray();
		
		for (int i = 0; i < line.length(); i++)
		{
			switch (linearr[i])
			{
			case ':':
				return linearr[i];
				
			case ';':
				return linearr[i];
				
			case '=':
				return linearr[i];
			}
		}
		
		return '_';		/* default case: no delimiter */
	}
	
	private char param_delimiter(String param)
	{
		char arr[] = new char[param.length()];
		arr = param.toCharArray();
		
		for (int i = 0; i < param.length(); i++)
		{
			switch (arr[i])
			{
			case ':':
				return arr[i];
				
			case ';':
				return arr[i];
				
			case '=':
				return arr[i];
			}
		}
		
		return '_';		/* default case: no delimiter */
	}
	
	/**
	 * Checks if {@link s} is all uppercase.
	 * 
	 * @param s The string to check
	 * @return true, iff {@link s} is all uppercase
	 */
	protected boolean isAllUpper(String s) {
	    for(char c : s.toCharArray()) {
	       if(Character.isLetter(c) && Character.isLowerCase(c)) {
	           return false;
	        }
	    }
	    return true;
	}

}
