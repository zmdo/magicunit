package cn.zmdo.magicunit;

import java.util.Iterator;
import java.util.Random;
import java.util.stream.IntStream;

public class RandomTool {
	
	public static final String KEY_DOMAIN = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	public static final int KEY_DOMAIN_LEN = KEY_DOMAIN.length();
	
	private static Random random = new Random() ;
	
	public static String getRandomKey(int n) {
		
		char chars[] = new char[n];
		IntStream sequence = random.ints(n, 0,KEY_DOMAIN_LEN);
		Iterator<Integer> iterator =  sequence.iterator();
		for(int i = 0 ; i < n ; i ++) {
			chars[i] = KEY_DOMAIN.charAt(iterator.next());
		}
		
		sequence.close();
		return new String(chars);
	}
	
}
