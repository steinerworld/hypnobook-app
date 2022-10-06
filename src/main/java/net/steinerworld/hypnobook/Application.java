package net.steinerworld.hypnobook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;

@SpringBootApplication
@Theme(value = "hypnobook")
@PWA(name = "hypnobook", shortName = "bhs", offlineResources = {})
@NpmPackage(value = "line-awesome", version = "1.3.0")
@NpmPackage(value = "@vaadin-component-factory/vcf-nav", version = "1.0.6")

@NpmPackage(value = "apexcharts", version = "3.30.0")
@NpmPackage(value = "onecolor", version = "3.1.0")
@JsModule("./com/github/appreciated/apexcharts/apexcharts-wrapper.ts")

public class Application extends SpringBootServletInitializer implements AppShellConfigurator {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
