package es.developer.achambi.tsproject;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import es.developer.achambi.coreframework.threading.Error;
import es.developer.achambi.coreframework.threading.MainExecutor;
import es.developer.achambi.coreframework.threading.Request;
import es.developer.achambi.coreframework.threading.Response;
import es.developer.achambi.coreframework.threading.ResponseHandler;
import es.developer.achambi.coreframework.ui.QuickDetailPopup;
import es.developer.achambi.coreframework.utils.WindowUtils;
import es.developer.achambi.tsproject.databinding.ModelResultItemBinding;
import es.developer.achambi.coreframework.ui.BaseSearchListFragment;
import es.developer.achambi.coreframework.ui.SearchAdapterDecorator;
import es.developer.achambi.tsproject.model.data;

import static android.support.v7.widget.RecyclerView.NO_POSITION;

public class MainFragment extends BaseSearchListFragment implements View.OnClickListener{
    private Adapter adapter;
    private AppDatabase database;
    private MainExecutor executor;
    private ArrayList<data> vehicles;

    private EditText brandEditText;
    private EditText modelEditText;
    private EditText periodEditText;
    private EditText gdEditText;
    private EditText cvfEditText;
    private EditText cvEditText;
    private EditText pkWEditText;
    private EditText cylindersText;
    private EditText ccEditText;

