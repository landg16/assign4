import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.CountDownLatch;

public class Cracker {
	// Array of chars used to produce strings
	public static final char[] CHARS = "abcdefghijklmnopqrstuvwxyz0123456789.,-!".toCharArray();	
	private int NUM_CHARS = 40;
	private int MAX_SIZE;
	private int WORKERS_NUM;

	private String password;
	private CountDownLatch cdLatch;

	public Cracker(String password, int max_size, int num_workers){
		this.password = password;
		MAX_SIZE = max_size;
		WORKERS_NUM = num_workers;
		cdLatch = new CountDownLatch(num_workers);
		createWorkers(NUM_CHARS%WORKERS_NUM, NUM_CHARS/WORKERS_NUM, (NUM_CHARS%WORKERS_NUM)*(NUM_CHARS/WORKERS_NUM + 1));
	}

	public static void main(String[] arg){
		if(arg.length > 1){
			Cracker cracker = new Cracker(arg[0], Integer.parseInt(arg[1]), Integer.parseInt(arg[2]));
			cracker.result();
		}
		if(arg.length == 1){
			System.out.println(createHash(arg[0]));
		}
	}

	private void createWorkers(int rem, int div, int rem2) {
		for(int i = 0; i<rem; i++){
			Worker tmp = new Worker(i*( div + 1), (i + 1)*(div + 1));
			tmp.start();
		}
		for(int i = 0; i<WORKERS_NUM - rem; i++){
			Worker tmp = new Worker(rem2+i*div, rem2+(i+1)*div);
			tmp.start();
		}
	}

	private void result() {
		try {
			cdLatch.await();
			System.out.println("Done");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	/*
	 Given a byte[] array, produces a hex String,
	 such as "234a6f". with 2 chars for each byte in the array.
	 (provided code)
	*/
	public static String hexToString(byte[] bytes) {
		StringBuffer buff = new StringBuffer();
		for (int i=0; i<bytes.length; i++) {
			int val = bytes[i];
			val = val & 0xff;  // remove higher bits, sign
			if (val<16) buff.append('0'); // leading 0
			buff.append(Integer.toString(val, 16));
		}
		return buff.toString();
	}
	
	/*
	 Given a string of hex byte values such as "24a26f", creates
	 a byte[] array of those values, one byte value -128..127
	 for each 2 chars.
	 (provided code)
	*/
	public static byte[] hexToArray(String hex) {
		byte[] result = new byte[hex.length()/2];
		for (int i=0; i<hex.length(); i+=2) {
			result[i/2] = (byte) Integer.parseInt(hex.substring(i, i+2), 16);
		}
		return result;
	}

	private static String createHash(String str){
		MessageDigest md = null;
		try	{
			md = MessageDigest.getInstance("SHA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		byte[] digArr = md.digest(str.getBytes());
		return hexToString(digArr);
	}

	
	// possible test values:
	// a 86f7e437faa5a7fce15d1ddcb9eaeaea377667b8
	// fm adeb6f2a18fe33af368d91b09587b68e3abcb9a7
	// a! 34800e15707fae815d7c90d49de44aca97e2d759
	// xyz 66b27417d37e024c46526c2f6d358a754fc552f3
	private class Worker extends Thread {
		private final int first;
		private final int last;

		public Worker(int first, int last) {
			this.first = first;
			this.last = last;
		}

		@Override
		public void run() {
			for(int i = first; i<last; i++){
				String str = "" + String.valueOf(CHARS[i]);
				//System.out.println(str);
				rec(str);
			}
			cdLatch.countDown();
		}

		private void rec(String str){
			String hash = createHash(str);
			if(hash.equals(password)) System.out.println(str);
			if(str.length() >= MAX_SIZE) return;
			for(int i = 0; i<CHARS.length; i++){
				rec(str+CHARS[i]);
			}
		}
	}
}
