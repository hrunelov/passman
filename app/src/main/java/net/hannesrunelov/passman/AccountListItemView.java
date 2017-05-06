package net.hannesrunelov.passman;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.widget.TextView;

public class AccountListItemView extends CardView {
    private String account;

    private TextView accountView;

    public AccountListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    private void init(Context context, AttributeSet attrs) {
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.AccountListItemView);

        a.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;
    }

    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        if (accountView == null) accountView = (TextView)findViewById(R.id.accountLabel);
        if (accountView == null) return;
        this.account = account;
        accountView.setText(account);
    }
}
