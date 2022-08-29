package com.talosvfx.talos.editor.widgets.propertyWidgets;

import com.badlogic.gdx.utils.Array;

public interface IPropertyProvider {
	public Array<PropertyWidget> getListOfProperties ();
	public String getPropertyBoxTitle ();
	int getPriority();
	public Array<PropertyOption> getOptionsList();
	Class<? extends IPropertyProvider> getType();
}
