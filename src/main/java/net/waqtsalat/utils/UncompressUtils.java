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

import static net.waqtsalat.WaqtSalat.logger;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.tools.bzip2.CBZip2InputStream;
import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;

/**
 * Contains some utilities to gunzip, bunzip, untar files and such uncompressing
 * stuffs.
 * 
 * @author Papa Issa DIAKHATE (<a href="mailto:paissad@gmail.com">paissad</a>)
 */
public class UncompressUtils {

    private static final String  REGEX_DEFAULT_EXTENSION_GZIP = "\\.gz$|\\.gzip$|\\.tgz$";
    private static final String  REGEX_DEFAULT_EXTENSION_BZIP = "\\.bz2$|\\.bzip2$";
    private static final String  REGEX_DEFAULT_EXTENSION_TAR  = "\\.tar$";
    private static final String  GUNZIP_METHOD                = "gunzip";
    private static final String  BUNZIP_METHOD                = "bunzip2";
    private static final String  TAR_METHOD                   = "untar";
    private static final boolean CLOSE_STREAM                 = true;

    /**
     * Compressed file (typically .gz, tar.gz, .bz2, .tar.bz2, .tar, ...)
     */
    private File                 source;
    /**
     * Destination of the uncompressed file.
     */
    private File                 dest;

    /**
     * Default constructor, source & destination files are initialised to null.
     */
    public UncompressUtils() {
        this.source = null;
        this.dest = null;
    }

    // =======================================================================

    /**
     * With this constructor: the destination file will have as the same name as
     * the source file,<br />
     * but without the linked extension of the uncompressing method used
     * (gunzip, bunzip2, untar ...)
     * 
     * @param source
     *            - File to uncompress.
     */
    public UncompressUtils(File source) {
        this.source = source;
        this.dest = null;
    }

    // =======================================================================

    /**
     * 'source' is the file to uncompress and 'dest' the new created file.
     * 
     * @param source
     *            - File to uncompress.
     * @param dest
     *            - Destination file.
     * @throws IOException
     *             If 'source' and 'dest' file have the same absolute paths.
     */
    public UncompressUtils(File source, File dest) throws IOException {
        if (source.getAbsolutePath().equals(dest.getAbsolutePath()))
            throw new IOException(
                    "Source file and destination file cannot be the same!");
        this.source = source;
        this.dest = dest;
    }

    // =======================================================================

