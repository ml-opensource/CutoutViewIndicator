# [0.8.1]
## Support latest stable android APIs

### Library changes:
* Wear project has been updated to build against the 2.0.0 release
* Updated SDK target: API 25

# [0.8.0]
## Clean codebase and streamline TextClippedImageView

### Library changes:
* New interface on `CutoutViewIndicator`: `ProxyReference` ([#56])
* New package '.indicator.proxy' for code related to `StateProxy` ([#56])
* New package '.indicator.cell' for `CutoutCell` and such ([#56])
* Removed method: `CutoutViewLayoutParams::from` ([#56])
* New inner class: `IntegratedDataSetObserver` ([#56])
* Greatly augment `TextClippedImageView`: ([#64])
  * Paths can now be read directly from `TCIV` attributes
  * `TCIV` automatically resizes to `wrap_content`
* `CutoutViewLayoutParams` of inflated views inherit attributes ([#66])
* Updated gradle: 2.14.1 -> 3.2.1
* Update copyright years to 2017 (where relevant)

### Mobile app changes:
* Remove field `cvi` from `RecyclerStateProxy` ([#56])
* Generify `InlineAdapter`
* `fragment_horizontal_inline.xml` now demonstrates `TCIV` usage
* New `StateProxy` implementation: `LoopingProxy` ([#68])
* New fragment: `AutomaticLogoFragment` ([#68])
* Update copyright years to 2017 (where relevant)

# [0.7.0]
## Move emphasis from CutoutCellGenerator to CutoutCell

### Library changes:
* New CutoutCell: FrameAwareCutoutImageCell ([#49])
* Fixed bug: ColorDrawable foregrounds weren't offset ([#53])
* Changed contract: mirror position with CutoutViewLayoutParams ([#49])
* New generator: `SequentialCellGenerator` ([#50])
* New view: `TextClippedImageView` ([#50])
* New feature: multiple offset animations at once ([#51])

# [0.6.0]
## Rename classes

### Library changes:
* Rename: `LayeredView` -> `CutoutCell` ([#42])
* Rename: `LayeredViewGenerator` -> `CutoutCellGenerator` ([#42])
* Rename: `IndicatorViewHolder` -> `TypicalCutoutCell` ([#42])
* Rename: `BoldTextViewGenerator` -> `BoldTextCellGenerator` ([#42])
* Rename: `holder` -> `cell` ([#42])
* Allow xml definition of `LayeredViewGenerator` implementation ([#38])
* Fixed bug: `CutoutViewIndicator` javadoc encoding looks bad ([#42])

# [0.5.1]
## Correct javadoc problems

### Library changes:
* Fixed bug: invalid javadoc references ([#40])

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
[#38]: https://github.com/fuzz-productions/CutoutViewIndicator/issues/38
[#40]: https://github.com/fuzz-productions/CutoutViewIndicator/issues/40
[#42]: https://github.com/fuzz-productions/CutoutViewIndicator/issues/42
[#49]: https://github.com/fuzz-productions/CutoutViewIndicator/issues/49
[#50]: https://github.com/fuzz-productions/CutoutViewIndicator/issues/50
[#51]: https://github.com/fuzz-productions/CutoutViewIndicator/issues/51
[#53]: https://github.com/fuzz-productions/CutoutViewIndicator/issues/53
[#56]: https://github.com/fuzz-productions/CutoutViewIndicator/issues/56
[#64]: https://github.com/fuzz-productions/CutoutViewIndicator/issues/64
[#66]: https://github.com/fuzz-productions/CutoutViewIndicator/issues/66
[#68]: https://github.com/fuzz-productions/CutoutViewIndicator/issues/68
