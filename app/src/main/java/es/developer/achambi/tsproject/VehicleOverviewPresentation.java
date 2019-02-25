package es.developer.achambi.tsproject;

import android.content.Context;


import java.math.BigDecimal;
import java.util.ArrayList;

import es.developer.achambi.coreframework.ui.presentation.SearchListData;

public class VehicleOverviewPresentation implements SearchListData {
    private static final int VALUE_DECIMAL_PLACE = 2;
    public final VehiclePresentation vehicle;
    public final String percent;
    public final String year;
    public final String depreciationValue;
    public final String percentIncreaseValue;

    public VehicleOverviewPresentation(VehiclePresentation vehicle, String percent,
                                       String year, String depreciationValue,
                                       String percentIncreaseValue) {
        this.vehicle = vehicle;
        this.percent = percent;
        this.year = year;
        this.depreciationValue = depreciationValue;
        this.percentIncreaseValue = percentIncreaseValue;
    }

    @Override
    public int getViewType() {
        return 0;
    }

    @Override
    public int getId() {
        return vehicle.getId();
    }

    public static class Builder {
        public static VehicleOverviewPresentation build(Context context,
                                                        VehicleOverview vehicleOverview ) {
            return new VehicleOverviewPresentation(
                    VehiclePresentation.Builder.build( vehicleOverview.getVehicle() ),
                    buildPercentString( context, VehicleOverview.TAX_VALUE ),
                    buildYearString( context, vehicleOverview.getYear() ),
                    buildValue( context, vehicleOverview.getDepreciationValue() ),
                    buildValue( context, vehicleOverview.getPercentIncreaseValue() ) );
        }

        public static ArrayList<VehicleOverviewPresentation> build(
                Context context, ArrayList<VehicleOverview> vehicleOverviews ) {
            ArrayList<VehicleOverviewPresentation> presentations = new ArrayList<>();
            for( VehicleOverview overview : vehicleOverviews ) {
                presentations.add( build( context, overview ) );
            }
            return presentations;
        }

        private static String buildPercentString( Context context, int percent ) {
            return percent + context.getResources().getString( R.string.vehicle_percent_label );
        }

        private static String buildYearString( Context context, String year ) {
            if( year == null || year.isEmpty() ) {
                return context.getResources().getString(R.string.empty_value);
            } else {
                return year;
            }
        }

        private static String buildValue( Context context, Float value ) {
            if( value != null ) {
                return context.getResources().getString( R.string.vehicle_value_label,
                        String.valueOf( round( value, VALUE_DECIMAL_PLACE ) ) );
            } else {
                return context.getResources().getString( R.string.empty_value );
            }
        }

        private static float round(float d, int decimalPlace) {
            BigDecimal bd = new BigDecimal(Float.toString(d));
            bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
            return bd.floatValue();
        }
    }
}
