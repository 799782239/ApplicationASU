package tools;

import android.support.v4.app.Fragment;

public abstract class BaseFragment extends Fragment {
    public boolean isShow;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isShow = true;
            onVisible();
        } else {
            isShow = false;
            onUnVisible();
        }
    }

    public void onVisible() {
        lazy();
    }

    public void onUnVisible() {
    }

    public abstract void lazy();
}