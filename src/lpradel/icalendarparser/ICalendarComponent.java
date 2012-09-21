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

/**
 * 
 * @author lpradel
 *
 */
public class ICalendarComponent
{
	private final String name;
	LinkedList<ICalendarComponentProperty> properties;
	
	public ICalendarComponent(String pName)
	{
		this.name = pName;
		this.properties = new LinkedList<ICalendarComponentProperty>();
	}
	
	/* Constructor including set of properties */
	public ICalendarComponent(String pName, LinkedList<ICalendarComponentProperty> pProperties)
	{
		this.name = pName;
		this.properties = pProperties;
	}
	
	/**
	 * Checks if this component has a summary.
	 * 
	 * @return true iff this component has a summary
	 */
	public boolean hasSummary()
	{
		for (ICalendarComponentProperty cp : this.getProperties())
		{
			if (cp.getName().equals("SUMMARY"))
				return true;
		}
		
		return false;
	}
	
	/**
	 * Returns this component's summary-property if it exists, otherwise null.
	 * 
	 * @return The summary of this component
	 */
	public String summary()
	{
		String summary = "";
		
		/* Safety-check */
		if (!this.hasSummary())	
			return null;
		
		/* Search and return summary-property */
		for (ICalendarComponentProperty cp : this.getProperties())
		{
			if (cp.getName().equals("SUMMARY"))
				summary = cp.getParameters().getFirst().getValues();
		}
		
		return summary;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public LinkedList<ICalendarComponentProperty> getProperties()
	{
		return this.properties;
	}
	
	public void addProperty(ICalendarProperty pProperty)
	{
		this.properties.add((ICalendarComponentProperty)pProperty);
	}
}
