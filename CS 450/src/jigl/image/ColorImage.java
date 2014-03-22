/* This file is part of the JIGL Java Image and Graphics Library.
 * 
 * Copyright 1999 Brigham Young University.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * A copy of the GNU Lesser General Public License is contained in
 * the 'licenses' directory accompanying this distribution.
 */
package jigl.image;

import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;

/**
 * A color image is a set of three GrayImage planes.
 */
public class ColorImage implements Image {

	/** Set of 3 GrayImages */
	protected GrayImage[] planes = new GrayImage[3];

	/** Cartesian width */
	protected int X;

	/** Cartesian height */
	protected int Y;

	/** RGB Color Model */
	public static final int RGB = 0;
	/** CMY Color Model */
	public static final int CMY = 1;
	/** YIQ Color Model */
	public static final int YIQ = 2;
	/** HSV Color Model */
	public static final int HSV = 3;
	/** HLS Color Model */
	public static final int HLS = 4;
	/** Current Color Model; Default is RGB */
	protected int colorModel = RGB;

	/*
	constructors
	*/

	/** Creates an empty two dimensional ColorImage with a height and width of zero */
	public ColorImage() {
		X = 0;
		Y = 0;
		planes[0] = null;
		planes[1] = null;
		planes[2] = null;
	}

	/** Creates a two dimensional ColorImage with a height and width of x and y repectively */
	public ColorImage(int x, int y) {
		X = x;
		Y = y;
		planes[0] = new GrayImage(X, Y);
		planes[1] = new GrayImage(X, Y);
		planes[2] = new GrayImage(X, Y);
	}

	/** Creates a two dimensional ColorImage from <i>img</i>. */
	public ColorImage(ColorImage img) {
		X = img.X();
		Y = img.Y();
		GrayImage r, g, b;
		r = new GrayImage(img.plane(0));
		g = new GrayImage(img.plane(1));
		b = new GrayImage(img.plane(2));
		planes[0] = r;
		planes[1] = g;
		planes[2] = b;
	}

	/** Creates a two dimensional GrayImage from the standard java.awt.Image */
	public ColorImage(java.awt.Image img) {
		int w = img.getWidth(jigl.internal.DummyObserver.dummy);
		int h = img.getHeight(jigl.internal.DummyObserver.dummy);
		X = w;
		Y = h;
		planes[0] = new GrayImage(X, Y);
		planes[1] = new GrayImage(X, Y);
		planes[2] = new GrayImage(X, Y);
		InitFromImage(img, 0, 0, w, h);
	}

	/**
	 * Makes a deep copy of this image
	 * 
	 *
	 * @return a deep copy of ColorImage
	 */
	public Image copy() {
		ColorImage c = new ColorImage(X, Y);

		c.setColorModel(colorModel);

		c.planes[0] = (GrayImage) planes[0].copy();
		c.planes[1] = (GrayImage) planes[1].copy();
		c.planes[2] = (GrayImage) planes[2].copy();

		/*		for(int y = 0; y < Y; y++) {
					for(int x = 0; x < X; x++) {
						c.planes[0].set(x,y,planes[0].get(x,y));
						c.planes[1].set(x,y,planes[1].get(x,y));
						c.planes[2].set(x,y,planes[2].get(x,y));
					}
				}
				*/
		return c;
	}

	/**
	 * Returns the width (maximum X value)
	 * 
	 *
	 */
	public final int X() {
		return X;
	}

	/**
	 * Returns the height (maximum Y value)
	 * 
	 *
	 */
	public final int Y() {
		return Y;
	}

	/**
	 * Returns the color model. <DT>
	 * <DL>
	 * <DL>
	 * <RGB = 0</DT> <DT>CMY = 1</DT> <DT>YIQ = 2</DT> <DT>HSV = 3</DT> <DT>HLS = 4</DT> <DT>default
	 * = RGB</DT> <DT></DT></DL></DL>
	 * 
	 *
	 */
	public final int getColorModel() {
		return colorModel;
	}

	/**
	 * sets the color model.
	 * 
	 * @param cm ColorModel <DT>
	 *            <DL>
	 *            <DL>
	 *            <RGB = 0</DT> <DT>CMY = 1</DT> <DT>YIQ = 2</DT> <DT>HSV = 3</DT> <DT>HLS = 4</DT>
	 *            <DT>default = RGB</DT> <DT></DT></DL></DL>
	 */
	public final void setColorModel(int cm) {
		colorModel = cm;
	}

	/**
	 * Returns the plane of this image.
	 * <P>
	 * If this were an RGB image, plane(0) would return the red plane.
	 * 
	 * @param planeIndex the plane of this image
	 * @return a shallow copy
	 */
	public final GrayImage plane(int planeIndex) {
		return planes[planeIndex];
	}

	/**
	 * Set the plane to a GrayImage pl
	 * <P>
	 * 
	 * @param planeIndex the plane of this image
	 * @param pl GrayImage to set the plane to
	 */
	public void setPlane(int planeIndex, GrayImage pl) {
		planes[planeIndex] = pl;
	}

