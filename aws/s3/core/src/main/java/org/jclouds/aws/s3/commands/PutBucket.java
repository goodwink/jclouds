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
package org.jclouds.aws.s3.commands;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.jclouds.aws.AWSResponseException;
import org.jclouds.aws.s3.commands.options.PutBucketOptions;
import org.jclouds.aws.s3.util.S3Utils;
import org.jclouds.http.HttpHeaders;
import org.jclouds.http.commands.callables.ReturnTrueIf2xx;

import com.google.common.annotations.VisibleForTesting;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;

/**
 * Create and name your own bucket in which to store your objects.
 * <p/>
 * The PUT request operation with a bucket URI creates a new bucket. Depending on your latency and
 * legal requirements, you can specify a location constraint that will affect where your data
 * physically resides. You can currently specify a Europe (EU) location constraint via
 * {@link PutBucketOptions}.
 * 
 * @see PutBucketOptions
 * @see <a
 *      href="http://docs.amazonwebservices.com/AmazonS3/2006-03-01/index.html?RESTBucketPUT.html"
 *      />
 * @author Adrian Cole
 * 
 */
public class PutBucket extends S3FutureCommand<Boolean> {

   @Inject
   public PutBucket(@Named("jclouds.http.address") String amazonHost, ReturnTrueIf2xx callable,
            @Assisted String bucketName, @Assisted PutBucketOptions options) {
      super("PUT", "/", callable, amazonHost, S3Utils.validateBucketName(bucketName));
      getRequest().getHeaders().putAll(options.buildRequestHeaders());
      String payload = options.buildPayload();
      if (payload != null) {
         getRequest().setPayload(payload);
         getRequest().getHeaders().put(HttpHeaders.CONTENT_LENGTH, payload.getBytes().length + "");
      }
   }

   @Override
   public Boolean get() throws InterruptedException, ExecutionException {
      try {
         return super.get();
      } catch (ExecutionException e) {
         return eventualConsistencyAlreadyOwnedIsOk(e);
      }
   }

   @VisibleForTesting
   static Boolean eventualConsistencyAlreadyOwnedIsOk(ExecutionException e) throws ExecutionException {
      if (e.getCause() != null && e.getCause() instanceof AWSResponseException) {
         AWSResponseException responseException = (AWSResponseException) e.getCause();
         if ("BucketAlreadyOwnedByYou".equals(responseException.getError().getCode())) {
            return true;
         }
      }
      throw e;
   }

   @Override
   public Boolean get(long l, TimeUnit timeUnit) throws InterruptedException, ExecutionException,
            TimeoutException {
      try {
         return super.get(l, timeUnit);
      } catch (ExecutionException e) {
         return eventualConsistencyAlreadyOwnedIsOk(e);
      }
   }
}