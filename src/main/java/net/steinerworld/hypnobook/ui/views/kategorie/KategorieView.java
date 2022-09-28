package net.steinerworld.hypnobook.ui.views.kategorie;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
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

   private static Grid<Kategorie> buildGrid() {
      Grid<Kategorie> grid = new Grid<>(Kategorie.class, false);
      grid.setHeightFull();
      grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
      grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
      return grid;
   }

   private static Button buildGridEditButton(Editor<Kategorie> editor, Kategorie kat) {
      Button editButton = new Button(VaadinIcon.EDIT.create());
      editButton.addClickListener(e -> {
         if (editor.isOpen()) {
            editor.cancel();
         }
         editor.editItem(kat);
      });
      return editButton;
   }

   private static TextField buildColumnEditFieldName(Binder<Kategorie> binder, ValidationMessage nameValidationMessage) {
      TextField field = new TextField();
      field.setWidthFull();
      binder.forField(field)
            .asRequired("name must not be empty")
            .withStatusLabel(nameValidationMessage)
            .bind(Kategorie::getName, Kategorie::setName);
      return field;
   }

   private static TextField buildColumnEditFieldInfo(Binder<Kategorie> binder) {
      TextField field = new TextField();
      field.setWidthFull();
      binder.forField(field)
            .bind(Kategorie::getBeschreibung, Kategorie::setBeschreibung);
      return field;
   }

   private static VerticalLayout buildAddDialogLayout(Binder<Kategorie> binder) {
      TextField nameField = new TextField("Name");
      binder.forField(nameField)
            .asRequired("Name darf nicht leer sein!")
            .bind(Kategorie::getName, Kategorie::setName);
      TextArea infoArea = new TextArea("Info");
      infoArea.setMaxLength(1024);
      infoArea.setValueChangeMode(ValueChangeMode.EAGER);
      infoArea.addValueChangeListener(e -> e.getSource().setHelperText(e.getValue().length() + "/" + 1024));
      binder.forField(infoArea).bind(Kategorie::getBeschreibung, Kategorie::setBeschreibung);

      VerticalLayout dialogLayout = new VerticalLayout(nameField, infoArea);
      dialogLayout.setPadding(false);
      dialogLayout.setSpacing(false);
      dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
      dialogLayout.getStyle().set("width", "30rem").set("max-width", "100%");
      return dialogLayout;
   }

   @PostConstruct
   public void initialize() {
      setHeightFull();
      Grid<Kategorie> grid = buildGrid();

      ValidationMessage nameValidationMessage = new ValidationMessage();

      Editor<Kategorie> editor = grid.getEditor();

      Grid.Column<Kategorie> nameColumn = grid.addColumn(Kategorie::getName).setHeader("Name").setWidth("200px").setFlexGrow(0);
      Grid.Column<Kategorie> infoColumn = grid.addColumn(Kategorie::getBeschreibung).setHeader("Info").setFlexGrow(1);
      Grid.Column<Kategorie> editColumn = grid.addComponentColumn(kat -> buildGridEditButton(editor, kat)).setWidth("100px").setFlexGrow(0);

      Binder<Kategorie> binder = new Binder<>(Kategorie.class);
      editor.setBinder(binder);
      editor.setBuffered(true);

      nameColumn.setEditorComponent(buildColumnEditFieldName(binder, nameValidationMessage));
      infoColumn.setEditorComponent(buildColumnEditFieldInfo(binder));

      Button saveButton = new Button(VaadinIcon.CHECK_CIRCLE.create(), e -> editor.save());
      Button cancelButton = new Button(VaadinIcon.CLOSE.create(), e -> editor.cancel());
      cancelButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR);
      HorizontalLayout actions = new HorizontalLayout(saveButton, cancelButton);
      actions.setPadding(false);
      editColumn.setEditorComponent(actions);

      editor.addSaveListener(e -> editorSaveHandler(e.getItem(), grid));
      editor.addCancelListener(e -> editorCancelHandler(nameValidationMessage));

      Binder<Kategorie> addKategorieBinder = new Binder<>(Kategorie.class, false);
      Dialog addDialog = buildAddDialog(addKategorieBinder, grid);
      Button addButton = new Button(VaadinIcon.PLUS.create(), e -> {
         addKategorieBinder.setBean(new Kategorie());
         addDialog.open();
      });

      add(grid, addButton, nameValidationMessage, addDialog);
      grid.setItems(kategorieService.findAll());
   }

   private void editorSaveHandler(Kategorie item, Grid<Kategorie> grid) {
      LOGGER.info("save event item: {}", item);
      kategorieService.save(item);
      grid.setItems(kategorieService.findAll());
   }

   private void editorCancelHandler(ValidationMessage nameValidationMessage) {
      nameValidationMessage.setText("");
   }

   private Dialog buildAddDialog(Binder<Kategorie> binder, Grid<Kategorie> grid) {
      Dialog dialog = new Dialog();
      dialog.setHeaderTitle("Neue Kategorie");
      dialog.add(buildAddDialogLayout(binder));

      Button saveAddButton = buildAddDialogSaveButton(dialog, binder, grid);
      Button cancelNewButton = new Button("Abbrechen", e -> dialog.close());
      dialog.getFooter().add(cancelNewButton);
      dialog.getFooter().add(saveAddButton);
      return dialog;
   }

   private Button buildAddDialogSaveButton(Dialog dialog, Binder<Kategorie> addBinder, Grid<Kategorie> grid) {
      Button saveButton = new Button("Hinzufügen", e -> {
         kategorieService.save(addBinder.getBean());
         dialog.close();
         grid.setItems(kategorieService.findAll());
      });
      saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
      addBinder.addStatusChangeListener(e -> saveButton.setEnabled(addBinder.isValid()));
      return saveButton;
   }

}
