package cn.hana.writenumber;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import cn.hana.writenumber.view.DrawView;
import cn.hana.writenumber.view.DrawViewPack.DrawModel;
import cn.hana.writenumber.view.mCustomProgressDialog;
import jp.narr.tensorflowmnist.DigitDetector;

public class NumberActivity extends Activity {
    private DrawView mDrawView;
    private DrawModel mModel;
    private static final String TAG = "MainActivity";

    private float mLastX;
    private float mLastY;
    private int num;

    private static DigitDetector mDetector = new DigitDetector();

    public mCustomProgressDialog mdialog;
    private TextView mResultText;

    private PointF mTmpPiont = new PointF();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_number);


        Intent it = getIntent();
        num = it.getIntExtra("TAG",0);



        boolean ret = mDetector.setup(this);
        if( !ret ) {
            Log.i(TAG, "Detector setup failed");
            return;
        }

        mModel = new DrawModel(28, 28);
        mDrawView = (DrawView) findViewById(R.id.view_draw);
        mDrawView.setModel(mModel);


        mDrawView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction() & MotionEvent.ACTION_MASK;

                if (action == MotionEvent.ACTION_DOWN) {
                    processTouchDown(event);
                    return true;

                } else if (action == MotionEvent.ACTION_MOVE) {
                    processTouchMove(event);
                    return true;

                } else if (action == MotionEvent.ACTION_UP) {
                    processTouchUp();
                    onDetectClicked();
                    return true;
                }
                return false;
            }
        });

        mResultText = findViewById(R.id.text_result);
    }

    @Override
    protected void onResume() {
        mDrawView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mDrawView.onPause();
        super.onPause();
    }


    private void processTouchDown(MotionEvent event) {
        mLastX = event.getX();
        mLastY = event.getY();
        mDrawView.calcPos(mLastX, mLastY, mTmpPiont);
        float lastConvX = mTmpPiont.x;
        float lastConvY = mTmpPiont.y;
        mModel.startLine(lastConvX, lastConvY);
    }

    private void processTouchMove(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        mDrawView.calcPos(x, y, mTmpPiont);
        float newConvX = mTmpPiont.x;
        float newConvY = mTmpPiont.y;
        mModel.addLineElem(newConvX, newConvY);

        mLastX = x;
        mLastY = y;
        mDrawView.invalidate();
    }

    private void processTouchUp() {
        mModel.endLine();
    }

    private void onDetectClicked() {
        int pixels[] = mDrawView.getPixelData();
        int digit = mDetector.detectDigit(pixels);

        Log.i(TAG, "digit =" + digit);

        if(num == digit)
            dialog();

        mResultText.setText("Detected = " + digit);
    }

    public void onClearClicked() {
        mModel.clear();
        mDrawView.reset();
        mDrawView.invalidate();
        mResultText.setText("");
    }
    public void onClearClicked(View v) {
        onClearClicked();
    }

    public void OnYS(View v) {
        if (mdialog == null) {
            mdialog = new mCustomProgressDialog(this, "演示中点击边缘取消", num);
        }
        mdialog.show();
    }

    protected void dialog() {
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setMessage("太棒了！书写完成！");
        builder.setTitle("提示");
        builder.setPositiveButton("完成",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.setNegativeButton("再来一次", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                onClearClicked();

            }
        });
        builder.create().show();
    }


}
