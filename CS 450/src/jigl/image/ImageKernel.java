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

/** ImageKernel is an extension of GrayImage with some pre-defined kernals. */

public class ImageKernel extends RealGrayImage {
	/**
	 * Uniform Kernel.
	 * <TABLE BORDER CELLPADDING=5 COLS=3 WIDTH="13" >
	 * <TR>
	 * <TD>1</TD>
	 * <TD>1</TD>
	 * <TD>1</TD>
	 * </TR>
	 * <TR>
	 * <TD>1</TD>
	 * <TD>1</TD>
	 * <TD>1</TD>
	 * </TR>
	 * <TR>
	 * <TD>1</TD>
	 * <TD>1</TD>
	 * <TD>1</TD>
	 * </TR>
	 * </TABLE>
	 */
	public static final int UNIFORM = 0;
	/**
	 * Sorbel Kernel with X orientation.
	 * <TABLE BORDER CELLPADDING=5 COLS=3 WIDTH="13" >
	 * <TR>
	 * <TD>-1</TD>
	 * <TD>0</TD>
	 * <TD>1</TD>
	 * </TR>
	 * <TR>
	 * <TD>-2</TD>
	 * <TD>0</TD>
	 * <TD>2</TD>
	 * </TR>
	 * <TR>
	 * <TD>-1</TD>
	 * <TD>0</TD>
	 * <TD>1</TD>
	 * </TR>
	 * </TABLE>
	 */
	public static final int SOBEL_X = 1;
	/**
	 * Sobel Kernel with Y orientation.
	 * <TABLE BORDER CELLPADDING=5 COLS=3 WIDTH="13" >
	 * <TR>
	 * <TD>-1</TD>
	 * <TD>-2</TD>
	 * <TD>-1</TD>
	 * </TR>
	 * <TR>
	 * <TD>0</TD>
	 * <TD>0</TD>
	 * <TD>0</TD>
	 * </TR>
	 * <TR>
	 * <TD>1</TD>
	 * <TD>2</TD>
	 * <TD>1</TD>
	 * </TR>
	 * </TABLE>
	 */
	public static final int SOBEL_Y = 2;
	/**
	 * Prewitt Kernel with X orientation.
	 * <TABLE BORDER CELLPADDING=5 COLS=3 WIDTH="13" >
	 * <TR>
	 * <TD>-1</TD>
	 * <TD>0</TD>
	 * <TD>1</TD>
	 * </TR>
	 * <TR>
	 * <TD>-1</TD>
	 * <TD>0</TD>
	 * <TD>1</TD>
	 * </TR>
	 * <TR>
	 * <TD>-1</TD>
	 * <TD>0</TD>
	 * <TD>1</TD>
	 * </TR>
	 * </TABLE>
	 */
	public static final int PREWITT_X = 3;
	/**
	 * Prewitt Kernel with Y orientation.
	 * <TABLE BORDER CELLPADDING=5 COLS=3 WIDTH="13" >
	 * <TR>
	 * <TD>-1</TD>
	 * <TD>-1</TD>
	 * <TD>-1</TD>
	 * </TR>
	 * <TR>
	 * <TD>0</TD>
	 * <TD>0</TD>
	 * <TD>0</TD>
	 * </TR>
	 * <TR>
	 * <TD>1</TD>
	 * <TD>1</TD>
	 * <TD>1</TD>
	 * </TR>
	 * </TABLE>
	 */
	public static final int PREWITT_Y = 4;
	/**
	 * Laplacian Kernel.
	 * <TABLE BORDER CELLPADDING=5 COLS=3 WIDTH="13" >
	 * <TR>
	 * <TD>0</TD>
	 * <TD>1</TD>
	 * <TD>0</TD>
	 * </TR>
	 * <TR>
	 * <TD>1</TD>
	 * <TD>4</TD>
	 * <TD>1</TD>
	 * </TR>
	 * <TR>
	 * <TD>0</TD>
	 * <TD>1</TD>
	 * <TD>0</TD>
	 * </TR>
	 * </TABLE>
	 */
	public static final int LAPLACIAN = 5;
	/**
	 * Laplacian 8 Kernel.
	 * <TABLE BORDER CELLPADDING=5 COLS=3 WIDTH="13" >
	 * <TR>
	 * <TD>1</TD>
	 * <TD>1</TD>
	 * <TD>1</TD>
	 * </TR>
	 * <TR>
	 * <TD>1</TD>
	 * <TD>-8</TD>
	 * <TD>1</TD>
	 * </TR>
	 * <TR>
	 * <TD>1</TD>
	 * <TD>1</TD>
	 * <TD>1</TD>
	 * </TR>
	 * </TABLE>
	 */
	public static final int LAPLACIAN_8 = 6;
	/**
	 * Unsharpen Kernel.
	 * <TABLE BORDER CELLPADDING=5 COLS=3 WIDTH="13" >
	 * <TR>
	 * <TD>0</TD>
	 * <TD>-1</TD>
	 * <TD>0</TD>
	 * </TR>
	 * <TR>
	 * <TD>-1</TD>
	 * <TD>5</TD>
	 * <TD>-1</TD>
	 * </TR>
	 * <TR>
	 * <TD>0</TD>
	 * <TD>-1</TD>
	 * <TD>0</TD>
	 * </TR>
	 * </TABLE>
	 */
	public static final int UNSHARP = 7;
	/**
	 * Unsharpen 8 Kernel.
	 * <TABLE BORDER CELLPADDING=5 COLS=3 WIDTH="13" >
	 * <TR>
	 * <TD>-1</TD>
	 * <TD>-1</TD>
	 * <TD>-1</TD>
	 * </TR>
	 * <TR>
	 * <TD>-1</TD>
	 * <TD>9</TD>
	 * <TD>-1</TD>
	 * </TR>
	 * <TR>
	 * <TD>-1</TD>
	 * <TD>-1</TD>
	 * <TD>-1</TD>
	 * </TR>
	 * </TABLE>
	 */
	public static final int UNSHARP_8 = 8;
	/**
	 * D_xx Kernel.
	 * <TABLE BORDER CELLPADDING=5 COLS=3 WIDTH="13" >
	 * <TR>
	 * <TD>1</TD>
	 * <TD>-2</TD>
	 * <TD>1</TD>
	 * </TR>
	 * <TR>
	 * <TD>1</TD>
	 * <TD>-2</TD>
	 * <TD>1</TD>
	 * </TR>
	 * <TR>
	 * <TD>1</TD>
	 * <TD>-2</TD>
	 * <TD>1</TD>
	 * </TR>
	 * </TABLE>
	 */
	public static final int D_XX = 9;
	/**
	 * D_yy Kernel.
	 * <TABLE BORDER CELLPADDING=5 COLS=3 WIDTH="13" >
	 * <TR>
	 * <TD>1</TD>
	 * <TD>1</TD>
	 * <TD>1</TD>
	 * </TR>
	 * <TR>
	 * <TD>-2</TD>
	 * <TD>-2</TD>
	 * <TD>-2</TD>
	 * </TR>
	 * <TR>
	 * <TD>1</TD>
	 * <TD>1</TD>
	 * <TD>1</TD>
	 * </TR>
	 * </TABLE>
	 */
	public static final int D_YY = 10;
	/**
	 * D_xy Kernel.
	 * <TABLE BORDER CELLPADDING=5 COLS=3 WIDTH="13" >
	 * <TR>
	 * <TD>1</TD>
	 * <TD>0</TD>
	 * <TD>-1</TD>
	 * </TR>
	 * <TR>
	 * <TD>0</TD>
	 * <TD>0</TD>
	 * <TD>0</TD>
	 * </TR>
	 * <TR>
	 * <TD>-1</TD>
	 * <TD>0</TD>
	 * <TD>1</TD>
	 * </TR>
	 * </TABLE>
	 */
	public static final int D_XY = 11;

