/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.lastupdated.internal;

import org.openhab.core.scriptengine.action.ActionDoc;
import org.openhab.core.scriptengine.action.ParamDoc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.openhab.core.types.State;
import org.openhab.core.items.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * This class contains the methods that are made available in scripts and rules for LastUpdated.
 * 
 * @author eschava
 * @since 1.5.1
 */
public class LastUpdated {

	private static final Logger logger = LoggerFactory.getLogger(LastUpdated.class);
	
	private static Map<String, Long> lastUpdatedTimeMap = Collections.synchronizedMap(new HashMap<String, Long>());
	private static StateChangeListener stateChangeListener = new Listener();

	// provide public static methods here
	
	// Example
	@ActionDoc(text="Method returns time when item was updated at last", 
			returns="Unix time in milliseconds")
	public static long lastUpdated(@ParamDoc(name="item", text="Item to get last update time") Item item) {
//		if (!LastUpdatedActionService.isProperlyConfigured) {
//			logger.debug("LastUpdated action is not yet configured - execution aborted!");
//			return 0l;
//		}
		Long lastUpdated = lastUpdatedTimeMap.get(item.getName());
		if (lastUpdated == null)
		{
			((GenericItem)item).addStateChangeListener(stateChangeListener);
			
			lastUpdated = 0l;
			lastUpdatedTimeMap.put(item.getName(), lastUpdated);			
		}
		return lastUpdated.longValue();
	}
	
	private static class Listener implements StateChangeListener
	{
		@Override
		public void stateChanged(Item item, State oldState, State newState)
		{
			lastUpdatedTimeMap.put(item.getName(), System.currentTimeMillis());
		}

		@Override
		public void stateUpdated(Item item, State state)
		{
			lastUpdatedTimeMap.put(item.getName(), System.currentTimeMillis());
		}
	}
}
