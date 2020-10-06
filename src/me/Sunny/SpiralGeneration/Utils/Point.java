package me.Sunny.SpiralGeneration.Utils;

/**
 * Point utility class.
 * @author Daniel Dovgun
 * @version 9/24/2020
 */
public final class Point 
{
	private final int x;
	private final int y;
	
	// Constant points that are useful for calculations.
	public static final Point ORIGIN = new Point(0,0);
	public static final Point UP = new Point(0,1);
	public static final Point RIGHT = new Point(1,0);
	public static final Point DOWN = new Point(0,-1);
	public static final Point LEFT = new Point(-1,0);
	
	/**
	 * Point constructor.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 */
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * X coordinate getter method.
	 * @return X coordinate of the point.
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Y coordinate getter method.
	 * @return Y coordinate of the point.
	 */
	public int getY() {
		return y;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Point))
			return false;
		else {
			Point other = (Point) o;
			return ( (x == other.getX()) && (y ==  other.getY()) );
		}
	}
	
	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}
}
