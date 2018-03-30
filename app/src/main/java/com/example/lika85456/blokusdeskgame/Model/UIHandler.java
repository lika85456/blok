package com.example.lika85456.blokusdeskgame.Model;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.Html;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lika85456.blokusdeskgame.Game.Move;
import com.example.lika85456.blokusdeskgame.Game.Piece;
import com.example.lika85456.blokusdeskgame.Game.Player;
import com.example.lika85456.blokusdeskgame.Listeners.UIListener;
import com.example.lika85456.blokusdeskgame.R;
import com.example.lika85456.blokusdeskgame.Utilities.Initialization.OnOnInitializedListener;
import com.example.lika85456.blokusdeskgame.Utilities.Utility;
import com.example.lika85456.blokusdeskgame.Views.GridView;
import com.example.lika85456.blokusdeskgame.Views.GridViewMoveListener;
import com.example.lika85456.blokusdeskgame.Views.SquareGroup;
import com.example.lika85456.blokusdeskgame.Views.SquareGroupScrollView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by lika85456 on 27.03.2018.
 */

public class UIHandler {

    public static final byte CONSOLE_STATE = 0;
    public static final byte CONFIRM_STATE = 1;
    public final GridView gridView;
    public byte state = CONSOLE_STATE;
    public SquareGroupScrollView scrollView;

    public TextView consoleView;
    public Button confirmButton;
    public LinearLayout consoleContainer;

    private Piece selectedPiece;
    private UIListener uiListener;

    private boolean canConfirm = false;

    public UIHandler(GridView gridViewv, SquareGroupScrollView scrollView, LinearLayout consoleContainer) {
        this.scrollView = scrollView;
        this.gridView = gridViewv;

        this.consoleContainer = consoleContainer;

        this.consoleView = consoleContainer.findViewById(R.id.consoleView);
        this.confirmButton = consoleContainer.findViewById(R.id.turn_confirm_button);

        /** Confirm button onClickListener **/
        this.confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onConfirm();
            }
        });

        this.gridView.setOnMoveListener(new GridViewMoveListener() {
            @Override
            public void onSelectedSquareGroupMove(int x, int y) {
                if (gridView.board.isValid(selectedPiece, gridView.selectedX, gridView.selectedY))
                    setConfirmButtonColor(0xFF3FF931);
                else
                    setConfirmButtonColor(Color.RED);
            }
        });

        final SquareGroupScrollView temp = scrollView;
        //SquareGroups initialization
        gridView.setOnInitializedListener(new OnOnInitializedListener() {
            public void onInit() {

                for (int i = 0; i < Piece.groups.size(); i++) {
                    temp.add(Piece.groups.get(i));
                }
            }
        });

        /***
         * called after selecting is done
         * @param squareGroup
         */
        scrollView.onClickListener = new View.OnClickListener() {
            private long lastTimeClick;


            public void onClick(final View view) {
                setConfirmState();


                final int width = view.getWidth();
                final int height = view.getHeight();

                final boolean doubleClick;
                doubleClick = System.currentTimeMillis() - lastTimeClick < 400;
                ValueAnimator singleClickAnimator = ValueAnimator.ofFloat(0f, (float) (Math.PI / 2));
                singleClickAnimator.setInterpolator(new LinearInterpolator());

                singleClickAnimator.setDuration(150);
                singleClickAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float progress = (float) animation.getAnimatedValue();
                        float multiplier = ((float) Math.sin(progress)) * 0.35f + 1;

                        ((SquareGroup) view).setSize(multiplier);

                        if (progress > (Math.PI / 2) - 0.01f) {
                            selectedPiece = ((SquareGroup) view).piece;
                            if (doubleClick) {
                                //If its double click - roatte the piece (and call listener?)
                                selectedPiece.rotateBy90();
                            }
                        }
                        //view.requestLayout();
                    }
                });

                singleClickAnimator.start();

                lastTimeClick = System.currentTimeMillis();
                selectedPiece = ((SquareGroup) view).piece;
                uiListener.onPieceSelected(selectedPiece);
                gridView.selected((SquareGroup) view);
                //TODO add moer events?
            }
        };


    }

    /***
     * Makes CONFIRM button apper
     */
    public void setConfirmState() {
        consoleView.setVisibility(GONE);
        confirmButton.setVisibility(VISIBLE);
        state = CONFIRM_STATE;
    }

    /***
     * Makes ConsoleView appear
     */
    public void setConsoleState() {
        consoleView.setVisibility(VISIBLE);
        confirmButton.setVisibility(GONE);
        state = CONSOLE_STATE;
    }

    public byte getState() {
        return state;
    }

    /***
     * Called when user confirms placing piece
     */
    public void onConfirm() {
        this.uiListener.onMoveConfirm(gridView.selectedX, gridView.selectedY);
        this.setConsoleState();
    }

    public void removeSquareGroupFromList(Piece piece) {
        scrollView.removeElementAtIndex(piece);
    }

    public void setUiListener(UIListener l) {
        this.uiListener = l;
    }

    /**
     * Sets text in consoleView - <font color='red'>red</font>
     *
     * @param text
     */
    private void setConsoleText(String text) {
        //<font color='red'>red</font>
        consoleView.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
    }

    public Piece getSelectedPiece() {
        return selectedPiece;
    }

    public void onPlayerMove(Player player, Move move) {
        //TODO do animation on move.piece, x,y

    }

    public void setConfirmButtonColor(int color) {
        GradientDrawable drawable = (GradientDrawable) confirmButton.getBackground();
        drawable.setStroke(Utility.convertDpToPixels(2.f, confirmButton.getContext()), color);
        confirmButton.setTextColor(color);
        drawable.mutate();
    }

}
