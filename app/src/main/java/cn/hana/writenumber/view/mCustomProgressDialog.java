package cn.hana.writenumber.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import cn.hana.writenumber.R;


public class mCustomProgressDialog extends ProgressDialog {
    private AnimationDrawable mAnimation;
    private Context mContext;
    private ImageView mImageView;
    private String mLoadingTip;
    private TextView mLoadingTv;
    private static int num;
    private static boolean flag;
    private Thread t;

    public mCustomProgressDialog(Context context,String content, int num) {
        super(context);
        this.mContext=context;
        this.mLoadingTip=content;
        this.num=num;
        setCanceledOnTouchOutside(true);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_dialog);
        mLoadingTv = (TextView) findViewById(R.id.loadingTv);
        mImageView = (ImageView) findViewById(R.id.loadingIv);
        flag = true;

        t = new Thread(new Runnable(){
            public void run(){
                int i = 1;
                while (flag)
                {
                    setBackground(i);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(i >= 24)
                        i = 1;
                    i++;
                }
                return;

            }
        });
        t.start();
        mLoadingTv.setText(mLoadingTip);
    }
    private void setBackground(int i)
    {
        String name = "on" + num + "_" + i;
        int imgid = mContext.getResources().getIdentifier(name, "drawable",
                "cn.hana.writenumber");
        mImageView.setBackgroundResource(imgid);
    }
    @Override
    public void dismiss() {
        flag = false;
        super.dismiss();
    }
}
