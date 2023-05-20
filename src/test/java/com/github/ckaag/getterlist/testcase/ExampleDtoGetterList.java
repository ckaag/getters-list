package com.github.ckaag.getterlist.testcase;

import java.util.List;
import java.util.ArrayList;
import java.util.AbstractMap;
import java.util.function.Function;

public class ExampleDtoGetterList {

    private ExampleDtoGetterList() {
    }

    public List<AbstractMap.SimpleImmutableEntry<String, Function<com.github.ckaag.getterlist.testcase.ExampleDto, Object>>> getGetters() {
        List<AbstractMap.SimpleImmutableEntry<String, Function<com.github.ckaag.getterlist.testcase.ExampleDto, Object>>> list = new ArrayList<>();
        list.add(new AbstractMap.SimpleImmutableEntry<>("id", (obj) -> obj.getId()));
        list.add(new AbstractMap.SimpleImmutableEntry<>("name", (obj) -> obj.getName()));
        list.add(new AbstractMap.SimpleImmutableEntry<>("active", (obj) -> obj.isActive()));

        return list;
    }

}