	/**
	 * initializes plane data from a Java image. Used by the java image constructor.
	 */
	public void InitFromImage(java.awt.Image img, int x, int y, int w, int h) {
		int pixels[] = new int[w * h];
		PixelGrabber pg = new PixelGrabber(img, x, y, w, h, pixels, 0, w);
		try {
			pg.grabPixels();
		} catch (InterruptedException e) {
			System.err.println("interrupted waiting for pixels!");
			return;
		}
		if ((pg.getStatus() & ImageObserver.ABORT) != 0) {
			System.err.println("image fetch aborted or errored");
			return;
		}

		// convert from grabbed pixels
		int red = 0;
		int green = 0;
		int blue = 0;
		int index = 0;
		for (int iy = 0; iy < Y; iy++) {
			for (int ix = 0; ix < X; ix++) {
				red = 0x0FF & pixels[index] >> 16;
				green = 0x0FF & pixels[index] >> 8;
				blue = 0x0FF & pixels[index];
				planes[0].set(ix, iy, (short) red);
				planes[1].set(ix, iy, (short) green);
				planes[2].set(ix, iy, (short) blue);
				index++;
			}
		}
	}

	/**
	 * Returns the pixel value at the given x, y value as a triplet
	 * 
	 * @param x the X coordinate
	 * @param y the Y coordinate
	 * @return three element array of integers
	 */
	public final int[] get(int x, int y) {
		int[] color = new int[3];
		color[0] = planes[0].get(x, y);
		color[1] = planes[1].get(x, y);
		color[2] = planes[2].get(x, y);
		return color;
	}

	/**
	 * Sets the pixel value at x, y to a value as a triplet
	 * 
	 * @param x the X coordinate
	 * @param y the Y coordinate
	 * @param v array of three integers holding the values for the set
	 */
	public final void set(int x, int y, int[] v) {
		planes[0].set(x, y, v[0]);
		planes[1].set(x, y, v[1]);
		planes[2].set(x, y, v[2]);
	}

	/**
	 * Makes a copy of this image with a buffer so the resulting image has a width w and height h
	 * 
	 * @param w width of buffered image
	 * @param h height of buffered image
	 * @param color array of 3 integers that is the default color to buffer with
	 * @return a deep copy of ColorImage
	 */
	public ColorImage addbuffer(int w, int h, int[] color) {
		ColorImage g = new ColorImage(w, h);

		g.setPlane(0, planes[0].addbuffer(w, h, color[0]));
		g.setPlane(1, planes[1].addbuffer(w, h, color[1]));
		g.setPlane(2, planes[2].addbuffer(w, h, color[2]));

		return g;
	}

	/**
	 * Makes a copy of this image with a buffer so the resulting image has a width w and height h
	 * 
	 * @param w width of buffered image
	 * @param h height of buffered image
	 * @param xoff x offset of this image in the buffered image
	 * @param yoff y offset of this image in the buffered image
	 * @param color array of 3 integers that is the default color to buffer with
	 * @return a deep copy of ColorImage
	 */
	public ColorImage addbuffer(int w, int h, int xoff, int yoff, int[] color) {
		ColorImage g = new ColorImage(w, h);

		g.setPlane(0, planes[0].addbuffer(w, h, xoff, yoff, color[0]));
		g.setPlane(1, planes[1].addbuffer(w, h, xoff, yoff, color[1]));
		g.setPlane(2, planes[2].addbuffer(w, h, xoff, yoff, color[2]));

		return g;
	}

	/**
	 * Adds a triplet to a single pixel
	 * 
	 * @param x X-coordinate
	 * @param y Y-coordinate
	 * @param value triplet to add to the pixel
	 */
	public final void add(int x, int y, int[] value) {
		planes[0].add(x, y, value[0]);
		planes[1].add(x, y, value[1]);
		planes[2].add(x, y, value[2]);
	}

	/**
	 * Adds a triplet to a single pixel
	 * 
	 * @param x X-coordinate
	 * @param y Y-coordinate
	 * @param val0 value to add to the pixel in the first plane
	 * @param val1 value to add to the pixel in the second plane
	 * @param val2 value to add to the pixel in the third plane
	 */
	public final void add(int x, int y, int val0, int val1, int val2) {
		planes[0].add(x, y, val0);
		planes[1].add(x, y, val1);
		planes[2].add(x, y, val2);
	}

	/**
	 * Subtracts a triplet from a single pixel
	 * 
	 * @param x X-coordinate
	 * @param y Y-coordinate
	 * @param value triplet to subtract from the pixel
	 */
	public final void subtract(int x, int y, int[] value) {
		planes[0].subtract(x, y, value[0]);
		planes[1].subtract(x, y, value[1]);
		planes[2].subtract(x, y, value[2]);
	}

	/**
	 * Subtracts a triplet from a single pixel
	 * 
	 * @param x X-coordinate
	 * @param y Y-coordinate
	 * @param val0 value to subtract from the pixel in the first plane
	 * @param val1 value to subtract from the pixel in the second plane
	 * @param val2 value to subtract from the pixel in the third plane
	 */
	public final void subtract(int x, int y, int val0, int val1, int val2) {
		planes[0].subtract(x, y, val0);
		planes[1].subtract(x, y, val1);
		planes[2].subtract(x, y, val2);
	}

	/**
	 * Multiplies a triplet with a single pixel
	 * 
	 * @param x X-coordinate
	 * @param y Y-coordinate
	 * @param value triplet to multiply the pixel by
	 */
	public final void multiply(int x, int y, int[] value) {
		planes[0].multiply(x, y, value[0]);
		planes[1].multiply(x, y, value[1]);
		planes[2].multiply(x, y, value[2]);
	}

	/**
	 * Multiplies a triplet with a single pixel
	 * 
	 * @param x X-coordinate
	 * @param y Y-coordinate
	 * @param val0 value to multiply the pixel by in the first plane
	 * @param val1 value to multiply the pixel by in the second plane
	 * @param val2 value to multiply the pixel by in the third plane
	 */

