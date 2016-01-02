package pl.merskip.ogamemobile.adapter;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Dostarcza dostępu do danych zapisanych w JavaScript
 */
public class ScriptData {

    protected String content;

    public ScriptData(Document document) {
        Element lastScript = document.select("#boxBG script").last();
        if (lastScript != null)
            content = lastScript.html();
    }

    public String getContent() {
        return content;
    }

    public String getTextValue(String key) {
        String value = getValue(key);
        value = StringEscapeUtils.unescapeJson(value);
        return Jsoup.clean(value, Whitelist.simpleText()).trim();
    }

    public String getValue(String key) {
        String regex = Pattern.quote(key) + "=\"?(.*?)\"?;";
        Matcher matcher = Pattern.compile(regex).matcher(content);

        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public String getAbortToken() {
        String regex = "modus=2&token=(.*?)(\"|&)";
        Matcher matcher = Pattern.compile(regex, Pattern.DOTALL).matcher(content);

        if (matcher.find())
            return matcher.group(1);
        else
            return null;
    }
}
