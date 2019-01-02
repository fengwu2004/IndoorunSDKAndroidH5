package com.yellfun.indoorunh5lib.bean;

public class Position implements Comparable<Position> {
	private double x;
	private double y;
	
	public Position() {
		super();
	}
	public Position(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	@Override
	public int hashCode() {
		return (int) x + (int) y;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Position))
			return false;
		final Position pos = (Position) obj;
		if (this.x != pos.x || this.y != pos.y)
			return false;
		return true;
	}

	@Override
	public int compareTo(Position o) {
		if (this.getX() < o.getX()) {
			return -1;
		} else if (this.getX() > o.getX()) {
			return 1;
		} else if (this.getY() < o.getY()) {
			return -1;
		} else if (this.getY() > o.getY()) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public String toString() {
		return "\"x\":"+(int)x+",\"y\":"+(int)y;
	}
	
}
