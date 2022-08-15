package dev.xkmc.l2magic.compat.jei.screen;

import com.mojang.datafixers.util.Either;

public interface ExtraInfo<T> {

	void getInfo(Con<T> con);

	default void getInfo(ConA<T> con) {
		getInfo((x, y, w, h, e) -> {
			con.apply(x, y, w, h, e);
			return false;
		});
	}

	default void getInfoMouse(double mx, double my, ConA<T> con) {
		getInfo((x, y, w, h, e) -> {
			if (mx > x && mx < x + w && my > y && my < y + h) {
				con.apply(x, y, w, h, e);
				return true;
			}
			return false;
		});
	}

	interface Con<T> {

		boolean apply(int x, int y, int w, int h, T t);

	}

	interface ConA<T> {

		void apply(int x, int y, int w, int h, T t);

	}

	interface DoubleInfo<A, B> extends ExtraInfo<Either<A, B>> {

		void getInfoA(Con<A> con);

		default void getInfoA(ConA<A> con) {
			getInfoA((x, y, w, h, e) -> {
				con.apply(x, y, w, h, e);
				return false;
			});
		}

		void getInfoB(Con<B> con);

		default void getInfoB(ConA<B> con) {
			getInfoB((x, y, w, h, e) -> {
				con.apply(x, y, w, h, e);
				return false;
			});
		}

		default void getInfo(Con<Either<A, B>> con) {
			getInfoA((Con<A>) (x, y, w, h, e) -> con.apply(x, y, w, h, Either.left(e)));
			getInfoB((Con<B>) (x, y, w, h, e) -> con.apply(x, y, w, h, Either.right(e)));
		}

		default void getInfoAMouse(double mx, double my, ConA<A> con) {
			getInfoA((x, y, w, h, e) -> {
				if (mx > x && mx < x + w && my > y && my < y + h) {
					con.apply(x, y, w, h, e);
					return true;
				}
				return false;
			});
		}

		default void getInfoBMouse(double mx, double my, ConA<B> con) {
			getInfoB((x, y, w, h, e) -> {
				if (mx > x && mx < x + w && my > y && my < y + h) {
					con.apply(x, y, w, h, e);
					return true;
				}
				return false;
			});
		}

	}

}
