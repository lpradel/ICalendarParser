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

/**
 * 
 * @author lpradel
 *
 */
public class ICalendarPropertyParameter
{
	private final String name;
	private final String values;
	
	public ICalendarPropertyParameter(String pName, String pValues)
	{
		this.name = pName;
		this.values = pValues;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public String getValues()
	{
		return this.values;
	}
}
