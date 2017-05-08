package ike.com.permessonrequestdemo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

import ike.com.permessonrequestdemo.utils.FileUtils;

/**
 * android7.0拍照，文件读取权限适配
 * Created by dell on 2017/5/8.
 */

public class Android7Canmera extends AppCompatActivity {
    private String fileName;
    private String mFilePath;
    private ImageView iv;
    private String PIC_PATH="pic_path";
    private RecyclerView rl;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_7);
        getbundle(savedInstanceState);
        iv= (ImageView) findViewById(R.id.iv);
        FileUtils.init();
        mFilePath = FileUtils.getFileDir() + File.separator;
        rl= (RecyclerView) findViewById(R.id.rl);
    }
    public void take_photo(View view){
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File path = new File(mFilePath);
            if (!path.exists()) {
                path.mkdirs();
            }
            File file = new File(path, System.currentTimeMillis() + ".jpg");
            if (file.exists()) {
                file.delete();
            }
            fileName=file.getAbsolutePath();
            Log.e("main","fileName:"+fileName);
            FileUtils.startActionCapture(this,file,1);
        } else {
            Log.e("main","sdcard not exists");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                Glide.with(this).load(new File(fileName)).into(iv);
                initReclyer();
                break;
        }
    }

    public void getbundle(Bundle outState) {
        if (outState!=null){
            fileName=outState.getString(PIC_PATH);
        }
    }

    public void setbundle(Bundle outState) {
        if (outState!=null){
            outState.putString(PIC_PATH,fileName);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        setbundle(outState);
        super.onSaveInstanceState(outState, outPersistentState);
    }
    public void initReclyer(){
        GridLayoutManager manager=new GridLayoutManager(this,4);
        manager.setOrientation(GridLayoutManager.VERTICAL);
        rl.setLayoutManager(manager);
        rl.addItemDecoration(new SpaceItemDecoration(10,4));
        rl.setAdapter(new Addaper());
    }
    public class Addaper extends RecyclerView.Adapter{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(View.inflate(parent.getContext(),R.layout.item_layout,null));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ViewHolder mHolder= (ViewHolder) holder;
            Glide.with(Android7Canmera.this).load(new File(fileName)).into(mHolder.iv);

        }

        @Override
        public int getItemCount() {
            return 4;
        }

    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView iv;
        public ViewHolder(View itemView) {
            super(itemView);
            iv= (ImageView) itemView.findViewById(R.id.iv);
        }
    }

}

