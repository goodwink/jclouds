/**
 *
 * Copyright (C) 2009 Global Cloud Specialists, Inc. <info@globalcloudspecialists.com>
 *
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 */
package org.jclouds.aws.s3;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.jclouds.aws.s3.config.LiveS3ConnectionModule;
import org.jclouds.http.config.HttpFutureCommandClientModule;
import org.jclouds.http.config.JavaUrlHttpFutureCommandClientModule;
import org.jclouds.logging.config.LoggingModule;
import org.jclouds.logging.config.NullLoggingModule;
import org.jclouds.logging.jdk.config.JDKLoggingModule;
import org.testng.annotations.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Module;

/**
 * Tests behavior of modules configured in S3ContextFactory
 * 
 * @author Adrian Cole
 */
@Test(groups = "unit", testName = "s3.S3ContextFactoryTest")
public class S3ContextFactoryTest {

   @HttpFutureCommandClientModule
   static class HttpModule extends AbstractModule {

      @Override
      protected void configure() {

      }
   }

   @Test
   public void testAddHttpModuleIfNotPresent() {
      List<Module> modules = new ArrayList<Module>();
      HttpModule module = new HttpModule();
      modules.add(module);
      S3ContextFactory.addHttpModuleIfNeededAndNotPresent(modules);
      assertEquals(modules.size(), 1);
      assertEquals(modules.remove(0), module);
   }

   @Test
   public void testAddLoggingModuleIfNotPresent() {
      List<Module> modules = new ArrayList<Module>();
      LoggingModule module = new NullLoggingModule();
      modules.add(module);
      S3ContextFactory.addLoggingModuleIfNotPresent(modules);
      assertEquals(modules.size(), 1);
      assertEquals(modules.remove(0), module);
   }

   @Test
   public void testAddNone() {
      List<Module> modules = new ArrayList<Module>();
      LoggingModule loggingModule = new NullLoggingModule();
      modules.add(loggingModule);
      HttpModule httpModule = new HttpModule();
      modules.add(httpModule);
      S3ContextFactory.addHttpModuleIfNeededAndNotPresent(modules);
      S3ContextFactory.addLoggingModuleIfNotPresent(modules);
      assertEquals(modules.size(), 2);
      assertEquals(modules.remove(0), loggingModule);
      assertEquals(modules.remove(0), httpModule);
   }

   @Test
   public void testAddBothWhenNotLive() {
      List<Module> modules = new ArrayList<Module>();
      S3ContextFactory.addHttpModuleIfNeededAndNotPresent(modules);
      S3ContextFactory.addLoggingModuleIfNotPresent(modules);
      assertEquals(modules.size(), 1);
      assert modules.remove(0) instanceof JDKLoggingModule;
   }

   @Test
   public void testAddBothWhenLive() {
      List<Module> modules = new ArrayList<Module>();
      modules.add(new LiveS3ConnectionModule());
      S3ContextFactory.addHttpModuleIfNeededAndNotPresent(modules);
      S3ContextFactory.addLoggingModuleIfNotPresent(modules);
      assertEquals(modules.size(), 3);
      assert modules.remove(0) instanceof LiveS3ConnectionModule;
      assert modules.remove(0) instanceof JavaUrlHttpFutureCommandClientModule;
      assert modules.remove(0) instanceof JDKLoggingModule;
   }

   public void testBuilder() {
      String awsAccessKeyId = "awsAccessKeyId";
      String awsSecretAccessKey = "awsSecretAccessKey";
      String httpAddress = "httpAddress";
      int httpMaxRetries = 9875;
      int httpPort = 3827;
      boolean httpSecure = false;
      int poolIoWorkerThreads= 2727;
      int poolMaxConnectionReuse = 3932;
      int poolMaxConnections = 3382;
      int poolMaxSessionFailures = 857;
      int poolRequestInvokerThreads = 8362;
      
      AbstractModule module1 = new AbstractModule() {

         @Override
         protected void configure() {
         }
      };
      AbstractModule module2 = new AbstractModule() {
         
         @Override
         protected void configure() {
         }
      };
      
      S3ContextFactory factory = S3ContextFactory.createContext(awsAccessKeyId, awsSecretAccessKey);
      factory.withHttpAddress(httpAddress);
      factory.withHttpMaxRetries(httpMaxRetries);
      factory.withHttpPort(httpPort);
      factory.withHttpSecure(httpSecure);
      factory.withModule(module1);
      factory.withModules(module2);
      factory.withPoolIoWorkerThreads(poolIoWorkerThreads);
      factory.withPoolMaxConnectionReuse(poolMaxConnectionReuse);
      factory.withPoolMaxConnections(poolMaxConnections);
      factory.withPoolMaxSessionFailures(poolMaxSessionFailures);
      factory.withPoolRequestInvokerThreads(poolRequestInvokerThreads);      
   }
}