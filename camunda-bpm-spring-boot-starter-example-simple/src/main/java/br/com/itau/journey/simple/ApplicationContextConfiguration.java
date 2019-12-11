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

import br.com.itau.journey.domain.ExternalTaskAccessInfo;
import br.com.itau.journey.domain.KafkaExternalTask;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.ExternalTaskService;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.impl.bpmn.behavior.ExternalTaskActivityBehavior;
import org.camunda.bpm.engine.impl.bpmn.parser.AbstractBpmnParseListener;
import org.camunda.bpm.engine.impl.bpmn.parser.BpmnParseListener;
import org.camunda.bpm.engine.impl.bpmn.parser.BpmnParser;
import org.camunda.bpm.engine.impl.cfg.AbstractProcessEnginePlugin;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.cfg.ProcessEnginePlugin;
import org.camunda.bpm.engine.impl.jobexecutor.JobExecutor;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityBehavior;
import org.camunda.bpm.engine.impl.pvm.process.ActivityImpl;
import org.camunda.bpm.engine.impl.pvm.process.ScopeImpl;
import org.camunda.bpm.engine.impl.util.xml.Element;
import org.camunda.bpm.engine.impl.util.xml.Namespace;
import org.camunda.bpm.engine.spring.ProcessEngineFactoryBean;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.camunda.bpm.spring.boot.starter.event.PostDeployEvent;
import org.camunda.bpm.spring.boot.starter.event.PreUndeployEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static br.com.itau.journey.simple.Showcase.SAMPLE;
import static java.util.Optional.ofNullable;

@Configuration
@EnableScheduling
@EnableProcessApplication("mySimpleApplication")
@EnableKafka
@Slf4j
@ComponentScan(
        basePackages = {"br.com.itau.journey"}
)
public class ApplicationContextConfiguration {

    public static final String TOPIC = "topic";
    public static final String EXTENSION_ELEMENTS = "extensionElements";
    public static final String PROPERTIES = "properties";
    public static final String PROPERTY = "property";
    public static final String NAME = "name";
    public static final String KAFKA_TOPIC = "kafkaTopic";
    public static final String VALUE = "value";
    public static final String COMMA = ",";
    boolean processApplicationStopped;
    @Autowired
    private JobExecutor jobExecutor;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private ConfigurableApplicationContext context;
    @Autowired
    private Showcase showcase;
    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private ExternalTaskScheduler externalTaskScheduler;
    @Autowired
    private ProcessEngineFactoryBean processEngineFactoryBean;
    @Autowired
    private ExternalTaskService externalTaskService;
    //hikariDataSource.close()
    @Autowired
    private HikariDataSource hikariDataSource;
    @Value("${org.camunda.bpm.spring.boot.starter.example.simple.SimpleApplication.exitWhenFinished:false}")
    private boolean exitWhenFinished;

    @Bean
    public ProcessEnginePlugin externalTaskEventPlugin() {
        return new KafkaBrokerProcessEngineTestPlugin();
    }

    @EventListener
    public void onPostDeploy(PostDeployEvent event) {
        log.info("postDeploy: {}", event);
    }

    @EventListener
    public void onPreUndeploy(PreUndeployEvent event) {
        log.info("preUndeploy: {}", event);
        processApplicationStopped = true;
    }


    @Scheduled(fixedDelay = 1L)
    public void exitApplicationWhenProcessIsFinished() {
        Assert.isTrue(!((ProcessEngineConfigurationImpl) processEngine.getProcessEngineConfiguration()).isDbMetricsReporterActivate());

        String processInstanceId = showcase.getProcessInstanceId();

        if (processInstanceId == null) {
           // log.info("processInstance not yet started!");
            return;
        }

        if (isProcessInstanceFinished()) {
            log.info("processinstance ended!");

            if (exitWhenFinished) {
                jobExecutor.shutdown();
                //processEngine.close();
                //springProcessApplication.stop();
                SpringApplication.exit(context, () -> 0);
            }
            showcase.startProcessInstance(SAMPLE);
            showcase.processUserTasks();
            return;
        }
        //log.info("processInstance not yet ended!");
    }

    public boolean isProcessInstanceFinished() {
        final HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(showcase.getProcessInstanceId()).singleResult();
        return historicProcessInstance != null && historicProcessInstance.getEndTime() != null;
    }


