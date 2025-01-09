# fork of hhunaid/react-native-image-crop-tools with changes related to android

## Usage example

```tsx
import { CropView } from 'react-native-image-crop-tools-android';
<CropView
  sourceUrl={uri}
  style={styles.cropView}
  ref={cropViewRef}
  onImageCrop={(res) => console.warn(res)}
  // See src/crop-view-component.tsx for props and default values
  androidCropImageOptions={{
    maxZoom: 1, 
    cropShape: "OVAL", // Circular cropping window
    fixAspectRatio: true,
    aspectRatioX: 1,
    aspectRatioY: 1,
    minCropResultWidth: 1024, // Prevents cropped image from getting smaller than 1024px,
    minCropResultHeight: 1024,// Prevents cropped image from getting smaller than 1024px,
  }}
/>
```

## Why fork this library?
1. The original library allowed users to zoom into the image multiple times, which caused the image to become very small and blurry.
2. The internal library used for android is now maintained by a different author.

## Difference from original library
1. Updated the android-image-cropper library. android-image-cropper is now maintained by a different author.
2. Added `androidCropImageOptions` prop
   This prop lets you customize maxZoom, minCropResultWidth, minCropResultHeight and other things. See `src/crop-view-component.tsx` for details.
3. `aspectRatio` and `keepAspectRatio` props are ignored by android implementation, you can specify them as part of `androidCropImageOptions`

## Opening in android studio
- Usually we have example app that can be opened in android studio. But the original author couldn't share the Example react native app because it was part of a closed source project. 
- So for now during developement uncomment 26:29 of build.gradle, and then sync the project. 

---------------------------------

# react-native-image-crop-tools

## Previews
<p float="left">
  <img src="https://github.com/hhunaid/react-native-image-crop-tools/blob/master/previews/android-preview.gif?raw=true" width="150" />
  <img src="https://github.com/hhunaid/react-native-image-crop-tools/blob/master/previews/ios-preview.gif?raw=true" width="150" /> 
</p>

## Getting started

`$ yarn add react-native-image-crop-tools`

### Automatic installation

Only RN > 0.61.x is supported.
- Android: Installation is automatic.
- iOS: Run `pod install`in `ios` folder.
   
### Why another cropping library?

Most cropping tools available for RN are usually wrappers over popular native tools which itself isn't a bad thing. But this means you are stuck with their UI and feature set. The ones made in RN are not the most optimized and correct tools.

## Features

1. Native views. Which means performance even on low end devices.
2. You can embed the view into you own UI. It's not very customizable (yet)
3. Change and lock/unlock aspect ratio on the fly (This is the main reason I am making this library)

# NOTE

This library is not supposed to work directly with remote images. There are very few usecases for that. You need to provide a sourceUrl string for a local file which you can obtain from image pickers or by downloading a remote file with rn-fetch-blob

## Usage
```javascript
import { CropView } from 'react-native-image-crop-tools';

        <CropView
          sourceUrl={uri}
          style={styles.cropView}
          ref={cropViewRef}
          onImageCrop={(res) => console.warn(res)}
          keepAspectRatio
          aspectRatio={{width: 16, height: 9}}
        />
```

Two methods are exposed on the ref you can use them as follows

```javascript
  this.cropViewRef.saveImage(true, 90 // image quality percentage)
  this.cropViewRef.rotateImage(true // true for clockwise, false for counterclockwise)
```

### Props

| Name | Description | Default |
| ---- | ----------- | ------- |
| sourceUrl | URL of the source image | `null` |
| aspectRatio | Aspect ratio of the cropped image | `null` |
| keepAspectRatio | Locks the aspect ratio to given aspect ratio | `false` |
| iosDimensionSwapEnabled | (iOS Only) Swaps the width and height of the crop rectangle upon rotation | `false` |

#### TODO:

- [x] Add screenshots
- [x] Support transparency
- [ ] Add access to prebuilt UI for those who want to use it.
