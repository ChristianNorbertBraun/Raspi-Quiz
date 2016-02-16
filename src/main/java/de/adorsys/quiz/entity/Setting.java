package de.adorsys.quiz.entity;

public class Setting {

	private int points;
	private int lvl_1;
	private int lvl_2;
	private int lvl_3;

	public Setting() {
	}

	public Setting(int points, int lvl_1, int lvl_2, int lvl_3) {
		this.points = points;
		this.lvl_1 = lvl_1;
		this.lvl_2 = lvl_2;
		this.lvl_3 = lvl_3;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getLvl_1() {
		return lvl_1;
	}

	public void setLvl_1(int lvl_1) {
		this.lvl_1 = lvl_1;
	}

	public int getLvl_2() {
		return lvl_2;
	}

	public void setLvl_2(int lvl_2) {
		this.lvl_2 = lvl_2;
	}

	public int getLvl_3() {
		return lvl_3;
	}

	public void setLvl_3(int lvl_3) {
		this.lvl_3 = lvl_3;
	}
}
