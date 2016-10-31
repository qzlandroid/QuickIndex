package cn.jit.quickindex.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 联系人快速索引 指示器
 */

public class QuickIndexBar extends View {

    private static final String[] LETTERS = new String[]{
            "A", "B", "C", "D", "E", "F",
            "G", "H", "I", "J", "K", "L",
            "M", "N", "O", "P", "Q", "R",
            "S", "T", "U", "V", "W", "X",
            "Y", "Z"};
    private int cellWidth; //单元格宽度
    private float cellHeight; //单元格高度
    private Paint mPaint;

    public QuickIndexBar(Context context) {
        this(context, null);
    }

    public QuickIndexBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QuickIndexBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG); //抗锯齿
        mPaint.setColor(Color.BLACK); //黑色
        mPaint.setTypeface(Typeface.DEFAULT_BOLD); //粗体
        mPaint.setTextSize(40f);
    }

    //获取每个单元格的高度和宽度
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        cellWidth = getMeasuredWidth();
        int mHeight = getMeasuredHeight();
        cellHeight = mHeight * 1.0f / LETTERS.length;
    }

    /**
     * 画出26个字母
     */
    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < LETTERS.length; i++) {
            String text = LETTERS[i];
            //计算绘制字母的坐标
            Rect bounds = new Rect();
            mPaint.getTextBounds(text, 0, text.length(), bounds);
            int x = (int) (cellWidth / 2.0f - bounds.width() / 2.0f);
            int y = (int) (cellHeight / 2.0f + bounds.height() / 2.0f + i * cellHeight);
            mPaint.setColor(i == curIndex ? Color.GRAY : Color.BLACK);
            canvas.drawText(text, x, y, mPaint);
        }
    }

    /**
     * 处理触摸事件
     */
    int preIndex = -1;
    int curIndex = -1;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //根据按下的坐标计算哪个字母被按下了
                curIndex = (int) (event.getY() / cellHeight);
                if (curIndex >= 0 && curIndex < 26) {
                    if (curIndex != preIndex) {
                        mListener.onLetterUpdate(LETTERS[curIndex]);
                        preIndex = curIndex;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                curIndex = (int) (event.getY() / cellHeight);
                if (curIndex >= 0 && curIndex < 26) {
                    if (curIndex != preIndex) {
                        mListener.onLetterUpdate(LETTERS[curIndex]);
                        preIndex = curIndex;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                curIndex = -1;
                preIndex = -1;
                break;
            default:
                break;
        }
        invalidate();
        return true; //true才能持续接收事件,不然只能接收到down事件
    }

    /**
     * 暴露字母状态改变的监听
     */
    public interface OnLetterUpdateListener {
        void onLetterUpdate(String letter);
    }

    private OnLetterUpdateListener mListener;

    public void setOnLetterUpdateListener(OnLetterUpdateListener listener) {
        mListener = listener;
    }
}
