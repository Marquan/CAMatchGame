// Generated by view binder compiler. Do not edit!
package iss.nus.edu.sg.ca.matchgame.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentContainerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import iss.nus.edu.sg.ca.matchgame.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityPlayBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final FragmentContainerView forAds;

  @NonNull
  public final FragmentContainerView forGame;

  @NonNull
  public final LinearLayout mainPlay;

  private ActivityPlayBinding(@NonNull LinearLayout rootView, @NonNull FragmentContainerView forAds,
      @NonNull FragmentContainerView forGame, @NonNull LinearLayout mainPlay) {
    this.rootView = rootView;
    this.forAds = forAds;
    this.forGame = forGame;
    this.mainPlay = mainPlay;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityPlayBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityPlayBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_play, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityPlayBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.for_ads;
      FragmentContainerView forAds = ViewBindings.findChildViewById(rootView, id);
      if (forAds == null) {
        break missingId;
      }

      id = R.id.for_game;
      FragmentContainerView forGame = ViewBindings.findChildViewById(rootView, id);
      if (forGame == null) {
        break missingId;
      }

      LinearLayout mainPlay = (LinearLayout) rootView;

      return new ActivityPlayBinding((LinearLayout) rootView, forAds, forGame, mainPlay);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
