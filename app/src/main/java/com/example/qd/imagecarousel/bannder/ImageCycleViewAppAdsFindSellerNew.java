package com.example.qd.imagecarousel.bannder;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.qd.imagecarousel.R;

import java.util.ArrayList;
import java.util.List;

/**
 * author: wu
 * date: on 2018/12/18.
 * describe:广告图片自动轮播控件
 */
public class ImageCycleViewAppAdsFindSellerNew extends LinearLayout {

    /**
     * 上下文
     */
    private Context mContext;

    /**
     * 图片轮播视图
     */
    private CycleViewPager mBannerPager = null;

    /**
     * 滚动图片视图适配器
     */
    private ImageCycleAdapter mAdvAdapter;

    /**
     * 图片轮播指示器控件
     */
    private ViewGroup mGroup;

    /**
     * 图片轮播指示器-个图
     */
    private ImageView mImageView = null;

    /**
     * 滚动图片指示器-视图列表
     */
    private ImageView[] mImageViews = null;

    /**
     * 图片滚动当前图片下标
     */
    private int mImageIndex = 1;

    /**
     * 手机密度
     */
    private float mScale;
    /**
     * 轮播图时间
     */
    private int delayMillis = 3000;

    /**
     * @param context
     */
    public ImageCycleViewAppAdsFindSellerNew(Context context) {
        super(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public ImageCycleViewAppAdsFindSellerNew(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mScale = context.getResources().getDisplayMetrics().density;
        LayoutInflater.from(context)
                .inflate(R.layout.view_banner_ads_content_findseller, this);
        mBannerPager = findViewById(R.id.pager_banner);
        mBannerPager.setOnPageChangeListener(new GuidePageChangeListener());
        mBannerPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        // 开始图片滚动
                        startImageTimerTask();
                        break;
                    default:
                        // 停止图片滚动
                        stopImageTimerTask();
                        break;
                }
                return false;
            }
        });
        // 滚动图片右下指示器视图
        mGroup = (ViewGroup) findViewById(R.id.viewGroup);
    }

    /**
     * 装填图片数据
     */
    public void setImageResources(List<String> ad_list,
                                  ImageCycleViewListener imageCycleViewListener) {
        // 清除所有子视图
        mGroup.removeAllViews();
        // 图片广告数量
        final int imageCount = ad_list.size();
        mImageViews = new ImageView[imageCount];
        for (int i = 0; i < imageCount; i++) {
            mImageView = new ImageView(mContext);
            int imageParams = (int) (mScale * 20 + 0.5f);// XP与DP转换，适应不同分辨率
            int imagePadding = (int) (mScale * 5 + 0.5f);
            LayoutParams layout = new LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            layout.setMargins(5, 0, 0, 0);
            mImageView.setLayoutParams(layout);
            // mImageView.setPadding(imagePadding, imagePadding, imagePadding,
            // imagePadding);
            mImageViews[i] = mImageView;
            if (i == 0) {
                mImageViews[i].setBackgroundResource(R.drawable.dot_start_ellipse_findseller);
            } else {
                mImageViews[i].setBackgroundResource(R.drawable.dot_start_findseller);
            }
            mGroup.addView(mImageViews[i]);
        }
        mAdvAdapter = new ImageCycleAdapter(mContext, ad_list,
                imageCycleViewListener);
        mBannerPager.setAdapter(mAdvAdapter);
        startImageTimerTask();
    }

    /**
     * 开始轮播(手动控制自动轮播与否，便于资源控制)
     */
    public void startImageCycle() {
        startImageTimerTask();
    }

    /**
     * 暂停轮播——用于节省资源
     */
    public void pushImageCycle() {
        stopImageTimerTask();
    }

    /**
     * 开始图片滚动任务
     */
    private void startImageTimerTask() {
        stopImageTimerTask();
        // 图片每3秒滚动一次
        mHandler.postDelayed(mImageTimerTask, delayMillis);
    }

    /**
     * 停止图片滚动任务
     */
    private void stopImageTimerTask() {
        mHandler.removeCallbacks(mImageTimerTask);
    }

    private Handler mHandler = new Handler();

    /**
     * 图片自动轮播Task
     */
    private Runnable mImageTimerTask = new Runnable() {

        @Override
        public void run() {
            if (mImageViews != null) {
                // 下标等于图片列表长度说明已滚动到最后一张图片,重置下标
                if ((++mImageIndex) == mImageViews.length + 1) {
                    mImageIndex = 1;
                }
                mBannerPager.setCurrentItem(mImageIndex);
            }
        }
    };

    /**
     * 轮播图片状态监听器
     */
    private final class GuidePageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE)
                startImageTimerTask(); // 开始下次计时
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int index) {

            if (index == 0 || index == mImageViews.length + 1) {
                return;
            }
            // 设置图片滚动指示器背景
            mImageIndex = index;
            index -= 1;
            mImageViews[index].setBackgroundResource(R.drawable.dot_start_ellipse_findseller);
            for (int i = 0; i < mImageViews.length; i++) {
                if (index != i) {
                    mImageViews[i].setBackgroundResource(R.drawable.dot_start_findseller);
                }
            }
        }
    }

    private class ImageCycleAdapter extends PagerAdapter {

        /**
         * 图片视图缓存列表
         */
        private List<ImageView> mImageViewCacheList;

        /**
         * 图片资源列表
         */
        private List<String> mAdList = new ArrayList<>();

        /**
         * 广告图片点击监听器
         */
        private ImageCycleViewListener mImageCycleViewListener;

        private Context mContext;

        public ImageCycleAdapter(Context context, List<String> adList,
                                 ImageCycleViewListener imageCycleViewListener) {
            mContext = context;
            mAdList = adList;
            mImageCycleViewListener = imageCycleViewListener;
            mImageViewCacheList = new ArrayList<>();
        }

        @Override
        public int getCount() {
            return mAdList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            String imageUrl = "";
            if (mAdList.size() != 0) {
                if (mAdList.get(position) != null && !mAdList.get(position).equals("")) {
                    imageUrl = mAdList.get(position);
                }
            }
            ImageView imageView = null;
            if (mImageViewCacheList.isEmpty()) {
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                //imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            } else {
                imageView = mImageViewCacheList.remove(0);
            }
            // 设置图片点击监听
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mImageCycleViewListener.onImageClick(position, v);
                }
            });
            imageView.setTag(imageUrl);
            container.addView(imageView);
            mImageCycleViewListener.displayImage(imageUrl, imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ImageView view = (ImageView) object;
            try {
                container.removeView(view);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            mImageViewCacheList.add(view);
        }

    }

    /**
     * 轮播控件的监听事件
     */
    public interface ImageCycleViewListener {

        /**
         * 加载图片资源
         */
        void displayImage(String imageURL, ImageView imageView);

        /**
         * 单击图片事件
         */
        void onImageClick(int postion, View imageView);
    }

}
