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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;

/**
 * Represents a simple window that will be shown in one of the corners of the
 * screen depending on the settings.<br>
 * Methods of this class are not guaranteed to be completely thread-safe.
 * 
 * @author Papa Issa DIAKHATE (<a href="mailto:paissad@gmail.com">paissad</a>)
 */
public class NotificationWindow extends JFrame implements MouseListener {

    private static final long serialVersionUID = 1L;

    /**
     * The corner where to show the notification window.<br>
     * Possible values are:<br>
     * <i>TOP_RIGHT</i>, <i>TOP_LEFT</i>, <i>BOTTOM_RIGHT</i> and
     * <i>BOTTOM_LEFT</i>.<br>
     * By default, the window is always on top of other windows.
     * 
     * @author Papa Issa DIAKHATE (<a
     *         href="mailto:paissad@gmail.com">paissad</a>)
     * 
     */
    public static enum Corner {
        TOP_RIGHT,
        TOP_LEFT,
        BOTTOM_RIGHT,
        BOTTOM_LEFT,
    }

    private static final Dimension SCREEN_SIZE        = Toolkit
                                                              .getDefaultToolkit()
                                                              .getScreenSize();
    public static final Corner     DEFAULT_CORNER     = Corner.BOTTOM_RIGHT;
    public static final int        DEFAULT_SPEED      = 8;
    private static final int       SHOW_NOTIFICATION  = 0;
    private static final int       HIDE_NOTIFICATION  = 1;

    /**
     * The screen's corner where to place the notification window. (optional)
     */
    private Corner                 _corner;

    /**
     * The default dimension of the notification window.
     */
    private Dimension              _defaultWindowSize = new Dimension(
                                                              SCREEN_SIZE.width / 4,
                                                              SCREEN_SIZE.height / 8);

    /**
     * The message to put into the notification window. (optional)
     */
    private String                 _message;

    /**
     * The icon to put into the notification window. (optional)
     */
    private Icon                   _icon;

    /**
     * The speed of showing/hiding the notification window. (in pixels,
     * optional)
     */
    private int                    _speed;

    /**
     * Whether or not to slide the notification window will hiding it.<br>
     * Default is <code>true</code>.
     */
    private boolean                _slide             = true;

    private JPanel                 _panel;
    private JLabel                 _label;

    // =======================================================================
    // Constructors ...

    /**
     * Constructs a simple notification window with no specified message and
     * icon, but using the default {@link Corner} <i>DEFAULT_CORNER</i>.
     */
    public NotificationWindow() {
        this(null, DEFAULT_CORNER, null);
    }

    /**
     * Constructs a simple notification window with the specified message and
     * using the default corner <i>DEFAULT_CORNER</i>.
     * 
     * @param message
     *            The message to show.
     */
    public NotificationWindow(String message) {
        this(message, DEFAULT_CORNER, null);
    }

    /**
     * Constructs a simple notification window using the specified
     * <code>Icon</code>, but using the default corner <i>DEFAULT_CORNER</i>.
     * 
     * @param icon
     *            The icon to use.
     */
    public NotificationWindow(Icon icon) {
        this(null, DEFAULT_CORNER, icon);
    }

    /**
     * Constructs a simple notification window using the specified
     * <code>Corner</code>.
     * 
     * @param corner
     *            The corner from where to show the window notification.
     */
    public NotificationWindow(Corner corner) {
        this(null, corner, null);
    }

    /**
     * Constructs a simple notification window using the specified message and
     * <code>Corner</code>.
     * 
     * @param message
     *            The message to show.
     * @param corner
     *            The corner from where to show the window notification.
     */
    public NotificationWindow(String message, Corner corner) {
        this(message, corner, null);
    }

    /**
     * Constructs a notification window.
     * 
     * @param message
     *            The message to show into the notification window.
     * @param corner
     *            The {@link Corner} from where to show the notification window.
     * @param icon
     *            The icon to use.
     */
    public NotificationWindow(String message, Corner corner, Icon icon) {
        this(message, corner, icon, DEFAULT_SPEED);
    }

