//
//  Provider.java
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2015-2020 Alex Lementuev, SpaceMadness.
//
//  Licensed under the Apache License, Version 2.0 (the "License");
//  you may not use this file except in compliance with the License.
//  You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.
//


package spacemadness.com.lunarconsole.dependency;

import java.util.HashMap;
import java.util.Map;

import static spacemadness.com.lunarconsole.utils.ObjectUtils.checkNotNull;

public class Provider {
    private static Map<Class<? extends ProviderDependency>, ProviderDependency> dependencyLookup = new HashMap<>();

    public static <T extends ProviderDependency> void register(Class<T> cls, T dependency) {
        dependencyLookup.put(checkNotNull(cls, "cls"), checkNotNull(dependency, "dependency"));
    }

    static void clear() {
        dependencyLookup.clear();
    }

    public static <T extends ProviderDependency> T of(Class<T> cls) {
        ProviderDependency dependency = dependencyLookup.get(cls);
        if (dependency == null) {
            throw new IllegalArgumentException("Dependency class not registered: " + cls);
        }
        //noinspection unchecked
        return (T) dependency;
    }
}
