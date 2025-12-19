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

package com.taotao.cloud.standalone.application.command.dict.dto;

import com.taotao.boot.ddd.model.application.dto.Command;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;

/**
 * DictOptionListQry
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "DictOptionListQry", description = "查询部门下拉框选择项命令请求")
public class DictOptionListQry extends Command {

    @Schema(name = "type", description = "字典类型")
    private String type;
}
