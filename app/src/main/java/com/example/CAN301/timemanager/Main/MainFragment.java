package com.example.CAN301.timemanager.Main;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.CAN301.timemanager.AddTask.AddTaskActivity;
import com.example.CAN301.timemanager.AddTask.AddTaskFragment;

import com.example.CAN301.timemanager.R;
import com.example.CAN301.timemanager.Utility.ItemTouchHelperClass;
import com.example.CAN301.timemanager.Utility.RecyclerViewEmptySupport;
import com.example.CAN301.timemanager.Utility.StoreRetrieveData;
import com.example.CAN301.timemanager.Utility.TaskItem;
import com.example.CAN301.timemanager.Utility.TaskNotificationService;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static android.app.Activity.RESULT_CANCELED;
import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class MainFragment extends Fragment {
    RecyclerViewEmptySupport mRecyclerView;
    FloatingActionButton mAddToDoItemFAB;
    ArrayList<TaskItem> mTaskItemsArrayList;
    CoordinatorLayout mCoordLayout;
    public static final String TODOITEM = "com.example.CAN301.timemanager.MainActivity";
    MainFragment.BasicListAdapter adapter;
    static final int REQUEST_ID_TODO_ITEM = 100;
    TaskItem mJustDeletedTaskItem;
    int mIndexOfDeletedToDoItem;
    public static final String DATE_TIME_FORMAT_12_HOUR = "MMM d,yyyy h:mm a";
    public static final String DATE_TIME_FORMAT_24_HOUR = "MMM d,yyyy k:mm";
    public static final String FILENAME = "todoitems.json";
    public static StoreRetrieveData storeRetrieveData;
    public ItemTouchHelper itemTouchHelper;
    CustomRecyclerScrollViewListener customRecyclerScrollViewListener;
    public static final String SHARED_PREF_DATA_SET_CHANGED = "com.example.CAN301.timemanager.datasetchanged";
    public static final String CHANGE_OCCURED = "com.example.CAN301.timemanager.changeoccured";
    int mTheme = -1;
    String theme = "name_of_the_theme";
    public static final String THEME_PREFERENCES = "com.example.CAN301.timemanager.themepref";
    public static final String RECREATE_ACTIVITY = "com.example.CAN301.timemanager.recreateactivity";
    public static final String THEME_SAVED = "com.example.CAN301.timemanager.savedtheme";
    public static final String DARKTHEME = "com.example.CAN301.timemanager.darktheme";
    public static final String LIGHTTHEME = "com.example.CAN301.timemanager.lighttheme";

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        theme = getActivity().getSharedPreferences(THEME_PREFERENCES, MODE_PRIVATE).getString(THEME_SAVED, LIGHTTHEME);
        if (theme.equals(LIGHTTHEME)) {
            mTheme = R.style.CustomStyle_LightTheme;
        } else {
            mTheme = R.style.CustomStyle_DarkTheme;
        }
        this.getActivity().setTheme(mTheme);
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF_DATA_SET_CHANGED, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(CHANGE_OCCURED, false);
        editor.apply();
        storeRetrieveData = new StoreRetrieveData(getContext(), FILENAME);
        mTaskItemsArrayList = getLocallyStoredData(storeRetrieveData);
        adapter = new MainFragment.BasicListAdapter(mTaskItemsArrayList);
        setAlarms();
        mCoordLayout = (CoordinatorLayout) view.findViewById(R.id.myCoordinatorLayout);
        mAddToDoItemFAB = (FloatingActionButton) view.findViewById(R.id.addToDoItemFAB);

        mAddToDoItemFAB.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                Intent newTodo = new Intent(getContext(), AddTaskActivity.class);
                TaskItem item = new TaskItem("", "", false, null);
                int color = ColorGenerator.MATERIAL.getRandomColor();
                item.setTodoColor(color);
                newTodo.putExtra(TODOITEM, item);
                startActivityForResult(newTodo, REQUEST_ID_TODO_ITEM);
            }
        });
        mRecyclerView = (RecyclerViewEmptySupport) view.findViewById(R.id.toDoRecyclerView);
        if (theme.equals(LIGHTTHEME)) {
            mRecyclerView.setBackgroundColor(getResources().getColor(R.color.primary_lightest));
        }else{
            mRecyclerView.setBackgroundColor(getResources().getColor(R.color.primary_darkest));
        }
        mRecyclerView.setEmptyView(view.findViewById(R.id.toDoEmptyView));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        customRecyclerScrollViewListener = new CustomRecyclerScrollViewListener() {
            @Override
            public void show() {
                mAddToDoItemFAB.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
            }

            @Override
            public void hide() {
                CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) mAddToDoItemFAB.getLayoutParams();
                int fabMargin = lp.bottomMargin;
                mAddToDoItemFAB.animate().translationY(mAddToDoItemFAB.getHeight() + fabMargin).setInterpolator(new AccelerateInterpolator(2.0f)).start();
            }
        };
        mRecyclerView.addOnScrollListener(customRecyclerScrollViewListener);
        ItemTouchHelper.Callback callback = new ItemTouchHelperClass(adapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(adapter);
    }



    @Override
    public void onResume() {
        super.onResume();
        if (getActivity().getSharedPreferences(THEME_PREFERENCES, MODE_PRIVATE).getBoolean(RECREATE_ACTIVITY, false)) {
            SharedPreferences.Editor editor = getActivity().getSharedPreferences(THEME_PREFERENCES, MODE_PRIVATE).edit();
            editor.putBoolean(RECREATE_ACTIVITY, false);
            editor.apply();
            getActivity().recreate();
        }
    }



    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF_DATA_SET_CHANGED, MODE_PRIVATE);
        if (sharedPreferences.getBoolean(CHANGE_OCCURED, false)) {
            mTaskItemsArrayList = getLocallyStoredData(storeRetrieveData);
            adapter = new MainFragment.BasicListAdapter(mTaskItemsArrayList);
            mRecyclerView.setAdapter(adapter);
            setAlarms();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(CHANGE_OCCURED, false);
            editor.apply();
        }
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED && requestCode == REQUEST_ID_TODO_ITEM) {
            TaskItem item = (TaskItem) data.getSerializableExtra(TODOITEM);
            if (item.getToDoText().length() <= 0) {
                return;
            }
            boolean existed = false;
            if (item.hasReminder() && item.getToDoDate() != null) {
                Intent i = new Intent(getContext(), TaskNotificationService.class);
                i.putExtra(TaskNotificationService.TODOTEXT, item.getToDoText());
                i.putExtra(TaskNotificationService.TODOUUID, item.getIdentifier());
                createAlarm(i, item.getIdentifier().hashCode(), item.getToDoDate().getTime());
            }
            for (int i = 0; i < mTaskItemsArrayList.size(); i++) {
                if (item.getIdentifier().equals(mTaskItemsArrayList.get(i).getIdentifier())) {
                    mTaskItemsArrayList.set(i, item);
                    existed = true;
                    adapter.notifyDataSetChanged();
                    break;
                }
            }
            if (!existed) {
                addToDataStore(item);
            }
        }
    }

    private void setAlarms() {
        if (mTaskItemsArrayList != null) {
            for (TaskItem item : mTaskItemsArrayList) {
                if (item.hasReminder() && item.getToDoDate() != null) {
                    if (item.getToDoDate().before(new Date())) {
                        item.setToDoDate(null);
                        continue;
                    }
                    Intent i = new Intent(getContext(), TaskNotificationService.class);
                    i.putExtra(TaskNotificationService.TODOUUID, item.getIdentifier());
                    i.putExtra(TaskNotificationService.TODOTEXT, item.getToDoText());
                    createAlarm(i, item.getIdentifier().hashCode(), item.getToDoDate().getTime());
                }
            }
        }
    }

    public static ArrayList<TaskItem> getLocallyStoredData(StoreRetrieveData storeRetrieveData) {
        ArrayList<TaskItem> items = null;
        try {
            items = storeRetrieveData.loadFromFile();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        if (items == null) {
            items = new ArrayList<>();
        }
        return items;
    }

    private AlarmManager getAlarmManager() {
        return (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
    }

    private boolean doesPendingIntentExist(Intent i, int requestCode) {
        PendingIntent pi = PendingIntent.getService(getContext(), requestCode, i, PendingIntent.FLAG_NO_CREATE);
        return pi != null;
    }

    private void createAlarm(Intent i, int requestCode, long timeInMillis) {
        AlarmManager am = getAlarmManager();
        PendingIntent pi = PendingIntent.getService(getContext(), requestCode, i, PendingIntent.FLAG_UPDATE_CURRENT);
        am.set(AlarmManager.RTC_WAKEUP, timeInMillis, pi);
    }

    private void deleteAlarm(Intent i, int requestCode) {
        if (doesPendingIntentExist(i, requestCode)) {
            PendingIntent pi = PendingIntent.getService(getContext(), requestCode, i, PendingIntent.FLAG_NO_CREATE);
            pi.cancel();
            getAlarmManager().cancel(pi);
        }
    }

    private void addToDataStore(TaskItem item) {
        mTaskItemsArrayList.add(item);
        adapter.notifyItemInserted(mTaskItemsArrayList.size() - 1);
    }

    public class BasicListAdapter extends RecyclerView.Adapter<BasicListAdapter.ViewHolder> implements ItemTouchHelperClass.ItemTouchHelperAdapter {
        ArrayList<TaskItem> items;

        @Override
        public void onItemMoved(int fromPosition, int toPosition) {
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(items, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(items, i, i - 1);
                }
            }
            notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public void onItemRemoved(final int position) {
            mJustDeletedTaskItem = items.remove(position);
            mIndexOfDeletedToDoItem = position;
            Intent i = new Intent(getContext(), TaskNotificationService.class);
            deleteAlarm(i, mJustDeletedTaskItem.getIdentifier().hashCode());
            notifyItemRemoved(position);
            String toShow = "Todo";
            Snackbar.make(mCoordLayout, "Deleted " + toShow, Snackbar.LENGTH_LONG)
                    .setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            items.add(mIndexOfDeletedToDoItem, mJustDeletedTaskItem);
                            if (mJustDeletedTaskItem.getToDoDate() != null && mJustDeletedTaskItem.hasReminder()) {
                                Intent i = new Intent(getContext(), TaskNotificationService.class);
                                i.putExtra(TaskNotificationService.TODOTEXT, mJustDeletedTaskItem.getToDoText());
                                i.putExtra(TaskNotificationService.TODOUUID, mJustDeletedTaskItem.getIdentifier());
                                createAlarm(i, mJustDeletedTaskItem.getIdentifier().hashCode(), mJustDeletedTaskItem.getToDoDate().getTime());
                            }
                            notifyItemInserted(mIndexOfDeletedToDoItem);
                        }
                    }).show();
        }


        @Override
        public BasicListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_circle_try, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final BasicListAdapter.ViewHolder holder, final int position) {
            TaskItem item = items.get(position);
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(THEME_PREFERENCES, MODE_PRIVATE);
            int bgColor;
            int todoTextColor;
            if (sharedPreferences.getString(THEME_SAVED, LIGHTTHEME).equals(LIGHTTHEME)) {
                bgColor = Color.WHITE;
                todoTextColor = getResources().getColor(R.color.secondary_text);
            } else {
                bgColor = Color.DKGRAY;
                todoTextColor = Color.WHITE;
            }
            holder.linearLayout.setBackgroundColor(bgColor);
            if (item.hasReminder() && item.getToDoDate() != null) {
                holder.mToDoTextview.setMaxLines(1);
                holder.mTimeTextView.setVisibility(View.VISIBLE);
            } else {
                holder.mTimeTextView.setVisibility(View.GONE);
                holder.mToDoTextview.setMaxLines(2);
            }
            holder.mToDoTextview.setText(item.getToDoText());
            holder.mToDoTextview.setTextColor(todoTextColor);
            TextDrawable myDrawable = TextDrawable.builder().beginConfig()
                    .textColor(Color.WHITE)
                    .useFont(Typeface.DEFAULT)
                    .toUpperCase()
                    .endConfig()
                    .buildRound(item.getToDoText().substring(0, 1), item.getTodoColor());
            holder.mColorImageView.setImageDrawable(myDrawable);
            if (item.getToDoDate() != null) {
                String targetTime;
                if (android.text.format.DateFormat.is24HourFormat(getContext())) {
                    targetTime = AddTaskFragment.formatDate(MainFragment.DATE_TIME_FORMAT_24_HOUR, item.getToDoDate());
                } else {
                    targetTime = AddTaskFragment.formatDate(MainFragment.DATE_TIME_FORMAT_12_HOUR, item.getToDoDate());
                }
                Date targetDate = item.getToDoDate();
                long targetMilliseconds = targetDate.getTime();
                Date currentDate = new Date(System.currentTimeMillis());
                long currentMilliseconds = currentDate.getTime();
                long millsecondsRemain = targetMilliseconds - currentMilliseconds;
                long remainDay = TimeUnit.MILLISECONDS.toDays(millsecondsRemain);
                long remainHours = TimeUnit.MILLISECONDS.toHours(millsecondsRemain)
                        - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millsecondsRemain));
                long remainMinutes = TimeUnit.MILLISECONDS.toMinutes(millsecondsRemain)
                        - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millsecondsRemain));
                holder.mTimeTextView.setText("Date: " + targetTime + "      " + "Still have: " + remainDay + "days " + remainHours + "h " + remainMinutes + "mins");
            }
        }


        @Override
        public int getItemCount() {
            return items.size();
        }

        BasicListAdapter(ArrayList<TaskItem> items) {
            this.items = items;
        }

        @SuppressWarnings("deprecation")
        public class ViewHolder extends RecyclerView.ViewHolder {
            View mView;
            LinearLayout linearLayout;
            TextView mToDoTextview;
            ImageView mColorImageView;
            TextView mTimeTextView;

            public ViewHolder(View v) {
                super(v);
                mView = v;
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TaskItem item = items.get(ViewHolder.this.getAdapterPosition());
                        Intent i = new Intent(getContext(), AddTaskActivity.class);
                        i.putExtra(TODOITEM, item);
                        startActivityForResult(i, REQUEST_ID_TODO_ITEM);
                    }
                });
                mToDoTextview = (TextView) v.findViewById(R.id.toDoListItemTextview);
                mTimeTextView = (TextView) v.findViewById(R.id.todoListItemTimeTextView);
                mColorImageView = (ImageView) v.findViewById(R.id.toDoListItemColorImageView);
                linearLayout = (LinearLayout) v.findViewById(R.id.listItemLinearLayout);
            }
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        try {
            storeRetrieveData.saveToFile(mTaskItemsArrayList);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRecyclerView.removeOnScrollListener(customRecyclerScrollViewListener);
    }



    public static MainFragment newInstance() {
        return new MainFragment();
    }
}
