/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.standalone.integration;

import com.taotao.boot.test.junitperf.core.annotation.TtcTest;
import com.taotao.boot.test.junitperf.core.report.impl.HtmlReporter;
import com.taotao.cloud.standalone.TaoTaoCloudStandaloneApplicationTests;
import com.taotao.cloud.standalone.application.command.dept.dto.DeptGetQry;
import com.taotao.cloud.standalone.application.command.dept.dto.clientobject.DeptCO;
import com.taotao.cloud.standalone.application.service.DeptsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * DeptsServiceTest
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
public class DeptsServiceTest extends TaoTaoCloudStandaloneApplicationTests {

    @Autowired
    private DeptsService deptsService;

    @TtcTest(duration = 1000, reporter = HtmlReporter.class)
    //@Test
    public void helloTest1111111() throws InterruptedException {
        System.out.println("Hello Junit5111111111111");

        int i = deptsService.hashCode();

        DeptGetQry qry = new DeptGetQry();
        qry.setId(1L);
        DeptCO deptCO = deptsService.findById(qry);

        Thread.sleep(100);
        Assertions.assertNull(deptCO);
    }

}
