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

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.datatype.Artwork;
import org.jaudiotagger.tag.id3.ID3v1Tag;
import org.jaudiotagger.tag.id3.ID3v24Tag;

/**
 * Got inspiration from <a
 * href="http://www.jthink.net/jaudiotagger/examples_id3.jsp">here</a>.
 * 
 * @author Papa Issa DIAKHATE (paissad)
 */
public class Mp3TagsReader extends TagsReader {

    private MP3File        mp3file;
    private AudioHeader    audioHeader;
    private MP3AudioHeader mp3header;
    private ID3v1Tag       v1Tag;
    private ID3v24Tag      v24tag;

    // ======================================================================

    public Mp3TagsReader(final File inputFile) throws
            CannotReadException, IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException {

        this.mp3file = (MP3File) AudioFileIO.read(inputFile);
        this.initTagAndHeader();
    }

    // =========================================================================

    @Override
    protected void initTagAndHeader() {
        this.audioHeader = mp3file.getAudioHeader();
        this.mp3header = mp3file.getMP3AudioHeader();
        this.tag = mp3file.getTag();
        this.v1Tag = mp3file.getID3v1Tag();
        this.v24tag = mp3file.getID3v2TagAsv24();
    }

    public ID3v24Tag getTagV24() {
        return v24tag;
    }

    @Override
    public AudioHeader getAudioHeader() {
        return audioHeader;
    }

    public MP3AudioHeader getMp3Header() {
        return mp3header;
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
        return (v1Tag.getFirstTitle() != null)
                ? v1Tag.getFirstTitle() : tag.getFirst(FieldKey.TITLE);
    }

    @Override
    public String getComment() {
        return (v1Tag.getFirstComment() != null)
                ? v1Tag.getFirstComment() : tag.getFirst(FieldKey.COMMENT);
    }

    @Override
    public String getYear() {
        return (v1Tag.getFirstYear() != null)
                ? v1Tag.getFirstYear() : tag.getFirst(FieldKey.YEAR);
    }

    @Override
    public String getTrack() {
        return (v1Tag.getFirstTrack() != null)
                ? v1Tag.getFirstTrack() : tag.getFirst(FieldKey.TRACK);
    }

    @Override
    public Artwork getArtwork() {
        return (v1Tag.getFirstArtwork() != null)
                ? v1Tag.getFirstArtwork() : tag.getFirstArtwork();
    }

    public String getGenre() {
        return (v1Tag.getFirstGenre() != null)
                ? v1Tag.getFirstGenre() : tag.getFirst(FieldKey.GENRE);
    }

    // =========================================================================

}
