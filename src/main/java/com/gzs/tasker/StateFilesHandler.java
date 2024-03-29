package com.gzs.tasker;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.gzs.tasker.state.State;

import java.io.*;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StateFilesHandler {
    private static final Logger LOGGER = Logger.getLogger(StateFilesHandler.class.getName());

    private String filePath;

    public static final int AUTO_SAVE_TIME = 5000;
    private final Timer stateSaver = new Timer();

    public StateFilesHandler() {
        readConfigProperties();
    }

    private void readConfigProperties() {
        try (InputStream input = TaskerApplication.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                LOGGER.severe("Unable to read config.properties");
                System.exit(991);
            }
            Properties config = new Properties();
            config.load(input);

            filePath = (String) config.getOrDefault("tasks.file.path",
                    System.getProperty("user.dir") + "/tasks.json");
            LOGGER.fine(filePath);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "", ex);
            System.exit(999);
        }
    }

    public State loadSavedState() {
        State state = null;
        try {
            File file = new File(filePath);
            if (file.createNewFile()) {
                LOGGER.log(Level.INFO, "New file created in :{0}", filePath);
            }
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new FileReader(file));
            state = gson.fromJson(reader, State.class);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "", ex);
        }
        if (state != null) {
            return state;
        }
        return new State();
    }

    void saveState(State state) {
        Gson gson = new Gson();
        try {
            File file = new File(filePath);
            if (file.createNewFile()) {
                LOGGER.log(Level.INFO, "New file created in :{0}", filePath);
            }
            FileWriter writer = new FileWriter(file);
            state.cleanUp();
            gson.toJson(state, writer);
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "", ex);
        }
    }

    public void finishAutoSave(State state) {
        stateSaver.cancel();
        saveState(state);
    }

    public void startAutoSave(State state) {
        stateSaver.schedule(new SaveStateTask(state), 0, AUTO_SAVE_TIME);
    }


    class SaveStateTask extends TimerTask {
        private final State stateToSave;

        public SaveStateTask(State state) {
            this.stateToSave = state;
        }

        public void run() {
            saveState(stateToSave);
        }
    }

}
