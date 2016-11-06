// Generated code from Butter Knife. Do not modify!
package com.codepath.apps.mysimpletweets.activities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.codepath.apps.mysimpletweets.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class TweetDetailActivity_ViewBinding<T extends TweetDetailActivity> implements Unbinder {
  protected T target;

  @UiThread
  public TweetDetailActivity_ViewBinding(T target, View source) {
    this.target = target;

    target.body = Utils.findRequiredViewAsType(source, R.id.bodyTextView, "field 'body'", TextView.class);
    target.screenname = Utils.findRequiredViewAsType(source, R.id.screenNameTextView, "field 'screenname'", TextView.class);
    target.name = Utils.findRequiredViewAsType(source, R.id.nameText, "field 'name'", TextView.class);
    target.timestamp = Utils.findRequiredViewAsType(source, R.id.detailTimestamp, "field 'timestamp'", TextView.class);
    target.ivBackgroundImage = Utils.findRequiredViewAsType(source, R.id.detailImageView, "field 'ivBackgroundImage'", ImageView.class);
    target.ivProfileImage = Utils.findRequiredViewAsType(source, R.id.profileImageViewSmall, "field 'ivProfileImage'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.body = null;
    target.screenname = null;
    target.name = null;
    target.timestamp = null;
    target.ivBackgroundImage = null;
    target.ivProfileImage = null;

    this.target = null;
  }
}
