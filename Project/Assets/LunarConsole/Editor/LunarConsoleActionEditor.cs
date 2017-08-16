//
//  LunarConsoleActionsEditor.cs
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2017 Alex Lementuev, SpaceMadness.
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

using System;
using System.Collections;
using System.Collections.Generic;
using System.Reflection;

using UnityEngine;
using UnityEditor;
using UnityEditorInternal;

using LunarConsolePlugin;
using LunarConsolePluginInternal;
using System.Text;

namespace LunarConsoleEditorInternal
{
    [CustomEditor(typeof(LunarConsoleAction))]
    class LunarConsoleActionEditor : Editor
    {
        private const string kPropCalls = "m_calls";
        private const string kPropMode = "m_mode";
        private const string kPropTarget = "m_target";
        private const string kPropMethod = "m_methodName";
        private const string kPropArgObject = "m_objectArgument";
        private const string kPropArgInt = "m_intArgument";
        private const string kPropArgFloat = "m_floatArgument";
        private const string kPropArgString = "m_stringArgument";
        private const string kPropArgBool = "m_boolArgument";
        private const string kPropArgAssemblyTypeName = "m_objectArgumentAssemblyTypeName";
        ReorderableList list;

        struct Function
        {
            public readonly UnityEngine.Object target;
            public readonly MethodInfo method;

            public Function(UnityEngine.Object target, MethodInfo method)
            {
                this.target = target;
                this.method = method;
            }

            public bool isProperty
            {
                get { return method.IsSpecialName && method.Name.StartsWith("set_"); }
            }

            public string simpleName
            {
                get { return isProperty ? method.Name.Substring("set_".Length) : method.Name; }
            }

            public Type paramType
            {
                get
                {
                    var methodParams = method.GetParameters();
                    return methodParams.Length > 0 ? methodParams[0].ParameterType : null;
                }
            }

            public string displayName
            {
                get
                {
                    var functionParamType = paramType;
                    if (functionParamType != null)
                    {
                        var typeName = ClassUtils.TypeShortName(functionParamType);
                        return isProperty ?
                            string.Format("{0} {1}", typeName, simpleName) :
                            string.Format("{0} ({1})", simpleName, typeName);
                    }
                    return string.Format("{0} ()", simpleName);
                }
            }
        }

        private static readonly Type[] kParamTypes = {
            typeof(int),
            typeof(float),
            typeof(string),
            typeof(bool)
        };

        private int m_selectedIndex = -1;

        private void OnEnable()
        {
            list = new ReorderableList(serializedObject, serializedObject.FindProperty(kPropCalls), false, true, true, true);
            list.drawHeaderCallback = DrawListHeader;
            list.drawElementCallback = DrawListElement;
            list.elementHeight = 43;
        }
        
        public override void OnInspectorGUI()
        {
            serializedObject.Update();
            list.DoLayoutList();
            serializedObject.ApplyModifiedProperties();
        }

        void DrawListHeader(Rect rect)
        {
            EditorGUI.LabelField(rect, "On Click ()");
        }

