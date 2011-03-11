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
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TimeZone;

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
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.waqtsalat.IpAddress;
import net.waqtsalat.configuration.WsConfiguration;
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
	private JPanel panelLocation;
	private JPanel panelTimezone;
	private JLabel lblAutomaticLoc;
	private JCheckBox chckbxAutomaticLocation;
	private JLabel lblCityCountry;
	private JTextField textFieldLocation;
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
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);

		panelLocation = new JPanel();
		GridBagConstraints gbc_panelLocation = new GridBagConstraints();
		gbc_panelLocation.gridwidth = 2;
		gbc_panelLocation.insets = new Insets(0, 5, 5, 5);
		gbc_panelLocation.fill = GridBagConstraints.HORIZONTAL;
		gbc_panelLocation.gridx = 0;
		gbc_panelLocation.gridy = 0;
		add(panelLocation, gbc_panelLocation);
		GridBagLayout gbl_panelLocation = new GridBagLayout();
		gbl_panelLocation.columnWidths = new int[]{0, 0};
		gbl_panelLocation.rowHeights = new int[]{0, 0, 0, 0};
		gbl_panelLocation.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panelLocation.rowWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		panelLocation.setLayout(gbl_panelLocation);

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
		panelLocation.add(chckbxAutomaticLocation, gbc_chckbxAutomaticLocation);

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
			//ioe.printStackTrace();
			logger.error(ioe.getMessage());
		}
		panelLocation.add(lblAutomaticLoc, gbc_lblAutomaticLoc);

		lblCityCountry = new JLabel("City, Country:");
		lblCityCountry.setEnabled(!isAutomaticLocation);
		GridBagConstraints gbc_lblCityCountry = new GridBagConstraints();
		gbc_lblCityCountry.anchor = GridBagConstraints.WEST;
		gbc_lblCityCountry.insets = new Insets(0, 0, 5, 5);
		gbc_lblCityCountry.gridx = 0;
		gbc_lblCityCountry.gridy = 1;
		panelLocation.add(lblCityCountry, gbc_lblCityCountry);

		scrollPane = new JScrollPane();
		scrollPane.setEnabled(!isAutomaticLocation);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 2;
		panelLocation.add(scrollPane, gbc_scrollPane);

		textFieldLocation = new JTextField();
		textFieldLocation.setEnabled(!isAutomaticLocation);
		textFieldLocation.setColumns(10);
		textFieldLocation.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				textFieldLocation_ActionPerformed(e);
			}
		});
		scrollPane.setColumnHeaderView(textFieldLocation);

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
						textFieldLocation.setText(
								(String) listModellocation.elementAt(index));
					}
				}

			}
		});
		scrollPane.setViewportView(listLocation);

		btnSetLocation = new JButton("Set");
		btnSetLocation.setEnabled(!isAutomaticLocation);
		GridBagConstraints gbc_btnSetLocation = new GridBagConstraints();
		gbc_btnSetLocation.anchor = GridBagConstraints.WEST;
		gbc_btnSetLocation.gridx = 1;
		gbc_btnSetLocation.gridy = 2;
		btnSetLocation.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnSetLocation_ActionPerformed();
			}
		});
		panelLocation.add(btnSetLocation, gbc_btnSetLocation);

		separator = new JSeparator();
		separator.setForeground(Color.BLACK);
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.gridwidth = 2;
		gbc_separator.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator.insets = new Insets(0, 10, 5, 10);
		gbc_separator.gridx = 0;
		gbc_separator.gridy = 1;
		add(separator, gbc_separator);

		panelTimezone = new JPanel();
		GridBagConstraints gbc_panelTimezone = new GridBagConstraints();
		gbc_panelTimezone.insets = new Insets(0, 5, 0, 5);
		gbc_panelTimezone.fill = GridBagConstraints.HORIZONTAL;
		gbc_panelTimezone.gridx = 0;
		gbc_panelTimezone.gridy = 2;
		add(panelTimezone, gbc_panelTimezone);
		GridBagLayout gbl_panelTimezone = new GridBagLayout();
		gbl_panelTimezone.columnWidths = new int[]{0, 0, 0};
		gbl_panelTimezone.rowHeights = new int[]{0, 0, 0, 0};
		gbl_panelTimezone.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_panelTimezone.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		panelTimezone.setLayout(gbl_panelTimezone);

		lblTimeZone = new JLabel("Time Zone:");
		GridBagConstraints gbc_lblTimeZone = new GridBagConstraints();
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
		if (textFieldLocation.getText().length() > 2) {
			try {
				searchWCDatabase();
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
		textFieldLocation.setEnabled(!_selected);
		listLocation.setEnabled(!_selected);
		btnSetLocation.setEnabled(!_selected);

		if(_selected) {
			try {
				getAutomaticLocation();
			} catch (IOException ioe) {
				//ioe.printStackTrace();
				logger.error(ioe.getMessage());
			}
		} else { // Search into the Lucene index.
			try {
				searchWCDatabase();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			} catch (ParseException pe) {
				pe.printStackTrace();
			}		
		}
	}

	// ------

	public void btnSetLocation_ActionPerformed() {
		String[] country_city = textFieldLocation.getText().split(locationSeparator);
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
		String locationString = textFieldLocation.getText();
		lblAutomaticLoc.setText(locationString);
		logger.debug("Location  : {}", locationString);
		logger.debug("Latitude  : {}", get_latitude());
		logger.debug("Longitude : {}", get_longitude());
	}

	// ======================================================================

	private void searchWCDatabase() throws SQLException, IOException, ParseException {
		ArrayList<String[]> locations = new ArrayList<String[]>();
		String entry = textFieldLocation.getText();
		entry = (entry.isEmpty()) ? "_____" : entry;
		locations = WorldCitiesLucene.search(entry);
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

	private static void set_latitude(float latitude) {
		_latitude = latitude;
	}

	public synchronized static float get_longitude() {
		return _longitude;
	}

	private static void set_longitude(float longitude) {
		_longitude = longitude;
	}

}

