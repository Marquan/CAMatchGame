// Generated by view binder compiler. Do not edit!
package iss.nus.edu.sg.ca.matchgame.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import iss.nus.edu.sg.ca.matchgame.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentGameBinding implements ViewBinding {
  @NonNull
  private final FrameLayout rootView;

  @NonNull
  public final TextView attemptCount;

  @NonNull
  public final ImageButton imgCard01;

  @NonNull
  public final ImageButton imgCard02;

  @NonNull
  public final ImageButton imgCard03;

  @NonNull
  public final ImageButton imgCard04;

  @NonNull
  public final ImageButton imgCard05;

  @NonNull
  public final ImageButton imgCard06;

  @NonNull
  public final ImageButton imgCard07;

  @NonNull
  public final ImageButton imgCard08;

  @NonNull
  public final ImageButton imgCard09;

  @NonNull
  public final ImageButton imgCard10;

  @NonNull
  public final ImageButton imgCard11;

  @NonNull
  public final ImageButton imgCard12;

  @NonNull
  public final TextView matchCount;

  @NonNull
  public final TextView timeShow;

  private FragmentGameBinding(@NonNull FrameLayout rootView, @NonNull TextView attemptCount,
      @NonNull ImageButton imgCard01, @NonNull ImageButton imgCard02,
      @NonNull ImageButton imgCard03, @NonNull ImageButton imgCard04,
      @NonNull ImageButton imgCard05, @NonNull ImageButton imgCard06,
      @NonNull ImageButton imgCard07, @NonNull ImageButton imgCard08,
      @NonNull ImageButton imgCard09, @NonNull ImageButton imgCard10,
      @NonNull ImageButton imgCard11, @NonNull ImageButton imgCard12, @NonNull TextView matchCount,
      @NonNull TextView timeShow) {
    this.rootView = rootView;
    this.attemptCount = attemptCount;
    this.imgCard01 = imgCard01;
    this.imgCard02 = imgCard02;
    this.imgCard03 = imgCard03;
    this.imgCard04 = imgCard04;
    this.imgCard05 = imgCard05;
    this.imgCard06 = imgCard06;
    this.imgCard07 = imgCard07;
    this.imgCard08 = imgCard08;
    this.imgCard09 = imgCard09;
    this.imgCard10 = imgCard10;
    this.imgCard11 = imgCard11;
    this.imgCard12 = imgCard12;
    this.matchCount = matchCount;
    this.timeShow = timeShow;
  }

  @Override
  @NonNull
  public FrameLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentGameBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentGameBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_game, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentGameBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.attempt_count;
      TextView attemptCount = ViewBindings.findChildViewById(rootView, id);
      if (attemptCount == null) {
        break missingId;
      }

      id = R.id.img_card_01;
      ImageButton imgCard01 = ViewBindings.findChildViewById(rootView, id);
      if (imgCard01 == null) {
        break missingId;
      }

      id = R.id.img_card_02;
      ImageButton imgCard02 = ViewBindings.findChildViewById(rootView, id);
      if (imgCard02 == null) {
        break missingId;
      }

      id = R.id.img_card_03;
      ImageButton imgCard03 = ViewBindings.findChildViewById(rootView, id);
      if (imgCard03 == null) {
        break missingId;
      }

      id = R.id.img_card_04;
      ImageButton imgCard04 = ViewBindings.findChildViewById(rootView, id);
      if (imgCard04 == null) {
        break missingId;
      }

      id = R.id.img_card_05;
      ImageButton imgCard05 = ViewBindings.findChildViewById(rootView, id);
      if (imgCard05 == null) {
        break missingId;
      }

      id = R.id.img_card_06;
      ImageButton imgCard06 = ViewBindings.findChildViewById(rootView, id);
      if (imgCard06 == null) {
        break missingId;
      }

      id = R.id.img_card_07;
      ImageButton imgCard07 = ViewBindings.findChildViewById(rootView, id);
      if (imgCard07 == null) {
        break missingId;
      }

      id = R.id.img_card_08;
      ImageButton imgCard08 = ViewBindings.findChildViewById(rootView, id);
      if (imgCard08 == null) {
        break missingId;
      }

      id = R.id.img_card_09;
      ImageButton imgCard09 = ViewBindings.findChildViewById(rootView, id);
      if (imgCard09 == null) {
        break missingId;
      }

      id = R.id.img_card_10;
      ImageButton imgCard10 = ViewBindings.findChildViewById(rootView, id);
      if (imgCard10 == null) {
        break missingId;
      }

      id = R.id.img_card_11;
      ImageButton imgCard11 = ViewBindings.findChildViewById(rootView, id);
      if (imgCard11 == null) {
        break missingId;
      }

      id = R.id.img_card_12;
      ImageButton imgCard12 = ViewBindings.findChildViewById(rootView, id);
      if (imgCard12 == null) {
        break missingId;
      }

      id = R.id.match_count;
      TextView matchCount = ViewBindings.findChildViewById(rootView, id);
      if (matchCount == null) {
        break missingId;
      }

      id = R.id.time_show;
      TextView timeShow = ViewBindings.findChildViewById(rootView, id);
      if (timeShow == null) {
        break missingId;
      }

      return new FragmentGameBinding((FrameLayout) rootView, attemptCount, imgCard01, imgCard02,
          imgCard03, imgCard04, imgCard05, imgCard06, imgCard07, imgCard08, imgCard09, imgCard10,
          imgCard11, imgCard12, matchCount, timeShow);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
