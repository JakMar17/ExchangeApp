package si.fri.jakmar.backend.exchangeapp.functions;

import si.fri.jakmar.backend.exchangeapp.constants.UrlConstants;
import si.fri.jakmar.backend.exchangeapp.exceptions.MailException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class RequestEmailCreator {
    public static void sendEmailConfirmation(String email, String registrationId) throws IOException, MailException {
        String url =  UrlConstants.REGISTRATION_CONFIRMATION_EMAIL_URL.replace("{{email}}", email).replace("{{id}}", registrationId);
        createRequest(url, "POST");
    }

    public static void sendEmailPasswordReset(String email, String resetId) throws IOException, MailException {
        String url =  UrlConstants.PASSWORD_RESET_EMAIL_URL.replace("{{email}}", email).replace("{{id}}", resetId);
        createRequest(url, "POST");
    }

    private static void createRequest(String urlString, String requestMethod) throws MailException, IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod(requestMethod);
        connection.connect();

        int code = connection.getResponseCode();
        if(code != 200)
            throw new MailException();

    }
}
