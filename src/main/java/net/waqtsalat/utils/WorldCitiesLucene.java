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

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

/**
 * This class contains convenience utilities to create a Lucene index for
 * searches ... <a
 * href="http://www.lucenetutorial.com/lucene-in-5-minutes.html">
 * http://www.lucenetutorial.com/lucene-in-5-minutes.html</a>
 * 
 * @author Papa Issa DIAKHATE (<a href="mailto:paissad@gmail.com">paissad</a>)
 */
public class WorldCitiesLucene {

	private static DBDatas _dbData           = new DBDatas();
	private static StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_30);
	private static String fieldName_location   = "location";
	private static Directory _indexDir;

	// ======================================================================

	/**
	 * Creates the Lucene index from the database.<br>
	 * 
	 * @throws SQLException
	 * @throws IOException
	 * 
	 */
	public static void create() throws SQLException, IOException {
		long startTime = System.currentTimeMillis();
		_dbData.loadDriver();
		Connection conn           = DriverManager.getConnection(_dbData.get_ConnectionURL());
		connectToIndex();
		IndexWriter writer        = new IndexWriter(_indexDir, analyzer, true, IndexWriter.MaxFieldLength.UNLIMITED);
		logger.info("Indexing to directory '{}'.", _indexDir.toString());
		addDocs(writer, conn);
		writer.optimize();
		writer.close();
		if (conn != null) {
			conn.close(); conn = null;
		}
		closeIndex();
		long endTime = System.currentTimeMillis();
		int duration = (int) ((endTime - startTime) / 1000);
		logger.debug("Lucene index created in {} seconds.", duration);
	}
	// ======================================================================

	/**
	 * @param queryStr 
	 *            The <code>String</code>(location) to search into the index/database.
	 * @return A {@link ArrayList} of arrays of String containing the results.
	 * @throws IOException 
	 * @throws ParseException 
	 * 
	 */
	public static ArrayList<String[]> search(String queryStr) throws IOException, ParseException {
		ArrayList<String[]> locations = new ArrayList<String[]>(); 
		connectToIndex();
		int hitsPerPage = 300;
		Query q = new QueryParser(Version.LUCENE_30, fieldName_location, analyzer).parse(queryStr);
		IndexSearcher searcher = new IndexSearcher(IndexReader.open(_indexDir));
		TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
		searcher.search(q, collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;
		logger.debug("Found {} hits.", hits.length); // TODO ...
		for (int i=0; i<hits.length; i++) {
			int docID = hits[i].doc;
			Document doc = searcher.doc(docID);
			locations.add(doc.getValues(fieldName_location));
		}
		return locations;
	}

	// ======================================================================

	public static void connectToIndex() throws IOException {
		if (_indexDir == null) {
			_indexDir = new SimpleFSDirectory(new File(
					new File(GEOIP_WORLDCITIES_FULL_PATH).getParent() + File.separator + "index"));
		} else {
			logger.debug("Seems like to be already connected to Lucene index ...");
		}
	}

	// ======================================================================

	/**
	 * Closes the index if opened.
	 * 
	 * @throws IOException
	 */
	public static void closeIndex() throws IOException {
		if (_indexDir != null) {
			_indexDir.close(); _indexDir = null;
		}
	}

	// ======================================================================

	/**
	 * Adds documents from the database to the index.
	 * 
	 * @param writer 
	 * @param conn 
	 * @throws SQLException 
	 * @throws IOException 
	 * 
	 */
	private static void addDocs(IndexWriter writer, Connection conn) throws SQLException, IOException {
		String sql = "SELECT COUNTRY_NAME, CITY, REGION, LATITUDE, LONGITUDE FROM " + _dbData.get_tableName() + ";";
		Statement stmt = conn.createStatement();
		ResultSet rs   = stmt.executeQuery(sql);
		while (rs.next()) {
			Document doc = new Document();
			doc.add(new Field(
					fieldName_location, rs.getString("COUNTRY_NAME"), Field.Store.YES, Field.Index.ANALYZED));
			doc.add(new Field(
					fieldName_location, rs.getString("CITY"), Field.Store.YES, Field.Index.ANALYZED));
			doc.add(new Field(
					fieldName_location, rs.getString("REGION"), Field.Store.YES, Field.Index.NO)); // TODO ...
			doc.add(new Field(
					fieldName_location, rs.getString("LATITUDE"), Field.Store.YES, Field.Index.NO));
			doc.add(new Field(
					fieldName_location, rs.getString("LONGITUDE"), Field.Store.YES, Field.Index.NO));
			writer.addDocument(doc);
		}
		if (stmt != null) {
			stmt.close(); stmt = null;
		}
		if (rs != null) {
			rs.close(); rs = null;
		}
	}

	// ======================================================================

}
