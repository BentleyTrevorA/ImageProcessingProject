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
package jigl.image.ops.morph;

import jigl.image.ColorImage;
import jigl.image.GrayImage;
import jigl.image.Image;
import jigl.image.ImageKernel;
import jigl.image.ROI;
import jigl.image.RealColorImage;
import jigl.image.RealGrayImage;
import jigl.image.SimpleOperator;
import jigl.image.io.ImageInputStream;
import jigl.image.io.ImageOutputStream;

/**
 * GOpen performs an open operation on an image. Supports GrayImage, RealGrayImage, ColorImage,
 * RealColorImage.
 */
public class GOpen extends SimpleOperator {
	private ImageKernel kernel;
	private int center_x;
	private int center_y;

	/** Initilizes Open */
	public GOpen(ImageKernel ker, int x, int y) {
		kernel = ker;
		center_x = x;
		center_y = y;
	}

	/**
	 * Performs an Open on a GrayImage. Returned image is a GrayImage. <code>image</code> is not
	 * modified.
	 * 
	 * @param image GrayImage to open.
	 * @return GrayImage.
	 */
	protected Image apply(GrayImage image) {
		GrayImage image2 = (GrayImage) image.copy();
		GDilate d = new GDilate(kernel, center_x, center_y);
		GErode e = new GErode(kernel, center_x, center_y);
		image2 = (GrayImage) e.apply(image);
		image2 = (GrayImage) d.apply(image2);
		return image2;
	}

	/**
	 * Performs an Open on a RealGrayImage. Returned image is a RealGrayImage. <code>image</code> is
	 * not modified
	 * 
	 * @param image RealGrayImage to open.
	 * @return RealGrayImage.
	 */
	protected Image apply(RealGrayImage image) {
		RealGrayImage image2 = (RealGrayImage) image.copy();
		GDilate d = new GDilate(kernel, center_x, center_y);
		GErode e = new GErode(kernel, center_x, center_y);
		image2 = (RealGrayImage) e.apply(image);
		image2 = (RealGrayImage) d.apply(image2);
		return image2;
	}

	/**
	 * Performs an Open on a GrayImage in a Region of Interest. Returned image is a GrayImage.
	 * <code>image</code> is not modified.
	 * 
	 * @param image GrayImage to open.
	 * @param roi Region of Interest of <code>image</code>.
	 * @return GrayImage
	 */
	protected Image apply(GrayImage image, ROI roi) {
		GrayImage image2 = (GrayImage) image.copy();
		GDilate d = new GDilate(kernel, center_x, center_y);
		GErode e = new GErode(kernel, center_x, center_y);
		image2 = (GrayImage) e.apply(image, roi);
		image2 = (GrayImage) d.apply(image2, roi);
		return image2;
	}

	/**
	 * Performs an Open on a RealGrayImage in a Region of Interest. Returned image is a
	 * RealGrayImage. <code>image</code> is not modified.
	 * 
	 * @param image RealGrayImage to open.
	 * @param roi Region of Interest of <code>image</code>.
	 * @return RealGrayImage.
	 */
	protected Image apply(RealGrayImage image, ROI roi) {
		RealGrayImage image2 = (RealGrayImage) image.copy();
		GDilate d = new GDilate(kernel, center_x, center_y);
		GErode e = new GErode(kernel, center_x, center_y);
		image2 = (RealGrayImage) e.apply(image, roi);
		image2 = (RealGrayImage) d.apply(image2, roi);
		return image2;
	}

	/**
	 * Performs an Open on a ColorImage. This is done to each GrayImage plane separately. Returned
	 * image is a ColorImage. <code>image</code> is not modified.
	 * 
	 * @param image ColorImage to open.
	 * @return ColorImage.
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
	 * Performs an Open on a ColorImage in a Region of Interest. This is done to each GrayImage
	 * plane separately. Returned image is a ColorImage. <code>image</code> is not modified.
	 * 
	 * @param image ColorImage to open.
	 * @param roi Region of Interest of <code>image</code>.
	 * @return ColorImage.
	 */
	protected Image apply(ColorImage image, ROI roi) {
		image.setPlane(0, (GrayImage) apply(image.plane(0), roi));
		image.setPlane(1, (GrayImage) apply(image.plane(1), roi));
		image.setPlane(2, (GrayImage) apply(image.plane(2), roi));

		return image;
	}

	/**
	 * Performs an Open on a RealColorImage in a Region of Interest. This is done to each
	 * RealGrayImage plane separately. Returned image is a RealColorImage. <code>image</code> is not
	 * modified.
	 * 
	 * @param image RealColorImage to open.
	 * @return RealColorImage.
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
	 * Performs an Open on a RealColorImage in a Region of Interest. This is done to each
	 * RealGrayImage plane separately. Returned image is a RealColorImage. <code>image</code> is not
	 * modified.
	 * 
	 * @param image RealColorImage to open.
	 * @param roi Region of Interest of <code>image</code>.
	 * @return RealColorImage.
	 */
	protected Image apply(RealColorImage image, ROI roi) {
		image.setPlane(0, (RealGrayImage) apply(image.plane(0), roi));
		image.setPlane(1, (RealGrayImage) apply(image.plane(1), roi));
		image.setPlane(2, (RealGrayImage) apply(image.plane(2), roi));

		return image;
	}

	/**
	 * For commandline option. The syntax is "java GOpen <u>input_filename</u>
	 * <u>kernel_filename</u> <u>center_x</u> <u>center_y</u> <u>output_filename</u>"
	 */
	public static void main(String[] argv) {

		try {
			Image image = null;
			Image image2 = null;
			Image image3 = null;
			String inputfile = argv[0];
			String kernelfile = argv[1];

			ImageInputStream is = new ImageInputStream(inputfile);
			image = is.read();
			is.close();

			is = new ImageInputStream(kernelfile);
			image2 = is.read();
			is.close();

			Integer f_val1 = Integer.valueOf(argv[2]);
			Integer f_val2 = Integer.valueOf(argv[3]);
			int val1 = f_val1.intValue();
			int val2 = f_val2.intValue();

			GOpen open = new GOpen((ImageKernel) image2, val1, val2);
			image3 = open.apply(image);

			//put command line stuff here.

			// create a new ImageOutputStream
			ImageOutputStream os = new ImageOutputStream(argv[4]);
			os.write(image3);
			os.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
