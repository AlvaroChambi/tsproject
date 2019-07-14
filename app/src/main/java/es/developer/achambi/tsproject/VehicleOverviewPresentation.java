package es.developer.achambi.tsproject;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;


import java.math.BigDecimal;
import java.util.ArrayList;

import es.developer.achambi.coreframework.ui.presentation.SearchListData;

public class VehicleOverviewPresentation implements SearchListData, Parcelable {
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

    protected VehicleOverviewPresentation(Parcel in) {
        vehicle = in.readParcelable(VehiclePresentation.class.getClassLoader());
        percent = in.readString();
        year = in.readString();
        depreciationValue = in.readString();
        percentIncreaseValue = in.readString();
    }

    public static final Creator<VehicleOverviewPresentation> CREATOR = new Creator<VehicleOverviewPresentation>() {
        @Override
        public VehicleOverviewPresentation createFromParcel(Parcel in) {
            return new VehicleOverviewPresentation(in);
        }

        @Override
        public VehicleOverviewPresentation[] newArray(int size) {
            return new VehicleOverviewPresentation[size];
        }
    };

    @Override
    public int getViewType() {
        return 0;
    }

    @Override
    public long getId() {
        return vehicle.getId();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(vehicle, flags);
        dest.writeString(percent);
        dest.writeString(year);
        dest.writeString(depreciationValue);
        dest.writeString(percentIncreaseValue);
    }

    public static class Builder {
        public static VehicleOverviewPresentation build(Context context,
                                                        VehicleOverview vehicleOverview ) {
            return new VehicleOverviewPresentation(
                    VehiclePresentation.Builder.build( context, vehicleOverview.getVehicle() ),
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
                        String.valueOf( round( value ) ) );
            } else {
                return context.getResources().getString( R.string.empty_value );
            }
        }

        private static float round(float d) {
            BigDecimal bd = new BigDecimal(Float.toString(d));
            bd = bd.setScale(VALUE_DECIMAL_PLACE, BigDecimal.ROUND_HALF_UP);
            return bd.floatValue();
        }
    }
}
