package ro.cupsa.darius.mqttdemo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;

import ro.cupsa.darius.mqttdemo.helpers.MqttHelper;

/**
 * Created by cupsadarius on 18.08.2017.
 */

public class ChanelAdapter extends ArrayAdapter<Chanel> implements View.OnClickListener {

    /**
     * Log class activity
     */
    private static final String LOGGER = AddServer.class.getSimpleName();

    private ArrayList<Chanel> chanels;
    Context context;
    private int lastPosition = -1;
    MqttHelper mqttHelper;

    @Override
    public void onClick(View view) {

    }

    private static class ViewHolder {
        TextView textName;
        Button on;
        Button off;
        Button tap;
    }

    public ChanelAdapter(ArrayList<Chanel> data, Context context, MqttHelper mqttHelper) {
        super(context, R.layout.row_item, data);
        this.chanels = data;
        this.context = context;
        this.mqttHelper = mqttHelper;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Chanel chanel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.textName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.on = (Button) convertView.findViewById(R.id.on);
            viewHolder.off = (Button) convertView.findViewById(R.id.off);
            viewHolder.tap = (Button) convertView.findViewById(R.id.tap);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.up_from_down : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.textName.setText(chanel.getName());
        viewHolder.on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOGGER, "Clicked on " + chanel.getName());
                try {
                    mqttHelper.mqttAndroidClient.publish(chanel.getName(), new MqttMessage("30".getBytes()));
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        });
        viewHolder.off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOGGER, "Clicked off " + chanel.getName());
                try {
                    mqttHelper.mqttAndroidClient.publish(chanel.getName(), new MqttMessage("140".getBytes()));
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        });
        viewHolder.tap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOGGER, "Clicked tap " + chanel.getName());
                try {
                    mqttHelper.mqttAndroidClient.publish(chanel.getName(), new MqttMessage("30".getBytes()));
                    for (int i  = 0; i < 500000000; i ++) {
                        i++;
                        i--;
                    }
                    mqttHelper.mqttAndroidClient.publish(chanel.getName(), new MqttMessage("140".getBytes()));

                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        });
        return convertView;
    }
}
