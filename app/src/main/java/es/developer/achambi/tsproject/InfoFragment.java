package es.developer.achambi.tsproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import es.developer.achambi.coreframework.ui.BaseFragment;

public class InfoFragment extends BaseFragment {
    public static InfoFragment newInstance() {
        return new InfoFragment();
    }

    @Override
    public int getLayoutResource() {
        return R.layout.about_fragment_layout;
    }

    @Override
    public void onViewSetup(View view, @Nullable Bundle savedInstanceState) {

    }
}
