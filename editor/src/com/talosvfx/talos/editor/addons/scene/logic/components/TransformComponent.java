package com.talosvfx.talos.editor.addons.scene.logic.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.talosvfx.talos.editor.addons.scene.logic.GameObject;
import com.talosvfx.talos.editor.widgets.propertyWidgets.FloatPropertyWidget;
import com.talosvfx.talos.editor.widgets.propertyWidgets.PropertyWidget;
import com.talosvfx.talos.editor.widgets.propertyWidgets.Vector2PropertyWidget;
import com.talosvfx.talos.editor.widgets.propertyWidgets.WidgetFactory;

public class TransformComponent implements IComponent {
    public Vector2 position = new Vector2();
    public float rotation;
    public Vector2 scale = new Vector2(1, 1);

    public static Array<GameObject> tmp = new Array<>();
    public static Vector2 vec = new Vector2();

    @Override
    public Array<PropertyWidget> getListOfProperties () {
        Array<PropertyWidget> properties = new Array<>();

        PropertyWidget positionWidget = WidgetFactory.generate(this, "position", "Position");
        PropertyWidget rotationWidget = WidgetFactory.generate(this, "rotation", "Position");
        PropertyWidget scaleWidget = WidgetFactory.generate(this, "scale", "Position");

        properties.add(positionWidget);
        properties.add(rotationWidget);
        properties.add(scaleWidget);

        return properties;
    }

    @Override
    public String getPropertyBoxTitle () {
        return "Transform";
    }

    @Override
    public int getPriority () {
        return 1;
    }

    public static Vector2 localToWorld(GameObject gameObject, Vector2 vector) {
        if(gameObject.hasComponent(TransformComponent.class)) {
            TransformComponent transform = gameObject.getComponent(TransformComponent.class);

            vector.scl(transform.scale);
            vector.rotateDeg(transform.rotation);

            vector.add(transform.position);

            if(gameObject.parent != null) {
                localToWorld(gameObject.parent, vector);
            }
        }

        return vector;
    }

    public static Vector2 worldToLocal(GameObject gameObject, Vector2 vector) {
        if(gameObject.parent == null) return vector;

        tmp.clear();
        tmp = getRootChain(gameObject, tmp);

        for(int i = tmp.size - 1; i >= 0; i--) {
            GameObject item = tmp.get(i);
            if(item.hasComponent(TransformComponent.class)) {
                TransformComponent transform = item.getComponent(TransformComponent.class);


                vector.sub(transform.position);
                vector.rotateDeg(-transform.rotation);
                vector.scl(1f/transform.scale.x, 1f/transform.scale.y);
            }
        }

        return vector;
    }

    private static Array<GameObject> getRootChain(GameObject currObject, Array<GameObject> chain) {
        chain.add(currObject);

        if(currObject.parent != null) {
            return getRootChain(currObject.parent, chain);
        }

        return chain;
    }
}
