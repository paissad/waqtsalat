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

package net.paissad.waqtsalat.utils.geoip;

import static net.paissad.waqtsalat.WSConstants.LUCENE_INDEX_PATH;
import static net.paissad.waqtsalat.WSConstants.WORLDCITIES_TABLE_NAME;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jna.Platform;

import net.paissad.waqtsalat.factory.DBConnection;
import net.paissad.waqtsalat.utils.jdbc.JdbcUtils;

/**
 * This class contains convenient methods to create a Lucene search index from
 * GeoIP World-Cities database.
 * <p>
 * <b>Tutorial</b>:<br>
 * <a href="http://www.lucenetutorial.com/lucene-in-5-minutes.html">
 * http://www.lucenetutorial.com/lucene-in-5-minutes.html</a>
 * </p>
 * 
 * @author Papa Issa DIAKHATE (paissad)
 */
public class WorldCitiesLucene {

    private static final String     FIELD_LOCATION    = "location";
    private static final Version    LUCENE_VERSION    = Version.LUCENE_30;

    private static Logger           logger            = LoggerFactory.getLogger(WorldCitiesLucene.class);
    private static Directory        indexDir;
    private static StandardAnalyzer analyzer          = new StandardAnalyzer(LUCENE_VERSION);

    // =========================================================================

    /**
     * Creates the Lucene index from the database.
     * 
     * @throws SQLException
     * @throws IOException
     * 
     */
    public static void createIndex() throws SQLException, IOException {

        long startTime = System.currentTimeMillis();
        connectToIndex();

        Connection conn = null;
        try {
            conn = DBConnection.getInstance();
            IndexWriter writer = new IndexWriter(getIndexDir(), analyzer, true, IndexWriter.MaxFieldLength.UNLIMITED);
            logger.info("Indexing to directory '{}'.", getIndexDir().toString());
            addDocs(writer, conn);
            writer.optimize();
            writer.close();

        } finally {
            JdbcUtils.closeQuietly(conn);
            closeIndex();
        }

        long endTime = System.currentTimeMillis();
        int duration = (int) ((endTime - startTime) / 1000);
        logger.debug("Lucene index created in {} seconds.", duration);
    }

    // ======================================================================

    /**
     * @param queryStr
     *            The <tt>String</tt>(location) to search into the
     *            index/database.
     * @return A {@link List} of arrays of String containing the results.
     * 
     * @throws IOException
     * @throws ParseException
     * 
     */
    public static List<String[]> search(String queryStr) throws IOException, ParseException {

        try {
            List<String[]> locations = new ArrayList<String[]>();
            connectToIndex();
            int hitsPerPage = 500;

            // TODO: when the String entry is escaped, the search is less
            // productive ...
            // will try to figure it out in the future
            // queryStr = QueryParser.escape(queryStr);

            Query q = new QueryParser(LUCENE_VERSION, FIELD_LOCATION, analyzer).parse(queryStr);
            IndexSearcher searcher = new IndexSearcher(IndexReader.open(getIndexDir()));
            TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
            searcher.search(q, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;
            logger.debug("Found {} hits.", hits.length);
            for (int i = 0; i < hits.length; i++) {
                int docID = hits[i].doc;
                Document doc = searcher.doc(docID);
                locations.add(doc.getValues(FIELD_LOCATION));
            }
            return locations;

        } finally {
            closeIndex();
        }
    }

    // =========================================================================

    /**
     * Connects to the Lucene index.
     * 
     * @throws IOException
     */
    private static void connectToIndex() throws IOException {
        if (getIndexDir() == null) {
            File destDir = new File(LUCENE_INDEX_PATH);
            if (Platform.isWindows()) {
                setIndexDir(new SimpleFSDirectory(destDir, null));
            } else {
                setIndexDir(new NIOFSDirectory(destDir, null));
            }
        } else {
            logger.trace("Already connected to the lucene index directory ({})", indexDir.toString());
        }
    }

    // =========================================================================

    /**
     * Closes the index if opened.
     * 
     * @throws IOException
     */
    private static void closeIndex() throws IOException {
        if (indexDir != null) {
            indexDir.close();
            indexDir = null;
        }
    }

    // =========================================================================

    /**
     * Adds documents from the database to the Lucene index.
     * <p>
     * <b>Note</b>: The connection is not closed into this method.
     * </p>
     * 
     * @param writer
     * @param conn
     * @throws SQLException
     * @throws IOException
     * 
     */
    private static void addDocs(final IndexWriter writer, final Connection conn) throws
            SQLException, IOException {

        String sql = "SELECT COUNTRY_NAME, CITY, REGION, LATITUDE, LONGITUDE FROM " + WORLDCITIES_TABLE_NAME + ";";
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Document doc = new Document();
                doc.add(new Field(
                        FIELD_LOCATION, rs.getString("COUNTRY_NAME"), Field.Store.YES, Field.Index.ANALYZED));
                doc.add(new Field(
                        FIELD_LOCATION, rs.getString("CITY"), Field.Store.YES, Field.Index.ANALYZED));
                doc.add(new Field(
                        FIELD_LOCATION, rs.getString("REGION"), Field.Store.YES, Field.Index.NO));
                doc.add(new Field(
                        FIELD_LOCATION, rs.getString("LATITUDE"), Field.Store.YES, Field.Index.NO));
                doc.add(new Field(
                        FIELD_LOCATION, rs.getString("LONGITUDE"), Field.Store.YES, Field.Index.NO));
                writer.addDocument(doc);
            }

        } finally {
            JdbcUtils.closeQuietly(stmt);
            JdbcUtils.closeQuietly(rs);
        }
    }

    // =========================================================================
    // Getters / Setters ...

    private static Directory getIndexDir() {
        return indexDir;
    }

    private static void setIndexDir(Directory indexDir) {
        WorldCitiesLucene.indexDir = indexDir;
    }

    // =========================================================================

}
