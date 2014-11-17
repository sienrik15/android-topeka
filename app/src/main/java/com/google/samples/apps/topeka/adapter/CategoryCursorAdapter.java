/*
 * Copyright 2014 Google Inc.
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
package com.google.samples.apps.topeka.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.google.samples.apps.topeka.model.Category;
import com.google.samples.apps.topeka.model.Theme;
import com.google.samples.apps.topeka.persistence.CategoryCursor;
import com.google.samples.apps.topeka.persistence.TopekaDatabaseHelper;
import com.google.samples.apps.topeka.widget.CategoryLayout;

/**
 * An adapter backed by a {@link CategoryCursor} that allows display of {@link Category} data.
 */
public class CategoryCursorAdapter extends CursorAdapter {

    public static final String ICON_CATEGORY = "icon_category_";
    public static final String DRAWABLE = "drawable";
    private final Resources mResources;
    private final String mPackageName;

    public CategoryCursorAdapter(Activity activity) {
        super(activity, TopekaDatabaseHelper.getCategoryCursor(activity), true);
        mResources = activity.getResources();
        mPackageName = activity.getPackageName();
    }

    private static void adjustStyles(Theme theme, CategoryLayout categoryLayout) {
        categoryLayout.setBackgroundResource(theme.getWindowBackgroundColor());
        Resources resources = categoryLayout.getResources();
        TextView name = categoryLayout.getName();
        name.setBackgroundResource(theme.getPrimaryColor());
        name.setTextColor(resources.getColor(theme.getTextPrimaryColor()));
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return new CategoryLayout(context);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Category category = getCategoryOrThrow(cursor);
        if (view instanceof CategoryLayout) {
            //TODO: 11/11/14 don't use resource lookup
            CategoryLayout categoryLayout = (CategoryLayout) view;
            categoryLayout.setImageResource(mResources.getIdentifier(
                    ICON_CATEGORY + category.getId(), DRAWABLE, mPackageName));
            categoryLayout.setText(category.getName());
            adjustStyles(category.getTheme(), categoryLayout);
        }
    }

    private Category getCategoryOrThrow(Cursor cursor) {
        final CategoryCursor categoryCursor = getCategoryCursorOrThrow(cursor);
        return categoryCursor.getCategory();
    }

    private CategoryCursor getCategoryCursorOrThrow(Cursor cursor) {
        if (cursor instanceof CategoryCursor) {
            return (CategoryCursor) cursor;
        } else {
            throw new UnsupportedOperationException(
                    "This adapter only works with an CategoryCursor");
        }
    }
}
