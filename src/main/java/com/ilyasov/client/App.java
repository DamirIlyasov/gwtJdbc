package com.ilyasov.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.ilyasov.shared.PersonResp;

import java.util.List;

public class App implements EntryPoint {

    private final PersonServiceAsync personService = GWT.create(PersonService.class);
    private final TextBox username = new TextBox();
    private final DateBox birthday = new DateBox();
    private Integer id = -1;

    private ListDataProvider<PersonResp> createTable(CellTable<PersonResp> table) {
        TextColumn<PersonResp> nameColumn = new TextColumn<PersonResp>() {
            @Override
            public String getValue(PersonResp person) {
                return person.getName();
            }
        };
        TextColumn<PersonResp> addressColumn = new TextColumn<PersonResp>() {
            @Override
            public String getValue(PersonResp person) {
                return person.getBirthday().toString();
            }
        };
        table.addColumn(nameColumn, "Username");
        table.addColumn(addressColumn, "Birthday");
        ListDataProvider<PersonResp> dataProvider = new ListDataProvider<PersonResp>();
        dataProvider.addDataDisplay(table);
        this.personService.list(new AsyncCallback<List<PersonResp>>() {
            @Override
            public void onFailure(Throwable throwable) {
                GWT.log("error", throwable);
            }

            @Override
            public void onSuccess(List<PersonResp> people) {
                dataProvider.getList().addAll(people);
            }
        });
        return dataProvider;
    }

    public void onModuleLoad() {
        CellTable<PersonResp> table = new CellTable<PersonResp>();
        ListDataProvider<PersonResp> dataProvider = createTable(table);
        DialogBox dialog = editDialog(dataProvider);
        Button add = new Button("Добавить", new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                username.setValue("");
                birthday.setValue(null);
                id = -1;
                dialog.center();
                dialog.show();
            }
        });
        Button edit = new Button("Редактировать", new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                PersonResp personResp = dataProvider.getList().get(table.getKeyboardSelectedRow());
                username.setValue(personResp.getName());
                birthday.setValue(personResp.getBirthday());
                id = personResp.getId();
                dialog.show();
                dialog.center();
            }
        });
        Button delete = new Button("Удалить", new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                final int index = table.getKeyboardSelectedRow();
                PersonResp person = dataProvider.getList().get(index);
                personService.delete(person, new AsyncCallback<Boolean>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        GWT.log("error", throwable);
                    }

                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        dataProvider.getList().remove(index);
                    }
                });
            }
        });
        HorizontalPanel control = new HorizontalPanel();
        control.add(add);
        control.add(edit);
        control.add(delete);
        VerticalPanel panel = new VerticalPanel();
        panel.add(control);
        panel.add(table);
        RootPanel.get().add(panel);
    }

    private DialogBox editDialog(ListDataProvider<PersonResp> dataProvider) {
        final DialogBox dialogBox = new DialogBox();
        dialogBox.setText("Добавить запись");
        dialogBox.setAnimationEnabled(true);
        VerticalPanel dpanel = new VerticalPanel();
        HorizontalPanel usernamePanel = new HorizontalPanel();
        Label label = new Label("Username");
        label.setWidth("100px");
        usernamePanel.add(label);
        usernamePanel.add(username);
        dpanel.add(usernamePanel);

        HorizontalPanel dbirthdayPanel = new HorizontalPanel();
        label = new Label("Birthday");
        label.setWidth("100px");
        dbirthdayPanel.add(label);
        dbirthdayPanel.add(birthday);
        dpanel.add(dbirthdayPanel);
        HorizontalPanel dcontrol = new HorizontalPanel();
        dcontrol.add(new Button("Сохранить", (ClickHandler) event -> {
            if (id == -1) {
                personService.save(new PersonResp(username.getValue(), new java.sql.Date(birthday.getValue().getTime())), new AsyncCallback<PersonResp>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        GWT.log("error", throwable);
                    }

                    @Override
                    public void onSuccess(PersonResp personResp) {
                        dataProvider.getList().add(personResp);
                        dialogBox.hide();
                    }
                });
            } else {
                personService.update(id, username.getValue(), new java.sql.Date(birthday.getValue().getTime()), new AsyncCallback<PersonResp>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        GWT.log("error", throwable);
                    }

                    @Override
                    public void onSuccess(PersonResp personResp) {
                        dataProvider.getList().set(dataProvider.getList().indexOf(personResp), personResp);
                        dialogBox.hide();
                    }
                });
            }
        }));
        dcontrol.add(new Button("Отменить", new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                dialogBox.hide();
            }
        }));
        dpanel.add(dcontrol);


        dialogBox.setWidget(dpanel);
        return dialogBox;
    }
}
