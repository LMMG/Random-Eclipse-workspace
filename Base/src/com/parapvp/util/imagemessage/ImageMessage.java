/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.craftbukkit.libs.joptsimple.internal.Strings
 *  org.bukkit.entity.Player
 */
package com.parapvp.util.imagemessage;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import javax.imageio.ImageIO;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.libs.joptsimple.internal.Strings;
import org.bukkit.entity.Player;

public class ImageMessage {
    private static final char TRANSPARENT_CHAR = ' ';
    private final String[] lines;
    private final Color[] colors = new Color[]{new Color(0, 0, 0), new Color(0, 0, 170), new Color(0, 170, 0), new Color(0, 170, 170), new Color(170, 0, 0), new Color(170, 0, 170), new Color(255, 170, 0), new Color(170, 170, 170), new Color(85, 85, 85), new Color(85, 85, 255), new Color(85, 255, 85), new Color(85, 255, 255), new Color(255, 85, 85), new Color(255, 85, 255), new Color(255, 255, 85), new Color(255, 255, 255)};

    public /* varargs */ ImageMessage(String ... imgLines) {
        this.lines = imgLines;
    }

    public ImageMessage(ChatColor[][] chatColors, char imgChar) {
        this.lines = this.toImgMessage(chatColors, imgChar);
    }

    public ImageMessage(BufferedImage image, int height, char imgChar) {
        this.lines = this.toImgMessage(this.toColourArray(image, height), imgChar);
    }

    public ImageMessage(String url, int height, char imgChar) {
        String[] result;
        try {
            BufferedImage image = ImageIO.read(new URL(url));
            ChatColor[][] colours = this.toColourArray(image, height);
            result = this.toImgMessage(colours, imgChar);
        }
        catch (IOException ex) {
            throw new IllegalArgumentException(ex);
        }
        this.lines = result;
    }

    public ImageMessage(String fileName, File folder, int height, char imgChar) {
        String[] result;
        try {
            BufferedImage bufferedImage = ImageIO.read(new File(folder, fileName));
            ChatColor[][] colours = this.toColourArray(bufferedImage, height);
            result = this.toImgMessage(colours, imgChar);
        }
        catch (IOException ex) {
            throw new IllegalArgumentException(ex);
        }
        this.lines = result;
    }

    public /* varargs */ ImageMessage appendText(String ... text) {
        for (int y = 0; y < this.lines.length; ++y) {
            if (text.length <= y) continue;
            String[] lines = this.lines;
            lines[y] = lines[y] + ' ' + text[y];
        }
        return this;
    }

    public /* varargs */ ImageMessage appendCenteredText(String ... text) {
        for (int y = 0; y < this.lines.length; ++y) {
            if (text.length <= y) {
                return this;
            }
            int len = 65 - this.lines[y].length();
            String[] arrstring = this.lines;
            int n = y;
            arrstring[n] = arrstring[n] + this.center(text[y], len);
        }
        return this;
    }

    private ChatColor[][] toColourArray(BufferedImage image, int height) {
        double ratio = image.getHeight() / image.getWidth();
        BufferedImage reSized = this.resizeImage(image, (int)((double)height / ratio), height);
        ChatColor[][] chatImg = new ChatColor[reSized.getWidth()][reSized.getHeight()];
        for (int x = 0; x < reSized.getWidth(); ++x) {
            for (int y = 0; y < reSized.getHeight(); ++y) {
                ChatColor closest;
                int rgb = reSized.getRGB(x, y);
                chatImg[x][y] = closest = this.getClosestChatColor(new Color(rgb, true));
            }
        }
        return chatImg;
    }

    private String[] toImgMessage(ChatColor[][] colors, char imgChar) {
        String[] lines = new String[colors[0].length];
        for (int y = 0; y < colors[0].length; ++y) {
            StringBuilder line = new StringBuilder();
            for (ChatColor[] color1 : colors) {
                ChatColor color2 = color1[y];
                line.append((Object)(color2 != null ? color1[y].toString() + imgChar : Character.valueOf(' ')));
            }
            lines[y] = line.toString() + (Object)ChatColor.RESET;
        }
        return lines;
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
        AffineTransform af = new AffineTransform();
        af.scale(width / originalImage.getWidth(), height / originalImage.getHeight());
        AffineTransformOp operation = new AffineTransformOp(af, 1);
        return operation.filter(originalImage, null);
    }

    private double getDistance(Color c1, Color c2) {
        double redMean = (double)(c1.getRed() + c2.getRed()) / 2.0;
        double r = c1.getRed() - c2.getRed();
        double g = c1.getGreen() - c2.getGreen();
        int b = c1.getBlue() - c2.getBlue();
        double weightR = 2.0 + redMean / 256.0;
        double weightG = 4.0;
        double weightB = 2.0 + (255.0 - redMean) / 256.0;
        return weightR * r * r + 4.0 * g * g + weightB * (double)b * (double)b;
    }

    private boolean areIdentical(Color c1, Color c2) {
        return Math.abs(c1.getRed() - c2.getRed()) <= 5 && Math.abs(c1.getGreen() - c2.getGreen()) <= 5 && Math.abs(c1.getBlue() - c2.getBlue()) <= 5;
    }

    private ChatColor getClosestChatColor(Color color) {
        int i;
        if (color.getAlpha() < 128) {
            return null;
        }
        int index = 0;
        double best = -1.0;
        for (i = 0; i < this.colors.length; ++i) {
            if (!this.areIdentical(this.colors[i], color)) continue;
            return ChatColor.values()[i];
        }
        for (i = 0; i < this.colors.length; ++i) {
            double distance = this.getDistance(color, this.colors[i]);
            if (distance >= best && best != -1.0) continue;
            best = distance;
            index = i;
        }
        return ChatColor.values()[index];
    }

    private String center(String string, int length) {
        if (string.length() > length) {
            return string.substring(0, length);
        }
        if (string.length() == length) {
            return string;
        }
        return Strings.repeat((char)' ', (int)((length - string.length()) / 2)) + string;
    }

    public String[] getLines() {
        return Arrays.copyOf(this.lines, this.lines.length);
    }

    public void sendToPlayer(Player player) {
        for (String line : this.lines) {
            player.sendMessage(line);
        }
    }
}

