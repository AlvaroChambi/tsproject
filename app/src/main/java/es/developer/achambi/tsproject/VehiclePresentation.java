package es.developer.achambi.tsproject;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import es.developer.achambi.coreframework.ui.presentation.SearchListData;
import es.developer.achambi.tsproject.model.data;

public class VehiclePresentation implements SearchListData, Parcelable {
    public final int id;
    public final String brand;
    public final String model;
    public final String value;
    public final String gd;
    public final String cv;
    public final String cvf;
    public final String pkW;
    public final String period;

    public VehiclePresentation(int id, String brand, String model, String value,
                               String gd, String cv, String cvf, String pkW,
                               String period) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.value = value;
        this.gd = gd;
        this.cv = cv;
        this.cvf = cvf;
        this.pkW = pkW;
        this.period = period;
    }

    protected VehiclePresentation(Parcel in) {
        id = in.readInt();
        brand = in.readString();
        model = in.readString();
        value = in.readString();
        gd = in.readString();
        cv = in.readString();
        cvf = in.readString();
        pkW = in.readString();
        period = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(brand);
        dest.writeString(model);
        dest.writeString(value);
        dest.writeString(gd);
        dest.writeString(cv);
        dest.writeString(cvf);
        dest.writeString(pkW);
        dest.writeString(period);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<VehiclePresentation> CREATOR = new Creator<VehiclePresentation>() {
        @Override
        public VehiclePresentation createFromParcel(Parcel in) {
            return new VehiclePresentation(in);
        }

        @Override
        public VehiclePresentation[] newArray(int size) {
            return new VehiclePresentation[size];
        }
    };

    @Override
    public int getViewType() {
        return 0;
    }

    @Override
    public int getId() {
        if( model != null ) {
            return model.hashCode();
        } else {
            return 0;
        }

    }

    static class Builder {
        public static VehiclePresentation build(data data) {
            int id = 0;
            if(data.modelo != null) {
                id = data.modelo.hashCode();
            }
            return new VehiclePresentation(
                    id,
                    data.marca,
                    data.modelo,
                    data.valor,
                    data.gD,
                    data.cv,
                    data.cvf,
                    data.potencia,
                    data.periodo
            );
        }

        public static ArrayList<VehiclePresentation> build(ArrayList<data> dataList) {
            ArrayList<VehiclePresentation> list = new ArrayList<>();
            for ( es.developer.achambi.tsproject.model.data data : dataList ) {
                list.add( build(data) );
            }
            return list;
        }
    }
}