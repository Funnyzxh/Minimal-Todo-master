package com.example.CAN301.timemanager.AddTask;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.CAN301.timemanager.Main.MainFragment;
import com.example.CAN301.timemanager.R;
import com.example.CAN301.timemanager.Utility.TaskItem;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class AddTaskFragment extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    EditText mTaskTextBodyEditText;
    EditText mTaskTextBodyDescription;
    SwitchCompat mTaskDateSwitch;
    LinearLayout mUserDateSpinnerContainingLinearLayout;
    TextView mReminderTextView;
    EditText mDateEditText;
    EditText mTimeEditText;
    TaskItem mUserTaskItem;
    FloatingActionButton mTaskSendFloatingActionButton;
    String mUserEnteredText;
    String mUserEnteredDescription;
    boolean mUserHasReminder;
    Toolbar mToolbar;
    Date mUserReminderDate;
    int mUserColor;
    LinearLayout mContainerLayout;
    String theme;

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_add_to_do, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageButton reminderIconImageButton;
        TextView reminderRemindMeTextView;
        theme = getActivity().getSharedPreferences(MainFragment.THEME_PREFERENCES, MODE_PRIVATE).getString(MainFragment.THEME_SAVED, MainFragment.LIGHTTHEME);
        if (theme.equals(MainFragment.LIGHTTHEME)) {
            getActivity().setTheme(R.style.CustomStyle_LightTheme);
        } else {
            getActivity().setTheme(R.style.CustomStyle_DarkTheme);
        }
        final Drawable cross = getResources().getDrawable(R.drawable.clear_image);
        if (cross != null) {
            cross.setColorFilter(getResources().getColor(R.color.icons), PorterDuff.Mode.SRC_ATOP);
        }
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setElevation(0);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(cross);
        }
        mUserTaskItem = (TaskItem) getActivity().getIntent().getSerializableExtra(MainFragment.TASKITEM);
        mUserEnteredText = mUserTaskItem.getTaskText();
        mUserEnteredDescription = mUserTaskItem.getmTaskDescription();
        mUserHasReminder = mUserTaskItem.hasReminder();
        mUserReminderDate = mUserTaskItem.getTaskDate();
        mUserColor = mUserTaskItem.getTaskColor();
        reminderIconImageButton = (ImageButton) view.findViewById(R.id.userTaskReminderIconImageButton);
        reminderRemindMeTextView = (TextView) view.findViewById(R.id.userTaskRemindMeTextView);
        if (theme.equals(MainFragment.DARKTHEME)) {
            reminderIconImageButton.setImageDrawable(getResources().getDrawable(R.drawable.clock_image_white));
            reminderRemindMeTextView.setTextColor(Color.WHITE);
        }

        mContainerLayout = (LinearLayout) view.findViewById(R.id.taskReminderAndDateContainerLayout);
        mUserDateSpinnerContainingLinearLayout = (LinearLayout) view.findViewById(R.id.taskEnterDateLinearLayout);
        mTaskTextBodyEditText = (EditText) view.findViewById(R.id.userTaskEditText);
        mTaskTextBodyDescription = (EditText) view.findViewById(R.id.userTaskDescription);
        mTaskDateSwitch = (SwitchCompat) view.findViewById(R.id.taskHasDateSwitchCompat);
        mTaskSendFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.makeTaskFloatingActionButton);
        mReminderTextView = (TextView) view.findViewById(R.id.newTaskDateTimeReminderTextView);
        View.OnClickListener layoutClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(mTaskTextBodyEditText);
                hideKeyboard(mTaskTextBodyDescription);
            }
        };
        mContainerLayout.setOnClickListener(layoutClickListener);
        if (mUserHasReminder && (mUserReminderDate != null)) {
            setReminderTextView();
            setEnterDateLayoutVisibleWithAnimations(true);
        }
        if (mUserReminderDate == null) {
            mTaskDateSwitch.setChecked(false);
            mReminderTextView.setVisibility(View.INVISIBLE);
        }
        mTaskTextBodyEditText.requestFocus();
        mTaskTextBodyEditText.setText(mUserEnteredText);
        mTaskTextBodyDescription.setText(mUserEnteredDescription);
        InputMethodManager imm = (InputMethodManager) this.getActivity().getSystemService(INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        mTaskTextBodyEditText.setSelection(mTaskTextBodyEditText.length());

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mUserEnteredText = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        mTaskTextBodyEditText.addTextChangedListener(textWatcher);
        mTaskTextBodyDescription.setText(mUserEnteredDescription);
        mTaskTextBodyDescription.setSelection(mTaskTextBodyDescription.length());
        mTaskTextBodyDescription.addTextChangedListener(textWatcher);
        setEnterDateLayoutVisible(mTaskDateSwitch.isChecked());
        mTaskDateSwitch.setChecked(mUserHasReminder && (mUserReminderDate != null));

        CompoundButton.OnCheckedChangeListener changeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    mUserReminderDate = null;
                }
                mUserHasReminder = isChecked;
                setDateAndTimeEditText();
                setEnterDateLayoutVisibleWithAnimations(isChecked);
                hideKeyboard(mTaskTextBodyEditText);
                hideKeyboard(mTaskTextBodyDescription);
            }
        };
        mTaskDateSwitch.setOnCheckedChangeListener(changeListener);

        View.OnClickListener taskClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTaskTextBodyEditText.length() <= 0) {
                    mTaskTextBodyEditText.setError(getString(R.string.task_error));
                } else if (mUserReminderDate != null && mUserReminderDate.before(new Date())) {
                    makeResult(RESULT_CANCELED);
                } else {
                    makeResult(RESULT_OK);
                    getActivity().finish();
                }
                hideKeyboard(mTaskTextBodyEditText);
                hideKeyboard(mTaskTextBodyDescription);
            }
        };
        mTaskSendFloatingActionButton.setOnClickListener(taskClickListener);
        mDateEditText = (EditText) view.findViewById(R.id.newTaskDateEditText);
        mTimeEditText = (EditText) view.findViewById(R.id.newTaskTimeEditText);

        View.OnClickListener dateClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                Date date;
                hideKeyboard(mTaskTextBodyEditText);
                if (mUserTaskItem.getTaskDate() != null) {
                    date = mUserReminderDate;
                } else {
                    date = new Date();
                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(AddTaskFragment.this, year, month, day);
                if (theme.equals(MainFragment.DARKTHEME)) {
                    datePickerDialog.setThemeDark(true);
                }
                datePickerDialog.show(getActivity().getFragmentManager(), "DateFragment");
            }
        };
        mDateEditText.setOnClickListener(dateClickListener);

        View.OnClickListener timeClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date;
                hideKeyboard(mTaskTextBodyEditText);
                if (mUserTaskItem.getTaskDate() != null) {
                    date = mUserReminderDate;
                } else {
                    date = new Date();
                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(AddTaskFragment.this, hour, minute, DateFormat.is24HourFormat(getContext()));
                if (theme.equals(MainFragment.DARKTHEME)) {
                    timePickerDialog.setThemeDark(true);
                }
                timePickerDialog.show(getActivity().getFragmentManager(), "TimeFragment");
            }
        };
        mTimeEditText.setOnClickListener(timeClickListener);
        setDateAndTimeEditText();
    }

    private void setDateAndTimeEditText() {
        if (mUserTaskItem.hasReminder() && mUserReminderDate != null) {
            String userDate = formatDate("d MMM, yyyy", mUserReminderDate);
            String formatToUse;
            if (DateFormat.is24HourFormat(getContext())) {
                formatToUse = "k:mm";
            } else {
                formatToUse = "h:mm a";
            }
            String userTime = formatDate(formatToUse, mUserReminderDate);
            mTimeEditText.setText(userTime);
            mDateEditText.setText(userDate);
        } else {
            mDateEditText.setText(getString(R.string.date_reminder_default));
            boolean time24 = DateFormat.is24HourFormat(getContext());
            Calendar cal = Calendar.getInstance();
            if (time24) {
                cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) + 1);
            } else {
                cal.set(Calendar.HOUR, cal.get(Calendar.HOUR) + 1);
            }
            cal.set(Calendar.MINUTE, 0);
            mUserReminderDate = cal.getTime();
            String timeString;
            if (time24) {
                timeString = formatDate("k:mm", mUserReminderDate);
            } else {
                timeString = formatDate("h:mm a", mUserReminderDate);
            }
            mTimeEditText.setText(timeString);
        }
    }


    public void hideKeyboard(EditText et) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
    }

    public void setDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        int hour, minute;
        Calendar reminderCalendar = Calendar.getInstance();
        reminderCalendar.set(year, month, day);
        if (reminderCalendar.before(calendar)) {
            return;
        }
        if (mUserReminderDate != null) {
            calendar.setTime(mUserReminderDate);
        }
        if (DateFormat.is24HourFormat(getContext())) {
            hour = calendar.get(Calendar.HOUR_OF_DAY);
        } else {
            hour = calendar.get(Calendar.HOUR);
        }
        minute = calendar.get(Calendar.MINUTE);
        calendar.set(year, month, day, hour, minute);
        mUserReminderDate = calendar.getTime();
        setReminderTextView();
        setDateEditText();
    }

    public void setTime(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        if (mUserReminderDate != null) {
            calendar.setTime(mUserReminderDate);
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(year, month, day, hour, minute, 0);
        mUserReminderDate = calendar.getTime();
        setReminderTextView();
        setTimeEditText();
    }

    public void setDateEditText() {
        String dateFormat = "d MMM, yyyy";
        mDateEditText.setText(formatDate(dateFormat, mUserReminderDate));
    }

    public void setTimeEditText() {
        String dateFormat;
        if (DateFormat.is24HourFormat(getContext())) {
            dateFormat = "k:mm";
        } else {
            dateFormat = "h:mm a";
        }
        mTimeEditText.setText(formatDate(dateFormat, mUserReminderDate));
    }

    public void setReminderTextView() {
        if (mUserReminderDate != null) {
            mReminderTextView.setVisibility(View.VISIBLE);
            if (mUserReminderDate.before(new Date())) {
                mReminderTextView.setText(getString(R.string.date_error_check_again));
                mReminderTextView.setTextColor(Color.RED);
                return;
            }
            Date date = mUserReminderDate;
            String dateString = formatDate("d MMM, yyyy", date);
            String timeString;
            String amPmString = "";
            if (DateFormat.is24HourFormat(getContext())) {
                timeString = formatDate("k:mm", date);
            } else {
                timeString = formatDate("h:mm", date);
                amPmString = formatDate("a", date);
            }
            String finalString = String.format(getResources().getString(R.string.remind_date_and_time), dateString, timeString, amPmString);
            mReminderTextView.setTextColor(getResources().getColor(R.color.secondary_text));
            mReminderTextView.setText(finalString);
        } else {
            mReminderTextView.setVisibility(View.INVISIBLE);
        }
    }

    public void makeResult(int result) {
        Intent i = new Intent();
        if (mUserEnteredText.length() > 0) {
            String capitalizedString = Character.toUpperCase(mUserEnteredText.charAt(0)) + mUserEnteredText.substring(1);
            mUserTaskItem.setTaskText(capitalizedString);
            mUserTaskItem.setmTaskDescription(mUserEnteredDescription);
        } else {
            mUserTaskItem.setTaskText(mUserEnteredText);
            mUserTaskItem.setmTaskDescription(mUserEnteredDescription);
        }
        if (mUserReminderDate != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(mUserReminderDate);
            calendar.set(Calendar.SECOND, 0);
            mUserReminderDate = calendar.getTime();
        }
        mUserTaskItem.setHasReminder(mUserHasReminder);
        mUserTaskItem.setTaskDate(mUserReminderDate);
        mUserTaskItem.setTaskColor(mUserColor);
        i.putExtra(MainFragment.TASKITEM, mUserTaskItem);
        getActivity().setResult(result, i);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (NavUtils.getParentActivityName(getActivity()) != null) {
                    makeResult(RESULT_CANCELED);
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                hideKeyboard(mTaskTextBodyEditText);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static String formatDate(String formatString, Date dateToFormat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatString);
        return simpleDateFormat.format(dateToFormat);
    }

    @Override
    public void onTimeSet(RadialPickerLayout radialPickerLayout, int hour, int minute) {
        setTime(hour, minute);
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        setDate(year, month, day);
    }

    public void setEnterDateLayoutVisible(boolean checked) {
        if (checked) {
            mUserDateSpinnerContainingLinearLayout.setVisibility(View.VISIBLE);
        } else {
            mUserDateSpinnerContainingLinearLayout.setVisibility(View.INVISIBLE);
        }
    }

    public void setEnterDateLayoutVisibleWithAnimations(boolean checked) {
        Animator.AnimatorListener animatorTrue = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                mUserDateSpinnerContainingLinearLayout.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationEnd(Animator animation) {
            }
            @Override
            public void onAnimationCancel(Animator animation) {
            }
            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        };

        Animator.AnimatorListener animatorFalse = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                mUserDateSpinnerContainingLinearLayout.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onAnimationCancel(Animator animation) {
            }
            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        };

        if (checked) {
            setReminderTextView();
            mUserDateSpinnerContainingLinearLayout.animate().alpha(1.0f).setDuration(500).setListener(animatorTrue);
        } else {
            mUserDateSpinnerContainingLinearLayout.animate().alpha(0.0f).setDuration(500).setListener(animatorFalse);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public static AddTaskFragment newInstance() {
        return new AddTaskFragment();
    }
}
