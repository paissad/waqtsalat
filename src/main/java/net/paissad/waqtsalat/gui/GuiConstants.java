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

import java.awt.Color;
import java.awt.Toolkit;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * This interface contains some preferred/fixed values for the GUI.
 * 
 * @author Papa Issa DIAKHATE (paissad)
 */
public interface GuiConstants {

    int       MAINFRAME_MINIMUM_WIDTH      = 500;
    int       MAINFRAME_MININUM_HEIGHT     = 450;
    int       MAINFRAME_PREFERED_WIDTH     = 760;
    int       MAINFRAME_PREFERED_HEIGHT    = 480;
    int       MENUBAR_PREFERED_HEIGHT      = 25;
    int       IMAGE_HEADER_PANEL_HEIGHT    = 50;
    int       SHORTCUT_KEY_MASK            = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

    // Colours ...
    Color     COLOR_BACKGROUND_MAINFRAME   = Color.LIGHT_GRAY;
    Color     COLOR_BACKGROUND_MENUBAR     = Color.LIGHT_GRAY;
    Color     COLOR_BACKGROUND_MENU        = Color.LIGHT_GRAY;

    ImageIcon HEADER_IMAGE_SUNSET          = new ImageIcon(ClassLoader.getSystemResource("images/sunset_red.jpg"));

    // About dialog ...
    int       ABOUT_WINDOW_PREFERED_WIDTH  = 200;
    int       ABOUT_WINDOW_PREFERED_HEIGHT = 200;

    // Icons ...
    Icon      TAB_ICON_GENERAL             = new ImageIcon(
                                                   ClassLoader.getSystemResource("icons/general_preferences.png"));
    Icon      TAB_ICON_LOCATION            = new ImageIcon(ClassLoader.getSystemResource("icons/earth.png"));
    Icon      TAB_ICON_PREFERENCES         = new ImageIcon(ClassLoader.getSystemResource("icons/preferences.png"));
    Icon      TAB_ICON_PRAYTIMES           = new ImageIcon(ClassLoader.getSystemResource("icons/carpet.png"));
    Icon      TAB_ICON_ALERTS              = new ImageIcon(ClassLoader.getSystemResource("icons/alert.png"));
    Icon      TAB_ICON_ADVANCED            = new ImageIcon(ClassLoader.getSystemResource("icons/advanced_config.png"));
    Icon      ICON_SEARCH_LOCATION         = new ImageIcon(ClassLoader.getSystemResource("icons/search-icon-1.png"));
    Icon      SEARCH_LOCATION              = new ImageIcon(ClassLoader.getSystemResource("icons/search-icon-2.png"));
    Icon      ICON_LOCATION_OK             = new ImageIcon(ClassLoader.getSystemResource("icons/ok-accept.png"));
    Icon      ICON_LOCATION_NOT_OK         = new ImageIcon(ClassLoader.getSystemResource("icons/notOk-cancel.png"));
    Icon      ICON_LOCATION_SEARCHING      = new ImageIcon(ClassLoader.getSystemResource("icons/loading-kit.gif"));
    Icon      ICON_LOCATION_TIMEZONE       = new ImageIcon(ClassLoader.getSystemResource("icons/timezone.png"));
    Icon      ICON_UNKNOWN_STATE           = new ImageIcon(ClassLoader.getSystemResource("icons/unknown-state.png"));
    Icon      ICON_RESET_PREFERENCES       = new ImageIcon(ClassLoader.getSystemResource("icons/reset_prefs.png"));
    Icon      ICON_NOTIFICATION_1          = new ImageIcon(ClassLoader.getSystemResource("icons/notification-1.png"));
    Icon      ICON_NOTIFICATION_2          = new ImageIcon(ClassLoader.getSystemResource("icons/notification-2.png"));
    Icon      ICON_AUDIO_VOLUME            = new ImageIcon(ClassLoader.getSystemResource("icons/audio-volume.png"));
    Icon      ICON_SOUND_ON                = new ImageIcon(ClassLoader.getSystemResource("icons/sound-unmute.png"));
    Icon      ICON_SOUND_OFF               = new ImageIcon(ClassLoader.getSystemResource("icons/sound-mute.png"));
    Icon      ICON_SOUND_PLAY              = new ImageIcon(ClassLoader.getSystemResource("icons/sound-play.png"));
    Icon      ICON_SOUND_STOP              = new ImageIcon(ClassLoader.getSystemResource("icons/sound-stop.png"));
    Icon      ICON_GROWL                   = new ImageIcon(ClassLoader.getSystemResource("icons/growl-icon.png"));

    String    ICON_FLAGS_DIR               = "images/flags/";

    // Adhans
    String    DEFAULT_ADHAN_SOUND          = ClassLoader.getSystemResource("sounds/adhan.mp3").getFile();
    String    DEFAULT_ADHAN_DIR            = "extras/sounds/praytimes.org/audio/adhan";
}
