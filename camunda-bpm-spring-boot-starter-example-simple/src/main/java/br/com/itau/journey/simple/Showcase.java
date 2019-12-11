/*
 * Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH
 * under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright
 * ownership. Camunda licenses this file to you under the Apache License,
 * Version 2.0; you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.com.itau.journey.simple;

import br.com.itau.journey.entity.Person;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.spring.boot.starter.event.PostDeployEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

@Slf4j
@Component
public class Showcase {

    public static final String SAMPLE = "ProcessCallActivityError";
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private TaskService taskService;
    private String processInstanceId;

    //@EventListener
    public void notify(final PostDeployEvent unused) {
        startProcessInstance(SAMPLE);
        processUserTasks();
    }

    public void processUserTasks() {
        while (findPendentTasks().parallelStream().map(processUnaryTask()).collect(Collectors.toList()).size() > 0) ;
    }

    UnaryOperator<Task> processUnaryTask() {
        return task -> {
            //do something with task
            return completeTask(task);
        };
    }

    private Task completeTask(Task task) {
        taskService.complete(task.getId());
        log.info("completed task: {}", task);
        return task;
    }

    private List<Task> findPendentTasks() {
        return taskService.createTaskQuery().processInstanceId(processInstanceId).list();
    }

    private Function<List<Task>, Object> processTaskList() {
        return tasks -> processIfHasTasks(tasks);
    }

    private Object processIfHasTasks(List<Task> tasks) {
        tasks.forEach(processTask());
        return tasks.size() > 0 ? Boolean.TRUE : null;
    }

    private Consumer<Task> processTask() {
        return task -> {
            taskService.complete(task.getId());
            log.info("completed task: {}", task);
        };
    }

    public void startProcessInstance(String sample) {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(sample);
        processInstanceId = processInstance.getProcessInstanceId();
        log.info("started instance: {}", processInstanceId);
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    private UnaryOperator<Person> cleanCpf() {
        return entity -> {
            entity.setCpf(documentService.cleanDocument(entity.getCpf()));
            return entity;
        };
    }
}
