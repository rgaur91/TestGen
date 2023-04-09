package org.testgen.ui.controller;

import org.dizitart.no2.objects.ObjectRepository;
import org.testgen.db.model.Field;

import org.testgen.ui.screens.FieldsScreen;


public class FieldCurdController extends AbstractCurdController<Field, FieldsScreen> {
    @Override
    String getScreenPath() {
        return null;
    }

    @Override
    protected FieldsScreen getScreen() {
        return FieldsScreen.getInstance();
    }

    @Override
    protected boolean validateDelete(ObjectRepository<Field> repository, Field data) {
        return true;
    }

    public void save(Field field) {
        ObjectRepository<Field> repository = getRepository();
        repository.update(field, true);
    }
}