    /**
     * Constructs a notification window.
     * 
     * @param message
     *            The message to show into the notification window.
     * @param corner
     *            The {@link Corner} from where to show the notification window.
     * @param icon
     *            The icon to use.
     * @param speed
     *            The speed of showing/hiding the notification window. (in
     *            pixels)
     */
    public NotificationWindow(String message, Corner corner, Icon icon,
            int speed) {

        this.setCorner(corner);
        this.setSize(this.getDefaultWindowSize());
        this.setAlwaysOnTop(true);
        this.setResizable(false);
        this.setUndecorated(true);
        _panel = new JPanel(new BorderLayout());
        _panel.setBorder(new CompoundBorder(new BevelBorder(BevelBorder.RAISED,
                new Color(0, 0, 0), null, null, null), new BevelBorder(
                BevelBorder.LOWERED, new Color(0, 0, 0), null, null, null)));
        _label = new customJLabel();
        this.setMessage(message);
        this.setIcon(icon);
        _panel.add(this.getJLabel());
        getContentPane().add(_panel, BorderLayout.CENTER);
        this.setSpeed(speed);
        this.addMouseListener(this);
        this.setDefaultLocation();
    }

    // =======================================================================

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
        hideNotification(this.isSlide());
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    // =======================================================================

    private class customJLabel extends JLabel {

        private static final long serialVersionUID = 1L;

        customJLabel() {
        }

        @Override
        protected void paintComponent(Graphics g) {
            /*
             * if (!isOpaque()) { System.out.println("IS NOT OPAQUE"); return; }
             */

            int w = this.getWidth();
            int h = this.getHeight();
            Color color1 = getBackground();
            Color color2 = color1.darker();

            color1 = new Color(color1.getRed(), color1.getGreen(),
                    color1.getBlue(), 0);
            color2 = new Color(color2.getRed(), color2.getGreen(),
                    color2.getBlue(), 255);
            // Paint a gradient from top to bottom.
            GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);

