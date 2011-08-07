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

import java.io.File;
import java.io.IOException;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.datatype.Artwork;

/**
 * @author Papa Issa DIAKHATE (paissad)
 */
public class BasicTagsReader extends TagsReader {

    private AudioFile   audioFile;
    private AudioHeader audioHeader;

    // =========================================================================

    /**
     * Got inspiration from <a
     * href="http://www.jthink.net/jaudiotagger/examples_read.jsp">here</a>.
     * 
     * @param inputFile
     *            - The audio file to use.
     * 
     * @throws CannotReadException
     * @throws IOException
     * @throws TagException
     * @throws ReadOnlyFileException
     * @throws InvalidAudioFrameException
     */
    public BasicTagsReader(final File inputFile) throws
            CannotReadException, IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException {

        this.audioFile = AudioFileIO.read(inputFile);
        this.initTagAndHeader();
    }

    // =========================================================================

    @Override
    protected void initTagAndHeader() {
        tag = audioFile.getTag();
        audioHeader = audioFile.getAudioHeader();
    }

    @Override
    public AudioHeader getAudioHeader() {
        return audioHeader;
    }

    @Override
    public String getArtist() {
        return tag.getFirst(FieldKey.ARTIST);
    }

    @Override
    public String getAlbum() {
        return tag.getFirst(FieldKey.ALBUM);
    }

    @Override
    public String getTitle() {
        return tag.getFirst(FieldKey.TITLE);
    }

    @Override
    public String getComment() {
        return tag.getFirst(FieldKey.COMMENT);
    }

    @Override
    public String getYear() {
        return tag.getFirst(FieldKey.YEAR);
    }

    @Override
    public String getTrack() {
        return tag.getFirst(FieldKey.TRACK);
    }

    @Override
    public Artwork getArtwork() {
        return tag.getFirstArtwork();
    }

}