    public class KafkaBrokerProcessEngineTestPlugin extends AbstractProcessEnginePlugin {

        public final Namespace CAMUNDA_BPMN_EXTENSIONS_NS = new Namespace(BpmnParser.CAMUNDA_BPMN_EXTENSIONS_NS);
        public final String TYPE = "type";
        public final String START_EVENT = "start";

        private List<BpmnParseListener> customPreBPMNParseListeners(final ProcessEngineConfigurationImpl processEngineConfiguration) {
            if (processEngineConfiguration.getCustomPreBPMNParseListeners() == null) {
                processEngineConfiguration.setCustomPreBPMNParseListeners(new ArrayList<BpmnParseListener>());
            }
            return processEngineConfiguration.getCustomPreBPMNParseListeners();
        }

        @Override
        public void preInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
            customPreBPMNParseListeners(processEngineConfiguration)
                    .add(new RegisterExternalTaskBpmnParseListener());
        }

        public class RegisterExternalTaskBpmnParseListener extends AbstractBpmnParseListener {


            @Override
            public void parseServiceTask(Element serviceTaskElement, ScopeImpl scope, ActivityImpl activity) {

                ActivityBehavior activityBehavior = activity.getActivityBehavior();
                if (activityBehavior instanceof ExternalTaskActivityBehavior) {
                    List<String> kafkaTopics = ofNullable(serviceTaskElement.element(EXTENSION_ELEMENTS))
                            .map(getPropertiesElement())
                            .map(getPropertyList())
                            .map(getFilteredTopicList()).orElse(new ArrayList<>());

                    activity.addListener(START_EVENT, getExecutionListener(kafkaTopics, serviceTaskElement.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, TOPIC)));
                }
            }

            private ExecutionListener getExecutionListener(List<String> kafkaTopics, String camundaTopic) {
                return execution -> {
                    String processInstanceId = execution.getProcessInstanceId();
                    String activityInstanceId = execution.getActivityInstanceId();
                    String currentActivityId = execution.getCurrentActivityId();
                    logExternalTaskInfo(kafkaTopics, camundaTopic, processInstanceId, activityInstanceId, currentActivityId);
                    ExternalTaskAccessInfo externalTaskAccessInfo = ExternalTaskAccessInfo.builder()
                            .kafkaTopics(kafkaTopics)
                            .kafkaExternalTask(KafkaExternalTask.builder()
                                    .camundaTopic(camundaTopic)
                                    .activityInstanceId(activityInstanceId)
                                    .currentActivityId(currentActivityId)
                                    .processInstanceId(processInstanceId)
                                    .build())
                            .build();
                    externalTaskScheduler.scheduler(externalTaskAccessInfo);
                };
            }

            private void logExternalTaskInfo(List<String> topics, String topic, String processInstanceId, String activityInstanceId, String currentActivityId) {
                log.info("processInstanceId: {}", processInstanceId);
                log.info("activityInstanceId: {}", activityInstanceId);
                log.info("currentActivityId: {}", currentActivityId);
                log.info("kafkaTopcis: [{}]", getKafkaTopicList(topics));
                log.info("camundaTopic: {}", topic);
            }

            private String getKafkaTopicList(List<String> topics) {
                if (CollectionUtils.isEmpty(topics)) return StringUtils.EMPTY;
                StringBuilder topicsName = new StringBuilder();
                topics.forEach(s -> topicsName.append(s).append(COMMA));
                topicsName.deleteCharAt(topicsName.length() - 1);
                return topicsName.toString();
            }

            private Function<List<Element>, List<String>> getFilteredTopicList() {
                return propertyList ->
                        propertyList
                                .stream()
                                .filter(getTopicNamePredicate())
                                .map(getTopic()).collect(Collectors.toList());
            }

            private Function<Element, String> getTopic() {
                return property -> property.attribute(VALUE);
            }

            private Predicate<Element> getTopicNamePredicate() {
                return property -> StringUtils.startsWith(property.attribute(NAME), KAFKA_TOPIC);
            }

            private Function<Element, List<Element>> getPropertyList() {
                return propertiesElement -> propertiesElement.elements(PROPERTY);
            }

            private Function<Element, Element> getPropertiesElement() {
                return extensionElement -> extensionElement.element(PROPERTIES);
            }
        }
    }
}
