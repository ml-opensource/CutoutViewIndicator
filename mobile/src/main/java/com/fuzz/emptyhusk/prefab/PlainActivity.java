/*
 * Copyright 2016 Philip Cohn-Cort
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fuzz.emptyhusk.prefab;

import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fuzz.emptyhusk.R;

/**
 * @author Philip Cohn-Cort (Fuzz)
 */
public class PlainActivity extends Activity {

    private static final String ARG_FRAGMENT_CLASS = "fragment_class";
    private static final String ARG_FRAGMENT_ARGS = "fragment_args";

    public static Intent request(@NonNull Context origin, @NonNull Class<? extends Fragment> fragClass, @Nullable Bundle fragArgs) {
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
