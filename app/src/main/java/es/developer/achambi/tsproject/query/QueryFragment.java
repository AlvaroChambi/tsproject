package es.developer.achambi.tsproject.query;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import es.developer.achambi.coreframework.threading.Error;
import es.developer.achambi.coreframework.ui.PagePresentation;
import es.developer.achambi.coreframework.ui.PaginatedDecoratorAdapter;
import es.developer.achambi.coreframework.ui.PaginatedInterface;
import es.developer.achambi.coreframework.utils.WindowUtils;
import es.developer.achambi.tsproject.TSApplication;
import es.developer.achambi.tsproject.about.InfoActivity;
import es.developer.achambi.tsproject.R;
import es.developer.achambi.tsproject.details.VehicleDetailsFragment;
import es.developer.achambi.tsproject.models.QueryParams;
import es.developer.achambi.tsproject.models.VehicleOverview;
import es.developer.achambi.tsproject.views.presentation.VehicleOverviewPresentation;
import es.developer.achambi.tsproject.databinding.ModelResultItemBinding;
import es.developer.achambi.coreframework.ui.BaseSearchListFragment;
import es.developer.achambi.coreframework.ui.SearchAdapterDecorator;

public class QueryFragment extends BaseSearchListFragment implements View.OnClickListener,
        SearchAdapterDecorator.OnItemClickedListener<VehicleOverviewPresentation>,
        QueryScreenInterface, PaginatedInterface {
    private static final String LIST_SAVED_STATE = "LIST_SAVED_STATE";
    private static final String PAGE_SAVED_STATE = "PAGED_SAVED_STATE";
    private Adapter adapter;
    private PaginatedDecoratorAdapter pageAdapter;

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

    private QueryPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = TSApplication.Companion.getQueryPresenterFactory()
                .createPresenter(this, getLifecycle());
    }

    @Override
    public void onViewSetup(View view) {
        super.onViewSetup(view);
        adapter.setListener(this);
    }

    @Override
    public SearchAdapterDecorator provideAdapter() {
        if(adapter == null) {
            pageAdapter = new PaginatedDecoratorAdapter(this);
            adapter = new Adapter(pageAdapter);
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
    public void onDataSetup() {
        presenter.setupInitialData(buildFilters());
    }

    @Override
    public void expandAdvancedSearch() {
        expanded = true;
        advancedSearchButton.setImageResource(R.drawable.baseline_expand_less_black_18dp);
        advancedSearchGroup.setVisibility(View.VISIBLE);
    }

    @Override
    public void collapseAdvancedSearch() {
        expanded = false;
        advancedSearchButton.setImageResource(R.drawable.baseline_expand_more_black_18dp);
        advancedSearchGroup.setVisibility(View.GONE);
    }

    @Override
    public void collapseKeyboard() {
        WindowUtils.hideSoftKeyboard( getActivity() );
    }

    @Override
    public void showLoading() {
        startLoading();
    }

    @Override
    public void stopLoading() {
        hideLoading();
    }

    @Override
    public void disableSearchButton() {
        advancedSearchButton.setEnabled(false);
    }

    @Override
    public void enableSearchButton() {
        advancedSearchButton.setEnabled(true);
    }

    @Override
    public void displayVehicles(@NotNull ArrayList<VehicleOverview> vehicles,
                                @NonNull ArrayList<PagePresentation> paginatedExtra) {
        pageAdapter.setData(paginatedExtra);
        adapter.setData( VehicleOverviewPresentation.Builder
                .build( getActivity(), vehicles ) );
        presentAdapterData();
    }

    @Override
    public void displayError(@NonNull Error error) {
        showError(error);
    }

    @Override
    public void displayNextPageError(@NotNull ArrayList<PagePresentation> paginatedData) {
        pageAdapter.setData(paginatedData);
        presentAdapterData();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.info_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_info) {
            startActivity(InfoActivity.getStartIntent(getActivity()));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if( v.getId() == R.id.header_search_button ) {
            presenter.performSearchSelected(buildFilters(), expanded);
        } else if( v.getId() == R.id.header_advanced_search_action_button ) {
            presenter.switchAdvancedSearchSelected(expanded);
        }
    }

    private QueryParams buildFilters() {
        QueryParams.Builder builder = new QueryParams.Builder();
        return builder.brand( brandEditText.getText().toString() )
                .model( modelEditText.getText().toString() )
                .period( periodEditText.getText().toString() )
                .gd( gdEditText.getText().toString() )
                .cvf( cvfEditText.getText().toString() )
                .cc( ccEditText.getText().toString() )
                .cylinders( cylindersText.getText().toString() )
                .cv( cvEditText.getText().toString() )
                .pkw( pkWEditText.getText().toString() )
                .build();
    }

    @Override
    public void onItemClicked(VehicleOverviewPresentation item) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        VehicleDetailsFragment detailsFragment =
                VehicleDetailsFragment.newInstance( item );
        detailsFragment.show( transaction, null );
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(LIST_SAVED_STATE, adapter.getData());
        outState.putParcelableArrayList(PAGE_SAVED_STATE, pageAdapter.getData());
    }

    @Override
    public void onRestoreInstanceState(@NotNull Bundle savedInstanceState) {
        adapter.setData( savedInstanceState.getParcelableArrayList(LIST_SAVED_STATE) );
        pageAdapter.setData( savedInstanceState.getParcelableArrayList(PAGE_SAVED_STATE) );
        presentAdapterData();
    }

    @Override
    public void requestNextPage(int nextPageIndex) {
        presenter.queryNextPage(buildFilters(), nextPageIndex);
    }


    class Adapter extends SearchAdapterDecorator<VehicleOverviewPresentation,ViewHolder> {
        public Adapter(SearchAdapterDecorator adapter) {
            super(adapter);
        }

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
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private ModelResultItemBinding binding;

        public ViewHolder(ModelResultItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
