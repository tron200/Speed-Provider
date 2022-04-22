// Generated code from Butter Knife. Do not modify!
package com.speed.provider.ui.activities;

import android.view.View;
import android.widget.ProgressBar;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.speed.provider.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SplashScreen_ViewBinding implements Unbinder {
  private SplashScreen target;

  @UiThread
  public SplashScreen_ViewBinding(SplashScreen target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public SplashScreen_ViewBinding(SplashScreen target, View source) {
    this.target = target;

    target.progressBar = Utils.findRequiredViewAsType(source, R.id.progressBar, "field 'progressBar'", ProgressBar.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    SplashScreen target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.progressBar = null;
  }
}
