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

import static net.waqtsalat.WaqtSalat.logger;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

import net.waqtsalat.PrayName;

/**
 * 
 * @author Papa Issa DIAKHATE (<a href="mailto:paissad@gmail.com">paissad</a>)
 */
public class SysTray implements ActionListener {

    private String            action_quit       = "action_QUIT";
    private String            action_gui        = "action_GUI";
    private ArrayList<String> action_prays_list = new ArrayList<String>();

    public SysTray() throws AWTException {
        // Checks if the system tray is supported
        if (!SystemTray.isSupported()) {
            logger.warn("System tray is not supported by this system !!!");
        } else {
            String tooltip = "WaqtSalat";
            PopupMenu popup = new PopupMenu("tray pop-up");

            MenuItem showInterfaceItem = new MenuItem("Show GUI");
            showInterfaceItem.setActionCommand(action_gui);
            popup.add(showInterfaceItem);
            popup.addSeparator();

            PrayName[] prayNames = PrayName.values();
            for (int i = 0; i < prayNames.length; i++) {
                PrayName pn = prayNames[i];
                // JRadioButtonMenuItem prayItem = new
                // JRadioButtonMenuItem(pn.getName(), false);
                MenuItem prayItem = new MenuItem(pn.getName());
                String action_pray = pn.toString();
                prayItem.setActionCommand(action_pray);
                action_prays_list.add(action_pray);

                popup.add(prayItem);
                // popup.add(new Menu(pn.getName()));
            }
            popup.addSeparator();

            MenuItem quitItem = new MenuItem("Quit");
            quitItem.setActionCommand(action_quit);
            popup.add(quitItem);

            popup.addActionListener(this);

            ImageIcon imgIcon = new ImageIcon(ClassLoader.getSystemResource("icons/mosque_16x16.png"));
            Image img = imgIcon.getImage();
            TrayIcon trayIcon = new TrayIcon(img, tooltip, popup);
            trayIcon.setImageAutoSize(true);
            trayIcon.setPopupMenu(popup);
            trayIcon.displayMessage("Message", "Text", TrayIcon.MessageType.INFO);

            SystemTray tray = SystemTray.getSystemTray();
            tray.add(trayIcon);

            // JOptionPane.showMessageDialog(null, "Message", "Title",
            // JOptionPane.NO_OPTION, null);
            // JOptionPane.showOptionDialog(null, "Message", "Title",
            // JOptionPane.NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
            // null, null);
            // JRadioButtonMenuItem
        }
    }

    // =======================================================================

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(action_quit)) {
            MainFrame.exitGUI();
        } else if (e.getActionCommand().equals(action_gui)) {
            // TODO:
            // show the gui ...
            return;
        } else if (action_prays_list.contains(e.getActionCommand())) {
            System.out.println("action of " + e.getActionCommand());
            // TODO: action of prays to implement ...
        }
    }

    // =======================================================================

    /*
     * For testing purpose ...
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new SysTray();
                } catch (AWTException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // =======================================================================

}
