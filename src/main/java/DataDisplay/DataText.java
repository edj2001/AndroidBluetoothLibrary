package DataDisplay;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.grandpaj.library2.R;
import com.grandpaj.library2.bluetooth.logger.Log;

import static android.R.color.black;

//import android.widget.TextView;

/*
TODO: 12/6/2016 Create a SimpleArrayMap of all these objects with the key as the variable name so that we can just match them
with incoming data.
*/

/**
 * Created by Linda on 11/23/2016.
 */

public class DataText extends AppCompatTextView {

    private CountDownTimer dataUpdateLostTimer;
    private DataRanges displayRanges;
    private int currentTextColor;
    private int currentBackgroundColor;

    public DataText(Context context) {

        super(context);
    }

    // This constructor is used by LayoutInflater
    public DataText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    public void setData(CharSequence inText){
        super.setText(inText);
        //if we receive data then restart the lost update timer.
        if (dataUpdateLostTimer != null) {
            dataUpdateLostTimer.cancel();
            dataUpdateLostTimer.start();
        }
    }

    private void init(Context context, AttributeSet attrs) {
        displayRanges = new DataRanges();
        currentTextColor = getCurrentTextColor();
        currentBackgroundColor = black;
        Drawable mDrawable = getBackground();
        ColorDrawable colorDrawable = (ColorDrawable) mDrawable;
        if (colorDrawable != null) {
            currentBackgroundColor = colorDrawable.getColor();
        }
        //currentBackgroundColor = setBackgroundColor(R.color.colorAccent);
        //setBackgroundColor(black);
        //setBackgroundColor(R.color.colorAccent);

        //check Attribute Set
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.DataText,
                    0, 0);

            String varName = "";
            String customFontName = "";
            int unitsFieldResourceId;
            int unitsField2ResourceId;
            int dataTimeoutMillis = -1;
            String[] AlarmStrings=new String[5];

            try {
                //check for custom Font entry
                customFontName = a.getString(R.styleable.DataText_customFont);

                //check for transmitted variable name entry
                varName = a.getString(R.styleable.DataText_varName);

                //check for a units Field Reference
                unitsFieldResourceId = a.getResourceId(R.styleable.DataText_unitFieldReference, 0);
                unitsField2ResourceId = a.getResourceId(R.styleable.DataText_unitField2Reference, 0);

                //check for alarm limits
                AlarmStrings[1] = a.getString(R.styleable.DataText_Alarm1);
                AlarmStrings[2] = a.getString(R.styleable.DataText_Alarm2);
                AlarmStrings[3] = a.getString(R.styleable.DataText_Alarm3);
                AlarmStrings[4] = a.getString(R.styleable.DataText_Alarm4);

                //check for timeout value
                dataTimeoutMillis = a.getInteger(R.styleable.DataText_dataTimeout, -1);

            } catch (Exception e) {
                Log.e("DataText ", "There was an error loading attributes.");
            } finally {
                a.recycle();
            }

            //apply the custom font if one was specified
            if (customFontName != null){
                Typeface face= Typeface.createFromAsset(context.getAssets(), "fonts/"+customFontName);
                this.setTypeface(face, Typeface.BOLD);

            }

            //apply the transmitted var name if one was specified
            if (varName != null){

            }

            //set up ranges if any specified
            displayRanges.setRanges(AlarmStrings);

            //set up data timeout if a value was specified
            //the timer will start when data is first received
            if (dataTimeoutMillis > 0) {
                dataUpdateLostTimer = new CountDownTimer(dataTimeoutMillis, dataTimeoutMillis) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        setText(R.string.dataUpdateTimeout);
                        setAlarmState();
                    }
                };
            }

        }
    }

    private void setAlarmState(){
        //set the TextView to show an alarm state.
    }

}