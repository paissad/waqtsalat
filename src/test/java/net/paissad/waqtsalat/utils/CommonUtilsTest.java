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

package net.paissad.waqtsalat.utils;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import net.waqtsalat.utils.CommonUtils;

/**
 * @author Papa Issa DIAKHATE (paissad)
 * 
 */
public class CommonUtilsTest {

    @Test
    public final void testGetFilenameExtension() {
        Assert.assertEquals(".ext", CommonUtils.getFilenameExtension("file.ext"));
        Assert.assertEquals(".ext", CommonUtils.getFilenameExtension("file.ext3.ext2.ext"));
        Assert.assertEquals("", CommonUtils.getFilenameExtension("fileWithoutExtension"));
        Assert.assertNull(CommonUtils.getFilenameExtension(null));
    }

    @Test
    public final void testHumanReadableByteCount() {
        File file = new File("src/test/resources/sounds/alarm.mp3");
        long filesize = file.length();
        System.out.println(filesize);
        Assert.assertEquals("6,0 kB", CommonUtils.humanReadableByteCount(filesize, true));
        Assert.assertEquals("5,9 KB", CommonUtils.humanReadableByteCount(filesize, false));
    }
}
