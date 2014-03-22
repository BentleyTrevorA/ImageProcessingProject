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

import jigl.image.ColorImage;
import jigl.image.GrayImage;
import jigl.image.Image;
import jigl.image.ROI;
import jigl.image.RealColorImage;
import jigl.image.RealGrayImage;
import jigl.image.SimpleOperator;

/**
 * Adds uniform noise distributions to an image. Supports GrayImage, RealGrayImage, ColorImage,
 * RealColorImage.
 */
public class UniformNoise extends SimpleOperator {
	/** The absolute maximum value of uniform noise. */
	private float m_range;

	/**
	 * Constructor.
	 * 
	 * @param range range (0..255)
	 */
	public UniformNoise(float range) {
		m_range = range;
	}

	/**
	 * Adds noise to <code>image</code> following the uniform distribution.
	 * 
	 * @param image GrayImage to add noise to.
	 * @return <code>image</code>.
	 */
	protected Image apply(GrayImage image) {
		return apply(image, new ROI(0, 0, image.X() - 1, image.Y() - 1));
	}

	/**
	 * Adds noise to <code>image</code> in the specified Region of Interest, following the uniform
	 * distribution.
	 * 
	 * @param image GrayImage to add noise to.
	 * @param roi Region of Interest of <code>image</code>.
	 * @return <code>image</code>.
	 */
	protected Image apply(GrayImage image, ROI roi) {
		java.util.Random random = new java.util.Random();
		int range = (int) m_range;
		int int_val = 0;
		float choice = 0;

		for (int x = roi.ux(); x <= roi.lx(); x++)
			for (int y = roi.uy(); y <= roi.ly(); y++) {
				choice = random.nextFloat();
				int_val = random.nextInt();
				int_val = int_val % range;
				if (choice > 0.5)
					image.add(x, y, int_val);
				else
					image.subtract(x, y, int_val);
			}

		return image;
	}

	/**
	 * Adds noise to <code>image</code> following the uniform distribution.
	 * 
	 * @param image RealGrayImage to add noise to.
	 * @return <code>image</code>.
	 */
	protected Image apply(RealGrayImage image) {
		return apply(image, new ROI(0, 0, image.X() - 1, image.Y() - 1));
	}

	/**
	 * Adds noise to <code>image</code> in the specified Region of Interest, following the uniform
	 * distribution.
	 * 
	 * @param image RealGrayImage to add noise to.
	 * @param roi Region of Interest of <code>image</code>.
	 * @return <code>image</code>.
	 */
	protected Image apply(RealGrayImage image, ROI roi) {
		java.util.Random random = new java.util.Random();
		float float_val = 0;
		float choice = 0;

		for (int x = roi.ux(); x <= roi.lx(); x++)
			for (int y = roi.uy(); y <= roi.ly(); y++) {
				choice = random.nextFloat();
				float_val = random.nextFloat();
				float_val = float_val * m_range;
				if (choice > 0.5)
					image.add(x, y, float_val);
				else
					image.subtract(x, y, float_val);
			}
		return image;
	}

	/**
	 * Adds noise to <code>image</code> following the uniform distribution. This is done by adding
	 * noise to each plane seperately.
	 * 
	 * @param image ColorImage to add noise to.
	 * @return <code>image</code>
	 */
	protected Image apply(ColorImage image) {
		image.setPlane(0, (GrayImage) apply(image.plane(0), new ROI(0, 0, image.X() - 1,
				image.Y() - 1)));
		image.setPlane(1, (GrayImage) apply(image.plane(1), new ROI(0, 0, image.X() - 1,
				image.Y() - 1)));
		image.setPlane(2, (GrayImage) apply(image.plane(2), new ROI(0, 0, image.X() - 1,
				image.Y() - 1)));

		return image;
	}

	/**
	 * Adds noise to <code>image</code> in the specified Region of Interest, following the uniform
	 * distribution. This is done by adding noise to each plane seperately.
	 * 
	 * @param image ColorImage to add noise to.
	 * @param roi Region of Interest of <code>image</code>.
	 * @return <code>image</code>.
	 */
	protected Image apply(ColorImage image, ROI roi) {
		image.setPlane(0, (GrayImage) apply(image.plane(0), roi));
		image.setPlane(1, (GrayImage) apply(image.plane(1), roi));
		image.setPlane(2, (GrayImage) apply(image.plane(2), roi));

		return image;
	}

	/**
	 * Adds noise to <code>image</code> following the uniform distribution. This is done by adding
	 * noise to each plane seperately.
	 * 
	 * @param image RealColorImage to add noise to.
	 * @return <code>image</code>
	 */
	protected Image apply(RealColorImage image) {
		image.setPlane(0, (RealGrayImage) apply(image.plane(0), new ROI(0, 0, image.X() - 1, image
				.Y() - 1)));
		image.setPlane(1, (RealGrayImage) apply(image.plane(1), new ROI(0, 0, image.X() - 1, image
				.Y() - 1)));
		image.setPlane(2, (RealGrayImage) apply(image.plane(2), new ROI(0, 0, image.X() - 1, image
				.Y() - 1)));

		return image;
	}

	/**
	 * Adds noise to <code>image</code> in the specified Region of Interest, following the uniform
	 * distribution. This is done by adding noise to each plane seperately.
	 * 
	 * @param image RealColorImage to add noise to.
	 * @param roi Region of Interest of <code>image</code>.
	 * @return <code>image</code>.
	 */
	protected Image apply(RealColorImage image, ROI roi) {
		image.setPlane(0, (RealGrayImage) apply(image.plane(0), roi));
		image.setPlane(1, (RealGrayImage) apply(image.plane(1), roi));
		image.setPlane(2, (RealGrayImage) apply(image.plane(2), roi));

		return image;
	}

}
