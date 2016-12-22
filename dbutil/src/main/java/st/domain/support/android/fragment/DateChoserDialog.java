package st.domain.support.android.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import st.domain.support.android.R;
import st.domain.support.android.model.Date;


/**
 * Created by xdata on 7/20/16.
 */
public class DateChoserDialog extends DialogFragment implements NumberPicker.OnValueChangeListener {
    private final String title;
    private View content;
    private NumberPicker pikerDay;
    private NumberPicker pikerMonth;
    private NumberPicker pikerYear;
    private Date date;
    private  OnDateChose onDateChose;

    public DateChoserDialog(String title, Context context, Date date)
    {
        this.title = title;
        this.date = date;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builderContent(builder, inflater);

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                ok();
            }
        });

        builder.setNegativeButton(R.string.cansel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                cansel();
            }
        });

        this.setCancelable(false);
        builder.setTitle(this.title);
        Dialog dialog = builder.create();
        return  dialog;
    }

    /**
     * Contuir o corpo do date chaser
     * @param builder
     * @param inflater
     */
    private void builderContent(AlertDialog.Builder builder, LayoutInflater inflater)
    {
        this.content = inflater.inflate(R.layout.lib_fragment_date, null);
        this.pikerDay = (NumberPicker) this.content.findViewById(R.id.pikerDay);
        this.pikerMonth = (NumberPicker) this.content.findViewById(R.id.pikerMonth);
        this.pikerYear = (NumberPicker) this.content.findViewById(R.id.pikerYear);

        builder.setView(this.content);

        this.pikerDay.setMinValue(1);
        this.pikerDay.setMaxValue(31);
        this.pikerMonth.setMinValue(1);
        this.pikerMonth.setMaxValue(12);
        this.pikerYear.setMinValue(2016);
        this.pikerYear.setMaxValue(2018);

        this.pikerDay.setOnClickListener(null);

        String [] months =  getContext().getResources().getStringArray(R.array.listMonth);
        this.pikerMonth.setDisplayedValues(months);

        this.pikerDay.setOnValueChangedListener(this);
        this.pikerMonth.setOnValueChangedListener(this);
        this.pikerYear.setOnValueChangedListener(this);

        if(date == null)
            this.date = new Date();
        this.dateier();
    }

    public void setDate(Date date)
    {
        this.date = date;
        this.dateier();
    }


    public void setOnDateChose(OnDateChose onDateChose) {
        this.onDateChose = onDateChose;
    }

    private void dateier()
    {
        int day = this.date.getDate();
        int month = this.date.getMonth();
        int year = this.date.getYear();
        this.pikerDay.setValue(day);
        this.pikerMonth.setValue(month);
        this.pikerYear.setValue(year);
    }

    private void ok()
    {
        if(this.onDateChose != null)
            this.onDateChose.chose(this.getDate());
    }

    private void cansel()
    {

    }

    public Date getDate()
    {
        return date;
    }

    @Override
    public void onValueChange(NumberPicker numberPicker, int i, int i1)
    {
        int dia = this.pikerDay.getValue();
        int mes = this.pikerMonth.getValue();
        int ano = this.pikerYear.getValue();
        this.date = new Date(ano, mes, dia);
        if(!this.date.isValid())
            this.date = null;


        mes = this.pikerMonth.getValue();
        Date.Month month = Date.Month.find(mes);
        this.pikerDay.setMaxValue(month.getMaxDay());

        //Para o meses de fiveriro em que so tem 28 dias
        if(month == Date.Month.FEV &&  date.getTypeYear() == Date.TypeYear.YEAR28_COMUN)
            this.pikerDay.setMaxValue(28);

        Log.i("APP", "TYPE YEAR = "+ this.date.getTypeYear().name());
    }


    public static interface  OnDateChose
    {
        public void chose(Date date);
    }
}
