package com.longforus.mylauncher;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import java.io.ByteArrayOutputStream;

public class BitmapUtil {

    public static byte[] drawableToByte(Drawable drawable) {
        // 第一步，将Drawable对象转化为Bitmap对象 , 使用下面的方法，防止VectorDrawable cannot be cast to Drawable
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        //注意，下面三行代码要用到，否则在View或者SurfaceView里的canvas.drawBitmap会看不到图
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        //第二步，声明并创建一个输出字节流对象
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        //第三步，调用compress将Bitmap对象压缩为PNG格式，第二个参数为PNG图片质量，第三个参数为接收容器，即输出字节流os
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        return os.toByteArray();
    }

    public static Drawable byteToDrawable(byte[] bytes) {
        //第一步，从数据库中读取出相应数据，并保存在字节数组中
        //第二步，调用BitmapFactory的解码方法decodeByteArray把字节数组转换为Bitmap对象
        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        //第三步，调用BitmapDrawable构造函数生成一个BitmapDrawable对象，该对象继承Drawable对象，所以在需要处直接使用该对象即可
//        Bitmapdrawable bd = new BitmapDrawable(bmp);
        return new BitmapDrawable(bmp);
    }

    public static Bitmap byteToBitmap(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}