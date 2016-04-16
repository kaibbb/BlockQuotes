package com.dtcj.liukai.blockquotes;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Layout;
import android.text.Spannable;
import android.text.style.LeadingMarginSpan;
import android.text.style.LineBackgroundSpan;
import android.text.style.QuoteSpan;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final int BACKGROUND_COLOR  = Color.RED;
    public static final int STRIPE_COLOR = Color.BLUE;
    public static final int STRIPE_WIDTH = 12;
    public static final int GAP = 44;

    public static final String html = "<blockquote><p>我的收入就是书的版税，我没有商业头脑，股票、基金从来不买，玩赛车是最费钱的，弄得家里常常揭不开锅。</p></blockquote> ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Spannable s = Spannable.Factory.getInstance().newSpannable(Html.fromHtml(html));
        replaceQuoteSpans(s);
        TextView textView = (TextView) findViewById(R.id.tv);
        textView.setText(s);
    }


    private void replaceQuoteSpans(Spannable spannable) {
        QuoteSpan[] quoteSpans = spannable.getSpans(0, spannable.length(), QuoteSpan.class);
        for (QuoteSpan quoteSpan : quoteSpans) {
            int start = spannable.getSpanStart(quoteSpan);
            int end = spannable.getSpanEnd(quoteSpan);
            int flags = spannable.getSpanFlags(quoteSpan);
            spannable.removeSpan(quoteSpan);
            spannable.setSpan(new CustomQuoteSpan(
                            BACKGROUND_COLOR,
                            STRIPE_COLOR,
                            STRIPE_WIDTH,
                            GAP),
                    start,
                    end,
                    flags);
        }
    }

    public class CustomQuoteSpan implements LeadingMarginSpan, LineBackgroundSpan {
        private final int backgroundColor;
        private final int stripeColor;
        private final float stripeWidth;
        private final float gap;

        public CustomQuoteSpan(int backgroundColor, int stripeColor, float stripeWidth, float gap) {
            this.backgroundColor = backgroundColor;
            this.stripeColor = stripeColor;
            this.stripeWidth = stripeWidth;
            this.gap = gap;
        }

        @Override
        public int getLeadingMargin(boolean first) {
            return (int) (stripeWidth + gap);
        }

        @Override
        public void drawLeadingMargin(Canvas c, Paint p, int x, int dir, int top, int baseline, int bottom,
                                      CharSequence text, int start, int end, boolean first, Layout layout) {
            Paint.Style style = p.getStyle();
            int paintColor = p.getColor();

            p.setStyle(Paint.Style.FILL);
            p.setColor(stripeColor);

            c.drawRect(x, top, x + dir * stripeWidth, bottom, p);

            p.setStyle(style);
            p.setColor(paintColor);
        }

        @Override
        public void drawBackground(Canvas c, Paint p, int left, int right, int top, int baseline, int bottom, CharSequence text, int start, int end, int lnum) {
            int paintColor = p.getColor();
            p.setColor(backgroundColor);
            c.drawRect(left, top, right, bottom, p);
            p.setColor(paintColor);
        }
    }
}
