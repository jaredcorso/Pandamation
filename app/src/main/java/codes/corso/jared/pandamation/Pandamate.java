package codes.corso.jared.pandamation;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.widget.ImageView;

import org.apache.commons.io.IOUtils;
import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.List;

public class Pandamate {
    public static class MyFrame {
        byte[] bytes;
        int duration;
        Drawable drawable;
        boolean isReady = false;
    }


    public interface OnDrawableLoadedListener {
        void onDrawableLoaded(List<MyFrame> myFrames);
    }

    public static void loadRaw(final int resourceId, final Context context, final OnDrawableLoadedListener onDrawableLoadedListener) {
        loadFromXml(resourceId, context, onDrawableLoadedListener);
    }

    private static void loadFromXml(final int resourceId, final Context context, final OnDrawableLoadedListener onDrawableLoadedListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<MyFrame> myFrames = new ArrayList<>();

                XmlResourceParser parser = context.getResources().getXml(resourceId);

                try {
                    int eventType = parser.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_DOCUMENT) {

                        } else if (eventType == XmlPullParser.START_TAG) {

                            if (parser.getName().equals("item")) {
                                byte[] bytes = null;
                                int duration = 1000;

                                for (int i=0; i<parser.getAttributeCount(); i++) {
                                    if (parser.getAttributeName(i).equals("drawable")) {
                                        int resId = Integer.parseInt(parser.getAttributeValue(i).substring(1));
                                        bytes = IOUtils.toByteArray(context.getResources().openRawResource(resId));
                                    }
                                    else if (parser.getAttributeName(i).equals("duration")) {
                                        duration = parser.getAttributeIntValue(i, 1000);
                                    }
                                }

                                MyFrame myFrame = new MyFrame();
                                myFrame.bytes = bytes;
                                myFrame.duration = duration;
                                myFrames.add(myFrame);
                            }

                        } else if (eventType == XmlPullParser.END_TAG) {

                        } else if (eventType == XmlPullParser.TEXT) {

                        }

                        eventType = parser.next();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

                // Run on UI Thread
                new Handler(context.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (onDrawableLoadedListener != null) {
                            onDrawableLoadedListener.onDrawableLoaded(myFrames);
                        }
                    }
                });
            }
        }).run();
    }

    public static void animateRawManually(int resourceId, final ImageView imageView, final Runnable onStart, final Runnable onComplete) {
        loadRaw(resourceId, imageView.getContext(), new OnDrawableLoadedListener() {
            @Override
            public void onDrawableLoaded(List<MyFrame> myFrames) {
                if (onStart != null) {
                    onStart.run();
                }

                animateRawManually(myFrames, imageView, onComplete);
            }
        });
    }

    public static void animateRawManually(List<MyFrame> myFrames, ImageView imageView, Runnable onComplete) {
        animateRawManually(myFrames, imageView, onComplete, 0);
    }

    private static void animateRawManually(final List<MyFrame> myFrames, final ImageView imageView, final Runnable onComplete, final int frameNumber) {
        final MyFrame thisFrame = myFrames.get(frameNumber);

        if (frameNumber == 0) {
            thisFrame.drawable = new BitmapDrawable(imageView.getContext().getResources(), BitmapFactory.decodeByteArray(thisFrame.bytes, 0, thisFrame.bytes.length));
        }
        else {
            MyFrame previousFrame = myFrames.get(frameNumber - 1);
            ((BitmapDrawable) previousFrame.drawable).getBitmap().recycle();
            previousFrame.drawable = null;
            previousFrame.isReady = false;
        }

        imageView.setImageDrawable(thisFrame.drawable);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (imageView.getDrawable() == thisFrame.drawable) {
                    if (frameNumber + 1 < myFrames.size()) {
                        MyFrame nextFrame = myFrames.get(frameNumber+1);

                        if (nextFrame.isReady) {
                            animateRawManually(myFrames, imageView, onComplete, frameNumber + 1);
                        }
                        else {
                            nextFrame.isReady = true;
                        }
                    }
                    else {
                        if (onComplete != null) {
                            onComplete.run();
                        }
                    }
                }
            }
        }, thisFrame.duration);

        if (frameNumber + 1 < myFrames.size()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    MyFrame nextFrame = myFrames.get(frameNumber+1);
                    nextFrame.drawable = new BitmapDrawable(imageView.getContext().getResources(), BitmapFactory.decodeByteArray(nextFrame.bytes, 0, nextFrame.bytes.length));
                    if (nextFrame.isReady) {
                        animateRawManually(myFrames, imageView, onComplete, frameNumber + 1);
                    }
                    else {
                        nextFrame.isReady = true;
                    }

                }
            }).run();
        }
    }
}