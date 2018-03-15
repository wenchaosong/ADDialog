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
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ad.indicator.CirclePageIndicator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

/**
 * 首页广告管理类
 */
public class AdManager {

    private Activity context;
    private DisplayMetrics displayMetrics = new DisplayMetrics();
    private View contentView;
    private ViewPager viewPager;
    private RelativeLayout adRootContent;
    private AdAdapter adAdapter;
    private CirclePageIndicator mIndicator;
    private AnimDialogUtils animDialogUtils;
    private List<String> advInfoListList;
    /**
     * 广告弹窗距离两侧的距离-单位(dp)
     */
    private int padding = 44;
    private int time = 1000;
    /**
     * 广告弹窗的宽高比
     */
    private float widthPerHeight = 0.75f;
    // 弹窗默认图片
    private int loadingRes = -1;
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

    private OnImageClickListener onImageClickListener = null;


    public AdManager(Activity context, List<String> advInfoListList) {
        this.context = context;
        this.advInfoListList = advInfoListList;

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
     * @param animType
     */
    public void showAdDialog(final int animType) {

        contentView = LayoutInflater.from(context).inflate(R.layout.ad_dialog_content_layout, null);
        adRootContent = (RelativeLayout) contentView.findViewById(R.id.ad_root_content);

        viewPager = (ViewPager) contentView.findViewById(R.id.viewPager);
        mIndicator = (CirclePageIndicator) contentView.findViewById(R.id.indicator);

        adAdapter = new AdAdapter();
        viewPager.setAdapter(adAdapter);

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

    /**
     * 开始执行销毁弹窗的操作
     */
    public void dismissAdDialog() {
        animDialogUtils.dismiss(AdConstant.ANIM_STOP_DEFAULT);
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
        if (advInfoListList.size() > 1) {
            mIndicator.setVisibility(View.VISIBLE);
        } else {
            mIndicator.setVisibility(View.INVISIBLE);
        }
    }

    class AdAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return advInfoListList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            String url = advInfoListList.get(position);

            View rootView = context.getLayoutInflater().inflate(R.layout.viewpager_item, null);
            final ImageView image = (ImageView) rootView.findViewById(R.id.simpleDraweeView);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            container.addView(rootView, params);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onImageClickListener != null) {
                        onImageClickListener.onImageClick(view, position);
                    }
                }
            });

            if (loadingRes > 0) {
                Glide.with(context)
                        .load(url)
                        .apply(new RequestOptions().placeholder(loadingRes))
                        .into(image);
            } else {
                Glide.with(context)
                        .load(url)
                        .into(image);
            }

            return rootView;
        }
    }


    // ######################## 点击事件处理操作类 ########################

    /**
     * ViewPager每一项的单击事件
     */
    public interface OnImageClickListener {

        void onImageClick(View view, int position);

    }

    // ######################## get set方法 #########################

    /**
     * 设置默认加载图片
     */
    public AdManager setLoadingRes(int res) {
        this.loadingRes = res;
        return this;
    }

    /**
     * 设置弹窗距离屏幕左右两侧的距离
     *
     * @param padding
     * @return
     */
    public AdManager setPadding(int padding) {
        this.padding = padding;

        return this;
    }

    /**
     * 设置弹窗宽高比
     *
     * @param widthPerHeight
     * @return
     */
    public AdManager setWidthPerHeight(float widthPerHeight) {
        this.widthPerHeight = widthPerHeight;

        return this;
    }

    /**
     * 设置ViewPager Item点击事件
     *
     * @param onImageClickListener
     * @return
     */
    public AdManager setOnImageClickListener(OnImageClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;

        return this;
    }

    /**
     * 设置背景是否透明
     *
     * @param animBackViewTransparent
     * @return
     */
    public AdManager setAnimBackViewTransparent(boolean animBackViewTransparent) {
        isAnimBackViewTransparent = animBackViewTransparent;

        return this;
    }

    /**
     * 设置弹窗关闭按钮是否可见
     *
     * @param dialogCloseable
     * @return
     */
    public AdManager setDialogCloseable(boolean dialogCloseable) {
        isDialogCloseable = dialogCloseable;

        return this;
    }

    /**
     * 设置弹窗关闭按钮点击事件
     *
     * @param onCloseClickListener
     * @return
     */
    public AdManager setOnCloseClickListener(View.OnClickListener onCloseClickListener) {
        this.onCloseClickListener = onCloseClickListener;

        return this;
    }

    /**
     * 设置弹窗背景颜色
     *
     * @param backViewColor
     * @return
     */
    public AdManager setBackViewColor(int backViewColor) {
        this.backViewColor = backViewColor;

        return this;
    }

    /**
     * 设置弹窗弹性动画弹性参数
     *
     * @param bounciness
     * @return
     */
    public AdManager setBounciness(double bounciness) {
        this.bounciness = bounciness;

        return this;
    }

    /**
     * 设置弹窗弹性动画速度参数
     *
     * @param speed
     * @return
     */
    public AdManager setSpeed(double speed) {
        this.speed = speed;

        return this;
    }

    /**
     * 设置弹窗弹出时间参数
     *
     * @param time
     * @return
     */
    public AdManager setDelayTime(int time) {
        this.time = time;

        return this;
    }

    /**
     * 设置ViewPager滑动动画效果
     *
     * @param pageTransformer
     */
    public AdManager setPageTransformer(ViewPager.PageTransformer pageTransformer) {
        this.pageTransformer = pageTransformer;

        return this;
    }

    /**
     * 设置弹窗背景是否覆盖全屏幕
     *
     * @param overScreen
     * @return
     */
    public AdManager setOverScreen(boolean overScreen) {
        isOverScreen = overScreen;

        return this;
    }
}
