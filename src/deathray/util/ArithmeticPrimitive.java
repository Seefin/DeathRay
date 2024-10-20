package deathray.util;

import java.io.Serializable;

public abstract class ArithmeticPrimitive implements Comparable<ArithmeticPrimitive>, Serializable {
	private static final long serialVersionUID = 1917294972189409209L;

	public abstract ArithmeticPrimitive add(ArithmeticPrimitive n);
	public abstract ArithmeticPrimitive subtract(ArithmeticPrimitive n);
	public abstract ArithmeticPrimitive multiplyBy(ArithmeticPrimitive n);
	public abstract ArithmeticPrimitive divideBy(ArithmeticPrimitive n);

	public abstract int compareTo(ArithmeticPrimitive o);

}
