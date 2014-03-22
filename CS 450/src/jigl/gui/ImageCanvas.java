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
package jigl.gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import jigl.image.BadImageException;
import jigl.image.BinaryImage;
import jigl.image.ColorImage;
import jigl.image.ComplexImage;
import jigl.image.GrayImage;
import jigl.image.RealColorImage;
import jigl.image.RealGrayImage;

/**
 * ImageCanvas is a class made to facilitate the displaying of a JIGL image. ImageCanvas also easily
 * supports a highlight box when active and a mouse drawn selection box. These options can be turned
 * on or off with the showSelectionBox() and showActiveBox() methods. The options are meant to be
 * used with the MouseListener and MouseMotionListener methods,and can be used as in the example
 * code (demonstrating the selecton box):
 * <p>
 * Supports JIGL images, Java images (.gif, .jpg, .jpeg),and PPM images (.ppm, .pgm, .pbm).
 * <P>
 * 
 * <pre>
 * //MouseListener method
 * public void mousePressed(MouseEvent e) {
 * 	if (e.getComponent() instanceof jigl.gui.ImageCanvas) {
 * 		jigl.gui.ImageCanvas canvas = (jigl.gui.ImageCanvas) e.getComponent();
 * 		canvas.clearSelectionBox();
 * 		canvas.setSelectionBoxAnchor(e.getX(), e.getY());
 * 	}
 * }
 * 
 * //MouseMotionListener method
 * public void mouseDragged(MouseEvent e) {
 * 	if (e.getComponent() instanceof jigl.gui.ImageCanvas) {
 * 		jigl.gui.ImageCanvas canvas = (jigl.gui.ImageCanvas) e.getComponent();
 * 		canvas.setSelectionBoxExtent(e.getX(), e.getY());
 * 	}
 * }
 * </pre>
 */
public class ImageCanvas extends Canvas {
	private static final long serialVersionUID = 1L;

	//  MediaTracker m_tracker = null;

	/** Java image */
	protected java.awt.Image image;
	/** JIGL image */
	protected jigl.image.Image jimage;

	private boolean m_bShowSelectionBox = false;
	private boolean m_bShowActive = false;
	private boolean m_bIsActive = false;
	private Color m_activeColor = Color.blue;

	private int m_boxlx = 0;
	private int m_boxly = 0;
	private int m_boxux = 0;
	private int m_boxuy = 0;

	/** Creates an empty ImageCanvas */
	public ImageCanvas() {
		super();
	}

	/**
	 * Creates an ImageCanvas from a GrayImage
	 * 
	 * @param image GrayImage
	 * @throws BadImageException
	 */
	public ImageCanvas(GrayImage image) throws BadImageException {
		super();
		this.image = getJavaImage(image);
		jimage = image;

	}

	/**
	 * Creates an ImageCanvas from a ColorImage
	 * 
	 * @param image ColorImage
	 * @throws BadImageException
	 */
	public ImageCanvas(ColorImage image) throws BadImageException {
		super();
		this.image = getJavaImage(image);
		jimage = image;

	}

	/**
	 * Creates an ImageCanvas from a RealGrayImage
	 * 
	 * @param image RealGrayImage
	 * @throws BadImageException
	 */
	public ImageCanvas(RealGrayImage image) throws BadImageException {
		super();
		this.image = getJavaImage(image);
		jimage = image;

	}

	/**
	 * Creates an ImageCanvas from a RealColorImage
	 * 
	 * @param image RealColorImage
	 * @throws BadImageException
	 */
	public ImageCanvas(RealColorImage image) throws BadImageException {
		super();
		this.image = getJavaImage(image);
		jimage = image;

	}

	/**
	 * Creates an ImageCanvas from a ComplexImage
	 * 
	 * @param image ComplexImage
	 * @throws BadImageException
	 */
	public ImageCanvas(ComplexImage image) throws BadImageException {
		super();
		this.image = getJavaImage(image);
		jimage = image;

	}

