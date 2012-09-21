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
public class ICalendarProperty
{
	String name;
	LinkedList<ICalendarPropertyParameter> parameters;
	
	public ICalendarProperty(String pName)
	{
		this.name = pName;
		this.parameters = new LinkedList<ICalendarPropertyParameter>();
	}
	
	/* Constructor for Property with only 1(!) parameter = value */
	public ICalendarProperty(String pName, String pValue)
	{
		this.name = pName;

		this.parameters = new LinkedList<ICalendarPropertyParameter>();
		this.addParameter(new ICalendarPropertyParameter("Value", pValue));
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public LinkedList<ICalendarPropertyParameter> getParameters()
	{
		return this.parameters;
	}
	
	public void addParameter(ICalendarPropertyParameter param)
	{
		parameters.add(param);
	}
	
	public void addParameter(String pName, String pValue)
	{
		parameters.add(new ICalendarPropertyParameter(pName, pValue));
	}
}
