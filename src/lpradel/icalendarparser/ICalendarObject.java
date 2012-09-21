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
public class ICalendarObject
{
	LinkedList<ICalendarComponent> components;
	LinkedList<ICalendarProperty> properties;
	
	public ICalendarObject()
	{
		this.components = new LinkedList<ICalendarComponent>();
		this.properties = new LinkedList<ICalendarProperty>();
	}
	
	public LinkedList<ICalendarComponent> getComponents()
	{
		return this.components;
	}
	
	public LinkedList<ICalendarProperty> getProperties()
	{
		return this.properties;
	}
	
	public void addComponent(ICalendarComponent pComponent)
	{
		components.add(pComponent);
	}
	
	public void addProperty(ICalendarProperty pProperty)
	{
		properties.add(pProperty);
	}
}
