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

package net.waqtsalat.utils;

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
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.datatype.Artwork;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;
import org.jaudiotagger.tag.id3.ID3v1Tag;
import org.jaudiotagger.tag.id3.ID3v24Tag;

/**
 * Got inspiration from this <a
 * href="http://www.jthink.net/jaudiotagger/examples_id3.jsp">link</a>.
 * 
 * @author Papa Issa DIAKHATE (<a href="mailto:paissad@gmail.com">paissad</a>)
 */
public class Mp3TagsReader implements TagsReader {

    private MP3File          _mp3file;
    private AudioHeader      _audioHeader;
    private MP3AudioHeader   _mp3header;
    private Tag              _tag;
    private ID3v1Tag         _v1Tag;
    private AbstractID3v2Tag _v2Tag;
    private ID3v24Tag        _v24tag;

    // ======================================================================

    public Mp3TagsReader(File inputFile) throws CannotReadException,
            IOException, TagException, ReadOnlyFileException,
            InvalidAudioFrameException {
        AudioTagsReader.suppressJaudioTaggerLogs();
        _mp3file = (MP3File) AudioFileIO.read(inputFile);
        initTagAndHeader();
    }

    // ======================================================================

    @Override
    public void initTagAndHeader() {
        //@formatter:off
        _audioHeader = _mp3file.getAudioHeader();
        _mp3header   = _mp3file.getMP3AudioHeader();
        _tag         = _mp3file.getTag();
        _v1Tag       = _mp3file.getID3v1Tag();
        _v2Tag       = _mp3file.getID3v2Tag();
        _v24tag      = _mp3file.getID3v2TagAsv24();
        //@formatter:on
    }

    // ======================================================================

    @Override
    public Tag getTag() {
        return _tag;
    }

    public ID3v1Tag getTagV1() {
        return _v1Tag;
    }

    public AbstractID3v2Tag getTagV2() {
        return _v2Tag;
    }

    public ID3v24Tag getTagV24() {
        return _v24tag;
    }

    @Override
    public AudioHeader getAudioHeader() {
        return _audioHeader;
    }

    public MP3AudioHeader getMp3Header() {
        return _mp3header;
    }

    @Override
    public String getTag_Artist() {
        return _tag.getFirst(FieldKey.ARTIST);
    }

    @Override
    public String getTag_Album() {
        return _tag.getFirst(FieldKey.ALBUM);
    }

    @Override
    public String getTag_Title() {
        if (_v1Tag.getFirstTitle() != null) {
            return _v1Tag.getFirstTitle();
        } else {
            return _tag.getFirst(FieldKey.TITLE);
        }
    }

    @Override
    public String getTag_Comment() {
        if (_v1Tag.getFirstComment() != null) {
            return _v1Tag.getFirstComment();
        } else {
            return _tag.getFirst(FieldKey.COMMENT);
        }
    }

    @Override
    public String getTag_Year() {
        if (_v1Tag.getFirstYear() != null) {
            return _v1Tag.getFirstYear();
        } else {
            return _tag.getFirst(FieldKey.YEAR);
        }
    }

    @Override
    public String getTag_Track() {
        if (_v1Tag.getFirstTrack() != null) {
            return _v1Tag.getFirstTrack();
        } else {
            return _tag.getFirst(FieldKey.TRACK);
        }
    }

    @Override
    public Artwork getTag_Artwork() {
        if (_v1Tag.getFirstArtwork() != null) {
            return _v1Tag.getFirstArtwork();
        } else {
            return _tag.getFirstArtwork();
        }

    }

    public String getTag_Genre() {
        if (_v1Tag.getFirstGenre() != null) {
            return _v1Tag.getFirstGenre();
        } else {
            return _tag.getFirst(FieldKey.GENRE);
        }

    }

    // ======================================================================

}
