package org.rmj.guanzongroup.useraccount.Activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.rmj.g3appdriver.etc.AppConstants;
import org.rmj.g3appdriver.etc.ImageFileHandler;
import org.rmj.g3appdriver.etc.MessageBox;
import org.rmj.g3appdriver.lib.Account.AccountInfo;
import org.rmj.g3appdriver.lib.Account.Obj.PhotoDetail;
import org.rmj.g3appdriver.utils.Dialogs.Dialog_Loading;
import org.rmj.guanzongroup.useraccount.R;
import org.rmj.guanzongroup.useraccount.ViewModel.VMUserVerification;

import java.io.IOException;
import java.util.Objects;

public class Activity_ProfileVerification extends AppCompatActivity {

    private VMUserVerification mViewModel;

    private Toolbar toolbar;
    private ImageView imageView;

    private String psPath, psFleNme;
    private PhotoDetail poPhoto;
    private Dialog_Loading poLoading;
    private MessageBox poDialogx;

    private final ActivityResultLauncher<Intent> poCamera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if(result != null) {
            try {
                if(result.getResultCode() == RESULT_OK) {
                    imageView.setImageBitmap(ImageFileHandler.getImagePreview(psPath));
                    initializePhotoDetail();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    });

    private final ActivityResultLauncher<Intent> poFileChooser = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode()
                        == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    // do your operation from here....
                    if (data != null
                            && data.getData() != null) {
                        Uri selectedImageUri = data.getData();
                        Bitmap selectedImageBitmap;
                        try {
                            selectedImageBitmap
                                    = MediaStore.Images.Media.getBitmap(
                                    Activity_ProfileVerification.this.getContentResolver(),
                                    selectedImageUri);
                            imageView.setImageBitmap(selectedImageBitmap);
                            initializePhotoDetail();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile_verification);

        mViewModel = new ViewModelProvider(Activity_ProfileVerification.this).get(VMUserVerification.class);
        poPhoto = new PhotoDetail();
        poLoading = new Dialog_Loading(Activity_ProfileVerification.this);
        poDialogx = new MessageBox(Activity_ProfileVerification.this);

        poDialogx.initDialog();

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Selfie Verification");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        imageView = findViewById(R.id.img_profile);

        findViewById(R.id.btn_next).setOnClickListener(v -> ImageFileHandler.InitializeFileManager(intent -> poFileChooser.launch(intent)));

        findViewById(R.id.btnCapture).setOnClickListener(v -> {
            ImageFileHandler.InitializeFrontCamera(Activity_ProfileVerification.this, (intent, path, fileName) -> {
                psPath = path;
                psFleNme = fileName;
                poCamera.launch(intent);
            });
        });

        findViewById(R.id.btn_next).setOnClickListener(v -> {
            if(!poPhoto.isDataValid()){
                Toast.makeText(Activity_ProfileVerification.this, "Please take a selfie or select from your gallery", Toast.LENGTH_SHORT).show();
            } else {
                mViewModel.UploadForVerification(poPhoto, new VMUserVerification.OnSelfieVerificationSubmitCallback() {
                    @Override
                    public void OnLoad() {
                        poLoading.initDialog("Selfie Verification", "Uploading selfie verification. Please wait...");
                        poLoading.show();
                    }

                    @Override
                    public void OnSuccess(String message) {
                        poLoading.dismiss();

                        poDialogx.setIcon(R.drawable.ic_baseline_message_24);
                        poDialogx.setTitle("Account Details");
                        poDialogx.setMessage(message);
                        poDialogx.setPositiveButton("Dismiss", new MessageBox.DialogButton() {
                            @Override
                            public void OnButtonClick(View view, AlertDialog dialog) {
                                dialog.dismiss();
                            }
                        });

                        poDialogx.show();

                    }

                    @Override
                    public void OnFailed(String message) {
                        poLoading.dismiss();

                        poDialogx.setIcon(R.drawable.baseline_error_24);
                        poDialogx.setTitle("Account Details");
                        poDialogx.setMessage(message);
                        poDialogx.setPositiveButton("Dismiss", new MessageBox.DialogButton() {
                            @Override
                            public void OnButtonClick(View view, AlertDialog dialog) {
                                dialog.dismiss();
                            }
                        });

                        poDialogx.show();

                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initializePhotoDetail(){
        String lsUserID = new AccountInfo(Activity_ProfileVerification.this).getUserID();
        poPhoto.setCaptured(new AppConstants().DATE_MODIFIED);
        poPhoto.setFileCode(AppConstants.DOC_FILE_APPLICANT_PHOTO);
        poPhoto.setSourceCD(AppConstants.SOURCE_CODE);
        poPhoto.setDtlSrcNo(lsUserID);
        poPhoto.setSourceNo(lsUserID);
        poPhoto.setImageNme(psFleNme);
        poPhoto.setFileLoct(psPath);
    }
}