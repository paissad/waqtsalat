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

package net.waqtsalat.utils;

import static net.waqtsalat.WaqtSalat.logger;
import static net.waqtsalat.utils.GeoipUtils.GEOIP_WORLDCITIES_FULL_PATH;
import net.waqtsalat.utils.DBDatas;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;

/**
 * This class contains utilities to create a database from a worldcitiespop.txt
 * file.
 * 
 * @author Papa Issa DIAKHATE (<a href="mailto:paissad@gmail.com">paissad</a>)
 */
public class WorldCitiesDB {

	private DBDatas _dbData = new DBDatas();
	private final String _csvFileName = GEOIP_WORLDCITIES_FULL_PATH;
	private final String _csvCharset = "ISO-8859-15";
	private Connection _conn = null;

	// list of Statements, PreparedStatements
	private ArrayList<Statement> _allStatements = new ArrayList<Statement>();

	private final StringBuffer dropTableString =  new StringBuffer()
	.append("DROP TABLE IF EXISTS ")
	.append(_dbData.get_tableName())
	.append(" CASCADE CONSTRAINTS;"); 

	private final StringBuffer createTableString = new StringBuffer()
	.append("CREATE TABLE IF NOT EXISTS ")
	.append(_dbData.get_tableName()).append(" (")
	.append("country_code VARCHAR(2)   NOT NULL, ")
	.append("city         VARCHAR(40)  NOT NULL, ")
	.append("region       VARCHAR (3)  NOT NULL, ")
	.append("latitude     FLOAT        NOT NULL, ")
	.append("longitude    FLOAT        NOT NULL, ")
	.append("UNIQUE (country_code, city, latitude, longitude) ")
	.append(" ) AS SELECT COUNTRY, ACCENTCITY, REGION, LATITUDE, LONGITUDE ")
	.append("FROM CSVREAD ('")
	.append(_csvFileName).append("', null, '").append(_csvCharset)
	.append("') WHERE POPULATION IS NOT NULL;");

	private final StringBuffer alterTableString = new StringBuffer()
	.append("ALTER TABLE ").append(_dbData.get_tableName())
	.append(" ADD country_name VARCHAR(40) BEFORE city;");

	private final StringBuffer updateCountryCode = new StringBuffer()
	.append("UPDATE ").append(_dbData.get_tableName())
	.append(" SET country_code = UPPER(country_code);");

	private final StringBuffer updateCountryName = new StringBuffer()
	.append("UPDATE ").append(_dbData.get_tableName())
	.append(" SET country_name = ? WHERE country_code LIKE ?;");

	// =======================================================================

	public WorldCitiesDB() {
	}

	// =======================================================================

	/**
	 * Creates the database.
	 * 
	 * @throws IOException
	 * @throws SQLException
	 */
	public void create() throws IOException, SQLException {

		long startTime = System.currentTimeMillis();

		try {
			connectToDB();
			_conn.setAutoCommit(false);

			Statement stmt = _conn.createStatement();
			_allStatements.add(stmt);

			stmt.execute(dropTableString.toString());
			stmt.execute(createTableString.toString());
			logger.info("The table '{}' created successfully.",
					_dbData.get_tableName());

			stmt.execute(alterTableString.toString());
			logger.info("The table '{}' altered successfully.",
					_dbData.get_tableName());

			stmt.execute(updateCountryCode.toString());
			logger.info("Finished putting country codes in uppercase.");

			// Update the country names entries.
			updateTableCountryName(Locale.US);

			_conn.commit();

		} catch (SQLException sqle) {
			logger.error("An error occured ...");
			logger.error("Rolling back ...");
			_conn.rollback();
			// printSQLException(sqle); // TODO
			throw new SQLException(sqle);
		} finally { // release all open resources to avoid unnecessary memory
			// usage.
			while (!_allStatements.isEmpty()) {
				Statement st = (Statement) _allStatements.remove(0);
				if (st != null) {
					st.close();
					st = null;
				}
			} // Finished freeing the memory used by the statements ...

			if (_conn != null) {
				_conn.setAutoCommit(true);
				_conn.close();
				_conn = null;
			} // Finished freeing the memory used by the connection ...
		}
		long endTime = System.currentTimeMillis();
		logger.debug("Database '{}' created in {} seconds.",
				_dbData.get_dbName(), (int) (endTime - startTime) / 1000);
	}

