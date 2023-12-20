package com.gzs.tasker.trayicon;

import javafx.application.Platform;

import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TaskerTrayIcon {
    private static final Logger LOGGER = Logger.getLogger(TaskerTrayIcon.class.getName());

    private TrayIcon trayIcon;

    public void initTrayIcon(Runnable openAction, Runnable closeAction) {
        try {
            java.awt.Toolkit.getDefaultToolkit();

            if (!SystemTray.isSupported()) {
                LOGGER.warning("No system tray support!");
                return;
            }

            SystemTray tray = SystemTray.getSystemTray();
            Image icon = MultiResolutionIcon.createMultiResolutionIcon();
            if (icon == null) {
                LOGGER.severe("Could not load icons for tray!");
                return;
            }
            trayIcon = new TrayIcon(icon);

            // double-click
            trayIcon.addActionListener(event -> Platform.runLater(openAction));
            final PopupMenu popup = getPopupMenu(openAction, closeAction, tray);
            trayIcon.setPopupMenu(popup);

            tray.add(trayIcon);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE,"", ex);
        }
    }

    private PopupMenu getPopupMenu(Runnable openAction, Runnable closeAction, SystemTray tray) {
        MenuItem openItem = new MenuItem("Open");
        openItem.addActionListener(event -> Platform.runLater(openAction));

        MenuItem exitItem = new MenuItem("Exit");
        exitItem.addActionListener(event -> {
            closeAction.run();
            Platform.exit();
            tray.remove(trayIcon);
        });

        final PopupMenu popup = new PopupMenu();
        popup.add(openItem);
        popup.addSeparator();
        popup.add(exitItem);
        return popup;
    }

}
