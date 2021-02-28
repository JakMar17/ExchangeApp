package si.fri.jakmar.backend.exchangeapp.functions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import si.fri.jakmar.backend.exchangeapp.constants.UrlConstants;
import si.fri.jakmar.backend.exchangeapp.exceptions.MailException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class RequestEmailCreator {
    @Autowired
    private UrlConstants urlConstants;

    public void sendEmailConfirmation(String email, String registrationId) throws IOException, MailException {
        String url =  urlConstants.REGISTRATION_CONFIRMATION_EMAIL_URL.replace("{{email}}", email).replace("{{id}}", registrationId);
        createRequest(url, "POST");
    }

    public void sendEmailPasswordReset(String email, String resetId) throws IOException, MailException {
        String url =  urlConstants.PASSWORD_RESET_EMAIL_URL.replace("{{email}}", email).replace("{{id}}", resetId);
        createRequest(url, "POST");
    }

    private void createRequest(String urlString, String requestMethod) throws MailException, IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod(requestMethod);
        connection.connect();

        int code = connection.getResponseCode();
        if(code != 200)
            throw new MailException();

    }
}
