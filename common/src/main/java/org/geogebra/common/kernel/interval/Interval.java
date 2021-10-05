package org.geogebra.common.kernel.interval;

import static org.geogebra.common.kernel.interval.IntervalConstants.empty;

import java.util.Objects;

import org.geogebra.common.kernel.arithmetic.MyDouble;
import org.geogebra.common.plugin.Operation;
import org.geogebra.common.util.DoubleUtil;

/**
 * Class to implement interval arithmetic
 *
 */
public class Interval implements IntervalArithmetic, IntervalMiscOperands {
	private final IntervalAlgebra algebra = new IntervalAlgebra(this);
	private final IntervalMultiply multiply = new IntervalMultiply(this);
	private final IntervalDivide divide = new IntervalDivide(this);
	private final IntervalTrigonometric trigonometric = new IntervalTrigonometric(this);
	private final IntervalMiscOperandsImpl misc = new IntervalMiscOperandsImpl(this);
	private final IntervalEvaluate evaluate = new IntervalEvaluate(this);
	private final IntervalMultiplicativeInverse multiplicativeInverse
			= new IntervalMultiplicativeInverse(this);
	private final IntervalRoot root = new IntervalRoot(this);
	private double low;
	private double high;

	private enum Inversion { NONE(""), INVERTED("I"), UNINVERTED("U");

		private final String postfix;

		Inversion(String postfix) {
			this.postfix = postfix;
		}

		public String getPostfix() {
			return "".equals(postfix) ?  "" : " (" + postfix + ")";
		}
	}

	private Inversion inverted = Inversion.NONE;

	/**
	 * Creates a singleton interval [value, value]
	 *
	 * @param value for the singleton interval.
	 */
	public Interval(double value) {
		this(value, value);
	}

	/**
	 * Creates an interval with bounds [low, high]
	 *
	 * @param low lower bound of the interval
	 * @param high higher bound of the interval
	 */
	public Interval(double low, double high) {
		if (high < low) {
			setEmpty();
		} else {
			set(low, high);
		}
	}

	/**
	 * Creates an empty interval.
	 */
	public Interval() {
		setEmpty();
	}

	/**
	 * Copy constructor
	 *
	 * @param other to copy.
	 */
	public Interval(Interval other) {
		this(other.low, other.high);
		inverted = other.inverted;
	}

	/**
	 *
	 * @param interval interval.
	 * @param other interval.
	 * @return the max of interval and other.
	 */
	public static Interval max(Interval interval, Interval other) {
		if (interval.isEmpty() && other.isEmpty()) {
			return empty();
		} else if (interval.isEmpty()) {
			return other;
		} else if (other.isEmpty()) {
			return interval;
		}

		return new Interval(Math.max(interval.low, other.low),
				Math.max(interval.high, other.high));
	}

	/**
	 *
	 * @param interval interval.
	 * @param other interval.
	 * @return the min of interval and other.
	 */
	public static Interval min(Interval interval, Interval other) {
		if (interval.isEmpty() && other.isEmpty()) {
			return empty();
		} else if (interval.isEmpty()) {
			return other;
		} else if (other.isEmpty()) {
			return interval;
		}

		return new Interval(Math.min(interval.low, other.low),
				Math.min(interval.high, other.high));
	}

	/** Empty interval is represented by [∞, -∞]
	 * as in the original lib. */
	public void setEmpty() {
		set(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
	}

	/**
	 * Interval addition
	 *
	 * @param other interval to add
	 * @return this as result
	 */
	public Interval add(Interval other) {
		low += other.low;
		high += other.high;
		inverted = other.inverted;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o instanceof Interval) {
			return almostEqual((Interval) o);
		}
		return false;

	}

	@Override
	public String toString() {
		if (isWhole()) {
			return "Interval [-Infinity, Infinity] 1E" + inverted.getPostfix();
		} else if (isRealWhole()) {
			return "Interval [-Infinity, Infinity] R"  + inverted.getPostfix();
		}

		String result = "Interval [";
		if (!isEmpty()) {
			result += low;
			if (!isSingleton()) {
				result += ", " + high;
			}
		}

		result += "]" + inverted.getPostfix();
		return result;
	}

