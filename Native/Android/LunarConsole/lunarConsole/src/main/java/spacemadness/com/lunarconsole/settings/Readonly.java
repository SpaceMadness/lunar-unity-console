package spacemadness.com.lunarconsole.settings;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Indicated a readonly setting (can only be set in the editor)
 */
@Retention(RetentionPolicy.RUNTIME)
@interface Readonly {
}
