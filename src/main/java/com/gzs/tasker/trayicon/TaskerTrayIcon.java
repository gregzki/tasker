package com.gzs.tasker.trayicon;

import javafx.application.Platform;

import java.awt.*;

import static java.lang.System.Logger.Level.ERROR;
import static java.lang.System.Logger.Level.WARNING;

public class TaskerTrayIcon {

    private static final System.Logger LOGGER = System.getLogger("TaskerTrayIcon");

    private TrayIcon trayIcon;

    public void initTrayIcon(Runnable openAction, Runnable closeAction) {
        try {
            java.awt.Toolkit.getDefaultToolkit();

            if (!SystemTray.isSupported()) {
                LOGGER.log(WARNING, "No system tray support!");
                return;
            }

            SystemTray tray = SystemTray.getSystemTray();
            Image icon = MultiResolutionIcon.createMultiResolutionIcon();
            if (icon == null) {
                LOGGER.log(ERROR, "Could not load icons for tray!");
                return;
            }
            trayIcon = new TrayIcon(icon);

            // double-click
            trayIcon.addActionListener(event -> Platform.runLater(openAction));
            final PopupMenu popup = getPopupMenu(openAction, closeAction, tray);
            trayIcon.setPopupMenu(popup);

            tray.add(trayIcon);
        } catch (Exception e) {
            LOGGER.log(ERROR, e);
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
