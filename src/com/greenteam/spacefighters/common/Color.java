package com.greenteam.spacefighters.common;

public class Color {
	
	public static final Color WHITE =	new Color(1.0f, 1.0f, 1.0f, 1.0f);
	public static final Color BLACK =	new Color(0.0f, 0.0f, 0.0f, 1.0f);
	public static final Color RED =		new Color(1.0f, 0.0f, 0.0f, 1.0f);
	public static final Color GREEN =	new Color(0.0f, 1.0f, 0.0f, 1.0f);
	public static final Color BLUE =	new Color(0.0f, 0.0f, 1.0f, 1.0f);
	public static final Color PURPLE =	new Color(1.0f, 0.0f, 1.0f, 1.0f);
	public static final Color YELLOW =	new Color(1.0f, 1.0f, 0.0f, 1.0f);
	public static final Color CYAN =	new Color(0.0f, 1.0f, 1.0f, 1.0f);

	//TODO: add more colors
	private float r;
	private float g;
	private float b;
	private float a;
	
	public Color(float r, float g, float b){
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = 1;
	}

	public Color(float r, float g, float b, float a){
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
	public Color(float[] vals){
		this.r = vals[0];
		this.g = vals[1];
		this.b = vals[2];
		this.a = vals[3];
	}
	public Color(int r, int g, int b){
		this.r = r / (float)255;
		this.g = g / (float)255;
		this.b = b / (float)255;
		this.a = 1;
	}

	public Color(int r, int g, int b, int a){
		this.r = r / (float)255;
		this.g = g / (float)255;
		this.b = b / (float)255;
		this.a = a / (float)255;
	}
	
	public Color(int[] vals){
		this.r = vals[0] / (float)255;
		this.g = vals[1] / (float)255;
		this.b = vals[2] / (float)255;
		this.a = vals[3] / (float)255;
	}
	
	public Color(double[] vals){
		this.r = (float) vals[0];
		this.g = (float) vals[1];
		this.b = (float) vals[2];
		this.a = (float) vals[3];
	}
	
	public Color(Color c) {
		if (c != null) {
			this.r = c.r;
			this.g = c.g;
			this.b = c.b;
			this.a = c.a;
		}
	}
	
	public float[] toFloatArray() {
		return new float[]{r, g, b, a};
	}
	
	public int[] toIntArray() {
		return new int[]{(int)(r * 255), (int)(g * 255), (int)(b * 255), (int)(a * 255)};
	}

	public float getRed() {
		return r;
	}

	public float getGreen() {
		return g;
	}

	public float getBlue() {
		return b;
	}

	public float getAlpha() {
		return a;
	}

	public void setRed(float r) {
		this.r = r;
	}

	public void setGreen(float g) {
		this.g = g;
	}

	public void setBlue(float b) {
		this.b = b;
	}

	public void setAlpha(float a) {
		this.a = a;
	}
	
	public void set(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
	public void set(float r, float g, float b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public int getRGBA() {
		int R = Math.round(r * 255);
		int G = Math.round(g * 255);
		int B = Math.round(b * 255);
		int A = Math.round(a * 255);

		A = (A << 24) & 0xFF000000;
		R = (R << 16) & 0x00FF0000;
		G = (G <<  8) & 0x0000FF00;
		B = (B <<  0) & 0x000000FF;

		return A | R | G | B;
	}

	public void scale(float k) {
		set(
				k * r,
				k * g,
				k * b,
				k * a
				);
	}

	public void scaleOpaque(float k) {
		set(
				k * r,
				k * g,
				k * b
				);
	}

	public void add(Color c) {
		set(
				r + c.r,
				g + c.g,
				b + c.b,
				a + c.a
				);
	}

	public void addOpaque(Color c) {
		set(
				r + c.r,
				g + c.g,
				b + c.b
				);
	}

	public void multiply(Color c) {
		set(
				r * c.r,
				g * c.g,
				b * c.b,
				a * c.a
				);
	}

	public void multiplyOpaque(Color c) {
		set(
				r * c.r,
				g * c.g,
				b * c.b
				);
	}
	
	public java.awt.Color toAWTColor() {
		return new java.awt.Color(getRGBA(), true);
	}

}