	public final void multiply(int x, int y, int val0, int val1, int val2) {
		planes[0].multiply(x, y, val0);
		planes[1].multiply(x, y, val1);
		planes[2].multiply(x, y, val2);
	}

	/**
	 * Divides a single pixel by a triplet
	 * 
	 * @param x X-coordinate
	 * @param y Y-coordinate
	 * @param value triplet to add to the pixel
	 */
	public final void divide(int x, int y, int[] value) {
		planes[0].divide(x, y, value[0]);
		planes[1].divide(x, y, value[1]);
		planes[2].divide(x, y, value[2]);
	}

	/**
	 * Divides a single pixel by a triplet
	 * 
	 * @param x X-coordinate
	 * @param val0 value to divide by the pixel in the first plane
	 * @param val1 value to divide by the pixel in the second plane
	 * @param val2 value to divide by the pixel in the third plane
	 */
	public final void divide(int x, int y, int val0, int val1, int val2) {
		planes[0].divide(x, y, val0);
		planes[1].divide(x, y, val1);
		planes[2].divide(x, y, val2);
	}

	/**
	 * Finds the minimum value of all the planes of this image
	 * 
	 *
	 * @return an integer containing the minimum value
	 */
	public final int min() {
		int m1, m2, m3;
		int min = Integer.MAX_VALUE;
		m1 = planes[0].min();
		m2 = planes[1].min();
		m3 = planes[2].min();
		if (m1 < min)
			min = m1;
		if (m2 < min)
			min = m2;
		if (m3 < min)
			min = m3;
		return min;
	}

	/**
	 * Finds the minimum value of a single plane of this image
	 * 
	 * @param planeIndex the plane
	 * @return an integer containing the minimum value
	 */
	public final int min(int planeIndex) {
		return planes[planeIndex].min();
	}

	/**
	 * Finds the maximum value of all the planes of this image
	 * 
	 *
	 * @return an integer containing the maximum value
	 */
	public final int max() {
		int m1, m2, m3;
		int max = Integer.MIN_VALUE;
		m1 = planes[0].max();
		m2 = planes[1].max();
		m3 = planes[2].max();
		if (m1 > max)
			max = m1;
		if (m2 > max)
			max = m2;
		if (m3 > max)
			max = m3;
		return max;
	}

	/**
	 * Finds the maximum value of a single plane of this image
	 * 
	 * @param planeIndex the plane
	 * @return an integer containing the maximum value
	 */
	public final int max(int planeIndex) {
		return planes[planeIndex].max();
	}

	/*
	Image-wide scalar operations
	*/

	/**
	 * Adds triplet to all the pixels in this image
	 * 
	 * @param val0 value to add to the pixel in the first plane
	 * @param val1 value to add to the pixel in the second plane
	 * @param val2 value to add to the pixel in the third plane
	 * @return this
	 */
	public final ColorImage add(int val0, int val1, int val2) {
		planes[0] = planes[0].add(val0);
		planes[1] = planes[1].add(val1);
		planes[2] = planes[2].add(val2);
		return this;
	}

	/**
	 * Adds triplet to all the pixels in this image
	 * 
	 * @param v triplet to be added to the pixels
	 * @return this
	 */
	public final ColorImage add(int[] v) {
		planes[0] = planes[0].add(v[0]);
		planes[1] = planes[1].add(v[1]);
		planes[2] = planes[2].add(v[2]);
		return this;
	}

	/**
	 * Adds a value to all the pixels in a plane of this image
	 * 
	 * @param planeIndex the plane
	 * @param v value to be added to the pixels
	 * @return this
	 */
	public final ColorImage add(int planeIndex, int v) {
		planes[planeIndex] = planes[planeIndex].add(v);
		return this;
	}

	/**
	 * Subtracts a triplet from all the pixels of this image
	 * 
	 * @param val0 value to subtract from the pixel in the first plane
	 * @param val1 value to subtract from the pixel in the second plane
	 * @param val2 value to subtract from the pixel in the third plane
	 * @return this
	 */
	public final ColorImage subtract(int val0, int val1, int val2) {
		planes[0] = planes[0].subtract(val0);
		planes[1] = planes[1].subtract(val1);
		planes[2] = planes[2].subtract(val2);
		return this;
	}

	/**
	 * Subtracts a triplet from all the pixels of this image
	 * 
	 * @param v triplet to be subtracted from the pixels
	 * @return this
	 */
	public final ColorImage subtract(int[] v) {
		planes[0] = planes[0].subtract(v[0]);
		planes[1] = planes[1].subtract(v[1]);
		planes[2] = planes[2].subtract(v[2]);
		return this;
	}

	/** subtracts the value from all pixels in plane <i>planeIndex</i> */
	/**
	 * Subtracts a value from all the pixels in a plane of this image
	 * 
	 * @param planeIndex the plane
	 * @param v value to be added to the pixels
	 * @return this
	 */
	public final ColorImage subtract(int planeIndex, int v) {
		planes[planeIndex] = planes[planeIndex].subtract(v);
		return this;
	}

	/**
	 * Multiplies a triplet by all the pixels of this image
	 * 
	 * @param v triplet to be multiplied by
	 * @return this
	 */
	public final ColorImage multiply(int[] v) {
		planes[0] = planes[0].multiply(v[0]);
		planes[1] = planes[1].multiply(v[1]);
		planes[2] = planes[2].multiply(v[2]);
		return this;
	}

