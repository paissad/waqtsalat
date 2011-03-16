/*
 * WaqtSalat, for indicating the muslim prayers times in most cities.
 * Copyright (C) 2011  Papa Issa DIAKHATE (paissad).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * PLEASE DO NOT REMOVE THIS COPYRIGHT BLOCK.
 * 
 */

package net.waqtsalat.gui.addons;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.imageio.ImageIO;

import java.io.File;
import java.io.IOException;

/**
 * A JLabel which contains an image. The image associated to the JLable is
 * always resized to automatically fits into the JLabel.
 * 
 * @author Papa Issa DIAKHATE (<a href="mailto:paissad@gmail.com">paissad</a>)
 */
public class ImageLabel extends JLabel {

	private static final long serialVersionUID = 1L;

	private String _imageFileName = "";
	private Image _originalImage = null;

	// =======================================================================

	/**
	 * Constructs a {@link ImageLabel} using the specified image filename.
	 * @param imageFileName The image filename to use.
	 */
	public ImageLabel(String imageFileName) {
		if(imageFileName != null && !imageFileName.isEmpty())
			initImage(imageFileName);
	}
	// =======================================================================

	public ImageLabel(ImageIcon icon) {
		if(icon != null)
			_originalImage = icon.getImage();

	}
	// =======================================================================

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		int width = g.getClipBounds().width;
		int height =  g.getClipBounds().height;
		g.drawImage(_originalImage, 0, 0, width, height, null);
	}
	// =======================================================================

	/**
	 * Set the image filename to use for the JLabel.
	 * @param imageFileName
	 */
	public void setImageFileName(String imageFileName) {
		_imageFileName = imageFileName;
		initImage(_imageFileName);
	}
	// =======================================================================

	/**
	 * Set the image to use for the JLabel.
	 * @param image
	 */
	public void setOriginalImage(Image image) {
		_originalImage = image;
	}
	// =======================================================================

	private void initImage(String imageFileName) {
		try {
			_originalImage = ImageIO.read(new File(imageFileName));
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
	// =======================================================================

}
