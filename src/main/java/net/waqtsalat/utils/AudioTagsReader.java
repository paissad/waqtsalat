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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import java.util.logging.LogManager;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.datatype.Artwork;

/**
 * Provides some utilities in order to retrieve tags/informations from an audio
 * file.
 * 
 * @author Papa Issa DIAKHATE (<a href="mailto:paissad@gmail.com">paissad</a>)
 */
public class AudioTagsReader implements TagsReader {

	private AudioFile _audioFile;

	protected Tag _tag;
	protected AudioHeader _audioHeader;

	// ======================================================================

	/**
	 * Got inspiration from this <a
	 * href="http://www.jthink.net/jaudiotagger/examples_read.jsp">link</a>.
	 * 
	 * @param inputFile The audio file to use.
	 * 
	 * @throws CannotReadException
	 * @throws IOException
	 * @throws TagException
	 * @throws ReadOnlyFileException
	 * @throws InvalidAudioFrameException
	 */
	public AudioTagsReader(File inputFile) throws CannotReadException, IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException {
		suppressJaudioTaggerLogs();
		_audioFile   = AudioFileIO.read(inputFile);
		initTagAndHeader();
	}

	// ======================================================================

	public static void suppressJaudioTaggerLogs() {
		try {
			LogManager.getLogManager().readConfiguration(
					new ByteArrayInputStream(
							("org.jaudiotagger.level = OFF").getBytes()));
		} catch (SecurityException e) {
			System.err.println("Fail to suppress the java.util.logger config." + e);
		} catch (IOException e) {
			System.err.println("Fail to suppress the java.util.logger config."+ e);
		}
	}

	// ======================================================================

	@Override
	public void initTagAndHeader() {
		_tag         = _audioFile.getTag();
		_audioHeader = _audioFile.getAudioHeader();
	}

	// ======================================================================

	/**
	 * @param file
	 *            The file to use.
	 * @return <code>true</code> if the name of the file ends with ".mp3"
	 *         extension.
	 */
	protected boolean isMp3(File file) {
		String extension = Utils.getExtension(file);
		if (extension != null) {
			if (extension.toLowerCase().equals("mp3")) {
				return true;
			}
		}
		return false;
	}

	// ======================================================================

	@Override
	public Tag getTag() {
		return _tag;
	}

	@Override
	public AudioHeader getAudioHeader() {
		return _audioHeader;
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
		return _tag.getFirst(FieldKey.TITLE);
	}

	@Override
	public String getTag_Comment() {
		return _tag.getFirst(FieldKey.COMMENT);
	}

	@Override
	public String getTag_Year() {
		return _tag.getFirst(FieldKey.YEAR);
	}

	@Override
	public String getTag_Track() {
		return _tag.getFirst(FieldKey.TRACK);
	}

	@Override
	public Artwork getTag_Artwork() {
		return _tag.getFirstArtwork();
	}

	// ======================================================================

}