	/**
	 * Interval subtraction
	 *
	 * @param other to subtract from this interval
	 * @return this as result.
	 */
	public Interval subtract(Interval other) {
		if (isUndefined() || other.isUndefined()) {
			setUndefined();
		} else {
			low -= other.high;
			high -= other.low;
			inverted = other.inverted;
		}
		return this;
	}

	@Override
	public Interval multiply(Interval other) {
		return multiply.multiply(other);
	}

	@Override
	public Interval divide(Interval other) {
		return divide.divide(other);
	}

	/**
	 *
	 * @return if interval has zero in it.
	 */
	public boolean hasZero() {
		return contains(0);
	}

	/**
	 *
	 * @return if interval represents all the real numbers R.
	 */
	public boolean isRealWhole() {
		return DoubleUtil.isEqual(low, Double.NEGATIVE_INFINITY)
				&& DoubleUtil.isEqual(high, Double.POSITIVE_INFINITY);
	}

	/**
	 *
	 * @return if interval represents all the real numbers R with limits
	 * defined in {@link IntervalConstants}.
	 */
	public boolean isWhole() {
		return DoubleUtil.isEqual(low, IntervalConstants.NEGATIVE_INFINITY)
				&& DoubleUtil.isEqual(high, IntervalConstants.POSITIVE_INFINITY);
	}

	/**
	 *
	 * @return if interval is in the form [n, n] where n is finite.
	 */
	public boolean isSingleton() {
		return MyDouble.isFinite(low) && DoubleUtil.isEqual(high, low, 1E-7);
	}

	@Override
	public int hashCode() {
		return Objects.hash(low, high);
	}

	/**
	 *
	 * @return if interval is empty.
	 */
	public boolean isEmpty() {
		return  low > high;
	}

	/**
	 *
	 * @return lower bound.
	 */
	public double getLow() {
		return low;
	}

	/**
	 *
	 * @return upper bound.
	 */
	public double getHigh() {
		return high;
	}

	/**
	 *
	 * @param other interval to check.
	 * @return if intervals are overlapping
	 */
	public boolean isOverlap(Interval other) {
		if (isEmpty() || other.isEmpty()) {
			return false;
		}
		return (low <= other.low && other.low <= high)
				|| (other.low <= low && low <= other.high);
	}

	/**
	 * Computes x mod y (x - k * y)
	 *
	 * @param other argument.
	 * @return this as result
	 */
	public Interval fmod(Interval other) {
		algebra.fmod(other);
		return this;
	}

	/**
	 *
	 * @param other to compare
	 * @return if the other interval is equal with a precision
	 */
	public boolean almostEqual(Interval other) {
		if (isUndefined() && other.isUndefined()) {
			return true;
		}

		return inverted == other.inverted
				&& DoubleUtil.isEqual(low, other.low, 1E-7)
				&& DoubleUtil.isEqual(high, other.high, 1E-7);
	}

	/**
	 *  Computes "1 / x"
	 * @return this as result.
	 */
	public Interval multiplicativeInverse() {
		return multiplicativeInverse.getResult();
	}

	void setWhole() {
		set(IntervalConstants.whole());
	}

	/**
	 *
	 * @param power of the interval
	 * @return power of the interval
	 */
	public Interval pow(double power) {
		return algebra.pow(power);
	}

	/**
	 * Sets the closed interval bounds as the other interval
	 * @param other interval to set bounds as the same.
	 */
	public void set(Interval other) {
		set(other.low, other.high);
		inverted = other.inverted;
	}

	/**
	 * Sets interval as singleton
	 * @param value to set.
	 */
	public void set(double value) {
		set(value, value);
	}

	/**
	 * Sets the closed interval bounds.
	 *
	 * @param low lower bound.
	 * @param high higher bound.
	 */
	public void set(double low, double high) {
		this.low = low;
		this.high = high;
	}

