package game.entities.components;

import java.awt.geom.Point2D;

import sun.security.util.Length;

/**
 * Mathematical vector class. Adapted from several sources, including Java3D's version
 * and libgdx's version. None of them did exactly what I want, so I made my own Vector.
 * With blackjack! And hookers!
 * 
 * @author Barnes - 9/5/13
 * 
 */

public class Vec2D extends Point2D.Double {
	
	/**
	 * Default constructor, generates a vector with length zero at 0,0
	 */
	public Vec2D() {
		super();
	}

	/**
	 * Generates a vector at the given location.
	 * 
	 * @param x - x coordinate
	 * @param y - y coordinate
	 */
	public Vec2D(double x, double y) {
		super(x, y);
	}

	/**
	 * Creates a deep copy of the passed vector.
	 * 
	 * @param copyVec - vector to be copied.
	 */
	public Vec2D(Vec2D copyVec) {
		super(copyVec.x, copyVec.y);
	}

	/**
	 * @return the length of the vector
	 */
	public double getLength() {
		return Math.sqrt((x * x) + (y * y));
	}

	/**
	 * Makes the vector have length 1.
	 * 
	 * @return - the new unit vector.
	 */
	public Vec2D normalize() {
		if (getLength() != 0) {
			x = x / getLength();
			y = y / getLength();
		}
		return this;
	}

	/**
	 * Multiplies this vector by the passed scalar quantity. This method actually changes the value of this Vec2D and
	 * also returns a vector with the same new length, in case you need it immediately.
	 * 
	 * @param scalar - nonvector quantity.
	 * @return - this Vec2D in its new multiplied state.
	 */
	public Vec2D multiply(double scalar) {
		x = scalar * x;
		y = scalar * y;
		return this;
	}

	/**
	 * Adds this vector to the passed vector. This method actually changes the value of this Vec2D and also returns it in case
	 * you need it immediately.
	 * 
	 * @param v - Vector to add to this vector.
	 * @return - the vector in its new added state.
	 */
	public Vec2D add(Vec2D v) {
		x += v.x;
		y += v.y;
		return this;
	}

	/**
	 * Adds this vector to the passed vector. This method actually changes the value of this Vec2D and also returns it in case
	 * you need it immediately.
	 * 
	 * @param v - vector to subtract from this vector
	 * @return - this vector, post subtracting
	 */
	public Vec2D subtract(Vec2D v) {
		x -= v.x;
		y -= v.y;
		return this;
	}
	
	/**
	 * NC for No Change- this method doesn't alter this Vec2D at all, merely returns a new Vector with the
	 * specified operation performed.
	 * 
	 * @param scalar - nonvector quantity.
	 * @return - a new vector equal to scalar * this
	 */
	public Vec2D multiplyNC(double scalar) {
		return new Vec2D(x*scalar, y*scalar);
	}

	/**
	 * NC for No Change- this method doesn't alter this Vec2D at all, merely returns a new Vector with the
	 * specified operation performed.
	 * 
	 * @param v - Vector to add to this vector.
	 * @return - a new vector that is equal to this + v
	 */
	public Vec2D addNC(Vec2D v) {
		return new Vec2D(x+v.x, y + v.y);
	}

	/**
	 * NC for No Change- this method doesn't alter this Vec2D at all, merely returns a new Vector with the
	 * specified operation performed.
	 * 
	 * @param v - the vector to subtract from this one
	 * @return the new vector, reflecting this - v
	 */
	public Vec2D subtractNC(Vec2D v) {
		return new Vec2D(x-v.x, y-v.y);
	}
	
    /**
     * 
     * @param eO
     *            ellipse origin/centre
     * @param eR
     *            ellipse radii
     * @return a unit normal vector
     */
    public Vec2D getNormal(Point2D eO, Point2D eR) {
        Vec2D p = this.subtractNC(new Vec2D(eO.getX(), eO.getY()));

        double xr2 = eR.getX() * eR.getX();
        double yr2 = eR.getY() * eR.getY();
        return new Vec2D(p.x / xr2, p.y / yr2).normalize();
    }
    
    public Vec2D alignToCompass(){
    	if(getX() == 0 && getY() == 0)
    		return null;
    	double angle = (1.)* Math.toDegrees(Math.atan2(y, x));
    	if(angle < 0)
			angle += 360;
    	System.out.println("pre " + angle);
		if(angle >= 337.5 || angle < 22.5 )
			angle = 0;
		else if(angle >= 22.5 && angle < 67.5  )
			angle = 45;
		else if(angle >= 67.5 && angle < 105 )
			angle = 90;
		else if(angle >= 105 && angle < 165)
			angle = 135;
		else if(angle >=  165 && angle < 195)
			angle = 180;
		else if(angle >= 202.5 && angle < 247.50)
			angle = 225;
		else if(angle >= 247.5 && angle < 292.5)
			angle = 270;
		else if(angle >= 292.5 && angle < 337.5 )
			angle = 315;
    	System.out.println("post " + angle);
		double length = getLength();
     	super.setLocation(length * Math.cos(angle), length * Math.sin(angle));
    	
    	return this;
    	
    }
    

}
