/* This file is part of Jiggler, a lightly maintained version
 * of JIGL (Java Imaging and Graphics Library)
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
package jigl.image.ops;

import jigl.image.ComplexImage;
import jigl.image.GrayImage;
import jigl.image.Image;
import jigl.image.RealGrayImage;
import jigl.image.SimpleOperator;
import jigl.image.utils.FFT;

/**
 * Performs the reverse FFT on an image. Simply provides a SimpleOperator wrapper around the FFT
 * class. Supports GrayImage, RealGrayImage, ComplexImage
 * 
 * @see jigl.image.utils.FFT
 */
public class ReverseFFT extends SimpleOperator {
	/** Constructor. Does nothing. */
	public ReverseFFT() {
	}

	/**
	 * Performs the reverse FFT on a GrayImage. Returned image is a ComplexImage.
	 * 
	 * @param image GrayImage to transform.
	 * @return ComplexImage
	 */
	protected Image apply(GrayImage image) {
		return FFT.reverse(image);
	}

	/**
	 * Performs the reverse FFT on a RealGrayImage. Returned image is a ComplexImage.
	 * 
	 * @param image RealGrayImage to transform.
	 * @return ComplexImage
	 */
	protected Image apply(RealGrayImage image) {
		return FFT.reverse(image);
	}

	/**
	 * Performs the reverse FFT on a ComplexImage. Returned image is a ComplexImage.
	 * 
	 * @param image ComplexImage to transform.
	 * @return ComplexImage
	 */
	protected Image apply(ComplexImage image) {
		return FFT.reverse(image);
	}

}
