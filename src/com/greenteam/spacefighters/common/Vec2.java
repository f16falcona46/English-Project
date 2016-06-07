package com.greenteam.spacefighters.common;

public class Vec2 {
	public static final Vec2 ZERO = new Vec2(0, 0);
	public static final Vec2 PINFINITY = new Vec2(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
	public static final Vec2 NINFINITY = new Vec2(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
	private double x;
	private double y;
	
	public Vec2() {
		x = 0;
		y = 0;
	}
	
	public Vec2(double xCoord, double yCoord) {
		x = xCoord;
		y = yCoord;
	}
	
	public Vec2(double[] coord) {
		x = coord[0];
		y = coord[1];
	}
	
	public Vec2(Vec2 v) {
		if (v != null) {
			x = v.x;
			y = v.y;
		}
	}
	
	public double[] toDoubleArray() {
		return new double[]{x, y};
	}
	
	public void set(double xVal, double yVal) {
		x = xVal;
		y = yVal;
	}
	
	public void set(double[] coord) {
		x = coord[0];
		y = coord[1];
	}
	
	public double getX() {
		return x;
	}

	public void setX(double val) {
		x = val;
	}

	public double getY() {
		return y;
	}

	public void setY(double val) {
		y = val;
	}
	
	public String toString() {
		return x + ", " + y;
	}

	public boolean isZeroVector() {
	    return (x == 0 && y == 0);
	}
	
	public Vec2 add(Vec2 v) {
		return new Vec2(x + v.x, y + v.y);
	}
	
	public Vec2 subtract(Vec2 v) {
		return new Vec2(x - v.x, y - v.y);
	}
	
	public Vec2 scale(double s) {
		return new Vec2(x * s, y * s);
	}
	
	public Vec2 negate() {
	    return new Vec2(-x, -y);
	}
	
	public double magnitude2() {
	    return x * x + y * y;
	}

	public double magnitude() {
	    return Math.sqrt(magnitude2());
	}

	public Vec2 normalize() {
	    double mag = magnitude();
	    return new Vec2(x / mag, y / mag);
	}

	public double distance2(Vec2 v) {
		double dx = x - v.x;
		double dy = y - v.y;
	    return dx * dx + dy * dy;
	}

	public double distance(Vec2 v) {
	    return Math.sqrt(distance2(v));
	}
	
	public Vec2 midpoint(Vec2 v) {
	    return this.add(v).scale(0.5);
	}
	
	public double dotProduct(Vec2 v) {
	    return x * v.x + y * v.y;
	}
	
	public double angle(Vec2 v) {
		return Math.acos(this.normalize().dotProduct(v.normalize()));
	}
	
	public double angle() {
		return Math.atan2(x, y);
	}

	public Vec2 reflect(Vec2 v) {
		return this.subtract(v.scale(2 * this.dotProduct(v)));
	}

	public Vec2 rotate(Vec2 center, double radians) {
	    
	    return new Vec2(
	    		(Math.cos(radians) * (x - center.x) - Math.sin(radians) * (y - center.y) + center.x),
	    		(Math.sin(radians) * (x - center.x) + Math.cos(radians) * (y - center.y) + center.y)
	    		);
	}
	
	public Vec2 round() {
	    return new Vec2(Math.round(x), Math.round(y));
	}

	public Vec2 ceil(Vec2 vec) {
	    return new Vec2(Math.ceil(x), Math.ceil(y));
	}

	public Vec2 floor() {
	    return new Vec2(Math.floor(x), Math.floor(y));
	}
	
	public Vec2 min(Vec2 v) {
	    return new Vec2(Math.min(x, v.x), Math.min(y, v.y));
	}

	public Vec2 max(Vec2 v) {
	    return new Vec2(Math.max(x, v.x), Math.max(y, v.y));
	}

	public Vec2 clamp(double min, double max) {
		return new Vec2(Math.min(Math.max(x, min), max), Math.min(Math.max(y, min), max));
	}

	public Vec2 clampVec(Vec2 min, Vec2 max) {
		return this.min(max).max(min);
	}
	
	public Vec2 multiply(Vec2 vec) {
		return new Vec2(x * vec.x, y * vec.y);
	}
	
	public Vec2 setMag(double s) {
		return this.normalize().scale(s);
	}
	
	public Vec2 getCWPerp() {
		return new Vec2(-y, x);
	}
	
	public Vec2 getCCWPerp() {
		return new Vec2(y, -x);
		
	}
	
	public boolean withinBounds(Vec2 min, Vec2 max) {
		return (this.x > min.x &&
				this.y > min.y &&
				this.x < max.x &&
				this.y < max.y);
	}
	
	public static Vec2 fromAngle(double angle) {
	    return new Vec2(Math.cos(angle), Math.sin(angle));
	}
	
	public static Vec2 random() {
		return new Vec2(Math.random(), Math.random());
	}
	
	public static Vec2 random(double max) {
		return new Vec2(max * Math.random(), max * Math.random());
	}
	
	public static Vec2 random(double xMax, double yMax) {
		return new Vec2(xMax * Math.random(), yMax * Math.random());
	}
	
}
