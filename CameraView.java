/**
 * Created by Mohammed Alimoor on 1/14/17  12:14 PM.
 * ameral.java@gmail   00970598072007
 */
import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Mohammed Alimoor on 1/14/17  12:14 PM.
 * ameral.java@gmail   00970598072007
 */
public class CameraView extends SurfaceView implements SurfaceHolder.Callback {

    Camera camera;
    SurfaceHolder surfaceHolder;

    Camera.PictureCallback rawCallback;
    Camera.ShutterCallback shutterCallback;
    Camera.PictureCallback jpegCallback;
public int defaultCamera = Camera.CameraInfo.CAMERA_FACING_FRONT;
    public CameraView(Context context) {
        super(context);
        ini();
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ini();
    }

    public CameraView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ini();
    }




    public void takePicture(final SaveImageListener saveFile) {

        camera.takePicture(shutterCallback, rawCallback, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                FileOutputStream outStream = null;
                try {

                    String Path = String.format(android.os.Environment.getExternalStorageDirectory() + File.separator + "%d.jpg", System.currentTimeMillis());
                    outStream = new FileOutputStream(Path);
                    outStream.write(data);
                    outStream.close();
                    Log.d("Log", "onPictureTaken - wrote bytes: " + data.length);
                    // Path;
                    saveFile.saveFile(Path);
                } catch (FileNotFoundException e) {
                    saveFile.error(e);

                    e.printStackTrace();
                } catch (IOException e) {
                    saveFile.error(e);

                    e.printStackTrace();
                } finally {
                }

            }
        });

    }

    public interface SaveImageListener {
        public void saveFile(String path);

        public void error(Exception e);

    }

    public void ini() {
        surfaceHolder = this.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        rawCallback = new Camera.PictureCallback() {
            public void onPictureTaken(byte[] data, Camera camera) {
                System.out.println("onPictureTaken - raw");
            }
        };
        shutterCallback = new Camera.ShutterCallback() {
            @Override
            public void onShutter() {

            }
        };
        jpegCallback = new Camera.PictureCallback() {
            public void onPictureTaken(byte[] data, Camera camera) {
                FileOutputStream outStream = null;
                try {

                    String Path = String.format(android.os.Environment.getExternalStorageDirectory() + File.separator + "%d.jpg", System.currentTimeMillis());
                    outStream = new FileOutputStream(Path);
                    outStream.write(data);
                    outStream.close();
                    Log.d("Log", "onPictureTaken - wrote bytes: " + data.length);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                }
                Toast.makeText(getContext(), "Picture Saved", Toast.LENGTH_LONG).show();
                refreshCamera();
            }

        };


    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        int currentCameraId = 1;
        if (Camera.getNumberOfCameras() == 1) {
            return;
            // useOtherCamera.setVisibility(View.INVISIBLE);
        }

//        if(currentCameraId == Camera.CameraInfo.CAMERA_FACING_BACK){
//            currentCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
//           // Log.e()
//        }
//        else {
//            currentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
//        }
        currentCameraId =defaultCamera;// Camera.CameraInfo.CAMERA_FACING_BACK;

        try {

            //camera = Camera.open();
            camera = Camera.open(currentCameraId);

        } catch (RuntimeException e) {
            System.err.println(e);
            return;
        }


        Camera.Parameters param;
        param = camera.getParameters();

        if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            param.set("orientation", "portrait");
            camera.setDisplayOrientation(90);
        }
        //param.set("camera-id", 3);
        //param.setRotation(180);
        camera.setParameters(param);
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (Exception e) {
            System.err.println(e);
            return;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        refreshCamera();
    }


    public void refreshCamera() {
        if (surfaceHolder.getSurface() == null) {
            return;
        }
        try {
            camera.stopPreview();
        } catch (Exception e) {

        }
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (Exception e) {

        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // stop preview and release camera
        camera.stopPreview();
        camera.release();
        camera = null;
    }
}
