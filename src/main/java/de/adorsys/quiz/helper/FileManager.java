package de.adorsys.quiz.helper;

import com.google.gson.Gson;
import de.adorsys.quiz.entity.Riddle;

import java.io.BufferedReader;
import java.io.FileReader;
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


	public Riddle readRiddle(int id) {
		ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
		Future<String> future = singleThreadExecutor.submit(new ReadCallable(id + ".json"));
		try {
			return gson.fromJson(future.get(), Riddle.class);
		} catch (InterruptedException | ExecutionException ex) {
			ex.printStackTrace();
			return null;
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
