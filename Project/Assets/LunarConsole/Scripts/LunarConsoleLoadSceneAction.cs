//
//  LunarConsoleLoadSceneAction.cs
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2020 Alex Lementuev, SpaceMadness.
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

using LunarConsolePlugin;
using UnityEngine;
using UnityEngine.SceneManagement;

namespace LunarConsolePluginInternal
{
    public class LunarConsoleLoadSceneAction : MonoBehaviour
    {
        [SerializeField]
        private string m_sceneName;
        
        [SerializeField]
        private string m_actionName;

        [SerializeField]
        private LoadSceneMode m_mode = LoadSceneMode.Single;

        private void OnValidate()
        {
            if (string.IsNullOrEmpty(m_sceneName))
            {
                Debug.LogWarning("Scene name is missing", gameObject);
            }
        }

        private void OnEnable()
        {
            if (string.IsNullOrEmpty(m_sceneName))
            {
                Debug.LogWarning("Scene name is missing", gameObject);
            }
            else
            {
                string title = string.IsNullOrEmpty(m_actionName) ? string.Format("Load '{0}'", m_sceneName) : m_actionName;
                LunarConsole.RegisterAction(title, LoadScene);
            }
        }

        private void OnDisable()
        {
            LunarConsole.UnregisterAction(LoadScene);
        }

        private void LoadScene()
        {
            SceneManager.LoadScene(m_sceneName, m_mode);
        }
    }
}