	/**
	 * Creates an ImageCanvas from a JIGL Image
	 * 
	 * @param image JIGL Image
	 * @throws BadImageException
	 */
	public ImageCanvas(jigl.image.Image image) throws BadImageException {
		super();
		this.image = getJavaImage(image);
		jimage = image;

	}

	/**
	 * Sets the Java image to <CODE>image</CODE> and JIGL Image to null
	 * 
	 * @param image java.awt.Image
	 * @see java.awt.Image
	 */
	public void setImage(java.awt.Image image) {
		this.image = image;
		jimage = null;
	}

	/**
	 * Sets the JIGL Image and Java Image to <CODE>image</CODE>
	 * 
	 * @param image JIGL Image
	 * @throws BadImageException
	 */
	public void setImage(jigl.image.Image image) throws BadImageException {
		this.image = getJavaImage(image);
		jimage = image;
	}

	/**
	 * Returns the JIGL image
	 * 
	 * @return JIGL Image
	 */
	public jigl.image.Image getImage() {
		return jimage;
	}

	/**
	 * Returns the Java image
	 * 
	 * @return java.awt.Image
	 * @see java.awt.Image
	 */
	public java.awt.Image getJavaImage() {
		return image;
	}

	/**
	 * Returns an instance of Graphics that, when modified, modifies the Java image
	 * 
	 * @return Graphics that is tied to the java.awt.Image
	 * @see java.awt.Image
	 */
	public Graphics setOffScreen() {
		java.awt.Image tempImage;
		Graphics g;

		tempImage = image;
		image = this.createImage(image.getWidth(this), image.getHeight(this));
		g = image.getGraphics();
		g.drawImage(tempImage, 0, 0, this);

		return g;

	}

	/**
	 * Returns an instance of Graphics that, when modified, modifies the Java image
	 * 
	 * @param xfactor Zoom of x axis
	 * @param yfactor Zoom of y axis
	 * @return Graphics tied to the java.awt.Image
	 * @see java.awt.Image
	 */
	public Graphics setOffScreen(double xfactor, double yfactor) {
		java.awt.Image tempImage;
		Graphics g;

		tempImage = image;
		image = this.createImage((int) (image.getWidth(this) * xfactor), (int) (image
				.getHeight(this) * yfactor));

		g = image.getGraphics();

		g.drawImage(tempImage, 0, 00, (int) (tempImage.getWidth(this) * xfactor), (int) (tempImage
				.getHeight(this) * yfactor), this);

		return g;
	}

	/**
	 * Overrides Component.setVisible(boolean)
	 * 
	 * @param b flag. true == visible.
	 */
	public void setVisible(boolean b) {
		setSize(imWidth(), imHeight());
		super.setVisible(b);

	}

	/**
	 * Returns the image height
	 * 
	 * @return image height
	 */
	public int imHeight() {
		return image.getHeight(this);
	}

	/**
	 * Returns the image width
	 * 
	 * @return image width
	 */
	public int imWidth() {
		return image.getWidth(this);
	}

