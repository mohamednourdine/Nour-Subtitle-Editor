package nour.subtitle.editor.srt.utils;

import javax.net.ssl.HttpsURLConnection;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;

public class YandexTranslate {
	private static int i = 0;

	public static String translate(String lang, String input) throws IOException {
		String urlStr = "https://translate.yandex.net/api/v1.5/tr.json/translate?key=trnsl.1.1.20170722T112549Z.27f3905cfecbd1fb.c2c00c14aa28b2717756f07b68f94062b2898357";
		URL urlObj = new URL(urlStr);
		HttpsURLConnection connection = (HttpsURLConnection)urlObj.openConnection();
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
		dataOutputStream.writeBytes("text=" + URLEncoder.encode(input, "UTF-8") + "&lang=" + lang);

		InputStream response = connection.getInputStream();
		String json = new java.util.Scanner(response).nextLine();
		int start = json.indexOf("[");
		int end = json.indexOf("]");
		String translated = json.substring(start + 2, end - 1);
		i++;
		if (translated.equals(input) && i < 2) {
			// if return equal of entered text - we need change direction of translation
			return translate("en", input);
		} else return translated;
	}
}