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

package net.paissad.waqtsalat.gui;

import static net.paissad.waqtsalat.pray.PrayConstants.PRAY_NAMES;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.paissad.waqtsalat.WSConstants;
import net.paissad.waqtsalat.pray.PrayTime.PrayName;

/**
 * 
 * @author Papa Issa DIAKHATE (paissad)
 */
public class SysTray implements ActionListener {

    private static Logger     logger            = LoggerFactory.getLogger(SysTray.class);

    private String            action_quit       = "action_QUIT";
    private String            action_gui        = "action_GUI";
    private ArrayList<String> action_prays_list = new ArrayList<String>();

    public SysTray() throws AWTException {
        // Checks if the system tray is supported
        if (!SystemTray.isSupported()) {
            logger.warn("System tray is not supported by this system !!!");
        } else {
            String tooltip = WSConstants.WS_NAME;
            PopupMenu popup = new PopupMenu("tray pop-up");

            MenuItem showInterfaceItem = new MenuItem("Show GUI");
            showInterfaceItem.setActionCommand(action_gui);
            popup.add(showInterfaceItem);
            popup.addSeparator();

            Iterator<Entry<PrayName, String>> iter = PRAY_NAMES.entrySet().iterator();
            while (iter.hasNext()) {
                Entry<PrayName, String> entry = iter.next();
                // JRadioButtonMenuItem prayItem = new
                // JRadioButtonMenuItem(pn.getName(), false);
                MenuItem prayItem = new MenuItem(entry.getValue());
                String actionPray = entry.getKey().toString();
                prayItem.setActionCommand(actionPray);
                action_prays_list.add(actionPray);
                popup.add(prayItem);
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
