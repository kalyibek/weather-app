package com.example.weatherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WeatherAdapterRV extends RecyclerView.Adapter<WeatherAdapterRV.ViewHolder> {

    private Context context;
    private ArrayList<WeatherModelRV> weatherModelRVArrayList;

    public WeatherAdapterRV(Context context, ArrayList<WeatherModelRV> weatherModelRVArrayList) {
        this.context = context;
        this.weatherModelRVArrayList = weatherModelRVArrayList;
    }

    @NonNull
    @Override
    public WeatherAdapterRV.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(context)
                .inflate(R.layout.weather_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherAdapterRV.ViewHolder holder, int position) {
        WeatherModelRV model = weatherModelRVArrayList.get(position);
        holder.temperatureTV.setText(model.getTemperature().concat("°C"));
        holder.timeTV.setText(model.getTime());
        holder.humidityTV.setText(model.getHumidity().concat("%"));


        SimpleDateFormat input = new SimpleDateFormat("YYYY-MM-DD HH:MM");
        SimpleDateFormat simpleTime = new SimpleDateFormat("HH:MM aa");
        try {
            Date t = input.parse(model.getTime());
            holder.timeTV.setText(simpleTime.format(t));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Picasso.get().load("http:".concat(model.getIcon())).into(holder.conditionIV);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView humidityTV, temperatureTV, timeTV;
        private ImageView conditionIV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            humidityTV = itemView.findViewById(R.id.idTVHumidity);
            temperatureTV = itemView.findViewById(R.id.idTVTemperature);
            timeTV = itemView.findViewById(R.id.idTVTime);
            conditionIV = itemView.findViewById(R.id.idIVCondition);

        }
    }
}