	/**
	 * Multiplies a triplet by all the pixels of this image
	 * 
	 * @param val0 value to multiply the pixel by in the first plane
	 * @param val1 value to multiply the pixel by in the second plane
	 * @param val2 value to multiply the pixel by in the third plane
	 * @return this
	 */
	public final ColorImage multiply(int val0, int val1, int val2) {
		planes[0] = planes[0].multiply(val0);
		planes[1] = planes[1].multiply(val1);
		planes[2] = planes[2].multiply(val2);
		return this;
	}

	/**
	 * Multiplies all the pixels in a plane of this image by a value
	 * 
	 * @param planeIndex the plane
	 * @param v value to be added to the pixels
	 * @return this
	 */
	public final ColorImage multiply(int planeIndex, int v) {
		planes[planeIndex] = planes[planeIndex].multiply(v);
		return this;
	}

	/**
	 * Divides all the pixels of this image by a triplet
	 * 
	 * @param v triplet to be divided by
	 * @return this
	 */
	public final ColorImage divide(int[] v) {
		planes[0] = planes[0].divide(v[0]);
		planes[1] = planes[1].divide(v[1]);
		planes[2] = planes[2].divide(v[2]);
		return this;
	}

	/**
	 * Divides all the pixels of this image by a triplet
	 * 
	 * @param val0 value to divide by the pixel in the first plane
	 * @param val1 value to divide by the pixel in the second plane
	 * @param val2 value to divide by the pixel in the third plane
	 * @return this
	 */
	public final ColorImage divide(int val0, int val1, int val2) {
		planes[0] = planes[0].divide(val0);
		planes[1] = planes[1].divide(val1);
		planes[2] = planes[2].divide(val2);
		return this;
	}

	/**
	 * Divides all the pixels in a plane of this image by a value
	 * 
	 * @param planeIndex the plane
	 * @param v value to be added to the pixels
	 * @return this
	 */
	public final ColorImage divide(int planeIndex, int v) {
		planes[planeIndex] = planes[planeIndex].divide(v);
		return this;
	}

	/*
	Image-by-image arithmetic operations
	*/

	/** Adds all the values together */
	public final int[] addSum() {
		int[] sum = new int[3];
		sum[0] = planes[0].addSum();
		sum[1] = planes[1].addSum();
		sum[2] = planes[2].addSum();
		return sum;
	}

	/** Adds absolute value of all the values together */
	public final int[] absSum() {
		int[] sum = new int[3];
		sum[0] = planes[0].absSum();
		sum[1] = planes[1].absSum();
		sum[2] = planes[2].absSum();
		return sum;
	}

	/** Adds the square of all the values together */
	public final long[] sqrSum() {
		long[] sum = new long[3];
		sum[0] = planes[0].sqrSum();
		sum[1] = planes[1].sqrSum();
		sum[2] = planes[2].sqrSum();
		return sum;
	}

	/**
	 * Adds another ColorImage to this image
	 * 
	 * @param im the ColorImage to add
	 * @return this
	 */
	public final ColorImage add(ColorImage im) {
		planes[0] = planes[0].add(im.plane(0));
		planes[1] = planes[1].add(im.plane(1));
		planes[2] = planes[2].add(im.plane(2));
		return this;
	}

	/**
	 * Subtracts a ColorImage from this image
	 * 
	 * @param im the ColorImage to subtract
	 * @return this
	 */
	public final ColorImage subtract(ColorImage im) {
		planes[0] = planes[0].subtract(im.plane(0));
		planes[1] = planes[1].subtract(im.plane(1));
		planes[2] = planes[2].subtract(im.plane(2));
		return this;
	}

	/**
	 * Subtracts a ColorImage from this image and returns the absolute value. Assumes the two images
	 * having the same size.
	 * 
	 * @param im the RealColorImage to diff
	 * @return this
	 */
	public final ColorImage diff(ColorImage im) {
		planes[0] = planes[0].diff(im.plane(0));
		planes[1] = planes[1].diff(im.plane(1));
		planes[2] = planes[2].diff(im.plane(2));
		return this;
	}

	/**
	 * Multiplies a ColorImage by this image and returns a new ColorImage.
	 * 
	 * @param im the ColorImage to multiply
	 * @return this
	 */
	public final ColorImage multiply(ColorImage im) {
		planes[0] = planes[0].multiply(im.plane(0));
		planes[1] = planes[1].multiply(im.plane(1));
		planes[2] = planes[2].multiply(im.plane(2));
		return this;
	}

	/**
	 * Divides this image by a ColorImage and returns a new ColorImage.
	 * 
	 * @param im the ColorImage to divide
	 * @return this
	 */
	public final ColorImage divide(ColorImage im) {
		planes[0] = planes[0].divide(im.plane(0));
		planes[1] = planes[1].divide(im.plane(1));
		planes[2] = planes[2].divide(im.plane(2));
		return this;
	}

	/**
	 * Prints the image in integer format. <DT>
	 * <DL>
	 * <DL>
	 * -Example of output on an image with width 100 and height 120:</DT>
	 * <DL>
	 * <DT>100 : 120</DT>
	 * <DT>10 87 32 65 32 65 40 59 43 12 43 ...</DT>
	 * <DT>100 : 120</DT>
	 * <DT>10 20 32 12 32 56 40 45 42 39 43 ...</DT>
	 * <DT>100 : 120</DT>
	 * <DT>10 20 32 12 54 56 73 59 42 23 43 ...</DT>
	 * </DL>
	 * </DL></DL>
	 */
	public final String toString() {
		String str = ""; // = ndims + " planes\n";
		str += planes[0].toString();
		str += planes[1].toString();
		str += planes[2].toString();
		return str;
	}

