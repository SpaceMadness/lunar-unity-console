package spacemadness.com.lunarconsole.settings;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Marks PRO only settings (not available in FREE version)
 */
@Retention(RetentionPolicy.RUNTIME)
@interface ProOnly {
}
