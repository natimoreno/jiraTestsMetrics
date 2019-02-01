package com.github.testing.jiraTestsMetrics;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Factory {

	Object make(String action) throws InvocationTargetException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException{
		
		String className = String.format("com.github.testing.jiraTestsMetrics.%s.java", action);
		Class<?> cl = Class.forName("com.github.testing.jiraTestsMetrics.Report");
		Constructor<?> cons = cl.getConstructor(String.class);
		return cons.newInstance();
	}
	
}
