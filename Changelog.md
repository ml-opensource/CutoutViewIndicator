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
[#22]: https://github.com/fuzz-productions/CutoutViewIndicator/issues/22
