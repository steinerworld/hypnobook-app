package net.steinerworld.hypnobook.ui.views.about;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;

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
    @PostConstruct
    public void initialize() {
        setSpacing(false);

        String row = AboutView.class.getPackage().getImplementationVersion();
        String version = row == null ? "" : row;
        add(new H2("Hypno-Book " + version));
        Image img = new Image("images/AlterEgoFull.jpeg", "Alter Ego");
        img.setWidth("200px");
        add(img);

        add(new H4("Developed with love by steinerworld in 2022"));
        add(new Paragraph("I wish you a successful future for your company Hypnose Steiner. \uD83D\uDC98"));

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }

}