	/**
	 * Turns this image into a Java Image (java.awt.Image). The ColorImage is assumed to be RGB.
	 * <P>
	 * 
	 *
	 * @see java.awt.image.ImageProducer
	 */
	public final ImageProducer getJavaImage() {

		// get range of this image
		double min = (double) min();
		double max = (double) max();

		// keep byte images in original range
		if (min >= 0 && max <= 255) {
			min = 0.0;
			max = 255.0;
		}
		double range = max - min;

		// convert jigl image to java image
		int pix[] = new int[X * Y];
		int index = 0;
		int red = 0;
		int green = 0;
		int blue = 0;
		int[] color = new int[3];
		for (int y = 0; y < Y; y++) {
			for (int x = 0; x < X; x++) {
				// map image values
				color = get(x, y);
				red = (int) ((255.0 / range) * ((double) color[0] - min));
				green = (int) ((255.0 / range) * ((double) color[1] - min));
				blue = (int) ((255.0 / range) * ((double) color[2] - min));

				// take lower 8 bits
				red = 0x00FF & red;
				green = 0x00FF & green;
				blue = 0x00FF & blue;

				// put this pixel in the java image
				pix[index] = (0xFF << 24) | (red << 16) | (green << 8) | blue;
				index++;
			}
		}

		// return java image
		return new MemoryImageSource(X, Y, pix, 0, X);
	}

	/**
	 * Scales the range of this image to byte (0..255)
	 * 
	 *
	 */
	public void byteSize() {

		// get range of this image
		double min = (double) min();
		double max = (double) max();

		double range = max - min;

		// convert to byte depth
		int red = 0;
		int green = 0;
		int blue = 0;
		int color[] = new int[3];
		for (int y = 0; y < Y; y++) {
			for (int x = 0; x < X; x++) {
				color = get(x, y);
				red = (int) ((255.0 / range) * ((double) color[0] - min));
				green = (int) ((255.0 / range) * ((double) color[1] - min));
				blue = (int) ((255.0 / range) * ((double) color[2] - min));

				// take lower 8 bits
				red = 0x00FF & red;
				green = 0x00FF & green;
				blue = 0x00FF & blue;
				color[0] = red;
				color[1] = green;
				color[2] = blue;
				set(x, y, color);
			}
		}

	}

	/**
	 * Clips the range of this image to an arbitrary min/max
	 * 
	 * @param min minimum value
	 * @param max maximum value
	 */
	public final void clip(int min, int max) {

		// clip each plane
		planes[0].clip(min, max);
		planes[1].clip(min, max);
		planes[2].clip(min, max);

	}

	/** Clears the image to zero */
	public final void clear() {
		planes[0].clear();
		planes[1].clear();
		planes[2].clear();

	}

	/**
	 * Clears the image to a value
	 * 
	 * @param v value to clear to
	 */
	public final void clear(int v) {
		planes[0].clear(v);
		planes[1].clear(v);
		planes[2].clear(v);

	}

	//*****************************************************************************************************
	//**********************************  ROI *************************************************************
	//*****************************************************************************************************

	/**
	 * Makes a deep copy of a Region of Interest
	 * 
	 * @param roi Region of Interest
	 * @return a deep copy of ColorImage
	 */
	public Image copy(ROI roi) {
		ColorImage c = new ColorImage(roi.X(), roi.Y());

		c.setColorModel(colorModel);

		c.planes[0] = (GrayImage) planes[0].copy(roi);
		c.planes[1] = (GrayImage) planes[1].copy(roi);
		c.planes[2] = (GrayImage) planes[2].copy(roi);

		/*		for(int y = roi.uy(); y < roi.ly(); y++) {
					for(int x = roi.ux(); x < roi.lx(); x++) {
						c.planes[0].set(x-roi.ux(),y-roi.uy(),planes[0].get(x,y));
						c.planes[1].set(x-roi.ux(),y-roi.uy(),planes[1].get(x,y));
						c.planes[2].set(x-roi.ux(),y-roi.uy(),planes[2].get(x,y));
					}
				}*/
		return c;
	}

	/**
	 * Returns the pixel value at the given x, y value as a triplet in a Region of Interest
	 * 
	 * @param x the X coordinate
	 * @param y the Y coordinate
	 * @param roi Region of Interest
	 * @return three element array of integers
	 */
	public final int[] get(int x, int y, ROI roi) {
		int[] color = new int[3];
		color[0] = planes[0].get(x + roi.ux(), y + roi.uy());
		color[1] = planes[1].get(x + roi.ux(), y + roi.uy());
		color[2] = planes[2].get(x + roi.ux(), y + roi.uy());
		return color;
	}

	/**
	 * Sets the pixel value at the given x, y value as a triplet in a Region of Interest
	 * 
	 * @param x the X coordinate
	 * @param y the Y coordinate
	 * @param v array of three integers holding the values for the set
	 * @param roi Region of Interest
	 */
	public final void set(int x, int y, int[] v, ROI roi) {
		planes[0].set(x, y, v[0], roi);
		planes[1].set(x, y, v[1], roi);
		planes[2].set(x, y, v[2], roi);
	}

	/**
	 * Adds a triplet to a single pixel in a Region of Interest
	 * 
	 * @param x X-coordinate
	 * @param y Y-coordinate
	 * @param val0 value to add to the pixel in the first plane
	 * @param val1 value to add to the pixel in the second plane
	 * @param val2 value to add to the pixel in the third plane
	 * @param roi Region of Interest
	 */
	public final void add(int x, int y, int val0, int val1, int val2, ROI roi) {
		planes[0].set(x, y, val0, roi);
		planes[1].set(x, y, val1, roi);
		planes[2].set(x, y, val2, roi);
	}

