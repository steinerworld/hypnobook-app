package net.steinerworld.hypnobook.ui.views.about;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.jar.Manifest;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import net.steinerworld.hypnobook.exceptions.MaloneyException;
import net.steinerworld.hypnobook.ui.views.MainLayout;


@PermitAll
@PageTitle("About")
@Route(value = "about", layout = MainLayout.class)
public class AboutView extends VerticalLayout {
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
        InputStream stream = getClass().getClassLoader().getResourceAsStream("META-INF/MANIFEST.MF");
        try {
            Manifest manifest = new Manifest(stream);
            String rowBuildDate = manifest.getMainAttributes().getValue("Build-date");
            String rowVersion = AboutView.class.getPackage().getImplementationVersion();
            String version = Objects.requireNonNullElse(rowVersion, "for development");
            String build = Objects.requireNonNullElse(rowBuildDate, "now build");
            return String.format("Version %s - %s", version, build);
        } catch (IOException e) {
            throw new MaloneyException("cannot read the manifest", e);
        }
    }

}
