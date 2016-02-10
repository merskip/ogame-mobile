package pl.merskip.ogamemobile.game.pages;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.io.Serializable;

import pl.merskip.ogamemobile.game.GameActivity;


public class SimpleFragmentViewer<DataType> extends ViewerPage<DataType> {

    public static final String DEFAULT_ARG_NAME = "data";

    private Fragment fragment;
    private String argName;

    public SimpleFragmentViewer(GameActivity activity, Fragment fragment) {
        this(activity, fragment, DEFAULT_ARG_NAME);
    }

    public SimpleFragmentViewer(GameActivity activity, Fragment fragment, String argName) {
        super(activity);
        this.fragment = fragment;
        this.argName = argName;
    }

    @Override
    public void show(DataType data) {
        Bundle args = new Bundle();
        args.putSerializable(argName, (Serializable) data);
        fragment.setArguments(args);

        showFragment(fragment);
    }
}
