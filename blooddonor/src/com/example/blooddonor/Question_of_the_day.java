package com.example.blooddonor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.blooddonor.LocationServices.AppLocationService;
import com.example.blooddonor.Utils.Utils;

public class Question_of_the_day extends Activity implements
		android.view.View.OnClickListener {
	JSONObject obj = new JSONObject();
	TextView questionediEditText;
	LinearLayout options;
	LayoutParams params;
	Button submitchoose;
	RadioGroup rg;
	String json;
	String answer;
	String explanation;
	TextView explanationTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question_of_the_day);
		questionediEditText = (TextView) findViewById(R.id.question);
		options = (LinearLayout) findViewById(R.id.options);
		submitchoose = (Button) findViewById(R.id.SubmitChoose);

		json = getIntent().getExtras().getString("question");
		explanationTextView = (TextView) findViewById(R.id.explanation);
		submitchoose.setOnClickListener(this);
		params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		System.out.println(json);

		try {
			JSONObject obj = new JSONObject(json);
			questionediEditText.setText(obj.getString("question"));
			answer = obj.getString("answer");
			explanation = obj.getString("explanation");
			JSONArray optionsarr = obj.getJSONArray("options");
			final RadioButton[] rb = new RadioButton[5];
			rg = new RadioGroup(this);
			rg.setOrientation(RadioGroup.VERTICAL);
			for (int i = 0; i < optionsarr.length(); i++) {
				rb[i] = new RadioButton(this);
				rg.addView(rb[i]);
				rb[i].setText(optionsarr.getString(i));

				if (i == 0)
					rb[i].setChecked(true);

			}
			options.addView(rg);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.SubmitChoose) {

			int id = rg.getCheckedRadioButtonId();
			submitchoose.setVisibility(Button.INVISIBLE);
			RadioButton checkedButton = (RadioButton) findViewById(id);
			System.out.println(checkedButton.getText());
			if (checkedButton.getText().equals(answer)) {
				checkedButton.setTextColor(Color.parseColor("#50b848"));
				explanationTextView.setText("Correct" + '\n' + "Explanation:"
						+ explanation);
			} else {
				checkedButton.setTextColor(Color.parseColor("#d71920"));
				explanationTextView.setText("Wrong" + '\n' + "Explanation:"
						+ explanation);

			}

		}
	}

}