	/**
	 * Power of an interval where power is also an interval
	 * that must be a singleton, ie [n, n]
	 *
	 * @param other interval power.
	 * @return this as result.
	 */
	public Interval pow(Interval other) {
		return algebra.pow(other);
	}

	/**
	 * [a, b] -> (a, b]
	 * @return this as result
	 */
	public Interval halfOpenLeft() {
		low = RMath.next(low);
		return this;
	}

	/**
	 * [a, b] -> (a, b]
	 *
	 * @param a low limit
	 * @param b high limit
	 * @return this as result
	 */
	public Interval halfOpenLeft(double a, double b) {
		set(RMath.next(a), b);
		return this;
	}

	/**
	 * [a, b] -> [a, b)
	 * @return this as result
	 */
	public Interval halfOpenRight() {
		high = RMath.prev(high);
		return this;
	}

	/**
	 * [a, b] -> [a, b)
	 *
	 * @param a low limit
	 * @param b high limit
	 * @return this as result
	 */
	public Interval halfOpenRight(double a, double b) {
		 set(a, RMath.prev(b));
		return this;
	}

	/**
	 *
	 * @return square root of the interval.
	 */
	public Interval sqrt() {
		return root.sqrt();
	}

	/**
	 * Computes the nth root of the interval
	 * if other (=n) is a singleton
	 *
	 * @param other interval
	 * @return nth root of the interval.
	 */
	public Interval nRoot(Interval other) {
		return root.nRoot(other);
	}

	/**
	 * Computes x^(1/n)
	 *
	 * @param n the root
	 * @return nth root of the interval.
	 */
	public Interval nRoot(double n) {
		return root.nRoot(n);
	}

	/**
	 *
	 * @return cosine of the interval.
	 */
	public Interval cos() {
		return trigonometric.cos();
	}

	/**
	 *
	 * @return secant of the interval
	 */
	public Interval sec() {
		return trigonometric.sec();
	}

	/**
	 *
	 * @return 1 / sin(x)
	 */
	public Interval csc() {
		return trigonometric.csc();
	}

	/**
	 *
	 * @return cotangent of the interval
	 */
	public Interval cot() {
		return trigonometric.cot();
	}

	/**
	 * Checks if the interval is
	 * either [-∞, -∞] or [∞, ∞].
	 *
	 * @return true if infinite.
	 */
	public boolean isInfiniteSingleton() {
		return (isLowInfinite() || isHighInfinite()) && DoubleUtil.isEqual(high, low);
	}

	/**
	 *
	 * @return width of the interval.
	 */
	public double getWidth() {
		if (isEmpty()) {
			return 0;
		}
		return RMath.subHigh(high, low);
	}

	/**
	 * "Invert" the interval
	 * @return this as result.
	 */
	public Interval negative() {
		set(-high, -low);
		return this;
	}

	/**
	 *
	 * @return the bounds as array
	 */
	public double[] toArray() {
		return new double[] {low, high};
	}

	/**
	 *
	 * @return sine of the interval.
	 */
	public Interval sin() {
		return trigonometric.sin();
	}

	/**
	 *
	 * @return tangent of the interval.
	 */
	public Interval tan() {
		return trigonometric.tan();
	}

	/**
	 *
	 * @return arc sine of the interval
	 */
	public Interval asin() {
		return trigonometric.asin();
	}

	/**
	 *
	 * @return arc cosine of the interval
	 */
	public Interval acos() {
		return trigonometric.acos();
	}

	/**
	 *
	 * @return arc tangent of the interval
	 */
	public Interval atan() {
		return trigonometric.atan();
	}

	/**
	 *
	 * @return hyperbolic sine of the interval
	 */
	public Interval sinh() {
		return trigonometric.sinh();
	}

	/**
	 *
	 * @return hyperbolic cosine of the interval
	 */
	public Interval cosh() {
		return trigonometric.cosh();
	}

