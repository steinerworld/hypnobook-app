package net.steinerworld.hypnobook.ui.views;


import java.io.ByteArrayInputStream;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.componentfactory.ToggleButton;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import com.vaadin.flow.theme.lumo.Lumo;
import com.vaadin.flow.theme.lumo.LumoUtility;

import lombok.RequiredArgsConstructor;
import net.steinerworld.hypnobook.domain.AppUser;
import net.steinerworld.hypnobook.repository.AppUserRepository;
import net.steinerworld.hypnobook.security.SecurityService;
import net.steinerworld.hypnobook.services.VersionService;
import net.steinerworld.hypnobook.ui.components.appnav.AppNav;
import net.steinerworld.hypnobook.ui.components.appnav.AppNavItem;
import net.steinerworld.hypnobook.ui.views.about.AboutView;
import net.steinerworld.hypnobook.ui.views.accounting.AccountingView;
import net.steinerworld.hypnobook.ui.views.category.CategoryView;
import net.steinerworld.hypnobook.ui.views.dashboard.DashboardView;
import net.steinerworld.hypnobook.ui.views.profile.ProfileView;
import net.steinerworld.hypnobook.ui.views.taxperiod.TaxPeriodView;

/**
 * The main view is a top-level placeholder for other views.
 */
@RequiredArgsConstructor
public class MainLayout extends AppLayout {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainLayout.class);

    private H2 viewTitle;

    private final SecurityService securityService;
    private final AccessAnnotationChecker accessChecker;
    private final AppUserRepository userRepository;
    private final VersionService versionService;

    @PostConstruct
    public void initialize() {
        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.getElement().setAttribute("aria-label", "Menu toggle");

        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        addToNavbar(true, toggle, viewTitle);
    }

    private void addDrawerContent() {
        H1 appName = new H1("Hypno Book " + versionService.getVersion());
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        Header header = new Header(appName);


        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(header, scroller, createFooter());
    }

    private AppNav createNavigation() {
        // AppNav is not yet an official component.
        // For documentation, visit https://github.com/vaadin/vcf-nav#readme
        AppNav nav = new AppNav();
        if (accessChecker.hasAccess(DashboardView.class)) {
            nav.addItem(new AppNavItem("Übersicht", DashboardView.class).setIcon(createIcon(VaadinIcon.HOME)));
        }
        if (accessChecker.hasAccess(AccountingView.class)) {
            nav.addItem(new AppNavItem("Buchung", AccountingView.class).setIcon(createIcon(VaadinIcon.SCALE_UNBALANCE)));
        }
        AppNavItem settingNav = new AppNavItem("Einstellungen");
        settingNav.setIcon(createIcon(VaadinIcon.SLIDERS));
        settingNav.addItem(new AppNavItem("Steuerperiode", TaxPeriodView.class).setIcon(createIcon(VaadinIcon.TASKS)));
        settingNav.addItem(new AppNavItem("Kategorie", CategoryView.class).setIcon(createIcon(VaadinIcon.TAGS)));
        nav.addItem(settingNav);
        if (accessChecker.hasAccess(AboutView.class)) {
            nav.addItem(new AppNavItem("Info", AboutView.class).setIcon(createIcon(VaadinIcon.INFO_CIRCLE)));
        }
        return nav;
    }

    private Footer createFooter() {
        Footer layout = new Footer();
        layout.addClassName("footer-div");

        Optional<AppUser> maybeUser = securityService.authenticatedAppUser();
        if (maybeUser.isPresent()) {
            AppUser user = maybeUser.get();

            Avatar avatar = new Avatar(user.getUsername());
            StreamResource resource = new StreamResource("profile-pic", () -> new ByteArrayInputStream(user.getProfilePicture()));
            avatar.setImageResource(resource);
            avatar.setThemeName("xsmall");
            avatar.getElement().setAttribute("tabindex", "-1");

            MenuBar userMenu = new MenuBar();
            userMenu.setThemeName("tertiary-inline contrast");

            MenuItem userName = userMenu.addItem("");
            Div div = new Div();
            div.add(avatar);
            div.add(user.getName());
            div.add(new Icon("lumo", "dropdown"));
            div.getElement().getStyle().set("display", "flex");
            div.getElement().getStyle().set("align-items", "center");
            div.getElement().getStyle().set("gap", "var(--lumo-space-s)");
            userName.add(div);
            userName.getSubMenu().addItem("Abmelden", e -> securityService.logout());
            userName.getSubMenu().addItem("Mein Profil", e -> UI.getCurrent().navigate(ProfileView.class));
            layout.add(createThemeModeToggle(user), userMenu);
        } else {
            Anchor loginLink = new Anchor("login", "Sign in");
            layout.add(loginLink);
        }
        return layout;
    }

    private Component createThemeModeToggle(AppUser user) {
        changeThemeTo(user.getTheme());
        Icon moon = new Icon(VaadinIcon.MOON_O);
        Icon sun = new Icon(VaadinIcon.SUN_O);
        ToggleButton toggle = new ToggleButton();
        boolean themeDark = Lumo.DARK.equals(user.getTheme());
        toggle.setValue(themeDark);
        toggle.addValueChangeListener(e -> changeThemeAndSave(user, e.getValue() ? Lumo.DARK : Lumo.LIGHT));
        return new HorizontalLayout(sun, toggle, moon);
    }

    private void changeThemeAndSave(AppUser user, String theme) {
        changeThemeTo(theme);
        saveTheme(user, theme);
    }

    private void changeThemeTo(String theme) {
        LOGGER.info("change theme to {}", theme);
        ThemeList themeList = UI.getCurrent().getElement().getThemeList();
        if (theme.equals(Lumo.LIGHT)) {
            themeList.remove(Lumo.DARK);
        } else {
            themeList.add(Lumo.DARK);
        }

    }

    private void saveTheme(AppUser user, String theme) {
        LOGGER.info("save theme {} for user {}", theme, user);
        user.setTheme(theme);
        userRepository.save(user);
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

    private Icon createIcon(VaadinIcon symbol) {
        Icon icon = new Icon(symbol);
        icon.setSize("16px");
        return icon;
    }
}
