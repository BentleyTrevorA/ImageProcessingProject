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
package jigl.image;

/**
 * Operator is an interface for performing operations on an image.
 * 
 * @see jigl.image.SimpleOperator
 */
public interface Operator {
	public Image apply(Image image) throws ImageNotSupportedException;

	public Image apply(Image image, ROI roi) throws ImageNotSupportedException;
}
