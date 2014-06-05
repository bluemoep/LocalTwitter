package edu.oldenburg.it.bluemoep;

public class Boundaries {

	private double north, east, south, west;
	
	public Boundaries(double north, double east, double south, double west) {
		this.north = north;
		this.east = east;
		this.south = south;
		this.west = west;
	}

	public double getNorth() {
		return north;
	}

	public double getEast() {
		return east;
	}

	public double getSouth() {
		return south;
	}

	public double getWest() {
		return west;
	}

	public Boundaries setNorth(double north) {
		return new Boundaries(north, east, south, west);
	}

	public Boundaries setEast(double east) {
		return new Boundaries(north, east, south, west);
	}

	public Boundaries setSouth(double south) {
		return new Boundaries(north, east, south, west);
	}

	public Boundaries setWest(double west) {
		return new Boundaries(north, east, south, west);
	}
	
}