	/**
	 *
	 * @return hyperbolic tangent of the interval
	 */
	public Interval tanh() {
		return trigonometric.tanh();
	}

	@Override
	public Interval exp() {
		return misc.exp();
	}

	@Override
	public Interval log() {
		return misc.log();
	}

	@Override
	public Interval log10() {
		return misc.log10();
	}

	@Override
	public Interval log2() {
		return misc.log2();
	}

	@Override
	public Interval hull(Interval other) {
		return misc.hull(other);
	}

	/**
	 * Sets interval to [0].
	 */
	public void setZero() {
		set(IntervalConstants.zero());
		inverted = Inversion.NONE;
	}

	/**
	 *
	 * @return if interval is [0].
	 */
	public boolean isZero() {
		return DoubleUtil.isEqual(low, 0, 1E-6)
				&& DoubleUtil.isEqual(high, 0, 1E-6);
	}

	@Override
	public Interval intersect(Interval interval) {
		return misc.intersect(interval);
	}

	@Override
	public Interval union(Interval other) throws IntervalsNotOverlapException {
		return misc.union(other);
	}

	@Override
	public Interval difference(Interval other) throws IntervalsDifferenceException {
		return misc.difference(other);
	}

	@Override
	public Interval abs() {
		return misc.abs();
	}

	/**
	 *
	 * @param other to check
	 * @return if this interval contains the other.
	 */
	public boolean contains(Interval other) {
		return other.low > low && other.high < high;
	}

	/**
	 *
	 * @param value to check
	 * @return if value os in this interval.
	 */
	public boolean contains(double value) {
		return low <= value && value <= high;
	}

	/**
	 *
	 * @param value to check
	 * @return if value os in this interval but not bounds.
	 */
	public boolean containsExclusive(double value) {
		return low < value && value < high;
	}

	/**
	 *
	 * @param other to compare with.
	 * @return if this interval is greater than the other.
	 */
	public boolean isGreaterThan(Interval other) {
		if (isEmpty() || other == null || other.isEmpty()) {
			return false;
		}

		return high > other.high;
	}

	/**
	 * Evaluate interval with given operation and param.
	 *
	 * @param operation to execute.
	 * @param other parameter for the operation
	 * @return the result of the operation.
	 * @throws Exception in case of division by zero
	 *
	 */
	public Interval evaluate(Operation operation,
			Interval other) throws Exception {
		return evaluate.evaluate(operation, other);
	}

	public Interval evaluate(Operation operation) throws Exception {
		return evaluate.evaluate(operation);
	}

	/**
	 * Shift the interval (both low and high) by a given value.
	 * @param deltaX to shift by.
	 * @return the result inteval;
	 */
	public Interval shiftBy(double deltaX) {
		set(low + deltaX, high + deltaX);
		return this;
	}

	/**
	 *
	 * @return shorten string form to compare and debug
	 */
	public String toShortString() {
		String result = "[";
		if (!isEmpty()) {
			result += low;
			if (!isSingleton()) {
				result += ", " + high;
			}
		}

		result += "]";
		if (inverted != Inversion.NONE) {
			result += inverted == Inversion.INVERTED ? " I" : " U";
		}
		return result;
	}

	/**
	 *
	 * @return the length of the interval
	 */
	public double getLength() {
		if (isEmpty()) {
			return 0;
		}
		return Math.abs(high - low);
	}

	/**
	 *
	 * @param low to set.
	 */
	public void setLow(double low) {
		this.low = low;
	}

	/**
	 *
	 * @param high to set.
	 */
	public void setHigh(double high) {
		this.high = high;
	}

	public boolean isHalfPositiveInfinity() {
		return DoubleUtil.isEqual(0, low, 1E-5) && high == Double.POSITIVE_INFINITY;
	}

	public boolean isHalfNegativeInfinity() {
		return low == Double.NEGATIVE_INFINITY && DoubleUtil.isEqual(high, 0, 1E-5);
	}

