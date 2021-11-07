package com.tmlabs.springraft.fsm;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StateMachine {

    private String name;
    private String title;
    private String commandProcessorClass;
    private List<State> states;
    private Map<String, State> nameToStateMap = new HashMap<>();


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCommandProcessorClass() {
        return commandProcessorClass;
    }

    public void setCommandProcessorClass(String commandProcessorClass) {
        this.commandProcessorClass = commandProcessorClass;
    }

    public List<State> getStates() {
        return states;
    }

    public void setStates(List<State> states) {
        this.states = states;
        states.forEach(state -> nameToStateMap.put(state.getName(), state));
    }

    public State getState(String name) {
        return nameToStateMap.get(name);
    }

    public Sketch materialize() throws StateManagerException {

        CommandProcessor commandProcessor = instantiateCommandProcessor();
        Sketch sketch = new Sketch(name, title, commandProcessor);
        sketch.init(nameToStateMap);

        return sketch;
    }


    private CommandProcessor instantiateCommandProcessor() throws StateManagerException {

        ClassLoader myClassLoader = ClassLoader.getSystemClassLoader();
        //The below loader was needed to test on local
        //ClassLoader myClassLoader = this.getClass().getClassLoader();

        Class myClass = null;
        try {
            myClass = myClassLoader.loadClass(commandProcessorClass);
        } catch (ClassNotFoundException e) {
            throw new StateManagerException(
                    e,
                    StateManagerException.ERROR_CODE.CLASS_LOAD_FAILED,
                    String.format("ClassName %s", commandProcessorClass)
            );
        }

        try {
            Constructor<CommandProcessor> constructor = myClass.getDeclaredConstructor();
            return constructor.newInstance();

        } catch (NoSuchMethodException|IllegalAccessException|InstantiationException|InvocationTargetException e) {
            throw new StateManagerException(
                    e,
                    StateManagerException.ERROR_CODE.OBJECT_INSTANTIATION_FAILED,
                    String.format("ClassName %s", commandProcessorClass)
            );
        }

    }

}
