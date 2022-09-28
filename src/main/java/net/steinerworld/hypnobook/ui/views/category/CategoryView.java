package net.steinerworld.hypnobook.ui.views.category;

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
import net.steinerworld.hypnobook.domain.Category;
import net.steinerworld.hypnobook.services.CategoryService;
import net.steinerworld.hypnobook.ui.views.MainLayout;

@PermitAll
@PageTitle("Hypno Book - Category")
@Route(value = "category", layout = MainLayout.class)
@RequiredArgsConstructor
public class CategoryView extends VerticalLayout {
   private static final Logger LOGGER = LoggerFactory.getLogger(CategoryView.class);

   private final CategoryService categoryService;

   private static Grid<Category> buildGrid() {
      Grid<Category> grid = new Grid<>(Category.class, false);
      grid.setHeightFull();
      grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
      grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
      return grid;
   }

   private static Button buildGridEditButton(Editor<Category> editor, Category kat) {
      Button editButton = new Button(VaadinIcon.EDIT.create());
      editButton.addClickListener(e -> {
         if (editor.isOpen()) {
            editor.cancel();
         }
         editor.editItem(kat);
      });
      return editButton;
   }

   private static TextField buildColumnEditFieldName(Binder<Category> binder, ValidationMessage nameValidationMessage) {
      TextField field = new TextField();
      field.setWidthFull();
      binder.forField(field)
            .asRequired("name must not be empty")
            .withStatusLabel(nameValidationMessage)
            .bind(Category::getName, Category::setName);
      return field;
   }

   private static TextField buildColumnEditFieldInfo(Binder<Category> binder) {
      TextField field = new TextField();
      field.setWidthFull();
      binder.forField(field)
            .bind(Category::getBeschreibung, Category::setBeschreibung);
      return field;
   }

   private static VerticalLayout buildAddDialogLayout(Binder<Category> binder) {
      TextField nameField = new TextField("Name");
      binder.forField(nameField)
            .asRequired("Name darf nicht leer sein!")
            .bind(Category::getName, Category::setName);
      TextArea infoArea = new TextArea("Info");
      infoArea.setMaxLength(1024);
      infoArea.setValueChangeMode(ValueChangeMode.EAGER);
      infoArea.addValueChangeListener(e -> e.getSource().setHelperText(e.getValue().length() + "/" + 1024));
      binder.forField(infoArea).bind(Category::getBeschreibung, Category::setBeschreibung);

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
      Grid<Category> grid = buildGrid();

      ValidationMessage nameValidationMessage = new ValidationMessage();

      Editor<Category> editor = grid.getEditor();

      Grid.Column<Category> nameColumn = grid.addColumn(Category::getName).setHeader("Name").setWidth("200px").setFlexGrow(0);
      Grid.Column<Category> infoColumn = grid.addColumn(Category::getBeschreibung).setHeader("Info").setFlexGrow(1);
      Grid.Column<Category> editColumn = grid.addComponentColumn(kat -> buildGridEditButton(editor, kat)).setWidth("100px").setFlexGrow(0);

      Binder<Category> binder = new Binder<>(Category.class);
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

      Binder<Category> addKategorieBinder = new Binder<>(Category.class, false);
      Dialog addDialog = buildAddDialog(addKategorieBinder, grid);
      Button addButton = new Button(VaadinIcon.PLUS.create(), e -> {
         addKategorieBinder.setBean(new Category());
         addDialog.open();
      });

      add(grid, addButton, nameValidationMessage, addDialog);
      grid.setItems(categoryService.findAll());
   }

   private void editorSaveHandler(Category item, Grid<Category> grid) {
      LOGGER.info("save event item: {}", item);
      categoryService.save(item);
      grid.setItems(categoryService.findAll());
   }

   private void editorCancelHandler(ValidationMessage nameValidationMessage) {
      nameValidationMessage.setText("");
   }

   private Dialog buildAddDialog(Binder<Category> binder, Grid<Category> grid) {
      Dialog dialog = new Dialog();
      dialog.setHeaderTitle("Neue Category");
      dialog.add(buildAddDialogLayout(binder));

      Button saveAddButton = buildAddDialogSaveButton(dialog, binder, grid);
      Button cancelNewButton = new Button("Abbrechen", e -> dialog.close());
      dialog.getFooter().add(cancelNewButton);
      dialog.getFooter().add(saveAddButton);
      return dialog;
   }

   private Button buildAddDialogSaveButton(Dialog dialog, Binder<Category> addBinder, Grid<Category> grid) {
      Button saveButton = new Button("Hinzufügen", e -> {
         categoryService.save(addBinder.getBean());
         dialog.close();
         grid.setItems(categoryService.findAll());
      });
      saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
      addBinder.addStatusChangeListener(e -> saveButton.setEnabled(addBinder.isValid()));
      return saveButton;
   }

}
