package cordova.plugin.set.locale;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * This class echoes a string called from JavaScript.
 */
public class SetLocale extends CordovaPlugin {
    /**
     * Cordova Actions.
     */
    private static final String ACTION_SET_LOCALE = "setLocale";
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (ACTION_SET_LOCALE.equals(action)) {
            String localeName = args.getString(0);
            this.setLocale(localeName, callbackContext);
            return true;
        }
        return false;
    }

    private void setLocale(String localeName, CallbackContext callbackContext) {
        if (localeName != null && localeName.length() > 0) {
            updateBaseContextLocale(localeName, cordova.getContext());
            callbackContext.success(localeName);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }

    private Context updateBaseContextLocale(String language, Context context) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResourcesLocale(context, locale);
        }

        return updateResourcesLocaleLegacy(context, locale);
    }

    @TargetApi(Build.VERSION_CODES.N)
    private Context updateResourcesLocale(Context context, Locale locale) {
        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        return context.createConfigurationContext(configuration);
    }

    @SuppressWarnings("deprecation")
    private Context updateResourcesLocaleLegacy(Context context, Locale locale) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return context;
    }
}
