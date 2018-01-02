/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */


package org.apache.skywalking.apm.plugin.spring.concurrent.define;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.InstanceMethodsInterceptPoint;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.ClassInstanceMethodsEnhancePluginDefine;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.ConstructorInterceptPoint;
import org.apache.skywalking.apm.agent.core.plugin.match.ClassMatch;
import org.apache.skywalking.apm.plugin.spring.concurrent.FailureCallbackInterceptor;

import static net.bytebuddy.matcher.ElementMatchers.named;
import static org.apache.skywalking.apm.plugin.spring.concurrent.match.FailedCallbackMatch.failedCallbackMatch;

/**
 * {@link FailureCallbackInstrumentation} enhance the onFailure method that class inherited
 * <code>org.springframework.util.concurrent.FailureCallback</code> by {@link FailureCallbackInterceptor}.
 *
 * @author zhangxin
 */
public class FailureCallbackInstrumentation extends ClassInstanceMethodsEnhancePluginDefine {

    public static final String FAILURE_CALLBACK_INTERCEPTOR = "org.apache.skywalking.apm.plugin.spring.concurrent.FailureCallbackInterceptor";
    public static final String FAILURE_METHOD_NAME = "onFailure";

    @Override
    protected ConstructorInterceptPoint[] getConstructorsInterceptPoints() {
        return new ConstructorInterceptPoint[0];
    }

    @Override
    protected InstanceMethodsInterceptPoint[] getInstanceMethodsInterceptPoints() {
        return new InstanceMethodsInterceptPoint[] {
            new InstanceMethodsInterceptPoint() {
                @Override
                public ElementMatcher<MethodDescription> getMethodsMatcher() {
                    return named(FAILURE_METHOD_NAME);
                }

                @Override
                public String getMethodsInterceptor() {
                    return FAILURE_CALLBACK_INTERCEPTOR;
                }

                @Override
                public boolean isOverrideArgs() {
                    return false;
                }
            }
        };
    }

    @Override
    protected ClassMatch enhanceClass() {
        return failedCallbackMatch();
    }
}
