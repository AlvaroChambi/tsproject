package es.developer.achambi.tsproject;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

import es.developer.achambi.coreframework.threading.Error;
import es.developer.achambi.coreframework.threading.MainExecutor;
import es.developer.achambi.coreframework.threading.Request;
import es.developer.achambi.coreframework.threading.Response;
import es.developer.achambi.coreframework.threading.ResponseHandler;
import es.developer.achambi.tsproject.databinding.ModelResultItemBinding;
import es.developer.achambi.coreframework.ui.BaseSearchListFragment;
import es.developer.achambi.coreframework.ui.SearchAdapterDecorator;
import es.developer.achambi.tsproject.model.data;

public class MainFragment extends BaseSearchListFragment implements View.OnClickListener,
        SearchAdapterDecorator.OnItemClickedListener<VehicleOverviewPresentation> {
    private Adapter adapter;
    private AppDatabase database;
    private MainExecutor executor;
    private ArrayList<data> vehicles;

    private EditText brandEditText;
    private EditText modelEditText;
    private EditText gdEditText;
    private EditText cvfEditText;
    private EditText periodEditText;
    private EditText ccEditText;
    private EditText cvEditText;
    private EditText pkwEditText;
    private EditText cylindersEditText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = AppDatabase.buildDatabase(getActivity());
        executor = MainExecutor.buildExecutor();
        adapter.setListener(this);
    }

    @Override
    public void onViewSetup(View view, @Nullable Bundle savedInstanceState) {
        super.onViewSetup(view, savedInstanceState);
        startLoading();
        executor.executeRequest(new Request<ArrayList<data>>() {
            @Override
            public Response<ArrayList<data>> perform() throws Exception {
                return new Response<>(new ArrayList<data>( database.getRowDao().queryAll()) );
            }
        }, new ResponseHandler<ArrayList<data>>() {
            @Override
            public void onSuccess(Response<ArrayList<data>> response) {
                hideLoading();
                vehicles = response.getData();
                ArrayList<VehicleOverview> vehicleOverviews = buildVehicles(
                        vehicles, periodEditText.getText().toString() );
                adapter.setData( VehicleOverviewPresentation.Builder
                        .build( getActivity(), vehicleOverviews ) );
                presentAdapterData();
            }

            @Override
            public void onError(Error error) {
                super.onError(error);
                showError(error);
            }
        });

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
        gdEditText = header.findViewById(R.id.gd_edit_text);
        cvfEditText = header.findViewById(R.id.cvf_edit_text);
        periodEditText = header.findViewById(R.id.period_edit_text);
        ccEditText = header.findViewById(R.id.cc_edit_text);
        cvEditText = header.findViewById(R.id.cv_edit_text);
        pkwEditText = header.findViewById(R.id.pkw_edit_text);
        cylindersEditText = header.findViewById(R.id.cylinders_edit_text);

        header.findViewById(R.id.header_search_button).setOnClickListener(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if( v.getId() == R.id.header_search_button ) {
            applyFilters();
        }
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

        executor.executeRequest(new Request<ArrayList<data>>() {
            @Override
            public Response<ArrayList<data>> perform() throws Exception {
                ArrayList<data> filtered = new ArrayList<>();
                String brand = brandEditText.getText().toString();
                String model = modelEditText.getText().toString();
                String gd = gdEditText.getText().toString();
                String cvf = cvfEditText.getText().toString();
                String period = periodEditText.getText().toString();
                String cc = ccEditText.getText().toString();
                String cylinders = cylindersEditText.getText().toString();
                String cv = cvEditText.getText().toString();
                String pKW = pkwEditText.getText().toString();

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
                return new Response<>( filtered );
            }
        }, new ResponseHandler<ArrayList<data>>() {
            @Override
            public void onSuccess(Response<ArrayList<data>> response) {
                hideLoading();
                ArrayList<VehicleOverview> vehicleOverviews = buildVehicles(
                        response.getData(), periodEditText.getText().toString() );
                adapter.setData( VehicleOverviewPresentation.Builder
                        .build( getActivity(), vehicleOverviews ) );
                presentAdapterData();
            }

            @Override
            public void onError(Error error) {
                super.onError(error);
                showError(error);
            }
        });
    }

    @Override
    public void onItemClicked(VehicleOverviewPresentation item) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        VehicleDetailsFragment detailsFragment =
                VehicleDetailsFragment.newInstance( item.vehicle );
        detailsFragment.show( transaction, "" );
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
