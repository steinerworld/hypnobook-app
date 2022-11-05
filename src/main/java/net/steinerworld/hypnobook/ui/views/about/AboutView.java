package net.steinerworld.hypnobook.ui.views.about;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import net.steinerworld.hypnobook.ui.views.MainLayout;


@PermitAll
@PageTitle("About")
@Route(value = "about", layout = MainLayout.class)
public class AboutView extends VerticalLayout {
    private static final Logger LOGGER = LoggerFactory.getLogger(AboutView.class);

    @PostConstruct
    public void initialize() {
        setSpacing(false);

        add(new H2("Hypno-Book"));
        add(new Paragraph(getFormattedBuildInfo()));

        Image img = new Image("images/AlterEgoFull.jpeg", "Alter Ego");
        img.setWidth("200px");
        add(img);

        add(new H4("Developed with pleasure by steinerworld in 2022"));
        add(new Paragraph("for my beloved wife and her business 'Hypnose Steiner'. \uD83D\uDC98"));

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }

    private String getFormattedBuildInfo() {
        try (InputStream stream = getClass().getResourceAsStream("/version.txt")) {
            if (stream != null) {
                String[] row = new String(stream.readAllBytes()).split(";");
                return String.format("Version %s - built at %s", row[0], LocalDateTime.parse(row[1]));
            } else {
                return "dev version - build on the fly";
            }
        } catch (IOException e) {
            LOGGER.info("cannot read version file", e);
            return "E: dev version - build on the fly";
        }
    }

}
