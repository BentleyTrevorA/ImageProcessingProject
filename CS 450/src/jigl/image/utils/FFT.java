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
package jigl.image.utils;

import jigl.image.ComplexImage;
import jigl.image.GrayImage;
import jigl.image.Image;
import jigl.image.RealGrayImage;

/**
 * Computes the Fast Fourier Transform of an image. Please note, the input images <i>must</i> be
 * square and a power of two. To get the correct shape of Fourier transform, you should shift the
 * result image both right by half width and up by half height.
 * <p>
 * If you apply a forward FFT and finally apply an inverse FFT, be sure to adjust the range of the
 * final image because it may become very big or small.
 */
public class FFT {
	/** Static constant definition, which is 1 */
	private static final int REAL = 1;
	/** Static constant definition, which is 2 */
	private static final int IMAG = 2;

	// final boolean FORWARD = true;
	// final boolean REVERSE = false;

	/** Default constructor. */
	public FFT() {
	}

	/** Perform the forward Fast Fourier Transform on <code>im</code> */
	public static ComplexImage forward(Image im) {
		return doFFT(im, true);
	}

	/** Perform the forward Fast Fourier Transform on <code>im</code> */
	public static ComplexImage reverse(Image im) {
		return doFFT(im, false);
	}

	/** Utility function */
	private static ComplexImage doFFT(Image im, boolean forward) {
		if (im.X() != im.Y()) {
			System.out
					.println("Error: the width and height of the image must be same in order to apply FFT.");
			return null;
		} else {
			// check if power of 2
			int temp = im.X();
			while (temp % 2 == 0)
				temp = temp / 2;
			if (temp != 1) {
				System.out
						.println("Error: the width and height of the image should be power of 2 in order to apply FFT.");
				return null;
			}
		}

		int dimlen[] = new int[3];
		dimlen[1] = im.X();
		dimlen[2] = im.Y();

		GrayImage gim = null;
		RealGrayImage rgim = null;
		ComplexImage cim = null;

		if (im instanceof GrayImage) {
			gim = (GrayImage) im;
			int d1 = dimlen[1];
			int d2 = dimlen[2];
			// +1 for 1-based arrays and *2 for complex
			double fftdata[] = new double[(d1 + 1) * (d2 + 1) * 2];

			// FFT fft = new FFT();
			for (int j = 0; j < d2; j++)
				// for each row
				for (int i = 0; i < d1; i++) {
					int addrbase = 2 * (j * d1 + i);
					fftdata[addrbase + REAL] = (double) gim.get(i, j);
					fftdata[addrbase + IMAG] = 0.0;
				} // for i

			return fourn(fftdata, dimlen, 2, forward);
		} else if (im instanceof RealGrayImage) {
			rgim = (RealGrayImage) im;
			int d1 = dimlen[1];
			int d2 = dimlen[2];
			// +1 for 1-based arrays and *2 for complex
			double fftdata[] = new double[(d1 + 1) * (d2 + 1) * 2];

			// FFT fft = new FFT();
			for (int j = 0; j < d2; j++)
				// for each row
				for (int i = 0; i < d1; i++) {
					int addrbase = 2 * (j * d1 + i);
					fftdata[addrbase + REAL] = (double) rgim.get(i, j);
					fftdata[addrbase + IMAG] = 0.0;
				} // for i
			return fourn(fftdata, dimlen, 2, forward);
		} else if (im instanceof ComplexImage) {
			cim = (ComplexImage) im;
			int d1 = dimlen[1];
			int d2 = dimlen[2];
			// +1 for 1-based arrays and *2 for complex
			double fftdata[] = new double[(d1 + 1) * (d2 + 1) * 2];

			// FFT fft = new FFT();
			for (int j = 0; j < d2; j++)
				// for each row
				for (int i = 0; i < d1; i++) {
					int addrbase = 2 * (j * d1 + i);
					fftdata[addrbase + REAL] = (double) cim.real().get(i, j);
					fftdata[addrbase + IMAG] = (double) cim.imag().get(i, j);
				} // for i
			return fourn(fftdata, dimlen, 2, forward);
		}

		return null;

	}