	/**
	 * Creates a kernel of one of the predefined types. <code>val</code> should be between 0 and 11. <br>
	 * 
	 * <pre>
	 * 0 - UNIFORM		1 - SOBEL_X		2 - SOBEL_Y
	 * 	3 - PREWITT_X		4 - PREWITT_Y		5 - LAPLACIAN
	 * 	6 - LAPLACIAN_8		7 - UNSHARP		8 - UNSHARP_8
	 * 	9 - D_XX		10 - D_YY		11 - D_XY
	 * </pre>
	 * 
	 * @param val kernel type
	 * @see jigl.image.ImageKernel#UNIFORM
	 * @see jigl.image.ImageKernel#SOBEL_X
	 * @see jigl.image.ImageKernel#SOBEL_Y
	 * @see jigl.image.ImageKernel#PREWITT_X
	 * @see jigl.image.ImageKernel#PREWITT_Y
	 * @see jigl.image.ImageKernel#LAPLACIAN
	 * @see jigl.image.ImageKernel#LAPLACIAN_8
	 * @see jigl.image.ImageKernel#UNSHARP
	 * @see jigl.image.ImageKernel#UNSHARP_8
	 * @see jigl.image.ImageKernel#D_XX
	 * @see jigl.image.ImageKernel#D_YY
	 * @see jigl.image.ImageKernel#D_XY
	 */
	public ImageKernel(int val) throws InvalidKernelException {

		super(3, 3);

		float[][] data2 = null;

		switch (val) {

		case 0: {
			float[][] data1 = { { 1, 1, 1 }, { 1, 1, 1 }, { 1, 1, 1 } };
			data2 = data1;
			break;
		}

		case 1: {
			float[][] data1 = { { -1, 0, 1 }, { -2, 0, 2 }, { -1, 0, 1 } };
			data2 = data1;
			break;
		}

		case 2: {
			float[][] data1 = { { -1, -2, -1 }, { 0, 0, 0 }, { 1, 2, 1 } };
			data2 = data1;
			break;
		}

		case 3: {
			float[][] data1 = { { -1, 0, 1 }, { -1, 0, 1 }, { -1, 0, 1 } };
			data2 = data1;
			break;
		}

		case 4: {
			float[][] data1 = { { -1, -1, -1 }, { 0, 0, 0 }, { 1, 1, 1 } };
			data2 = data1;
			break;
		}

		case 5: {
			float[][] data1 = { { 0, 1, 0 }, { 1, -4, 1 }, { 0, 1, 0 } };
			data2 = data1;
			break;
		}

		case 6: {
			float[][] data1 = { { 1, 1, 1 }, { 1, -8, 1 }, { 1, 1, 1 } };
			data2 = data1;
			break;
		}

		case 7: {
			float[][] data1 = { { 0, -1, 0 }, { -1, 5, -1 }, { 0, -1, 0 } };
			data2 = data1;
			break;
		}

		case 8: {
			float[][] data1 = { { -1, -1, -1 }, { -1, 9, -1 }, { -1, -1, -1 } };
			data2 = data1;
			break;
		}
			//		case 9: {float[][] data1={{0,0,0},{1,1,0},{0,1,0}};
			//	         data2=data1;
			//				 break;}
		case 9: {
			float[][] data1 = { { 1, -2, 1 }, { 1, -2, 1 }, { 1, -2, 1 } };
			data2 = data1;
			break;
		}

		case 10: {
			float[][] data1 = { { 1, 1, 1 }, { -2, -2, -2 }, { 1, 1, 1 } };
			data2 = data1;
			break;
		}

		case 11: {
			float[][] data1 = { { 1, 0, -1 }, { 0, 0, 0 }, { -1, 0, 1 } };
			data2 = data1;
			break;
		}

		default:
			throw new InvalidKernelException();
		}
		data = data2;
	}

