package de.adorsys.quiz.helper;

import com.google.gson.Gson;
import de.adorsys.quiz.entity.Setting;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SettingsHelper {

	private Gson gson;

	public SettingsHelper() {
		gson = new Gson();
	}

	public Setting readSetting() {
			try {
				ClassLoader classLoader = getClass().getClassLoader();
				FileReader fileReader = new FileReader(classLoader.getResource("settings.json").getFile());
				BufferedReader reader = new BufferedReader(fileReader);

				String temp;
				StringBuilder text = new StringBuilder();
				while ((temp = reader.readLine()) != null) {
					text.append(temp);
				}
				return gson.fromJson(text.toString(), Setting.class);
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
				return null;
			}
	}

	public boolean writeToFile(Setting setting) {
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			FileWriter fileWriter = new FileWriter(classLoader.getResource("settings.json").getFile(),false);
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
