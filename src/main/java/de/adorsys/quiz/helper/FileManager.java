package de.adorsys.quiz.helper;

import com.google.gson.Gson;
import de.adorsys.quiz.entity.Riddle;
import de.adorsys.quiz.entity.Setting;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FileManager {

	private static FileManager fileManager;
	private Gson gson;

	private FileManager() {
		gson = new Gson();
	}

	public static FileManager getInstance() {
		if (fileManager == null)
			fileManager = new FileManager();
		return fileManager;
	}

	public Setting readSetting() {
		ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
		Future<String> future = singleThreadExecutor.submit(new ReadCallable("settings.json"));
		try {
			return gson.fromJson(future.get(), Setting.class);
		} catch (InterruptedException | ExecutionException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public Riddle readRiddle(int id, int lvl) {
		ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
		Future<String> future = singleThreadExecutor.submit(new ReadCallable(getPathToRiddle(id, lvl)));
		try {
			return gson.fromJson(future.get(), Riddle.class);
		} catch (InterruptedException | ExecutionException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public boolean writeToFile(Setting setting) {
		ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
		Future<Boolean> future = singleThreadExecutor.submit(new WriteCallable(setting));
		try {
			return future.get();
		} catch (InterruptedException | ExecutionException ex) {
			ex.printStackTrace();
			return false;
		}

	}

	private String getPathToRiddle(int id, int lvl) {
		return String.format("lvl_%d/%d.json", lvl, id);
	}

	private class WriteCallable implements Callable<Boolean> {

		private Setting setting;

		public WriteCallable(Setting setting) {
			this.setting = setting;
		}

		@Override
		public Boolean call() throws Exception {
			try {
				ClassLoader classLoader = getClass().getClassLoader();
				FileWriter fileWriter = new FileWriter(classLoader.getResource("settings.json").getFile(), false);
				BufferedWriter writer = new BufferedWriter(fileWriter);
				try {

					writer.write(gson.toJson(setting));

				} catch (Exception ex) {
					return false;
				} finally {
					writer.flush();
					return true;
				}
			} catch (IOException ex) {
				return false;
			}
		}
	}

	private class ReadCallable implements Callable<String> {
		private String fileName;

		public ReadCallable(String fileName) {
			this.fileName = fileName;
		}

		@Override
		public String call() {
			try {
				ClassLoader classLoader = getClass().getClassLoader();
				FileReader fileReader = new FileReader(classLoader.getResource(fileName).getFile());
				BufferedReader reader = new BufferedReader(fileReader);

				String temp;
				StringBuilder text = new StringBuilder();
				while ((temp = reader.readLine()) != null) {
					text.append(temp);
				}
				return text.toString();
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
				return "";
			}
		}
	}
}
