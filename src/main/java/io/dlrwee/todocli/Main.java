package io.dlrwee.todocli;

import io.dlrwee.todocli.repository.TaskRepository;
import io.dlrwee.todocli.repository.impl.InMemoryTaskRepository;
import io.dlrwee.todocli.service.TaskService;

import java.util.Scanner;

public final class Main {

    public static void main(String[] args) {
        TaskRepository repository = new InMemoryTaskRepository();
        TaskService service = new TaskService(repository);
        Scanner scanner = new Scanner(System.in);
        App app = new App(service, scanner);

        app.run();
    }
}
