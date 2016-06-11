package com.greenteam.spacefighters.common;

public class BoundRect {
	private static final double DELTA = -0.8;
	
	private double midX;
	private double midY;
	private double halfWidth;
	private double halfHeight;
	
	public BoundRect(double midX, double midY, double width, double height) {
		this.midX = midX;
		this.midY = midY;
		this.halfWidth = width/2;
		this.halfHeight = height/2;
	}
	
	public void setX(double x) {
		this.midX = x;
	}
	
	public double getX() {
		return this.midX;
	}
	
	public void setY(double y) {
		this.midY = y;
	}
	
	public double getY() {
		return this.midY;
	}
	
	public void setWidth(double width) {
		this.halfWidth = width/2;
	}
	
	public double getWidth() {
		return this.halfWidth*2;
	}
	
	public void setHeight(double height) {
		this.halfHeight = height/2;
	}
	
	public double getHeight() {
		return this.halfHeight*2;
	}
	
	public void set(double x, double y, double width, double height) {
		this.setX(x);
		this.setY(y);
		this.setWidth(width);
		this.setHeight(height);
	}
	
	public boolean intersects(BoundRect other) {
		if (Math.abs(other.midX - this.midX) < (this.halfWidth + other.halfWidth)) {
			if (Math.abs(other.midY - this.midY) < (this.halfHeight + other.halfHeight)) return true;
		}
		return false;
	}
	
	public RectCollisionSide whichSideIntersected(BoundRect other) {
		if (!intersects(other)) return RectCollisionSide.NONE;
		double w = this.halfWidth + other.halfWidth;
		double h = this.halfHeight + other.halfHeight;
		double dx = this.midX - other.midX;
		double dy = this.midY - other.midY;
		double wy = w*dy;
		double hx = h*dx;
		if (wy > hx) {
			if (wy + DELTA*wy > -hx) {
				return RectCollisionSide.TOP;
			}
			else {
				return RectCollisionSide.RIGHT;
			}
		}
		else {
			if (wy - DELTA*wy > -hx) {
				return RectCollisionSide.LEFT;
			}
			else {
				return RectCollisionSide.BOTTOM;
			}
		}
	}
	
	@Override
	public String toString() {
		return String.format("com.greenteam.spacefighters.common.BoundRect [midX=%f,midY=%f,halfWidth=%f,halfHeight=%f]",midX,midY,halfWidth,halfHeight);
	}
}
