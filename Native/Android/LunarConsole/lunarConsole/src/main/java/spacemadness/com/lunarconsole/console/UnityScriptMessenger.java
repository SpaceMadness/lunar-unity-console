package spacemadness.com.lunarconsole.console;

import com.unity3d.player.UnityPlayer;

public class UnityScriptMessenger
{
    private final String target;

    public UnityScriptMessenger(String target)
    {
        if (target == null)
        {
            throw new NullPointerException("Target is null");
        }

        this.target = target;
    }

    public void sendMessage(String message)
    {
        sendMessage(message, "");
    }

    public void sendMessage(String message, String param)
    {
        UnityPlayer.UnitySendMessage(target, message, param);
    }
}
