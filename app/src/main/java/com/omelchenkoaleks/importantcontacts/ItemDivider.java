package com.omelchenkoaleks.importantcontacts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

class ItemDivider extends RecyclerView.ItemDecoration {
    private final Drawable divider;

    // конструктор загружает встроенный разделитель элементов списка Android
    public ItemDivider(Context context) {
        int[] attrs = {android.R.attr.listDivider};
        divider = context.obtainStyledAttributes(attrs).getDrawable(0);
    }

    // рисует разделители элементов списка в RecyclerView
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent,
                           RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        // вычислить левые и правые координаты для всех делителей
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        // рассчитать верхнюю и нижнюю координаты для текущего делителя
        for (int i = 0; i < parent.getChildCount() - 1; ++i) {
            View item = parent.getChildAt(i); // get ith list item

            // calculate top/bottom y-coordinates for current divider
            int top = item.getBottom() + ((RecyclerView.LayoutParams)
                    item.getLayoutParams()).bottomMargin;
            int bottom = top + divider.getIntrinsicHeight();

            // нарисовать делитель с рассчитанными границами
            divider.setBounds(left, top, right, bottom);
            divider.draw(c);
        }
    }
}
