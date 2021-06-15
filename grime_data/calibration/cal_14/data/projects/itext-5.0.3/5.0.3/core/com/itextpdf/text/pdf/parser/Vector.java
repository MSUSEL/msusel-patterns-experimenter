/**
 * The MIT License (MIT)
 *
 * MSUSEL Arc Framework
 * Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory and Idaho State University, Informatics and
 * Computer Science, Empirical Software Engineering Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.itextpdf.text.pdf.parser;

/**
 * Represents a vector (i.e. a point in space).  This class is completely
 * unrelated to the {@link java.util.Vector} class in the standard JRE.
 * <br><br>
 * For many PDF related operations, the z coordinate is specified as 1
 * This is to support the coordinate transformation calculations.  If it
 * helps, just think of all PDF drawing operations as occurring in a single plane
 * with z=1.
 */
public class Vector {
    /** index of the X coordinate */
    public static final int I1 = 0;
    /** index of the Y coordinate */
    public static final int I2 = 1;
    /** index of the Z coordinate */
    public static final int I3 = 2;
    
    /** the values inside the vector */
    private final float[] vals = new float[]{
            0,0,0
    };

    /**
     * Creates a new Vector
     * @param x the X coordinate
     * @param y the Y coordinate
     * @param z the Z coordinate
     */
    public Vector(float x, float y, float z) {
        vals[I1] = x;
        vals[I2] = y;
        vals[I3] = z;
    }
    
    /**
     * Gets the value from a coordinate of the vector
     * @param index the index of the value to get (I1, I2 or I3)
     * @return a coordinate value
     */
    public float get(int index){
        return vals[index];
    }
    
    /**
     * Computes the cross product of this vector and the specified matrix
     * @param by the matrix to cross this vector with
     * @return the result of the cross product
     */
    public Vector cross(Matrix by){
        
        float x = vals[I1]*by.get(Matrix.I11) + vals[I2]*by.get(Matrix.I21) + vals[I3]*by.get(Matrix.I31);
        float y = vals[I1]*by.get(Matrix.I12) + vals[I2]*by.get(Matrix.I22) + vals[I3]*by.get(Matrix.I32);
        float z = vals[I1]*by.get(Matrix.I13) + vals[I2]*by.get(Matrix.I23) + vals[I3]*by.get(Matrix.I33);
        
        return new Vector(x, y, z);
    }
    
    /**
     * Computes the difference between this vector and the specified vector
     * @param v the vector to subtract from this one
     * @return the results of the subtraction
     */
    public Vector subtract(Vector v){
        float x = vals[I1] - v.vals[I1];
        float y = vals[I2] - v.vals[I2];
        float z = vals[I3] - v.vals[I3];
        
        return new Vector(x, y, z);
    }
    
    /**
     * Computes the cross product of this vector and the specified vector
     * @param with the vector to cross this vector with
     * @return the cross product
     */
    public Vector cross(Vector with){
        float x = vals[I2]*with.vals[I3] - vals[I3]*with.vals[I2];
        float y = vals[I3]*with.vals[I1] - vals[I1]*with.vals[I3];
        float z = vals[I1]*with.vals[I2] - vals[I2]*with.vals[I1];
        
        return new Vector(x, y, z);
    }
    
    /**
     * Normalizes the vector (i.e. returns the unit vector in the same orientation as this vector)
     * @return the unit vector
     * @since 5.0.1
     */
    public Vector normalize(){
        float l = this.length();
        float x = vals[I1]/l;
        float y = vals[I2]/l;
        float z = vals[I3]/l;
        return new Vector(x, y, z);
    }

    /**
     * Multiplies the vector by a scalar
     * @param by the scalar to multiply by
     * @return the result of the scalar multiplication
     * @since 5.0.1
     */
    public Vector multiply(float by){
        float x = vals[I1] * by;
        float y = vals[I2] * by;
        float z = vals[I3] * by;
        return new Vector(x, y, z);
    }
    
    /**
     * Computes the dot product of this vector with the specified vector
     * @param with the vector to dot product this vector with
     * @return the dot product
     */
    public float dot(Vector with){
        return vals[I1]*with.vals[I1] + vals[I2]*with.vals[I2] + vals[I3]*with.vals[I3];
    }
    
    /**
     * Computes the length of this vector
     * <br>
     * <b>Note:</b> If you are working with raw vectors from PDF, be careful - 
     * the Z axis will generally be set to 1.  If you want to compute the
     * length of a vector, subtract it from the origin first (this will set
     * the Z axis to 0).
     * <br>
     * For example: 
     * <code>aVector.subtract(originVector).length();</code>
     *  
     * @return the length of this vector
     */
    public float length(){
        return (float)Math.sqrt(lengthSquared());
    }
    
    /**
     * Computes the length squared of this vector.
     * 
     * The square of the length is less expensive to compute, and is often
     * useful without taking the square root.
     * <br><br>
     * <b>Note:</b> See the important note under {@link Vector#length()}
     * 
     * @return the square of the length of the vector
     */
    public float lengthSquared(){
        return vals[I1]*vals[I1] + vals[I2]*vals[I2] + vals[I3]*vals[I3];
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return vals[I1]+","+vals[I2]+","+vals[I3];
    }
    
    /**
     * @since 5.0.1
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        Vector rhs = (Vector)obj;
        return rhs.vals[I1] == vals[I1] && rhs.vals[I2] == vals[I2] && rhs.vals[I3] == vals[I3];
    }
}
