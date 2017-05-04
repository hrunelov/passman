package net.hannesrunelov.passman;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AccountInfoView extends LinearLayout {
    private String account;
    private int length;
    private int include;

    private TextView accountView;
    private TextView lengthView;
    private TextView includeView;

    public AccountInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    private void init(Context context, AttributeSet attrs) {
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.AccountInfoView);

        try {
            View view = View.inflate(context, R.layout.account_info_view, this);

            accountView = (TextView) findViewById(R.id.accountView);
            lengthView = (TextView) findViewById(R.id.lengthView);
            includeView = (TextView) findViewById(R.id.include);

            setAccount(a.getString(R.styleable.AccountInfoView_account));
            setLength(a.getInt(R.styleable.AccountInfoView_length, 20));
            setInclude(a.getInt(R.styleable.AccountInfoView_include, 15));
        } finally {
            a.recycle();
        }
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
        this.account = account;
        accountView.setText(account);
    }

    public int getLength() {
        return length;
    }
    public void setLength(int length) {
        this.length = length;
        lengthView.setText(length);
    }

    public int getInclude() {
        return include;
    }
    public void setInclude(int include) {
        this.include = include;
        StringBuilder sb = new StringBuilder();
        if ((include & PasswordGenerator.UPPERCASE) == PasswordGenerator.UPPERCASE) sb.append("A");
        if ((include & PasswordGenerator.LOWERCASE) == PasswordGenerator.LOWERCASE) sb.append("a");
        if ((include & PasswordGenerator.NUMBERS) == PasswordGenerator.NUMBERS) sb.append("3");
        if ((include & PasswordGenerator.SYMBOLS) == PasswordGenerator.SYMBOLS) sb.append("$");
        includeView.setText(sb.toString());
    }
}
