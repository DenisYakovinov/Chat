package ru.job4j.chat.service;

import org.springframework.validation.annotation.Validated;
import ru.job4j.chat.exception.ServiceException;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Validated
public interface GenericService<T> {

    T getById(long id);

    List<T> getAll();

    T save(@Valid T model);

    void delete(T model);

    default T setNewNotNullFieldsToModel(T newModel, T currentModel) throws InvocationTargetException,
            IllegalAccessException {
        var methods = currentModel.getClass().getDeclaredMethods();
        var namesToMethods = getNamesToMethods(methods);
        for (var name : namesToMethods.keySet()) {
            if (name.startsWith("get")) {
                var getMethod = namesToMethods.get(name);
                var setMethod = namesToMethods.get(name.replace("get", "set"));
                if (setMethod == null) {
                    throw new ServiceException(
                            "Impossible invoke set method from object : " + currentModel + ", Check set and get pairs.");
                }
                var newValue = getMethod.invoke(newModel);
                if (newValue != null) {
                    setMethod.invoke(currentModel, newValue);
                }
            }
        }
        return currentModel;
    }

    private Map<String, Method> getNamesToMethods(Method[] methods) {
        var namesToMethods = new HashMap<String, Method>();
        for (var method : methods) {
            var name = method.getName();
            if (name.startsWith("get") || name.startsWith("set")) {
                namesToMethods.put(name, method);
            }
        }
        return namesToMethods;
    }
}
