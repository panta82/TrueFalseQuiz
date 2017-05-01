package net.pantas.truefalsequiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

	private final static String EXTRA_CORRECT_ANSWER = "net.pantas.truefalsequiz.EXTRA_CORRECT_ANSWER";
	private final static String EXTRA_ANSWER_SHOWN = "net.pantas.truefalsequiz.EXTRA_ANSWER_SHOWN";

	private final static String CORRECT_ANSWER_KEY = "CORRECT_ANSWER_KEY";
	private final static String REVEALED_KEY = "REVEALED_KEY";

	private TextView mAnswerTextView;
	private Button mRevealButton;

	private boolean mCorrectAnswer;
	private boolean mRevealed = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cheat);

		mAnswerTextView = (TextView) findViewById(R.id.cheat_answer);
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
			setRevealed(false);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean(CORRECT_ANSWER_KEY, mCorrectAnswer);
		outState.putBoolean(REVEALED_KEY, mRevealed);
		super.onSaveInstanceState(outState);
	}

	private void setRevealed(boolean animate) {
		mRevealed = true;
		mAnswerTextView.setText(mCorrectAnswer ? "TRUE" : "FALSE");
		if (!animate || android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
			mAnswerTextView.setVisibility(View.VISIBLE);
			mRevealButton.setVisibility(View.INVISIBLE);
		} else {
			int cx = mRevealButton.getWidth() / 2;
			int cy = mRevealButton.getHeight() / 2;
			float radius = mRevealButton.getWidth();
			Animator anim = ViewAnimationUtils.createCircularReveal(mRevealButton, cx, cy, radius, 0);
			anim.addListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					super.onAnimationEnd(animation);
					mAnswerTextView.setVisibility(View.VISIBLE);
					mRevealButton.setVisibility(View.INVISIBLE);
				}
			});
			anim.start();
		}

		Intent returnIntent = new Intent();
		returnIntent.putExtra(EXTRA_ANSWER_SHOWN, true);
		setResult(RESULT_OK, returnIntent);
	}

	public void onClickBtnCheatReveal(View view) {
		setRevealed(true);
	}

	public void onClickBtnCheatBack(View view) {
		this.finish();
	}

	public static Intent newIntent(Context packageContext, boolean correctAnswer) {
		Intent intent = new Intent(packageContext, CheatActivity.class);
		intent.putExtra(CheatActivity.EXTRA_CORRECT_ANSWER, correctAnswer);
		return intent;
	}

	public static boolean wasResultShown(int resultCode, Intent intent) {
		return resultCode == RESULT_OK && intent != null && intent.hasExtra(EXTRA_ANSWER_SHOWN) && intent.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
	}
}