	/**
	 * Takes a jigl image as input, converts it to a java image, and returns the java image.
	 * 
	 * @param img The JIGL image.
	 * @throws BadImageException
	 * @return java.awt.Image
	 * @see java.awt.Image
	 */
	public java.awt.Image getJavaImage(jigl.image.Image img) throws BadImageException//, RuntimeException
	{

		int w = 0;
		int h = 0;
		java.awt.Image jimg = null;

		//      short max=Short.MIN_VALUE;
		//      short min=Short.MAX_VALUE;
		if (img instanceof GrayImage) {
			GrayImage im = (GrayImage) img;
			w = im.X();
			h = im.Y();
			jimg = createImage(im.getJavaImage());
		} else if (img instanceof BinaryImage) {
			BinaryImage im = (BinaryImage) img;
			w = im.X();
			h = im.Y();
			jimg = createImage(im.getJavaImage());
		} else if (img instanceof ColorImage) {
			ColorImage im = (ColorImage) img;
			w = im.X();
			h = im.Y();
			jimg = createImage(im.getJavaImage());
		} else if (img instanceof RealGrayImage) {
			RealGrayImage im = (RealGrayImage) img;
			w = im.X();
			h = im.Y();
			jimg = createImage(im.getJavaImage());
		} else if (img instanceof RealColorImage) {
			RealColorImage im = (RealColorImage) img;
			w = im.X();
			h = im.Y();
			jimg = createImage(im.getJavaImage());
		} else if (img instanceof ComplexImage) {
			ComplexImage im = (ComplexImage) img;
			w = im.X();
			h = im.Y();
			jimg = createImage(im.getJavaImage());
		} else {
			throw new BadImageException();
		}

		w += 0;
		h += 0;

		if (w > getSize().width) {
			setSize(w, h);
		}
		if (h > getSize().height) {
			setSize(w, h);
		}

		return jimg;
	}

	/**
	 * Repaints the ImageCanvas. Also draws the selection box and active border if those options are
	 * enabled.
	 * 
	 * @param g Graphics
	 */
	public void update(Graphics g) {
		g.drawImage(image, 0, 0, this);
		if (m_bShowSelectionBox) {
			if (m_boxux != m_boxlx || m_boxuy != m_boxly) {
				g.setXORMode(Color.white);
				drawBox(g, m_boxux, m_boxuy, m_boxlx, m_boxly);
			}
		}
		if (m_bShowActive) {
			if (m_bIsActive) {
				drawActiveBox(g);
			}
		}
	}

	/** Draw the active box. */
	private void drawActiveBox(Graphics g) {
		g.setXORMode(m_activeColor);
		drawBox(g, 0, 0, imWidth() - 1, imHeight() - 1);
		drawBox(g, 1, 1, imWidth() - 2, imHeight() - 2);
	}

	/** Draw a rectangle. */
	private void drawBox(Graphics g, int ux, int uy, int lx, int ly) {
		g.drawLine(ux, uy, lx, uy);
		g.drawLine(ux, ly, lx, ly);
		g.drawLine(ux, uy, ux, ly);
		g.drawLine(lx, uy, lx, ly);
	}

	/**
	 * Overrides the paint method for smoother redraw
	 * 
	 * @param g Graphics object
	 */
	public void paint(Graphics g) {
		update(g);
	}

	/**
	 * Sets the upper corner of the selection box (ux and uy). Must be followed by
	 * setSelectionBoxExtent for a box to actually be drawn.
	 * 
	 * @param ux Upper x coordinate of box
	 * @param uy Upper y coordinate of box
	 */
	public void setSelectionBoxAnchor(int ux, int uy) {
		m_boxux = ux;
		m_boxuy = uy;

		if (m_boxux < 0)
			m_boxux = 0;
		else if (m_boxux > imWidth() - 1)
			m_boxux = imWidth() - 1;
		if (m_boxuy < 0)
			m_boxuy = 0;
		else if (m_boxuy > imHeight() - 1)
			m_boxuy = imHeight() - 1;

		m_boxlx = m_boxux;
		m_boxly = m_boxuy;
	}

	/**
	 * Sets the lower corner of the selection box (lx and ly). The selection box anchor must have
	 * been set previously for this to draw a box.
	 * 
	 * @param lx Lower x coordinate of box
	 * @param ly Lower y coordinate of box
	 */
	public void setSelectionBoxExtent(int lx, int ly) {
		Graphics g = this.getGraphics();
		g.setXORMode(Color.white);

		//erase box
		if (m_boxux != m_boxlx || m_boxuy != m_boxly) {
			if (m_bShowSelectionBox) {
				drawBox(g, m_boxux, m_boxuy, m_boxlx, m_boxly);
			}
		}

		m_boxlx = lx;
		m_boxly = ly;

		if (m_boxlx < 0)
			m_boxlx = 0;
		else if (m_boxlx > imWidth() - 1)
			m_boxlx = imWidth() - 1;
		if (m_boxly < 0)
			m_boxly = 0;
		else if (m_boxly > imHeight() - 1)
			m_boxly = imHeight() - 1;

		//draw new box
		if (m_bShowSelectionBox) {
			drawBox(g, m_boxux, m_boxuy, m_boxlx, m_boxly);
		}
	}

