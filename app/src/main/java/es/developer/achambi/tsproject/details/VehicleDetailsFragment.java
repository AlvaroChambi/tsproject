package es.developer.achambi.tsproject.details;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import es.developer.achambi.tsproject.R;
import es.developer.achambi.tsproject.views.presentation.VehicleOverviewPresentation;
import es.developer.achambi.tsproject.databinding.VehicleDetailsFragmentLayoutBinding;

import es.developer.achambi.coreframework.ui.BaseDialogFragment;
import es.developer.achambi.tsproject.views.ValueDetailsView;

public class VehicleDetailsFragment extends BaseDialogFragment {
    private static final String VEHICLE_KEY = "VEHICLE_KEY";

    private VehicleOverviewPresentation presentation;
    private ValueDetailsView valueDetailsView;

    public static VehicleDetailsFragment newInstance(VehicleOverviewPresentation vehiclePresentation) {
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
        valueDetailsView = view.findViewById(R.id.value_details_view);
        binding.setItem( presentation.vehicle );
        valueDetailsView.displayDetails(presentation);
    }
}
