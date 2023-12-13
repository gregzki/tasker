package com.gzs.tasker.trayicon;

import javax.swing.*;
import java.awt.*;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.net.URL;

class MultiResolutionIcon extends ImageFilter {
    private final int width;
    private final int height;
    private final Image highResImage;

    public static Image createMultiResolutionIcon() {
        URL icon16URL = MultiResolutionIcon.class.getResource("/icon16.png");
        URL icon32URL = MultiResolutionIcon.class.getResource("/icon32.png");

        if (icon16URL == null || icon32URL == null) {
            return null;
        }

        Image trayIconImage = new ImageIcon(icon16URL).getImage();
        trayIconImage = trayIconImage.getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        Image trayIconImageHighRes = new ImageIcon(icon32URL).getImage();
        trayIconImageHighRes = trayIconImageHighRes.getScaledInstance(32, 32, Image.SCALE_SMOOTH);

        Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(
                trayIconImage.getSource(),
                new MultiResolutionIcon(16, 16, trayIconImageHighRes)
        ));

        return trayIconImage;
    }

    public MultiResolutionIcon(int width, int height, Image highResImage) {
        this.width = width;
        this.height = height;
        this.highResImage = highResImage;
    }

    @Override
    public void setDimensions(int w, int h) {
        if (w <= width && h <= height) {
            consumer.setDimensions(w, h);
        } else {
            consumer.setDimensions(highResImage.getWidth(null), highResImage.getHeight(null));
        }
    }
}