package groups.kma.editappver2;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import groups.kma.editappver2.adapter.ListGrid;
import groups.kma.editappver2.adapter.ListMenu;
import groups.kma.editappver2.model.MenuAction;

public class MainActivity extends AppCompatActivity implements ListMenu.CallbackInterface,
        ListGrid.CallbackInterfaceDialog {
    private ImageView img1,img2,img3;
    private FrameLayout main_frame,frame1,frame2,frame3;
    private float height_f1,width_f23,height_f23,setX3,setY23;
    //height_f1= height frame1, width_f23= width frame2,frame3, height_f23 = height frame2,frame3,
    //setX3 = setX frame3, setY23 = setY frame 2,frame3
    float listLastChange[]=new float[5]; //listen 5 variable above
    int lastProgress=-1;

    private String lastImg; //image choosing
    public static final int PICK_IMAGE = 1;
    public static final int REQUEST_IMAGE = 200;
    float[] lastEvent = null;
    float d = 0f;
    float newRot = 0f;
    private boolean isZoomAndRotate;
    private boolean isOutSide;
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;
    private PointF start = new PointF();
    private PointF mid = new PointF();
    float oldDist = 1f;
    private float xCoOrdinate, yCoOrdinate;

    RecyclerView rvMenu;
    ArrayList<MenuAction> listItem;
    ListMenu adapter;
    ArrayList<Integer> listGrid;
    ListGrid adapterGrid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        addControls();

    }

    private void addEvents() {
        img1.setOnTouchListener(new View.OnTouchListener() {
            private long startTime=0;
            private long endTime=0;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startTime = event.getEventTime();
                        break;
                    case MotionEvent.ACTION_UP:
                        endTime = event.getEventTime();
                        if (endTime-startTime<100)
                        {
                            showPictureDialog("img1");
                        }
                        break;
                }
                v.getParent().requestDisallowInterceptTouchEvent(true); //specific to my project
                ImageView view = (ImageView) v;
                view.bringToFront();
                viewTransformation(view, event);
                return true;
            }
        });
        img2.setOnTouchListener(new View.OnTouchListener() {
            private long startTime=0;
            private long endTime=0;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startTime = event.getEventTime();
                        break;
                    case MotionEvent.ACTION_UP:
                        endTime = event.getEventTime();
                        if (endTime-startTime<100)
                        {
                            showPictureDialog("img2");
                        }
                        break;
                }
                v.getParent().requestDisallowInterceptTouchEvent(true); //specific to my project
                ImageView view = (ImageView) v;
                view.bringToFront();
                viewTransformation(view, event);
                return true;
            }
        });
        img3.setOnTouchListener(new View.OnTouchListener() {
            private long startTime=0;
            private long endTime=0;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startTime = event.getEventTime();
                        break;
                    case MotionEvent.ACTION_UP:
                        endTime = event.getEventTime();
                        if (endTime-startTime<100)
                        {
                            showPictureDialog("img3");
                        }
                        break;
                }
                v.getParent().requestDisallowInterceptTouchEvent(true); //specific to my project
                ImageView view = (ImageView) v;
                view.bringToFront();
                viewTransformation(view, event);
                return true;
            }
        });
    }
    private void addControls() {
        main_frame= this.<FrameLayout>findViewById(R.id.main_frame);
        drawImageView();

        rvMenu =(RecyclerView)findViewById(R.id.mnuButton);
        rvMenu.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager =new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL,false);
        rvMenu.setLayoutManager(layoutManager);
        listItem=prepareData();
        adapter = new ListMenu(this, listItem);
        rvMenu.setAdapter(adapter);
    }

    private ArrayList<MenuAction> prepareData() {
        ArrayList<MenuAction> list=new ArrayList<>();
        list.add(new MenuAction(R.drawable.grid,"Select Grid"));
        list.add(new MenuAction(R.drawable.border,"Border"));
        return list;
    }
    private void setDefaultXY(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        height_f1=240;
        width_f23=(width-48)/2;
        height_f23=500;
        setX3=(width-48)/2;
        setY23=240;
        listLastChange[0]=height_f1;
        listLastChange[1]=width_f23;
        listLastChange[2]=setY23;
        listLastChange[3]=(setX3);
        listLastChange[4]=setY23;

    }
    private void drawImageView() {
        setDefaultXY();
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT
                , (int) height_f1);
        frame1= new FrameLayout(this);
        frame1.setLayoutParams(params);

        img1=new ImageView(this);
        img1.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
        frame1.addView(img1);
        main_frame.addView(frame1);
        img1.setBackgroundResource(R.drawable.background);

        frame2=new FrameLayout(this);
        frame2.setLayoutParams(new FrameLayout.LayoutParams((int) width_f23, (int) height_f23));
        frame2.setY(setY23);
        img2=new ImageView(this);
        img2.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
        frame2.addView(img2);
        main_frame.addView(frame2);
        img2.setBackgroundColor(Color.YELLOW);

        frame3=new FrameLayout(this);
        frame3.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT
                , (int) height_f23));
        frame3.setX(setX3);
        frame3.setY(setY23);
        img3=new ImageView(this);
        img3.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
        frame3.addView(img3);
        Log.i("F",""+frame3.getWidth());
        Log.i("F",""+frame3.getX());
        main_frame.addView(frame3);
        img3.setBackgroundColor(Color.GREEN);
        addEvents();
    }
    private void showPictureDialog(String str) {
        if (str.equalsIgnoreCase("img1"))
        {
            lastImg = str;
        }
        if (str.equalsIgnoreCase("img2"))
        {
            lastImg=str;
        }
        if (str.equalsIgnoreCase("img3"))
        {
            lastImg=str;
        }
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItem = {"Select photo from gallery", "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                        choosePhotoFromGallery();
                        break;
                    case 1:
                        takePhotoFromCamera();
                        break;
                }
            }
        });
        pictureDialog.show();
    }
    private void takePhotoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,REQUEST_IMAGE);
    }
    private void choosePhotoFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), PICK_IMAGE);
    }
    private void viewTransformation(View view, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                xCoOrdinate = view.getX() - event.getRawX();
                yCoOrdinate = view.getY() - event.getRawY();

                start.set(event.getX(), event.getY());
                isOutSide = false;
                mode = DRAG;
                lastEvent = null;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                if (oldDist > 10f) {
                    midPoint(mid, event);
                    mode = ZOOM;
                }

                lastEvent = new float[4];
                lastEvent[0] = event.getX(0);
                lastEvent[1] = event.getX(1);
                lastEvent[2] = event.getY(0);
                lastEvent[3] = event.getY(1);
                d = rotation(event);
                break;
            case MotionEvent.ACTION_UP:
                isZoomAndRotate = false;
                if (mode == DRAG) {
                    float x = event.getX();
                    float y = event.getY();
                }
            case MotionEvent.ACTION_OUTSIDE:
                isOutSide = true;
                mode = NONE;
                lastEvent = null;
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                lastEvent = null;
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isOutSide) {
                    if (mode == DRAG) {
                        isZoomAndRotate = false;
                        view.animate().x(event.getRawX() + xCoOrdinate).y(event.getRawY() + yCoOrdinate).setDuration(0).start();
                    }
                    if (mode == ZOOM && event.getPointerCount() == 2) {
                        float newDist1 = spacing(event);
                        if (newDist1 > 10f) {
                            float scale = newDist1 / oldDist * view.getScaleX();
                            view.setScaleX(scale);
                            view.setScaleY(scale);
                        }
                        if (lastEvent != null) {
                            newRot = rotation(event);
                            view.setRotation((float) (view.getRotation() + (newRot - d)));
                        }
                    }
                }
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == PICK_IMAGE &&resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (lastImg.equalsIgnoreCase("img1")){
                        img1.setBackgroundResource(android.R.color.transparent);
                        img1.setImageBitmap(bitmap);
                        img1.setScaleX((float) 2.5);
                        img1.setScaleY((float) 2.5);
                    }
                    if (lastImg.equalsIgnoreCase("img2")){
                        img2.setBackgroundResource(android.R.color.transparent);
                        img2.setImageBitmap(bitmap);
                        img2.setScaleX((float) 2.5);
                        img2.setScaleY((float) 2.5);
                    }
                    if (lastImg.equalsIgnoreCase("img3")){
                        img3.setBackgroundResource(android.R.color.transparent);
                        img3.setImageBitmap(bitmap);
                        img3.setScaleX((float) 2.5);
                        img3.setScaleY((float) 2.5);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == REQUEST_IMAGE &&resultCode == Activity.RESULT_OK) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            if (lastImg.equalsIgnoreCase("img1")){
                img1.setScaleType(ImageView.ScaleType.FIT_CENTER);
                img1.setImageBitmap(thumbnail);
                img1.setScaleX((float) 2.5);
                img1.setScaleY((float) 2.5);
            }
            if (lastImg.equalsIgnoreCase("img2")){
                img2.setScaleType(ImageView.ScaleType.FIT_CENTER);
                img2.setImageBitmap(thumbnail);
                img2.setScaleX((float) 2.5);
                img2.setScaleY((float) 2.5);
            }if (lastImg.equalsIgnoreCase("img3")){
                img3.setScaleType(ImageView.ScaleType.FIT_CENTER);
                img3.setImageBitmap(thumbnail);
                img3.setScaleX((float) 2.5);
                img3.setScaleY((float) 2.5);
            }

        }
        setTitle("Edit image");
    }
    private float rotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (int) Math.sqrt(x * x + y * y);
    }
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    @Override
    public void onHandleSelection(int position) {
        switch (position){
            case 0:
                createGridDialog();
                break;
            case 1:
                createBorderDialog();
                break;
            case 2:

                break;
        }
    }

    private void createGridDialog() {
        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = this.getLayoutInflater().inflate(R.layout.grid_dialog, null);
        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.show();
        Button btnCancel= sheetView.<Button>findViewById(R.id.btnCancel);
        Button btnOk= sheetView.<Button>findViewById(R.id.btnOk);
        RecyclerView rvGrid= sheetView.<RecyclerView>findViewById(R.id.rvGrid);
        rvGrid.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager =new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL,false);
        rvGrid.setLayoutManager(layoutManager);
        listGrid=prepareGridData();
        adapterGrid = new ListGrid(this, listGrid);
        rvGrid.setAdapter(adapterGrid);
    }

    private ArrayList<Integer> prepareGridData() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(R.drawable.mnu3_hor);
        list.add(R.drawable.mnu3_ver);
        list.add(R.drawable.mnu3_cus1);
        return list;
    }

    private void createBorderDialog() {
        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = this.getLayoutInflater().inflate(R.layout.bottom_dialog, null);
        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.show();
        Button btnCancel= sheetView.<Button>findViewById(R.id.btnCancel);
        Button btnOk= sheetView.<Button>findViewById(R.id.btnOk);
        final SeekBar bar = sheetView.<SeekBar>findViewById(R.id.seekBar);
        if (lastProgress >=0){
            bar.setProgress(lastProgress);
        }else {
            bar.setProgress(0);
        }
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastChange();
                mBottomSheetDialog.dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listLastChange[0]=frame1.getHeight();
                listLastChange[1]=frame2.getWidth();
                listLastChange[2]=(frame2.getY());
                listLastChange[3]=(frame3.getX());
                listLastChange[4]=(frame3.getY());
                lastProgress=bar.getProgress();
                mBottomSheetDialog.dismiss();
            }
        });
        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                changeBorder(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

    }

    private void lastChange() {
        if (listLastChange!=null){
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) frame1.getLayoutParams();
            // Changes the height and width to the specified *pixels*
            params.height =Math.round(listLastChange[0]);
            frame1.setLayoutParams(params);

            FrameLayout.LayoutParams params1 = (FrameLayout.LayoutParams) frame2.getLayoutParams();
            params1.width=Math.round(listLastChange[1]);
            frame2.setY(Math.round(listLastChange[2]));
            frame2.setLayoutParams(params1);

            FrameLayout.LayoutParams params2 = (FrameLayout.LayoutParams) frame3.getLayoutParams();
            frame3.setX(Math.round(listLastChange[3]));
            frame3.setY(Math.round(listLastChange[4]));
            frame3.setLayoutParams(params1);
        }else {
            setDefaultXY();
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) frame1.getLayoutParams();
            // Changes the height and width to the specified *pixels*
            params.height =Math.round(listLastChange[0]);
            frame1.setLayoutParams(params);

            FrameLayout.LayoutParams params1 = (FrameLayout.LayoutParams) frame2.getLayoutParams();
            params1.width=Math.round(listLastChange[1]);
            frame2.setY(Math.round(listLastChange[2]));
            frame2.setLayoutParams(params1);

            frame3.setX(Math.round(listLastChange[3]));
            frame3.setY(Math.round(listLastChange[4]));

        }
    }

    private void changeBorder(int progress) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) frame1.getLayoutParams();
        // Changes the height and width to the specified *pixels*
        params.height = (int) (height_f1-0.1*progress);
        frame1.setLayoutParams(params);

        FrameLayout.LayoutParams params1 = (FrameLayout.LayoutParams) frame2.getLayoutParams();
        params1.width= (int) (width_f23-0.1*progress);
        frame2.setY((float) (240+0.1*progress));
        frame2.setLayoutParams(params1);

        frame3.setX((float) (setX3+0.1*progress));
        frame3.setY((float) (240+0.1*progress));

    }

    @Override
    public void onSelection(int position) {
        switch (position){
            case 0:
                Toast.makeText(getApplicationContext(),"Comming soon",Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(getApplicationContext(),"Comming soon",Toast.LENGTH_SHORT).show();
                break;
            case 2:
                main_frame.removeAllViews();
                drawImageView();
                break;
        }
    }
}