	/**
	 *
	 * @return true if interval one or both border is infinite.
	 */
	public boolean hasInfinity() {
		return !isEmpty() && isLowInfinite() || isHighInfinite();
	}

	/**
	 *
	 * @return true if high is infinite
	 */
	public boolean isHighInfinite() {
		return high == Double.POSITIVE_INFINITY;
	}

	/**
	 *
	 * @return true if low is infinite
	 */
	public boolean isLowInfinite() {
		return low == Double.NEGATIVE_INFINITY;
	}

	/**
	 *
	 * @return if the interval is the unit one.
	 */
	public boolean isOne() {
		return low == 1 && high == 1;
	}

	/**
	 *
	 * @return if the interval is the negative unit one.
	 */
	public boolean isMinusOne() {
		return low == -1 && high == -1;
	}

	public boolean isFinite() {
		return MyDouble.isFinite(low) && MyDouble.isFinite(high);
	}

	/**
	 *
	 * @return true if any of the bounds is infinite but not both.
	 */
	public boolean isSemiInfinite() {
		return (isLowInfinite() && !isHighInfinite())
				|| (!isLowInfinite() && isHighInfinite());
	}

	public boolean isPositiveInfinity() {
		return low == Double.POSITIVE_INFINITY && high == low;
	}
	public boolean isNegativeInfinity() {
		return low == Double.NEGATIVE_INFINITY && high == low;
	}

	/**
	 *
	 * @return true if interval is undefined (division by zero).
	 */
	public boolean isUndefined() {
		return Double.isNaN(low) && Double.isNaN(high);
	}

	/**
	 * Sets interval undefined (result of division by zero).
	 */
	public void setUndefined() {
		set(Double.NaN, Double.NaN);
		inverted = Inversion.NONE;
	}

	/**
	 *
	 * @param other interval to check
	 * @return true if the bounds of the intervals has the same sign
	 * respectively, ie low (or high) < 0 then other.low (or other.high)< 0 or reverse.
	 */
	public boolean isSignEquals(Interval other) {
		return DoubleUtil.isEqual(Math.abs(low), other.low)
				&& DoubleUtil.isEqual(Math.abs(high), other.high);
	}

	/**
	 *
	 * @return if both bounds are positive.
	 */
	public boolean isPositive() {
		return !isEmpty() && low > 0;
	}

	/**
	 *
	 * @return if both bounds are >= 0.
	 */
	public boolean isNatural() {
		return !isEmpty() && low > 0;
	}

	/**
	 *
	 * @return if both bounds are negative.
	 */
	public boolean isNegative() {
		return !isEmpty() && high < 0;
	}

	/**
	 *
	 * @return if interval is [n, n] where n is an integer .
	 */
	public boolean isSingletonInteger() {
		return isSingleton() && DoubleUtil.isEqual(low, Math.round(low));
	}

	/**
	 * Sets interval [low, high] inverted. This really means:
	 * [-∞, low] union [high, ∞]
	 * Note: zero interval cannot be inverted.
	 */
	public void setInverted() {
		if (true || !isZero()) {
			inverted = Inversion.INVERTED;
		}
	}

	/**
	 * Inverts interval
	 * @return this
	 */
	public Interval invert() {
		inverted = isInverted()
				? Inversion.NONE
				: Inversion.INVERTED;

		return this;
	}

	/**
	 * Clears interval as inverted.
	 * @return this
	 */
	public Interval uninvert() {
		if (isInverted()) {
			inverted = Inversion.UNINVERTED;
		}
		return this;
	}

	/**
	 *
	 * @return if interval is inverted,
	 * ie equals [-∞, low] union [high, ∞].
	 */
	public boolean isInverted() {
		return inverted == Inversion.INVERTED;
	}

	/**
	 *
	 * @return if interval was uninverted at some point,
	 */
	public boolean isUninverted() {
		return inverted == Inversion.UNINVERTED;
	}

	/**
	 * Resets inversion as it never was touched.
	 */
	public void resetInversion() {
		inverted = Inversion.NONE;
	}
}