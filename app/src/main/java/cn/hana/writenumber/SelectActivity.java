package cn.hana.writenumber;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class SelectActivity extends Activity {
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_select);

        SelectListener listener = new SelectListener();

        int view_id = 0;
        try {
            for (short i=0 ;i<10;i++)
            {
                view_id = R.id.class.getField("num_" + i).getInt(null);
                View view = findViewById(view_id);
                view.setTag(i);
                view.setOnClickListener(listener);
            }
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }


    }

    public static Context getContext() {
        return context;
    }

    private void StartActivity(Class<?> cls,int tag)
    {
        Intent intent = new Intent(this,cls);
        intent.putExtra("TAG",tag);
        startActivity(intent);
    }

    public class SelectListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            short tag =  (short)v.getTag();
            //System.out.println( "本类监听 TAG:" + tag);
            StartActivity(NumberActivity.class, tag);
        }
    }


}
