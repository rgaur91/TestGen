package org.testgen.ui.controller;

import org.dizitart.no2.objects.ObjectRepository;

import org.testgen.db.model.SourcedField;
import org.testgen.ui.screens.FieldsScreen;


public class FieldCurdController extends AbstractCurdController<SourcedField, FieldsScreen> {
    @Override
    String getScreenPath() {
        return null;
    }

    @Override
    protected FieldsScreen getScreen() {
        return FieldsScreen.getInstance();
    }

    @Override
    protected boolean validateDelete(ObjectRepository<SourcedField> repository, SourcedField data) {
        return false;
    }


}