	/**
	 * Adds a triplet to a single pixel in a Region of Interest
	 * 
	 * @param x X-coordinate
	 * @param y Y-coordinate
	 * @param value triplet to subtract from the pixel
	 * @param roi Region of Interest
	 */
	public final void add(int x, int y, int[] value, ROI roi) {
		planes[0].add(x, y, value[0], roi);
		planes[1].add(x, y, value[1], roi);
		planes[2].add(x, y, value[2], roi);
	}

	/**
	 * Subtracts a triplet from a single pixel in a Region of Interest
	 * 
	 * @param x X-coordinate
	 * @param y Y-coordinate
	 * @param val0 value to subtract from the pixel in the first plane
	 * @param val1 value to subtract from the pixel in the second plane
	 * @param val2 value to subtract from the pixel in the third plane
	 * @param roi Region of Interest
	 */
	public final void subtract(int x, int y, int val0, int val1, int val2, ROI roi) {
		planes[0].subtract(x, y, val0, roi);
		planes[1].subtract(x, y, val1, roi);
		planes[2].subtract(x, y, val2, roi);

	}

	/**
	 * Subtracts a triplet from a single pixel in a Region of Interest
	 * 
	 * @param x X-coordinate
	 * @param y Y-coordinate
	 * @param value triplet to subtract from to the pixel
	 * @param roi Region of Interest
	 */
	public final void subtract(int x, int y, int[] value, ROI roi) {
		planes[0].subtract(x, y, value[0], roi);
		planes[1].subtract(x, y, value[1], roi);
		planes[2].subtract(x, y, value[2], roi);
	}

	/**
	 * Multiplies a triplet with a single pixel in a Region of Interest
	 * 
	 * @param x X-coordinate
	 * @param y Y-coordinate
	 * @param val0 value to multiply the pixel by in the first plane
	 * @param val1 value to multiply the pixel by in the second plane
	 * @param val2 value to multiply the pixel by in the third plane
	 * @param roi Region of Interest
	 */
	public final void multiply(int x, int y, int val0, int val1, int val2, ROI roi) {
		planes[0].multiply(x, y, val0, roi);
		planes[1].multiply(x, y, val1, roi);
		planes[2].multiply(x, y, val2, roi);
	}

	/**
	 * Multiplies a triplet with a single pixel in a Region of Interest
	 * 
	 * @param x X-coordinate
	 * @param y Y-coordinate
	 * @param value triplet to multiply by the pixel
	 * @param roi Region of Interest
	 */
	public final void multiply(int x, int y, int[] value, ROI roi) {
		planes[0].multiply(x, y, value[0], roi);
		planes[1].multiply(x, y, value[1], roi);
		planes[2].multiply(x, y, value[2], roi);
	}

	/**
	 * Divides a single pixel by a triplet in a Region of Interest
	 * 
	 * @param x X-coordinate
	 * @param y Y-coordinate
	 * @param value triplet to add to the pixel
	 * @param roi Region of Interest
	 */
	public final void divide(int x, int y, int[] value, ROI roi) {
		planes[0].divide(x, y, value[0], roi);
		planes[1].divide(x, y, value[1], roi);
		planes[2].divide(x, y, value[2], roi);
	}

	/**
	 * Divides a single pixel by a triplet in a Region of Interest
	 * 
	 * @param x X-coordinate
	 * @param val0 value to divide by the pixel in the first plane
	 * @param val1 value to divide by the pixel in the second plane
	 * @param val2 value to divide by the pixel in the third plane
	 * @param roi Region of Interest
	 */
	public final void divide(int x, int y, int val0, int val1, int val2, ROI roi) {
		planes[0].divide(x, y, val0, roi);
		planes[1].divide(x, y, val1, roi);
		planes[2].divide(x, y, val2, roi);
	}

	/**
	 * Finds the minimum value of all the planes in a Region of Interest
	 * 
	 * @param roi Region of Interest
	 * @return an integer containing the minimum value
	 */
	public final int min(ROI roi) {
		int m1, m2, m3;
		int min = Integer.MAX_VALUE;
		m1 = planes[0].min(roi);
		m2 = planes[1].min(roi);
		m3 = planes[2].min(roi);
		if (m1 < min)
			min = m1;
		if (m2 < min)
			min = m2;
		if (m3 < min)
			min = m3;
		return min;
	}

	/**
	 * Finds the minimum value of a single plane in a Region of Interest
	 * 
	 * @param planeIndex the plane
	 * @param roi Region of Interest
	 * @return an integer containing the minimum value
	 */
	public final int min(int planeIndex, ROI roi) {
		return planes[planeIndex].min(roi);
	}

	/**
	 * Finds the maximum value of all the planes in a Region of Interest
	 * 
	 * @param roi Region of Interest
	 * @return an integer containing the maximum value
	 */
	public final int max(ROI roi) {
		int m1, m2, m3;
		int max = Integer.MIN_VALUE;
		m1 = planes[0].max(roi);
		m2 = planes[1].max(roi);
		m3 = planes[2].max(roi);
		if (m1 > max)
			max = m1;
		if (m2 > max)
			max = m2;
		if (m3 > max)
			max = m3;
		return max;
	}

	/**
	 * Finds the maximum value of a single plane in a Region of Interest
	 * 
	 * @param planeIndex the plane
	 * @param roi Region of Interest
	 * @return an integer containing the maximum value
	 */
	public final int max(int planeIndex, ROI roi) {
		return planes[planeIndex].max(roi);
	}

	/*
	Image-wide scalar operations
	*/