            /*
             * Normally, if the component is opaque, the UI delegate starts by
             * filling the background with a constant color. But this will cover
             * up your custom background. To prevent this, call setOpaque(false)
             * before calling super.paintComponent( ), then setOpaque(true)
             * again after the component has drawn itself. This tricks the UI
             * delegate into thinking the background should be left alone.
             */
            Graphics2D g2d = (Graphics2D) getGraphics();
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, w, h);
            setOpaque(false);
            super.paintComponent(g);
            setOpaque(true);
        }
    }

    // =======================================================================

    /**
     * @param The
     *            corner where the notification window is placed.
     * @return <code>true</code> if we must continue to show the window
     *         notification, <code>false</code> otherwise.
     */
    private boolean continueShowingNotification(Corner corner) {

        boolean continueToShow = true;
        int current_Y = getBounds().y;
        switch (corner) {
            case BOTTOM_LEFT:
            case BOTTOM_RIGHT:
                if (current_Y <= (SCREEN_SIZE.height - getSize().height)) {
                    continueToShow = false;
                }
                break;
            case TOP_LEFT:
            case TOP_RIGHT:
                if (current_Y >= 0) {
                    continueToShow = false;
                }
                break;
        }

        return continueToShow;
    }

    // =======================================================================

    /**
     * Shows a notification window during a specified amount of time (in
     * seconds).<br>
     * If <i>duration</i> is set to a negative value, the notification window
     * remains sticky, it won't hide until you call the
     * {@link #hideNotification(boolean)} method.
     * 
     * @param duration
     *            The number of seconds to show/keep the notification before
     *            hiding it.
     */
    public synchronized void showNotification(final int duration) {
        new Thread() {
            @Override
            public void run() {
                setVisible(true);
                Corner corner = getCorner();
                while (continueShowingNotification(corner)) {
                    try {
                        Thread.sleep(100);
                        moveNotificationWindow(SHOW_NOTIFICATION);
                    } catch (InterruptedException e) {
                    }
                }
                if (duration >= 0) {
                    try {
                        Thread.sleep(duration * 1000L);
                    } catch (InterruptedException e) {
                    }
                    hideNotification(isSlide());
                } else {
                    // Keep sticky ;-)
                }
            }
        }.start();
    }

    // =======================================================================

    /**
     * Verify whether or not we can continue to hide the notification window.
     * 
     * @return <code>true</code> if we can continue to hide/slide the
     *         notification window, <code>false</code> otherwise.
     */
    private boolean continueHidingNotification(Corner corner) {
        boolean continueToHide = true;
        int current_Y = getBounds().y;

        switch (corner) {
            case BOTTOM_LEFT:
            case BOTTOM_RIGHT:
                if (current_Y >= SCREEN_SIZE.height) {
                    continueToHide = false;
                }
                break;
            case TOP_LEFT:
            case TOP_RIGHT:
                if (current_Y <= -getSize().height) {
                    continueToHide = false;
                }
                break;
        }
        return continueToHide;
    }

    // =======================================================================

    /**
     * Hides a notification window. This calls the {@link #isSlide()} method in
     * order to determine whether or not the sliding feature must be used.
     * 
     * @see #hideNotification(boolean)
     */
    public synchronized void hideNotification() {
        hideNotification(isSlide());
    }

    // =======================================================================

    /**
     * Hides a notification window.
     * 
     * @param slide
     *            <code>true</code> for enabling sliding, <code>false</code> for
     *            hiding directly the window without sliding.
     */
    public synchronized void hideNotification(final boolean slide) {
        new Thread() {
            @Override
            public void run() {
                if (slide) {
                    Corner corner = getCorner();
                    while (continueHidingNotification(corner)) {
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                        }
                        moveNotificationWindow(HIDE_NOTIFICATION);
                    }
                } else {
                    // no sliding ...
                }
                dispose();
            }
        }.start();
    }

    // =======================================================================

    private void moveNotificationWindow(int showOrHide) {
        Corner corner = getCorner();
        if (corner == null) {
            throw new NullPointerException("Corner cannot be null !!!");
        }

        Rectangle bounds = getBounds();
        int dy = getSpeed();

        switch (corner) {
            case BOTTOM_RIGHT:
            case BOTTOM_LEFT:
                if (showOrHide == SHOW_NOTIFICATION) {
                    dy = -dy;
                }
                bounds.y += dy;
                break;
            case TOP_RIGHT:
            case TOP_LEFT:
                if (showOrHide == HIDE_NOTIFICATION) {
                    dy = -dy;
                }
                bounds.y += dy;
                break;
            default:
                corner = DEFAULT_CORNER;
                moveNotificationWindow(showOrHide);
                break;
        }
        setBounds(bounds);
    }

    // =======================================================================

    /**
     * Put the notification window into the correct place (corner ...) onto the
     * screen.<br>
     * This method is called once by the constructor.
     */
    private void setDefaultLocation() {

        Corner corner = getCorner();

        if (corner == null) {
            throw new NullPointerException("Corner cannot be null !!!");
        }

        int x = 0, y = 0;
        Dimension windowSize = getSize();
        switch (corner) {
            case BOTTOM_RIGHT:
                x = SCREEN_SIZE.width - windowSize.width;
                y = SCREEN_SIZE.height;
                break;
            case BOTTOM_LEFT:
                x = 0;
                y = SCREEN_SIZE.height;
                break;
            case TOP_RIGHT:
                x = SCREEN_SIZE.width - windowSize.width;
                y = -windowSize.height;
                break;
            case TOP_LEFT:
                x = 0;
                y = -windowSize.height;
                break;
            default:
                corner = DEFAULT_CORNER;
                setDefaultLocation();
                break;
        }
        setLocation(x, y);
    }

    // =======================================================================
    // Getters / Setters ...

    /**
     * Set the {@link Corner} to use: from where to show the notification window
     * onto the screen.
     * 
     * @param corner
     *            The corner to use.
     */
    public void setCorner(Corner corner) {
        _corner = corner;
        setDefaultLocation();
    }

    /**
     * @return The current <code>Corner</code> which is used.
     */
    public Corner getCorner() {
        return _corner;
    }

    public Dimension getDefaultWindowSize() {
        return _defaultWindowSize;
    }

    public void setDefaultWindowSize(Dimension defaultWindowSize) {
        _defaultWindowSize = defaultWindowSize;
    }

    /**
     * Set the message to show onto the notification window.
     * 
     * @param message
     */
    public void setMessage(String message) {
        _message = message;
        _label.setText(_message);
    }

    /**
     * Get the current message used for the notification window.
     * 
     * @return The message.
     */
    public String getMessage() {
        return _message;
    }

    /**
     * Set the speed to use.
     * 
     * @param speed
     */
    public void setSpeed(int speed) {
        _speed = speed;
    }

    /**
     * @return the current speed used.
     */
    public int getSpeed() {
        return _speed;
    }

    /**
     * Set the icon to use.
     * 
     * @param icon
     *            The icon to use.
     */
    public void setIcon(Icon icon) {
        this.setIcon(icon, SwingConstants.LEADING);
    }

    /**
     * Set the icon to use
     * 
     * @param icon
     *            The icon to use.
     * @param horizontalAlignement
     *            The type of horizontal alignement.
     */
    public void setIcon(Icon icon, int horizontalAlignement) {
        _icon = icon;
        _label.setIcon(_icon);
        _label.setHorizontalAlignment(horizontalAlignement);
    }

    /**
     * 
     * @return The current icon which is used.
     */
    public Icon getIcon() {
        return _icon;
    }

    /**
     * Set the <code>JLabel</code> to use for the notification window.
     * 
     * @param label
     *            The <code>JLabel</code> to use.
     */
    public void setJLabel(JLabel label) {
        _label = label;
    }

    /**
     * @return The current <code>JLabel</code> used for this notification
     *         window.
     */
    public JLabel getJLabel() {
        return _label;
    }

    /**
     * Whether or not to slide the notification window while hiding it.<br>
     * Default value is <code>true</code>.
     * 
     * @param slide
     */
    public void setSlide(boolean slide) {
        _slide = slide;
    }

    public boolean isSlide() {
        return _slide;
    }

    // =======================================================================

    /*
     * For testing purpose only ... This method main() is also for
     * understandings purpose.
     */
    public static void main(String[] args) {

        NotificationWindow w1 = new NotificationWindow(Corner.TOP_LEFT);
        NotificationWindow w2 = new NotificationWindow(Corner.BOTTOM_LEFT);
        NotificationWindow w3 = new NotificationWindow(
                "A simple notification, but my text will be centered !");
        NotificationWindow w4 = new NotificationWindow(
                "Cool, i have my icon !!!");

        w1.setBackground(new Color(0f, 0f, 0f, 0.35f)); // use transparency ...
        w1.setMessage("I use transparency !");
        w1.showNotification(5); // show it for only 5 seconds & hide it
                                // automatically.

        w2.setMessage("I will be sticky until you enter the mouse ...");
        w2.setSlide(false); // Do not slide, hide automatically/quickly
        w2.showNotification(-1);

        w3.setCorner(Corner.TOP_RIGHT);
        JLabel lab = w3.getJLabel();
        lab.setHorizontalAlignment(JLabel.CENTER);
        lab.setText("My text is CENTERED now !");
        w3.setJLabel(lab);
        w3.setSlide(false);
        w3.setBackground(Color.ORANGE);
        w3.showNotification(8);

        Icon icon = new ImageIcon("src/main/resources/icons/mosque_48x48.png");
        w4.setIcon(icon);
        w4.showNotification(-1);
    }

    // =======================================================================

}
