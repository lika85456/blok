package com.lika85456.lika85456.blokusdeskgame.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.lika85456.lika85456.blokusdeskgame.Game.Piece;

import java.util.ArrayList;

/**
 * Created by lika85456 on 24.03.2018.
 */

public class SquareGroupScrollView extends RelativeLayout {

    public OnClickListener onClickListener;
    public ArrayList<Piece> list;
    public byte color = 0;

    public SquareGroupScrollView(Context context) {
        super(context);
    }

    public SquareGroupScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareGroupScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void onMeasure(int w, int h) {
        int width = MeasureSpec.getSize(w);
        int height = MeasureSpec.getSize(h);
        int childs = getChildCount();
        int rows = 7;
        if (childs > 0)
            rows = (int) Math.ceil((double) childs / (double) 3);
        this.setMeasuredDimension(width, (int) (((float) width / 3.f - 15.f) * rows));
    }

    @Override
    protected void onLayout(boolean b, int i0, int i1, int i2, int i3) {
        //this.setMeasuredDimension(getWidth(), (int) (((float) getWidth() / 3.f - 15.f) * list.size() / 3));
        //init();
        for (int i = 0; i < this.getChildCount(); i++) {
            SquareGroup temp = (SquareGroup) this.getChildAt(i);
            int width = (i2 - i0) / 3 - 15;
            int margin = (width + 15) * (i % 3) + 7;
            temp.layout(margin, i / 3 * width, margin + width, i / 3 * width + width);

            //this.getChildAt(x).layout(margin, ((int)(x / 3)) * width, margin + width, ((int)(x / 3)) * width + width);
        }
    }

    public void removeElementWithIndex(int index) {
        int realIndex = 0;
        for (Piece p : list) {
            if (p.index == index) {
                break;
            }
            realIndex++;
        }
        list.remove(realIndex);
        removeViewAt(realIndex);
        requestLayout();
    }

    public int getIndexOfElement(Piece piece) {
        return list.indexOf(piece);
    }

    /*public void onFinishInflate() {
        super.onFinishInflate();
        ArrayList<Piece> pieces = Piece.getAllPieces(color);
        for (int i = 0; i < pieces.size(); i++) {
            add(pieces.get(i));
        }

//        this.requestLayout();
    }
    */

    public void setSquareGroupOnClickListener(OnClickListener l) {
        this.onClickListener = l;
        if (this.getChildCount() > 0) {
            for (int i = 0; i < this.getChildCount(); i++)
                this.getChildAt(i).setOnClickListener(l);
        }

    }

    public void add(Piece piece) {
        if (list == null)
            list = new ArrayList<>();
        list.add(piece);
        SquareGroup temp = new SquareGroup(this.getContext(), piece);
        this.addView(temp);
        temp.setOnClickListener(onClickListener);
    }

    public void updatePiece(Piece piece) {
        for (int i = 0; i < getChildCount(); i++) {
            if (((SquareGroup) getChildAt(i)).getPiece().index == piece.index)
                ((SquareGroup) getChildAt(i)).fromPiece(piece);
        }
    }

    public void addAtIndex(Piece piece, int index) {
        if (list == null)
            list = new ArrayList<>();
        list.add(piece);
        SquareGroup temp = new SquareGroup(this.getContext(), piece);
        this.addView(temp, index);
        temp.setOnClickListener(onClickListener);
    }
}