	/**
	 * Creates a uniform kernel of specified size
	 * 
	 * @param val uniform value for the kernel
	 * @param dimension size of kernel (dimension X dimension)
	 */
	public ImageKernel(float val, int dimension) {
		super(dimension, dimension);
		for (int x = 0; x < dimension; x++)
			for (int y = 0; y < dimension; y++)
				data[x][y] = val;

	}

	/**
	 * Creates a kernel out of the given array. The kernel created will have dimensions equal to the
	 * number of elements in the first row.
	 * 
	 * @param dat two-dimensional data array
	 */
	public ImageKernel(float[][] dat) {
		super(dat.length, dat.length);
		data = dat;

	}

	/** Creates an ImageKernel from a RealGrayImage */
	public ImageKernel(RealGrayImage img) {
		super(img);
	}

	/** "Normalizes" the ImageKernel by 255. However the result kernel is not totally normalized. */
	public void normalize255() {
		for (int x = 0; x < X; x++) {
			for (int y = 0; y < Y; y++) {
				data[y][x] = data[y][x] / 255;
			}
		}

	}

	/** Normalize the kernel by total grayscales. */
	public void normalize() {
		float total = 0;
		for (int x = 0; x < X; x++)
			for (int y = 0; y < Y; y++)
				total += data[y][x];
		if (total == 0.0f)
			total = 1.0f;

		for (int x = 0; x < X; x++) {
			for (int y = 0; y < Y; y++) {
				data[y][x] = data[y][x] / total;
			}
		}

	}
}
