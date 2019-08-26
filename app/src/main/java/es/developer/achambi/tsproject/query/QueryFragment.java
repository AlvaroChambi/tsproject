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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import es.developer.achambi.coreframework.threading.Error;
import es.developer.achambi.coreframework.ui.pagination.PaginatedPresentation;
import es.developer.achambi.coreframework.ui.pagination.PaginatedDecoratorAdapter;
import es.developer.achambi.coreframework.ui.pagination.PaginatedInterface;
import es.developer.achambi.coreframework.utils.WindowUtils;
import es.developer.achambi.tsproject.TSApplication;
import es.developer.achambi.tsproject.about.InfoActivity;
import es.developer.achambi.tsproject.R;
import es.developer.achambi.tsproject.details.VehicleDetailsFragment;
import es.developer.achambi.tsproject.views.SearchVehicleHeader;
import es.developer.achambi.tsproject.views.SearchVehicleHeaderListener;
import es.developer.achambi.tsproject.views.presentation.SearchVehicleHeaderPresentation;
import es.developer.achambi.tsproject.views.presentation.VehicleOverviewPresentation;
import es.developer.achambi.tsproject.databinding.ModelResultItemBinding;
import es.developer.achambi.coreframework.ui.BaseSearchListFragment;
import es.developer.achambi.coreframework.ui.SearchAdapterDecorator;

public class QueryFragment extends BaseSearchListFragment implements
        SearchVehicleHeaderListener,
        SearchAdapterDecorator.OnItemClickedListener<VehicleOverviewPresentation>,
        QueryScreenInterface, PaginatedInterface {
    private static final String LIST_SAVED_STATE = "LIST_SAVED_STATE";
    private static final String PAGE_SAVED_STATE = "PAGED_SAVED_STATE";
    private Adapter adapter;
    private PaginatedDecoratorAdapter pageAdapter;
    private SearchVehicleHeader searchVehicleHeader;

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
        return R.layout.search_header_layout;
    }

    @Override
    public void onHeaderSetup(View header) {
        searchVehicleHeader = header.findViewById(R.id.search_header);
        searchVehicleHeader.setListener(this);
    }

    @Override
    public void onDataSetup() {
        presenter.setupInitialData(searchVehicleHeader.getQuery());
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
        searchVehicleHeader.disableSearch();
    }

    @Override
    public void enableSearchButton() {
        searchVehicleHeader.enableSearch();
    }

    @Override
    public void displayVehicles(@NotNull ArrayList<VehicleOverviewPresentation> vehicles,
                                @NonNull ArrayList<PaginatedPresentation> paginatedExtra) {
        pageAdapter.setData(paginatedExtra);
        adapter.setData(vehicles);
        presentAdapterData();
    }

    @Override
    public void displayError(@NonNull Error error) {
        showError(error);
    }

    @Override
    public void displayNextPageError(@NotNull ArrayList<PaginatedPresentation> paginatedData) {
        pageAdapter.setData(paginatedData);
        presentAdapterData();
    }

    @Override
    public void requestNextPage(int nextPageIndex) {
        presenter.queryNextPage(searchVehicleHeader.getQuery(), nextPageIndex);
    }

    @Override
    public void displaySearchResultsCount(@NotNull SearchVehicleHeaderPresentation presentation) {
        searchVehicleHeader.display(presentation);
    }

    @Override
    public void onSearchButtonSelected() {
        presenter.performSearchSelected(searchVehicleHeader.getQuery());
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
