package bk.lvtn.fragment_adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by Phupc on 12/23/17.
 */

public class WrapContentListview extends ListView {

    public WrapContentListview(Context context) {
        super(context);
    }

    public WrapContentListview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WrapContentListview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public WrapContentListview(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        setHeightWrapContent();
    }

    public void setHeightWrapContent() {
        ListAdapter adapter = getAdapter();
        if (adapter == null) {
            return;
        }
        int totalHeight = getPaddingTop() + getPaddingBottom();
        ;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, this);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = this.getLayoutParams();
        params.height = totalHeight + (this.getDividerHeight() * (adapter.getCount() - 1));
        this.setLayoutParams(params);
    }
}
