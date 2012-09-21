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

import java.util.LinkedList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.SimpleTimeZone;

/**
 * 
 * @author lpradel
 *
 */
public class ICalendarPrinter
{
	private ICalendarObject calendar;
	
	/* Component types, ref. 3.6 */
	private final static String CAL_COMPONENT_VEVENT = "VEVENT";
	
	/* Object property types, ref. 3.7 */
	private final static String CAL_PROP_CALSCALE = "CALSCALE";
	private final static String CAL_PROP_METHOD = "METHOD";
	private final static String CAL_PROP_PRODID = "PRODID";
	private final static String CAL_PROP_VERSION = "VERSION";
	
	/* Extension Object property types, ref. Internet */
	private final static String CAL_PROP_X_WR_CALNAME = "X-WR-CALNAME";
	private final static String CAL_PROP_X_WR_TIMEZONE = "X-WR-TIMEZONE";	/* note: SHOULD use VTIMEZONE! */
	
	/* Component property types, ref. 3.8
	 * 
	 * 
	 * */
	/* Descriptive Component Properties, ref. 3.8.1 */
	private final static String CAL_COMP_PROP_DESCRIPTION = "DESCRIPTION";
	private final static String CAL_COMP_PROP_CATEGORIES = "CATEGORIES";
	private final static String CAL_COMP_PROP_LOCATION = "LOCATION";
	
	/* Date and Time Component Properties, ref. 3.8.2 */
	private final static String CAL_COMP_PROP_DTSTART = "DTSTART";
	private final static String CAL_COMP_PROP_DTEND = "DTEND";
	private final static String CAL_COMP_PROP_DTSTAMP = "DTSTAMP";
	private final static String CAL_COMP_PROP_CREATED = "CREATED";
	private final static String CAL_COMP_PROP_LAST_MODIFIED = "LAST-MODIFIED";
	
	/* Relationship Component Properties, ref. 3.8.4 */
	private final static String CAL_COMP_PROP_ORGANIZER = "ORGANIZER";
	private final static String CAL_COMP_PROP_UID = "UID";
	
	/* Other Component Properties */
	private final static String CAL_COMP_PROP_SEQUENCE = "SEQUENCE";
	private final static String CAL_COMP_PROP_STATUS = "STATUS";
	private final static String CAL_COMP_PROP_TRANSP = "TRANSP";
	
	public ICalendarPrinter(ICalendarObject pCalendar)
	{
		this.calendar = pCalendar;
	}
	
	/**
	 * Prints the calendar.
	 * 
	 * @throws Exception
	 */
	public void printCalendar() throws Exception
	{
		/* Safety-check */
		if (calendar == null)
			throw new Exception("ICalendarObject is not initialized!");
		
		/* Header */
		print("\n");
		print("-----------------");
		print("KALENDER :");
		print("\n");
		
		/* print a summary */
		print("- Anzahl an Einträgen im Kalender: " + calendar.getComponents().size());
		
		/* print the calendar-as-whole attributed properties */
		printCalendarProperties();
		print("\n");
		
		/* print the components */
		printCalendarComponents();
	}
	
	/**
	 * Prints all of the calendar's properties, i.e. calendar-as-whole attributed properties.
	 */
	private void printCalendarProperties()
	{	/* note: these are all 1-parameter-properties! */
		
		String value;
		
		for (ICalendarProperty p : calendar.getProperties())
		{
			value = p.getParameters().getFirst().getValues();
			
			/* Extension-Properties first, most general */
			if (p.getName().equals(CAL_PROP_X_WR_CALNAME))
			{
				print("- Name des Kalenders: " + value);
			}
			else if (p.getName().equals(CAL_PROP_X_WR_TIMEZONE))
			{
				print("- Zeitzone des Kalenders: " + value);
			}
			else if (p.getName().equals(CAL_PROP_CALSCALE))
			{
				print("- Kalendersystem: " + value);
			}
			else if (p.getName().equals(CAL_PROP_METHOD))
			{
				print("- Objekt-Methode des Kalenders: " + value);
			}
			else if (p.getName().equals(CAL_PROP_PRODID))
			{
				print("- Kennzeichen des Produkts, mit dem der Kalender angelegt wurde: " + value);
			}
			else if (p.getName().equals(CAL_PROP_VERSION))
			{
				print("- Versionsnummer der iCalendar-Spezifikation des Kalenders: " + value);
			}
			else
			{
				/* error case, possible causes:
				 * 1. we did not implement this aspect of the iCalendar-Protocol fully
				 * 2. the Property name is invalid: Syntax error!
				 */
			}
		}
	}
	
