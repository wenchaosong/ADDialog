package com.ad;

import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ad.indicator.CirclePageIndicator;

/**
 * 首页广告管理类
 */
public class AdManager {

    private Activity context;
    private DisplayMetrics displayMetrics = new DisplayMetrics();
    private RelativeLayout adRootContent;
    private CirclePageIndicator mIndicator;
    private AnimDialogUtils animDialogUtils;
    // 广告弹窗距离两侧的距离
    private int padding = 44;
    private int time = 1000;
    // 广告弹窗的宽高比
    private float widthPerHeight = 0.75f;
    // 弹窗默认图片
    private PagerAdapter adapter;
    // 弹窗背景是否透明
    private boolean isAnimBackViewTransparent = false;
    // 弹窗是否可关闭
    private boolean isDialogCloseable = true;
    // 弹窗关闭点击事件
    private View.OnClickListener onCloseClickListener = null;
    // 设置弹窗背景颜色
    private int backViewColor = Color.parseColor("#bf000000");
    // 弹性动画弹性参数
    private double bounciness = AdConstant.BOUNCINESS;
    // 弹性动画速度参数
    private double speed = AdConstant.SPEED;
    // viewPager滑动动画效果
    private ViewPager.PageTransformer pageTransformer = null;
    // 是否覆盖全屏幕
    private boolean isOverScreen = true;

    public AdManager(Activity context, PagerAdapter adapter) {
        this.context = context;
        this.adapter = adapter;

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        DisplayUtil.density = dm.density;
        DisplayUtil.densityDPI = dm.densityDpi;
        DisplayUtil.screenWidthPx = dm.widthPixels;
        DisplayUtil.screenhightPx = dm.heightPixels;
        DisplayUtil.screenWidthDip = DisplayUtil.px2dip(context, dm.widthPixels);
        DisplayUtil.screenHightDip = DisplayUtil.px2dip(context, dm.heightPixels);
    }

    /**
     * 开始执行显示广告弹窗的操作
     *
     * @param animType 显示位置
     */
    public void showAdDialog(final int animType) {

        View contentView = LayoutInflater.from(context).inflate(R.layout.ad_dialog_content_layout, null);
        adRootContent = (RelativeLayout) contentView.findViewById(R.id.ad_root_content);
        ViewPager viewPager = (ViewPager) contentView.findViewById(R.id.viewPager);
        mIndicator = (CirclePageIndicator) contentView.findViewById(R.id.indicator);

        if (adapter != null)
            viewPager.setAdapter(adapter);
        else {
            new Exception("adapter is empty");
        }

        if (pageTransformer != null) {
            viewPager.setPageTransformer(true, pageTransformer);
        }

        mIndicator.setViewPager(viewPager);
        isShowIndicator();

        animDialogUtils = AnimDialogUtils.getInstance(context)
                .setAnimBackViewTransparent(isAnimBackViewTransparent)
                .setDialogCloseable(isDialogCloseable)
                .setDialogBackViewColor(backViewColor)
                .setOnCloseClickListener(onCloseClickListener)
                .setOverScreen(isOverScreen)
                .initView(contentView);
        setRootContainerHeight();

        // 延迟1s展示，为了避免ImageLoader还为加载完缓存图片时就展示了弹窗的情况
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                animDialogUtils.show(animType, bounciness, speed);
            }
        }, time);
    }

    private void setRootContainerHeight() {

        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthPixels = displayMetrics.widthPixels;
        int totalPadding = DisplayUtil.dip2px(context, padding * 2);
        int width = widthPixels - totalPadding;
        final int height = (int) (width / widthPerHeight);
        ViewGroup.LayoutParams params = adRootContent.getLayoutParams();
        params.height = height;
    }

    /**
     * 根据页面数量，判断是否显示Indicator
     */
    private void isShowIndicator() {
        if (adapter.getCount() > 1) {
            mIndicator.setVisibility(View.VISIBLE);
        } else {
            mIndicator.setVisibility(View.INVISIBLE);
        }
    }

    // ######################## API #########################

    /**
     * 开始执行销毁弹窗的操作
     */
    public void dismissAdDialog() {
        animDialogUtils.dismiss(AdConstant.ANIM_STOP_DEFAULT);
    }

    /**
     * 设置弹窗距离屏幕左右两侧的距离
     */
    public AdManager setPadding(int padding) {
        this.padding = padding;
        return this;
    }

    /**
     * 设置弹窗宽高比
     */
    public AdManager setWidthPerHeight(float widthPerHeight) {
        this.widthPerHeight = widthPerHeight;
        return this;
    }

    /**
     * 设置背景是否透明
     */
    public AdManager setAnimBackViewTransparent(boolean animBackViewTransparent) {
        this.isAnimBackViewTransparent = animBackViewTransparent;
        return this;
    }

    /**
     * 设置弹窗关闭按钮是否可见
     */
    public AdManager setDialogCloseable(boolean dialogCloseable) {
        this.isDialogCloseable = dialogCloseable;
        return this;
    }

    /**
     * 设置弹窗关闭按钮点击事件
     */
    public AdManager setOnCloseClickListener(View.OnClickListener onCloseClickListener) {
        this.onCloseClickListener = onCloseClickListener;
        return this;
    }

    /**
     * 设置弹窗背景颜色
     */
    public AdManager setBackViewColor(int backViewColor) {
        this.backViewColor = backViewColor;
        return this;
    }

    /**
     * 设置弹窗弹性动画弹性参数
     */
    public AdManager setBounciness(double bounciness) {
        this.bounciness = bounciness;
        return this;
    }

    /**
     * 设置弹窗弹性动画速度参数
     */
    public AdManager setSpeed(double speed) {
        this.speed = speed;
        return this;
    }

    /**
     * 设置弹窗弹出时间参数
     */
    public AdManager setDelayTime(int time) {
        this.time = time;
        return this;
    }

    /**
     * 设置ViewPager滑动动画效果
     */
    public AdManager setPageTransformer(ViewPager.PageTransformer pageTransformer) {
        this.pageTransformer = pageTransformer;
        return this;
    }

    /**
     * 设置弹窗背景是否覆盖全屏幕
     */
    public AdManager setOverScreen(boolean overScreen) {
        this.isOverScreen = overScreen;
        return this;
    }
}
