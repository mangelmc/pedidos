package com.example.mike.pedidos;

import android.app.DatePickerDialog;
import android.app.Dialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;

public class SeekbarTestActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String DOS_PUNTOS = ":";
    private static final String BARRA = "/";
    private static final String CERO = "0";
    int current=1,precio = 25;;
    Dialog alertSeekbarDialog;
    TextView textSuma;
    NumberPicker numberPicker;
    Integer number;
    EditText editTextHora, editTextDia;
    ImageButton btnDia,btnHora;
    Button btnSeekbar;
    int mAnio, mMes, mDia, mHora, mMinuto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seekbar_test);

        //load comps
        textSuma = findViewById(R.id.textSuma);
        editTextHora = findViewById(R.id.editTextHora);
        editTextDia = findViewById(R.id.editTextDia);

        btnDia = findViewById(R.id.btnDia);
        btnHora = findViewById(R.id.btnHora);
        btnSeekbar = findViewById(R.id.btnSeekbar);

        btnDia.setOnClickListener(this);
        btnHora.setOnClickListener(this);
        btnDia.setOnClickListener(this);



        numberPicker = findViewById(R.id.numberPicker);
        numberPicker.setMaxValue(10);
        numberPicker.setMinValue(1);
        numberPicker.setValue(1);
        numberPicker.setOnValueChangedListener(onValueChangeListener);

        final Calendar c = Calendar.getInstance();
        mAnio = c.get(Calendar.YEAR);
        mMes = c.get(Calendar.MONTH);
        mDia = c.get(Calendar.DAY_OF_MONTH);
        mHora = c.get(Calendar.HOUR_OF_DAY);
        mMinuto = c.get(Calendar.MINUTE);

    }

    NumberPicker.OnValueChangeListener onValueChangeListener = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker numberPicker, int i, int i1) {
            number = numberPicker.getValue();
            Toast.makeText(SeekbarTestActivity.this,"numero : " + number , Toast.LENGTH_SHORT).show();
        }
    };
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSeekbar: showAlertSeekbar();break;
            case R.id.btnDia: showAlertDia();break;
            case R.id.btnHora: showAlertHora();break;
        }


    }

    private void showAlertHora() {
        TimePickerDialog recogerHora = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                //Formateo el hora obtenido: antepone el 0 si son menores de 10
                String horaFormateada =  (hourOfDay < 10|| hourOfDay > 12)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);

                //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                String minutoFormateado = (minute < 10)? String.valueOf(CERO + minute):String.valueOf(minute);
                //Obtengo el valor a.m. o p.m., dependiendo de la selección del usuario
                String AM_PM;
                if(hourOfDay < 12) {
                    AM_PM = "a.m.";
                } else {
                    AM_PM = "p.m.";
                }
                //Muestro la hora con el formato deseado
                //editTextHora.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);

                editTextHora.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + " " +AM_PM);
            }
            //Estos valores deben ir en ese orden
            //Al colocar en false se muestra en formato 12 horas y true en formato 24 horas
            //Pero el sistema devuelve la hora en formato 24 horas

        }, mHora, mMinuto, false);

        recogerHora.show();
    }

    private void showAlertDia() {

        DatePickerDialog fecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                final int mesActual = month + 1;
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
                //Muestro la fecha con el formato deseado
                //
                //etFecha.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);
                editTextDia.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);

            }
            //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
            /**
             *También puede cargar los valores que usted desee
             */
        },mAnio, mMes, mDia);
        //Muestro el widget

        fecha.show();

    }


    private void showAlertSeekbar() {
        //Toast.makeText(this,"anything",Toast.LENGTH_LONG).show();
        alertSeekbarDialog = new Dialog(this);
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View layout = inflater.inflate(R.layout.alert_seekbar_layout, (ViewGroup)findViewById(R.id.dialog_root_element));
        alertSeekbarDialog.setContentView(layout);
        final TextView result = layout.findViewById(R.id.textResult);
        Button dialogButton = layout.findViewById(R.id.btnDialog);
        SeekBar dialogSeekBar = (SeekBar)layout.findViewById(R.id.seekbarPrecio);


        dialogSeekBar.setMax(25);

        dialogSeekBar.setProgress(1);
        result.setText("Cantidad : " + current);


        dialogSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //add code here
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //add code here
            }

            @Override
            public void onProgressChanged(SeekBar seekBark, int progress, boolean fromUser) {
                //add code here
                current = progress;

                result.setText("Total : " +  precio+" * " + current + " = " + current * precio);

            }
        });
        alertSeekbarDialog.show();
    }


    public void seleccionar(View view) {

        if (current >0){
            textSuma.setText(" Precio : "+current * precio);
            alertSeekbarDialog.dismiss();
        }

    }


}
