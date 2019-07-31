package es.developer.achambi.tsproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import es.developer.achambi.coreframework.ui.BaseFragment;

public class InfoFragment extends BaseFragment {
    public static InfoFragment newInstance() {
        return new InfoFragment();
    }
    private static final String PHONE = "688928286";

    @Override
    public int getLayoutResource() {
        return R.layout.about_fragment_layout;
    }

    @Override
    public void onViewSetup(View view, @Nullable Bundle savedInstanceState) {
        view.findViewById(R.id.phone_call_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(getString(R.string.phone_uri, PHONE)));
                startActivity(intent);
            }
        });
        Button phoneButton = view.findViewById(R.id.phone_call_button);
        phoneButton.setText(getString(R.string.gestoria_afines_phone_text, PHONE));
    }
}
