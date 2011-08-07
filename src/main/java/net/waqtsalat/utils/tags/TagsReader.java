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

package net.waqtsalat.utils.tags;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.LogManager;

import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.datatype.Artwork;

/**
 * This class contains convenient methods that allow to retrieve some
 * tags/informations from an audio file.
 * 
 * @author Papa Issa DIAKHATE (paissad)
 */
public abstract class TagsReader {

    static {
        suppressJaudioTaggerLogs();
    }

    /**
     * The tag object.
     */
    protected Tag tag;

    /**
     * This method must initialize the {@link Tag} to use and the
     * {@link AudioHeader}. It is intended to be used internally in the class
     * which extends/implements <tt>TagsReader</tt>.
     */
    protected abstract void initTagAndHeader();

    public Tag getTag() {
        return this.tag;
    }

    public abstract AudioHeader getAudioHeader();

    public abstract String getArtist();

    public abstract String getAlbum();

    public abstract String getTitle();

    public abstract String getComment();

    public abstract String getYear();

    public abstract String getTrack();

    public abstract Artwork getArtwork();

    /**
     * Allows to suppress the log messages coming from the jaudiotagger library.
     */
    private static void suppressJaudioTaggerLogs() {
        try {
            LogManager.getLogManager().readConfiguration(
                    new ByteArrayInputStream(("org.jaudiotagger.level = OFF").getBytes()));
        } catch (SecurityException se) {
            System.err.println("Failed to suppress the java.util.logger config : " + se);
        } catch (IOException ioe) {
            System.err.println("Failed to suppress the java.util.logger config : " + ioe);
        }
    }
}
