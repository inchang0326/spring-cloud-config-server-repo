package com.example.exam.util;

public class StringUtil {

	public static boolean isValid(String str) {
		if (null != str && !str.isEmpty()) {
			return true;
		}
		return false;
	}
}