    /**
     * Tries to uncompress a file automatically by guessing the method to use
     * from the file extension.
     * 
     * @return Returns the new file/directory created after the decompression.
     * @throws IOException
     * @see #gunzip()
     * @see #bunzip2()
     * @see #untar
     */
    public File uncompressSmart() throws IOException {

        try {

            String fileName = source.toString().toLowerCase();
            if (fileName.endsWith(".tar.gz") || fileName.endsWith(".tgz")) {

                File tarFile = null;
                try {
                    logger.info("TAR-GZIP file format: {}", source.toString());
                    tarFile = new UncompressUtils(source).gunzip();
                    return new UncompressUtils(tarFile).untar();
                } finally {
                    if (tarFile.exists())
                        tarFile.delete();
                }
            }

            else if (fileName.endsWith("tar.bz2")
                    || fileName.endsWith("tar.bzip2")) {

                File tarFile = null;
                try {
                    logger.info("TAR-BZIP2 file format: {}", source.toString());
                    tarFile = new UncompressUtils(source).bunzip2();
                    return new UncompressUtils(tarFile).untar();
                } finally {
                    if (tarFile.exists())
                        tarFile.delete();
                }
            }

            else if (fileName.endsWith(".gzip") || fileName.endsWith(".gz")) {
                logger.info("GZIP file format: {}", source.toString());
                return gunzip();
            }

            else if (fileName.endsWith(".bzip2") || fileName.endsWith(".bz2")) {
                logger.info("BZIP file format: {}", source.toString());
                return bunzip2();
            }

            else if (fileName.endsWith(".tar")) {
                logger.info("TAR file format: {}", source.toString());
                return untar();
            }

            else {
                logger.error("Unkwown extension ! ... Try to use another method instead of this one.");
                throw new RuntimeException(
                        "Known extensions: .gz, .gzip, .bz2, .bzip2, .tar, .tar.gz, .tgz, .tar.bz2, tar.bzip2");
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw new IOException();
        }
    }

    // =======================================================================

    /**
     * Gunzip the source file.
     * 
     * @return File that has been uncompressed.
     * @throws IOException
     * @see #bunzip2
     * @see #untar
     */
    public File gunzip() throws IOException {
        try {
            return uncompress(GUNZIP_METHOD);
        } catch (IOException ioe) {
            throw new IOException("Gunzip failed.");
        }
    }

    // =======================================================================

    /**
     * Bunzip the source file.
     * 
     * @return File that has been uncompressed.
     * @throws IOException
     * @see #gunzip
     * @see #untar
     */
    public File bunzip2() throws IOException {
        try {
            return uncompress(BUNZIP_METHOD);
        } catch (IOException ioe) {
            throw new IOException("Bunzip2 failed.");
        }
    }

    // =======================================================================

    /**
     * Untar the source file.
     * 
     * @return File that has been uncompressed.
     * @throws IOException
     * @see #gunzip()
     * @see #bunzip2()
     */
    public File untar() throws IOException {
        return uncompress(TAR_METHOD);
    }

    // =======================================================================

    /**
     * Implements different methods used to uncompress a file.
     * 
     * @param method
     *            Method to use for uncompressing the file. Available methods
     *            are GUNZIP_METHOD, BUNZIP_METHOD.
     * @return File that has been created after the decompression process.
     * @throws IOException
     * @see #gunzip
     * @see #bunzip2
     */
    private File uncompress(final String method) throws IOException {
        try {
            logger.info("Uncompressing file '{}' ...", source.getAbsolutePath());
            // We must set the destination filename, if not set before !
            if (this.dest == null) {
                logger.trace("'dest' file is null ...");
                Pattern pattern;
                String outputFileName;

                if (method.equals(BUNZIP_METHOD)) {
                    pattern = Pattern.compile(REGEX_DEFAULT_EXTENSION_BZIP,
                            Pattern.CASE_INSENSITIVE);
                }

                else if (method.equals(GUNZIP_METHOD)) {
                    pattern = Pattern.compile(REGEX_DEFAULT_EXTENSION_GZIP,
                            Pattern.CASE_INSENSITIVE);
                }

                else if (method.equals(TAR_METHOD)) {
                    pattern = Pattern.compile(REGEX_DEFAULT_EXTENSION_TAR);
                }

                else {
                    throw new RuntimeException("Unsupported method '" + method
                            + "'.");
                }

                // Set output filename.
                Matcher matcher = pattern.matcher(source.getAbsolutePath());
                if (source.toString().toLowerCase().endsWith(".tgz")) {
                    outputFileName = matcher.replaceFirst(".tar");
                } else {
                    outputFileName = matcher.replaceFirst("");
                }

                dest = new File(outputFileName);
            }

            // GZIP and BZIP2 (decompression)
            if (method.equals(BUNZIP_METHOD) || method.equals(GUNZIP_METHOD)) {
                dest = inputstreamToFile(uncompressStream(method), dest,
                        CLOSE_STREAM);
            }

            // TAR (decompression)
            else if (method.equals(TAR_METHOD)) {
                TarInputStream tis = (TarInputStream) uncompressStream(TAR_METHOD);
                try {

                    TarEntry tarEntry = tis.getNextEntry();
                    while (tarEntry != null) {
                        File destPath = new File(dest.getParent()
                                + File.separatorChar + tarEntry.getName());

                        if (tarEntry.isDirectory()) {
                            destPath.mkdir();
                        } else {
                            if (!destPath.getParentFile().exists()) {
                                destPath.getParentFile().mkdirs();
                            }
                            logger.trace("untar: '{}'", destPath);
                            FileOutputStream fos = new FileOutputStream(
                                    destPath);
                            try {
                                tis.copyEntryContents(fos);
                            } finally {
                                fos.flush();
                                fos.close();
                            }
                        }
                        tarEntry = tis.getNextEntry();
                    }
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                    throw new IOException("Untar failed.");
                } finally {
                    if (tis != null)
                        tis.close();
                }

            } // End of tar method.
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw new IOException("Uncompressing file '"
                    + source.getAbsolutePath() + "' failed.");
        }
        logger.info("Uncompressing file '{}' finished successfully.",
                source.getAbsolutePath());
        return dest;
    }

    // =======================================================================

    /**
     * Implements different methods for creating InputStream from the source
     * file.
     * 
     * @param method
     *            Method to use to uncompress the file to an InputStream.
     * @return InputStream of the 'source' file using the specified method.
     * @throws IOException
     */
    private InputStream uncompressStream(final String method)
            throws IOException {
        try {
            logger.trace("Converting file '{}' to inputstream.",
                    source.getAbsolutePath());
            BufferedInputStream bis = new BufferedInputStream(
                    new FileInputStream(source));

            if (method.equals(GUNZIP_METHOD)) {
                return new GZIPInputStream(bis); // gzip
            }

            else if (method.equals(BUNZIP_METHOD)) {
                // Let's test the validity of the .bzip2 file.
                final char[] magic = new char[] { 'B', 'Z' };
                for (int i = 0; i < magic.length; i++) {
                    if (bis.read() != magic[i]) {
                        bis.close();
                        throw new RuntimeException("Invalid bz2 file: "
                                + source.getAbsolutePath());
                    }
                } // End of validity check.
                  // CBZip2InputStream documentation says that the 2 first
                  // bytes 'B' and 'Z' must be read !!!
                return new CBZip2InputStream(bis); // bz2
            }

            else if (method.equals(TAR_METHOD)) {
                return new TarInputStream(bis); // tar
            }

            else {
                if (bis != null)
                    bis.close();
                throw new RuntimeException("Unknown method '" + method + "'.");
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw new IOException("Creating inputstream failed.");
        }
    }

    // =======================================================================

    /**
     * Creates a file from an InputStream.
     * 
     * @param is
     *            InputStream to read from.
     * @param outputFile
     *            File to be created from the InputStream.
     * @param closeStream
     *            Whether or not to close the InputStream after the operation.
     *            If set to true, the InputStream will be closed.
     * @return File that has been created from the InputStream.
     * @throws IOException
     */
    private File inputstreamToFile(InputStream is, File outputFile,
            boolean closeStream) throws IOException {

        if (source.getAbsolutePath().equals(outputFile.getAbsolutePath()))
            throw new IOException(outputFile.getAbsolutePath()
                    + " must not have as the same absolute path as the source.");

        try {
            logger.trace("Saving inputstream to file '{}'.",
                    outputFile.getAbsolutePath());
            File tempFile = File.createTempFile(outputFile.getName(), null,
                    outputFile.getParentFile());

            try {
                if (outputFile.exists()) {
                    tempFile.delete();
                    FileUtils.moveFile(outputFile, tempFile);
                }
                FileUtils.copyInputStreamToFile(is, outputFile);
                return outputFile;
            } catch (IOException ioe) {
                if (tempFile.exists()) {
                    if (outputFile.exists())
                        outputFile.delete();
                    FileUtils.moveFile(tempFile, outputFile);
                }
                ioe.printStackTrace();
                throw new IOException();
            } finally {
                if (tempFile.exists())
                    tempFile.delete();
                if (closeStream)
                    if (is != null)
                        is.close();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw new IOException("Saving inputstream failed to file '"
                    + outputFile + "'");
        }
    }
    // =======================================================================
}
