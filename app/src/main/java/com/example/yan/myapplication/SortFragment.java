package com.example.yan.myapplication;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.yan.myapplication.vo.BeatWorldVo;
import com.shinelw.library.ColorArcProgressBar;
import com.yan.db.DbConfig;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.DecimalFormat;
import java.text.Format;

import tools.BaseFragment;

/**
 * Created by yan on 2016/1/21.
 */
public class SortFragment extends BaseFragment implements View.OnClickListener {
    private TextView myTextView, beatWorldTextView;
    private boolean isPrepare;
    private Double avgAsu;
    private Button shareButton, aroundButton;
    private String str = "我击败了全国0%人";
    private ColorArcProgressBar mProgressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, null);
        myTextView = (TextView) view.findViewById(R.id.beat_my_asu);
        beatWorldTextView = (TextView) view.findViewById(R.id.beat_world);
        shareButton = (Button) view.findViewById(R.id.my_share);
        aroundButton = (Button) view.findViewById(R.id.my_around);
        mProgressBar = (ColorArcProgressBar) view.findViewById(R.id.bar1);
        shareButton.setOnClickListener(this);
        aroundButton.setOnClickListener(this);

        x.Ext.init(getActivity().getApplication());
        isPrepare = true;
        lazy();
        return view;
    }

    @Override
    public void lazy() {
        if (!isPrepare || !isShow) {
            System.out.println("exit");
            return;
        }
        SortFragment.MyQuery myQuery = new MyQuery(getActivity().getContentResolver());
        myQuery.startQuery(0, null, DbConfig.CONTENT_AVG_ASU_URI, null, null, null, "SELECT AVG(asu) AS OrderAverage FROM " + DbConfig.TABLE_NAME);
        System.out.println("------------send----------");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_around:
                Intent aroundIntent = new Intent(getActivity(), MapActivity.class);
                startActivity(aroundIntent);
                break;
            case R.id.my_share:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("image/*");
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "beat");
                shareIntent.putExtra(Intent.EXTRA_TEXT, str);
                startActivity(Intent.createChooser(shareIntent, "beatWorld"));
                break;
            default:
                break;
        }
    }

    public class WorldAsy extends AsyncTask<String, String, String> {
        private String myAsu = null;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            BeatWorldVo myWorldVo = new BeatWorldVo();
            String tempResult = null;
            try {
                RequestParams param = new RequestParams(Config.URL_BEATWORLD);
                myAsu = params[0];
                System.out.println(params[0]);
                param.addQueryStringParameter("myAsu", params[0]);
                tempResult = x.http().getSync(param, String.class);

            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            return tempResult;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            myTextView.setText(myAsu + "");
            if (TextUtils.isEmpty(s)) {
                beatWorldTextView.setText("无法获取到数据，请稍后再试或重新连接网络！");
            } else {
                Format format = new DecimalFormat(".##");
                Double tempAsu = Double.valueOf(s);
                tempAsu = tempAsu * 100;
                String tempAvgAsu = String.valueOf(avgAsu);
                mProgressBar.setCurrentValues(Float.valueOf(tempAvgAsu));
                beatWorldTextView.setText("您击败了全国" + format.format(tempAsu) + "%的人");
                str = "我击败了全国" + format.format(tempAsu) + "%的人";
            }
        }
    }


    public class MyQuery extends AsyncQueryHandler {

        public MyQuery(ContentResolver cr) {
            super(cr);
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            super.onQueryComplete(token, cookie, cursor);
            while (cursor.moveToNext()) {
                avgAsu = cursor.getDouble(cursor.getColumnIndex("OrderAverage"));
                Format format = new DecimalFormat(".##");

                myTextView.setText("" + format.format(avgAsu));
                WorldAsy worldAsy = new WorldAsy();
                worldAsy.execute(avgAsu + "");
            }
        }
    }
}
