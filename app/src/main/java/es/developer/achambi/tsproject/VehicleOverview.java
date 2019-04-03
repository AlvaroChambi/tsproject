package es.developer.achambi.tsproject;

import java.util.Calendar;

import es.developer.achambi.tsproject.model.data;

public class VehicleOverview {
    public static int TAX_VALUE = 4;
    private static final float TAX_PERCENT_INCREASE = 0.004f;
    private data vehicle;
    private String year;
    private Float depreciationValue;
    private Float percentIncreaseValue;

    public data getVehicle() {
        return vehicle;
    }

    public void setVehicle(data vehicle) {
        this.vehicle = vehicle;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
        if( year == null || year.isEmpty() ) {
            return;
        }
        try{
            int elapsedTime = Calendar.getInstance().get( Calendar.YEAR ) - Integer.valueOf( year );
            Depreciation depreciation;
            if( elapsedTime >= Depreciation.values().length ) {
                depreciation = Depreciation.MORE_THAN_TWELVE_YEARS;
            } else {
                depreciation = Depreciation.values()[elapsedTime];
            }
            depreciationValue = Integer.valueOf( vehicle.valor )
                    * depreciation.getDepreciationPercent();
            percentIncreaseValue = depreciationValue * TAX_PERCENT_INCREASE;
        }catch ( NumberFormatException e ) {
            /* Only integer base values will be handled */
            /* Only integer year values make sense in this context */
            e.printStackTrace();
        }
    }

    public Float getDepreciationValue() {
        return depreciationValue;
    }

    public Float getPercentIncreaseValue() {
        return percentIncreaseValue;
    }
}
