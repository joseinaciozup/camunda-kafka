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
package org.camunda.bpm.spring.boot.example.simple;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.impl.cfg.TransactionState;
import org.camunda.bpm.engine.impl.context.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SayHelloDelegate implements JavaDelegate {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Override
  public void execute(DelegateExecution execution) throws Exception {
    logger.info("executed sayHelloDelegate: {}", execution);
    //throw new BpmnError("FuiNoTororo2");
    String id = execution.getProcessInstanceId();
    execution.createIncident("executeInternal", "ProcessVariableDoesNotExist", "Exceção genérica");
    /*Context.getCommandContext().getTransactionContext().addTransactionListener(TransactionState.COMMITTED, commandContext -> {
      execution.getProcessEngine().getRuntimeService().suspendProcessInstanceById(id);
    });*/

    //Thread.sleep(5000);
  }

}