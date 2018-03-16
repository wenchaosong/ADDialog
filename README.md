### 广告弹出框

在原项目[android-adDialog](https://github.com/yipianfengye/android-adDialog) 基础上优化了集成的第三方库，更轻便，具体的实现交由自己去处理

![image](/pics/ad.gif )

#### 导入方式

在项目根目录下的build.gradle中的allprojects{}中，添加jitpack仓库地址，如下：

    allprojects {
	    repositories {
	        jcenter()
	        maven { url 'https://jitpack.io' }//添加jitpack仓库地址
	    }
	}

打开app的module中的build.gradle，在dependencies{}中，添加依赖，如下：

    dependencies {
	        compile 'com.github.wenchaosong:ADDialog:1.1.2'
	}

#### 使用

	  AdManager adManager = new AdManager(MainActivity.this, new AdAdapter());
              adManager
                      /**
                       * 设置弹窗背景全屏显示还是在内容区域显示
                       */
                      .setOverScreen(true)
                      /**
                       * 设置ViewPager的滑动动画
                       */
                      .setPageTransformer(new ZoomOutPageTransformer())
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
                       * 设置弹窗弹出时间
                       */
                      .setDelayTime(0)
                      /**
                       * 设定关闭按钮点击事件回调
                       */
                      .setOnCloseClickListener(new View.OnClickListener() {
                          @Override
                          public void onClick(View view) {

                              Toast.makeText(MainActivity.this, "dismiss", Toast.LENGTH_SHORT).show();

                          }
                      })
                      /**
                       * 开始执行弹窗的显示操作，可传值为0-360，0表示从右开始弹出，逆时针方向，也可以传入自定义的方向值
                       */
                      .showAdDialog(AdConstant.ANIM_UP_TO_DOWN);


      class AdAdapter extends PagerAdapter {

              @Override
              public int getCount() {
                  return advList.size();
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
                  String url = advList.get(position);

                  View rootView = getLayoutInflater().inflate(R.layout.viewpager_item, null);
                  final ProgressView progress = (ProgressView) rootView.findViewById(R.id.progress);
                  final ImageView image = (ImageView) rootView.findViewById(R.id.imageview);
                  ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                          ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                  container.addView(rootView, params);
                  image.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {

                          Toast.makeText(MainActivity.this, "position: " + position, Toast.LENGTH_SHORT).show();

                      }
                  });

                  progress.onAttachedToWindow();

                  Glide.with(MainActivity.this)
                          .load(url)
                          .listener(new RequestListener<Drawable>() {
                              @Override
                              public boolean onLoadFailed(GlideException e,
                                                          Object model,
                                                          Target<Drawable> target,
                                                          boolean isFirstResource) {
                                  return false;
                              }

                              @Override
                              public boolean onResourceReady(Drawable resource,
                                                             Object model,
                                                             Target<Drawable> target,
                                                             DataSource dataSource,
                                                             boolean isFirstResource) {

                                  progress.setVisibility(View.GONE);

                                  return false;
                              }
                          })
                          .into(image);

                  return rootView;
              }
          }

详细使用请看 demo