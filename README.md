Flexible Indicators for Android
===============================

These indicators animate every little touch on your [ViewPager][1].

Want to use a [SpinningViewPager][2] instead? Just [enable an offset][3]
and you're good to go. Need even finer control? Under the hood,
**CutoutViewIndicator** extends LinearLayout; cell styling is done
through LayoutParams. You can set individual values dynamically on
these params and Android's View save/restore state logic will keep
them in order.

You can define each cell directly in your xml file, or add them manually.
Note that only ImageView cells are supported right now.

Pictures:
---------

The sample mobile app in use:

![An Android device screen, its background a light blue. Near the top 
lies a dark blue ViewPager - just 20 dp below it is a row of narrow
rectangles, all but one of which are dark blue too. One of these
rectangles, however, is orange. As the animated gif plays, we see
the ViewPager page to new views. The orange bar underneath reliably
indicates exactly which page is currently showing, mimicking the
slightest perturbation in the ViewPager](http://i.imgur.com/fKOez3z.gif)

There's demo source code for mobile under the `mobile` directory; we're
looking to expand compatibility to Android Wear and Android TV too.

Sample code:
------------

Add a view like this to your xml layout.
```
<com.fuzz.indicator.CutoutViewIndicator
        android:id="@+id/indicator"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_below="@id/cutoutViewIndicator"
        android:orientation="horizontal"
        app:rcv_drawable="@drawable/indicator"
        app:rcv_drawable_unselected="@drawable/indicator_background"
        app:rcv_internal_margin="@dimen/indicator_internal_spacing"
        app:rcv_tools_indicator_count="4"
        />
```

Note that `rcv_drawable` and `rcv_drawable_unselected` default to 0.
You won't see the indicator unless `rcv_drawable` is set to something,
so be sure not to forget about it.

You can leave `rcv_drawable_unselected` equal to 0 if you want a
transparent background.

In your Java (or other JVM-compatible language) file, get a reference
to the view and set it up with a ViewPager.

```
ViewPager pager;
pager.setAdapter(adapter);


// Activity
CutoutViewIndicator cvi = (CutoutViewIndicator) findViewById(R.id.indicator);
cvi.setViewPager(pager);
...
// Fragment
CutoutViewIndicator cvi = (CutoutViewIndicator) view.findViewById(R.id.indicator);
cvi.setViewPager(pager);
...
// DataBinding
CutoutViewIndicator cvi = binding.indicator;
cvi.setViewPager(pager);
```

Then you're good to go. When you're done with the view, it's probably a
good idea to de-initialise the CutoutViewIndicator.

```
cvi.setViewPager(null);
```

If you want to use something other than simple ImageViews, you can
set your own Generator on the object.

```
cvi.setGenerator(new GifGenerator());
```


Caveats
-------

Right now it only supports Android SDK 15 and up
(that makes it compatible with Jelly Bean, Lollipop, Marshmallow, and
_probably_ Nougat), but there's little stopping it from being backported
all the way to APIv11.

Grab a copy of the demo application (source available [here][4]) to try
it in action.

Credit:
-------

This project was inspired by the work of others in the Android community,
especially [ViewPagerIndicator][5].


 [1]: https://developer.android.com/reference/android/support/v4/view/ViewPager.html
 [2]: https://github.com/eccyan/SpinningTabStrip
 [3]: src/main/java/com/fuzz/indicator/CutoutViewIndicator.java#L392
 [4]: https://github.com/fuzz-productions/CutoutViewIndicator-Demo
 [5]: https://github.com/JakeWharton/ViewPagerIndicator
 [6]: https://github.com/fuzz-productions/CutoutViewIndicator