	/**
	 * Prints all of the calendar's components.
	 */
	private void printCalendarComponents()
	{
		String description;

		for (ICalendarComponent component : calendar.getComponents())
		{
			/* Check if summary, i.e. component-name, exists */
			if (component.hasSummary())
				description = "Kalendareintrag " + component.summary() + " vom Typ ";
			else
				description = "Kalendereintrag vom Typ ";
			
			/* Interpret Component type */
			if (component.getName().equals(CAL_COMPONENT_VEVENT))
			{
				description = description + "Ereignis:";
			}
			else
			{
				/* error case, possible causes:
				 * 1. we did not implement this aspect of the iCalendar-Protocol fully
				 * 2. the Component name is invalid: Syntax error!
				 */
			}
			
			/* Print component type */
			print(description);
			
			/* Interpret all of the components' properties */
			for (ICalendarComponentProperty cp : component.getProperties())
			{
				printCalendarComponentProperty(cp);
			}
			
			System.out.println("\n");
		}
	}
	
	/**
	 * Interprets a component-attributed property
	 */
	private void printCalendarComponentProperty(ICalendarComponentProperty p)
	{
		String property_name = p.getName();
		LinkedList<ICalendarPropertyParameter> parameters = p.getParameters();
		ICalendarPropertyParameter first_param = parameters.getFirst();	/* handy for 1-param-properties */
		String out;	/* needed for some multi-parameter Properties */
		
		/* TODO: perhaps sort paramaters meaningfully */
		
		/* N.B.: Summary already interpreted in printCalendarComponents!!!
		 * 
		 */
		
		if (property_name.equals(CAL_COMP_PROP_DESCRIPTION))
		{
			print("\t- Was/Beschreibung: " + first_param.getValues());
		}
		else if (property_name.equals(CAL_COMP_PROP_LOCATION))
		{
			print("\t- Wo: " + first_param.getValues());
		}
		else if (property_name.equals(CAL_COMP_PROP_ORGANIZER))
		{
			out = "\t- Wer/Veranstalter: ";
			/* loop parameters */
			for (ICalendarPropertyParameter param : parameters)
			{
				if (param.getName().equals("CN"))
				{	/* Organizer common name */
					out = out + param.getValues();
				}
			}
			print(out);
		}
		else if (property_name.equals(CAL_COMP_PROP_UID))
		{
			print("\t- Identifikationszeichen: " + first_param.getValues());
		}
		else if (property_name.equals(CAL_COMP_PROP_CATEGORIES))
		{
			print("\t- Kategorien: " + first_param.getValues());
		}
		else if (property_name.equals(CAL_COMP_PROP_SEQUENCE))
		{
			print("\t- Änderungs/Revisionsnummer: " + first_param.getValues());
		}
		else if (property_name.equals(CAL_COMP_PROP_STATUS))
		{
			out = "\t- Status: ";

			/* evaluate status */
			if (first_param.getValues().equals("TENTATIVE"))
				out += "Provisorisch";
			else if (first_param.getValues().equals("CONFIRMED"))
				out += "Bestätigt";
			else if (first_param.getValues().equals("CONFIRMED"))
				out += "Bestätigt";
			else if (first_param.getValues().equals("CANCELLED"))
				out += "Fällt aus";
			else if (first_param.getValues().equals("NEEDS-ACTION"))
				out += "Muss noch erledigt werden";
			else if (first_param.getValues().equals("COMPLETED"))
				out += "Erledigt";
			else if (first_param.getValues().equals("IN-PROCESS"))
				out += "Wird bearbeitet";
			else if (first_param.getValues().equals("DRAFT"))
				out += "Entwurf";
			else if (first_param.getValues().equals("FINAL"))
				out += "Finale Version";
			else
			{
				/* error case, possible causes:
				 * invalid param-value, this property was fully implemented
				 */
			}
			
			/* print */
			print(out);
		}
		else if (property_name.equals(CAL_COMP_PROP_TRANSP))
		{
			if (first_param.getValues().equals("OPAQUE"))
			{
				print("\t- Sonstiges: Beansprucht Zeit");
			}
			else if (first_param.getValues().equals("TRANSPARENT"))
			{
				print("\t- Sonstiges: Beansprucht keine Zeit");
			}
			else
			{
				/* error case, possible causes:
				 * invalid param-value, this property was fully implemented
				 */
			}
		}
		/* Date and Time Component Properties
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 */
		else if (property_name.equals(CAL_COMP_PROP_DTSTART))
		{
			SimpleDateFormat sdf = new SimpleDateFormat();
			Date date = new Date();

			out = "\t- Wann/Beginn: ";
			/* loop parameters */
			for (ICalendarPropertyParameter param : parameters)
			{
				if (param.getName().equals("Value") || param.getName().equals("VALUE"))
				{	/* Decode time
					 * 1. YYYYMMDD (Day only)
					 * 2. YYYYMMDDTHHMMSS (Day and Time, Floating time)
					 * 3. YYYYMMDDTHHMMSSZ (Day and Time, UTC)
					 */

					/* set time-pattern accordingly */
					if (param.getValues().contains("T"))
					{	/* 2. and 3. */
						if (param.getValues().contains("Z"))
						{	/* 3. */
							sdf.applyPattern("yyyyMMdd'T'HHmmss'Z'");
							sdf.setTimeZone(new SimpleTimeZone(0, ""));	/* UTC: 3600000 */
						}
						else
						{	/* 2. */
							sdf.applyPattern("yyyyMMdd'T'HHmmss");
						}
					}
					else
					{
						/* 1.  */
						sdf.applyPattern("yyyyMMdd");
					}
					try
					{
						date = sdf.parse(param.getValues());
					}
					catch (Exception e)
					{
						print("ICalendarPrinter: Error parsing date\nMessage: "
								+ e.getMessage());
					}
					out = out + date.toString();
				}
			}
			print(out);
		}
		else if (property_name.equals(CAL_COMP_PROP_DTEND))
		{
			SimpleDateFormat sdf = new SimpleDateFormat();
			Date date = new Date();

			out = "\t- Wann/Ende: ";
			/* loop parameters */
			for (ICalendarPropertyParameter param : parameters)
			{
				if (param.getName().equals("Value") || param.getName().equals("VALUE"))
				{	/* Decode time
					 * 1. YYYYMMDD (Day only)
					 * 2. YYYYMMDDTHHMMSS (Day and Time, Floating time)
					 * 3. YYYYMMDDTHHMMSSZ (Day and Time, UTC)
					 */
					
					/* set time-pattern accordingly */
					if (param.getValues().contains("T"))
					{	/* 2. and 3. */
						if (param.getValues().contains("Z"))
						{	/* 3. */
							sdf.applyPattern("yyyyMMdd'T'HHmmss'Z'");
							sdf.setTimeZone(new SimpleTimeZone(0, ""));	/* UTC: 3600000 */
						}
						else
						{	/* 2. */
							sdf.applyPattern("yyyyMMdd'T'HHmmss");
						}
					}
					else
					{
						/* 1.  */
						sdf.applyPattern("yyyyMMdd");
					}
					try
					{
						date = sdf.parse(param.getValues());
					}
					catch (Exception e)
					{
						print("ICalendarPrinter: Error parsing date\nMessage: "
								+ e.getMessage());
					}
					out = out + date.toString();
				}
			}
			print(out);
		}
		else if (property_name.equals(CAL_COMP_PROP_DTSTAMP))
		{
			SimpleDateFormat sdf = new SimpleDateFormat();
			Date date = new Date();

			out = "\t- Wann/iCalendar-Objekt-Erstellung: ";
			/* loop parameters */
			for (ICalendarPropertyParameter param : parameters)
			{
				if (param.getName().equals("Value") || param.getName().equals("VALUE"))
				{	/* Decode time
					 * 1. YYYYMMDD (Day only)
					 * 2. YYYYMMDDTHHMMSS (Day and Time, Floating time)
					 * 3. YYYYMMDDTHHMMSSZ (Day and Time, UTC)
					 */
					
					/* set time-pattern accordingly */
					if (param.getValues().contains("T"))
					{	/* 2. and 3. */
						if (param.getValues().contains("Z"))
						{	/* 3. */
							sdf.applyPattern("yyyyMMdd'T'HHmmss'Z'");
							sdf.setTimeZone(new SimpleTimeZone(0, ""));	/* UTC: 3600000 */
						}
						else
						{	/* 2. */
							sdf.applyPattern("yyyyMMdd'T'HHmmss");
						}
					}
					else
					{
						/* 1.  */
						sdf.applyPattern("yyyyMMdd");
					}
					try
					{
						date = sdf.parse(param.getValues());
					}
					catch (Exception e)
					{
						print("ICalendarPrinter: Error parsing date\nMessage: "
								+ e.getMessage());
					}
					out = out + date.toString();
				}
			}
			print(out);
		}
		else if (property_name.equals(CAL_COMP_PROP_CREATED))
		{
			SimpleDateFormat sdf = new SimpleDateFormat();
			Date date = new Date();

			out = "\t- Wann/erstellt: ";
			/* loop parameters */
			for (ICalendarPropertyParameter param : parameters)
			{
				if (param.getName().equals("Value") || param.getName().equals("VALUE"))
				{	/* Decode time
					 * 1. YYYYMMDD (Day only)
					 * 2. YYYYMMDDTHHMMSS (Day and Time, Floating time)
					 * 3. YYYYMMDDTHHMMSSZ (Day and Time, UTC)
					 */
					
					/* set time-pattern accordingly */
					if (param.getValues().contains("T"))
					{	/* 2. and 3. */
						if (param.getValues().contains("Z"))
						{	/* 3. */
							sdf.applyPattern("yyyyMMdd'T'HHmmss'Z'");
							sdf.setTimeZone(new SimpleTimeZone(0, ""));	/* UTC: 3600000 */
						}
						else
						{	/* 2. */
							sdf.applyPattern("yyyyMMdd'T'HHmmss");
						}
					}
					else
					{
						/* 1.  */
						sdf.applyPattern("yyyyMMdd");
					}
					try
					{
						date = sdf.parse(param.getValues());
					}
					catch (Exception e)
					{
						print("ICalendarPrinter: Error parsing date\nMessage: "
								+ e.getMessage());
					}
					out = out + date.toString();
				}
			}
			print(out);
		}
		else if (property_name.equals(CAL_COMP_PROP_LAST_MODIFIED))
		{
			SimpleDateFormat sdf = new SimpleDateFormat();
			Date date = new Date();

			out = "\t- Wann/zuletzt geändert: ";
			/* loop parameters */
			for (ICalendarPropertyParameter param : parameters)
			{
				if (param.getName().equals("Value") || param.getName().equals("VALUE"))
				{	/* Decode time
					 * 1. YYYYMMDD (Day only)
					 * 2. YYYYMMDDTHHMMSS (Day and Time, Floating time)
					 * 3. YYYYMMDDTHHMMSSZ (Day and Time, UTC)
					 */
					
					/* set time-pattern accordingly */
					if (param.getValues().contains("T"))
					{	/* 2. and 3. */
						if (param.getValues().contains("Z"))
						{	/* 3. */
							sdf.applyPattern("yyyyMMdd'T'HHmmss'Z'");
							sdf.setTimeZone(new SimpleTimeZone(0, ""));	/* UTC: 3600000 */
						}
						else
						{	/* 2. */
							sdf.applyPattern("yyyyMMdd'T'HHmmss");
						}
					}
					else
					{
						/* 1.  */
						sdf.applyPattern("yyyyMMdd");
					}
					try
					{
						date = sdf.parse(param.getValues());
					}
					catch (Exception e)
					{
						print("ICalendarPrinter: Error parsing date\nMessage: "
								+ e.getMessage());
					}
					out = out + date.toString();
				}
			}
			print(out);
		}
		else
		{
			/* error case, possible causes:
			 * 1. we did not implement this aspect of the iCalendar-Protocol fully
			 * 2. the Component Property name is invalid: Syntax error!
			 */
		}
	}
	
	/* TODO: modify for graphic output */
	private void print(String pText)
	{
		System.out.println(pText);
	}
}
