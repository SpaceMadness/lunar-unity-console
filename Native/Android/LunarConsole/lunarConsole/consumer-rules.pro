-keep public class spacemadness.com.lunarconsole.settings.*
-keepclassmembers class spacemadness.com.lunarconsole.settings.* {
   public *;
}
-keep public enum spacemadness.com.lunarconsole.settings.ExceptionWarningSettings$** {
    **[] $VALUES;
    public *;
}
-keep public enum spacemadness.com.lunarconsole.settings.Gesture {
    **[] $VALUES;
    public *;
}
-keep public class spacemadness.com.lunarconsole.console.NativeBridge
-keepclassmembers class spacemadness.com.lunarconsole.console.NativeBridge {
   public *;
}