package com.example.yan.myapplication.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.yan.myapplication.service.MyService;
import com.example.yan.myapplication.R;
import com.example.yan.myapplication.model.UserData;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

public class ScrollingActivity extends AppCompatActivity {
    private List<UserData> data = new ArrayList<>();
    private Button button;
    private Button button2;
    private Button button3;
    private LineChartView lineChartView;
    private List<PointValue> mPointValues = new ArrayList<>();
    private List<AxisValue> mAxisValues = new ArrayList<>();
    private List<AxisValue> mYAxisValues = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        //
        lineChartView = (LineChartView) findViewById(R.id.lineChart1);
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.yan.myapplication_preferences", MODE_PRIVATE);
        Boolean upData = sharedPreferences.getBoolean("switch_connect", true);
        if (upData) {
            Intent intent = new Intent(ScrollingActivity.this, MyService.class);
            startService(intent);
        }
//        initChart();
//        button = (Button) findViewById(R.id.showChart);
//        button3 = (Button) findViewById(R.id.Main);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ScrollingActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
//        button2 = (Button) findViewById(R.id.change);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScrollingActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                initChart();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


//    public void initChart() {
//        mPointValues.clear();
//        mAxisValues.clear();
//        mYAxisValues.clear();
//        for (int i = 0; i < SaveData.data.size(); i++) {
//            mPointValues.add(new PointValue(i, SaveData.data.get(i).getAsu()));
////            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
//            mAxisValues.add(new AxisValue(i).setLabel(SaveData.data.get(i).getDate())); //为每个对应的i设置相应的label(显示在X轴)
//        }
//        for (int i = 0; i < 36; i++) {
//            mYAxisValues.add(new AxisValue(i).setLabel(i + ""));
//        }
//
//        Line line = new Line(mPointValues);
//        line.setFilled(false);
//        line.setCubic(false);
//        line.setColor(Color.parseColor("#00ad82"));
//
//        List<Line> lines = new ArrayList<>();
//        lines.add(line);
//
//        LineChartData data1 = new LineChartData(lines);
//
//        //坐标轴
//        Axis axisX = new Axis(); //X轴
//        axisX.setTextColor(Color.parseColor("#FFFFFF"));
//        axisX.setMaxLabelChars(10);
//        axisX.setValues(mAxisValues);
//        data1.setAxisXBottom(axisX);
//
//        Axis axisY = new Axis().setHasLines(true);  //Y轴
//        axisY.setValues(mYAxisValues);
//        axisY.setMaxLabelChars(10); //默认是3，只能看最后三个数字
//        axisY.setTextColor(Color.parseColor("#FFFFFF"));
//        data1.setAxisYLeft(axisY);
//        data1.setBaseValue(Float.NEGATIVE_INFINITY);
//
//        final Viewport vv = new Viewport(lineChartView.getMaximumViewport());
//        vv.bottom = 0;
//        vv.top = 36;
//        vv.left = 0;
//        vv.right = SaveData.data.size();
//        //设置行为属性，支持缩放、滑动以及平移
//        lineChartView.setInteractive(true);
//        lineChartView.setZoomType(ZoomType.HORIZONTAL_AND_VERTICAL);
//        lineChartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
//        lineChartView.setLineChartData(data1);
//        lineChartView.setMaximumViewport(vv);
//        lineChartView.setCurrentViewport(vv);
//        System.out.println(SaveData.data.size());
//
//        lineChartView.setOnValueTouchListener(new LineChartOnValueSelectListener() {
//            @Override
//            public void onValueSelected(int i, int i1, PointValue pointValue) {
////                System.out.println("----------->" + i + "-------->" + i1);
//                //System.out.println(SaveData.data.get(i1).getLaLatitude() + SaveData.data.get(i1).getLaLatitude() + "");
//                String[] diaStrings = new String[]{"Latitude:" + SaveData.data.get(i1).getLaLatitude(),
//                        "Longitude:" + SaveData.data.get(i1).getLongitude(),
//                        "ASU:" + SaveData.data.get(i1).getAsu()};
//                Dialog dialog = new AlertDialog.Builder(ScrollingActivity.this).setItems(diaStrings, null).create();
//                dialog.show();
//            }
//
//            @Override
//            public void onValueDeselected() {
//
//            }
//        });
//    }


}
