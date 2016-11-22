# [0.5.0]

## Make dev work easier
### Library changes:
* New CVI view support: inflated XML attributes ([#35])
* New CVI view support: views added via `addView` ([#35])
* New warning: ImageView with ScaleType not equal to MATRIX ([#36])
* New warning: CutoutViewLayoutParams without any drawable ids ([#36])
* New overload: CVI::showOffsetIndicator(int, IndicatorOffsetEvent) ([#33])
* Changed contract: MigratorySpan now uses ints, not floats ([#33])
* Rename: percentage -> fraction ([#33])

### Mobile app changes:
* New fragment: InlineViewPagerIndicatorFragment ([#15])
* Fixed bug: limit PlainActivity to one fragment at a time
* New interface: ViewProvidingStateProxy ([#33])

# [0.4.0]
## Demonstrate usage of library with RecyclerView

### Library changes:
* Changed method: StateProxy::resendPositionInfo ([#30])
* Changed method: CVI::showOffsetIndicator is now public
* Fixed bug: ClippedImageView distorts foreground drawables
* New method: CutoutViewLayoutParams::getViewHolder()
* New method: CutoutViewLayoutParams::setViewHolder(LayeredView)

### Mobile app changes:
* Tests: run continuous integration on Travis ([#21])
* New fragment: VerticalDotsFragment ([#15])
* New fragment: NarrowerSegmentsFragment ([#15])
* New generator: ProportionalImageViewGenerator ([#15])

# [0.3.0]
## Reduce library dependency on ViewPager

### Library changes:
* Changed dependency: `support-v4` instead of `appcompat-v7`
* Fixed bug: depend less strongly on ViewPager ([#12], [#22])
* Fixed bug: design-time now looks much closer to runtime ([#10])
* Tests: added some basic tests ([#13], [#14])

### Mobile app changes:
* New CutoutViewLayoutParams: Bitmap over Bitmap
* New CutoutViewLayoutParams: Vector over Shape


# [0.2.0]
## First public release of CutoutViewIndicator.

### Library changes:
* New generator: ClippedImageViewGenerator ([#1])
* New generator: TextViewGenerator ([#2])
* Changed dependency: SparseArray is used instead of SparseArrayCompat
* Changed dependency: minSDKVersion dropped from 15 to 14

### Mobile app changes:
* Allow runtime configuration of CVI orientation
* Allow runtime configuration of CVI generator
* New generator: BoldTextViewGenerator


[#1]: https://github.com/fuzz-productions/CutoutViewIndicator/issues/1
[#2]: https://github.com/fuzz-productions/CutoutViewIndicator/issues/2
[#10]: https://github.com/fuzz-productions/CutoutViewIndicator/issues/10
[#12]: https://github.com/fuzz-productions/CutoutViewIndicator/issues/12
[#13]: https://github.com/fuzz-productions/CutoutViewIndicator/issues/13
[#14]: https://github.com/fuzz-productions/CutoutViewIndicator/issues/14
[#15]: https://github.com/fuzz-productions/CutoutViewIndicator/issues/15
[#21]: https://github.com/fuzz-productions/CutoutViewIndicator/issues/21
[#22]: https://github.com/fuzz-productions/CutoutViewIndicator/issues/22
[#30]: https://github.com/fuzz-productions/CutoutViewIndicator/issues/30
[#33]: https://github.com/fuzz-productions/CutoutViewIndicator/issues/33
[#35]: https://github.com/fuzz-productions/CutoutViewIndicator/issues/35
[#36]: https://github.com/fuzz-productions/CutoutViewIndicator/issues/36
