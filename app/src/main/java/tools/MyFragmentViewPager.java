package tools;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import lecho.lib.hellocharts.view.LineChartView;
import lecho.lib.hellocharts.view.PreviewLineChartView;

/**
 * Created by yan on 2016/1/21.
 */
public class MyFragmentViewPager extends ViewPager {
    public MyFragmentViewPager(Context context) {
        super(context);
    }

    public MyFragmentViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if (v != this && v instanceof LineChartView) {
            return true;
        } else if (v != this && v instanceof PreviewLineChartView) {
            return true;
        }
        return super.canScroll(v, checkV, dx, x, y);
    }
}
