package net.steinerworld.hypnobook.ui.views;


import javax.annotation.PostConstruct;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.router.PageTitle;

import lombok.RequiredArgsConstructor;
import net.steinerworld.hypnobook.services.SecurityService;
import net.steinerworld.hypnobook.ui.components.appnav.AppNav;
import net.steinerworld.hypnobook.ui.components.appnav.AppNavItem;
import net.steinerworld.hypnobook.ui.views.about.AboutView;
import net.steinerworld.hypnobook.ui.views.helloworld.HelloWorldView;

/**
 * The main view is a top-level placeholder for other views.
 */
@RequiredArgsConstructor
public class MainLayout extends AppLayout {

    private final SecurityService securityService;
    private H1 viewTitle;

    @PostConstruct
    public void initialize() {
        setPrimarySection(Section.DRAWER);
        addToNavbar(true, createHeaderContent());
        addToDrawer(createDrawerContent());
    }

    private Component createHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.addClassNames("view-toggle");
        toggle.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        toggle.getElement().setAttribute("aria-label", "Menu toggle");

        viewTitle = new H1();
        viewTitle.addClassNames("view-title");

        Header header = new Header(toggle, viewTitle);
        if (securityService.getAuthenticatedUser() != null) {
            Button logout = new Button("Logout", click ->
                  securityService.logout());
            Div logoutDiv = new Div(logout);
            logoutDiv.addClassName("view-logout");
            header.add(logoutDiv);
        }

        header.addClassNames("view-header");
        return header;
    }

    private Component createDrawerContent() {
        H2 appName = new H2("Hypno Book");
        appName.addClassNames("app-name");

        com.vaadin.flow.component.html.Section section = new com.vaadin.flow.component.html.Section(appName,
                createNavigation(), createFooter());
        section.addClassNames("drawer-section");
        return section;
    }

    private AppNav createNavigation() {
        // AppNav is not yet an official component.
        // For documentation, visit https://github.com/vaadin/vcf-nav#readme
        AppNav nav = new AppNav();
        nav.addClassNames("app-nav");

        nav.addItem(new AppNavItem("Hello World", HelloWorldView.class, "la la-globe"));
        nav.addItem(new AppNavItem("About", AboutView.class, "la la-file"));

        return nav;
    }

    private Footer createFooter() {
        Footer layout = new Footer();
        layout.addClassNames("app-nav-footer");

        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}
