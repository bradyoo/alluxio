/*
 * The Alluxio Open Foundation licenses this work under the Apache License, version 2.0
 * (the "License"). You may not use this work except in compliance with the License, which is
 * available at www.apache.org/licenses/LICENSE-2.0
 *
 * This software is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied, as more fully set forth in the License.
 *
 * See the NOTICE file distributed with this work for information regarding copyright ownership.
 */

package alluxio.table.common.transform.action;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.ServiceLoader;

/**
 * The registry of transform actions.
 */
public class TransformActionRegistry {
  private static final Logger LOG = LoggerFactory.getLogger(TransformActionRegistry.class);

  private static final List<TransformActionFactory> FACTORIES = new ArrayList<>();

  static {
    refresh();
  }

  private TransformActionRegistry() {} // prevent instantiation

  /**
   * Creates a new instance of a {@link TransformAction}.
   *
   * @param definition the raw definition of the action
   * @return a new instance of an action
   */
  public static List<TransformAction> create(Properties definition) {
    final ArrayList<TransformAction> actions = new ArrayList<>();
    for (TransformActionFactory factory : FACTORIES) {
      final TransformAction transformAction = factory.create(definition);
      if (transformAction != null) {
        actions.add(transformAction);
      }
    }
    return actions;
  }

  /**
   * Refreshes the registry by service loading classes.
   */
  private static void refresh() {
    FACTORIES.clear();
    for (TransformActionFactory factory : ServiceLoader
        .load(TransformActionFactory.class, TransformActionFactory.class.getClassLoader())) {
      FACTORIES.add(factory);
    }
    FACTORIES.sort(Comparator.comparingInt((factory) -> factory.getOrder()));
    LOG.info("Registered Transform actions: " + StringUtils.join(FACTORIES, ","));
  }
}