    private ImageView advancedSearchButton;
    private View advancedSearchGroup;
    private boolean expanded;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = AppDatabase.buildDatabase(getActivity());
        executor = MainExecutor.buildExecutor();
    }

    @Override
    public void onViewSetup(View view, @Nullable Bundle savedInstanceState) {
        super.onViewSetup(view, savedInstanceState);
    }

    private ArrayList<VehicleOverview> buildVehicles( ArrayList<data> dataList, String year ) {
        ArrayList<VehicleOverview> vehicleOverviews = new ArrayList<>();
        for( data item : dataList ) {
            VehicleOverview vehicle = new VehicleOverview();
            vehicle.setVehicle( item );
            vehicle.setYear( year );
            vehicleOverviews.add( vehicle );
        }
        return vehicleOverviews;
    }

    @Override
    public SearchAdapterDecorator provideAdapter() {
        if(adapter == null) {
            adapter = new Adapter();
        }
        return adapter;
    }

    @Override
    public int getHeaderLayoutResource() {
        return R.layout.filter_header;
    }

    @Override
    public void onHeaderSetup(View header) {
        brandEditText = header.findViewById(R.id.brand_edit_text);
        modelEditText = header.findViewById(R.id.model_edit_text);
        periodEditText = header.findViewById(R.id.period_edit_text);
        gdEditText = header.findViewById(R.id.gd_input_text);
        cvEditText = header.findViewById(R.id.cv_input_text);
        cvfEditText = header.findViewById(R.id.cvf_input_text);
        pkWEditText = header.findViewById(R.id.pkw_input_text);
        cylindersText = header.findViewById(R.id.cylinders_input_text);
        ccEditText = header.findViewById(R.id.cc_input_text);

        advancedSearchButton = header.findViewById(R.id.header_advanced_search_action_button);
        advancedSearchGroup = header.findViewById(R.id.advanced_search_group);
        brandEditText.requestFocus();
        header.findViewById(R.id.header_search_button).setOnClickListener(this);
        advancedSearchButton.setOnClickListener(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if( v.getId() == R.id.header_search_button ) {
            if(expanded) {
                switchAdvancedSearchOptions();
            }
            WindowUtils.hideSoftKeyboard( getActivity() );
            applyFilters();
        } else if( v.getId() == R.id.header_advanced_search_action_button ) {
            switchAdvancedSearchOptions();
        }
    }

    private void switchAdvancedSearchOptions() {
        if( expanded ) {
            advancedSearchButton.setImageResource(R.drawable.baseline_expand_more_black_18dp);
            advancedSearchGroup.setVisibility(View.GONE);
        } else {
            advancedSearchButton.setImageResource(R.drawable.baseline_expand_less_black_18dp);
            advancedSearchGroup.setVisibility(View.VISIBLE);
        }
        expanded = !expanded;
    }

    private boolean isInPeriod(String year, String period) {
        if(year.isEmpty()) {
            return true;
        }
        String firstYear = period.substring(0, period.lastIndexOf("-"));
        String secondYear = period.substring(period.lastIndexOf("-") + 1, period.length());
        if(secondYear.isEmpty()) {
            return year.equals( firstYear );
        } else {
            int first = Integer.valueOf( firstYear );
            int second = Integer.valueOf( secondYear );
            int yearToFind = Integer.valueOf( year );
            return first <= yearToFind && yearToFind <= second;
        }
    }

    private void applyFilters() {
        startLoading();

        executor.executeRequest(new Request<ArrayList<VehicleOverviewPresentation>>() {
            @Override
            public Response<ArrayList<VehicleOverviewPresentation>> perform() throws Exception {
                vehicles = new ArrayList<>( database.getRowDao().queryAll() );
                ArrayList<data> filtered = new ArrayList<>();
                String brand = brandEditText.getText().toString();
                String model = modelEditText.getText().toString();
                String period = periodEditText.getText().toString();
                String gd = gdEditText.getText().toString();
                String cvf = cvfEditText.getText().toString();
                String cc = ccEditText.getText().toString();
                String cylinders = cylindersText.getText().toString();
                String cv = cvEditText.getText().toString();
                String pKW = pkWEditText.getText().toString();

                for( data vehicle : vehicles ) {
                    if( vehicle.marca.toLowerCase().
                            contains( brand.toLowerCase() ) &&
                            vehicle.modelo.toLowerCase().
                                    contains( model.toLowerCase() ) &&
                            vehicle.gD.toLowerCase().
                                    contains( gd.toLowerCase() ) &&
                            vehicle.cvf.toLowerCase().
                                    contains( cvf.toLowerCase() ) &&
                            vehicle.cc.toLowerCase().
                                    contains( cc.toLowerCase() ) &&
                            isInPeriod( period, vehicle.periodo ) &&
                            vehicle.cilindros.toLowerCase()
                                    .contains( cylinders.toLowerCase() ) &&
                            vehicle.cv.toLowerCase()
                                    .contains( cv.toLowerCase() ) &&
                            vehicle.potencia.toLowerCase()
                                    .contains( pKW.toLowerCase() ) ) {
                        filtered.add( vehicle );
                    }
                }
                ArrayList<VehicleOverview> vehicles = buildVehicles(
                        filtered, periodEditText.getText().toString() );
                return new Response<>( VehicleOverviewPresentation.Builder
                        .build( getActivity(), vehicles ) );
            }
        }, new ResponseHandler<ArrayList<VehicleOverviewPresentation>>() {
            @Override
            public void onSuccess(Response<ArrayList<VehicleOverviewPresentation>> response) {
                hideLoading();
                adapter.setData( response.getData() );
                presentAdapterData();
            }

            @Override
            public void onError(Error error) {
                super.onError(error);
                showError(error);
            }
        });
    }

    private void onVehicleItemClicked(VehicleOverviewPresentation item) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        VehicleDetailsFragment detailsFragment =
                VehicleDetailsFragment.newInstance( item.vehicle );
        detailsFragment.show( transaction, "" );
    }

    private void onVehicleValueClicked(VehicleOverviewPresentation item, View view) {
        @SuppressLint("InflateParams") View detailsView =  LayoutInflater.from(getActivity())
                .inflate( R.layout.value_quick_details_layout, null );
        TextView baseValue = detailsView.findViewById(R.id.values_details_base_value_text);
        TextView depreciationText = detailsView.findViewById(R.id.values_details_depreciation_year_text);
        TextView depreciationValue = detailsView.findViewById(R.id.values_details_depreciation_value_text);
        TextView taxIncrease = detailsView.findViewById(R.id.values_details_taxes_increase_value);
        baseValue.setText( item.vehicle.value );
        depreciationText.setText( getResources()
                .getString( R.string.vehicle_value_details_depreciation, item.year ) );
        depreciationValue.setText( item.depreciationValue );
        taxIncrease.setText( item.percentIncreaseValue );
        QuickDetailPopup.displayDetails( detailsView, view );
    }

    class Adapter extends SearchAdapterDecorator<VehicleOverviewPresentation,ViewHolder> {

        @Override
        public int getLayoutResource() {
            return R.layout.model_result_item;
        }

        @Override
        public ViewHolder createViewHolder(View rootView) {
            ModelResultItemBinding binding = DataBindingUtil.bind(rootView);
            return new ViewHolder(binding);
        }

        @Override
        protected void registerListeners(final ViewHolder viewHolder,
                                         final SortedList<VehicleOverviewPresentation> data) {
            viewHolder.itemView.findViewById(R.id.view).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = viewHolder.getAdapterPosition();
                    if( position != NO_POSITION ) {
                        onVehicleValueClicked( data.get( position ), v );
                    }
                }
            });
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = viewHolder.getAdapterPosition();
                    if( position != NO_POSITION ) {
                        onVehicleItemClicked( data.get( position ) );
                    }
                }
            });
        }

        @Override
        public void bindViewHolder(ViewHolder holder, VehicleOverviewPresentation item) {
            holder.binding.setItem( item );
        }

        @Override
        public int getAdapterViewType() {
            return 0;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ModelResultItemBinding binding;

        public ViewHolder(ModelResultItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
