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

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.UIManager;

/**
 * Got it from this <a href=
 * "http://cyberpython.wordpress.com/2010/03/30/java-a-custom-jtextfield-for-searching/"
 * >link</a>.
 */
public class JSearchTextField extends JIconTextField implements FocusListener {

    private static final long serialVersionUID = 1L;

    private String            _textWhenNotFocused;

    // ======================================================================
    // Constructors ...

    public JSearchTextField() {
        super();
        this.setTextWhenNotFocused("Search ...");
        this.addFocusListener(this);
    }

    // ======================================================================

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!this.hasFocus() && this.getText().equals("")) {
            int height = this.getHeight();
            Font prev = g.getFont();
            Font italic = prev.deriveFont(Font.ITALIC);
            Color prevColor = g.getColor();
            g.setFont(italic);
            g.setColor(UIManager.getColor("textInactiveText"));
            int h = g.getFontMetrics().getHeight();
            int textBottom = (height - h) / 2 + h - 4;
            int x = this.getInsets().left;
            Graphics2D g2d = (Graphics2D) g;
            RenderingHints hints = g2d.getRenderingHints();
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2d.drawString(getTextWhenNotFocused(), x, textBottom);
            g2d.setRenderingHints(hints);
            // this.setText(getTextWhenNotFocused());
            g.setFont(prev);
            g.setColor(prevColor);
        }
    }

    // ======================================================================

    /**
     * Clear the content of the text into the <code>JSearchTextField</code> and
     * put the default text when not focused.
     * 
     * @see #getTextWhenNotFocused()
     */
    public void resetContent() {
        this.setText("");
        this.repaint();
    }

    // ======================================================================

    @Override
    public void focusGained(FocusEvent e) {
        this.repaint();
    }

    // ======================================================================

    @Override
    public void focusLost(FocusEvent e) {
        this.repaint();
    }

    // ======================================================================
    // Getters / Setters ...

    /**
     * Set the text to use when the <code>Component</code> does not have the
     * focus.
     * 
     * @param textWhenNotFocused
     *            The text to use.
     */
    public void setTextWhenNotFocused(String textWhenNotFocused) {
        this._textWhenNotFocused = textWhenNotFocused;
        this.repaint();
    }

    /**
     * Get the text that is used when the <code>Component</code> does not have
     * the focus.
     * 
     * @return The text used.
     */
    public String getTextWhenNotFocused() {
        return _textWhenNotFocused;
    }

    // ======================================================================

}
