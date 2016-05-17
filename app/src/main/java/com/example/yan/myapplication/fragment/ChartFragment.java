package com.example.yan.myapplication.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.yan.myapplication.R;
import com.example.yan.myapplication.model.UserData;
import com.yan.db.DbConfig;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;
import tools.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChartFragment extends BaseFragment implements View.OnClickListener {

    private TextView startTime, endTime;
    private Button imageButton;
    private boolean isPrepare;
    //chart
    private List<UserData> datas = new ArrayList<>();
    private LineChartView lineChartView;
    private List<PointValue> mPointValues = new ArrayList<>();
    private List<AxisValue> mAxisValues = new ArrayList<>();
    private List<AxisValue> mYAxisValues = new ArrayList<>();

    public ChartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_scrolling, null);
        startTime = (TextView) view.findViewById(R.id.startTimeEdit);
        endTime = (TextView) view.findViewById(R.id.endTimeEdit);
        imageButton = (Button) view.findViewById(R.id.showChart);
        lineChartView = (LineChartView) view.findViewById(R.id.lineChart1);
        imageButton.setOnClickListener(this);
        startTime.setOnClickListener(this);
        endTime.setOnClickListener(this);
        isPrepare = true;
        lazy();
        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.startTimeEdit:
                AlertDialog.Builder startBuilder = new AlertDialog.Builder(getActivity());
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.date_selector, null);
                startBuilder.setView(view);
                final DatePicker datePicker = (DatePicker) view.findViewById(R.id.datepicker);
                final TimePicker timePicker = (TimePicker) view.findViewById(R.id.timepicker);
                datePicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
                timePicker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);
                timePicker.setIs24HourView(true);
                startBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int tempMouth = datePicker.getMonth() + 1;
                        int tempDay = datePicker.getDayOfMonth();
                        startTime.setText(datePicker.getYear() + "-"
                                + dataFormat(tempMouth) + "-"
                                + dataFormat(tempDay) + " "
                                + timePicker.getCurrentHour() + ":"
                                + timePicker.getCurrentMinute() + ":00");
                    }
                });
                startBuilder.setNegativeButton("cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                startBuilder.show();
                break;
            case R.id.endTimeEdit:
                AlertDialog.Builder endBuilder = new AlertDialog.Builder(getActivity());
                View endView = LayoutInflater.from(getActivity()).inflate(R.layout.date_selector, null);
                endBuilder.setView(endView);
                final DatePicker endDatePicker = (DatePicker) endView.findViewById(R.id.datepicker);
                final TimePicker endTimePicker = (TimePicker) endView.findViewById(R.id.timepicker);
                endDatePicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
                endTimePicker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);
                endTimePicker.setIs24HourView(true);
                endBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int tempEndMouth = endDatePicker.getMonth() + 1;
                        int tempEndDay = endDatePicker.getDayOfMonth();
                        endTime.setText(endDatePicker.getYear() + "-"
                                + dataFormat(tempEndMouth) + "-"
                                + dataFormat(tempEndDay) + " "
                                + endTimePicker.getCurrentHour() + ":"
                                + endTimePicker.getCurrentMinute() + ":00");
                    }
                });
                endBuilder.setNegativeButton("cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                endBuilder.show();
                break;
            case R.id.showChart:
                MyQuery myQuery = new MyQuery(getActivity().getContentResolver());
                myQuery.startQuery(0, null, DbConfig.CONTENT_NOTE_DATA_URI, null, null, null, "BETWEEN '" + startTime.getText().toString() + "' AND '" + endTime.getText().toString() + "'");
                break;
            default:
                break;

        }
    }

    @Override
    public void lazy() {
        if (!isPrepare || !isShow) {
            System.out.println("exit");
            return;
        }
        System.out.println("------------send----------");
    }

    public class MyQuery extends AsyncQueryHandler {

        public MyQuery(ContentResolver cr) {
            super(cr);
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            super.onQueryComplete(token, cookie, cursor);
            datas.clear();
            if (cursor == null) {
                return;
            }
            while (cursor.moveToNext()) {
                UserData data = new UserData();
                data.setAsu(cursor.getInt(cursor.getColumnIndex(DbConfig.ASU)));
                data.setLongitude(cursor.getDouble(cursor.getColumnIndex(DbConfig.LONGITUDE)));
                data.setLaLatitude(cursor.getDouble(cursor.getColumnIndex(DbConfig.ALTITUDE)));
                data.setDate(cursor.getString(cursor.getColumnIndex(DbConfig.DATE)));
                datas.add(data);
            }
//            if (datas.size() < 15) {
//                initLineChart();
//            } else {
//            }
            initLineChart();
        }
    }

    public void initLineChart() {
        mPointValues.clear();
        mAxisValues.clear();
        mYAxisValues.clear();
        lineChartView.setVisibility(View.VISIBLE);
        for (int i = 0; i < datas.size(); i++) {
            mPointValues.add(new PointValue(i, datas.get(i).getAsu()));
//            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            mAxisValues.add(new AxisValue(i).setLabel(datas.get(i).getDate())); //为每个对应的i设置相应的label(显示在X轴)
        }
        for (int i = 0; i < 36; i++) {
            mYAxisValues.add(new AxisValue(i).setLabel(i + ""));
        }

        Line line = new Line(mPointValues);
        line.setFilled(false);
        line.setCubic(false);
        line.setColor(Color.parseColor("#00ad82"));

        List<Line> lines = new ArrayList<>();
        lines.add(line);

        LineChartData data1 = new LineChartData(lines);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setTextColor(Color.parseColor("#009688"));
        axisX.setMaxLabelChars(10);
        axisX.setValues(mAxisValues);
        data1.setAxisXBottom(axisX);

        Axis axisY = new Axis().setHasLines(true);  //Y轴
        axisY.setValues(mYAxisValues);
        axisY.setMaxLabelChars(10); //默认是3，只能看最后三个数字
        axisY.setTextColor(Color.parseColor("#009688"));
        data1.setAxisYLeft(axisY);
        data1.setBaseValue(Float.NEGATIVE_INFINITY);

        final Viewport vv = new Viewport(lineChartView.getMaximumViewport());
        vv.bottom = 0;
        vv.top = 36;
        vv.left = 0;
        vv.right = datas.size();
        //设置行为属性，支持缩放、滑动以及平移
        lineChartView.setSelected(true);
        lineChartView.setInteractive(true);
        lineChartView.setContainerScrollEnabled(false, ContainerScrollType.HORIZONTAL);
        lineChartView.setLineChartData(data1);
        lineChartView.setMaximumViewport(vv);
        lineChartView.setCurrentViewport(vv);
        System.out.println(datas.size());
        lineChartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("----------click----linechart___--");
            }
        });
        lineChartView.setOnValueTouchListener(new LineChartOnValueSelectListener() {
            @Override
            public void onValueSelected(int i, int i1, PointValue pointValue) {
//                System.out.println("----------->" + i + "-------->" + i1);
                //System.out.println(SaveData.data.get(i1).getLaLatitude() + SaveData.data.get(i1).getLaLatitude() + "");
                String[] diaStrings = new String[]{"Latitude:" + datas.get(i1).getLaLatitude(),
                        "Longitude:" + datas.get(i1).getLongitude(),
                        "ASU:" + datas.get(i1).getAsu()};
                System.out.println("-----click---------->");
                Dialog dialog = new AlertDialog.Builder(getActivity()).setItems(diaStrings, null).create();
                dialog.show();
            }

            @Override
            public void onValueDeselected() {

            }
        });
    }

    public void initPreviewChart() {

    }

    public String dataFormat(int mouth) {
        if (mouth < 10) {
            return "0" + mouth;
        } else {
            return mouth + "";
        }
    }
}
