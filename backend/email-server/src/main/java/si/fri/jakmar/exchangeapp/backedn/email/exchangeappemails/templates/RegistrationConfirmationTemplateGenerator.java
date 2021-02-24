package si.fri.jakmar.exchangeapp.backedn.email.exchangeappemails.templates;

import com.github.mustachejava.DefaultMustacheFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Service
public class RegistrationConfirmationTemplateGenerator {

    public String createTemplate(String email, String link) {
        Map<String, String> model = new HashMap<>();
        model.put("email", email);
        model.put("povezava", link);

        var writer = new StringWriter();
        var mf = new DefaultMustacheFactory();
        var mustache = mf.compile("templates/register-template.mustache");
        mustache.execute(writer, model);
        return writer.toString();
    }
}
