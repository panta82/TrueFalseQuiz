package net.pantas.truefalsequiz;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

	private final static String EXTRA_CORRECT_ANSWER = "net.pantas.truefalsequiz.EXTRA_CORRECT_ANSWER";
	private final static String EXTRA_ANSWER_SHOWN = "net.pantas.truefalsequiz.EXTRA_ANSWER_SHOWN";

	private final static String CORRECT_ANSWER_KEY = "CORRECT_ANSWER_KEY";
	private final static String REVEALED_KEY = "REVEALED_KEY";

	private TextView mAnswer;
	private Button mRevealButton;

	private boolean mCorrectAnswer;
	private boolean mRevealed = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cheat);

		mAnswer = (TextView) findViewById(R.id.cheat_answer);
		mRevealButton = (Button) findViewById(R.id.cheat_reveal_btn);

		if (savedInstanceState != null) {
			mCorrectAnswer = savedInstanceState.getBoolean(CORRECT_ANSWER_KEY);
			mRevealed = savedInstanceState.getBoolean(REVEALED_KEY, false);
		}

		Intent startingIntent = getIntent();
		if (startingIntent.hasExtra(EXTRA_CORRECT_ANSWER)) {
			mCorrectAnswer = getIntent().getBooleanExtra(EXTRA_CORRECT_ANSWER, false);
		}

		if (mRevealed) {
			setRevealed();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean(CORRECT_ANSWER_KEY, mCorrectAnswer);
		outState.putBoolean(REVEALED_KEY, mRevealed);
		super.onSaveInstanceState(outState);
	}

	private void setRevealed() {
		mRevealed = true;
		mRevealButton.setEnabled(false);
		mAnswer.setText(mCorrectAnswer ? "TRUE" : "FALSE");
		mAnswer.setVisibility(View.VISIBLE);

		Intent returnIntent = new Intent();
		returnIntent.putExtra(EXTRA_ANSWER_SHOWN, true);
		setResult(RESULT_OK, returnIntent);
	}

	public void onClickBtnCheatReveal(View view) {
		setRevealed();
	}

	public static Intent newIntent(Context packageContext, boolean correctAnswer) {
		Intent intent = new Intent(packageContext, CheatActivity.class);
		intent.putExtra(CheatActivity.EXTRA_CORRECT_ANSWER, correctAnswer);
		return intent;
	}

	public static boolean wasResultShown(int resultCode, Intent intent) {
		return resultCode == RESULT_OK && intent != null && intent.hasExtra(EXTRA_ANSWER_SHOWN) && intent.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
	}

	public void onClickBtnCheatBack(View view) {
		this.finish();
	}
}
