package io.dlrwee.todocli.repository.impl;

import io.dlrwee.todocli.repository.TaskRepositoryTest;

class InMemoryTaskRepositoryTest extends TaskRepositoryTest<InMemoryTaskRepository> {

    @Override
    protected InMemoryTaskRepository createRepository() {
        return new InMemoryTaskRepository();
    }
}