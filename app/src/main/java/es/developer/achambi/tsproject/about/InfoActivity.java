package es.developer.achambi.tsproject.about;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import es.developer.achambi.coreframework.ui.BaseActivity;
import es.developer.achambi.coreframework.ui.BaseFragment;
import es.developer.achambi.tsproject.R;

public class InfoActivity extends BaseActivity {
    public static Intent getStartIntent(Context context) {
        return new Intent(context, InfoActivity.class);
    }

    @Override
    public int getScreenTitle() {
        return R.string.about_activity_title;
    }

    @Override
    public BaseFragment getFragment(Bundle args) {
        return InfoFragment.newInstance();
    }
}
