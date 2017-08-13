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
        ReorderableList list;

        struct Function
        {
            public readonly Type targetType;
            public readonly MethodInfo method;

            public Function(Type targetType, MethodInfo method)
            {
                this.targetType = targetType;
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
                    return simpleName;
                }
            }
        }

        /// <summary>
        /// This methods should not be listed for action delegates
        /// </summary>
        private static readonly string[] kIgnoredMethods = {
            "Awake",
            "FixedUpdate",
            "LateUpdate",
            "OnAnimatorIK",
            "OnAnimatorMove",
            "OnApplicationFocus",
            "OnApplicationPause",
            "OnApplicationQuit",
            "OnAudioFilterRead",
            "OnBecameInvisible",
            "OnBecameVisible",
            "OnCollisionEnter",
            "OnCollisionEnter2D",
            "OnCollisionExit",
            "OnCollisionExit2D",
            "OnCollisionStay",
            "OnCollisionStay2D",
            "OnConnectedToServer",
            "OnControllerColliderHit",
            "OnDestroy",
            "OnDisable",
            "OnDisconnectedFromServer",
            "OnDrawGizmos",
            "OnDrawGizmosSelected",
            "OnEnable",
            "OnFailedToConnect",
            "OnFailedToConnectToMasterServer",
            "OnGUI",
            "OnJointBreak",
            "OnJointBreak2D",
            "OnMasterServerEvent",
            "OnMouseDown",
            "OnMouseDrag",
            "OnMouseEnter",
            "OnMouseExit",
            "OnMouseOver",
            "OnMouseUp",
            "OnMouseUpAsButton",
            "OnNetworkInstantiate",
            "OnParticleCollision",
            "OnParticleTrigger",
            "OnPlayerConnected",
            "OnPlayerDisconnected",
            "OnPostRender",
            "OnPreCull",
            "OnPreRender",
            "OnRenderImage",
            "OnRenderObject",
            "OnSerializeNetworkView",
            "OnServerInitialized",
            "OnTransformChildrenChanged",
            "OnTransformParentChanged",
            "OnTriggerEnter",
            "OnTriggerEnter2D",
            "OnTriggerExit",
            "OnTriggerExit2D",
            "OnTriggerStay",
            "OnTriggerStay2D",
            "OnValidate",
            "OnWillRenderObject",
            "Reset",
            "Start",
            "Update",
        };

        private static readonly Type[] kParamTypes = {
            typeof(int),
            typeof(float),
            typeof(string),
            typeof(bool)
        };

        private int m_selectedIndex = -1;

        private void OnEnable()
        {
            list = new ReorderableList(serializedObject, serializedObject.FindProperty("m_calls"), false, true, true, true);
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
            SerializedProperty modeProperty = arrayElementAtIndex.FindPropertyRelative("m_mode");
            SerializedProperty targetProperty = arrayElementAtIndex.FindPropertyRelative("m_target");
            SerializedProperty methodProperty = arrayElementAtIndex.FindPropertyRelative("m_methodName");
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
                    argumentProperty = arrayElementAtIndex.FindPropertyRelative("m_objectArgument");
                    break;
                case PersistentListenerMode.Int:
                    argumentProperty = arrayElementAtIndex.FindPropertyRelative("m_intArgument");
                    break;
                case PersistentListenerMode.Float:
                    argumentProperty = arrayElementAtIndex.FindPropertyRelative("m_floatArgument");
                    break;
                case PersistentListenerMode.String:
                    argumentProperty = arrayElementAtIndex.FindPropertyRelative("m_stringArgument");
                    break;
                case PersistentListenerMode.Bool:
                    argumentProperty = arrayElementAtIndex.FindPropertyRelative("m_boolArgument");
                    break;
                default:
                    argumentProperty = arrayElementAtIndex.FindPropertyRelative("m_intArgument");
                    break;
            }
            string argumentAssemblyTypeName = arrayElementAtIndex.FindPropertyRelative("m_objectArgumentAssemblyTypeName").stringValue;
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
                    BuildFunctionPopupList();
                }
                EditorGUI.EndProperty();
            }
            GUI.backgroundColor = backgroundColor;
        }

        private void BuildFunctionPopupList()
        {
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

        private string[] GetFunctionNames(Function[] functions)
        {
            List<string> names = new List<string>();
            names.Add("No Function");
            names.Add("/");
            foreach (var function in functions)
            {
                names.Add(function.targetType.Name + "/" + function.displayName);
            }
            return names.ToArray();
        }

        int IndexOfFunction(Function[] functions, string typeName, string methodName)
        {
            if (typeName == null || methodName == null)
            {
                return -1;
            }

            var type = Type.GetType(typeName);
            if (type == null)
            {
                return -1;
            }

            int index = 0;
            foreach (var function in functions)
            {
                if (function.targetType.Name == typeName && function.method.Name == methodName)
                {
                    return index;
                }

                ++index;
            }

            return -1;
        }

        Function[] ListFunctions(GameObject obj)
        {
            List<Function> functions = new List<Function>();
            if (obj != null)
            {
                List<Type> types = new List<Type>();
                types.Add(typeof(GameObject));
                foreach (Component component in obj.GetComponents<Component>())
                {
                    types.Add(component.GetType());
                }

                foreach (var type in types)
                {
                    var methods = ClassUtils.ListInstanceMethods(type, IsValidActionMethod);
                    foreach (var method in methods)
                    {
                        functions.Add(new Function(type, method));
                    }
                }
            }

            functions.Sort(delegate (Function a, Function b)
            {
                return a.isProperty == b.isProperty ? a.displayName.CompareTo(b.displayName) : (a.isProperty ? -1 : 1);
            });

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

            if (Array.IndexOf(kIgnoredMethods, method.Name) != -1)
            {
                return false; // this method should be ignored
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
