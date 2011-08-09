/*
 * WaqtSalat, for indicating the muslim prayers times in most cities. Copyright
 * (C) 2011 Papa Issa DIAKHATE (paissad).
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 * 
 * PLEASE DO NOT REMOVE THIS COPYRIGHT BLOCK.
 */

package net.paissad.waqtsalat.gui.addons;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * A JLabel which contains an image. The image associated to the JLabel is
 * always resized to automatically fits into the JLabel.
 * 
 * @author Papa Issa DIAKHATE (<a href="mailto:paissad@gmail.com">paissad</a>)
 */
public class ImageLabel extends JLabel {

    private static final long serialVersionUID = 1L;
    private Image             _image           = null;

    // =======================================================================
    // Constructors.

    /**
     * Constructs an <b>ImageLabel</b> component using the specified image
     * filename.
     * 
     * @param imageFileName
     *            The image filename to use.
     * @throws IOException
     */
    public ImageLabel(String imageFileName) throws IOException {
        this(createImageFromFilename(imageFileName));
    }

    /**
     * Constructs an <b>ImageLabel</b> component using the specified image.
     * 
     * @param image
     */
    public ImageLabel(Image image) {
        _image = image;
    }

    /**
     * Constructs an <b>ImageLabel</b> component using the specified icon.
     * 
     * @param icon
     *            The icon to use.
     */
    public ImageLabel(ImageIcon icon) {
        if (icon == null)
            throw new NullPointerException("The icon cannot be null.");
        _image = icon.getImage();
    }

    // =======================================================================
    // Getters / Setters.

    /**
     * Sets the image filename to use for the JLabel.
     * 
     * @param imageFileName
     *            The name of the file to use.
     * @throws IOException
     */
    public void setImageFileName(String imageFileName) throws IOException {
        this.setImage(createImageFromFilename(imageFileName));
    }

    public void setImage(Image image) {
        _image = image;
    }

    public Image getImage() {
        return _image;
    }

    // =======================================================================

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Rectangle clippingArea = g.getClipBounds();
        if (clippingArea == null)
            throw new NullPointerException("The clipping area must not be null");
        int width = clippingArea.width;
        int height = clippingArea.height;
        g.drawImage(_image, 0, 0, width, height, null);
    }

    private static Image createImageFromFilename(String imageFileName)
            throws IOException {
        return ImageIO.read(new File(imageFileName));
    }

    // =======================================================================

    /*
     * For testing purpose only !
     */
    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        ImageLabel imgLabel = new ImageLabel("src/main/resources/images/sunset_red.jpg");

        // It's simpler to use this layout.
        panel.setLayout(new BorderLayout(0, 0));
        panel.add(imgLabel);
        frame.add(panel);
        frame.setSize(100, 100);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

}
