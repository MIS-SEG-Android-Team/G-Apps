package org.rmj.g3appdriver.etc;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import java.util.List;

public class FacebookShare {

    private final Context context;

    public FacebookShare(Context context){
        this.context = context;
    }

    public void OpenShareFeed(String Url){

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, Url);

        boolean facebookAppFound = false;

        List<ResolveInfo> matches = context.getPackageManager().queryIntentActivities(intent, 0);

        for (ResolveInfo info : matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith("com.facebook.katana")) {
                intent.setPackage(info.activityInfo.packageName);
                facebookAppFound = true;
                break;
            }
        }

        if (!facebookAppFound) {
            String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + Url;
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
        }

        startActivity(context, intent, null);
    }
}
