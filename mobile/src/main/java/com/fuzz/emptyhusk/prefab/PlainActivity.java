package com.fuzz.emptyhusk.prefab;

import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.fuzz.emptyhusk.R;

/**
 * @author Philip Cohn-Cort (Fuzz)
 */
public class PlainActivity extends Activity {

    private static final String ARG_FRAGMENT_CLASS = "fragment_class";
    private static final String ARG_FRAGMENT_ARGS = "fragment_args";

    public static Intent request(@NonNull Context origin, @NonNull Class<? extends Fragment> fragClass, @NonNull Bundle fragArgs) {
        Intent intent = new Intent(origin, PlainActivity.class);
        intent.putExtra(ARG_FRAGMENT_CLASS, fragClass);
        intent.putExtra(ARG_FRAGMENT_ARGS, fragArgs);
        return intent;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    protected Class<? extends Fragment> getPrimaryFragmentClass() {
        Class<? extends Fragment> fragmentClass = (Class<? extends Fragment>) getIntent().getSerializableExtra(ARG_FRAGMENT_CLASS);
        if (fragmentClass == null) {
            fragmentClass = ListFragment.class;
        }
        return fragmentClass;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_plain);

        Bundle args = getIntent().getBundleExtra(ARG_FRAGMENT_ARGS);

        Class<? extends Fragment> fragmentClass = getPrimaryFragmentClass();
        Fragment fragment = Fragment.instantiate(this, fragmentClass.getName(), args);

        getFragmentManager()
                .beginTransaction()
                .add(R.id.primaryContent, fragment, fragmentClass.getSimpleName())
                .commit();
    }
}