        void DrawListElement(Rect rect, int index, bool isActive, bool isFocused)
        {
            SerializedProperty arrayElementAtIndex = list.serializedProperty.GetArrayElementAtIndex(index);
            rect.y += 1f;
            Rect[] rowRects = GetRowRects(rect);
            Rect runtimeModeRect = rowRects[0];
            Rect targetRect = rowRects[1];
            Rect methodRect = rowRects[2];
            Rect argumentRect = rowRects[3];
            SerializedProperty modeProperty = arrayElementAtIndex.FindPropertyRelative(kPropMode);
            SerializedProperty targetProperty = arrayElementAtIndex.FindPropertyRelative(kPropTarget);
            SerializedProperty methodProperty = arrayElementAtIndex.FindPropertyRelative(kPropMethod);
            Color backgroundColor = GUI.backgroundColor;
            GUI.backgroundColor = Color.white;
            GUI.Box(runtimeModeRect, "Runtime Only", EditorStyles.popup);
            EditorGUI.BeginChangeCheck();
            GUI.Box(targetRect, GUIContent.none);
            EditorGUI.PropertyField(targetRect, targetProperty, GUIContent.none);
            if (EditorGUI.EndChangeCheck())
            {
                methodProperty.stringValue = null;
            }
            PersistentListenerMode persistentListenerMode = (PersistentListenerMode) modeProperty.enumValueIndex;
            if (targetProperty.objectReferenceValue == null || string.IsNullOrEmpty(methodProperty.stringValue))
            {
                persistentListenerMode = PersistentListenerMode.Void;
            }
            SerializedProperty argumentProperty;
            switch (persistentListenerMode)
            {
                case PersistentListenerMode.Object:
                    argumentProperty = arrayElementAtIndex.FindPropertyRelative(kPropArgObject);
                    break;
                case PersistentListenerMode.Int:
                    argumentProperty = arrayElementAtIndex.FindPropertyRelative(kPropArgInt);
                    break;
                case PersistentListenerMode.Float:
                    argumentProperty = arrayElementAtIndex.FindPropertyRelative(kPropArgFloat);
                    break;
                case PersistentListenerMode.String:
                    argumentProperty = arrayElementAtIndex.FindPropertyRelative(kPropArgString);
                    break;
                case PersistentListenerMode.Bool:
                    argumentProperty = arrayElementAtIndex.FindPropertyRelative(kPropArgBool);
                    break;
                default:
                    argumentProperty = arrayElementAtIndex.FindPropertyRelative(kPropArgInt);
                    break;
            }
            string argumentAssemblyTypeName = arrayElementAtIndex.FindPropertyRelative(kPropArgAssemblyTypeName).stringValue;
            Type argumentType = typeof(UnityEngine.Object);
            if (!string.IsNullOrEmpty(argumentAssemblyTypeName))
            {
                argumentType = (Type.GetType(argumentAssemblyTypeName, false) ?? typeof(UnityEngine.Object));
            }
            if (persistentListenerMode == PersistentListenerMode.Object)
            {
                EditorGUI.BeginChangeCheck();
                UnityEngine.Object objectReferenceValue = EditorGUI.ObjectField(argumentRect, GUIContent.none, argumentProperty.objectReferenceValue, argumentType, true);
                if (EditorGUI.EndChangeCheck())
                {
                    argumentProperty.objectReferenceValue = objectReferenceValue;
                }
            }
            else if (persistentListenerMode != PersistentListenerMode.Void)
            {
                EditorGUI.PropertyField(argumentRect, argumentProperty, GUIContent.none);
            }
            using (new EditorGUI.DisabledScope(targetProperty.objectReferenceValue == null))
            {
                EditorGUI.BeginProperty(methodRect, GUIContent.none, methodProperty);
                GUIContent content;
                {
                    StringBuilder stringBuilder = new StringBuilder();
                    if (targetProperty.objectReferenceValue == null || string.IsNullOrEmpty(methodProperty.stringValue))
                    {
                        stringBuilder.Append("No Function");
                    }
                    else
                    {
                        stringBuilder.Append(targetProperty.objectReferenceValue.GetType().Name);
                        if (!string.IsNullOrEmpty(methodProperty.stringValue))
                        {
                            stringBuilder.Append(".");
                            if (methodProperty.stringValue.StartsWith("set_"))
                            {
                                stringBuilder.Append(methodProperty.stringValue.Substring(4));
                            }
                            else
                            {
                                stringBuilder.Append(methodProperty.stringValue);
                            }
                        }
                    }
                    content = new GUIContent(stringBuilder.ToString());
                }
                if (GUI.Button(methodRect, content, EditorStyles.popup))
                {
                    BuildPopupList(arrayElementAtIndex).DropDown(methodRect);
                }
                EditorGUI.EndProperty();
            }
            GUI.backgroundColor = backgroundColor;
        }

        private GenericMenu BuildPopupList(SerializedProperty serializedProperty)
        {
            SerializedProperty targetProperty = serializedProperty.FindPropertyRelative(kPropTarget);
            SerializedProperty methodProperty = serializedProperty.FindPropertyRelative(kPropMethod);

            var menu = new GenericMenu();
            menu.AddItem(new GUIContent("No Function"), methodProperty.stringValue == null, delegate() {
                methodProperty.stringValue = null;
                serializedObject.ApplyModifiedProperties();
            });

            var target = targetProperty.objectReferenceValue;
            if (target != null)
            {
                menu.AddSeparator("/");

                var functions = ListFunctions(target);
                foreach (var function in functions)
                {
                    var selected = target == function.target && methodProperty.stringValue == function.method.Name;
                    menu.AddItem(new GUIContent(function.target.GetType().Name + "/" + function.displayName), selected, delegate () {
                        targetProperty.objectReferenceValue = function.target;
                        methodProperty.stringValue = function.method.Name;
                        UpdateParamProperty(serializedProperty, function.paramType);
                        serializedObject.ApplyModifiedProperties();
                    });
                }
            }

            return menu;
        }

