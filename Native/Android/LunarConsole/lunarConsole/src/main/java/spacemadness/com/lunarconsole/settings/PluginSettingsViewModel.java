//
//  PluginSettingsViewModel.java
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2015-2021 Alex Lementuev, SpaceMadness.
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


package spacemadness.com.lunarconsole.settings;

import java.util.ArrayList;
import java.util.List;

import spacemadness.com.lunarconsole.reflection.FieldProperty;
import spacemadness.com.lunarconsole.reflection.PropertyHelper;
import spacemadness.com.lunarconsole.ui.ListViewItem;
import spacemadness.com.lunarconsole.utils.StringUtils;

import static spacemadness.com.lunarconsole.utils.ObjectUtils.checkNotNull;

class PluginSettingsViewModel {
    private final PluginSettingsEditor settingsEditor;
    private final boolean isProVersion;

    PluginSettingsViewModel(PluginSettingsEditor settingsEditor) {
        this.settingsEditor = checkNotNull(settingsEditor, "settingsEditor");
        this.isProVersion = settingsEditor.isProVersion();
    }

    List<ListViewItem> createItems() {
        PluginSettings settings = settingsEditor.getSettings();

        List<ListViewItem> items = new ArrayList<>();
        items.add(new PropertyItem(PropertyHelper.getProperty(settings, "richTextTags")));
        items.add(new HeaderItem("Exception Warning"));
        items.add(new PropertyItem(PropertyHelper.getProperty(settings, "exceptionWarning.displayMode")));
        items.add(new HeaderItem("Log Overlay"));
        items.add(new PropertyItem(PropertyHelper.getProperty(settings, "logOverlay.enabled"), isProVersion));
        items.add(new PropertyItem(PropertyHelper.getProperty(settings, "logOverlay.maxVisibleLines"), isProVersion));
        items.add(new PropertyItem(PropertyHelper.getProperty(settings, "logOverlay.timeout"), isProVersion));
        return items;
    }

    private void notifyPropertyChanged(FieldProperty property) {
        settingsEditor.setSettings(settingsEditor.getSettings());
    }

    enum ItemType {HEADER, PROPERTY}

    static abstract class Item extends ListViewItem {
        private final ItemType type;

        Item(ItemType type) {
            this.type = type;
        }

        @Override
        protected int getItemViewType() {
            return type.ordinal();
        }
    }

    static class HeaderItem extends Item {
        public final String title;

        HeaderItem(String title) {
            super(ItemType.HEADER);
            this.title = title;
        }
    }

    class PropertyItem extends Item {
        final String displayName;
        final boolean enabled;
        private final FieldProperty property;

        PropertyItem(FieldProperty property) {
            this(property, true);
        }

        PropertyItem(FieldProperty property, boolean enabled) {
            super(ItemType.PROPERTY);
            this.property = property;
            this.enabled = enabled;
            this.displayName = StringUtils.camelCaseToWords(property.name);
        }

        public Object getValue() {
            return property.getValue();
        }

        public void setValue(Object value) {
            property.setValue(value);
            notifyPropertyChanged(property);
        }

        public Class<?> getType() {
            return property.getType();
        }
    }
}
