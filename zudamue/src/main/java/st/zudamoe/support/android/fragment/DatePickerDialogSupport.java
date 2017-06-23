package st.zudamoe.support.android.fragment;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
/**
 *
 * Created by dchost on 07/03/17.
 */

public class DatePickerDialogSupport extends DatePickerDialog {


    private OnCheckSelection onCheckSelection;

    public static DatePickerDialogSupport newInstance(OnDateSetListener callBack, int year,
                                               int monthOfYear,
                                               int dayOfMonth) {
        DatePickerDialogSupport ret = new DatePickerDialogSupport();
        ret.initialize(callBack, year, monthOfYear, dayOfMonth);
        return ret;
    }

    @Override
    public boolean isOutOfRange(int year, int month, int day) {
        if( this.onCheckSelection == null ) return  super.isOutOfRange( year, month, day );
        return onCheckSelection.isSelectable( year, month, day );
    }

    public void setOnCheckSelection(OnCheckSelection onCheckSelection) {
        this.onCheckSelection = onCheckSelection;
    }


    public interface OnCheckSelection {
        public boolean isSelectable( int year, int month, int day );
    }

}
