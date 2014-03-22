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

import java.awt.image.ImageProducer;

/**
 * Image is the interface for BinaryImage, GrayImage, RealGrayImage, ColorImage, RealColorImage,
 * ComplexImage.
 * <p>
 * In addition, all typed Image classes must support the following typed methods:
 * 
 * get set clear ...
 */
public interface Image {

	/** Returns the width of the image */
	public int X();

	/** Returns the height of the image */
	public int Y();

	/** Returns a string representation of an image */
	public String toString();

	/**
	 * Turns this image into a Java Image (java.awt.Image).
	 *
	 * @see java.awt.image.ImageProducer
	 */
	public ImageProducer getJavaImage();

	/** Returns a deep copy of an image */
	public Image copy();

	/**
	 * Returns a deep copy of the specified region of interest of the image.
	 * 
	 * @param r region of interest of the image.
	 */
	public Image copy(ROI r);

} // image

