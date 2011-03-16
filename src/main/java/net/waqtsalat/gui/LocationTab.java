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

package net.waqtsalat.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.io.IOException;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.TimeZone;

import net.waqtsalat.IpAddress;
import net.waqtsalat.configuration.WsConfiguration;
import net.waqtsalat.gui.addons.JSearchTextField;
import net.waqtsalat.utils.GeoipUtils;
import net.waqtsalat.utils.Utils;
import net.waqtsalat.utils.WorldCitiesDB;
import net.waqtsalat.utils.WorldCitiesLucene;
import static net.waqtsalat.WaqtSalat.logger;

import org.apache.commons.configuration.ConfigurationException;

import org.apache.lucene.queryParser.ParseException;

import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;

/**
 * 
 * @author Papa Issa DIAKHATE (<a href="mailto:paissad@gmail.com">paissad</a>)
 */
public class LocationTab extends JPanel {

	private static float _latitude;
	private static float _longitude;
	private String locationSeparator = ", ";

	private static final long serialVersionUID = 1L;
	private JPanel panelLocation_1;
	private JPanel panelLocation_2;
	private JPanel panelTimezone;
	private JLabel lblAutomaticLoc;
	private JCheckBox chckbxAutomaticLocation;
	private JLabel lblCityCountry;
	private JSearchTextField textFieldSearchLocation;
	private JButton btnSetLocation;
	private JLabel lblTimeZone;
	private JCheckBox chckbxUseSystemTime;
	private JComboBox comboBoxTimezones;
	private JCheckBox chckbxDaylightSavings;
	private JSeparator separator;
	private boolean isUseSystemTimezone = true;
	private boolean isAutomaticLocation = true;
	private JScrollPane scrollPane;
	private JList listLocation;
	private DefaultListModel listModellocation;

