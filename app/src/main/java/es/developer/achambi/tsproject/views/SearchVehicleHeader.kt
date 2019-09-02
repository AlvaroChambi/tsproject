package es.developer.achambi.tsproject.views

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import androidx.constraintlayout.widget.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import es.developer.achambi.tsproject.R
import es.developer.achambi.tsproject.models.QueryParams
import es.developer.achambi.tsproject.views.presentation.SearchVehicleHeaderPresentation
import kotlinx.android.synthetic.main.filter_header_view_layout.view.*

class SearchVehicleHeader @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null,
        defStyleAttr: Int = 0)
    : ConstraintLayout(context, attrs, defStyleAttr) {
    private var expanded = false
    private lateinit var listener: SearchVehicleHeaderListener

    init {
        init(context)
    }

    fun display( presentation: SearchVehicleHeaderPresentation ) {
        results_count_text.text = presentation.countDescription
    }

    fun getQuery() : QueryParams {
        val builder = QueryParams.Builder()
        return builder
                .brand( brand_edit_text.text.toString() )
                .model( model_edit_text.text.toString() )
                .period( period_edit_text.text.toString() )
                .gd( gd_input_text.text.toString() )
                .cv( cv_input_text.text.toString() )
                .cvf( cvf_input_text.text.toString() )
                .pkw( pkw_input_text.text.toString() )
                .cylinders( cylinders_input_text.text.toString() )
                .cc( cc_input_text.text.toString() )
                .build()
    }

    fun setListener( listener: SearchVehicleHeaderListener ) {
        this.listener = listener
    }

    fun enableSearch() {
        header_search_button.isEnabled = true
    }

    fun disableSearch() {
        header_search_button.isEnabled = false
    }

    private fun init(context: Context) {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.filter_header_view_layout, this, true)
        header_search_button.setOnClickListener {
            if(expanded) {
                switchAdvancedSearchSelected()
            }
            listener.onSearchButtonSelected()
        }
        header_advanced_search_action_button.setOnClickListener {
            switchAdvancedSearchSelected()
        }
    }

    private fun switchAdvancedSearchSelected() {
        expanded = !expanded
        applyExpandedState()
    }

    private fun applyExpandedState() {
        if(expanded) {
            header_advanced_search_action_button.setImageResource(
                    R.drawable.baseline_expand_less_black_18dp)
            advanced_search_group.visibility = View.VISIBLE
        } else {
            header_advanced_search_action_button.setImageResource(
                    R.drawable.baseline_expand_more_black_18dp)
            advanced_search_group.visibility = View.GONE
        }
    }

    public override fun onSaveInstanceState(): Parcelable? {
        val savedState = SavedState(super.onSaveInstanceState())
        savedState.isExpanded = expanded
        return savedState
    }

    public override fun onRestoreInstanceState(state: Parcelable) {
        if (state is SavedState) {
            super.onRestoreInstanceState(state.superState)
            expanded = state.isExpanded
            applyExpandedState()
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    internal class SavedState : BaseSavedState {

        var isExpanded: Boolean = false

        constructor(source: Parcel) : super(source) {
            isExpanded = source.readByte().toInt() != 0
        }

        constructor(superState: Parcelable) : super(superState)

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeByte((if (isExpanded) 1 else 0).toByte())
        }

        companion object {
            @JvmField
            val CREATOR = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(source: Parcel): SavedState {
                    return SavedState(source)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }
}

interface SearchVehicleHeaderListener {
    fun onSearchButtonSelected()
}