	/**
	 * Adds triplet to all the pixels in a Region of Interest
	 * 
	 * @param v triplet to be added to the pixels
	 * @param roi Region of Interest
	 * @return this
	 */
	public final ColorImage add(int[] v, ROI roi) {
		planes[0] = planes[0].add(v[0], roi);
		planes[1] = planes[1].add(v[1], roi);
		planes[2] = planes[2].add(v[2], roi);
		return this;
	}

	/**
	 * Adds triplet to all the pixels in this image in a Region of Interest
	 * 
	 * @param val0 value to add to the pixel in the first plane
	 * @param val1 value to add to the pixel in the second plane
	 * @param val2 value to add to the pixel in the third plane
	 * @param roi Region of Interest
	 * @return this
	 */
	public final ColorImage add(int val0, int val1, int val2, ROI roi) {
		planes[0] = planes[0].add(val0, roi);
		planes[1] = planes[1].add(val1, roi);
		planes[2] = planes[2].add(val2, roi);
		return this;
	}

	/**
	 * Adds a value to all the pixels in a plane in a Region of Interest
	 * 
	 * @param planeIndex the plane
	 * @param v value to be added to the pixels
	 * @param roi Region of Interest
	 * @return this
	 */
	public final ColorImage add(int planeIndex, int v, ROI roi) {
		planes[planeIndex] = planes[planeIndex].add(v, roi);
		return this;
	}

	/**
	 * Subtracts a triplet from all the pixels in a Region of Interest
	 * 
	 * @param v triplet to be subtracted from the pixels
	 * @param roi Region of Interest
	 * @return this
	 */
	public final ColorImage subtract(int[] v, ROI roi) {
		planes[0] = planes[0].subtract(v[0], roi);
		planes[1] = planes[1].subtract(v[1], roi);
		planes[2] = planes[2].subtract(v[2], roi);
		return this;
	}

	/**
	 * Subtracts a triplet from all the pixels in a Region of Interest
	 * 
	 * @param val0 value to subtract from the pixel in the first plane
	 * @param val1 value to subtract from the pixel in the second plane
	 * @param val2 value to subtract from the pixel in the third plane
	 * @param roi Region of Interest
	 * @return this
	 */
	public final ColorImage subtract(int val0, int val1, int val2, ROI roi) {
		planes[0] = planes[0].subtract(val0, roi);
		planes[1] = planes[1].subtract(val1, roi);
		planes[2] = planes[2].subtract(val2, roi);
		return this;
	}

	/** subtracts the value from all pixels in plane <i>planeIndex</i> */
	/**
	 * Subtracts a value from all the pixels in a Region of Interest
	 * 
	 * @param planeIndex the plane
	 * @param v value to be added to the pixels
	 * @param roi Region of Interest
	 * @return this
	 */
	public final ColorImage subtract(int planeIndex, int v, ROI roi) {
		planes[planeIndex] = planes[planeIndex].subtract(v, roi);
		return this;
	}

	/**
	 * Multiplies a triplet by all the pixels in a Region of Interest
	 * 
	 * @param v triplet to be multiplied by
	 * @param roi Region of Interest
	 * @return this
	 */
	public final ColorImage multiply(int[] v, ROI roi) {
		planes[0] = planes[0].multiply(v[0], roi);
		planes[1] = planes[1].multiply(v[1], roi);
		planes[2] = planes[2].multiply(v[2], roi);
		return this;
	}

	/**
	 * Multiplies a triplet by all the pixels of this image in a Region of Interest
	 * 
	 * @param val0 value to multiply the pixel by in the first plane
	 * @param val1 value to multiply the pixel by in the second plane
	 * @param val2 value to multiply the pixel by in the third plane
	 * @param roi Region of Interest
	 * @return this
	 */
	public final ColorImage multiply(int val0, int val1, int val2, ROI roi) {
		planes[0] = planes[0].multiply(val0, roi);
		planes[1] = planes[1].multiply(val1, roi);
		planes[2] = planes[2].multiply(val2, roi);
		return this;
	}

	/** multiplies all pixels in plane p by the value */
	/**
	 * Multiplies all the pixels in a plane in a Region of Interest by a value
	 * 
	 * @param planeIndex the plane
	 * @param v value to be added to the pixels
	 * @param roi Region of Interest
	 * @return this
	 */
	public final ColorImage multiply(int planeIndex, int v, ROI roi) {
		planes[planeIndex] = planes[planeIndex].multiply(v, roi);
		return this;
	}

	/**
	 * Divides all the pixels in a Region of Interest by a triplet
	 * 
	 * @param v triplet to be divided by
	 * @param roi Region of Interest
	 * @return this
	 */
	public final ColorImage divide(int[] v, ROI roi) {
		planes[0] = planes[0].divide(v[0], roi);
		planes[1] = planes[1].divide(v[1], roi);
		planes[2] = planes[2].divide(v[2], roi);
		return this;
	}

	/**
	 * Divides all the pixels in a Region of Interest by a triplet
	 * 
	 * @param val0 value to divide by the pixel in the first plane
	 * @param val1 value to divide by the pixel in the second plane
	 * @param val2 value to divide by the pixel in the third plane
	 * @param roi Region of Interest
	 * @return this
	 */
	public final ColorImage divide(int val0, int val1, int val2, ROI roi) {
		planes[0] = planes[0].divide(val0, roi);
		planes[1] = planes[1].divide(val1, roi);
		planes[2] = planes[2].divide(val2, roi);
		return this;
	}

