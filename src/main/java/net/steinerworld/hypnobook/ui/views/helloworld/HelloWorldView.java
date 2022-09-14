package net.steinerworld.hypnobook.ui.views.helloworld;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;

import org.springframework.security.core.userdetails.UserDetails;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import lombok.RequiredArgsConstructor;
import net.steinerworld.hypnobook.services.SecurityService;
import net.steinerworld.hypnobook.ui.views.MainLayout;

@PermitAll
@PageTitle("Hello World")
@Route(value = "hello", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@RequiredArgsConstructor
public class HelloWorldView extends HorizontalLayout {

    private final SecurityService securityService;
    private TextField name;
    private Button sayHello;

    @PostConstruct
    public void initialize() {
        name = new TextField("Your name");
        sayHello = new Button("Say hello");
        sayHello.addClickListener(e -> Notification.show("Hello " + name.getValue()));
        sayHello.addClickShortcut(Key.ENTER);

        setMargin(true);
        setVerticalComponentAlignment(Alignment.END, name, sayHello);

        UserDetails user = securityService.getAuthenticatedUser();
        H3 username = new H3(user.getUsername());
        add(username, name, sayHello);
    }
}
