# CameraView
CameraView for android 
###very easy to use  just  add CameraView in xml
![alt tag](https://raw.githubusercontent.com/MohammedAlimoor/CameraView/master/Screenshot_2017-01-14-13-52-01.png)
# how to use
 ````xml
     <your.packge.name.CameraView
        android:id="@+id/cameraView"
        android:layout_width="250dp"
        android:layout_height="300dp"
        android:layout_margin="4dp"
        />
 ````
## Take Picture
  ````java
    ((CameraView) findViewById(R.id.cameraView)).takePicture(new CameraView.SaveImageListener() {
            @Override
            public void saveFile(String path) {
                Toast.makeText(MainActivity.this, "Path" + path, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void error(Exception e) {
                Toast.makeText(MainActivity.this, "Error" + e.toString(), Toast.LENGTH_SHORT).show();

            }
        });
        
  ````