        void UpdateParamProperty(SerializedProperty serializedProperty, Type paramType)
        {
            SerializedProperty modeProperty = serializedProperty.FindPropertyRelative(kPropMode);
            SerializedProperty typeAssemblyProperty = serializedProperty.FindPropertyRelative(kPropArgAssemblyTypeName);

            PersistentListenerMode mode = PersistentListenerMode.Void;
            if (paramType != null)
            {
                if (paramType.IsSubclassOf(typeof(UnityEngine.Object)))
                {
                    mode = PersistentListenerMode.Object;
                }
                else if (paramType == typeof(int))
                {
                    mode = PersistentListenerMode.Int;
                }
                else if (paramType == typeof(float))
                {
                    mode = PersistentListenerMode.Float;
                }
                else if (paramType == typeof(string))
                {
                    mode = PersistentListenerMode.String;
                }
                else if (paramType == typeof(bool))
                {
                    mode = PersistentListenerMode.Bool;
                }
                else
                {
                    Log.e("Unexpected param type: {0}", paramType);
                }
            }
            modeProperty.enumValueIndex = (int)mode;
            typeAssemblyProperty.stringValue = paramType != null ? paramType.AssemblyQualifiedName : null;
        }

        private Rect[] GetRowRects(Rect rect)
        {
            Rect[] array = new Rect[4]; 
            rect.height = 16f;
            rect.y += 2f;
            Rect rect2 = rect;
            rect2.width *= 0.3f;
            Rect rect3 = rect2;
            rect3.y += EditorGUIUtility.singleLineHeight + 2f;
            Rect rect4 = rect;
            rect4.xMin = rect3.xMax + 5f;
            Rect rect5 = rect4;
            rect5.y += EditorGUIUtility.singleLineHeight + 2f;
            array[0] = rect2;
            array[1] = rect3;
            array[2] = rect4;
            array[3] = rect5;
            return array;
        }

        Function[] ListFunctions(UnityEngine.Object obj)
        {
            if (obj is Component)
            {
                obj = ((Component)obj).gameObject;
            }

            List<Function> functions = new List<Function>();
            if (obj != null)
            {
                List<UnityEngine.Object> targets = new List<UnityEngine.Object>();
                targets.Add(obj);

                if (obj is GameObject)
                {
                    var gameObject = obj as GameObject;
                    var components = gameObject.GetComponents<Component>();
                    foreach (var component in gameObject.GetComponents<Component>())
                    {
                        targets.Add(component);
                    }
                }

                foreach (var target in targets)
                {
                    List<MethodInfo> methods = new List<MethodInfo>();
                    ClassUtils.ListMethods(methods, target.GetType(), IsValidActionMethod, BindingFlags.Instance | BindingFlags.Public | BindingFlags.FlattenHierarchy);
                    methods.Sort(delegate (MethodInfo a, MethodInfo b) {
                        return a.IsSpecialName == b.IsSpecialName ? a.Name.CompareTo(b.Name) : (a.IsSpecialName ? -1 : 1);
                    });

                    foreach (var method in methods)
                    {
                        functions.Add(new Function(target, method));
                    }
                }
            }

            return functions.ToArray();
        }

        /// <summary>
        /// Determines if method is a valid action delegate
        /// </summary>
        private static bool IsValidActionMethod(MethodInfo method)
        {
            if (!method.IsPublic)
            {
                return false; // only list public methods
            }

            if (method.ReturnType != typeof(void))
            {
                return false; // non-void return type are not allowed
            }

            if (method.IsAbstract)
            {
                return false; // don't list abstract methods
            }

            var methodParams = method.GetParameters();
            if (methodParams.Length > 1)
            {
                return false; // no more then a single param
            }

            var attributes = method.GetCustomAttributes(typeof(ObsoleteAttribute), false);
            if (attributes != null && attributes.Length > 0)
            {
                return false; // no obsolete methods
            }

            if (methodParams.Length == 1)
            {
                var paramType = methodParams[0].ParameterType;
                if (!paramType.IsSubclassOf(typeof(UnityEngine.Object)) && Array.IndexOf(kParamTypes, paramType) == -1)
                {
                    return false;
                }
            }

            return true;
        }
    }
}
