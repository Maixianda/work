package com.gzliangce.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.gzliangce.AppContext;
import com.gzliangce.R;

import java.util.Random;

import io.ganguo.library.util.Strings;


/**
 * 验证码生成工具类
 */
public class CodeUtil {
    private static final char[] CHARS = {'2', '3', '4', '5', '6', '7', '8',
            '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n',
            'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B',
            'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'P',
            'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    private static CodeUtil bmpCode;
    private Bitmap bitmap;

    public static CodeUtil getInstance() {
        if (bmpCode == null) {
            bmpCode = new CodeUtil();
        }
        return bmpCode;
    }

    private int width = 100, height = 40;
    private int base_padding_left = 10,
            range_padding_left = 20,
            base_padding_top = 15,
            range_padding_top = 20;

    // number of chars, lines; font size
    private int codeLength = 4,
            line_number = 2, font_size = 60;

    // variables
    private String code;
    private int padding_left, padding_top;
    private Random random = new Random();


    /**
     * 生成一张验证码图片
     */
    public Bitmap createBitmap(Context context, String code) {
        initData(context, code);
        Bitmap bp = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        drawText(bp, context);
        return bp;
    }


    /**
     * 初始化数据
     */
    private void initData(Context context, String mCode) {
        padding_left = 0;
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_code_img_bg);
        }
        if (bitmap != null) {
            width = bitmap.getWidth();
            height = bitmap.getHeight();
        }
        if (Strings.isNotEmpty(mCode)) {
            this.code = mCode;
            codeLength = code.length();
        } else {
            this.code = createCode();
        }
    }


    /**
     * 绘制图片验证码
     */
    private void drawText(Bitmap bp, Context context) {
        float with = 0;
        Canvas c = new Canvas(bp);
        c.drawColor(Color.BLUE);
        c.drawColor(context.getResources().getColor(R.color.gray_b5));
        font_size = context.getResources().getDimensionPixelOffset(R.dimen.font_18);
        Paint paint = new Paint();
        paint.setTextSize(font_size);
        randomTextStyle(paint);
        randomPadding();
        initLeftWith(paint);
        for (int i = 0; i < code.length(); i++) {
            randomTextStyle(paint);
            if (i == 0) {
                with = paint.measureText(code.charAt(i) + "");
            }
            c.drawText(code.charAt(i) + "", padding_left * (i + 1) + with * i, height / 2 + font_size / 2, paint);
        }
        for (int i = 0; i < line_number; i++) {
            drawLine(c, paint);
        }
        c.save(Canvas.ALL_SAVE_FLAG);
        c.restore();
    }


    /**
     * 获取左边距
     */
    private void initLeftWith(Paint paint) {
        int width = AppContext.me().getResources().getDimensionPixelSize(R.dimen.dp_79);
        int length = codeLength + 1;
        padding_left = (width - getTextWidth(paint)) / length;
    }

    /**
     * 获取文字所占用宽度
     */
    private int getTextWidth(Paint paint) {
        int width = 0;
        for (int i = 0; i < code.length(); i++) {
            width += paint.measureText(code.charAt(i) + "");
        }
        return width;
    }


    /**
     * 获取已经生成的验证码
     */
    public String getCode() {
        return code;
    }


    /**
     * 随机生成一个验证码
     */
    private String createCode() {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < codeLength; i++) {
            buffer.append(CHARS[random.nextInt(CHARS.length)]);
        }
        return buffer.toString();
    }

    /**
     * 画干扰线
     */
    private void drawLine(Canvas canvas, Paint paint) {
        int color = randomColor();
        int startX = random.nextInt(width);
        int startY = random.nextInt(height);
        int stopX = random.nextInt(width);
        int stopY = random.nextInt(height);
        paint.setStrokeWidth(1);
        paint.setColor(color);
        canvas.drawLine(startX, startY, stopX, stopY, paint);
    }

    private int randomColor() {
        return randomColor(1);
    }


    /**
     * 随机生成字体颜色
     */
    private int randomColor(int rate) {
        int red = random.nextInt(256) / rate;
        int green = random.nextInt(256) / rate;
        int blue = random.nextInt(256) / rate;
        return Color.rgb(red, green, blue);
    }

    /**
     * 随机生成字体风格
     */
    private void randomTextStyle(Paint paint) {
        int color = randomColor();
        paint.setColor(color);
        paint.setFakeBoldText(random.nextBoolean());
        float skewX = random.nextInt(11) / 10;
        skewX = random.nextBoolean() ? skewX : -skewX;
        paint.setTextSkewX(skewX);
    }

    /**
     * 随机生成边距
     */
    private void randomPadding() {
        padding_left += base_padding_left + random.nextInt(range_padding_left);
        padding_top = base_padding_top + random.nextInt(range_padding_top);
    }
}
