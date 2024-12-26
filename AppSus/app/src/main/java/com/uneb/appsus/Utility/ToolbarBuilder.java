package com.uneb.appsus.Utility;

import androidx.activity.OnBackPressedCallback;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.uneb.appsus.R;

public class ToolbarBuilder {
    private final Activity activity;
    private final Toolbar toolbar;
    private boolean hasReturnButton = false;

    public ToolbarBuilder(Activity activity, Toolbar toolbar) {
        this.activity = activity;
        this.toolbar = toolbar;
    }

    public ToolbarBuilder withTitle(String title) {
        TextView toolbarTitle = toolbar.findViewById(R.id.toolbarTitle);
        if (toolbarTitle != null) {
            toolbarTitle.setText(title);
        }
        return this;
    }

    public ToolbarBuilder withReturnButton() {
        ImageButton buttonReturn = toolbar.findViewById(R.id.buttonReturn);
        if (buttonReturn != null) {
            buttonReturn.setVisibility(View.VISIBLE);
            buttonReturn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.onBackPressed();
                }
            });
        }
        hasReturnButton = true;
        return this;
    }

    public ToolbarBuilder withBurgerButton(final Class<?> settingsActivityClass) {
        ImageButton buttonMenu = toolbar.findViewById(R.id.buttonMenu);
        if (buttonMenu != null) {
            buttonMenu.setVisibility(View.VISIBLE);
            buttonMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, settingsActivityClass);
                    activity.startActivity(intent);
                }
            });
        }
        return this;
    }

    public void build() {
        if (activity instanceof AppCompatActivity) {
            ((AppCompatActivity) activity).setSupportActionBar(toolbar);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, R.color.dark_blue));
            }

            if (!hasReturnButton) {
                ((AppCompatActivity) activity).getOnBackPressedDispatcher().addCallback((AppCompatActivity) activity, new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        // Do nothing, block back press
                    }
                });
            }
        }
    }
}