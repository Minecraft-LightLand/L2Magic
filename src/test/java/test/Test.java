package test;

import java.util.Random;

public class Test {

	public static void main(String[] args) {
		Random r = new Random(12346);
		Vec3 dir = new Vec3(r.nextGaussian(), r.nextGaussian(), r.nextGaussian()).normalize();
		var ori = Orientation.fromForward(dir);

		// F x (N x F) = N

		// S = F x N
		// N = S x F
		// F = N x S
		System.out.println(ori.forward().cross(ori.normal().cross(ori.forward())).distanceTo(ori.normal()));
	}


}
