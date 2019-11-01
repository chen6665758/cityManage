package com.cg.citymanage;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cg.citymanage.untils.PermissionUntil;

public class TextActivity extends BaseActivity {

    private Button btn_perm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        initControls();
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_text);
    }

    /**
     * 初始化控件
     */
    private void initControls()
    {
        btn_perm = (Button)findViewById(R.id.btn_perm);
        btn_perm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PermissionUntil.checkGalleryPermission(mContext,TextActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE))
                {
                    Toast.makeText(mContext,"权限开通可以使用",Toast.LENGTH_LONG).show();
                }else{
                    PermissionUntil.JudgePermission(TextActivity.this,mContext,"您拒绝了对文件的读写，可能照片读取功能不可用",Manifest.permission.READ_EXTERNAL_STORAGE);
                }
            }
        });
    }

    @Override
    public void Jump_intent(Class<?> cla, Bundle bundle) {

    }
}
