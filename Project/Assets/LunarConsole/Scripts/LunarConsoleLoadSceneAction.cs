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