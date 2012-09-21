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
public class ICalendarComponentProperty extends ICalendarProperty
{
	public ICalendarComponentProperty(String pName)
	{
		super(pName);
	}
	
	/* Constructor for Property with only 1(!) parameter = value (!!!) */
	public ICalendarComponentProperty(String pName, String pValue)
	{
		super(pName, pValue);
	}
}
