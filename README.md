### 广告弹出框

### 使用

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

#### **导入方式**

在项目根目录下的build.gradle中的allprojects{}中，添加jitpack仓库地址，如下：

    allprojects {
	    repositories {
	        jcenter()
	        maven { url 'https://jitpack.io' }//添加jitpack仓库地址
	    }
	}
 
打开app的module中的build.gradle，在dependencies{}中，添加依赖，如下：

    dependencies {
	        compile 'com.github.wenchaosong:ADDialog1.0.0'
	}

