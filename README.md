Flexible Indicators for Android
===============================

These indicators animate every little touch on your [ViewPager][1].

Want to use a [SpinningViewPager][2] instead? Just [enable an offset][3] and you're good to go.
Need even finer control? Under the hood, **CutoutViewIndicator** extends LinearLayout; cell styling is done through LayoutParams.

You can define each cell directly in your xml file, or add them manually.
When there is no  **CutoutViewIndicator** will automatically adjust to its new state.

Right now it only supports Android SDK 15 and up
(that makes it compatible with Jelly Bean, Lollipop, Marshmallow, and
_probably_ Nougat), but there's little stopping it from being backported
all the way to APIv11.

Grab a copy of the demo application (source available [here][4]) to try
it in action.

Primary Developer
=================
Philip Cohn-Cort

Credit:
-------

This project was inspired by the work of others in the Android community,
especially [ViewPagerIndicator][5].


 [1]: https://developer.android.com/reference/android/support/v4/view/ViewPager.html
 [2]: https://github.com/eccyan/SpinningTabStrip
 [3]: src/main/java/com/fuzz/indicator/CutoutViewIndicator.java#L392
 [4]: https://github.com/fuzz-productions/CutoutViewIndicator-Demo
 [5]: https://github.com/JakeWharton/ViewPagerIndicator