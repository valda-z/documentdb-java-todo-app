package com.microsoft.azure.documentdb.sample.controller;

import java.util.List;

import com.microsoft.azure.documentdb.sample.esb.TopicHelper;
import lombok.NonNull;

import com.microsoft.azure.documentdb.sample.dao.TodoDao;
import com.microsoft.azure.documentdb.sample.dao.TodoDaoFactory;
import com.microsoft.azure.documentdb.sample.model.TodoItem;

public class TodoItemController {
    public static TodoItemController getInstance() {
        if (todoItemController == null) {
            todoItemController = new TodoItemController(TodoDaoFactory.getDao());
        }
        return todoItemController;
    }

    private static TodoItemController todoItemController;

    private final TodoDao todoDao;

    TodoItemController(TodoDao todoDao) {
        this.todoDao = todoDao;
    }

    public TodoItem createTodoItem(@NonNull String name,
            @NonNull String category, boolean isComplete) {
        TodoItem todoItem = TodoItem.builder().name(name).category(category)
                .complete(isComplete).build();
        TodoItem itm = todoDao.createTodoItem(todoItem);

        //Create Topic message and send it to Topic.
        try {
            new TopicHelper().sendToDo(itm);
        }catch (Exception e){
            e.printStackTrace();
        }

        return itm;
    }

    public boolean deleteTodoItem(@NonNull String id) {
        return todoDao.deleteTodoItem(id);
    }

    public TodoItem getTodoItemById(@NonNull String id) {
        return todoDao.readTodoItem(id);
    }

    public List<TodoItem> getTodoItems() {
        return todoDao.readTodoItems();
    }

    public TodoItem updateTodoItem(@NonNull String id, boolean isComplete) {
        return todoDao.updateTodoItem(id, isComplete);
    }
}
