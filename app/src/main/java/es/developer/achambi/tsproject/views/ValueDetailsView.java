package es.developer.achambi.tsproject.views;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import es.developer.achambi.tsproject.R;
import es.developer.achambi.tsproject.views.presentation.VehicleOverviewPresentation;

public class ValueDetailsView extends ConstraintLayout {
    private TextView baseValue;
    private TextView depreciationText;
    private TextView depreciationValue;
    private TextView taxIncrease;

    public ValueDetailsView(Context context) {
        super(context);
        init(context);
    }

    public ValueDetailsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ValueDetailsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.value_quick_details_layout, this, true);
        baseValue = findViewById(R.id.values_details_base_value_text);
        depreciationText = findViewById(R.id.values_details_depreciation_year_text);
        depreciationValue = findViewById(R.id.values_details_depreciation_value_text);
        taxIncrease = findViewById(R.id.values_details_taxes_increase_value);
    }

    public void displayDetails(VehicleOverviewPresentation item) {
        baseValue.setText(item.getVehicle().getValue());
        depreciationText.setText( getResources()
                .getString( R.string.vehicle_value_details_depreciation, item.getYear()) );
        depreciationValue.setText(item.getDepreciationValue());
        taxIncrease.setText(item.getPercentIncreaseValue());
    }
}
