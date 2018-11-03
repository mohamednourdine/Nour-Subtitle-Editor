package nour.subtitle.editor.dictionary;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Translator {
    private Languages sourceLang;
    private Languages destLang;
    private String sourceText;
    private String destText;
    private String hostname;
    private int port;

    public Translator() {
        initialize();
    }

    private void initialize() {
        sourceLang = null;
        destLang = null;
        sourceText = "";
        destText = "";
        hostname = "";
    }

    public String execute() {
        destText = "";

        // Check if we have all variables set
        if (sourceLang == null || sourceText.isEmpty() || destLang == null) {
            System.err.println("Missing parameters; please set the Source Language, Destination Language and the "
                    + "Source Text first");
        } else {
            execute(sourceText, sourceLang, destLang);
        }

        return destText;
    }

    public String execute(String text, Languages sl, Languages dl) {
        String regex, temp, params = null;
        StringBuilder translated = new StringBuilder();
        Matcher matcher;
        Pattern pattern;

        // We initialize the variables first
        initialize();

        sourceLang = sl;
        destLang = dl;
        sourceText = text;

        // URL enconding the text
        try {
            params = "q=" + URLEncoder.encode(text, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String tk = GoogleTK.TL(text);

        // Creating the URL
        String urlString = "https://translate.google.cn/translate_a/single?client=t"
                + "&sl=" + sourceLang.getValue()
                + "&tl=" + destLang.getValue()
                + "&hl=" + destLang.getValue()
                + "&dt=at&dt=bd&dt=ex&dt=ld&dt=md&dt=qca&dt=rw&dt=rm&dt=ss&dt=t"
                + "&ie=UTF-8&oe=UTF-8&source=btn&ssel=3&tsel=6&kc=0" + "&tk=" + tk;
       
     //   System.out.println(urlString);

        // Get the JS
        String js = sendPost(urlString, params);

        // Parse the JS
        regex = "\\[\\[(.*?)\\]\\]";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(js);

        if (matcher.find()) {
            temp = matcher.group(1);
            regex = "\\[\"(.*?)\",\"";
            pattern = Pattern.compile(regex);
            matcher = pattern.matcher(temp);
            while (matcher.find())
                translated.append(matcher.group(1));
            destText = translated.toString();

            // Removing unnecessary spaces
            destText = destText.replace(" .", ".").replace(" ,", ",").replace(" -", "-").replace(" ;", ";")
                    .replace(" :", ":").replace("( ", "(").replace(" )", ")");
        }

        return destText;
    }

    private String sendPost(String urlString, String params) {
        String line;
        StringBuilder html = new StringBuilder();

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = null;
            if (!"".equals(hostname) && !(port == -1)) {
                SocketAddress addr = new InetSocketAddress(hostname, port);
                Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);
                conn = (HttpURLConnection) url.openConnection(proxy);
            } else {
                conn = (HttpURLConnection) url.openConnection();
            }

            // Fake the User-Agent
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");

            // Setting the post parameters
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(params);
            wr.flush();
            wr.close();

            // Check the HTTP response code
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));

                // Reading the HTML
                while ((line = in.readLine()) != null)
                    html.append(line.trim());

                in.close();
            }

            // Close the connection
            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return html.toString();
    }

    /**
     * @return the sourceLang
     */
    public Languages getSourceLang() {
        return sourceLang;
    }

    /**
     * @param sourceLang
     * the sourceLang to set
     */
    public void setSourceLang(Languages sourceLang) {
        this.sourceLang = sourceLang;
    }

    /**
     * @return the destLang
     */
    public Languages getDestLang() {
        return destLang;
    }

    /**
     * @param destLang
     * the destLang to set
     */
    public void setDestLang(Languages destLang) {
        this.destLang = destLang;
    }

    /**
     * @return the sourceText
     */
    public String getSourceText() {
        return sourceText;
    }

    /**
     * @param sourceText
     * the sourceText to set
     */
    public void setSourceText(String sourceText) {
        this.sourceText = sourceText;
    }

    /**
     * @return the destText
     */
    public String getDestText() {
        return destText;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}