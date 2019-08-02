package es.developer.achambi.tsproject.query;

import android.os.Bundle;

import es.developer.achambi.coreframework.ui.BaseActivity;
import es.developer.achambi.coreframework.ui.BaseFragment;
import es.developer.achambi.tsproject.R;

public class MainActivity extends BaseActivity {
    @Override
    public int getScreenTitle() {
        return R.string.activity_title;
    }

    @Override
    public BaseFragment getFragment(Bundle args) {
        return new MainFragment();
    }
}