	/** divides all pixels in plane <i>planeIndex</i> by the value */
	/**
	 * Divides all the pixels in a plane in a Region of Interest by a value
	 * 
	 * @param planeIndex the plane index
	 * @param v value to be added to the pixels
	 * @param roi Region of Interest
	 * @return this
	 */
	public final ColorImage divide(int planeIndex, int v, ROI roi) {
		planes[planeIndex] = planes[planeIndex].divide(v, roi);
		return this;
	}

	/*
	Image-by-image arithmetic operations
	*/

	/**
	 * Adds a Region of Interest (sourceROI) in another ColorImage to a Region of Interest (destROI)
	 * of this image
	 * 
	 * @param im the ColorImage to add
	 * @param sourceROI Region of Interest for the Source Image
	 * @param destROI Region of Interest for the Destination Image
	 * @return this
	 */
	public final ColorImage add(ColorImage im, ROI sourceROI, ROI destROI) {
		planes[0] = planes[0].add(im.plane(0), sourceROI, destROI);
		planes[1] = planes[1].add(im.plane(1), sourceROI, destROI);
		planes[2] = planes[2].add(im.plane(2), sourceROI, destROI);
		return this;
	}

	/**
	 * Subtracts a Region of Interest (sourceROI) from another ColorImage from a Region of Interest
	 * (destROI) of this image
	 * 
	 * @param im the ColorImage to subtract
	 * @param sourceROI Region of Interest for the Source Image
	 * @param destROI Region of Interest for the Destination Image
	 * @return this
	 */
	public final ColorImage subtract(ColorImage im, ROI sourceROI, ROI destROI) {
		planes[0] = planes[0].subtract(im.plane(0), sourceROI, destROI);
		planes[1] = planes[1].subtract(im.plane(1), sourceROI, destROI);
		planes[2] = planes[2].subtract(im.plane(2), sourceROI, destROI);
		return this;
	}

	/**
	 * Multiplies a Region of Interest (sourceROI) from another ColorImage by a Region of Interest
	 * (destROI) of this image
	 * 
	 * @param im the ColorImage to multiply
	 * @param sourceROI Region of Interest for the Source Image
	 * @param destROI Region of Interest for the Destination Image
	 * @return this
	 */
	public final ColorImage multiply(ColorImage im, ROI sourceROI, ROI destROI) {
		planes[0] = planes[0].multiply(im.plane(0), sourceROI, destROI);
		planes[1] = planes[1].multiply(im.plane(1), sourceROI, destROI);
		planes[2] = planes[2].multiply(im.plane(2), sourceROI, destROI);
		return this;
	}

	/**
	 * Divides a Region of Interest (sourceROI) in this image by a Region of Interest (destROI) from
	 * another ColorImage
	 * 
	 * @param im the ColorImage to divide
	 * @param sourceROI Region of Interest for the Source Image
	 * @param destROI Region of Interest for the Destination Image
	 * @return this
	 */
	public final ColorImage divide(ColorImage im, ROI sourceROI, ROI destROI) {
		planes[0] = planes[0].divide(im.plane(0), sourceROI, destROI);
		planes[1] = planes[1].divide(im.plane(1), sourceROI, destROI);
		planes[2] = planes[2].divide(im.plane(2), sourceROI, destROI);
		return this;
	}

	/**
	 * Prints a Region of Interest in integer format. <DT>
	 * <DL>
	 * <DL>
	 * -Example of output on an image with width 100 and height 120:</DT>
	 * <DL>
	 * <DT>100 : 120</DT>
	 * <DT>10 87 32 65 32 65 40 59 43 12 43 ...</DT>
	 * <DT>100 : 120</DT>
	 * <DT>10 20 32 12 32 56 40 45 42 39 43 ...</DT>
	 * <DT>100 : 120</DT>
	 * <DT>10 20 32 12 54 56 73 59 42 23 43 ...</DT>
	 * </DL>
	 * </DL></DL>
	 * 
	 * @param roi Region of Interest
	 */
	public final String toString(ROI roi) {
		String str = ""; // = ndims + " planes\n";
		str += planes[0].toString(roi);
		str += planes[1].toString(roi);
		str += planes[2].toString(roi);
		return str;
	}

	/**
	 * Scales the range of a Region of Interest of this image to byte (0..255)
	 * 
	 * @param roi Region of Interest of image.
	 */
	public void byteSize(ROI roi) {

		// get range of this image
		double min = (double) min(roi);
		double max = (double) max(roi);

		double range = max - min;

		// convert to byte depth
		int red = 0;
		int green = 0;
		int blue = 0;
		int color[] = new int[3];
		for (int y = roi.uy(); y <= roi.ly(); y++) {
			for (int x = roi.ux(); x <= roi.lx(); x++) {
				color = get(x, y);
				red = (int) ((255.0 / range) * ((double) color[0] - min));
				green = (int) ((255.0 / range) * ((double) color[1] - min));
				blue = (int) ((255.0 / range) * ((double) color[2] - min));

				// take lower 8 bits
				red = 0x00FF & red;
				green = 0x00FF & green;
				blue = 0x00FF & blue;
				color[0] = red;
				color[1] = green;
				color[2] = blue;
				set(x, y, color);
			}
		}

	}

	/**
	 * Clips the range of this image to an arbitrary min/max in a Region of Interest
	 * 
	 * @param min minimum value
	 * @param max maximum value
	 * @param roi Region of Interest+
	 */
	public final void clip(int min, int max, ROI roi) {

		// clip each plane
		planes[0].clip(min, max, roi);
		planes[1].clip(min, max, roi);
		planes[2].clip(min, max, roi);

	}
}
