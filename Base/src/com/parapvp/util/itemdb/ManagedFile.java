/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.plugin.java.JavaPlugin
 */
package com.parapvp.util.itemdb;

import com.parapvp.base.BasePlugin;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;

public class ManagedFile {
    private static final int BUFFER_SIZE = 8192;
    private final transient File file;

    public ManagedFile(String filename, JavaPlugin plugin) {
        this.file = new File(plugin.getDataFolder(), filename);
        if (!this.file.exists()) {
            try {
                ManagedFile.copyResourceAscii("" + '/' + filename, this.file);
            }
            catch (IOException ex) {
                plugin.getLogger().log(Level.SEVERE, "items.csv has not been loaded", ex);
            }
        }
    }

    public static void copyResourceAscii(String resourceName, File file) throws IOException {
        InputStreamReader reader = new InputStreamReader(ManagedFile.class.getResourceAsStream(resourceName), StandardCharsets.UTF_8);
        Throwable throwable = null;
        try {
            MessageDigest digest = ManagedFile.getDigest();
            DigestOutputStream digestStream = new DigestOutputStream(new FileOutputStream(file), digest);
            Throwable throwable2 = null;
            try {
                OutputStreamWriter writer = new OutputStreamWriter((OutputStream)digestStream, StandardCharsets.UTF_8);
                Throwable throwable3 = null;
                try {
                    int length;
                    char[] buffer = new char[8192];
                    while ((length = reader.read(buffer)) >= 0) {
                        writer.write(buffer, 0, length);
                    }
                    writer.write("\n");
                    writer.flush();
                    digestStream.on(false);
                    digestStream.write(35);
                    digestStream.write(new BigInteger(1, digest.digest()).toString(16).getBytes(StandardCharsets.UTF_8));
                }
                catch (Throwable buffer) {
                    throwable3 = buffer;
                    throw buffer;
                }
                finally {
                    if (writer != null) {
                        if (throwable3 != null) {
                            try {
                                writer.close();
                            }
                            catch (Throwable buffer) {
                                throwable3.addSuppressed(buffer);
                            }
                        } else {
                            writer.close();
                        }
                    }
                }
            }
            catch (Throwable writer) {
                throwable2 = writer;
                throw writer;
            }
            finally {
                if (digestStream != null) {
                    if (throwable2 != null) {
                        try {
                            digestStream.close();
                        }
                        catch (Throwable writer) {
                            throwable2.addSuppressed(writer);
                        }
                    } else {
                        digestStream.close();
                    }
                }
            }
        }
        catch (Throwable digest) {
            throwable = digest;
            throw digest;
        }
        finally {
            if (reader != null) {
                if (throwable != null) {
                    try {
                        reader.close();
                    }
                    catch (Throwable digest) {
                        throwable.addSuppressed(digest);
                    }
                } else {
                    reader.close();
                }
            }
        }
    }

    public static MessageDigest getDigest() throws IOException {
        try {
            return MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException ex) {
            throw new IOException(ex);
        }
    }

    public File getFile() {
        return this.file;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public List<String> getLines() {
        try {
            BufferedReader reader = Files.newBufferedReader(Paths.get(this.file.getPath(), new String[0]), StandardCharsets.UTF_8);
            Throwable throwable = null;
            try {
                String line;
                ArrayList<String> lines = new ArrayList<String>();
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
                ArrayList<String> arrayList = lines;
                return arrayList;
            }
            catch (Throwable lines) {
                throwable = lines;
                throw lines;
            }
            finally {
                if (reader != null) {
                    if (throwable != null) {
                        try {
                            reader.close();
                        }
                        catch (Throwable var6_8) {
                            throwable.addSuppressed(var6_8);
                        }
                    } else {
                        reader.close();
                    }
                }
            }
        }
        catch (IOException ex) {
            BasePlugin.getPlugin().getLogger().log(Level.SEVERE, ex.getMessage(), ex);
            return Collections.emptyList();
        }
    }
}

