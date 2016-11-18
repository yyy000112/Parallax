package android.ye.parallax;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * Created by ye on 2016/11/18.
 */
public class ParallaxListView extends ListView {
    public ParallaxListView(Context context) {
        super(context);
    }

    public ParallaxListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ParallaxListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ParallaxListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private ImageView imageView;
    private int maxHeight;
    private int originalHeight;//imageView 原始高度

    public void setParallaxImageView(final ImageView imageView){
        this.imageView = imageView;
        //设定最大高度
        imageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //先移除已有的监听
                imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                originalHeight = imageView.getHeight();
                int height = imageView.getDrawable().getIntrinsicHeight();//获取图片高度
                maxHeight = originalHeight>height?originalHeight*2:height;
            }
        });
    }


    /**
     * 在listview滑动到头的时候执行，可以获取到继续滑动的距离和方向
     * @param deltaX 继续滑动x方向的距离
     * @param deltaY 继续滑动y方向的距离  负：表示顶部到头   正：表示底部到头
     * @param scrollX x方向可以滚动的距离
     * @param scrollY y方向可以滚动的距离
     * @param scrollRangeX x方向可以滚动的范围
     * @param scrollRangeY y方向可以滚动的范围
     * @param maxOverScrollX x方向最大可以滚动的距离
     * @param maxOverScrollY y方向最大可以滚动的距离
     * @param isTouchEvent true: 是手指拖动滑动 false:表示fling靠惯性滑动;
     * @return
     */
    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        if (deltaY<0 && isTouchEvent){
            //表示顶部到头，并且是手动拖动到头的情况,需要不断的增加ImageView的高度
            if (imageView!=null){
                int newHeight = imageView.getHeight()-deltaY/3;//deltaY/3是为了缓慢增加高度
                //限制高度
                if (newHeight>maxHeight) newHeight = maxHeight;
                imageView.getLayoutParams().height = newHeight;
                imageView.requestLayout();//使ImageView的布局参数生效
            }
        }
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP){
            //手指抬起时需要将imageView恢复到原来高度
            final ValueAnimator animator = ValueAnimator.ofInt(imageView.getHeight(),originalHeight);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    //获取动画的值，设置给imageView
                    int animatedValue = (int) animator.getAnimatedValue();
                    imageView.getLayoutParams().height = animatedValue;
                    imageView.requestLayout();
                }
            });
            // 设置来回弹动的效果
            animator.setInterpolator(new OvershootInterpolator(4));//弹性插值器
            animator.setDuration(300).start();


        }
        return super.onTouchEvent(ev);
    }
}
