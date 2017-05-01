package net.pantas.truefalsequiz;

import android.opengl.Visibility;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import net.pantas.truefalsequiz.models.Question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
	private static final int QUESTIONS = 5;
	private static final String CURRENT_QUESTION_KEY = "CURRENT_QUESTION_KEY";
	private static final String CORRECT_GUESSES_KEY = "CORRECT_GUESSES_KEY";
	private static final String RANDOM_SEED_KEY = "RANDOM_SEED_KEY";

	private ViewGroup mTitleScreen;
	private Button mBtnStart;
	private TextView mScoreText;

	private ViewGroup mQuestionScreen;
	private TextView mQuestionText;

	private ViewGroup mAnswerScreen;
	private TextView mAnswerText;
	private Button mBtnNext;

	private ArrayList<Question> mQuestions;
	private int mCurrentQuestion;
	private int mCorrectGuesses;
	private long mRandomSeed;
	private int mQuestionCount = QUESTIONS;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTitleScreen = (ViewGroup) this.findViewById(R.id.title_screen);
		mBtnStart = (Button) this.findViewById(R.id.btn_start);
		mScoreText = (TextView) this.findViewById(R.id.score_text);

		mQuestionScreen = (ViewGroup) this.findViewById(R.id.question_screen);
		mQuestionText = (TextView) this.findViewById(R.id.question_text);

		mAnswerScreen = (ViewGroup) this.findViewById(R.id.answer_screen);
		mAnswerText = (TextView) this.findViewById(R.id.answer_text);
		mBtnNext = (Button) this.findViewById(R.id.btn_next);

		mQuestions = new ArrayList<>();
		for (String rawQuestion: getResources().getStringArray(R.array.questions)) {
			mQuestions.add(Question.fromRaw(rawQuestion));
		}

		if (null == savedInstanceState) {
			savedInstanceState = new Bundle();
		}
		mCurrentQuestion = savedInstanceState.getInt(CURRENT_QUESTION_KEY, -1);
		mCorrectGuesses = savedInstanceState.getInt(CORRECT_GUESSES_KEY, 0);
		mRandomSeed = savedInstanceState.getLong(RANDOM_SEED_KEY, 0);

		if (mCurrentQuestion >= 0) {
			randomizeQuestions(mRandomSeed);
			showQuestion();
		} else {
			showTitle(true);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(CURRENT_QUESTION_KEY, mCurrentQuestion);
		outState.putInt(CORRECT_GUESSES_KEY, mCorrectGuesses);
		outState.putLong(RANDOM_SEED_KEY, mRandomSeed);

		super.onSaveInstanceState(outState);
	}

	private void randomizeQuestions() {
		randomizeQuestions(0);
	}

	private void randomizeQuestions(long seed) {
		Random random;
		if (seed != 0) {
			random = new Random(seed);
		} else {
			random = new Random();
			mRandomSeed = random.nextLong();
			random = new Random(mRandomSeed);
		}
		Collections.shuffle(mQuestions, random);
	}

	protected void showTitle(boolean firstTime) {
		if (!firstTime) {
			mBtnStart.setText(R.string.try_again);
			mScoreText.setVisibility(View.VISIBLE);
			mScoreText.setText("Your score is " + mCorrectGuesses + " out of " + mQuestionCount);
		}

		mTitleScreen.bringToFront();
	}

	private boolean hasMoreQuestions() {
		return mCurrentQuestion < mQuestionCount - 1;
	}

	protected void showAnswer(boolean correct) {
		if (correct) {
			mAnswerScreen.setBackgroundColor(getResources().getColor(R.color.colorTrue));
			mAnswerText.setText(R.string.answer_true);
			mCorrectGuesses++;
		} else {
			mAnswerScreen.setBackgroundColor(getResources().getColor(R.color.colorFalse));
			mAnswerText.setText(R.string.answer_false);
		}

		if (hasMoreQuestions()) {
			mBtnNext.setText(R.string.btn_next_question);
		} else {
			mBtnNext.setText(R.string.btn_see_your_score);
		}

		mAnswerScreen.bringToFront();
	}

	protected void showQuestion() {
		mQuestionText.setText(mQuestions.get(mCurrentQuestion).text);
		mQuestionScreen.bringToFront();
	}

	public void onClickBtnStart(View view) {
		randomizeQuestions();

		mCurrentQuestion = 0;
		mCorrectGuesses = 0;
		showQuestion();
	}

	public void onClickBtnTrue(View view) {
		//noinspection PointlessBooleanExpression
		showAnswer(mQuestions.get(mCurrentQuestion).correctAnswer == true);
	}

	public void onClickBtnFalse(View view) {
		//noinspection PointlessBooleanExpression
		showAnswer(mQuestions.get(mCurrentQuestion).correctAnswer == false);
	}

	public void onClickBtnNext(View view) {
		if (hasMoreQuestions()) {
			mCurrentQuestion++;
			showQuestion();
		} else {
			showTitle(false);
		}
	}
}
