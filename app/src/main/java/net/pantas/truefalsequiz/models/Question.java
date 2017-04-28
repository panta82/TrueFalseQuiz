package net.pantas.truefalsequiz.models;

public class Question {
	public String text;
	public boolean correctAnswer;

	public static Question fromRaw(String rawQuestion) {
		Question q = new Question();
		String[] parts = rawQuestion.split("\\|");
		q.text = parts[0];
		q.correctAnswer = Boolean.parseBoolean(parts[1]);
		return q;
	}
}