	public LocationTab() {

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.rowWeights = new double[]{1.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);

		panelLocation_1 = new JPanel();
		GridBagConstraints gbc_panelLocation_1 = new GridBagConstraints();
		gbc_panelLocation_1.gridwidth = 2;
		gbc_panelLocation_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_panelLocation_1.anchor = GridBagConstraints.BASELINE;
		gbc_panelLocation_1.insets = new Insets(5, 5, 5, 5);
		gbc_panelLocation_1.gridx = 0;
		gbc_panelLocation_1.gridy = 0;
		add(panelLocation_1, gbc_panelLocation_1);
		GridBagLayout gbl_panelLocation_1 = new GridBagLayout();
		gbl_panelLocation_1.columnWidths = new int[]{0, 0, 0};
		gbl_panelLocation_1.rowHeights = new int[]{0};
		gbl_panelLocation_1.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_panelLocation_1.rowWeights = new double[]{0.0};
		panelLocation_1.setLayout(gbl_panelLocation_1);

		chckbxAutomaticLocation = new JCheckBox("Get the location automatically");
		chckbxAutomaticLocation.setSelected(isAutomaticLocation);
		chckbxAutomaticLocation.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					chckbxAutomaticLocation_ActionPerformed(e);
				} catch (ConfigurationException ce) {
					ce.printStackTrace();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		});
		GridBagConstraints gbc_chckbxAutomaticLocation = new GridBagConstraints();
		gbc_chckbxAutomaticLocation.anchor = GridBagConstraints.WEST;
		gbc_chckbxAutomaticLocation.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxAutomaticLocation.gridx = 0;
		gbc_chckbxAutomaticLocation.gridy = 0;
		panelLocation_1.add(chckbxAutomaticLocation, gbc_chckbxAutomaticLocation);

		lblAutomaticLoc = new JLabel("Guess the city and the country ...");
		lblAutomaticLoc.setEnabled(false);
		GridBagConstraints gbc_lblAutomaticLoc = new GridBagConstraints();
		gbc_lblAutomaticLoc.anchor = GridBagConstraints.WEST;
		gbc_lblAutomaticLoc.insets = new Insets(0, 0, 5, 0);
		gbc_lblAutomaticLoc.gridx = 1;
		gbc_lblAutomaticLoc.gridy = 0;
		try {
			getAutomaticLocation();
		} catch (ConfigurationException ce) {
			ce.printStackTrace();
		} catch (IOException ioe) {
			logger.error(ioe.getMessage());
			//ioe.printStackTrace();
		}
		panelLocation_1.add(lblAutomaticLoc, gbc_lblAutomaticLoc);

		panelLocation_2 = new JPanel();
		GridBagConstraints gbc_panelLocation_2 = new GridBagConstraints();
		gbc_panelLocation_2.gridwidth = 2;
		gbc_panelLocation_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_panelLocation_2.anchor = GridBagConstraints.BASELINE;
		gbc_panelLocation_2.insets = new Insets(0, 5, 5, 5);
		gbc_panelLocation_2.gridx = 0;
		gbc_panelLocation_2.gridy = 1;
		add(panelLocation_2, gbc_panelLocation_2);
		GridBagLayout gbl_panelLocation_2 = new GridBagLayout();
		gbl_panelLocation_2.columnWidths = new int[]{0, 0};
		gbl_panelLocation_2.rowHeights = new int[]{0};
		gbl_panelLocation_2.columnWeights = new double[]{1.0, 0.0};
		gbl_panelLocation_2.rowWeights = new double[]{0.0};
		panelLocation_2.setLayout(gbl_panelLocation_2);

		lblCityCountry = new JLabel("City, Country:");
		lblCityCountry.setEnabled(!isAutomaticLocation);
		GridBagConstraints gbc_lblCityCountry = new GridBagConstraints();
		gbc_lblCityCountry.gridwidth = 2;
		gbc_lblCityCountry.anchor = GridBagConstraints.WEST;
		gbc_lblCityCountry.insets = new Insets(0, 0, 5, 5);
		gbc_lblCityCountry.gridx = 0;
		gbc_lblCityCountry.gridy = 0;
		panelLocation_2.add(lblCityCountry, gbc_lblCityCountry);

		scrollPane = new JScrollPane();
		scrollPane.setEnabled(!isAutomaticLocation);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.anchor = GridBagConstraints.WEST;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		panelLocation_2.add(scrollPane, gbc_scrollPane);

		textFieldSearchLocation = new JSearchTextField();
		textFieldSearchLocation.setEnabled(!isAutomaticLocation);
		textFieldSearchLocation.setIcon(WsConstants.SEARCH_LOCATION);
		textFieldSearchLocation.setTextWhenNotFocused("Search ..."); // FIXME: there is a problem with the apple look&feel, the text "search" is under the icon, set icon to null :(
		textFieldSearchLocation.setColumns(10);
		textFieldSearchLocation.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				textFieldLocation_ActionPerformed(e);
			}
		});
		scrollPane.setColumnHeaderView(textFieldSearchLocation);

		listLocation = new JList();
		listLocation.setEnabled(!isAutomaticLocation);
		listModellocation = new DefaultListModel();
		listLocation.setModel(listModellocation);
		listLocation.setVisibleRowCount(5);
		listLocation.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listLocation.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				if (e.getValueIsAdjusting() == false) {
					int index = listLocation.getSelectedIndex();
					if (index == -1) {
						// No selection ...
					} else {
						textFieldSearchLocation.setText(
								(String) listModellocation.elementAt(index));
					}
				}

			}
		});
		scrollPane.setViewportView(listLocation);

		btnSetLocation = new JButton("Set");
		btnSetLocation.setEnabled(!isAutomaticLocation);
		GridBagConstraints gbc_btnSetLocation = new GridBagConstraints();
		gbc_btnSetLocation.insets = new Insets(0, 0, 5, 5);
		gbc_btnSetLocation.anchor = GridBagConstraints.WEST;
		gbc_btnSetLocation.gridx = 1;
		gbc_btnSetLocation.gridy = 1;
		btnSetLocation.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnSetLocation_ActionPerformed();
			}
		});
		panelLocation_2.add(btnSetLocation, gbc_btnSetLocation);

		separator = new JSeparator();
		separator.setForeground(Color.BLACK);
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.weightx = 1.0;
		gbc_separator.gridwidth = 2;
		gbc_separator.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator.insets = new Insets(0, 5, 0, 5);
		gbc_separator.gridx = 0;
		gbc_separator.gridy = 2;
		add(separator, gbc_separator);

		panelTimezone = new JPanel();
		GridBagConstraints gbc_panelTimezone = new GridBagConstraints();
		gbc_panelTimezone.fill = GridBagConstraints.HORIZONTAL;
		gbc_panelTimezone.anchor = GridBagConstraints.BASELINE;
		gbc_panelTimezone.insets = new Insets(0, 5, 5, 5);
		gbc_panelTimezone.gridx = 0;
		gbc_panelTimezone.gridy = 3;
		add(panelTimezone, gbc_panelTimezone);
		GridBagLayout gbl_panelTimezone = new GridBagLayout();
		gbl_panelTimezone.columnWidths = new int[]{0, 0, 0};
		gbl_panelTimezone.rowHeights = new int[]{0, 0, 0, 0};
		gbl_panelTimezone.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_panelTimezone.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		panelTimezone.setLayout(gbl_panelTimezone);

		lblTimeZone = new JLabel("Time Zone:");
		GridBagConstraints gbc_lblTimeZone = new GridBagConstraints();
		gbc_lblTimeZone.anchor = GridBagConstraints.WEST;
		gbc_lblTimeZone.insets = new Insets(0, 0, 5, 5);
		gbc_lblTimeZone.gridx = 0;
		gbc_lblTimeZone.gridy = 0;
		panelTimezone.add(lblTimeZone, gbc_lblTimeZone);

		chckbxUseSystemTime = new JCheckBox("Use system time zone");
		chckbxUseSystemTime.setSelected(isUseSystemTimezone);
		chckbxUseSystemTime.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chckbxUseSystemTime_ActionPerformed(e);
			}
		});
		GridBagConstraints gbc_chckbxUseSystemTime = new GridBagConstraints();
		gbc_chckbxUseSystemTime.anchor = GridBagConstraints.WEST;
		gbc_chckbxUseSystemTime.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxUseSystemTime.gridx = 1;
		gbc_chckbxUseSystemTime.gridy = 0;
		panelTimezone.add(chckbxUseSystemTime, gbc_chckbxUseSystemTime);

		comboBoxTimezones = new JComboBox();
		comboBoxTimezones.setEnabled(!isUseSystemTimezone);
		comboBoxTimezones.setMaximumRowCount(10);
		comboBoxTimezones.setModel(new DefaultComboBoxModel(Utils.getAllTimezonesSorted()));
		comboBoxTimezones.setSelectedIndex(Utils.getAllTimezonesListSorted().indexOf(TimeZone.getDefault().getID()));
		GridBagConstraints gbc_comboBoxTimezones = new GridBagConstraints();
		gbc_comboBoxTimezones.anchor = GridBagConstraints.WEST;
		gbc_comboBoxTimezones.insets = new Insets(0, 0, 5, 0);
		gbc_comboBoxTimezones.gridx = 1;
		gbc_comboBoxTimezones.gridy = 1;
		panelTimezone.add(comboBoxTimezones, gbc_comboBoxTimezones);

		chckbxDaylightSavings = new JCheckBox("Daylight Savings");
		chckbxDaylightSavings.setEnabled(!isUseSystemTimezone);
		GridBagConstraints gbc_chckbxDaylightSavings = new GridBagConstraints();
		gbc_chckbxDaylightSavings.anchor = GridBagConstraints.WEST;
		gbc_chckbxDaylightSavings.gridx = 1;
		gbc_chckbxDaylightSavings.gridy = 2;
		panelTimezone.add(chckbxDaylightSavings, gbc_chckbxDaylightSavings);

	}
	// ======================================================================

	// ---------- ACTIONS ...
	public void chckbxUseSystemTime_ActionPerformed(ActionEvent e) {
		boolean _selected = chckbxUseSystemTime.isSelected();
		comboBoxTimezones.setEnabled(!_selected);
		chckbxDaylightSavings.setEnabled(!_selected);
	}

	// ------

	public void textFieldLocation_ActionPerformed(ActionEvent e) {
		if (textFieldSearchLocation.getText().length() > 2) {
			try {
				searchWorldCitiesLucene();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			} catch (ParseException pe) {
				pe.printStackTrace();
			}
		}
	}

	// ------

	public void chckbxAutomaticLocation_ActionPerformed(ActionEvent e) throws ConfigurationException, IOException {
		boolean _selected = chckbxAutomaticLocation.isSelected();
		lblCityCountry.setEnabled(!_selected);
		textFieldSearchLocation.setEnabled(!_selected);
		listLocation.setEnabled(!_selected);
		btnSetLocation.setEnabled(!_selected);

		if(_selected) {
			try {
				getAutomaticLocation();
			} catch (IOException ioe) {
				logger.error(ioe.getMessage());
				//ioe.printStackTrace();
			}
		} else { // Search into the Lucene index.
			try {
				searchWorldCitiesLucene();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			} catch (ParseException pe) {
				pe.printStackTrace();
			}		
		}
	}

	// ------

	public void btnSetLocation_ActionPerformed() {
		String[] country_city = textFieldSearchLocation.getText().split(locationSeparator);
		float[] coordinates = { -1, -1};   // TODO ...
		// 0 -> country
		// 1 -> city
		// Get the latitude and the longitude.
		WorldCitiesDB db = new WorldCitiesDB();
		try {
			if (country_city.length ==  2) {
				coordinates = db.getCoordinates(country_city[0], country_city[1]);
			} else {
				logger.error("Bad location ...");
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			return;
		}
		set_latitude(coordinates[0]);
		set_longitude(coordinates[1]);
		String locationString = textFieldSearchLocation.getText();
		lblAutomaticLoc.setText(locationString);
		logger.debug("Location  : {}", locationString);
		logger.debug("Latitude  : {}", get_latitude());
		logger.debug("Longitude : {}", get_longitude());
	}

	// ======================================================================

	/**
	 * http://lucene.apache.org/java/2_9_1/queryparsersyntax.html
	 */
	private void searchWorldCitiesLucene() throws SQLException, IOException, ParseException {
		ArrayList<String[]> locations = new ArrayList<String[]>();
		String entry = textFieldSearchLocation.getText();
		entry = (entry.isEmpty()) ? "_____" : entry;
		locations = WorldCitiesLucene.search(entry + "~"); // fuzzy search ...
		WorldCitiesLucene.closeIndex();
		listModellocation.removeAllElements();
		for (int i=0; i<locations.size(); i ++) {
			String country = locations.get(i)[0];
			String city    = locations.get(i)[1];
			listModellocation.addElement(country + locationSeparator + city);
		}
	}

	// ------

	private void getAutomaticLocation()  throws ConfigurationException, IOException {
		String _ip = new WsConfiguration("").getIpAddress();
		_ip = new IpAddress().retreiveIpAddress();
		if (_ip.equals("-1"))
			logger.error("The Ip address cannot be '-1', an error occured.");
		Location location = new LookupService(GeoipUtils.GEOIP_DATABASE_FULL_PATH).getLocation(_ip);
		String city = (location.city != null) ? location.city : "";
		String country = (location.countryName != null) ? location.countryName : "";
		String locationString = country + locationSeparator + city;
		lblAutomaticLoc.setText(locationString);
		if (textFieldSearchLocation != null) {
			textFieldSearchLocation.resetContent();
		}
		set_latitude(location.latitude);
		set_longitude(location.longitude);
		logger.debug("Ip Address: {}", _ip);
		logger.debug("Location  : {}", locationString);
		logger.debug("Latitude  : {}", get_latitude());
		logger.debug("Longitude : {}", get_longitude());		
	}

	// ======================================================================
	// Getters / Setters ...

	public synchronized static float get_latitude() {
		return _latitude;
	};

	private synchronized static void set_latitude(float latitude) {
		_latitude = latitude;
	}

	public synchronized static float get_longitude() {
		return _longitude;
	}

	private synchronized static void set_longitude(float longitude) {
		_longitude = longitude;
	}

}

