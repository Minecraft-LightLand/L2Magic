package test;

import java.util.TreeSet;

public class Test {

	public static void main(String[] args) {
		int min = 0;
		for (int r = 1; r < 64 * 64; r++) {
			TreeSet<Long> set = new TreeSet<>();
			TreeSet<Long> triplet = new TreeSet<>();
			int bx = (int) Math.floor(Math.sqrt(r));
			for (int x = -bx; x <= bx; x++) {
				int by = (int) Math.floor(Math.sqrt(r - x * x));
				for (int y = -by; y <= by; y++) {
					int z = (int) Math.floor(Math.sqrt(r - x * x - y * y));
					if (x * x + y * y + z * z == r) {
						set.add((long) (bx + x) << 16 | (long) (bx + y) << 8 | (long) (bx + z));
						set.add((long) (bx + x) << 16 | (long) (bx + y) << 8 | (long) (bx - z));

						long ax = Math.abs(x);
						long ay = Math.abs(y);
						long aa = Math.min(Math.min(ax, ay), z);
						long ac = Math.max(Math.max(ax, ay), z);
						long ab = ax + ay + z - aa - ac;
						triplet.add(aa << 16 | ab);
					}
				}
			}
			if (set.size() > min) {
				min = set.size();
				int sr = (int) Math.ceil(Math.sqrt(r));
				System.out.println("r <= " + sr + ", r^2 = " + r + ", count = " + min);
				for (long val : triplet) {
					long x = val & 0xFFFF;
					long y = val >> 16 & 0xFFFF;
					int z = (int) Math.sqrt(r - x * x - y * y);
					System.out.println("(" + y + "," + x + "," + z + ")");
				}
			}
		}
	}


}
