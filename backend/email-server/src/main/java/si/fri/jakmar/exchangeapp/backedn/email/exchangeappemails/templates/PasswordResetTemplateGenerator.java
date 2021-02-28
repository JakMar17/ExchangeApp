package si.fri.jakmar.exchangeapp.backedn.email.exchangeappemails.templates;

import com.github.mustachejava.DefaultMustacheFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Service
public class PasswordResetTemplateGenerator {

    public String createTemplate(String email,String link) {
        Map<String, String> model = new HashMap<>();
        model.put("link", link);
        model.put("email", email);

        var writer = new StringWriter();
        var mf = new DefaultMustacheFactory();
        var mustache = mf.compile("templates/password-reset-template.mustache");
        mustache.execute(writer, model);
        return writer.toString();
    }
}
