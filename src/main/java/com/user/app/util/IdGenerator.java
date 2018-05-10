package com.user.app.util;

import java.util.concurrent.atomic.AtomicLong;

public class IdGenerator {

	private static AtomicLong idCounter = new AtomicLong();

	public static Long createID()
	{
	    return idCounter.getAndIncrement();
	}
}
