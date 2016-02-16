package de.adorsys.quiz.entity;

import com.owlike.genson.annotation.JsonIgnore;

import java.io.Serializable;

public class Riddle implements Serializable {

	private int id;
	private String title;
	private String question;
	@JsonIgnore
	private String answer;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

}
