using System.Collections;
using System.Collections.Generic;

using UnityEngine;
using UnityEngine.UI;
using UnityEngine.SceneManagement;

public class SceneButton : MonoBehaviour
{
    [SerializeField]
    string m_sceneName;

    void Start()
    {
        var button = GetComponent<Button>();
        button.onClick.AddListener(delegate()
        {
            SceneManager.LoadScene(m_sceneName);
        });
    }
}