	// =======================================================================

	public static void printSQLException(SQLException e) {
		String format = "%-12s : %s\n";
		while (e != null) {
			String errMsg = "------------- SQLException ----------------\n";
			errMsg += String.format(format, "SQL State", e.getSQLState());
			errMsg += String.format(format, "Error Code", e.getErrorCode());
			errMsg += String.format(format, "Cause", e.getCause());
			errMsg += String.format(format, "Message", e.getMessage());
			System.err.print(errMsg);
			// for stack traces, refer to derby.log or uncomment this:
			// e.printStackTrace(System.err);
			System.err
			.println("--------------------------------------------\n");
			e = e.getNextException();
		}
	}

	// =======================================================================

	/**
	 * Update the country names entries of the database using the default
	 * {@link Locale}.
	 * 
	 * @throws SQLException
	 */
	public void updateTableCountryName() throws SQLException {
		updateTableCountryName(Locale.getDefault());
	}

	// =======================================================================

	/**
	 * Update the country names entries of the database using the specified
	 * {@link Locale}.
	 * 
	 * @param aLocale The <code>Locale</code> to use.
	 * @throws SQLException
	 * 
	 */
	public void updateTableCountryName(Locale aLocale) throws SQLException {
		connectToDB();
		PreparedStatement pstmt = _conn.prepareStatement(updateCountryName.toString());
		_allStatements.add(pstmt);
		HashMap<String, String> countries = Utils.getCountriesCodeAndName(aLocale);
		Set<String> country_codes_SET = countries.keySet();
		Iterator<String> country_codes_IT = country_codes_SET.iterator();
		while (country_codes_IT.hasNext()) {
			String cc = country_codes_IT.next(); // cc like Country Code.
			pstmt.setString(1, countries.get(cc));
			pstmt.setString(2, cc);
			pstmt.addBatch();
		}
		pstmt.executeBatch();
		logger.info("Finished updating the country names into table '{}' using Locale '{}'.",
				_dbData.get_tableName(), aLocale.getDisplayName());

		if(pstmt != null) {
			pstmt.close(); pstmt = null;
		}
	}

	// =======================================================================

	/**
	 * @param country
	 * @param city
	 * @return An array of float containing the coordinates.<br>
	 *         The 1st element of the array is the latitude, the 2nd element is
	 *         the longitude.
	 * @throws SQLException
	 * 
	 */
	public float[] getCoordinates(String country, String city) throws SQLException {
		connectToDB();
		String sql = "SELECT latitude, longitude FROM " + _dbData.get_tableName()
		+ " WHERE country_name LIKE '" + country + "' AND city LIKE '" +  city +  "';";
		Statement stmt = _conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		if (rs.next()) {  // We only select the 1st row !
			float latitude  = rs.getFloat(1);
			float longitude = rs.getFloat(2);
			float[] coordinates = { latitude, longitude };
			if (rs != null) {
				rs.close(); rs = null;
			}
			return  coordinates;
		} else {
			return null;
		}
	}

	// =======================================================================

	private void connectToDB() throws SQLException {
		if (_conn == null) {
			_dbData.loadDriver();
			Properties connectionProps = new Properties();
			connectionProps.put("user", "");
			connectionProps.put("password", "");
			String connectionURL = _dbData.get_ConnectionURL();
			_conn = DriverManager.getConnection(connectionURL, connectionProps);
			logger.info("Connected to {} database.", _dbData.get_dbName());
		} else {
			logger.info("It seems like we are already connected to {} database !",
					_dbData.get_dbName());
		}
	}

	// =======================================================================
}
