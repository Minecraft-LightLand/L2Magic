package test;

import dev.xkmc.l2library.serial.SerialClass;

public class Test {

	@SerialClass
	public static class R {

	}

	@SerialClass
	public static class T extends R {

		@SerialClass.SerialField
		public int a;

		@SerialClass.SerialField
		public String str = "";

		public T() {

		}

	}

	public static void main(String[] args) {
		String property = "java.io.tmpdir";

		// Get the temporary directory and print it.
		String tempDir = System.getProperty(property);
		System.out.println("OS temporary directory is " + tempDir);
	}



}
