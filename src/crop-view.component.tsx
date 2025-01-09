import React, { createRef } from 'react';
import {
  findNodeHandle,
  NativeSyntheticEvent,
  requireNativeComponent,
  StyleProp,
  UIManager,
  ViewStyle,
} from 'react-native';

const RCTCropView = requireNativeComponent('CropView');

type Response = {
  uri: string;
  width: number;
  height: number;
  x: number;
  y: number;
};

export type AndroidCropImageOptions = {
  /**
   * Shape of the cropping window.
   * @default "RECTANGLE"
   */
  cropShape?: 'RECTANGLE' | 'OVAL' | 'RECTANGLE_VERTICAL_ONLY' | 'RECTANGLE_HORIZONTAL_ONLY';
  /**
   * Shape of the crop window corners.
   * @default "RECTANGLE"
   */
  cornerShape?: 'RECTANGLE' | 'OVAL';
  /**
   * Radius of the crop window corners in pixels.
   * float
   * @default 10
   */
  cropCornerRadius?: number;
  /**
   * An edge of the crop window will snap to the corresponding edge of a specified bounding box when the crop window edge is less than or equal to this distance away from the bounding box edge.
   * float
   * @default 3
   */
  snapRadius?: number;

  /**
   * float
   * The radius of the touchable area around the handle.
   * @default 24
   */
  touchRadius?: number;

  /**
   * Scale type of the image in the crop view.
   * @default "FIT_CENTER"
   */
  scaleType?: 'FIT_CENTER' | 'CENTER' | 'CENTER_CROP' | 'CENTER_INSIDE';

  /**
   * @default true
   */
  autoZoomEnabled?: boolean;

  /**
   * Multitouch allows to resize and drag the cropping window at the same time.
   * @default false
   */
  multiTouchEnabled?: boolean;
  /**
   * If the crop window can be moved by dragging the crop window in the center.
   * @default true
   */
  centerMoveEnabled?: boolean;
  /**
   * If you are allowed to change the crop window by resizing it.
   * @default true
   */
  canChangeCropWindow?: boolean;

  /**
   * Integer
   * @default 4
   */
  maxZoom?: number;

  /**
   *
   * @default false
   */
  fixAspectRatio?: boolean;

  /**
   * Integer
   * @default 1
   */
  aspectRatioX?: number;

  /**
   * Integer
   * @default 1
   */
  aspectRatioY?: number;

  /**
   * Integer
   * @default 42
   */
  minCropWindowWidth?: number;

  /**
   * Integer
   * @default 42
   */
  minCropWindowHeight?: number;

  /**
   * Integer
   * @default 40
   */
  minCropResultWidth?: number;

  /**
   * Integer
   * @default 40
   */
  minCropResultHeight?: number;

  /**
   * Integer
   * @default 99999
   */
  maxCropResultWidth?: number;

  /**
   * Integer
   * @default 99999
   */
  maxCropResultHeight?: number;
};

type Props = {
  sourceUrl: string;
  style?: StyleProp<ViewStyle>;
  onImageCrop?: (res: Response) => void;
  /**
   * Ignored on android use androidCropImageOptions instead
   */
  keepAspectRatio?: boolean;
  /**
   * Ignored on android use androidCropImageOptions instead
   */
  aspectRatio?: { width: number; height: number };
  iosDimensionSwapEnabled?: boolean;
  androidCropImageOptions?: AndroidCropImageOptions;
};

class CropView extends React.PureComponent<Props> {
  public static defaultProps = {
    keepAspectRatio: false,
    iosDimensionSwapEnabled: false,
  };

  private viewRef = createRef<any>();

  public saveImage = (preserveTransparency: boolean = true, quality: number = 90) => {
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this.viewRef.current!),
      UIManager.getViewManagerConfig('CropView').Commands.saveImage,
      [preserveTransparency, quality]
    );
  };

  public rotateImage = (clockwise: boolean = true) => {
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this.viewRef.current!),
      UIManager.getViewManagerConfig('CropView').Commands.rotateImage,
      [clockwise]
    );
  };

  public render() {
    const { onImageCrop, androidCropImageOptions, aspectRatio, ...rest } = this.props;

    return (
      <RCTCropView
        ref={this.viewRef}
        cropImageOptions={androidCropImageOptions}
        cropAspectRatio={aspectRatio}
        onImageSaved={(event: NativeSyntheticEvent<Response>) => {
          onImageCrop!(event.nativeEvent);
        }}
        {...rest}
      />
    );
  }
}

export default CropView;
