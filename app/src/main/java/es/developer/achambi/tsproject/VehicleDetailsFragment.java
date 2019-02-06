package es.developer.achambi.tsproject;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import es.developer.achambi.tsproject.databinding.VehicleDetailsFragmentLayoutBinding;

import es.developer.achambi.coreframework.ui.BaseDialogFragment;

public class VehicleDetailsFragment extends BaseDialogFragment {
    private static final String VEHICLE_KEY = "VEHICLE_KEY";

    private VehiclePresentation presentation;

    public static VehicleDetailsFragment newInstance(VehiclePresentation vehiclePresentation) {
        Bundle args = new Bundle();
        args.putParcelable(VEHICLE_KEY, vehiclePresentation);
        VehicleDetailsFragment fragment = new VehicleDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presentation = getArguments().getParcelable(VEHICLE_KEY);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.vehicle_details_fragment_layout;
    }

    @Override
    public void onViewSetup(View view, @Nullable Bundle savedInstanceState) {
        VehicleDetailsFragmentLayoutBinding binding = DataBindingUtil.bind(view);
        binding.setItem( presentation );
    }
}