	/**
	 * Sets whether or not selection box is displayed.
	 * 
	 * @param b true = box is displayed, false = box is not displayed.
	 */
	public void showSelectionBox(boolean b) {
		if (m_bShowSelectionBox == b)
			return;

		m_bShowSelectionBox = b;

		//if box is set, show or hide it (depending on b)
		if (m_boxux != m_boxlx || m_boxuy != m_boxly) {
			Graphics g = this.getGraphics();
			g.setXORMode(Color.white);
			drawBox(g, m_boxux, m_boxuy, m_boxlx, m_boxly);
		}
	}

	/** Clears the selection box. */
	public void clearSelectionBox() {
		//erase box, if drawn
		if (m_boxux != m_boxlx || m_boxuy != m_boxly) {
			if (m_bShowSelectionBox) {
				Graphics g = this.getGraphics();
				g.setXORMode(Color.white);
				drawBox(g, m_boxux, m_boxuy, m_boxlx, m_boxly);
			}
		}

		//clear box
		m_boxux = m_boxuy = m_boxlx = m_boxly = 0;
	}

	/**
	 * Returns the selection box in a Rectangle object. Returns <code>null</code> if no selection
	 * box is set.
	 * 
	 * @return Current selection box
	 */
	public Rectangle getSelectionBox() {
		if (m_boxux == m_boxlx && m_boxuy == m_boxly)
			return null;
		else
			return new Rectangle(m_boxux, m_boxuy, m_boxlx - m_boxux, m_boxly - m_boxuy);
	}

	/**
	 * Returns the upper x (anchor) coordinate of the selection box.
	 * 
	 * @return x anchor coordinate of the selection box.
	 */
	public int getSelectionBoxAnchorX() {
		return m_boxux;
	}

	/**
	 * Returns the upper y (anchor) coordinate of the selection box.
	 * 
	 * @return y anchor coordinate of the selection box.
	 */
	public int getSelectionBoxAnchorY() {
		return m_boxuy;
	}

	/**
	 * Rturns the lower x (extent) coordinate of the selection box.
	 * 
	 * @return x extent coordinate of the selection box.
	 */
	public int getSelectionBoxExtentX() {
		return m_boxlx;
	}

	/**
	 * Returns the lower y (extent) coordinate of the selection box.
	 * 
	 * @return y extent coordinate of the selection box.
	 */
	public int getSelectionBoxExtentY() {
		return m_boxly;
	}

	/**
	 * Sets whether or not the image is active. If showActiveBox is set to true, the image will have
	 * a highlight border when active.
	 * 
	 * @param active true = the image is active, false = the image is not active.
	 */
	public void setActive(boolean active) {
		if (m_bIsActive == active)
			return;

		m_bIsActive = active;
		if (m_bShowActive) {
			drawActiveBox(this.getGraphics());
		}
	}

	/**
	 * Sets whether or not the image is highlighted with a border when activated.
	 * 
	 * @param b true = show border when active, false = don't show border
	 */
	public void showActiveBox(boolean b) {
		if (m_bShowActive == b)
			return;

		m_bShowActive = b;
		if (m_bIsActive) {
			drawActiveBox(this.getGraphics());
		}
	}

	/**
	 * Sets the color used to highlight the image when active (default is blue).
	 * 
	 * @param color Color object.
	 */
	public void setActiveColor(Color color) {
		m_activeColor = color;
	}
}
