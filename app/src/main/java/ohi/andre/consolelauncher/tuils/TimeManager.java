package ohi.andre.consolelauncher.tuils;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.format.Time;
import android.text.style.ForegroundColorSpan;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ohi.andre.consolelauncher.managers.SkinManager;
import ohi.andre.consolelauncher.managers.XMLPrefsManager;

/**
 * Created by francescoandreuzzi on 26/07/2017.
 */

public class TimeManager {

    static String[] formats;
    static Time time;

    static Pattern extractor = Pattern.compile("%t([0-9]+)", Pattern.CASE_INSENSITIVE);

    public static void create() {
        String format = XMLPrefsManager.get(String.class, XMLPrefsManager.Behavior.time_format);
        String separator = XMLPrefsManager.get(String.class, XMLPrefsManager.Behavior.time_format_separator);

        time = new Time();

        formats = format.split(separator);
        if(formats.length == 0) formats = new String[] {Tuils.EMPTYSTRING};

        Arrays.asList(formats);
    }

    public static String get(int index) {
        if(formats == null) return null;

        if(index < 0 || index >= formats.length) index = 0;
        return formats[index];
    }

    public static CharSequence replace(CharSequence cs) {
        return replace(cs, -1, SkinManager.COLOR_NOT_SET);
    }

    public static CharSequence replace(CharSequence cs, int color) {
        return replace(cs, -1, color);
    }

    public static CharSequence replace(CharSequence cs, long tm, int color) {
        if(tm == -1) {
            time.setToNow();
        } else {
            time.set(tm);
        }

        Matcher matcher = extractor.matcher(cs.toString());
        if(matcher.find()) {
            for(int count = 1; count <= matcher.groupCount(); count++) {
                String t = get(Integer.parseInt(matcher.group(count)));
                if(t == null) continue;

                String tf = time.format(t);

                SpannableString spannableString = null;
                if(color != SkinManager.COLOR_NOT_SET) {
                    spannableString = new SpannableString(tf);
                    spannableString.setSpan(new ForegroundColorSpan(color), 0, tf.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                }

                cs = TextUtils.replace(cs, new String[] {"%t" + matcher.group(count)}, new CharSequence[] {spannableString != null ? spannableString : tf});
            }
        }

        String t = get(0);
        if(t == null) return cs;

        String tf = time.format(t);

        SpannableString spannableString = null;
        if(color != SkinManager.COLOR_NOT_SET) {
            spannableString = new SpannableString(tf);
            spannableString.setSpan(new ForegroundColorSpan(color), 0, tf.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        }
        cs = TextUtils.replace(cs, new String[] {"%t"}, new CharSequence[] {spannableString != null ? spannableString : tf});

        return cs;
    }
}