	/**
	 * FIXME: Refactor so there aren't 27 variables in this method and so the code is readable
	 * Utility methods
	 */
	private static ComplexImage fourn(double data[], int nn[], int ndim, boolean forward) {

		int i1, i2, i3, i2rev, i3rev;
		int ip1, ip2, ip3, ifp1, ifp2;
		int ibit, idim, k1, k2, n, nprev, nrem, ntot;
		double tempi, tempr, theta, wi, wpi, wpr, wr, wtemp, sign2pi;

		if (forward)
			sign2pi = 2 * Math.PI;
		else
			sign2pi = -2 * Math.PI;
		ntot = 1;
		for (idim = 1; idim <= ndim; idim++)
			ntot *= nn[idim];
		nprev = 1;

		for (idim = ndim; idim >= 1; idim--) {
			n = nn[idim];
			nrem = ntot / (n * nprev);
			ip1 = nprev << 1;
			ip2 = ip1 * n;
			ip3 = ip2 * nrem;
			i2rev = 1;
			for (i2 = 1; i2 <= ip2; i2 += ip1) { /* bit reversal */
				if (i2 < i2rev) {
					for (i1 = i2; i1 <= i2 + ip1 - 2; i1 += 2) {
						for (i3 = i1; i3 <= ip3; i3 += ip2) {
							i3rev = i2rev + i3 - i2;
							tempr = data[i3];
							data[i3] = data[i3rev];
							data[i3rev] = tempr;
							tempr = data[i3 + 1];
							data[i3 + 1] = data[i3rev + 1];
							data[i3rev + 1] = tempr;
						} // for i3
					} // for i1
				} // if i2
				ibit = ip2 >> 1;
				while (ibit >= ip1 && i2rev > ibit) {
					i2rev -= ibit;
					ibit >>= 1;
				} // while ibit
				i2rev += ibit;
			} // for i2
			ifp1 = ip1;
			while (ifp1 < ip2) {
				ifp2 = ifp1 << 1;
				theta = sign2pi * ip1 / ifp2;
				wtemp = Math.sin(0.5 * theta);
				wpr = -2.0 * wtemp * wtemp;
				wpi = Math.sin(theta);
				wr = 1.0;
				wi = 0.0;
				for (i3 = 1; i3 <= ifp1; i3 += ip1) {
					for (i1 = i3; i1 <= i3 + ip1 - 2; i1 += 2) {
						for (i2 = i1; i2 <= ip3; i2 += ifp2) {
							k1 = i2;
							k2 = k1 + ifp1;
							tempr = wr * data[k2] - wi * data[k2 + 1];
							tempi = wr * data[k2 + 1] + wi * data[k2];
							data[k2] = data[k1] - tempr;
							data[k2 + 1] = data[k1 + 1] - tempi;
							data[k1] += tempr;
							data[k1 + 1] += tempi;
						} // for i2
					} // for i1
					wr = (wtemp = wr) * wpr - wi * wpi + wr;
					wi = wi * wpr + wtemp * wpi + wi;
				} // for i3
				ifp1 = ifp2;
			} // while ifp1 < ip2
			nprev *= n;
		} // for idim
		return convertToComplex(data, nn);
	}

	/** Utility method */
	private static ComplexImage convertToComplex(double data[], int nn[]) {
		ComplexImage ci = new ComplexImage(nn[1], nn[2]);
		int addrbase = 0;
		for (int i = 0; i < nn[1]; i++)
			for (int j = 0; j < nn[2]; j++) {
				addrbase = 2 * (j * nn[1] + i);
				ci.real().set(i, j, (float) data[addrbase + REAL]);
				ci.imag().set(i, j, (float) data[addrbase + IMAG]);
			}
		return ci;
	}
}
