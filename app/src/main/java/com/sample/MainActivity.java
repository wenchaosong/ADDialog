package com.sample;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import com.ad.AdConstant;
import com.ad.AdManager;
import com.sample.R;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private ArrayList<String> advList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();

        AdManager adManager = new AdManager(MainActivity.this, advList);
        adManager
                /**
                 * 设置弹窗背景全屏显示还是在内容区域显示
                 */
                .setOverScreen(true)
                /**
                 * 设置ViewPager的滑动动画
                 */
                //                .setPageTransformer(new DepthPageTransformer())
                /**
                 * 设置弹窗距离屏幕两侧的距离（单位dp）
                 */
                .setPadding(100)
                /**
                 * 设置弹窗的宽高比
                 */
                .setWidthPerHeight(0.45f)
                /**
                 * 设置弹窗的背景色（当弹窗背景设置透明时，此设置失效）
                 */
                .setBackViewColor(Color.parseColor("#AA333333"))
                /**
                 * 设置弹窗背景是否透明
                 */
                .setAnimBackViewTransparent(true)
                /**
                 * 设置弹窗关闭图标是否可见
                 */
                .setDialogCloseable(true)
                /**
                 * 设置弹窗弹性滑动弹性值
                 */
                .setBounciness(15)
                /**
                 * 设置弹窗弹性滑动速度值
                 */
                .setSpeed(5)
                /**
                 * 设定弹窗点击事件回调
                 */
                /*.setOnImageClickListener(new AdManager.OnImageClickListener() {
                    @Override
                    public void onImageClick(View view, String url) {

                    }
                })*/
                /**
                 * 设定关闭按钮点击事件回调
                 */
                /*.setOnCliseClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                })*/
                /**
                 * 开始执行弹窗的显示操作，可传值为0-360，0表示从右开始弹出，逆时针方向，也可以传入自定义的方向值
                 */
                .showAdDialog(AdConstant.ANIM_UP_TO_DOWN);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        advList.add("https://raw.githubusercontent.com/yipianfengye/android-adDialog/master/images/testImage1.png");

        advList.add("https://raw.githubusercontent.com/yipianfengye/android-adDialog/master/images/testImage2.png");
    }
}
