/*
 * Copyright (C) 2013 The Android Open Source Project
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

package com.android.build.gradle.internal.api;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.build.gradle.BasePlugin;
import com.android.build.gradle.api.LibraryVariant;
import com.android.build.gradle.api.LibraryVariantOutput;
import com.android.build.gradle.api.TestVariant;
import com.android.build.gradle.internal.variant.BaseVariantData;
import com.android.build.gradle.internal.variant.LibraryVariantData;

import org.gradle.api.tasks.bundling.Zip;

/**
 * implementation of the {@link LibraryVariant} interface around a
 * {@link LibraryVariantData} object.
 *
 * This is a wrapper around the internal data model, in order to control what is accessible
 * through the external API.
 */
public class LibraryVariantImpl extends BaseVariantImpl implements LibraryVariant, TestedVariant {

    @NonNull
    private final LibraryVariantData variantData;
    @Nullable
    private TestVariant testVariant = null;

    public LibraryVariantImpl(
            @NonNull LibraryVariantData variantData,
            @NonNull BasePlugin plugin,
            @NonNull ReadOnlyObjectProvider readOnlyObjectProvider) {
        super(plugin, readOnlyObjectProvider);
        this.variantData = variantData;
    }

    @Override
    @NonNull
    protected BaseVariantData<?> getVariantData() {
        return variantData;
    }

    @Override
    public void setTestVariant(@Nullable TestVariant testVariant) {
        this.testVariant = testVariant;
    }

    @Override
    @Nullable
    public TestVariant getTestVariant() {
        return testVariant;
    }

    // ---- Deprecated, will be removed in 1.0
    //STOPSHIP

    @Override
    public Zip getPackageLibrary() {
        // if more than one output, refuse to use this method
        if (outputs.size() > 1) {
            throw new RuntimeException(String.format(
                    "More than one output on variant '%s', cannot call getPackageLibrary() on it. Call it on one of its outputs instead.",
                    getName()));
        }

        // deprecation warning.
        plugin.displayDeprecationWarning("variant.getPackageLibrary() is deprecated. Call it on one of variant.getOutputs() instead.");

        // use the single output for compatibility.
        return ((LibraryVariantOutput) outputs.get(0)).getPackageLibrary();
    }
}
