package net.steinerworld.hypnobook.ui.views.kategorie;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import lombok.RequiredArgsConstructor;
import net.steinerworld.hypnobook.domain.Kategorie;
import net.steinerworld.hypnobook.services.KategorieService;
import net.steinerworld.hypnobook.ui.views.MainLayout;

@PermitAll
@PageTitle("Hypno Book - Kategorie")
@Route(value = "kategorie", layout = MainLayout.class)
@RequiredArgsConstructor
public class KategorieView extends VerticalLayout {
   private static final Logger LOGGER = LoggerFactory.getLogger(KategorieView.class);

   private final KategorieService kategorieService;


   @PostConstruct
   public void initialize() {
      setHeightFull();
      Grid<Kategorie> grid = new Grid<>(Kategorie.class, false);
      grid.setHeightFull();
      grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
      grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);

      Editor<Kategorie> editor = grid.getEditor();

      Grid.Column<Kategorie> nameColumn = grid.addColumn(Kategorie::getName).setHeader("Name").setWidth("200px").setFlexGrow(0);
      Grid.Column<Kategorie> infoColumn = grid.addColumn(Kategorie::getBeschreibung).setHeader("Info").setFlexGrow(1);
      Grid.Column<Kategorie> editColumn = grid.addComponentColumn(kat -> {
         Button editButton = new Button("Edit");
         editButton.addClickListener(e -> {
            if (editor.isOpen()) {
               editor.cancel();
            }
            grid.getEditor().editItem(kat);
         });
         return editButton;
      }).setWidth("150px").setFlexGrow(0);

      Binder<Kategorie> binder = new Binder<>(Kategorie.class);
      editor.setBinder(binder);
      editor.setBuffered(true);

      TextField nameField = new TextField();
      nameField.setWidthFull();
      binder.forField(nameField)
            .asRequired("name must not be empty")
            .bind(Kategorie::getName, Kategorie::setName);
      nameColumn.setEditorComponent(nameField);

      TextField infoField = new TextField();
      infoField.setWidthFull();
      binder.forField(infoField)
            .bind(Kategorie::getBeschreibung, Kategorie::setBeschreibung);
      infoColumn.setEditorComponent(infoField);

      Button saveButton = new Button("Save", e -> editor.save());
      Button cancelButton = new Button(VaadinIcon.CLOSE.create(), e -> editor.cancel());
      cancelButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR);
      HorizontalLayout actions = new HorizontalLayout(saveButton, cancelButton);
      actions.setPadding(false);
      editColumn.setEditorComponent(actions);

      editor.addSaveListener(e -> {
         kategorieService.save(e.getItem());
      });

      add(grid);
      grid.setItems(kategorieService.findAll());
   }
}
