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

package net.waqtsalat.gui;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import net.waqtsalat.I18N;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTabbedPane;

/**
 * 
 * @author Papa Issa DIAKHATE (<a href="mailto:paissad@gmail.com">paissad</a>)
 */
public class About extends JDialog {

    private static final long serialVersionUID = 1L;
    private JButton           closeButton;
    private JLabel            jlab;

    // =======================================================================

    public About() {
        getContentPane().setLayout(new GridBagLayout());
        setAlwaysOnTop(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(GuiConstants.ABOUT_WINDOW_PREFERED_WIDTH, GuiConstants.ABOUT_WINDOW_PREFERED_HEIGHT));
        setMinimumSize(getPreferredSize());
        setLocationRelativeTo(getParent());

        init();
    }

    // =======================================================================

    private void init() {
        jlab = new JLabel();

        jlab.setText(I18N.getString("About"));
        // jlab.setToolTipText("My tooltip");
        jlab.setFocusable(true);
        jlab.setEnabled(true);
        GridBagConstraints gbc_jlab = new GridBagConstraints();
        gbc_jlab.insets = new Insets(0, 0, 5, 0);
        gbc_jlab.gridx = 0;
        gbc_jlab.gridy = 0;
        getContentPane().add(jlab, gbc_jlab);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
        gbc_tabbedPane.fill = GridBagConstraints.BOTH;
        gbc_tabbedPane.gridx = 0;
        gbc_tabbedPane.gridy = 1;
        getContentPane().add(tabbedPane, gbc_tabbedPane);

        closeButton = new JButton();
        tabbedPane.addTab("New tab", null, closeButton, null);

        closeButton.setBorderPainted(true);
        closeButton.setText(I18N.getString("Close"));
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionPerformedCloseButton(e);
            }
        });

    }

    // =======================================================================

    private void actionPerformedCloseButton(ActionEvent e) {
        // this.setVisible(false);
        setEnabled(false);
    }
    // =======================================================================

}
